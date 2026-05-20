package fr.ign.common.analysis.building;

import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.common.util.Box3D;
import fr.ign.common.analysis.roof.RoofDetection;

import java.util.Objects;

public class StoreyCalculation {
    public static double HAUTEUR_ETAGE = 3;
    public static boolean CONSIDER_ROOF = true;
    public static boolean USE_STOREYS_HEIGH_ATT = false;
    public final static String STOREYS_HEIGH_ATT = "storeys";
    public static int process(AbstractBuilding abstractBuilding) {
        Box3D b = new Box3D(abstractBuilding.getGeometry());
        double zMin = b.getLLDP().getZ();
        double zMax = 0;
        if (CONSIDER_ROOF) {
            zMax = b.getURDP().getZ();
        } else {
            Box3D b2 = new Box3D(Objects.requireNonNull(RoofDetection.detectRoof(abstractBuilding, 0.2)));
            zMax = b2.getLLDP().getZ();
        }
        double hauteurEtage = 0;
        if (USE_STOREYS_HEIGH_ATT) {
            //FIXME
            //hauteurEtage = Double.parseDouble(abstractBuilding.getAttribute(STOREYS_HEIGH_ATT).toString());
        }
        if (hauteurEtage < 0.1) {
            hauteurEtage = HAUTEUR_ETAGE;
        }
        int nbEtage = (int) ((zMax - zMin) / hauteurEtage);
        return Math.max(nbEtage, 1);
    }
}
