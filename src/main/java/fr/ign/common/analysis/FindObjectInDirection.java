package fr.ign.common.analysis;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.model.UrbanFeature;
import fr.ign.common.conversion.FromGeomToLineString;
import fr.ign.common.util.MathConstant;
import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.index.SpatialIndex;
import org.locationtech.jts.index.strtree.STRtree;

import java.util.Collection;
import java.util.logging.Logger;

public class FindObjectInDirection {
    private final static double smallVectorSize = 0.05;
    private final static Logger logger = Logger.getLogger(FindObjectInDirection.class.getName());

    public static <T extends UrbanFeature> T find(
            UrbanFeature feature,
            UrbanFeature oppositeFeature,
            SpatialRepository<T> collection,
            double maximalValue) {
        return find(feature.getGeometry(), oppositeFeature.getGeometry(), collection, maximalValue);
    }
    public static <T extends UrbanFeature> T find(
            Geometry linestring,
            Geometry oppositeDirection,
            SpatialRepository<T> collectionToSelect,
            double maximumDistance) {
        if (collectionToSelect.count() == 0) {
            return null;
        }
        LineString ls = generateLineOfSight(linestring, oppositeDirection, maximumDistance);
        if (ls == null) {
            return null;
        }
        double distance = Double.POSITIVE_INFINITY;
        T bestcandidateParcel = null;
        // use spatialIndex
        Collection<T> features = collectionToSelect.findIntersects(ls);
        for (T boundaryTemp : features) {
            double distTemp = boundaryTemp.getGeometry().distance(oppositeDirection);
            if (oppositeDirection.buffer(0.5).contains(boundaryTemp.getGeometry())) {
                continue;
            }
            if (distTemp < distance) {
                distance = distTemp;
                bestcandidateParcel = boundaryTemp;
            }
        }
        return bestcandidateParcel;
    }

    private static LineString generateLineOfSight(
            Geometry geom,
            Geometry oppositeDirection,
            double maximumDistance) {
        Vecteur vLine = new Vecteur(geom.getCoordinates()[0], geom.getCoordinates()[1]);
        // make sure the rest of the code makes sense
        if (Double.isNaN(vLine.getZ())) vLine.setZ(0);
        Vecteur vectOrth = vLine.prodVectoriel(MathConstant.vectZ).getNormalised();
        Vecteur vectOrthNeg = vectOrth.multConstante(-1).getNormalised();
        vectOrth = vectOrth.multConstante(smallVectorSize);
        vectOrthNeg = vectOrthNeg.multConstante(smallVectorSize);
        Coordinate lineCenter = geom.getCentroid().getCoordinate();
        Coordinate dpDep = vectOrth.translate(lineCenter);
        Coordinate dpDepNeg = vectOrthNeg.translate(lineCenter);
        boolean isInPolygonDep = oppositeDirection.contains(geom.getFactory().createPoint(dpDep));
        boolean isInPolygonDepNeg = oppositeDirection.contains(geom.getFactory().createPoint(dpDepNeg));
        if (isInPolygonDep && isInPolygonDepNeg) {
            MultiLineString iOC = geom.getFactory().createMultiLineString(FromGeomToLineString.convert(oppositeDirection).toArray(new LineString[0]));
            double distDep = iOC.distance(geom.getFactory().createPoint(dpDep));
            double distDepNeg = iOC.distance(geom.getFactory().createPoint(dpDepNeg));
            if (distDep < distDepNeg) {
                isInPolygonDepNeg = false;
            } else {
                isInPolygonDep = false;
            }
            logger.warning(FindObjectInDirection.class + " TRANSLATION IS IN PARCEL IN BOTH DIRECTION " + oppositeDirection);
        }
        if ((!isInPolygonDep) && (!isInPolygonDepNeg)) {
            MultiLineString iOC = geom.getFactory().createMultiLineString(FromGeomToLineString.convert(oppositeDirection).toArray(new LineString[0]));
            double distDep = iOC.distance(geom.getFactory().createPoint(dpDep));
            double distDepNeg = iOC.distance(geom.getFactory().createPoint(dpDepNeg));
            if (distDep > distDepNeg) {
                isInPolygonDep = true;
            }
            logger.warning(FindObjectInDirection.class + " TRANSLATION IS IN PARCEL IN NO DIRECTION " + oppositeDirection);
        }
        Vecteur rightVector = null;
        if (isInPolygonDep) {
            vectOrthNeg.normalise();
            rightVector = vectOrthNeg.multConstante(maximumDistance);
        }
        if (!isInPolygonDep) {
            vectOrth.normalise();
            rightVector = vectOrth.multConstante(maximumDistance);
        }
        if (rightVector == null) {
            return null;
        }
        Coordinate dpLine = rightVector.translate(lineCenter);
        return geom.getFactory().createLineString(new Coordinate[]{dpLine, lineCenter});
    }
}
