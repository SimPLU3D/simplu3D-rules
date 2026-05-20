package fr.ign.common.equation;

import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.Coordinate;

public class ApproximatedPlanEquation extends PlanEquation {
    public ApproximatedPlanEquation(Coordinate[] dpl) {
        super();
        if (dpl == null) {
            return;
        }
        if (dpl.length < 3) {
            // ApproximatedPlanEquation.logger.error(Messages.getString("PLAN.POINTS"));
            return;
        }

        int nbPoints = dpl.length;
        Coordinate dpAct = dpl[0];
        double xini = dpAct.getX();
        double yini = dpAct.getY();
        double zini;
        if (Double.isNaN(dpAct.getZ())) {
            zini = 0;
        } else {
            zini = dpAct.getZ();
        }
        this.coeffa = 0;
        this.coeffb = 0;
        this.coeffc = 0;

        double x0 = 0;
        double y0 = 0;
        double z0 = 0;

        // Il s'agit de calculer les produits vectoriels des différents
        // triangles formes par
        // dpl.get(0), dpAct, dpSuiv pour obtenir le vecteur normal moyen
        for (int i = 1; i < nbPoints; i++) {
            Coordinate dpSuiv = dpl[i];
            double zsuiv;
            if (Double.isNaN(dpSuiv.getZ())) {
                zsuiv = 0;
            } else {
                zsuiv = dpSuiv.getZ();
            }
            double zact;
            if (Double.isNaN(dpAct.getZ())) {
                zact = 0;
            } else {
                zact = dpAct.getZ();
            }
            x0 = x0 + dpSuiv.getX();
            y0 = y0 + dpSuiv.getY();
            z0 = z0 + zsuiv;
            this.coeffa = this.coeffa + ((dpAct.getY() - yini) - (dpSuiv.getY() - yini)) * ((zact - zini) + (zsuiv - zini));
            this.coeffb = this.coeffb + ((zact - zini) - (zsuiv - zini)) * ((dpAct.getX() - xini) + (dpSuiv.getX() - xini));
            this.coeffc = this.coeffc + ((dpAct.getX() - xini) - (dpSuiv.getX() - xini)) * ((dpAct.getY() - yini) + (dpSuiv.getY() - yini));
            dpAct = dpSuiv;
        }
        Vecteur centre = new Vecteur(
                x0 / (nbPoints - 1),
                y0 / (nbPoints - 1), z0 / (nbPoints - 1));
        /*
         * if (Math.abs(coeffa) < 0.01) { coeffa = 0; } if (Math.abs(coeffb) <
         * 0.01) { coeffb = 0; } if (Math.abs(coeffc) < 0.01) { coeffc = 0; }
         */

        this.normaleToPlane = new Vecteur(this.coeffa, this.coeffb, this.coeffc);

        this.normaleToPlane.normalise();

        this.coeffa = this.normaleToPlane.getX();
        this.coeffb = this.normaleToPlane.getY();
        this.coeffc = this.normaleToPlane.getZ();

        this.coeffd = -centre.prodScalaire(this.normaleToPlane);

        // Affichage de l'équation implicite de l'équation
        this.equation = this.coeffa + "x +" + this.coeffb + "y +" + this.coeffc + "z +" + this.coeffd + " = 0";
    }
}
