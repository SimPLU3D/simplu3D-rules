package fr.ign.common.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

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
}
