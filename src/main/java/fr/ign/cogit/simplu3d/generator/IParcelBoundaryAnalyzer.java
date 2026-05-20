package fr.ign.cogit.simplu3d.generator;

import fr.ign.common.topo.CarteTopo;
import fr.ign.common.topo.Face;

public interface IParcelBoundaryAnalyzer {
	void analyze(CarteTopo carteTopo, Face f);
}
