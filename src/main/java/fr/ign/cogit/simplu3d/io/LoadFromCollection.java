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
package fr.ign.cogit.simplu3d.io;

import java.util.Collection;
import java.util.logging.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.semantic.AbstractDTM;
import fr.ign.cogit.simplu3d.analysis.AssignOppositeToBoundary;
import fr.ign.cogit.simplu3d.analysis.AssignRoadToParcelBoundary;
import fr.ign.cogit.simplu3d.generator.BasicPropertyUnitGenerator;
import fr.ign.cogit.simplu3d.generator.SubParcelGenerator;
import fr.ign.cogit.simplu3d.importer.AssignBuildingPartToSubParcel;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.feature.BuildingReader;
import fr.ign.cogit.simplu3d.io.feature.PrescriptionReader;
import fr.ign.cogit.simplu3d.io.feature.RoadReader;
import fr.ign.cogit.simplu3d.io.feature.UrbaDocumentReader;
import fr.ign.cogit.simplu3d.io.feature.UrbaZoneReader;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.Prescription;
import fr.ign.cogit.simplu3d.model.Road;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.model.UrbaDocument;
import fr.ign.cogit.simplu3d.model.UrbaZone;
import fr.ign.cogit.simplu3d.util.AssignZ;

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

	private final static Logger logger = Logger.getLogger(LoadFromCollection.class.getCanonicalName());

	public static Environnement load(IFeature featPLU, IFeatureCollection<IFeature> zoneColl,
			IFeatureCollection<IFeature> parcelleColl, IFeatureCollection<IFeature> voirieColl,
			IFeatureCollection<IFeature> batiColl, IFeatureCollection<IFeature> prescriptions, String ruleFolder,
			AbstractDTM dtm) throws Exception {
		Environnement env = Environnement.createEnvironnement();

		return LoadFromCollection.load(featPLU, zoneColl, parcelleColl, voirieColl, batiColl, prescriptions, ruleFolder,
				dtm, env);
	}

	public static Environnement load(IFeature featPLU, IFeatureCollection<IFeature> zoneColl,
			IFeatureCollection<IFeature> parcelleColl, IFeatureCollection<IFeature> voirieColl,
			IFeatureCollection<IFeature> batiColl, IFeatureCollection<IFeature> prescriptions, String ruleFolder,
			AbstractDTM dtm, Environnement env) throws Exception {

		// Etape 0 : doit on translater tous les objets ?

		if (Environnement.TRANSLATE_TO_ZERO) {
			Environnement.dpTranslate = zoneColl.envelope().center();
			for (IFeature feat : zoneColl) {
				feat.setGeom(feat.getGeom().translate(-Environnement.dpTranslate.getX(),
						-Environnement.dpTranslate.getY(), 0));
			}
			for (IFeature feat : parcelleColl) {
				feat.setGeom(feat.getGeom().translate(-Environnement.dpTranslate.getX(),
						-Environnement.dpTranslate.getY(), 0));
			}
			for (IFeature feat : voirieColl) {
				feat.setGeom(feat.getGeom().translate(-Environnement.dpTranslate.getX(),
						-Environnement.dpTranslate.getY(), 0));
			}
			for (IFeature feat : batiColl) {
				feat.setGeom(feat.getGeom().translate(-Environnement.dpTranslate.getX(),
						-Environnement.dpTranslate.getY(), 0));
			}
			for (IFeature feat : prescriptions) {
				feat.setGeom(feat.getGeom().translate(-Environnement.dpTranslate.getX(),
						-Environnement.dpTranslate.getY(), 0));
			}
		}

		// Etape 1 : création de l'objet PLU
		logger.info("Read UrbaDocument...");
		UrbaDocument plu;
		if (featPLU == null) {
			plu = new UrbaDocument();
		} else {
			UrbaDocumentReader urbaDocumentReader = new UrbaDocumentReader();
			plu = urbaDocumentReader.read(featPLU);
		}
		env.setUrbaDocument(plu);

		// Etape 2 : création des zones et assignation des règles aux zones
		logger.info("Loading UrbaZone...");
		UrbaZoneReader urbaZoneReader = new UrbaZoneReader();
		IFeatureCollection<UrbaZone> zones = new FT_FeatureCollection<>();
		zones.addAll(urbaZoneReader.readAll(zoneColl));

		// Etape 3 : assignement des zonages au PLU
		env.setUrbaZones(zones);

		logger.info("Loading CadastralParcel and compute ParcelBoundary...");
		// Etape 4 : chargement des parcelles et créations des bordures
		IFeatureCollection<CadastralParcel> parcelles = CadastralParcelLoader
				.assignBordureToParcelleWithOrientation(parcelleColl);
		env.setCadastralParcels(parcelles);

		// Etape 5 : import des sous parcelles
		logger.info("Loading SubParcels...");
		{
			IFeatureCollection<SubParcel> sousParcelles = new FT_FeatureCollection<>();
			SubParcelGenerator subParcelGenerator = new SubParcelGenerator(zones);
			for (CadastralParcel cadastralParcel : parcelles) {
				sousParcelles.addAll(subParcelGenerator.createSubParcels(cadastralParcel));
			}
			env.setSubParcels(sousParcelles);
		}

		// Etape 6 : création des unités foncirèes
		logger.info("Loading BasicPropertyUnits...");
		BasicPropertyUnitGenerator bpuBuilder = new BasicPropertyUnitGenerator(parcelles);
		IFeatureCollection<BasicPropertyUnit> collBPU = bpuBuilder.createPropertyUnits();
		env.setBpU(collBPU);

		// Etape 7 : import des bâtiments
		logger.info("Loading Buildings...");
		BuildingReader buildingReader = new BuildingReader();
		Collection<Building> buildings = buildingReader.readAll(batiColl);
		env.getBuildings().addAll(buildings);

		// Etape 7.1 : assignation des batiments aux BpU
		logger.info("Assigning building to SubParcels...");
		AssignBuildingPartToSubParcel.assign(buildings, collBPU);

		// Etape 8 : chargement des voiries
		logger.info("Loading Roads...");
		RoadReader roadReader = new RoadReader();
		IFeatureCollection<Road> roads = new FT_FeatureCollection<>();
		roads.addAll(roadReader.readAll(voirieColl));
		env.setRoads(roads);

		// Etape 9 : on affecte les liens entres une bordure et ses objets
		// adjacents (bordure sur route => route + relation entre les limites de
		// parcelles)
		logger.info("Assigning Roads to ParcelBoundaries...");
		AssignRoadToParcelBoundary.process(parcelles, roads);

		// Etape 10 : on détecte les limites séparatives opposées
		logger.info("Assigning opposite boundaries to parcel boundaries...");
		AssignOppositeToBoundary.process(parcelles);

		// Etape 11 : on importe les alignements
		logger.info("Loading Prescriptions...");
		{
			PrescriptionReader prescriptionReader = new PrescriptionReader();
			Collection<Prescription> prescriptionsRead = prescriptionReader.readAll(prescriptions);
			env.getPrescriptions().addAll(prescriptionsRead);
		}

		logger.info("Assign Z to features...");
		// Etape 12 : on affecte des z à tout ce bon monde // - parcelles,
		// sous-parcelles route sans z, zonage, les bordures etc...
		env.setTerrain(dtm);
		try {
			AssignZ.toParcelle(env.getCadastralParcels(), dtm, SURSAMPLED);
			AssignZ.toSousParcelle(env.getSubParcels(), dtm, SURSAMPLED);
			AssignZ.toVoirie(env.getRoads(), dtm, SURSAMPLED);
			AssignZ.toPrescriptions(env.getPrescriptions(), dtm, SURSAMPLED);
			AssignZ.toZone(env.getUrbaZones(), dtm, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Loading complete");

		return env;
	}

}
