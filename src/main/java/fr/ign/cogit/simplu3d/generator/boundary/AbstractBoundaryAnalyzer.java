package fr.ign.cogit.simplu3d.generator.boundary;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.util.algo.SmallestSurroundingRectangleComputation;
import fr.ign.cogit.simplu3d.generator.IParcelBoundaryAnalyzer;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundarySide;

public abstract class AbstractBoundaryAnalyzer implements IParcelBoundaryAnalyzer {

	private double thresholdIni;
	
	public AbstractBoundaryAnalyzer(){
		this.thresholdIni = 3.0;
	}
	
	public double getThresholdIni() {
		return thresholdIni;
	}

	public void setThresholdIni(double thresholdIni) {
		this.thresholdIni = thresholdIni;
	}
	
	
	protected static double determineThreshold(Face f, double thresholdIni) {
		IPolygon poly = SmallestSurroundingRectangleComputation.getSSR(f.getGeometrie());
		double l1 = poly.coord().get(0).distance2D(poly.coord().get(1));
		double l2 = poly.coord().get(1).distance2D(poly.coord().get(2));

		double largeur = Math.min(l1, l2);

		// System.out.println(largeur);

		if (largeur / 2.5 < thresholdIni) {

			// System.out.println("Modification de la largeur de dépassementj'y
			// passe");

			return largeur / 2.5;
		}

		return thresholdIni;
	}
	
	
	
	protected static void determineSide(Arc aTemp, Arc a, Face f) {

		// C'est un arc direct
		if (f.getArcsDirects().contains(aTemp)) {

			if (aTemp.getNoeudFin().equals(a.getNoeudIni()) || aTemp.getNoeudFin().equals(a.getNoeudFin())) {

				aTemp.setPoids(SpecificCadastralBoundarySide.LEFT.getValueType());

			} else {

				aTemp.setPoids(SpecificCadastralBoundarySide.RIGHT.getValueType());
			}

			return;
		}

		if (f.getArcsIndirects().contains(aTemp)) {

			if (aTemp.getNoeudFin().equals(a.getNoeudIni()) || aTemp.getNoeudFin().equals(a.getNoeudFin())) {

				aTemp.setPoids(SpecificCadastralBoundarySide.RIGHT.getValueType());

			} else {

				aTemp.setPoids(SpecificCadastralBoundarySide.LEFT.getValueType());
			}

		}

	}

	
}
