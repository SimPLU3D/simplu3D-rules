/**
 * 
 * This software is released under the licence CeCILL
 *<p>
 * see LICENSE.TXT
 * <p>
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
package fr.ign.cogit.simplu3d.io.feature;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.model.Road;
import fr.ign.common.equation.ApproximatedPlanEquation;
import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.analysis.Proximity;
import fr.ign.common.util.Vecteur;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.FeatureCollection;
import org.locationtech.jts.geom.*;

/**
 * Reader for ROAD.shp
 * 
 * @author MBrasebin
 * @author MBorne
 *
 */
public class RoadReader extends AbstractFeatureReader<Road> {

  public static final String ATT_NOM_RUE;// = "NOM_RUE_G";
  public static final String ATT_LARGEUR;// = "LARGEUR";
  public static final String ATT_TYPE;// = "NATURE";

  private static final double epsilon = 0.01;

  static {
    ATT_NOM_RUE = AttribNames.getATT_NOM_RUE();
    ATT_LARGEUR = AttribNames.getATT_LARGEUR();
    ATT_TYPE = AttribNames.getATT_TYPE_ROAD();
  }
  public RoadReader() {
    super(new FeatureType("Road"));
    this.featureType.setGeometryType("MultiPolygon");
    this.featureType.getAttributes().put(ATT_TYPE, "String");
    this.featureType.getAttributes().put(ATT_NOM_RUE, "String");
    this.featureType.getAttributes().put(ATT_LARGEUR, "Double");
  }

  @Override
  public Road read(SimpleFeature feature) {
    Road result = new Road();
    result.setId(feature.getID());
    result.setRoadName(readStringAttribute(feature, ATT_NOM_RUE));
    Double width = readDoubleAttribute(feature, ATT_LARGEUR);
    if (width == null) {
      return null;
    }
    result.setWidth(width);
    result.setAttribute(ATT_TYPE, readStringAttribute(feature, ATT_TYPE));
    List<String> usages = new ArrayList<>();
    usages.add(readStringAttribute(feature, ATT_TYPE));
    result.setUsages(usages);
    // read axis...
    Geometry geom = (Geometry) feature.getDefaultGeometry();
    MultiLineString axe = null;
    if (geom instanceof LineString c) {
        axe = c.getFactory().createMultiLineString(new LineString[]{c});
    } else if (geom instanceof MultiLineString) {
      axe = (MultiLineString) geom;
    }
    if (axe == null) {
      throw new RuntimeException(
          "Error in Voirie Importer axe is not a ILineString");
    }
    result.setAxis(axe);
    // build surface geometry...
    // TODO epsilon check?
    if (result.getWidth() < 0.0) {
      throw new RuntimeException("Unsupported Road " + ATT_LARGEUR
          + " (0.0) for feature " + feature.getID());
    }
    if (result.getWidth() == 0) {
      result.setWidth(epsilon);
    }
    Geometry obj = geom.buffer(result.getWidth());
    Coordinate[] dpl = obj.getCoordinates();
    Coordinate[] dplRoute = ((Geometry) feature.getDefaultGeometry()).getCoordinates();
    Proximity c = new Proximity();
    for (Coordinate coordinate : dpl) {
      c.nearest(coordinate, dplRoute);
      coordinate.setZ(c.getNearest().getZ());
    }
    ApproximatedPlanEquation eq = new ApproximatedPlanEquation(dpl);
    Vecteur normal = eq.getNormale();
    if (normal.getZ() < 0) {
      CoordinateArrays.reverse(dpl);
    }
    MultiPolygon surfVoie = FromGeomToSurface.convertMSGeom(obj);
    result.setGeometry(surfVoie);
    return result;
  }
  @Override
  public SpatialRepository<Road> create(FeatureCollection<SimpleFeatureType, SimpleFeature> features) {
    return new SpatialRepository<>(readAll(features), this.featureType, (features.getSchema() != null) ? features.getSchema().getCoordinateReferenceSystem() : null);
  }
}
