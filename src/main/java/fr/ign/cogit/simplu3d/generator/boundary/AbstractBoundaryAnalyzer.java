package fr.ign.cogit.simplu3d.generator.boundary;

import fr.ign.cogit.simplu3d.generator.IParcelBoundaryAnalyzer;
import fr.ign.cogit.simplu3d.model.ParcelBoundarySide;
import fr.ign.common.topo.Arc;
import fr.ign.common.topo.CarteTopo;
import fr.ign.common.topo.Face;
import fr.ign.common.util.SmallestSurroundingRectangleComputation;
import org.locationtech.jts.geom.Polygon;

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
		Polygon poly = SmallestSurroundingRectangleComputation.getSSR(f.getGeometrie());
		if(poly == null) {
			poly = (Polygon) f.getGeometrie().getEnvelope();
		}
		double l1 = poly.getCoordinates()[0].distance(poly.getCoordinates()[1]);
		double l2 = poly.getCoordinates()[1].distance(poly.getCoordinates()[2]);
		double largeur = Math.min(l1, l2);
        return Math.min(largeur / 2.5, thresholdIni);
    }

	protected static void determineSide(Arc aTemp, Arc a, Face f) {
		// C'est un arc direct
		if (f.equals(aTemp.getFaceGauche())) {

			if (aTemp.getNoeudFin().getGeom().distance(a.getGeometrie()) < 0.01) {

				aTemp.setSide(ParcelBoundarySide.LEFT.getValueType());
				return;
			}else if(aTemp.getNoeudIni().getGeom().distance(a.getGeometrie()) < 0.01) {

				aTemp.setSide(ParcelBoundarySide.RIGHT.getValueType());
				return;
			}
			
		}

		// C'est un arc direct
		if (f.equals(aTemp.getFaceDroite())) {

			if (aTemp.getNoeudIni().getGeom().distance(a.getGeometrie()) < 0.01) {

				aTemp.setSide(ParcelBoundarySide.LEFT.getValueType());
				return;
			} else if(aTemp.getNoeudFin().getGeom().distance(a.getGeometrie()) < 0.01) {

				aTemp.setSide(ParcelBoundarySide.RIGHT.getValueType());
				return;
			}
		
		}
		aTemp.setSide(ParcelBoundarySide.UNKNOWN.getValueType());
	}

	public abstract void analyze(CarteTopo carteTopo, Face f);
}
