package fr.ign.cogit.simplu3d.util;

import fr.ign.common.extrusion.Extrusion2DObject;
import fr.ign.common.conversion.FromGeomToSurface;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

public class Extrusion {
  public MultiPolygon extrusion(Polygon pol, double hauteur) {
    Geometry geom = Extrusion2DObject.convertFromPolygon(pol, 0, hauteur);
    assert geom != null;
    return FromGeomToSurface.convertMSGeom(geom);
  }
}
