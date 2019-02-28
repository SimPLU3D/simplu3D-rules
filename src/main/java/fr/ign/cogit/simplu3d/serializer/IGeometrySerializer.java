package fr.ign.cogit.simplu3d.serializer;

import java.io.IOException;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.util.JTS;

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
		jtsSerializer.serialize(JTS.toOldJTS(value), gen, serializers);
	}

}
