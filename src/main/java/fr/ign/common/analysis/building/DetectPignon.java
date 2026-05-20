package fr.ign.common.analysis.building;

import fr.ign.common.util.GeomUtils;
import fr.ign.common.util.Operateurs;
import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DetectPignon {
    private static final Logger logger = Logger.getLogger(DetectPignon.class.getName());

    public static List<LineString> detectPignon(MultiLineString ext, MultiLineString roofing, double dist, double epsilonAngle) {
        //Il s'agit de la liste des segments proche du faitage et de pente non nulle
        List<LineString> lS = new ArrayList<>();
        boucleAlpha: for (LineString oc : GeomUtils.getLineStrings(ext)) {
            for (LineString fait : GeomUtils.getLineStrings(roofing)) {
                if (oc.distance(fait) < dist) {
                    if (Math.abs(pente(oc)) > epsilonAngle) {
                        lS.add(oc);
                    }
                    continue boucleAlpha;
                }
            }
        }
        for (int e = 0; e < 10; e++) { // c'est moche mais ça marche ....
            // normalement ca devrait être une boucle
            // while
            int nbELem = lS.size();
            for (int i = 0; i < nbELem; i++) {
                LineString l = lS.get(i);
                for (LineString oc : GeomUtils.getLineStrings(ext)) {
                    if (oc.equals(l)) {
                        continue;
                    }
                    if (lS.contains(oc)) {
                        continue;
                    }
                    if (oc.distance(l) == 0) {
                        if (Math.abs(pente(oc)) > epsilonAngle) {
                            lS.add(oc);
                        }
                    } else if (oc.distance(l) < dist) {
                        if (Math.abs(pente(oc)) > epsilonAngle) {
                            Point p1 = l.getFactory().createPoint(oc.getCoordinateN(0));
                            Point p2 = l.getFactory().createPoint(oc.getCoordinateN(1));
                            if (l.distance(p1) > l.distance(p2)) {
                                p2 = p1;
                            }
                            Point p3 = l.getFactory().createPoint(l.getCoordinateN(0));
                            Point p4 = l.getFactory().createPoint(l.getCoordinateN(1));
                            if (oc.distance(p3) > oc.distance(p4)) {
                                p3 = p4;
                            }
                            lS.add(l.getFactory().createLineString(new Coordinate[]{p3.getCoordinate(), p2.getCoordinate()}));
                        }
                    }
                }
            }
        }
        // On va créer des listes de listes
        boolean hasFusionned = true;
        boucleWhile: while (hasFusionned) {
            hasFusionned = false;
            int nbElem = lS.size();
            for (int i = 0; i < nbElem; i++) {
                LineString lsi = lS.get(i);
                for (int j = i + 1; j < nbElem; j++) {
                    LineString lsj = lS.get(j);
                    if (lsi.touches(lsj)) {
                        List<LineString> lSTemp = new ArrayList<>();
                        lSTemp.add(lsi);
                        lSTemp.add(lsj);
                        LineString lsOut = Operateurs.union(lSTemp);
                        lS.add(lsOut);
                        lS.remove(j);
                        lS.remove(i);
                        hasFusionned = true;
                        continue boucleWhile;
                    }
                }
            }
        }
        // Nettoyage
        for (int i = 0; i < lS.size(); i++) {
            LineString ls = lS.get(i);
            if (ls.getLength() < 25 * dist) {
                lS.remove(i);
                i--;
            }
        }
        return lS;
    }

    private static double pente(LineString oc) {
        Vecteur v = new Vecteur(oc.getCoordinates()[0], oc.getCoordinates()[1]);
        return v.getZ() / oc.getLength();
    }
}
