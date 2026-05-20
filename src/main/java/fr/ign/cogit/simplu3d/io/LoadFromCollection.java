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
package fr.ign.cogit.simplu3d.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import fr.ign.cogit.simplu3d.analysis.AssignOppositeToBoundary;
import fr.ign.cogit.simplu3d.analysis.AssignRoadToParcelBoundary;
import fr.ign.cogit.simplu3d.dao.UrbaZoneRepository;
import fr.ign.cogit.simplu3d.dao.geoxygene.UrbaZoneRepositoryGeoxygene;
import fr.ign.cogit.simplu3d.generator.BasicPropertyUnitGenerator;
import fr.ign.cogit.simplu3d.generator.SubParcelGenerator;
import fr.ign.cogit.simplu3d.importer.AssignBuildingPartToSubParcel;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.feature.BuildingReader;
import fr.ign.cogit.simplu3d.io.feature.PrescriptionReader;
import fr.ign.cogit.simplu3d.io.feature.RoadReader;
import fr.ign.cogit.simplu3d.io.feature.UrbaDocumentReader;
import fr.ign.cogit.simplu3d.io.feature.UrbaZoneReader;
import fr.ign.cogit.simplu3d.model.*;
import fr.ign.cogit.simplu3d.util.AssignZ;

import fr.ign.common.dtm.AbstractDTM;
import fr.ign.common.util.GeomUtils;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.feature.DefaultFeatureCollection;
import org.locationtech.jts.geom.Geometry;

/**
 * 
 * TODO EnvironnementBuilder to ease/clarify option management setZoneUrbas(..)
 * set... setGenerateSubParcel(true) setTranslateToZero(origin) //... build() :
 * Environnement
 * 
 * @author MBrasebin
 *
 */
public class LoadFromCollection {

	public final static boolean SURSAMPLED = true;

	private final static Logger logger = Logger.getLogger(LoadFromCollection.class.getName());

	public static Environnement load(SimpleFeature featPLU,
                                     DefaultFeatureCollection zoneColl,
                                     DefaultFeatureCollection parcelleColl,
                                     DefaultFeatureCollection voirieColl,
                                     DefaultFeatureCollection batiColl,
                                     DefaultFeatureCollection prescriptions,
                                     AbstractDTM dtm) throws Exception {
		Environnement env = Environnement.createEnvironnement();
		return LoadFromCollection.load(featPLU, zoneColl, parcelleColl, voirieColl, batiColl, prescriptions, 
				dtm, env);
	}

