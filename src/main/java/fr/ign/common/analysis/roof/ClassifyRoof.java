package fr.ign.common.analysis.roof;

import fr.ign.cogit.simplu3d.model.RoofSurface;
import fr.ign.common.equation.ApproximatedPlanEquation;
import fr.ign.common.equation.LineEquation;
import fr.ign.common.equation.PlanEquation;
import fr.ign.common.topology.CarteTopo3D;
import fr.ign.common.topology.Edge;
import fr.ign.common.topology.Triangle;
import fr.ign.common.util.SmallestSurroundingRectangleComputation;
import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ClassifyRoof {
    static Logger logger = Logger.getLogger(ClassifyRoof.class.getName());
    /**
     * Les noms associés aux différents types
     */
    public static final String[] NOMS_TYPES = {"Toit plat", "Toits terrasses", "Toit 1 pan", "Toit 2 pans transverses", "Toit 2 pans non-transvers", "Toit 3 pans", "Toit 3 pans autres", "Toit 4 pans", "Autre", "Indécidable"};

    /**
     * Les identifiants rattachés
     */
    public static final int TYPE_TOIT_PLAT = 0;
    public static final int TYPE_TOIT_TERRASSE = 1;
    public static final int TYPE_TOIT_APPENTIS = 2;
    public static final int TYPE_2_PANS_TRANSVERSE = 3;
    public static final int TYPE_2_PANS_NON_TRANSVERSE = 4;
    public static final int TYPE_3_PANS = 5;
    public static final int TYPE_3_PANS_AUTRE = 6;
    public static final int TYPE_4_PANS = 7;
    public static final int TYPE_AUTRE = 8;
    public static final int TYPE_UNDECIDED = 9;

    public static final int NB_CLASSES = 10;
    private final RoofSurface feature;
    List<Edge> lEdgeRoof = new ArrayList<>();
    List<Edge> lEdgeInteriorRoof = new ArrayList<>();
    List<Edge> lEdgeExteriorRoof = new ArrayList<>();
    List<List<Edge>> lEdgeGroup = new ArrayList<>();
    List<List<Triangle>> triangleGroup = null;
    private int type = -1;

    public int getType() {
        return type;
    }

    public ClassifyRoof(RoofSurface t, double epsilonAngle, double epsilonDist) {
        this.feature = t;
        this.classify(epsilonAngle, epsilonDist);
/*
        if (this.getType() == TYPE_AUTRE || this.getType() == TYPE_UNDECIDED) {
            this.classifyComplex(epsilonAngle, epsilonDistRoof);
        }
*/
    }

    /**
     * On classifie un bâtiment
     *
     * @param epsilonAngle
     * @param epsilonDistRoof
     * @return
     */
    private int classify(double epsilonAngle, double epsilonDistRoof) {
        logger.fine("Début de classification du Toit");
        MultiPolygon iMS = RoofDetection.detectRoof(feature, 0.05);
        assert iMS != null;
        if (iMS.getNumGeometries() == 1) {
            ApproximatedPlanEquation aPE = new ApproximatedPlanEquation(iMS.getGeometryN(0).getCoordinates());
            if (aPE.getNormale().getNormalised().getZ() > 0.9) {
                return TYPE_TOIT_PLAT;
            } else {
                return TYPE_TOIT_APPENTIS;
            }
        }
        logger.fine("Extraction du toit : " + iMS.getNumGeometries() + "   faces ");
        logger.fine("Création de la topologie pour le toits");
        CarteTopo3D cT = new CarteTopo3D(iMS);
        List<Triangle> lTri = cT.getlTrianglesTopo();
        return this.classifyLTriangle(lTri, epsilonAngle, epsilonDistRoof);
    }

    private int classifyLTriangle(List<Triangle> lTri, double epsilonAngle, double epsilonDistRoof) {
        logger.fine("Nombre de triangles : " + lTri.size());
        // La sortie
        List<List<Triangle>> lLTriFinal = new ArrayList<>();
        // On prépare les equations de plant
        List<PlanEquation> lPlanEq = new ArrayList<>();
        logger.fine("Initialisation des équations de plan");
        List<Edge> uniqueNeighbour = new ArrayList<>();
        for (Triangle t : lTri) {
            List<Edge> lE = t.calculEdge();
            for (Edge e : lE) {
                if (e.getNeighbourTriangles().size() == 1) {
                    uniqueNeighbour.add(e);
                }
            }
            lPlanEq.add(new PlanEquation(t.getCoordinates()));
        }
        logger.fine("Classification des triangles en fonctions des équations de plan");
        while (!lTri.isEmpty()) {
            logger.fine("************ Triangles restants " + lTri.size() + "************");
            Triangle triAct = lTri.remove(0);
            PlanEquation pEqAct = lPlanEq.remove(0);
            Vecteur vAct = pEqAct.getNormale();
            vAct.normalise();
            List<Triangle> lTriTemp = new ArrayList<>();
            lTriTemp.add(triAct);
            int nbTri = lTri.size();
            for (int i = 0; i < nbTri; i++) {
                Triangle triTemp = lTri.get(i);
                PlanEquation pEqTemp = lPlanEq.get(i);
                Vecteur vTemp = pEqTemp.getNormale();
                vTemp.normalise();
                double prodSca = vTemp.prodScalaire(vAct);
                boolean samePlan = Math.abs(prodSca) > 1 - epsilonAngle;
                if (!samePlan) {
                    continue;
                }
                boolean toAdd = false;
                Coordinate centre = triTemp.getCenter();
                double val = pEqAct.equationValue(centre);
                toAdd = Math.abs(val) < epsilonDistRoof;
                if (!toAdd) {
                    continue;
                }
                lTri.remove(i);
                lPlanEq.remove(i);
                i--;
                nbTri--;
                lTriTemp.add(triTemp);
            }
            logger.fine("************ Groupe crée  " + lTriTemp.size() + "triangles ************");
            lLTriFinal.add(lTriTemp);
        }
        triangleGroup = lLTriFinal;
        logger.fine("************ Création des groupes d'arrêtes ************");
        for (List<Triangle> lTriTemp : triangleGroup) {
            List<Edge> lETemp = new ArrayList<>();
            for (Triangle triTemp : lTriTemp) {
                List<Edge> eT = triTemp.calculEdge();
                for (Edge e : eT) {
                    List<Triangle> lTriN = e.getNeighbourTriangles();
                    int count = 0;
                    for (Triangle tN : lTriN) {
                        if (lTriTemp.contains(tN)) {
                            count++;
                        }
                    }
                    if (count == 1) {
                        this.lEdgeRoof.add(e);
                        lETemp.add(e);
                    }
                }
            }
            lEdgeGroup.add(lETemp);
        }
        logger.fine("************ Groupes d'arrêtes crées ************");
        logger.fine("************ Choix du toit de toit************");
        this.type = this.detectType(epsilonAngle, epsilonDistRoof);
//    // logger.info("Type choisi  : " + NOMS_TYPES[this.type]);
        boucle2:
        for (Edge e2 : this.lEdgeRoof) {
            for (Edge e : uniqueNeighbour) {
                if (areSimilar(e, e2, 0.01)) {
                    this.lEdgeExteriorRoof.add(e2);
                    continue boucle2;
                }
            }
            this.lEdgeInteriorRoof.add(e2);
        }
        return this.type;
    }


    private static List<Edge> getCommonEdge(List<Edge> l1, List<Edge> l2) {
        List<Edge> lE = new ArrayList<>();
        for (Edge e1 : l1) {
            for (Edge e2 : l2) {
                if (e1.equals(e2)) {
                    lE.add(e1);
                }
            }
        }
        return lE;
    }

    private static List<Edge> getCommonEdgeHeavy(List<Edge> l1, List<Edge> l2, double angleEpsilon) {
        List<Edge> lE = new ArrayList<>();
        for (Edge e1 : l1) {
            for (Edge e2 : l2) {
                if (areSimilar(e1, e2, angleEpsilon)) {
                    lE.add(e1);
                }
            }
        }
        return lE;
    }

    private static boolean areSimilar(Edge e1, Edge e2, double angleEpsilon) {
        Vecteur v1 = e1.getVector().getNormalised();
        Vecteur v2 = e2.getVector().getNormalised();
        // System.out.println(Math.abs(v1.prodScalaire(v2)));
        if (Math.abs(v1.prodScalaire(v2)) < 1 - 4 * angleEpsilon) {
            return false;
        }
        LineEquation lE = new LineEquation(e1.getVertIni().getCoordinate(), e1.getVertFin().getCoordinate());
        return lE.isPointOnLine(e2.getVertFin().getCoordinate());
    }

    private int detectType(double epsilonAngle, double epsilonDistRoof) {
        if (this.triangleGroup.size() == 1) {
            logger.fine("1 seul pan de toit");
            // Toit 1 pan
            if (isRoofFlat(this.triangleGroup.get(0), epsilonAngle)) {
                return TYPE_TOIT_PLAT;
            } else {
                return TYPE_TOIT_APPENTIS;
            }
        }
        boolean allRoofFlat = true;
        for (List<Triangle> lT : this.triangleGroup) {
            if (!isRoofFlat(lT, epsilonAngle)) {
                allRoofFlat = false;
            }
        }
        // Toutes les contributions sont plates
        if (allRoofFlat) {
            return TYPE_TOIT_TERRASSE;
        }
        // TOIT à 2 PANS ?
        if (this.triangleGroup.size() == 2) {
            return this.classify2Pans(epsilonAngle);
        }
        if (this.triangleGroup.size() == 3) {
            return this.classify3Pans(epsilonAngle);
        }
        if (this.triangleGroup.size() == 4) {
            return this.classify4Pans(epsilonAngle);
        }
        return TYPE_AUTRE;
    }

    private boolean isRoofFlat(List<Triangle> lT, double epsilonAngle) {
        boolean isFlat = true;
        for (Triangle t : lT) {
            PlanEquation pE = new PlanEquation(t.getCoordinates());
            double z = Math.abs(pE.getNormale().getZ());
            if (z < 1 - epsilonAngle) {
                isFlat = false;
            }
        }
        return isFlat;
    }

    public List<LineString> getInteriorLineStrings() {
        return this.lEdgeInteriorRoof.stream().map(Edge::getLineString).toList();
    }

    public List<LineString> getFaitage(double pente) {
        return this.getInteriorLineStrings().stream().filter(lineString -> {
            double zVar = lineString.getCoordinateN(0).getZ() - lineString.getCoordinateN(1).getZ();
            return (Math.abs(zVar)/lineString.getLength() < pente);
        }).toList();
    }

    public List<LineString> getExteriorLineStrings() {
        return this.lEdgeExteriorRoof.stream().map(Edge::getLineString).toList();
    }
    private int classify2Pans(double angleEpsilon) {
        logger.fine("************ Classification 2 pans de toits ************");
        List<Edge> lECent = getCommonEdge(lEdgeGroup.get(0), lEdgeGroup.get(1));
        if (lECent.isEmpty()) {
            logger.warning("************ Problème topologique pas d'arrêtes communes entre les pans de toits (application égalité lourde)************");
            lECent = getCommonEdgeHeavy(lEdgeGroup.get(0), lEdgeGroup.get(1), angleEpsilon);
            if (lECent.isEmpty()) {
                logger.severe("************ Non décidable :  pas d'arrêtes communes entre les pans de toits  ************");
                return TYPE_UNDECIDED;
            }
        }
        Vecteur orientation = null;
        if (lECent.size() >= 2) {
            int nbCE = lECent.size();
            for (int i = 0; i < nbCE; i++) {
                Edge e1 = lECent.get(i);
                for (int j = i + 1; j < nbCE; j++) {
                    Edge e2 = lECent.get(j);
                    e1.getVector().normalise();
                    e2.getVector().normalise();
                    if (Math.abs(e1.prodScalaire(e2.getVector())) < 1 - angleEpsilon) {
                        logger.severe("************ Non décidable : pas d'orientation principale pour les arrêtes du milieu ************");
                        return TYPE_UNDECIDED;
                    }
                }
            }
        }
        orientation = lECent.get(0).getVector().getNormalised();
        List<Polygon> iMS = new ArrayList<>();
        iMS.addAll(this.triangleGroup.get(0).stream().map(Triangle::getPolygon).toList());
        iMS.addAll(this.triangleGroup.get(1).stream().map(Triangle::getPolygon).toList());
        MultiPolygon mp = new GeometryFactory().createMultiPolygon(iMS.toArray(new Polygon[0]));
        Polygon poly = SmallestSurroundingRectangleComputation.getSSR(mp);
        assert poly != null;
        Coordinate[] dpl = poly.getCoordinates();
        Vecteur v1 = new Vecteur(dpl[0], dpl[1]);
        v1.normalise();
        Vecteur v2 = new Vecteur(dpl[1], dpl[2]);
        v2.normalise();
        if (Math.abs(v1.prodScalaire(orientation)) > 1 - angleEpsilon * 4) {
            return TYPE_2_PANS_TRANSVERSE;
        }
        if (Math.abs(v2.prodScalaire(orientation)) > 1 - angleEpsilon * 4) {
            return TYPE_2_PANS_TRANSVERSE;
        }
        return TYPE_2_PANS_NON_TRANSVERSE;
    }
    private void removeDouble(List<Edge> lECent) {
        for (int i = 0; i < lECent.size(); i++) {
            for (int j = i + 1; j < lECent.size(); j++) {
                if (lECent.get(i).equals(lECent.get(j))) {
                    lECent.remove(i);
                    i--;
                    break;
                }
            }
        }
    }
    private int classify3Pans(double epsilonAngle) {
        logger.fine("************ Classification 3 pans de toits ************");
        List<Edge> lECent = getCommonEdge(lEdgeGroup.get(0), lEdgeGroup.get(1));
        lECent.addAll(getCommonEdge(lEdgeGroup.get(0), lEdgeGroup.get(2)));
        lECent.addAll(getCommonEdge(lEdgeGroup.get(1), lEdgeGroup.get(2)));
        removeDouble(lECent);
        if (lECent.size() == 3) {
            // Toit 3 pans : 76 / 994
            // Toit 3 pans autres : 8 / 994
            // On regarde si les normales formées par les plans principaux forment 2 angles droits
            int nbAngleDroit = getNbAngleDroit();
            if (nbAngleDroit == 2) {
                return TYPE_3_PANS;
            }
            //logger.warning("Entité : " + this.feature.getAttribute("ID_GEO") + " 3 pans - non classé");
            return TYPE_3_PANS_AUTRE;
        }
        return TYPE_UNDECIDED;
    }

    private int getNbAngleDroit() {
        PlanEquation pE1 = new PlanEquation(triangleGroup.get(0).get(0).getCoordinates());
        PlanEquation pE2 = new PlanEquation(triangleGroup.get(1).get(0).getCoordinates());
        PlanEquation pE3 = new PlanEquation(triangleGroup.get(2).get(0).getCoordinates());
        Vecteur v1 = pE1.getNormale();
        Vecteur v2 = pE2.getNormale();
        Vecteur v3 = pE3.getNormale();
        v1.setZ(0);
        v2.setZ(0);
        v3.setZ(0);
        v1.normalise();
        v2.normalise();
        v3.normalise();
        int nbAngleDroit = 0;
        double p1 = Math.abs(v1.prodScalaire(v2));
        double p2 = Math.abs(v1.prodScalaire(v3));
        double p3 = Math.abs(v3.prodScalaire(v2));
        if (p1 < 0.2) {
            nbAngleDroit++;
        }
        if (p2 < 0.2) {
            nbAngleDroit++;
        }
        if (p3 < 0.2) {
            nbAngleDroit++;
        }
        return nbAngleDroit;
    }

    private int classify4Pans(double epsilonAngle) {
        return TYPE_4_PANS;
    }
}
