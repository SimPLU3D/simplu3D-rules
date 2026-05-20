package fr.ign.common.analysis.building;

import fr.ign.common.conversion.FromPolygonToTriangle;
import fr.ign.common.equation.PlanEquation;
import fr.ign.common.topo.SimplePolygonizer;
import fr.ign.common.topology.Triangle;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;

public class CutBuilding {
    public static List<Geometry> cut(Triangle t, PlanEquation eq) {
        List<Geometry> lGeom = new ArrayList<>();
        Geometry geom = eq.triangleIntersection(t);
        if (geom != null) {
            lGeom.add(geom);
        }
        return lGeom;
    }
    public static List<Geometry> cut(List<Triangle> trianglesList, PlanEquation eq) {
        List<Geometry> lGeom = new ArrayList<>();
        for (Triangle t : trianglesList) {
            lGeom.addAll(cut(t, eq));
        }
        return lGeom;
    }

    public static List<Polygon> cutAt(List<Polygon> lOS, double z) {
        List<Triangle> lT = FromPolygonToTriangle.convertAndTriangle(lOS);
        return cutFromTriAt(lT, z);
    }

    public static List<Polygon> cutFromTriAt(List<Triangle> lT, double z) {
        //System.out.println("cutFromTriAt " + z);
        //lT.forEach(t -> System.out.println(t.getPolygon()));
        PlanEquation eq = new PlanEquation(0., 0., 1., -z);
        List<Geometry> lG = CutBuilding.cut(lT, eq);
        //System.out.println("CutBuilding.cut ");
        //lG.forEach(System.out::println);
        SimplePolygonizer polygonizer = new SimplePolygonizer(10);
        polygonizer.addGeometries(lG);
        Geometry union = polygonizer.getGeometry().union();
        //System.out.println("polygonizer");
        //polygonizer.getPolygons().forEach(System.out::println);
        Envelope envelope = union.getEnvelopeInternal();
        //System.out.println("envelope = " + envelope);
        envelope.expandBy(100.);
        //System.out.println("expanded envelope = " + union.getFactory().toGeometry(envelope));
        //System.out.println("difference = " + union.getFactory().toGeometry(envelope).difference(union));
        Polygon englobingPolygon = (Polygon) union.getFactory().toGeometry(envelope).difference(union);
        List<Polygon> result = new ArrayList<>();
        for (int index = 0 ; index < englobingPolygon.getNumInteriorRing(); index++) {
            result.add(union.getFactory().createPolygon(englobingPolygon.getInteriorRingN(index)));
        }
        return result;
        /*
        Collection<UrbanFeature> featCollOut = new ArrayList<>();
        for (Geometry geom : lG) {
            if (geom.getArea() == 0) {
                featCollOut.add(new ParcelBoundary(geom));
            }
        }
        if(featCollOut.size() < 3){
            return lPS;
        }
        CarteTopo map = CarteTopoFactory.newCarteTopo("", featCollOut, 0.5, true);
        if(map.getPopArcs().envelope() == null || map.getPopArcs().envelope().isEmpty()){
            return lPS;
        }
        for (Face f : map.getPopFaces()) {
            if (f.isInfinite()) {
                Polygon pol = f.getGeometrie();
                for (IRing r : pol.getInterior()) {
                    IDirectPositionList dpl = r.coord();
                    dpl.inverseOrdre();
                    for (IDirectPosition dp : dpl) {
                        dp.setZ(z);
                    }
                    lPS.add(new GM_Polygon(new GM_LineString(dpl)));
                }
            }
        }
        return lPS;
        */
    }
}
