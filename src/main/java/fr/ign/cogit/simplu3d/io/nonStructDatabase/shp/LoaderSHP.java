/**
 * 
 * This software is released under the licence CeCILL
 * <p>
 * see LICENSE.TXT
 * <p>
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import fr.ign.cogit.simplu3d.io.LoadFromCollection;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.util.FileLocator;
import fr.ign.common.dtm.AbstractDTM;
import fr.ign.common.dtm.DTMArea;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.data.DataUtilities;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureIterator;

/**
 * 
 * Helper to load shapefile data
 * <p>
 *
 * @author MBrasebin
 *
 */
public class LoaderSHP {
	private static final Logger logger = Logger.getLogger(LoaderSHP.class.getName());

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

	public static Environnement load(
			URL fileDoc ,
			URL fileZonage,
			URL fileParcels,
			URL fileVoirie,
			URL fileBuild,
			URL filePrescPonct,
			URL filePrescLin,
			URL filePrescSurf,
			InputStream dtmStream) throws Exception{
		DefaultFeatureCollection pluColl = readShapefile(fileDoc);
		DefaultFeatureCollection zoneColl = readShapefile(fileZonage);
		DefaultFeatureCollection parcelleColl = readShapefile(fileParcels);
		DefaultFeatureCollection voirieColl = readShapefile(fileVoirie);
		DefaultFeatureCollection batiColl = readShapefile(fileBuild);
		DefaultFeatureCollection prescriptions = readShapefile(filePrescLin);
		prescriptions.addAll(readShapefile(filePrescPonct).stream().toList());
		prescriptions.addAll(readShapefile(filePrescSurf).stream().toList());
		SimpleFeature featPLU = null;
		if (!pluColl.isEmpty()) {
			featPLU = pluColl.stream().toList().get(0);
		}
		
		DTMArea dtm = null;
		if (dtmStream != null) {
			dtm = new DTMArea(dtmStream, "Terrain", 1);
		}
        return LoadFromCollection.load(featPLU, zoneColl, parcelleColl, voirieColl, batiColl, prescriptions, dtm);
	}
	
	public static Environnement load(URL url) throws Exception {
		File folder = new File(url.toURI());
		File terrainFile = FileLocator.findFile(folder, NOM_FICHIER_TERRAIN);
		if (terrainFile == null) {
			throw new FileNotFoundException((new File(folder, NOM_FICHIER_TERRAIN)).toString());
		}
		return load(url, new FileInputStream(terrainFile));
	}

	public static Environnement loadNoDTM(URL url) throws Exception {
		return load(url, null);
	}

	public static Environnement load(URL url, InputStream dtmStream) throws Exception {
		// Chargement des fichiers
		DefaultFeatureCollection pluColl = readShapefile(url, NOM_FICHIER_PLU);
		SimpleFeature featPLU = null;
		if (!pluColl.isEmpty()) {
			FeatureIterator<SimpleFeature> iterator = pluColl.features();
			featPLU = iterator.next();
			iterator.close();
		} else {
		  logger.fine("No PLU file found");
		}
		DefaultFeatureCollection zoneColl = readShapefile(url, NOM_FICHIER_ZONAGE);
		logger.finer(zoneColl.size() + " zones found");
		DefaultFeatureCollection parcelleColl = readShapefile(url, NOM_FICHIER_PARCELLE);
		logger.finer(parcelleColl.size() + " parcels found");
		DefaultFeatureCollection voirieColl = readShapefile(url, NOM_FICHIER_VOIRIE);
		logger.finer(voirieColl.size() + " roads found");
		DefaultFeatureCollection batiColl = readShapefile(url, NOM_FICHIER_BATIMENTS);
		logger.finer(batiColl.size() + " buildings found");
		DefaultFeatureCollection prescriptions = readShapefile(url, NOM_FICHIER_PRESC_LINEAIRE);
		prescriptions.addAll(readShapefile(url, NOM_FICHIER_PRESC_PONCTUELLE).stream().toList());
		prescriptions.addAll(readShapefile(url, NOM_FICHIER_PRESC_SURFACIQUE).stream().toList());
		logger.finer(prescriptions.size() + " prescriptions found");
		// sous-parcelles route sans z, zonage, les bordures etc...
		AbstractDTM dtm = null;
		if (dtmStream != null) {
			dtm = new DTMArea(dtmStream, "Terrain", 1);
		}
        return LoadFromCollection.load(featPLU, zoneColl, parcelleColl, voirieColl, batiColl, prescriptions, dtm);
	}

	private static DefaultFeatureCollection readShapefile(URL url) throws MalformedURLException {
		if (url.getPath().isEmpty()) {
			logger.warning("LoaderSHP : File  not found  " + url);
			return new DefaultFeatureCollection();
		}
		File folder = new File(url.getPath());
		String fileName = folder.getName();
		return readShapefile(folder.getParentFile().toURI().toURL(), fileName);
	}

	/**
	 * helper to read a shapefile with case insensitive name.
	 * 
	 * @param url the folder url
	 * @param filename the filename
	 * @return a feature collection
	 */
	private static DefaultFeatureCollection readShapefile(URL url, String filename) {
		File folder = new File(url.getPath());
		if(!folder.exists()) {
			logger.fine("File " + filename + " not found in " + folder);
			return new DefaultFeatureCollection();
		}
		File file = FileLocator.findFile(folder, filename);
		if (null == file) {
			logger.fine("File " + filename + " not found in " + folder);
			return new DefaultFeatureCollection();
		}
		logger.finer("Loading features from " + file);
        try {
			Map<String, Object> map = new HashMap<>();
            map.put("url", file.toURI().toURL());
			DataStore dataStore = DataStoreFinder.getDataStore(map);
			String typeName = dataStore.getTypeNames()[0];
			FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
			Filter filter = Filter.INCLUDE;
			return DataUtilities.collection(source.getFeatures(filter));
        } catch (Exception e) {
            logger.warning("Can't load url from " + file);
        }
		return new DefaultFeatureCollection();
	}
}
