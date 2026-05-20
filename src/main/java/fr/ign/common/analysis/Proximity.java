package fr.ign.common.analysis;

import org.locationtech.jts.geom.Coordinate;

public class Proximity {
    private Coordinate nearest;
    private double distance;
    public Coordinate getNearest() {
        return nearest;
    }
    public double getDistance() {
        return distance;
    }
    public void nearest(Coordinate dp, Coordinate[] lP1) {
        int nb = lP1.length;
        if (nb == 0) {
            this.nearest = null;
            this.distance = Double.NaN;
            return;
        }
        Coordinate pTemp = lP1[0];
        double d = dp.distance(pTemp);
        this.distance = d;
        for (int i = 1; i < nb; i++) {
            Coordinate p2 = lP1[i];
            double dtemp = dp.distance(p2);
            if (dtemp < d) {
                d = dtemp;
                pTemp = p2;
            }
        }
        this.nearest = pTemp;
        this.distance = d;
    }
}
