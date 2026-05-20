package fr.ign.common.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

import java.util.Arrays;

public class Box3D {
    private final Coordinate pMin;
    private final Coordinate pMax;

    public Box3D(Coordinate pMin, Coordinate pMax) {
        this.pMin = pMin;
        this.pMax = pMax;
    }

    public Box3D(Coordinate[] dpl) {
        double xMin = Arrays.stream(dpl).map(Coordinate::getX).min(Double::compareTo).orElse(Double.POSITIVE_INFINITY);
        double yMin = Arrays.stream(dpl).map(Coordinate::getY).min(Double::compareTo).orElse(Double.POSITIVE_INFINITY);
        double zMin = Arrays.stream(dpl).map(Coordinate::getZ).min(Double::compareTo).orElse(Double.POSITIVE_INFINITY);
        double xMax = Arrays.stream(dpl).map(Coordinate::getX).max(Double::compareTo).orElse(Double.NEGATIVE_INFINITY);
        double yMax = Arrays.stream(dpl).map(Coordinate::getY).max(Double::compareTo).orElse(Double.NEGATIVE_INFINITY);
        double zMax = Arrays.stream(dpl).map(Coordinate::getZ).max(Double::compareTo).orElse(Double.NEGATIVE_INFINITY);
        this.pMin = new Coordinate(xMin, yMin, zMin);
        this.pMax = new Coordinate(xMax, yMax, zMax);
    }

    public Box3D(Geometry geom) {
        this(geom.getCoordinates());
    }

    public Box3D(double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
        this.pMin = new Coordinate(xMin, yMin, zMin);
        this.pMax = new Coordinate(xMax, yMax, zMax);
    }

    public Coordinate getCenter() {
        double x = (this.pMin.getX() + this.pMax.getX()) / 2;
        double y = (this.pMin.getY() + this.pMax.getY()) / 2;
        double z = (this.pMin.getZ() + this.pMax.getZ()) / 2;
        return new Coordinate(x, y, z);
    }

    public Coordinate getLLDP() {
        return this.pMin;
    }

    public Coordinate getURDP() {
        return this.pMax;
    }

    public boolean intersect(Box3D b) {
        double xmin1 = pMin.getX();
        double xmin2 = b.pMin.getX();
        double xmax1 = pMax.getX();
        double xmax2 = b.pMax.getX();
        double ymin1 = pMin.getY();
        double ymin2 = b.pMin.getY();
        double ymax1 = pMax.getY();
        double ymax2 = b.pMax.getY();
        double zmin1 = pMin.getZ();
        double zmin2 = b.pMin.getZ();
        double zmax1 = pMax.getZ();
        double zmax2 = b.pMin.getZ();
        boolean bx = ((xmin2 <= xmax1) && (xmin2 >= xmin1)) || ((xmin1 <= xmax2) && (xmin1 >= xmin2));
        boolean by = ((ymin2 <= ymax1) && (ymin2 >= ymin1)) || ((ymin1 <= ymax2) && (ymin1 >= ymin2));
        boolean bz = ((zmin2 <= zmax1) && (zmin2 >= zmin1)) || ((zmin1 <= zmax2) && (zmin1 >= zmin2));
        return bx && by && bz;
    }
    public Box3D intersection(Box3D b) {
        if (!intersect(b)) {
            return null;
        }
        double xmin = Math.max(pMin.getX(), b.pMin.getX());
        double ymin = Math.max(pMin.getY(), b.pMin.getY());
        double zmin = Math.max(pMin.getZ(), b.pMin.getZ());
        double xmax = Math.min(pMax.getX(), b.pMax.getX());
        double ymax = Math.min(pMax.getY(), b.pMax.getY());
        double zmax = Math.min(pMax.getZ(), b.pMax.getZ());
        Coordinate pmin = new Coordinate(xmin, ymin, zmin);
        Coordinate pmax = new Coordinate(xmax, ymax, zmax);
        return new Box3D(pmin, pmax);
    }
}
