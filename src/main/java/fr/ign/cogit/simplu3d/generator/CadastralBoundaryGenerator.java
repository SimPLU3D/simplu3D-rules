package fr.ign.cogit.simplu3d.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.algo.PointInPolygon;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.generator.boundary.CarteTopoParcelBoundaryBuilder;
import fr.ign.cogit.simplu3d.generator.boundary.NullParcelBoundaryAnalyzer;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundarySide;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;

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


	public Collection<ParcelBoundary> createParcelBoundaries(CadastralParcel cadastralParcel){
		buildCarteTopoAndAnalyzeFaces();
		
		List<ParcelBoundary> result = new ArrayList<>();
			
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
			logger.error("Face not found for parcel "+cadastralParcel.getCode());
			logger.error("No boundaries set for the parcel "+cadastralParcel.getCode());
			return result;
		}
		//TODO blindage (contrôle topologique en amont, cas des polygones mal modélisé (trou ~ contour))
		if ( candidateFaces.size() != 1 ){
			logger.error(candidateFaces.size()+" faces found for parcel "+cadastralParcel.getCode());
		}

		Face face = candidateFaces.iterator().next();

	
		for (Arc arc : face.arcs()) {
			ParcelBoundary boundary = new ParcelBoundary();
			boundary.setCadastralParcel(cadastralParcel);
			//TODO custom arc attribute
			boundary.setType(ParcelBoundaryType.getTypeFromInt(arc.getOrientation()));
			boundary.setSide(ParcelBoundarySide.getTypeFromInt((int)arc.getPoids()));
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
		
		IFeatureCollection<IFeature> parcellesTemp = new FT_FeatureCollection<>();
		parcellesTemp.addAll(cadastralParcels);
		if(! parcellesTemp.hasSpatialIndex()) {
			parcellesTemp.initSpatialIndex(Tiling.class, false);
		}
		carteTopo = CarteTopoParcelBoundaryBuilder.newCarteTopo("parcelle", parcellesTemp, 0.2);
		for ( Face f : carteTopo.getPopFaces() ){
			getBoundaryAnalyzer().analyze(f);
		}
	}
	
}
