package fr.ign.cogit.simplu3d.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import fr.ign.cogit.simplu3d.generator.boundary.CarteTopoParcelBoundaryBuilder;
import fr.ign.cogit.simplu3d.generator.boundary.NullParcelBoundaryAnalyzer;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundarySide;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.common.topo.Arc;
import fr.ign.common.topo.CarteTopo;
import fr.ign.common.topo.Face;
import org.locationtech.jts.algorithm.InteriorPoint;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.index.SpatialIndex;
import org.locationtech.jts.index.strtree.STRtree;

/**
 * 
 * @author MBorne
 *
 */
public class CadastralBoundaryGenerator {
	private static final Logger logger = Logger.getLogger(CadastralBoundaryGenerator.class.getName());
	/**
	 * Context cadastral parcels
	 */
	private final Collection<CadastralParcel> cadastralParcels ;
	/**
	 * cached carteTopo
	 */
	private CarteTopo carteTopo;
	public CarteTopo getCarteTopo() {
		return this.carteTopo;
	}
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
		logger.fine("createParcelBoundaries");
		buildCarteTopoAndAnalyzeFaces();
		List<ParcelBoundary> result = new ArrayList<>();
		/*
		 * retrieve face for cadastralParcel
		 */
		Collection<Face> allFaces = carteTopo.getPopFaces();
		SpatialIndex index = new STRtree();
		for (Face f : allFaces) {
			index.insert(f.getGeometrie().getEnvelopeInternal(), f);
		}
		Coordinate centroid = InteriorPoint.getInteriorPoint(cadastralParcel.getGeometry());
		Point point = new GeometryFactory().createPoint(centroid);
		@SuppressWarnings("unchecked")
		Collection<Face> candidateFaces = index.query(point.getEnvelopeInternal());
		candidateFaces = candidateFaces.stream().filter(f->f.getGeometrie().contains(point)).toList();
		//allFaces.select(centroid, 0);
		if ( candidateFaces.isEmpty() ){
			logger.severe("Face not found for parcel "+cadastralParcel.getCode());
			logger.severe("No boundaries set for the parcel "+cadastralParcel.getCode());
			return result;
		}
		//TODO blindage (contrôle topologique en amont, cas des polygones mal modélisé (trou ~ contour))
		if ( candidateFaces.size() != 1 ) {
			logger.severe(candidateFaces.size() + " faces found for parcel " + cadastralParcel.getCode());
		}
		Face face = candidateFaces.iterator().next();
		if(CadastralParcelLoader.REAFFECT_GEOM_TOPOLOGICAL_MAP) {
			cadastralParcel.setGeometry(face.getGeometrie());
		}
		for (Arc arc : carteTopo.getEdges(face)) {
			ParcelBoundary boundary = new ParcelBoundary();
			boundary.setCadastralParcel(cadastralParcel);
			CadastralParcel parcelAdjacente = determineAdjacentCadastralParcel(arc, face);
			if(parcelAdjacente != null ) {
				boundary.setAdjacentCadastralParcel(parcelAdjacente);
			}
			//TODO custom arc attribute
			boundary.setParcelBoundaryType(ParcelBoundaryType.getTypeFromInt(arc.getType()));
			boundary.setSide(ParcelBoundarySide.getTypeFromInt(arc.getSide()));
			boundary.setGeometry(arc.getGeometrie());
			result.add(boundary);
		}
		//TODO add boundaries to cadastralParcel
		return result;
	}

	public CadastralParcel determineAdjacentCadastralParcel(Arc a, Face currentFace) {
		//It only concerns arc with two adjacent faces
		if(a.getFaceDroite() == null ||a.getFaceGauche() == null) {
			return null;
		}
		Face adjacentFace = (a.getFaceDroite() == currentFace) ?a.getFaceGauche()  : a.getFaceDroite();
		Coordinate centroid = InteriorPoint.getInteriorPoint(adjacentFace.getGeometrie());
		SpatialIndex index = new STRtree();
		for (CadastralParcel p : cadastralParcels) {
			index.insert(p.getGeometry().getEnvelopeInternal(), p);
		}
		Point point = new GeometryFactory().createPoint(centroid);
		@SuppressWarnings("unchecked")
		Collection<CadastralParcel> candidateFaces = index.query(point.getEnvelopeInternal());
		candidateFaces = candidateFaces.stream().filter(cadastralParcel -> cadastralParcel.getGeometry().contains(point)).toList();
		//cadastralParcels.select(centroid,0);
		if ( candidateFaces.isEmpty() ){
			logger.severe("Face not found for parcel neighbour of "+currentFace);
		}
		//TODO blindage (contrôle topologique en amont, cas des polygones mal modélisé (trou ~ contour))
		if ( candidateFaces.size() != 1 ){
			logger.severe(candidateFaces.size()+" faces found for face "+adjacentFace);
			return null;
		}
		return candidateFaces.iterator().next();
	}

	public void buildCarteTopoAndAnalyzeFaces() {
		if ( carteTopo != null ){
			return;
		}
        Collection<CadastralParcel> parcellesTemp = new ArrayList<>(cadastralParcels);
		carteTopo = CarteTopoParcelBoundaryBuilder.newCarteTopo(parcellesTemp, 0.2);
		for ( Face f : carteTopo.getPopFaces() ) {
			getBoundaryAnalyzer().analyze(carteTopo, f);
		}
	}
}
