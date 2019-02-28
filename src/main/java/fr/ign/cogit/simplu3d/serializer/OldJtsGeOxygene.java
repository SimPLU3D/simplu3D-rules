package fr.ign.cogit.simplu3d.serializer;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.geoxygene.util.conversion.WktGeOxygene;

/**
 * Conversions entre les GM_Object GeOxygene et les Geometry JTS.
 * 
 * @author Thierry Badard, Arnaud Braun & Christophe Pele & Julien Perret
 * @version 1.2 2008/07/31 (Julien Perret) : ajout de la factory {@link AdapterFactory}
 * @version 1.3 2008/07/31 (Julien Perret) : commentaires javadoc
 */

public class OldJtsGeOxygene {
  /*------------------------------------------------------------*/
  /*-- Fields --------------------------------------------------*/
  /*------------------------------------------------------------*/

  // private static int jtsSRID=0;
  private static PrecisionModel jtsPrecision = new PrecisionModel();

  // private static GeometryFactory jtsGeomFactory=new
  // GeometryFactory(JtsGeOxygene.jtsPrecision,JtsGeOxygene.jtsSRID);
  // private static WKTReader jtsWktReader=new
  // WKTReader(JtsGeOxygene.jtsGeomFactory);
  // private static WKTWriter jtsWktWriter=new WKTWriter();

  /*------------------------------------------------------------*/
  /*-- Conversion beetween JTS and GeOxygene objects -----------*/
  /*------------------------------------------------------------*/


  /**
   * Conversion d'une géométrie GeOxygene {@link IGeometry} en géométrie JTS {@link Geometry}.
   * 
   * @param geOxyGeom
   *          une géométrie GeOxygene
   * @param adapter
   *          si adapter est vrai, on utiliser la factory {@link AdapterFactory}, sinon, on passe par WKT
   * @return une géométrie JTS équivalente
   * @throws Exception
   *           Exception renvoie une exception si la géométrie en entrée n'est pas valide
   */
  public static Geometry makeJtsGeom(IGeometry geOxyGeom) throws Exception {
    GeometryFactory jtsGeomFactory = new GeometryFactory(OldJtsGeOxygene.jtsPrecision, geOxyGeom.getCRS());
    WKTReader jtsWktReader = new WKTReader(jtsGeomFactory);
    String wktGeom = WktGeOxygene.makeWkt(geOxyGeom);
    return jtsWktReader.read(wktGeom);
  }

  /**
   * Conversion d'une géométrie JTS {@link Geometry} en géométrie GeOxygene {@link IGeometry}.
   * 
   * @param jtsGeom
   *          une géométrie JTS
   * @return une géométrie GeOxygene équivalente
   * @throws Exception
   *           Exception renvoie une exception si la géométrie en entrée n'est pas valide
   */
  public static IGeometry makeGeOxygeneGeom(Geometry jtsGeom) throws Exception {
    WKTWriter jtsWktWriter = new WKTWriter();
    String wktResult = jtsWktWriter.write(jtsGeom);
    return WktGeOxygene.makeGeOxygene(wktResult);
  }

  /**
   * Conversion d'une coordonnée JTS {@link CoordinateSequence} en position GeOxygene {@link IDirectPosition}.
   * 
   * @param jtsCoord
   *          une coordonnée JTS
   * @return une position GeOxygene équivalente
   * @throws Exception
   *           Exception renvoie une exception si la géométrie en entrée n'est pas valide
   */
  public static IDirectPosition makeDirectPosition(CoordinateSequence jtsCoord) throws Exception {
    GeometryFactory jtsGeomFactory = new GeometryFactory(OldJtsGeOxygene.jtsPrecision, 0);
    Geometry jtsPoint = new Point(jtsCoord, jtsGeomFactory);
    IPoint geOxyPoint = (IPoint) OldJtsGeOxygene.makeGeOxygeneGeom(jtsPoint);
    IDirectPosition geOxyDirectPos = geOxyPoint.getPosition();
    return geOxyDirectPos;
  }

  /**
   * Conversion d'un tableau de coordonnées JTS {@link CoordinateSequence} en liste de positions GeOxygene {@link IDirectPositionList}.
   * 
   * @param jtsCoords
   *          un tableau de coordonnées JTS
   * @return une list de positions GeOxygene équivalente
   * @throws Exception
   *           Exception renvoie une exception si la géométrie en entrée n'est pas valide
   */
  public static IDirectPositionList makeDirectPositionList(CoordinateSequence[] jtsCoords) throws Exception {
    DirectPositionList list = new DirectPositionList();
    for (CoordinateSequence jtsCoord : jtsCoords) {
      list.add(OldJtsGeOxygene.makeDirectPosition(jtsCoord));
    }
    return list;
  }
}
