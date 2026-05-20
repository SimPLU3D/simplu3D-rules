package fr.ign.cogit.simplu3d.checker.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import fr.ign.cogit.simplu3d.serializer.IGeometrySerializer;
import org.locationtech.jts.geom.Geometry;

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
    Geometry geometry;
	String code;

	public UnrespectedRule() {
	
	}
	
	public UnrespectedRule(String message, Geometry geom, String code) {
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
	
	public String getCode() {
		return code;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry){
		this.geometry = geometry;
	}
}
