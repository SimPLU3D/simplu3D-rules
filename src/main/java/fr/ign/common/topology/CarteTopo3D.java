package fr.ign.common.topology;

import fr.ign.cogit.simplu3d.model.UrbanFeature;
import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.conversion.FromPolygonToTriangle;
import fr.ign.common.equation.PlanEquation;
import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CarteTopo3D {
    private static final Logger logger = Logger.getLogger(CarteTopo3D.class.getName());

    private final List<Triangle> lTrianglesTopo = new ArrayList<>();
    private final List<Edge> lEdgeTopo = new ArrayList<>();
    private final List<Vertex> lVertexTopo = new ArrayList<>();
    private List<List<Triangle>> lGroupes = null;
    public static final double AREA_EPSILON = 0.0001;

    public CarteTopo3D(Geometry geom) {
        List<Polygon> lIOS = FromGeomToSurface.convertGeom(geom);
        if (!lIOS.isEmpty()) {
            List<Triangle> lTri = FromPolygonToTriangle.convertAndTriangle(lIOS);
            if (!lTri.isEmpty()) {
                process(lTri);
            }
        }
    }

    public CarteTopo3D(UrbanFeature feat) {
        this(feat.getGeometry());
    }

    private void process(List<Triangle> lTri) {
        for (Triangle tri : lTri) {
            if (tri.area() < AREA_EPSILON) {
                continue;
            }
            Vertex v1 = new Vertex(tri.getCoordinate(0));
            Vertex v2 = new Vertex(tri.getCoordinate(1));
            Vertex v3 = new Vertex(tri.getCoordinate(2));
            int index1 = lVertexTopo.indexOf(v1);
            if (index1 != -1) {
                v1 = lVertexTopo.get(index1);
            } else {
                lVertexTopo.add(v1);
            }
            int index2 = lVertexTopo.indexOf(v2);
            if (index2 != -1) {
                v2 = this.lVertexTopo.get(index2);
            } else {
                lVertexTopo.add(v2);
            }
            int index3 = lVertexTopo.indexOf(v3);
            if (index3 != -1) {
                v3 = this.lVertexTopo.get(index3);
            } else {
                lVertexTopo.add(v3);
            }
            Triangle triangle = new Triangle(v1, v2, v3);
            if (!triangle.equals(tri)) {
                logger.severe("Different");
            }
            if (this.lTrianglesTopo.contains(triangle)) {
                continue;
            }
            v1.ajouteTriangle(triangle);
            v2.ajouteTriangle(triangle);
            v3.ajouteTriangle(triangle);

            this.lTrianglesTopo.add(triangle);

            List<Edge> lE = new ArrayList<>(triangle.calculEdge());

            for (Edge eTemp : lE) {

                int indexE = lEdgeTopo.indexOf(eTemp);

                if (indexE != -1) {

                    triangle.calculEdge().remove(eTemp);

                    Edge eToAddTemp = lEdgeTopo.get(indexE);

                    triangle.calculEdge().add(eToAddTemp);
                    eToAddTemp.getNeighbourTriangles().add(triangle);

                } else {

                    lEdgeTopo.add(eTemp);

                }

            }

        }

    }

    private List<List<Triangle>> processGroupes() {
        List<List<Triangle>> lLTri = new ArrayList<>();
        // On initialise
        List<Triangle> triIni = new ArrayList<>(this.getlTrianglesTopo());

        // On parcourt chaque triangle
        while (!triIni.isEmpty()) {

            // On enlève le premier de la liste
            Triangle triAct = triIni.remove(0);

            // On entame une nouveau groupes
            List<Triangle> lTriTemp = new ArrayList<>();
            // On ajoute le triangle actuel au groupe
            lTriTemp.add(triAct);

            // On ajoute le nouveau groupe à la liste des groupes
            lLTri.add(lTriTemp);

            // On liste les triangles suivants à traiter
            List<Triangle> lTriToTreat = new ArrayList<>();
            // On met tous les voisines

            List<Triangle> tNeighTemp = triAct.getNeighBourTriangles();

            for (Triangle t : tNeighTemp) {
                // patch ...
                if (!triIni.remove(t)) {
                    logger.severe("Error");
                } else {
                    lTriToTreat.add(t);
                }
            }
            // On traite chaque voisin
            while (!lTriToTreat.isEmpty()) {
                // On recupère le premier triangle à enlever
                Triangle triToTriTreat = lTriToTreat.remove(0);
                // On regarde si il est déjà dans la liste
                int index = lTriTemp.indexOf(triToTriTreat);
                // Il n'est pas dans la liste, on l'ajoute et on rajoute ses voisins
                if (index == -1) {
                    triIni.remove(triToTriTreat);
                    lTriTemp.add(triToTriTreat);
                    List<Triangle> newLTriangle = triToTriTreat.getNeighBourTriangles();
                    for (Triangle triNew : newLTriangle) {
                        if (!lTriTemp.contains(triNew) && !lTriToTreat.contains(triNew)) {
                            lTriToTreat.add(triNew);
                        }
                    }
                }
            }
        }
        return lLTri;
    }

    public List<List<Triangle>> processGroupByPlaneEquation(double epsilon) {
        List<List<Triangle>> lLTri = new ArrayList<>();
        // On initialise
        List<Triangle> triIni = new ArrayList<>(this.getlTrianglesTopo());
        // On parcourt chaque triangle
        while (!triIni.isEmpty()) {
            // On enlève le premier de la liste
            Triangle triAct = triIni.remove(0);
            Vecteur vAct = (new PlanEquation(triAct.getCoordinates())).getNormale();
            vAct.normalise();
            // On entame une nouveau groupes
            List<Triangle> lTriTemp = new ArrayList<>();
            // On ajoute le triangle actuel au groupe
            lTriTemp.add(triAct);
            // On ajoute le nouveau groupe à la liste des groupes
            lLTri.add(lTriTemp);
            // On liste les triangles suivants à traiter
            // On met tous les voisines
            List<Triangle> tNeighTemp = triAct.getNeighBourTriangles();
            // patch ...
            List<Triangle> lTriToTreat = new ArrayList<>(tNeighTemp);
            // On traite chaque voisin
            while (!lTriToTreat.isEmpty()) {
                // On recupère le premier triangle à enlever
                Triangle triToTriTreat = lTriToTreat.remove(0);
                // On regarde si il est déjà dans la liste
                int index = lTriTemp.indexOf(triToTriTreat);
                Vecteur vTemp = (new PlanEquation(triToTriTreat.getCoordinates())).getNormale();
                vTemp.normalise();
                // on vérifie qu'ils sont dans le même plan
                boolean samePlan = Math.abs(vTemp.prodScalaire(vAct)) > 1 - epsilon;
                // Il n'est pas dans la liste, on l'ajoute et on rajoute ses voisins
                if (index == -1 && samePlan) {
                    triIni.remove(triToTriTreat);
                    lTriTemp.add(triToTriTreat);
                    List<Triangle> newLTriangle = triToTriTreat.getNeighBourTriangles();
                    for (Triangle triNew : newLTriangle) {
                        if (!lTriTemp.contains(triNew) && !lTriToTreat.contains(triNew)) {
                            lTriToTreat.add(triNew);
                        }
                    }
                }
            }
        }
        return lLTri;
    }

    public List<Triangle> getlTrianglesTopo() {
        return lTrianglesTopo;
    }

    public List<Edge> getlEdgeTopo() {
        return lEdgeTopo;
    }

    public List<Vertex> getlVertexTopo() {
        return lVertexTopo;
    }

    public List<List<Triangle>> getGroupes() {
        if (lGroupes == null) {
            lGroupes = processGroupes();
        }
        return lGroupes;
    }
}
