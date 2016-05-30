package fr.ign.cogit.simplu3d.serializer;

import java.io.IOException;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;

/**
 * 
 * IGeometry serialization based on JTS
 * 
 * @author MBorne
 *
 */
public class IGeometrySerializer extends JsonSerializer<IGeometry>{

	private GeometrySerializer jtsSerializer = new GeometrySerializer();
	
	@Override
	public void serialize(IGeometry value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		jtsSerializer.serialize(toJTS(value), gen, serializers);
	}

	private Geometry toJTS(IGeometry value){
		try {
			return JtsGeOxygene.makeJtsGeom(value);
		} catch (Exception e) {
			return null;
		}
	}
	
}
