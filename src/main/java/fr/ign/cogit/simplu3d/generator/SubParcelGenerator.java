package fr.ign.cogit.simplu3d.generator;

import java.util.ArrayList;
import java.util.Collection;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.model.UrbaZone;

/**
 * 
 * Generate SubParcels as an intersection of CadastralParcel and UrbaZone
 * 
 * @author MBorne
 *
 */
public class SubParcelGenerator {

	/**
	 * 
	 */
	private IFeatureCollection<CadastralParcel> cadastralParcels;

	/**
	 * Minimal area (filter subparcels bellow this value)
	 */
	private double minArea;

	/**
	 * 
	 * @param cadastralParcels
	 */
	public SubParcelGenerator(IFeatureCollection<CadastralParcel> cadastralParcels) {
		this.cadastralParcels = cadastralParcels;
	}

	/**
	 * Create SubParcels for an UrbaZone
	 * @param urbaZone
	 * @return
	 */
	public Collection<SubParcel> createSubParcels(UrbaZone urbaZone) {
		Collection<SubParcel> result = new ArrayList<>();

		Collection<CadastralParcel> candidates = cadastralParcels.select(urbaZone.getGeom());
		for (CadastralParcel candidate : candidates) {
			IGeometry intersection = candidate.getGeom().intersection(urbaZone.getGeom());
			if (intersection == null) {
				continue;
			}
			if (intersection.area() < minArea) {
				continue;
			}
			
			SubParcel subParcel = createSubParcel(intersection);

			// CadastralParcel / SubParcel
			subParcel.setParcelle(candidate);
			candidate.getSubParcel().add(subParcel);
			
			// UrbaZone / SubParcel
			subParcel.setZoneUrba(urbaZone);
			urbaZone.getSubParcels().add(subParcel);
			
			result.add(subParcel);
		}

		return result;
	}
	
	/**
	 * Create SubParcel from intersection's geometry
	 * @param intersection
	 * @return
	 */
	private SubParcel createSubParcel(IGeometry intersection) {
		SubParcel subParcel = new SubParcel();

		IMultiSurface<IOrientableSurface> iMS = FromGeomToSurface.convertMSGeom(intersection);

		//TODO check this code (create a copy would be better?)
		int nbContrib = iMS.size();
		for (int i = 0; i < nbContrib; i++) {
			IOrientableSurface os = iMS.get(i);

			if (os == null || os.isEmpty()) {
				iMS.remove(i);
				i--;
				nbContrib--;
			}
		}

		//TODO check why it's necessary to duplicate geometry (feature/CityGML inheritance?)
		subParcel.setGeom(iMS);
		subParcel.setLod2MultiSurface(iMS);

		return subParcel;
	}
}
