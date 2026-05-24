package fr.ign.common.analysis.parcel;

import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.common.geometry.Box3D;
import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.analysis.building.StoreyCalculation;
import fr.ign.common.analysis.building.CutBuilding;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;

public class SHONCalculation {
    // METHODE
    public static double COEF_PLANCHER_PAR_ETAGE = 0.8;
    // COMPLEXE
    public static double HABITABLE_HAUTEUR = 1.8;
    public enum METHOD {
        SIMPLE, FLOOR_CUT
    }
    public static double assess(List<? extends AbstractBuilding> lB, METHOD m) {
        return switch (m) {
            case SIMPLE -> SHONCalculation.assessSimpleAireBati(lB);
            case FLOOR_CUT -> SHONCalculation.assessAireBatieFromCut(lB);
        };
    }

    public static double assessSimpleAireBati(List<? extends AbstractBuilding> lBP) {
        double aireBatie = 0;
        for (AbstractBuilding b : lBP) {
            aireBatie = aireBatie + assessSimpleSHON(b);
        }
        return aireBatie;
    }

    public static double assessAireBatieFromCut(List<? extends AbstractBuilding> lBP) {
        return lBP.stream().map(SHONCalculation::assessCUTSHON).reduce(0.0, Double::sum);
    }
    public static double assessSimpleSHON(AbstractBuilding bati) {
        int nbFloor = StoreyCalculation.process(bati);
        return bati.getGeometry().getArea() * nbFloor * COEF_PLANCHER_PAR_ETAGE;
    }

    public static List<Polygon> DEBUG = new ArrayList<>();

    public static double assessCUTSHON(AbstractBuilding bati) {
        double hauteurEtage = 0;
        if (StoreyCalculation.USE_STOREYS_HEIGH_ATT) {
            hauteurEtage = StoreyCalculation.HAUTEUR_ETAGE;
        }
        if (hauteurEtage < 0.1) {
            hauteurEtage = StoreyCalculation.HAUTEUR_ETAGE;
        }
        List<Polygon> lOS = new ArrayList<>(FromGeomToSurface.convertGeom(bati.getGeometry()));
        Box3D b = new Box3D(bati.getGeometry().getFactory().createMultiPolygon(lOS.toArray(new Polygon[0])));
        double zMin = b.getLLDP().getZ();
        int nbFloor = Math.max(StoreyCalculation.process(bati), 1);
        double zActu = zMin;
        double areaSHON = 0;
        for (int i = 0; i < nbFloor; i++) {
            List<Polygon> lG = CutBuilding.cutAt(lOS, zActu + HABITABLE_HAUTEUR);
            double areaTemp = calculAreaFromCut(lG);
            if (areaTemp == -1) {
                return -1;
            }
            DEBUG.addAll(lG);
            zActu = zActu + hauteurEtage;
            areaSHON = areaSHON + areaTemp;
        }
        return areaSHON * COEF_PLANCHER_PAR_ETAGE;
    }

    /**
     *
     */
    private static double calculAreaFromCut(List<Polygon> lG) {
        if (lG == null) {
            return -1;
        }
        return lG.stream().map(Polygon::getArea).reduce(0.0, Double::sum);
    }
}
