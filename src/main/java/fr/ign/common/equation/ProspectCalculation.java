package fr.ign.common.equation;

import fr.ign.common.util.Box3D;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProspectCalculation {
    public static Geometry calculate(Geometry geomModif, Geometry geom, double pente, double hObj) {
        List<Coordinate> dplIni = new ArrayList<>(List.of(geomModif.getCoordinates()));
        ApproximatedPlanEquation eq = new ApproximatedPlanEquation(geomModif.getCoordinates());
        if (eq.getNormale().getZ() < 0) {
            Collections.reverse(dplIni);
        }
        int nbPoints = dplIni.size();
        Box3D b = new Box3D(geomModif);
        double zObj = b.getLLDP().getZ();
        GeometryFactory fact = new GeometryFactory();
        List<Coordinate> dpl =new ArrayList<>();
        for (int i = 0; i < nbPoints; i++) {
            Coordinate dp = dplIni.get(i);
            Point geom2 = fact.createPoint(dp);
            double distance = geom2.distance(geom);
            dpl.add(new Coordinate(dp.getX(), dp.getY(), distance * pente + zObj + hObj));
        }
        return fact.createPolygon(dpl.toArray(new Coordinate[0]));
    }
}
