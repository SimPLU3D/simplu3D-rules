package fr.ign.common.topology;

import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Vertex used in the geometric-topologic model.
 * It can access to every related triangles.
 */
public class Vertex {
    Coordinate coordinate;
    public Coordinate getCoordinate() {
        return coordinate;
    }
    /**
     * Contient la liste des triangles passant par le sommet
     */
    ArrayList<Triangle> lTriRel = new ArrayList<>();
    /**
     * @return La liste des triangles liés au sommet
     */
    public List<Triangle> getLTRiRel() {
        return this.lTriRel;
    }

    /**
     * Rajoute un triangle à la liste de triangles adjacents
     */
    public void ajouteTriangle(Triangle tri) {
        this.lTriRel.add(tri);

    }
    /**
     * Création d'un sommet à partir de 3 coordonnées
     *
     */
    public Vertex(double x, double y, double z) {
        this.coordinate = new Coordinate(x, y, z);
    }

    /**
     * Création d'un sommet à partir d'un directposition
     *
     * @param c coordinate
     */
    public Vertex(Coordinate c) {
        this.coordinate = new Coordinate(c.getX(), c.getY(), c.getZ());
    }

    /**
     * Permet de déterminer les triangles communs de 2 sommets On considère que
     * chaque triangle est unique en mémoire
     *
     * @param som un sommet dont on cherche les triangles communs
     * @return les triangles communs aux deux somemts
     */
    public List<Triangle> hasTriangleCommun(Vertex som) {
        List<Triangle> lTriFinal = new ArrayList<>();
        List<Triangle> lTriangle1 = som.getLTRiRel();
        for (Triangle tTemp : this.lTriRel) {
            if (lTriangle1.contains(tTemp)) {
                lTriFinal.add(tTemp);
            }
        }
        return lTriFinal;
    }

    @Override
    public boolean equals(Object o) {
        if(! (o instanceof Vertex)){
            return false;
        }
        return this.coordinate.equals3D(((Vertex)o).coordinate);
    }
}
