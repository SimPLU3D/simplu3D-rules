package fr.ign.cogit.simplu3d.io.export;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.*;
import fr.ign.common.io.AttributeManager;
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
 *
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
		logger.info("Number of zones ni zoning : " + env.getUrbaZones().size());
		Collection<ParcelBoundary> bordures_translated = new ArrayList<>();
		Collection<ParcelBoundary> bordures = new ArrayList<>();
		int count = 0;
		Collection<ParcelBoundary> lTotArc = new ArrayList<>();
		Collection<ParcelBoundary> lOppositeBoundary = new ArrayList<>();
		logger.info("Number of parcels : " + env.getCadastralParcels().size());
		for (BasicPropertyUnit bPU : env.getBpU()) {
			for (CadastralParcel sp : bPU.getCadastralParcels()) {
				count = count + sp.getBoundaries().size();
				Coordinate centroidParcel = sp.getGeometry().getCentroid().getCoordinate();
				AttributeManager.addAttribute(sp, "ID", sp.getId(), "Integer");
				AttributeManager.addAttribute(sp, "BounNum", sp.getBoundaries().size(), "Integer");
				AttributeManager.addAttribute(sp, "BuildNum", bPU.getBuildings().size(), "Integer");
				for (ParcelBoundary b : sp.getBoundaries()) {
					bordures.add(b);
					if (b.getOppositeBoundary() != null) {
						Collection<Coordinate> dpl = new ArrayList<>();
						dpl.add(b.getGeometry().getCentroid().getCoordinate());
						dpl.add(b.getOppositeBoundary().getGeometry().getCentroid().getCoordinate());
						LineString ls = b.getGeometry().getFactory().createLineString(dpl.toArray(new Coordinate[0]));
						ParcelBoundary feat = new ParcelBoundary(ls);
						AttributeManager.addAttribute(feat, "length", ls.getLength(), "Double");
						lOppositeBoundary.add(feat);
					}
					AttributeManager.addAttribute(b, "Type", b.getParcelBoundaryType().getValueType(), "Integer");
					AttributeManager.addAttribute(b, "IDPar", sp.getCode(), "Integer");
					AttributeManager.addAttribute(b, "Side", b.getSide().getValueType(), "Integer");
					if (b.getFeatAdj() != null) {
						if (b.getFeatAdj() instanceof CadastralParcel) {
							AttributeManager.addAttribute(b, "Adj", ((CadastralParcel) b.getFeatAdj()).getCode(),
									"String");
						} else if (b.getFeatAdj() instanceof Road) {
							AttributeManager.addAttribute(b, "Adj", ((Road) b.getFeatAdj()).getRoadName(), "String");
						}
					} else {
						AttributeManager.addAttribute(b, "Adj", 0, "String");
					}
					if (b.getGeometry() == null || b.getGeometry().isEmpty()) {
						continue;
					}
					Coordinate centroidGeom = b.getGeometry().getCoordinates()[0];
					Vecteur v = new Vecteur(centroidParcel, centroidGeom);
					Vecteur v2 = new Vecteur(b.getGeometry().getCoordinates()[0],
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
					AttributeManager.addAttribute(feat, "Type", b.getParcelBoundaryType().getValueType(), "Integer");
					AttributeManager.addAttribute(feat, "Side", b.getSide().getValueType(), "Integer");
					bordures_translated.add(feat);
				}
			}
		}
		logger.info("Number of borders" + count);
		// Export des parcelles
		ShapefileWriter.write(lOppositeBoundary, folderOut + "opposites.shp");
		ShapefileWriter.write(env.getCadastralParcels(), folderOut + "parcelles.shp");
		ShapefileWriter.write(bordures, folderOut + "bordures.shp");
		ShapefileWriter.write(bordures_translated, folderOut + "bordures_translated.shp");
		ShapefileWriter.write(lTotArc, folderOut + "arcs.shp");
		logger.info("Nombre de bpu : " + env.getBpU().size());
		ShapefileWriter.write(env.getBpU(), folderOut + "bpu.shp");
		logger.info("Sous Parcelles  " + env.getSubParcels().size());
		for (SubParcel sp : env.getSubParcels()) {
			AttributeManager.addAttribute(sp, "Test", 0, "Integer");
			AttributeManager.addAttribute(sp, "NB Bat", sp.getBuildingsParts().size(), "Integer");
		}
		ShapefileWriter.write(env.getSubParcels(), folderOut + "subParcels.shp");
		Collection<BuildingPart> featToits = new ArrayList<>();
		logger.info("NB emprise " + env.getBuildings().size());
		for (Building b : env.getBuildings()) {
			BuildingPart roof = new BuildingPart();
			roof.setGeometry(b.getFootprint());
			featToits.add(roof);//TODO better class?
		}
		ShapefileWriter.write(featToits, folderOut + "footprints.shp");
		Collection<BuildingPart> featFaitage = new ArrayList<>();
		Collection<BuildingPart> featPignon = new ArrayList<>();
		for (AbstractBuilding b : env.getBuildings()) {
			RoofSurface r = b.getRoof();
			if (r != null) {
				if (r.getRoofing() != null && !r.getRoofing().isEmpty()) {
					BuildingPart faitage = new BuildingPart();
					faitage.setGeometry(r.getRoofing());
					featFaitage.add(faitage);//TODO better class?
				}
				if (r.getGable() != null && !r.getGable().isEmpty()) {
					BuildingPart pignon = new BuildingPart();
					pignon.setGeometry(r.getGable());
					featPignon.add(pignon);//TODO better class?
				}
			}
		}
		logger.info("Faitage : " + featFaitage.size());
		ShapefileWriter.write(featFaitage, folderOut + "faitage.shp");
		logger.info("Pignon : " + featPignon.size());
		ShapefileWriter.write(featPignon, folderOut + "pignon.shp");
		Collection<Road> featRoute = new ArrayList<>();
		for (Road r : env.getRoads()) {
			AttributeManager.addAttribute(r, "Nom", r.getRoadName(), "String");
			featRoute.add(r);
		}
		ShapefileWriter.write(featRoute, folderOut + "roads.shp");
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
