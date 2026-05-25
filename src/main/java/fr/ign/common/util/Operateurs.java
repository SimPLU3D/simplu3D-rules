package fr.ign.common.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Operateurs {
    private static final Logger logger = Logger.getLogger(Operateurs.class.getName());
    public static LineString union(List<LineString> linestringList) {
        return union(linestringList, 0d);
    }
    public static LineString union(List<LineString> linestringList, double tolerance) {
        List<LineString> copy = new ArrayList<>(linestringList);
        // Si il n'y a pas de polylignes dans la liste, arrêt de la procédure
        if (linestringList.isEmpty()) {
            return null;
        }
        LineString lineStringCourante = null;
        // Si il n'y a qu'une seule polyligne dans la liste, alors on retourne
        // uniquement cette polyligne
        if (linestringList.size() == 1) {
            lineStringCourante = linestringList.get(0);
            return lineStringCourante;
        }
        // Sinon, il faud comparer toutes les polylignes de la liste (toutes les
        // combinaisons point de départ, point d'arrivée) et les fusionner au
        // fur et à mesure en les orientant.
        // Initialisation de la polyligne de référence.
        lineStringCourante = linestringList.remove(0);
        for (int i = 0; i < linestringList.size(); i++) {
            LineString lineStringSuivante = linestringList.get(i);
            List<Coordinate> lineStringCopie = new ArrayList<>(Arrays.asList(lineStringSuivante.getCoordinates()));
            List<Coordinate> pointsLiaison = new ArrayList<>();
            // Si le point de départ de la polyligne courante = point de départ
            // de la polyligne suivante
            if (lineStringCourante.getCoordinateN(0).equals2D(lineStringSuivante.getCoordinateN(0), tolerance)) {
                pointsLiaison.addAll(Arrays.asList((lineStringCourante.reverse()).getCoordinates()));
                lineStringCopie.remove(lineStringCopie.get(0));
                pointsLiaison.addAll(lineStringCopie);
                lineStringCourante = lineStringCourante.getFactory().createLineString(pointsLiaison.toArray(new Coordinate[0]));
                pointsLiaison.clear();
                linestringList.remove(i);
                i = -1;
            }
            // Si le point d'arrivée de la polyligne courante = point d'arrivée
            // de la polyligne suivante
            else if (lineStringCourante.getEndPoint().getCoordinate().equals2D(lineStringSuivante.getEndPoint().getCoordinate(), tolerance)) {
                pointsLiaison.addAll(Arrays.asList(lineStringCourante.getCoordinates()));
                lineStringCopie.remove(lineStringCopie.get(lineStringCopie.size() - 1));
                List<Coordinate> list = new ArrayList<>(lineStringCopie);
                Collections.reverse(list);
                pointsLiaison.addAll(list);
                lineStringCourante = lineStringCourante.getFactory().createLineString(pointsLiaison.toArray(new Coordinate[0]));
                pointsLiaison.clear();
                linestringList.remove(i);
                i = -1;
            }
            // Si le point d'arrivée de la polyligne courante = point de départ
            // de la polyligne suivante
            else if (lineStringCourante.getEndPoint().getCoordinate().equals2D(lineStringSuivante.getStartPoint().getCoordinate(), tolerance)) {
                pointsLiaison.addAll(Arrays.asList(lineStringCourante.getCoordinates()));
                lineStringCopie.remove(lineStringCopie.get(0));
                pointsLiaison.addAll(lineStringCopie);
                lineStringCourante = lineStringCourante.getFactory().createLineString(pointsLiaison.toArray(new Coordinate[0]));
                pointsLiaison.clear();
                linestringList.remove(i);
                i = -1;
            }
            // Si le point de départ de la polyligne courant = point d'arrivée
            // de la polyligne suivante
            else if (lineStringCourante.getStartPoint().getCoordinate().equals2D(lineStringSuivante.getEndPoint().getCoordinate(), tolerance)) {
                lineStringCopie.remove(lineStringCopie.get(lineStringCopie.size() - 1));
                pointsLiaison.addAll(lineStringCopie);
                pointsLiaison.addAll(Arrays.asList(lineStringCourante.getCoordinates()));
                lineStringCourante = lineStringCourante.getFactory().createLineString(pointsLiaison.toArray(new Coordinate[0]));
                pointsLiaison.clear();
                linestringList.remove(i);
                i = -1;
            }
        }
        if (!linestringList.isEmpty()) {
            logger.warning(
                    "WARNING. All input lines are not used to produce the output merged line (unused lines = " + linestringList.size() + ")");
            logger.warning("init\n");
            copy.forEach(line -> logger.warning(line.toString()));
            logger.warning("unused\n");
            linestringList.forEach(line -> logger.warning(line.toString()));
        }
        return lineStringCourante;
    }
    /**
     * Renvoie le point translaté de P avec le vecteur V; Contrairement au "move" de
     * DirectPosition, on ne deplace pas le point P
     * <p>
     * English : Shift of a point
     */
    public static Coordinate translate(Coordinate P, Vecteur V) {
        if (!Double.isNaN(P.getZ()) && !Double.isNaN(V.getZ())) {
            return new Coordinate(P.getX() + V.getX(), P.getY() + V.getY(), P.getZ() + V.getZ());
        }
        return new Coordinate(P.getX() + V.getX(), P.getY() + V.getY(), Double.NaN);
    }

    /**
     * Coordonnées du point situé sur la ligne à l'abscisse curviligne passée en
     * paramètre. Renvoie Null si l'abscisse est négative ou plus grande que la
     * longueur de la ligne.
     * <p>
     * English: Point located at the curvilinear abscisse.
     */
    public static Coordinate pointEnAbscisseCurviligne(LineString ls, double abscisse) {
        int i;
        double l = 0;
        double d;
        Coordinate pt1, pt2;
        Vecteur v1;
        if (abscisse > ls.getLength() || abscisse < 0) {
            return null;
        }
        pt1 = ls.getCoordinateN(0);
        for (i = 1; i < ls.getCoordinates().length; i++) {
            pt2 = ls.getCoordinateN(i);
            d = pt1.distance(pt2);
            if (d != 0) {
                if (l + d > abscisse) {
                    v1 = new Vecteur(pt1, pt2);
                    v1 = v1.multConstante((abscisse - l) / d);
                    return Operateurs.translate(pt1, v1);
                }
                l = l + d;
                pt1 = pt2;
            }
        }
        return ls.getCoordinateN(ls.getCoordinates().length - 1);
    }

    /**
     * Coordonnées du point situé sur la ligne à l'abscisse curviligne passée en
     * paramètre. Renvoie Null si l'abscisse est négative ou plus grande que la
     * longueur de la ligne.
     * <p>
     * English: Point located at the curvilinear abscisse of a multi linear
     * geometry.
     */
    public static Coordinate pointEnAbscisseCurviligne(
            MultiLineString multiLs,
            double abscisse) {
        if (abscisse < 0 || abscisse > multiLs.getLength()) {
            return null;
        }
        for (int i = 0 ; i < multiLs.getNumGeometries(); i++) {
            LineString ls = (LineString) multiLs.getGeometryN(i);
            double lengthTemp = ls.getLength();
            if (lengthTemp < abscisse) {
                abscisse = abscisse - lengthTemp;
                continue;
            }
            return pointEnAbscisseCurviligne(ls, abscisse);
        }
        return null;
    }

    public static double getAngle(Coordinate pt1, Coordinate pt2) {
        double x = pt2.getX() - pt1.getX();
        double y = pt2.getY() - pt1.getY();
        return Math.atan2(y, x);
    }
}
