/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 * 
 *          Classe pour affecter un z à différents types d'objets
 * 
 */
package fr.ign.cogit.simplu3d.util;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.sig3d.semantic.AbstractDTM;
import fr.ign.cogit.simplu3d.model.Alignement;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.Road;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.model.UrbaZone;

/**
 * 
 * Assign Z to features according to DTM
 * 
 * @author MBrasebin
 *
 */
public class AssignZ {
  // IF no DTM is used a default Z can be set
  public static double DEFAULT_Z = 70;

  public static void toParcelle(IFeatureCollection<CadastralParcel> parcelles,
      AbstractDTM dtm, boolean sursampled) throws Exception {

    boolean isZSet = (dtm != null);

    for (CadastralParcel p : parcelles) {

      if (isZSet) {
        IGeometry geom = dtm.mapGeom(p.getGeom(), 0, true, sursampled);
        p.setGeom(geom);

      } else {

        p.setGeom(Extrusion2DObject.convertFromGeometry(p.getGeom(), DEFAULT_Z,
            DEFAULT_Z));

      }

      for (SpecificCadastralBoundary b : p.getSpecificCadastralBoundary()) {

        if (isZSet) {
          IGeometry geomB = dtm.mapGeom(b.getGeom(), 0, true, sursampled);
          
          if(geomB.isEmpty()){
            geomB = dtm.mapGeom(b.getGeom(), 0, true, sursampled);
          }
          
          b.setGeom(geomB);

        } else {
          b.setGeom(Extrusion2DObject.convertFromGeometry(b.getGeom(),
              DEFAULT_Z, DEFAULT_Z));

        }
        
      }

    }

  }

  public static void toSousParcelle(IFeatureCollection<SubParcel> parcelles,
      AbstractDTM dtm, boolean sursampled) throws Exception {

    boolean isZSet = (dtm != null);

    for (SubParcel p : parcelles) {

      if (isZSet) {
        IGeometry geom = dtm.mapGeom(p.getGeom(), 0, true, sursampled);
        p.setGeom(geom);
      } else {
        p.setGeom(Extrusion2DObject.convertFromGeometry(p.getGeom(), DEFAULT_Z,
            DEFAULT_Z));

      }

    }

  }

  public static void toVoirie(IFeatureCollection<Road> voiries,
      AbstractDTM dtm, boolean sursampled) throws Exception {

    boolean isZSet = (dtm != null);

    for (Road z : voiries) {

      if (isZSet) {

        IGeometry geom = dtm.mapGeom(z.getGeom(), 0, true, sursampled);
        z.setGeom(geom);
      } else {
        z.setGeom(Extrusion2DObject.convertFromGeometry(z.getGeom(), DEFAULT_Z,
            DEFAULT_Z));

      }

    }

  }

  public static void toZone(IFeatureCollection<UrbaZone> zones,
      AbstractDTM dtm, boolean sursampled) throws Exception {

    for (UrbaZone z : zones) {

      z.setGeom(Extrusion2DObject.convertFromGeometry(z.getGeom(), DEFAULT_Z,
          DEFAULT_Z));

    }

  }

  public static void toAlignement(
      IFeatureCollection<Alignement> alignementColl, AbstractDTM dtm,
      boolean sursampled) throws Exception {

    boolean isZSet = (dtm != null);

    for (Alignement a : alignementColl) {
      if (isZSet) {

        IGeometry geom = dtm.mapGeom(a.getGeom(), 0, true, sursampled);
        a.setGeom(geom);
      } else {
        a.setGeom(Extrusion2DObject.convertFromGeometry(a.getGeom(), DEFAULT_Z,
            DEFAULT_Z));
      }
    }

  }
}
