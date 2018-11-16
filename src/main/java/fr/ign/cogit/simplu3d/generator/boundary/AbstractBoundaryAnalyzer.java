package fr.ign.cogit.simplu3d.generator.boundary;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.util.algo.SmallestSurroundingRectangleComputation;
import fr.ign.cogit.simplu3d.generator.IParcelBoundaryAnalyzer;
import fr.ign.cogit.simplu3d.model.ParcelBoundarySide;

public abstract class AbstractBoundaryAnalyzer implements IParcelBoundaryAnalyzer {

	private static double THRESHOLDINI = 3.0;

	public AbstractBoundaryAnalyzer() {
	
	}

	public double getThresholdIni() {
		return THRESHOLDINI;
	}

	public static void setThresholdIni(double thresholdIni) {
		AbstractBoundaryAnalyzer.THRESHOLDINI = thresholdIni;
	}

	protected static double determineThreshold(Face f, double thresholdIni) {
		IPolygon poly = SmallestSurroundingRectangleComputation.getSSR(f.getGeometrie());
		
		if(poly == null) {
			poly = f.getGeometrie().envelope().getGeom();
		}
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
		if (f.equals(aTemp.getFaceGauche())) {

			if (aTemp.getNoeudFin().getGeom().distance(a.getGeom()) < 0.01) {

				aTemp.setPoids(ParcelBoundarySide.LEFT.getValueType());
				return;
			}else if(aTemp.getNoeudIni().getGeom().distance(a.getGeom()) < 0.01) {

				aTemp.setPoids(ParcelBoundarySide.RIGHT.getValueType());
				return;
			}
			
		}

		
		// C'est un arc direct
		if (f.equals(aTemp.getFaceDroite())) {

			if (aTemp.getNoeudIni().getGeom().distance(a.getGeom()) < 0.01) {

				aTemp.setPoids(ParcelBoundarySide.LEFT.getValueType());
				return;
			} else if(aTemp.getNoeudFin().getGeom().distance(a.getGeom()) < 0.01) {

				aTemp.setPoids(ParcelBoundarySide.RIGHT.getValueType());
				return;
			}
		
		}
		aTemp.setPoids(ParcelBoundarySide.UNKNOWN.getValueType());

	}

}
