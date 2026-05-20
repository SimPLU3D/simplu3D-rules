package fr.ign.cogit.simplu3d.generator.boundary;

import fr.ign.cogit.simplu3d.generator.IParcelBoundaryAnalyzer;
import fr.ign.cogit.simplu3d.model.ParcelBoundarySide;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.common.topo.Arc;
import fr.ign.common.topo.CarteTopo;
import fr.ign.common.topo.Face;

public class NullParcelBoundaryAnalyzer implements IParcelBoundaryAnalyzer {

	@Override
	public void analyze(CarteTopo carteTopo, Face f) {
		for (Arc a : carteTopo.getEdges(f)) {
			a.setTYpe(ParcelBoundaryType.UNKNOWN.getValueType());
			a.setSide(ParcelBoundarySide.UNKNOWN.getValueType());
		}
	}
}
