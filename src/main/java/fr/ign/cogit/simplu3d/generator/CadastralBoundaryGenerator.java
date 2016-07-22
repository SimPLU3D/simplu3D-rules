package fr.ign.cogit.simplu3d.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.generator.boundary.CarteTopoParcelBoundaryBuilder;
import fr.ign.cogit.simplu3d.generator.boundary.NullParcelBoundaryAnalyzer;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundarySide;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundaryType;
import fr.ign.cogit.simplu3d.util.PointInPolygon;

/**
 * 
 * @author MBorne
 *
 */
public class CadastralBoundaryGenerator {
	
	private static Logger logger = Logger.getLogger(CadastralBoundaryGenerator.class);
	
	/**
	 * Context cadastral parcels
	 */
	private Collection<CadastralParcel> cadastralParcels ;
	
	/**
	 * cached carteTopo
	 */
	private CarteTopo carteTopo;
	
	/**
	 * 
	 */
	private IParcelBoundaryAnalyzer boundaryAnalyzer;
	
	public CadastralBoundaryGenerator(Collection<CadastralParcel> cadastralParcels){
		this.cadastralParcels = cadastralParcels;
		this.boundaryAnalyzer = new NullParcelBoundaryAnalyzer();
	}
	
	
	public IParcelBoundaryAnalyzer getBoundaryAnalyzer() {
		return boundaryAnalyzer;
	}

	public void setBoundaryAnalyzer(IParcelBoundaryAnalyzer boundaryAnalyzer) {
		this.boundaryAnalyzer = boundaryAnalyzer;
	}


	public Collection<SpecificCadastralBoundary> createParcelBoundaries(CadastralParcel cadastralParcel){
		buildCarteTopoAndAnalyzeFaces();
			
		/*
		 * retrieve face for cadastralParcel
		 */
		IPopulation<Face> allFaces = carteTopo.getPopFaces() ;
		if ( ! allFaces.hasSpatialIndex() ){
			allFaces.initSpatialIndex(Tiling.class, false);
		}
		
		IDirectPosition centroid = PointInPolygon.get((IPolygon) cadastralParcel.getGeom());
		Collection<Face> candidateFaces = allFaces.select(centroid, 0);
		if ( candidateFaces.isEmpty() ){
			throw new RuntimeException("Face not found for parcel "+cadastralParcel.getCode());
		}
		//TODO blindage (contrôle topologique en amont, cas des polygones mal modélisé (trou ~ contour))
		if ( candidateFaces.size() != 1 ){
			logger.error(candidateFaces.size()+" faces found for parcel "+cadastralParcel.getCode());
		}

		Face face = candidateFaces.iterator().next();

		List<SpecificCadastralBoundary> result = new ArrayList<>();
		for (Arc arc : face.arcs()) {
			SpecificCadastralBoundary boundary = new SpecificCadastralBoundary();
			boundary.setCadastralParcel(cadastralParcel);
			//TODO custom arc attribute
			boundary.setType(SpecificCadastralBoundaryType.getTypeFromInt(arc.getOrientation()));
			boundary.setSide(SpecificCadastralBoundarySide.getTypeFromInt((int)arc.getPoids()));
			boundary.setGeom(arc.getGeom());
			result.add(boundary);
		}
		//TODO add boundaries to cadastralParcel
		return result;
	}

	private void buildCarteTopoAndAnalyzeFaces() {
		if ( carteTopo != null ){
			return ;
		}
		carteTopo = CarteTopoParcelBoundaryBuilder.newCarteTopo("parcelle", cadastralParcels, 0.2);
		for ( Face f : carteTopo.getPopFaces() ){
			getBoundaryAnalyzer().analyze(f);
		}
	}
	
}
