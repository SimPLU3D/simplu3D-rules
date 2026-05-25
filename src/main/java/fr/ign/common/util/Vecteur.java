package fr.ign.common.util;

import org.locationtech.jts.geom.Coordinate;

public class Vecteur {
    private final Coordinate coordinate;
    public Coordinate getCoordinate() {
        return coordinate;
    }
    public double getX() {
        return coordinate.getX();
    }

    public double getY() {
        return coordinate.getY();
    }

    public double getZ() {
        return coordinate.getZ();
    }

    public void setX(double v) {
        coordinate.setX(v);
    }

    public void setY(double v) {
        coordinate.setY(v);
    }

    public void setZ(double v) {
        coordinate.setZ(v);
    }

    public Vecteur(Coordinate coordinate1, Coordinate coordinate2) {
        if (!Double.isNaN(coordinate1.getZ()) && !Double.isNaN(coordinate2.getZ())) {
            this.coordinate = new Coordinate(
                    coordinate2.getX() - coordinate1.getX(),
                    coordinate2.getY() - coordinate1.getY(),
                    coordinate2.getZ() - coordinate1.getZ());
        } else {
            this.coordinate = new Coordinate(
                    coordinate2.getX() - coordinate1.getX(),
                    coordinate2.getY() - coordinate1.getY(),
                    Double.NaN);
        }
    }

    public Vecteur(double x, double y){
        coordinate = new Coordinate(x,y);
    }
    public Vecteur(double x, double y, double z) {
        coordinate = new Coordinate(x, y, Double.isNaN(z) ? 0 : z);
    }

    public Vecteur(Coordinate coordinate) {
        this(coordinate.getX(), coordinate.getY(), coordinate.getZ());
    }

    public double norme() {
        if (!Double.isNaN(this.getZ())) {
            return Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2) + Math.pow(this.getZ(), 2));
        }
        return Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2));
    }

    public void normalise() {
        double norm = this.norme();
        if (!Double.isNaN(this.getZ())) {
            this.setX((this.getX() / norm));
            this.setY((this.getY() / norm));
            this.setZ((this.getZ() / norm));
        } else {
            this.setX((this.getX() / norm));
            this.setY((this.getY() / norm));
        }
    }

    public double prodScalaire(Vecteur v1) {
        if (!Double.isNaN(this.getZ()) && !Double.isNaN(v1.getZ())) {
            return (this.getX() * v1.getX() + this.getY() * v1.getY() + this.getZ() * v1.getZ());
        }
        return (this.getX() * v1.getX() + this.getY() * v1.getY());
    }

    public Vecteur prodVectoriel(Vecteur v1) {
        if (!Double.isNaN(this.getZ()) && !Double.isNaN(v1.getZ())) {
            return new Vecteur(
                    this.getY() * v1.getZ() - this.getZ() * v1.getY(),
                    this.getZ() * v1.getX() - this.getX() * v1.getZ(),
                    this.getX() * v1.getY() - this.getY() * v1.getX()
            );
        }
        return new Vecteur(
                0,
                0,
                this.getX() * v1.getY() - this.getY() * v1.getX()
        );
    }

    public Vecteur multConstante(double lambda) {
        if (!Double.isNaN(this.getZ())) {
            return new Vecteur(
                    lambda * this.getX(),
                    lambda * this.getY(),
                    lambda * this.getZ()
            );
        }
        return new Vecteur(
                lambda * this.getX(),
                lambda * this.getY(),
                Double.NaN
        );
    }

    public Vecteur getNormalised() {
        double x, y, z;
        double norm = this.norme();
        x = (this.getX() / norm);
        y = (this.getY() / norm);
        if (!Double.isNaN(this.getZ())) {
            z = this.getZ() / norm;
            return new Vecteur(x, y, z);
        }
        return new Vecteur(x, y, Double.NaN);
    }

    public Coordinate translate(Coordinate p) {
        Coordinate p2 = new Coordinate(p.getX() + this.getX(), p.getY() + this.getY());
        if (Double.isNaN(this.getZ()) || Double.isNaN(p.getZ())) {
            p2.setZ(Double.NaN);
        } else {
            p2.setZ(p.getZ() + this.getZ());
        }
        return p2;
    }

    public Vecteur vectNorme() {
        double norm = this.norme();
        if (!Double.isNaN(this.getZ())) {
            return new Vecteur(new Coordinate(this.getX() / norm, this.getY() / norm, this.getZ() / norm));
        }
        return new Vecteur(new Coordinate(this.getX() / norm, this.getY() / norm, Double.NaN));
    }

    /** Angle entre l'axe des X et le vecteur this projeté sur le plan XY */
    public double getDirection() {
        return Math.atan2(this.getY(), this.getX());
    }

    public String toString() {
        return "Vecteur = " + this.coordinate.toString();
    }
}
