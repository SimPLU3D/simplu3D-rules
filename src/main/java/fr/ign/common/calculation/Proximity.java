package fr.ign.common.calculation;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

import java.util.Arrays;
import java.util.List;

public class Proximity {
    private Coordinate nearest;
    private Coordinate nearest2;
    private Polygon containingFace;
    private double distance;

    public Coordinate getNearest() {
        return nearest;
    }

    public Coordinate getNearest2() {
        return nearest2;
    }

    public Polygon getContainingFace() {
        return containingFace;
    }
    public double getDistance() {
        return distance;
    }

    public void nearest(Coordinate dp, Coordinate[] lP1) {
        int nb = lP1.length;
        this.nearest2 = null;
        if (nb == 0) {
            this.nearest = null;
            this.distance = Double.NaN;
            return;
        }
        Coordinate pTemp = lP1[0];
        double d = dp.distance3D(pTemp);
        this.distance = d;
        for (int i = 1; i < nb; i++) {
            Coordinate p2 = lP1[i];
            double dtemp = dp.distance3D(p2);
            if (dtemp < d) {
                d = dtemp;
                pTemp = p2;
            }
        }
        this.nearest = pTemp;
        this.distance = d;
    }

    public void nearestLC(List<Coordinate> lP1, List<Coordinate> lP2) {
        int nb = lP1.size();
        int nb2 = lP2.size();
        if ((nb == 0) || (nb2 == 0)) {
            this.nearest = null;
            this.nearest2 = null;
            this.distance = Double.NaN;
            return;
        }
        this.distance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < nb; i++) {
            Coordinate dp1 = lP1.get(i);
            for (int j = 0; j < nb2; j++) {
                Coordinate dp2 = lP2.get(j);
                double d = dp1.distance3D(dp2);
                if (d < this.distance) {
                    this.distance = d;
                    this.nearest = dp1;
                    this.nearest2 = dp2;
                }
            }
        }
    }

    public void nearest(Coordinate P1, List<Polygon> lF) {
        if (lF == null || lF.isEmpty()) {
            this.nearest = null;
            this.nearest2 = null;
            this.distance = Double.NaN;
            this.containingFace = null;
            return;
        }
        int nb = lF.size();
        Polygon faceTemp = lF.get(0);

        Coordinate pTemp2 = faceTemp.getCoordinates()[0];
        double dTemp = pTemp2.distance3D(P1);
        for (int i = 0; i < nb; i++) {
            Polygon fTemp = lF.get(i);
            Coordinate[] lPTemp = fTemp.getCoordinates();
            for (Coordinate p : lPTemp) {
                double d = p.distance3D(P1);
                if (d < dTemp) {
                    // On garde le point
                    dTemp = d;
                    pTemp2 = p;
                    faceTemp = fTemp;
                }
            }
        }
        this.nearest = P1;
        this.nearest2 = pTemp2;
        this.distance = dTemp;
        this.containingFace = faceTemp;
    }

    public void nearestLP(List<Coordinate> lP1, List<Polygon> lF) {
        int nb = lF.size();
        if ((nb == 0) || (lP1.isEmpty())) {
            this.nearest = null;
            this.nearest2 = null;
            this.distance = Double.NaN;
            this.containingFace = null;
            return;
        }
        Polygon faceTemp = lF.get(0);
        this.nearestLC(lP1, Arrays.asList(faceTemp.getCoordinates()));
        Coordinate pTemp1 = this.nearest;
        Coordinate pTemp2 = this.nearest2;
        double dTemp = this.distance;
        for (int i = 0; i < nb; i++) {
            this.nearestLC(lP1, Arrays.asList(lF.get(i).getCoordinates()));
            if (this.distance < dTemp) {
                pTemp1 = this.nearest;
                pTemp2 = this.nearest2;
                dTemp = this.distance;
                faceTemp = this.containingFace;
            }
        }
        this.nearest = pTemp1;
        this.nearest2 = pTemp2;
        this.distance = dTemp;
        this.containingFace = faceTemp;
    }
}