package fr.ign.cogit.simplu3d.io.export;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import fr.ign.cogit.simplu3d.io.feature.FeatureType;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.*;
import fr.ign.common.util.GeomUtils;
import fr.ign.common.io.ShapefileWriter;
import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

/**
 * 
 * 
 * Class to export instances from the simPLU3D model
 * 
 * @author mbrasebin
 * FIXME ADD TEST
 */
public class ExportInstance {
	private static final Logger logger = Logger.getLogger(ExportInstance.class.getName());

	// The borders are shifted from this value into the interio of the parcel they
	// belong to
	public static double VALUE_SHIFT_BORDER = 1.5;

	/**
	 * Export features from environnement of SimPLU3D mode
	 * 
	 * @param env       environnement to export the shapefile are stored
	 * @param folderOut folder where the instances will be written
	 */
	public static void export(Environnement env, String folderOut) {
		// Creating the output folder
		boolean mkdirs = (new File(folderOut)).mkdirs();
		logger.info("Number of zones ni zoning : " + env.getUrbaZones().count());
		Collection<ParcelBoundary> bordures_translated = new ArrayList<>();
		Collection<ParcelBoundary> bordures = new ArrayList<>();
		int count = 0;
        Collection<ParcelBoundary> lOppositeBoundary = new ArrayList<>();
		logger.info("Number of parcels : " + env.getCadastralParcels().count());
		for (BasicPropertyUnit bPU : env.getBpU().findAll()) {
			for (CadastralParcel sp : bPU.getCadastralParcels()) {
				count = count + sp.getBoundaries().size();
				Coordinate centroidParcel = sp.getGeometry().getCentroid().getCoordinate();
				sp.setAttribute("BounNum", sp.getBoundaries().size());
				sp.setAttribute("BuildNum", bPU.getBuildings().size());
				for (ParcelBoundary b : sp.getBoundaries()) {
					bordures.add(b);
					if (b.getOppositeBoundary() != null) {
						Collection<Coordinate> dpl = new ArrayList<>();
						dpl.add(b.getGeometry().getCentroid().getCoordinate());
						dpl.add(b.getOppositeBoundary().getGeometry().getCentroid().getCoordinate());
						LineString ls = b.getGeometry().getFactory().createLineString(dpl.toArray(new Coordinate[0]));
						ParcelBoundary feat = new ParcelBoundary(ls);
						feat.setAttribute("length", ls.getLength());
						lOppositeBoundary.add(feat);
					}
					b.setAttribute("Type", b.getParcelBoundaryType().getValueType());
					b.setAttribute("IDPar", sp.getCode());
					b.setAttribute("Side", b.getSide().getValueType());
					if (b.getFeatAdj() != null) {
						if (b.getFeatAdj() instanceof CadastralParcel) {
							b.setAttribute("Adj", ((CadastralParcel) b.getFeatAdj()).getCode());
						} else if (b.getFeatAdj() instanceof Road) {
							b.setAttribute("Adj", ((Road) b.getFeatAdj()).getRoadName());
						}
					} else {
						b.setAttribute("Adj", "0");
					}
					if (b.getGeometry() == null || b.getGeometry().isEmpty()) {
						continue;
					}
					Coordinate centroidGeom = b.getGeometry().getCoordinates()[0];
					Vecteur v = new Vecteur(centroidParcel, centroidGeom);
					Vecteur v2 = new Vecteur(
							b.getGeometry().getCoordinates()[0],
							b.getGeometry().getCoordinates()[b.getGeometry().getCoordinates().length - 1]);
					v2.setZ(0);
					v2.normalise();
					Vecteur vOut = v2.prodVectoriel(new Vecteur(0, 0, 1));
					Geometry geom = b.getGeometry().copy();
					if (v.prodScalaire(vOut) < 0) {
						vOut = vOut.multConstante(-1);
					}
					Geometry geom2 = GeomUtils.translate(geom, VALUE_SHIFT_BORDER * vOut.getX(), VALUE_SHIFT_BORDER * vOut.getY(),
							0);
                    if (!geom2.intersects(sp.getGeometry())) {
						geom2 = GeomUtils.translate(geom, -VALUE_SHIFT_BORDER * vOut.getX(), -VALUE_SHIFT_BORDER * vOut.getY(), 0);
					}
					ParcelBoundary feat = new ParcelBoundary(geom2);
					feat.setAttribute("Type", b.getParcelBoundaryType().getValueType());
					feat.setAttribute("Side", b.getSide().getValueType());
					bordures_translated.add(feat);
				}
			}
		}
		logger.info("Number of borders" + count);
		// Export des parcelles
		FeatureType oppositeBoundaryType = new FeatureType("Boundary");
		oppositeBoundaryType.setGeometryType("LineString");
		oppositeBoundaryType.addAttribute("length", "Double");
		ShapefileWriter.write(lOppositeBoundary, oppositeBoundaryType, env.getCadastralParcels().getCoordinateReferenceSystem(), folderOut + "opposites.shp");
		// add the missing attributes
		env.getCadastralParcels().getFeatureType().addAttribute("BounNum", "Integer");
		env.getCadastralParcels().getFeatureType().addAttribute("BuildNum", "Integer");
		ShapefileWriter.write(env.getCadastralParcels(), folderOut + "parcelles.shp");
		//TODO do we need to write these?
		FeatureType boundaryType = new FeatureType("Boundary");
		boundaryType.setGeometryType("LineString");
		boundaryType.addAttribute("Type", "Integer");
		boundaryType.addAttribute("IDPar", "Integer");
		boundaryType.addAttribute("Side", "Integer");
		ShapefileWriter.write(bordures, boundaryType, env.getCadastralParcels().getCoordinateReferenceSystem(), folderOut + "bordures.shp");
		FeatureType translatedBoundaryType = new FeatureType("Boundary");
		translatedBoundaryType.setGeometryType("LineString");
		translatedBoundaryType.addAttribute("Type", "Integer");
		translatedBoundaryType.addAttribute("Side", "Integer");
		ShapefileWriter.write(bordures_translated, translatedBoundaryType, null, folderOut + "bordures_translated.shp");
		logger.info("Nombre de bpu : " + env.getBpU().count());
		ShapefileWriter.write(env.getBpU(), folderOut + "bpu.shp");
		logger.info("Sous Parcelles  " + env.getSubParcels().count());
		for (SubParcel sp : env.getSubParcels().findAll()) {
			sp.setAttribute("Test", 0);
			sp.setAttribute("NB Bat", sp.getBuildingsParts().size());
		}
		env.getSubParcels().getFeatureType().addAttribute("Test", "Integer");
		env.getSubParcels().getFeatureType().addAttribute("NB Bat", "Integer");
/*
		FeatureType subParcelType = new FeatureType("SubParcel");
		subParcelType.setGeometryType("LineString");
		subParcelType.addAttribute("Test", "Integer");
		subParcelType.addAttribute("NB Bat", "Integer");
*/
		ShapefileWriter.write(env.getSubParcels(), folderOut + "subParcels.shp");
		Collection<AbstractUrbanFeature> featToits = new ArrayList<>();
		logger.info("NB emprise " + env.getBuildings().count());
		for (Building b : env.getBuildings().findAll()) {
			AbstractUrbanFeature roof = new AbstractUrbanFeature();
			roof.setGeometry(b.getFootprint());
			featToits.add(roof);
		}
		FeatureType roofFT = new FeatureType("Roof");
		roofFT.setGeometryType("Polygon");
		ShapefileWriter.write(featToits, roofFT, env.getBuildings().getCoordinateReferenceSystem(), folderOut + "footprints.shp");
		Collection<AbstractUrbanFeature> featFaitage = new ArrayList<>();
		Collection<AbstractUrbanFeature> featPignon = new ArrayList<>();
		for (AbstractBuilding b : env.getBuildings().findAll()) {
			RoofSurface r = b.getRoof();
			if (r != null) {
				if (r.getRoofing() != null && !r.getRoofing().isEmpty()) {
					AbstractUrbanFeature faitage = new AbstractUrbanFeature();
					faitage.setGeometry(r.getRoofing());
					featFaitage.add(faitage);
				}
				if (r.getGable() != null && !r.getGable().isEmpty()) {
					AbstractUrbanFeature pignon = new AbstractUrbanFeature();
					pignon.setGeometry(r.getGable());
					featPignon.add(pignon);
				}
			}
		}
		logger.info("Faitage : " + featFaitage.size());
		FeatureType ridgeFT = new FeatureType("Ridge");
		ridgeFT.setGeometryType("MultiLineString");
		ShapefileWriter.write(featFaitage, ridgeFT, env.getBuildings().getCoordinateReferenceSystem(), folderOut + "faitage.shp");
		logger.info("Pignon : " + featPignon.size());
		FeatureType gableFT = new FeatureType("Gable");
		gableFT.setGeometryType("MultiLineString");
		ShapefileWriter.write(featPignon, gableFT, env.getBuildings().getCoordinateReferenceSystem(), folderOut + "pignon.shp");
		ShapefileWriter.write(env.getRoads(), folderOut + "roads.shp");
	}

	/**
	 * Create and export instance from SimPLU3D mode
	 * 
	 * @param folderIn  folder where the shapefile are stored
	 * @param folderOut folder where the instances will be written
	 * @throws Exception exception
	 */
	public static void export(URL folderIn, String folderOut) throws Exception {
		Environnement env = LoaderSHP.loadNoDTM(folderIn);
		export(env, folderOut);
	}
}
