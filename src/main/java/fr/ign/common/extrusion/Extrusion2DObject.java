package fr.ign.common.extrusion;

import fr.ign.common.equation.ApproximatedPlanEquation;
import fr.ign.common.util.GeomUtils;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Extrusion2DObject {
    private final static Logger logger = Logger.getLogger(Extrusion2DObject.class.getName());
    public static Geometry convertFromGeometry(Geometry geometry, double zMin, double zMax) {
        if (geometry == null) {
            return null;
        }
        if (geometry instanceof Polygon) {
            return convertFromPolygon((Polygon) geometry, zMin, zMax);
        }
        if (geometry instanceof MultiPolygon) {
            return convertFromMultiPolygon((MultiPolygon) geometry, zMin, zMax);
        }
        if (geometry instanceof LineString) {
            return convertFromLine((LineString) geometry, zMin, zMax);
        }
        if (geometry instanceof MultiLineString) {
            return convertFromMultiLine((MultiLineString) geometry, zMin, zMax);
        }
        if (geometry instanceof Point) {
            return convertFromPoint((Point) geometry, zMin, zMax);
        }
        if (geometry instanceof MultiPoint) {
            return convertFromMultiPoint((MultiPoint) geometry, zMin, zMax);
        }
        logger.warning("Representation.GeomUnk" + geometry.getClass().getName());
        return null;
    }

    private static Geometry convertFromMultiLine(MultiLineString mls, double zMin, double zMax) {
        int nbLignes = mls.getNumGeometries();
        if (nbLignes == 0) {
            return null;
        }
        // 2 cas possible :
        // - soit on récupère une line string
        // - soit on récupère des surfaces
        List<LineString> mCurve = new ArrayList<>();
        List<Polygon> mSurf = new ArrayList<>();
        // On décompose le multiPolygon en liste de polygones
        for (int indligne = 0; indligne < nbLignes; indligne++) {
            // On applique la transformation à chaque polygone
            LineString l = (LineString) mls.getGeometryN(indligne);
            Geometry obj = Extrusion2DObject.convertFromLine(l, zMin, zMax);
            // On utilise la contribution de chaque polygone
            // Pour instancier la géométrie ad hoc
            if (Double.isNaN(zMax) || (zMin >= zMax)) {
                // MultiLineString
                mCurve.add((LineString) obj);
            } else {
                // MultiSurface
                MultiPolygon surf = (MultiPolygon) obj;
                mSurf.addAll(GeomUtils.getPolygons(surf));
            }
        }
        // On renvoie la bonne géométrie
        if (Double.isNaN(zMax) || (zMin >= zMax)) {
            // MultiLineString
            return mls.getFactory().createMultiLineString(mCurve.toArray(new LineString[0]));
        }
        // MultiSurface
        return mls.getFactory().createMultiPolygon(mSurf.toArray(new Polygon[0]));
    }

    public static Geometry convertFromLine(LineString lsIni, double zMin, double zMax) {
        Coordinate[] coordSeq = lsIni.getCoordinates();
        List<Polygon> surf = new ArrayList<>();
        int nbpoints = coordSeq.length;
        // On renvoie une polyligne d'altitude Zmin
        if (Double.isNaN(zMax) || (zMin >= zMax)) {
            List<Coordinate> dpl = new ArrayList<>();
            for (Coordinate c : coordSeq) {
                // On ajoute un z
                if (Double.isNaN(zMin)) {
                    dpl.add(new Coordinate(c.getX(), c.getY(), 0));
                } else {
                    dpl.add(new Coordinate(c.getX(), c.getY(), zMin));
                }
            }
            return lsIni.getFactory().createLineString(dpl.toArray(new Coordinate[0]));
        }
        // Le résultat est une polyligne extrudée car
        // Un attribut de hauteur existe
        // On initialize
        Coordinate c = coordSeq[0];
        Coordinate pPred = new Coordinate(c.getX(), c.getY(), zMin);
        Coordinate pPredZ = new Coordinate(c.getX(), c.getY(), zMax);
        // On parcours chaque point
        Coordinate pAct = null;
        Coordinate pActZ = null;
        // On trace un part un les plans verticaux
        // correspondant aux différents segments de la polyligne
        for (int j = 1; j < nbpoints; j++) {
            List<Coordinate> fTemp = new ArrayList<>();
            c = coordSeq[j];
            pAct = new Coordinate(c.getX(), c.getY(), zMin);
            pActZ = new Coordinate(c.getX(), c.getY(), zMax);
            if (GeomUtils.equals(pAct, pPred, 0.1)) {
                continue;
            }
            // On constuit une face verticale
            fTemp.add(pAct);
            fTemp.add(pPred);
            fTemp.add(pPredZ);
            fTemp.add(pActZ);
            fTemp.add(pAct);
            pPred = pAct;
            pPredZ = pActZ;
            Polygon oS = lsIni.getFactory().createPolygon(fTemp.toArray(new Coordinate[0]));
            // On l'ajoute à la liste des faces verticales
            surf.add(oS);
        }
        return lsIni.getFactory().createMultiPolygon(surf.toArray(new Polygon[0]));
    }

    public static Geometry convertFromPolygon(Polygon pol, double zMin, double zMax) {
        // Recuperation des coordonnees du contour
        ApproximatedPlanEquation eq = new ApproximatedPlanEquation(pol.getCoordinates());
        if (eq.getNormale().getZ() > 0) {
            pol = pol.getFactory().createPolygon(pol.getExteriorRing().reverse());
        }
        // Nombre de contributions
        int contribution = pol.getNumInteriorRing() + 1;
        // Liste des trous a l'interieur d'un polygone
        ArrayList<LinearRing> lHoles = new ArrayList<>();
        // Pour chaque contribution
        // les premieres contributions correspondent aux trous
        // a l'exterieur du polyone
        // Attention boucle inverse pour recuperer les trous en memoire
        // avant de creer les 2 faces sup et inf avec ces trous
        for (int idCont = contribution - 1; idCont >= 0; idCont--) {
            // Recuperation des arretes a etudier
            Coordinate[] dPL = null;
            if (idCont == 0) {
                dPL = pol.getExteriorRing().getCoordinates();
            } else {
                dPL = pol.getInteriorRingN(idCont - 1).getCoordinates();
            }
            int nbpoints = dPL.length;
            // On renvoie une polyligne d'altitude Zmin
            if (Double.isNaN(zMax) || (zMin >= zMax)) {
                List<Coordinate> dpl = new ArrayList<>();
                ApproximatedPlanEquation eq2 = new ApproximatedPlanEquation(dPL);
                boolean isInverse = (eq2.getNormale().getZ() < 0);
                if (isInverse) {
                    for (int i = nbpoints - 1; i >= 0; i--) {
                        // On ajoute un z
                        Coordinate c = dPL[i];
                        if (Double.isNaN(zMin)) {
                            dpl.add(new Coordinate(c.getX(), c.getY(), 0));
                        } else {
                            dpl.add(new Coordinate(c.getX(), c.getY(), zMin));
                        }
                    }
                } else {
                    for (Coordinate c : dPL) {
                        // On ajoute un z
                        if (Double.isNaN(zMin)) {
                            dpl.add(new Coordinate(c.getX(), c.getY(), 0));
                        } else {
                            dpl.add(new Coordinate(c.getX(), c.getY(), zMin));
                        }
                    }
                }
                if (idCont != 0) {
                    lHoles.add(pol.getFactory().createLinearRing(dpl.toArray(new Coordinate[0])));
                } else {
                    List<LinearRing> holes = new ArrayList<>();
                    for (LinearRing lHole : lHoles) {
                        if (!isInverse) {
                            holes.add(lHole.reverse());
                        } else {
                            holes.add(lHole);
                        }
                    }
                    return pol.getFactory().createPolygon(
                            pol.getFactory().createLinearRing(dpl.toArray(new Coordinate[0])),
                            holes.toArray(new LinearRing[0])
                    );
                }
            }
            // Le résultat est un polygone extrudé
            // Un attribut de hauteur existe
            Coordinate dpInit = dPL[0];
            // Ce point servira lors de la fermeture
            Coordinate pPred = new Coordinate(dpInit.getX(), dpInit.getY(), zMin);
            Coordinate pPredZ = new Coordinate(dpInit.getX(), dpInit.getY(), zMax);
            // On parcourt chaque point
            Coordinate pAct = null;
            Coordinate pActZ = null;
            ArrayList<Polygon> pLFacet = new ArrayList<>(nbpoints);
            List<Coordinate> lPFaceSup = new ArrayList<>();
            List<Coordinate> lPFaceInf = new ArrayList<>();
            // On boucle en sens inverse pour des questions d'orientation
            // On 'saute' un point car la géométrie est fermée
            // et cela ferait une face avec 2 points
            for (int j = nbpoints - 2; j >= 0; j--) {
                List<Coordinate> fTemp = new ArrayList<>();
                dpInit = dPL[j];
                pAct = new Coordinate(dpInit.getX(), dpInit.getY(), zMin);
                pActZ = new Coordinate(dpInit.getX(), dpInit.getY(), zMax);
                // Ceci permet d'éviter des murs "grotesques"
                if (GeomUtils.equals(pAct, pPred, 0.0001)) {
                    continue;
                }
                lPFaceInf.add(pAct);
                lPFaceSup.add(pActZ);
                fTemp.add(pAct);
                fTemp.add(pActZ);
                fTemp.add(pPredZ);
                fTemp.add(pPred);
                fTemp.add(pAct);
                pPred = pAct;
                pPredZ = pActZ;
                Polygon oS = pol.getFactory().createPolygon(fTemp.toArray(new Coordinate[0]));
                pLFacet.add(oS);
            }
            if (lPFaceSup.isEmpty()) {
                return null;
            }
            // On ferme
            lPFaceSup.add(lPFaceSup.get(0));
            lPFaceInf.add(lPFaceInf.get(0));
            LinearRing gmRing = pol.getFactory().createLinearRing(lPFaceSup.toArray(new Coordinate[0]));
            LinearRing gmRing2 = pol.getFactory().createLinearRing(lPFaceInf.toArray(new Coordinate[0]));
            // La contribution est finie
            if (idCont == 0) {
                // Il s'agit des polygones supérierieurs et inférieurs percés
                int nbTrous = lHoles.size();
                List<LinearRing> holes1 = new ArrayList<>();
                List<LinearRing> holes2 = new ArrayList<>();
                for (int idTrous = 0; idTrous < nbTrous; idTrous = idTrous + 2) {
                    holes1.add(lHoles.get(idTrous));
                    holes2.add(lHoles.get(idTrous + 1));
                }
                Polygon poly = pol.getFactory().createPolygon(gmRing,  holes1.toArray(new LinearRing[0]));
                Polygon poly2 = pol.getFactory().createPolygon(gmRing2.reverse(), holes2.toArray(new LinearRing[0]));
                // On ajoute les faces sup et inf à la liste des faces formant l'objet
                pLFacet.add(poly);
                // On inverse la face inférieure
                pLFacet.add(poly2);
                // On renvoie l'objet
                return pol.getFactory().createGeometryCollection(pLFacet.toArray(new Geometry[0]));
            }
            // Il s'agit d'un trou
            // On ajoute un trou pour la surface du haut
            // Un pour la surface du bas
            lHoles.add(gmRing);
            lHoles.add(gmRing2);
        }
        return null;
    }
    public static Geometry convertFromMultiPolygon(MultiPolygon mp, double zMin, double zMax) {
        int nbPolygon = mp.getNumGeometries();
        if (nbPolygon == 0) {
            return null;
        }
        // 2 cas possible :
        // - soit on recupere des surfaces si il n'y a pas de hauteur
        // - soit on recupere des solides
        List<GeometryCollection> mSolid = new ArrayList<>();
        List<Polygon> mSurface = new ArrayList<>();
        // On décompose le multiPolygon en liste de polygones
        for (int indpoly = 0; indpoly < nbPolygon; indpoly++) {
            // On applique la transformation a chaque polygone
            Polygon p = (Polygon) mp.getGeometryN(indpoly);
            Geometry obj = Extrusion2DObject.convertFromPolygon(p, zMin, zMax);
            if (obj == null) {
                continue;
            }
            // On utilise la contribution de chaque polygone
            // Pour instancier la geometrie ad hoc
            if (Double.isNaN(zMax) || (zMin >= zMax)) {
                // Polygon 'plat'
                mSurface.add((Polygon) obj);
            } else {
                // Solide
                //System.out.println("\n" + obj.getClass());
                mSolid.add((GeometryCollection) obj);
                //System.out.println(mSolid);
            }
        }
        // On renvoie la bonne geometrie
        if (Double.isNaN(zMax) || (zMin >= zMax)) {
            // Polygon 'plat'
            return mp.getFactory().createMultiPolygon(mSurface.toArray(new Polygon[0]));
        }
        // Solide
        // System.out.println(mSolid);
        return mp.getFactory().createGeometryCollection(mSolid.toArray(new Geometry[0]));
    }

    public static Geometry convertFromPoint(Point p, double zMin, double zMax) {
        Coordinate dp = p.getCoordinate();
        // On regarde si il y a un Zmax
        if (Double.isNaN(zMax) || (zMin >= zMax)) {
            // Pas de ZMax on a juste un point en sorti
            if (Double.isNaN(zMin)) {
                return p.getFactory().createPoint(new Coordinate(dp.getX(), dp.getY(), 0));
            }
            return p.getFactory().createPoint(new Coordinate(dp.getX(), dp.getY(), zMin));
        }
        // Un Zmax, on trace un segment
        Coordinate p1 = new Coordinate(dp.getX(), dp.getY(), zMin);
        Coordinate p2 = new Coordinate(dp.getX(), dp.getY(), zMax);
        return p.getFactory().createLineString(new Coordinate[]{p1, p2});
    }

    private static Geometry convertFromMultiPoint(MultiPoint mp, double zMin, double zMax) {
        // Nombre de polygones présents dans la géométrie
        int nbPoints = mp.getNumGeometries();
        if (nbPoints == 0) {
            return null;
        }
        // 2 cas possible :
        // - soit on récupère des points si il n'y a pas de hauteur
        // - soit on récupère des lignes
        List<Point> mPoints = new ArrayList<>();
        List<Polygon> mCurve = new ArrayList<>();
        // On décompose le multiPoints en liste de points
        for (int indpoint = 0; indpoint < nbPoints; indpoint++) {
            // On applique la transformation à chaque polygone
            Point p = (Point) mp.getGeometryN(indpoint);
            Geometry obj = Extrusion2DObject.convertFromPoint(p, zMin, zMax);
            // On utilise la contribution de chaque polygone
            // Pour instancier la géométrie ad hoc
            if (Double.isNaN(zMax) || (zMin >= zMax)) {
                // Multipoints
                mPoints.add((Point) obj);
            } else {
                // MultiCurve
                mCurve.add((Polygon) obj);
            }
        }
        // On renvoie la bonne géométrie
        if (Double.isNaN(zMax) || (zMin >= zMax)) {
            // Multipoints
            return mp.getFactory().createMultiPoint(mPoints.toArray(new Point[0]));
        }
        // MultiCurve
        return mp.getFactory().createMultiPolygon(mCurve.toArray(new Polygon[0]));
    }
}
