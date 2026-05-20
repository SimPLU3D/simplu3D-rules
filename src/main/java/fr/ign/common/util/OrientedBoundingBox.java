package fr.ign.common.util;

import fr.ign.common.analysis.roof.RoofDetection;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import java.util.List;

public class OrientedBoundingBox {
    private Polygon poly = null;
    public OrientedBoundingBox(Geometry geometry) {
        if (geometry != null && geometry.getNumPoints() != 0) {
            this.computeOrientedBoundingBox(geometry);
        }
    }

    private void computeOrientedBoundingBox(Geometry geometry) {
        List<Polygon> lOS =  RoofDetection.detectRoof(geometry, 0.2);//FIXME
        if (lOS == null || lOS.isEmpty()) {
            poly = SmallestSurroundingRectangleComputation.getSSR(geometry);
            return;
        }else {
            poly = SmallestSurroundingRectangleComputation.getSSR(geometry.getFactory().createMultiPolygon(lOS.toArray(new Polygon[0])));
        }
        //Box3D b = new Box3D(geometry);
        //double zMin = b.getLLDP().getZ();
        //double zMax = b.getURDP().getZ();
    }

    public Polygon getPoly() {
        return this.poly;
    }
}
