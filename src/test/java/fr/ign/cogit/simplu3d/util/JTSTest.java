package fr.ign.cogit.simplu3d.util;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ISphere;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import junit.framework.TestCase;

public class JTSTest extends TestCase {

	public void testNullGeometry(){
		assertNull( JTS.toJTS(null) ) ;
		assertNull( JTS.fromJTS(null) ) ;
	}
	
	public void testPointToJTS(){
		IPoint point = new GM_Point(new DirectPosition(3.0,4.0));
		Geometry jts = JTS.toJTS(point);
		assertEquals("POINT (3 4)", jts.toText());
	}
	
	public void testPoint3DToJTS(){
		IPoint point = new GM_Point(new DirectPosition(3.0,4.0,5.0));
		assertEquals(3, point.coordinateDimension() );
		Geometry jts = JTS.toJTS(point);
		assertEquals(3.0, jts.getCoordinates()[0].x);
		assertEquals(4.0, jts.getCoordinates()[0].y);		
		assertEquals(5.0, jts.getCoordinates()[0].z);
	}

	public void testLineStringToJTS(){
		GM_LineString geoxygene = new GM_LineString();
		geoxygene.addControlPoint(new DirectPosition(0.0,0.0));
		geoxygene.addControlPoint(new DirectPosition(3.0,4.0));
		Geometry jts = JTS.toJTS(geoxygene);
		assertEquals("LINESTRING (0 0, 3 4)", jts.toText());
	}

	
}
