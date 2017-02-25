package fr.ign.cogit.simplu3d.util;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;

public class Extrusion {

  public IMultiSurface<IOrientableSurface> extrusion(IPolygon pol,
      double hauteur) {

    IGeometry geom = Extrusion2DObject.convertFromPolygon(pol, 0, hauteur);
    IMultiSurface<IOrientableSurface> ims = FromGeomToSurface.convertMSGeom(geom);
    return ims;
  }

}
