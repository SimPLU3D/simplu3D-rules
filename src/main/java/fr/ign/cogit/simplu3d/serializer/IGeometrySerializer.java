package fr.ign.cogit.simplu3d.serializer;

import java.io.IOException;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.locationtech.jts.geom.Geometry;

/**
 * 
 * IGeometry serialization based on JTS
 * 
 * @author MBorne
 *
 */
public class IGeometrySerializer extends JsonSerializer<Geometry>{
	private final GeometrySerializer jtsSerializer = new GeometrySerializer();
	@Override
	public void serialize(Geometry value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException {
		jtsSerializer.serialize(value, gen, serializers);
	}
}