	public static Environnement load(SimpleFeature featPLU,
									 DefaultFeatureCollection zoneColl,
									 DefaultFeatureCollection parcelleColl,
									 DefaultFeatureCollection voirieColl,
									 DefaultFeatureCollection batiColl,
									 DefaultFeatureCollection prescriptions,
									 AbstractDTM dtm,
									 Environnement env) throws Exception {

		// Step 0: should we translate all objects?
		if (Environnement.TRANSLATE_TO_ZERO) {
			Environnement.dpTranslate = zoneColl.getBounds().centre();
			for (SimpleFeature feat : zoneColl) {
				feat.setDefaultGeometry(GeomUtils.translate((Geometry) feat.getDefaultGeometry(),-Environnement.dpTranslate.getX(),
						-Environnement.dpTranslate.getY(), 0));
			}
			for (SimpleFeature feat : parcelleColl) {
				feat.setDefaultGeometry(GeomUtils.translate((Geometry) feat.getDefaultGeometry(), -Environnement.dpTranslate.getX(),
						-Environnement.dpTranslate.getY(), 0));
			}
			for (SimpleFeature feat : voirieColl) {
				feat.setDefaultGeometry(GeomUtils.translate((Geometry) feat.getDefaultGeometry(), -Environnement.dpTranslate.getX(),
						-Environnement.dpTranslate.getY(), 0));
			}
			for (SimpleFeature feat : batiColl) {
				feat.setDefaultGeometry(GeomUtils.translate((Geometry) feat.getDefaultGeometry(), -Environnement.dpTranslate.getX(),
						-Environnement.dpTranslate.getY(), 0));
			}
			for (SimpleFeature feat : prescriptions) {
				feat.setDefaultGeometry(GeomUtils.translate((Geometry) feat.getDefaultGeometry(), -Environnement.dpTranslate.getX(),
						-Environnement.dpTranslate.getY(), 0));
			}
		}
		// Step 1: create PLU object
		logger.fine("Read UrbaDocument...");
		UrbaDocument plu;
		if (featPLU == null) {
			plu = new UrbaDocument();
		} else {
			UrbaDocumentReader urbaDocumentReader = new UrbaDocumentReader();
			plu = urbaDocumentReader.read(featPLU);
		}
		env.setUrbaDocument(plu);

		// Etape 2 : création des zones et assignation des règles aux zones
		logger.fine("Loading UrbaZone...");
		UrbaZoneReader urbaZoneReader = new UrbaZoneReader();
        Collection<UrbaZone> zones = urbaZoneReader.readAll(zoneColl);

		// Etape 3 : assignement des zonages au PLU
		env.setUrbaZones(zones);

		logger.fine("Loading CadastralParcel and compute ParcelBoundary...");
		// Etape 4 : chargement des parcelles et créations des bordures
		Collection<CadastralParcel> parcelles = CadastralParcelLoader.assignBordureToParcelleWithOrientation(parcelleColl);
		env.setCadastralParcels(parcelles);
		logger.fine("Parcels...");
		parcelles.forEach(parcelle -> {
			logger.finer(parcelle.toString());
			logger.finer("Parcel boundaries: " + parcelle.getBoundaries().size());
		});

		// Etape 5 : import des sous parcelles
		logger.fine("Loading SubParcels...");
		{
			Collection<SubParcel> sousParcelles = new ArrayList<>();
			SubParcelGenerator subParcelGenerator = new SubParcelGenerator(zones);
			for (CadastralParcel cadastralParcel : parcelles) {
				sousParcelles.addAll(subParcelGenerator.createSubParcels(cadastralParcel));
			}
			env.setSubParcels(sousParcelles);
		}

		// Etape 6 : création des unités foncirèes
		logger.fine("Loading BasicPropertyUnits...");
		BasicPropertyUnitGenerator bpuBuilder = new BasicPropertyUnitGenerator(parcelles);
		Collection<BasicPropertyUnit> collBPU = bpuBuilder.createPropertyUnits();
		env.setBpU(collBPU);

		// Etape 7 : import des bâtiments
		logger.fine("Loading Buildings...");
		BuildingReader buildingReader = new BuildingReader();
		Collection<Building> buildings = buildingReader.readAll(batiColl);
		env.setBuildings(buildings);

		// Etape 7.1 : assignation des batiments aux BpU
		logger.fine("Assigning building to SubParcels...");
		AssignBuildingPartToSubParcel.assign(buildings, collBPU);

		// Etape 8 : chargement des voiries
		logger.fine("Loading Roads...");
		RoadReader roadReader = new RoadReader();
        Collection<Road> roads = new ArrayList<>(roadReader.readAll(voirieColl));
		env.setRoads(roads);

		// Etape 9 : on affecte les liens entre une bordure et ses objets
		// adjacents (bordure sur route -> route + relation entre les limites de
		// parcelles)
		logger.fine("Assigning Roads to ParcelBoundaries...");
		AssignRoadToParcelBoundary.process(parcelles, roads);

		// Etape 10 : on détecte les limites séparatives opposées
		logger.fine("Assigning opposite boundaries to parcel boundaries...");
		AssignOppositeToBoundary.process(parcelles);

		// Etape 11 : on importe les alignements
		logger.fine("Loading Prescriptions...");
		{
			PrescriptionReader prescriptionReader = new PrescriptionReader();
			Collection<Prescription> prescriptionsRead = prescriptionReader.readAll(prescriptions);
			env.setPrescriptions(prescriptionsRead);
		}

		logger.fine("Assign Z to features...");
		// Etape 12 : on affecte des z à tout ce bon monde // - parcelles,
		// sous-parcelles route sans z, zonage, les bordures, etc.
		env.setTerrain(dtm);
		try {
			AssignZ.toParcelle(env.getCadastralParcels(), dtm, SURSAMPLED);
			AssignZ.toSousParcelle(env.getSubParcels(), dtm, SURSAMPLED);
			AssignZ.toVoirie(env.getRoads(), dtm, SURSAMPLED);
			AssignZ.toPrescriptions(env.getPrescriptions(), dtm, SURSAMPLED);
			AssignZ.toZone(env.getUrbaZones(), dtm, false);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}

		logger.fine("Loading complete");
		return env;
	}
}
