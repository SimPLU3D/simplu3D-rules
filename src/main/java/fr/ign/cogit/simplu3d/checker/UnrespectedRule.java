package fr.ign.cogit.simplu3d.checker;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.serializer.IGeometrySerializer;

/**
 * 
 * Represents a non respected rule
 * 
 * @author MBrabesin
 *
 */
public class UnrespectedRule {
	String message;
	@JsonSerialize(using = IGeometrySerializer.class)
	IGeometry geometry;
	String code;

	public UnrespectedRule() {
	
	}
	
	public UnrespectedRule(String message, IGeometry geom, String code) {
		this.message = message;
		this.geometry = geom;
		this.code = code;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	public void setCode(String code){
		this.code = code ;
	}
	
	public String getCode(){
		return code;
	}

	public IGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(IGeometry geometry){
		this.geometry = geometry;
	}
	
}
