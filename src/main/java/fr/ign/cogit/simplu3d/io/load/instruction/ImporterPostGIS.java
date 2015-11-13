package fr.ign.cogit.simplu3d.io.load.instruction;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;

public class ImporterPostGIS {

  public static IFeatureCollection<UrbaZone> importZoneUrba(
      IFeatureCollection<IFeature> featZone) {

    IFeatureCollection<UrbaZone> featZoneOut = new FT_FeatureCollection<>();

    for (IFeature feat : featZone) {
      UrbaZone z = new UrbaZone(feat.getGeom());

      // Here, it is necessary to define all the parameters
      z.setDestdomi(feat.getAttribute(
          ParametersInstructionPG.ATT_ZONE_URBA_DESTDOMI).toString());

    }

    return featZoneOut;

  }

  public static IFeatureCollection<SubParcel> importSubParcel(
      IFeatureCollection<IFeature> featParcel) {

    IFeatureCollection<SubParcel> featSubParcelOut = new FT_FeatureCollection<>();

    for (IFeature feat : featParcel) {
      SubParcel sp = new SubParcel(feat.getGeom());
    }

    return featSubParcelOut;

  }

}
