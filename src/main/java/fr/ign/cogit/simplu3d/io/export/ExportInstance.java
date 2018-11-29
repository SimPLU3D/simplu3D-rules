package fr.ign.cogit.simplu3d.io.export;

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.Road;
import fr.ign.cogit.simplu3d.model.RoofSurface;
import fr.ign.cogit.simplu3d.model.SubParcel;

/**
 * 
 * 
 * Class to export instances from the simPLU3D model
 * 
 * @author mbrasebin
 *
 */
public class ExportInstance {

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
		(new File(folderOut)).mkdirs();

		//
		System.out.println("Number of zones ni zoning : " + env.getUrbaZones().size());

		IFeatureCollection<IFeature> bordures_translated = new FT_FeatureCollection<>();
		IFeatureCollection<ParcelBoundary> bordures = new FT_FeatureCollection<ParcelBoundary>();

		int count = 0;

		IFeatureCollection<IFeature> lTotArc = new FT_FeatureCollection<>();
		IFeatureCollection<IFeature> lOppositeBoundary = new FT_FeatureCollection<>();

		System.out.println("Number of parcels : " + env.getCadastralParcels().size());

		for (BasicPropertyUnit bPU : env.getBpU()) {

			for (CadastralParcel sp : bPU.getCadastralParcels()) {

				count = count + sp.getBoundaries().size();

				IDirectPosition centroidParcel = sp.getGeom().centroid();

				AttributeManager.addAttribute(sp, "ID", sp.getId(), "Integer");
				AttributeManager.addAttribute(sp, "BounNum", sp.getBoundaries().size(), "Integer");
				AttributeManager.addAttribute(sp, "BuildNum", bPU.getBuildings().size(), "Integer");

				for (ParcelBoundary b : sp.getBoundaries()) {
					bordures.add(b);

					if (b.getOppositeBoundary() != null) {

						IDirectPositionList dpl = new DirectPositionList();
						dpl.add(b.getGeom().centroid());
						dpl.add(b.getOppositeBoundary().getGeom().centroid());
						ILineString ls = new GM_LineString(dpl);
						IFeature feat = new DefaultFeature(ls);
						AttributeManager.addAttribute(feat, "length", ls.length(), "Double");
						lOppositeBoundary.add(feat);
					}

		
					AttributeManager.addAttribute(b, "Type", b.getType().getValueType(), "Integer");
					AttributeManager.addAttribute(b, "IDPar", sp.getCode(), "Integer");
					AttributeManager.addAttribute(b, "Side", b.getSide().getValueType(), "Integer");

					if (b.getFeatAdj() != null) {

						if (b.getFeatAdj() instanceof CadastralParcel) {

							AttributeManager.addAttribute(b, "Adj", ((CadastralParcel) b.getFeatAdj()).getCode(),
									"String");
						} else if (b.getFeatAdj() instanceof Road) {
							AttributeManager.addAttribute(b, "Adj", ((Road) b.getFeatAdj()).getName(), "String");
						}

					} else {
						AttributeManager.addAttribute(b, "Adj", 0, "String");

					}

					if (b.getGeom() == null || b.getGeom().isEmpty()) {

						continue;
					}

					IDirectPosition centroidGeom = b.getGeom().coord().get(0);

					Vecteur v = new Vecteur(centroidParcel, centroidGeom);

					Vecteur v2 = new Vecteur(b.getGeom().coord().get(0),
							b.getGeom().coord().get(b.getGeom().coord().size() - 1));
					v2.setZ(0);
					v2.normalise();

					Vecteur vOut = v2.prodVectoriel(new Vecteur(0, 0, 1));

					IGeometry geom = ((IGeometry) b.getGeom().clone());

					if (v.prodScalaire(vOut) < 0) {
						vOut = vOut.multConstante(-1);
					}

					IGeometry geom2 = geom.translate(VALUE_SHIFT_BORDER * vOut.getX(), VALUE_SHIFT_BORDER * vOut.getY(),
							0);

					if (!geom2.intersects(sp.getGeom())) {
						geom2 = geom.translate(-VALUE_SHIFT_BORDER * vOut.getX(), -VALUE_SHIFT_BORDER * vOut.getY(), 0);
					}

					IFeature feat = new DefaultFeature(geom2);

					AttributeManager.addAttribute(feat, "Type", b.getType().getValueType(), "Integer");
					AttributeManager.addAttribute(feat, "Side", b.getSide().getValueType(), "Integer");
					bordures_translated.add(feat);

				}

			}
		}

