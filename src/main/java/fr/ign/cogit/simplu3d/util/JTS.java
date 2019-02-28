package fr.ign.cogit.simplu3d.util;

import org.locationtech.jts.geom.Geometry;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.simplu3d.serializer.OldJtsGeOxygene;

/**
 * 
 * Quiet version of JtsGeOxygene
 * 
 * @author MBorne
 *
 */
public class JTS {
	
	/**
	 * Convert GeOxygene's geometry to JTS geometry
	 * @param value  a IGeometry GeOxygene geometry
	 * @return a JTS geometry
	 */
	public static Geometry toJTS(IGeometry value){
		if ( value == null ){
			return null;
		}
		try {
			return JtsGeOxygene.makeJtsGeom(value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	 /**
   * Convert GeOxygene's geometry to JTS geometry
   * @param value  a IGeometry GeOxygene geometry
   * @return a JTS geometry
   */
  public static com.vividsolutions.jts.geom.Geometry toOldJTS(IGeometry value){
    if ( value == null ){
      return null;
    }
    try {
      return OldJtsGeOxygene.makeJtsGeom(value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

	/**
	 * Convert JTS geometry to GeOxygene's geometry
	 * @param value  a JTS geometry
	 * @return a IGeometry GeOxygene geometry
	 */
	public static IGeometry fromJTS(Geometry value){
		if ( value == null ){
			return null;
		}
		try {
			return JtsGeOxygene.makeGeOxygeneGeom(value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

  /**
   * Convert JTS geometry to GeOxygene's geometry
   * @param value  a JTS geometry
   * @return a IGeometry GeOxygene geometry
   */
  public static IGeometry fromOldJTS(com.vividsolutions.jts.geom.Geometry value){
    if ( value == null ){
      return null;
    }
    try {
      return OldJtsGeOxygene.makeGeOxygeneGeom(value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
}
