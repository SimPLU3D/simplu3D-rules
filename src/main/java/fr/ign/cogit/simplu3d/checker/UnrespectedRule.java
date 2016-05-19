package fr.ign.cogit.simplu3d.checker;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;

public class UnrespectedRule {
	String message;
	IGeometry geom;
	String code;

	public UnrespectedRule() {
		super();
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UnrespectedRule(String message, IGeometry geom, String code) {
		super();
		this.message = message;
		this.geom = geom;
		this.code = code;

	}

	public String getMessage() {
		return message;
	}

	public IGeometry getGeom() {
		return geom;
	}

	public String getCode(){
		return ""+code;
	}
	
	public Geometry getGeometry() {

		try {
			return JtsGeOxygene.makeJtsGeom(geom);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

}
