/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
package fr.ign.cogit.simplu3d.io.nonStructDatabase.shp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTMArea;
import fr.ign.cogit.geoxygene.sig3d.util.ColorShade;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.io.LoadFromCollection;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.util.FileLocator;

/**
 * 
 * Helper to load shapefile data
 * 
 * TODO move constants to "final"
 * 
 * @author MBrasebin
 *
 */
public class LoaderSHP {
	private static Logger logger = LogManager.getLogger(LoaderSHP.class);

	/*
	 * Nom des fichiers en entrée
	 */
	public static final String NOM_FICHIER_ZONAGE = "zone_urba.shp";
	public static final String NOM_FICHIER_PARCELLE = "parcelle.shp";
	public static final String NOM_FICHIER_VOIRIE = "route.shp";
	public static final String NOM_FICHIER_BATIMENTS = "batiment.shp";
	public static final String NOM_FICHIER_TERRAIN = "mnt.asc";
	public static final String NOM_FICHIER_PRESC_LINEAIRE = "prescription_lin.shp";
	public static final String NOM_FICHIER_PRESC_PONCTUELLE = "prescription_pct.shp";
	public static final String NOM_FICHIER_PRESC_SURFACIQUE = "prescription_surf.shp";
	public static final String NOM_FICHIER_PLU = "doc_urba.shp";

	public static Environnement load(File simuFile , File fileDoc ,File fileZonage, File fileParcels, File fileVoirie,  File fileBuild, File filePrescPonct,File filePrescLin,File filePrescSurf, InputStream dtmStream) throws Exception{
		IFeatureCollection<IFeature> pluColl = readShapefile(fileDoc);
		IFeatureCollection<IFeature> zoneColl = readShapefile(fileZonage);
		IFeatureCollection<IFeature> parcelleColl = readShapefile(fileParcels);
		IFeatureCollection<IFeature> voirieColl = readShapefile(fileVoirie);
		IFeatureCollection<IFeature> batiColl = readShapefile(fileBuild);
		IFeatureCollection<IFeature> prescriptions = readShapefile(filePrescLin);
		prescriptions.addAll(readShapefile(filePrescPonct));
		prescriptions.addAll(readShapefile(filePrescSurf));
		IFeature featPLU = null;
		if (!pluColl.isEmpty()) {
			featPLU = pluColl.get(0);
		}
		
		DTMArea dtm = null;

		if (dtmStream != null) {
			dtm = new DTMArea(dtmStream, "Terrain", true, 1, ColorShade.BLUE_CYAN_GREEN_YELLOW_WHITE);
		}
		
		Environnement env = LoadFromCollection.load(featPLU, zoneColl, parcelleColl, voirieColl, batiColl,
				prescriptions, dtm);

		
	
		return env;
	}
	
	public static Environnement load(File folder) throws Exception {
		File terrainFile = FileLocator.findFile(folder, NOM_FICHIER_TERRAIN);
		if (terrainFile == null) {
			throw new FileNotFoundException((new File(folder, NOM_FICHIER_TERRAIN)).toString());
		}
		return load(folder, new FileInputStream(terrainFile));
	}

	public static Environnement loadNoDTM(File folder) throws Exception {
		return load(folder, null);
	}

	public static Environnement load(File folder, InputStream dtmStream) throws Exception {

		// Chargement des fichiers
		IFeatureCollection<IFeature> pluColl = readShapefile(folder, NOM_FICHIER_PLU);
		IFeature featPLU = null;
		if (!pluColl.isEmpty()) {
			featPLU = pluColl.get(0);
		} else {
		  logger.warn("No PLU file found");
		}
		IFeatureCollection<IFeature> zoneColl = readShapefile(folder, NOM_FICHIER_ZONAGE);
		logger.warn(zoneColl.size() + " zones found");
		IFeatureCollection<IFeature> parcelleColl = readShapefile(folder, NOM_FICHIER_PARCELLE);
		logger.warn(parcelleColl.size() + " parcels found");
		IFeatureCollection<IFeature> voirieColl = readShapefile(folder, NOM_FICHIER_VOIRIE);
		IFeatureCollection<IFeature> batiColl = readShapefile(folder, NOM_FICHIER_BATIMENTS);
		IFeatureCollection<IFeature> prescriptions = readShapefile(folder, NOM_FICHIER_PRESC_LINEAIRE);
		prescriptions.addAll(readShapefile(folder, NOM_FICHIER_PRESC_LINEAIRE));
		prescriptions.addAll(readShapefile(folder, NOM_FICHIER_PRESC_PONCTUELLE));
		prescriptions.addAll(readShapefile(folder, NOM_FICHIER_PRESC_SURFACIQUE));
		// sous-parcelles route sans z, zonage, les bordures etc...
		DTMArea dtm = null;

		if (dtmStream != null) {
			dtm = new DTMArea(dtmStream, "Terrain", true, 1, ColorShade.BLUE_CYAN_GREEN_YELLOW_WHITE);
		}

		Environnement env = LoadFromCollection.load(featPLU, zoneColl, parcelleColl, voirieColl, batiColl,
				prescriptions, dtm);
	
		
		
		return env;
	}

	private static IFeatureCollection<IFeature> readShapefile(File folder) {
		if(folder == null) {
			logger.warn("LoaderSHP : File  not found  " + folder);
			return new FT_FeatureCollection<>();
		}
		String fileName = folder.getName();
		return readShapefile(folder.getParentFile(), fileName);
	}
	
	
	/**
	 * helper to read a shapefile with case insensitive name
	 * 
	 * @param folder
	 * @param filename
	 * @return
	 */
	private static IFeatureCollection<IFeature> readShapefile(File folder, String filename) {
		if(! folder.exists()) {
			logger.warn("File " + filename + " not found in " + folder);
			return new FT_FeatureCollection<>();
		}
		File file = FileLocator.findFile(folder, filename);
		if (null == file) {
			logger.warn("File " + filename + " not found in " + folder);
			return new FT_FeatureCollection<>();
		}
		logger.info("Loading features from " + file);
		return ShapefileReader.read(file.toString());
	}

}