		System.out.println("Number of borders" + count);

		// Export des parcelles
		ShapefileWriter.write(lOppositeBoundary, folderOut + "opposites.shp");
		ShapefileWriter.write(env.getCadastralParcels(), folderOut + "parcelles.shp");
		ShapefileWriter.write(bordures, folderOut + "bordures.shp");
		ShapefileWriter.write(bordures_translated, folderOut + "bordures_translated.shp");

		ShapefileWriter.write(lTotArc, folderOut + "arcs.shp");

		System.out.println("Nombre de bpu : " + env.getBpU().size());

		ShapefileWriter.write(env.getBpU(), folderOut + "bpu.shp");

		System.out.println("Sous Parcelles  " + env.getSubParcels().size());

		for (SubParcel sp : env.getSubParcels()) {
			AttributeManager.addAttribute(sp, "Test", 0, "Integer");
			AttributeManager.addAttribute(sp, "NB Bat", sp.getBuildingsParts().size(), "Integer");
		}

		ShapefileWriter.write(env.getSubParcels(), folderOut + "subParcels.shp");

		IFeatureCollection<IFeature> featToits = new FT_FeatureCollection<IFeature>();

		System.out.println("NB emprise " + env.getBuildings().size());
		for (AbstractBuilding b : env.getBuildings()) {
			featToits.add(new DefaultFeature(b.getFootprint()));
		}

		ShapefileWriter.write(featToits, folderOut + "footprints.shp");

		IFeatureCollection<IFeature> featFaitage = new FT_FeatureCollection<IFeature>();
		IFeatureCollection<IFeature> featPignon = new FT_FeatureCollection<IFeature>();
		for (AbstractBuilding b : env.getBuildings()) {
			RoofSurface r = b.getRoof();

			if (r != null) {

				if (r.getRoofing() != null && !r.getRoofing().isEmpty()) {
					featFaitage.add(new DefaultFeature(r.getRoofing()));
				}
				if (r.getGable() != null && !r.getGable().isEmpty()) {
					featPignon.add(new DefaultFeature(r.getGable()));
				}

			}

		}

		System.out.println("Faitage : " + featFaitage.size());

		ShapefileWriter.write(featFaitage, folderOut + "faitage.shp");


		System.out.println("Pignon : " + featPignon.size());

		ShapefileWriter.write(featPignon, folderOut + "pignon.shp");

		IFeatureCollection<IFeature> featRoute = new FT_FeatureCollection<>();

		for (Road r : env.getRoads()) {

			AttributeManager.addAttribute(r, "Nom", r.getName(), "String");
			featRoute.add(r);

		}

		ShapefileWriter.write(featRoute, folderOut + "roads.shp");
		/*
		 * IFeatureCollection<IFeature> featOutTestCons = new FT_FeatureCollection<>();
		 * for (CadastralParcel sp : env.getCadastralParcels()) {
		 * 
		 * featOutTestCons.add(new DefaultFeature(sp.getConsLine()));
		 * 
		 * }
		 * 
		 * ShapefileWriter.write(featOutTestCons, folderOut + "featConsF.shp");
		 */
	}

	/**
	 * Create and export instance from SimPLU3D mode
	 * 
	 * @param folderIn  folder where the shapefile are stored
	 * @param folderOut folder where the instances will be written
	 * @throws Exception exception
	 */
	public static void export(String folderIn, String folderOut) throws Exception {

		Environnement env = LoaderSHP.loadNoDTM(new File(folderIn));

		export(env, folderOut);

	}

}
