package fr.ign.cogit.simplu3d.generator.boundary;

import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.simplu3d.generator.IParcelBoundaryAnalyzer;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundarySide;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundaryType;

public class NullParcelBoundaryAnalyzer implements IParcelBoundaryAnalyzer {

	public void analyze(Face f) {
		for (Arc a : f.arcs()) {
			a.setOrientation(SpecificCadastralBoundaryType.UNKNOWN.getValueType());			
			a.setPoids(SpecificCadastralBoundarySide.UNKNOWN.getValueType());
		}
	}

}
