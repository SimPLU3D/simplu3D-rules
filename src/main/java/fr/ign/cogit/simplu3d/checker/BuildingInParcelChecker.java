package fr.ign.cogit.simplu3d.checker;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.SubParcel;

public class BuildingInParcelChecker implements IRuleChecker {

	public static final String CODE = "BUILDING_IN_PARCEL";
	
	
	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU) {
		List<UnrespectedRule> unrespectedRules = new ArrayList<>();

		//TODO see why bPU.getBuildings() is empty (loader issue?)
		
		for (CadastralParcel cP : bPU.getCadastralParcel()) {
			for (SubParcel sP : cP.getSubParcel()) {
				Geometry parcelGeometry = toJTS(sP.getGeom());
				for (AbstractBuilding bP : sP.getBuildingsParts()) {
					Geometry footprint = toJTS(bP.getFootprint());
					if ( footprint == null ){
						throw new RuntimeException("No footprint!");
					}
					if ( parcelGeometry.contains(footprint) ){
						continue;
					}
					Geometry difference = footprint.difference(parcelGeometry);
					double area = difference.getArea();
					if ( area < 1.0 ){
						continue;
					}
					unrespectedRules.add(new UnrespectedRule(
						"Le bâtiment est en dehors de la parcelle",
						fromJTS(difference),
						CODE
					));
				}
			}
		}

		return unrespectedRules;
	}
	
	private IGeometry fromJTS(Geometry geometry){
		try {
			return JtsGeOxygene.makeGeOxygeneGeom(geometry);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Geometry toJTS(IGeometry geometry){
		try {
			return JtsGeOxygene.makeJtsGeom(geometry);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
