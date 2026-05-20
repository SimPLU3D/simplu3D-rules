/**
 * 
 * This software is released under the licence CeCILL
 * <p>
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
 * 
 *          Classe pour affecter un z à différents types d'objets
 * 
 */
package fr.ign.cogit.simplu3d.util;

import fr.ign.cogit.simplu3d.dao.AbstractRepository;
import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.Prescription;
import fr.ign.cogit.simplu3d.model.Road;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.model.UrbaZone;
import fr.ign.common.dtm.AbstractDTM;
import fr.ign.common.extrusion.Extrusion2DObject;
import org.locationtech.jts.geom.Geometry;

/**
 * 
 * Assign Z to features according to DTM
 * 
 * @author MBrasebin
 *
 */
public class AssignZ {
  // IF no DTM is used a default Z can be set
  public static double DEFAULT_Z = 0;

  public static void toParcelle(SpatialRepository<CadastralParcel> parcelles, AbstractDTM dtm, boolean sursampled) {
    boolean isZSet = (dtm != null);
    for (CadastralParcel p : parcelles.findAll()) {
      if (isZSet) {
        Geometry geom = dtm.mapGeom(p.getGeometry(), 0, true, sursampled);
        p.setGeometry(geom);
      } else {
        p.setGeometry(Extrusion2DObject.convertFromGeometry(p.getGeometry(), DEFAULT_Z, DEFAULT_Z));
      }
      for (ParcelBoundary b : p.getBoundaries()) {
        if (isZSet) {
          Geometry geomB = dtm.mapGeom(b.getGeometry(), 0, true, sursampled);
          if(geomB.isEmpty()){
            geomB = dtm.mapGeom(b.getGeometry(), 0, true, sursampled);
          }
          b.setGeometry(geomB);
        } else {
          b.setGeometry(Extrusion2DObject.convertFromGeometry(b.getGeometry(), DEFAULT_Z, DEFAULT_Z));
        }
      }
    }
  }

  public static void toSousParcelle(SpatialRepository<SubParcel> parcelles,
      AbstractDTM dtm, boolean sursampled) {

    boolean isZSet = (dtm != null);

    for (SubParcel p : parcelles.findAll()) {

      if (isZSet) {
        Geometry geom = dtm.mapGeom(p.getGeometry(), 0, true, sursampled);
        p.setGeometry(geom);
      } else {
        p.setGeometry(Extrusion2DObject.convertFromGeometry(p.getGeometry(), DEFAULT_Z,
            DEFAULT_Z));

      }

    }

  }

  public static void toVoirie(
          SpatialRepository<Road> voiries,
          AbstractDTM dtm,
          boolean sursampled) {
    boolean isZSet = (dtm != null);
    for (Road z : voiries.findAll()) {
      if (isZSet) {
        Geometry geom = dtm.mapGeom(z.getGeometry(), 0, true, sursampled);
        z.setGeometry(geom);
      } else {
        z.setGeometry(Extrusion2DObject.convertFromGeometry(z.getGeometry(), DEFAULT_Z,
            DEFAULT_Z));
      }
    }
  }

  public static void toZone(
          AbstractRepository<UrbaZone> zones,
          AbstractDTM dtm,//TODO should we use the dtm?
          boolean sursampled) {
    for (UrbaZone z : zones.findAll()) {
      z.setGeometry(Extrusion2DObject.convertFromGeometry(z.getGeometry(), DEFAULT_Z, DEFAULT_Z));
    }
  }

  public static void toPrescriptions(
          SpatialRepository<Prescription> prescriptions,
          AbstractDTM dtm,
          boolean sursampled) {
    boolean isZSet = (dtm != null);
    for (Prescription a : prescriptions.findAll()) {
      if (isZSet) {
        Geometry geom = dtm.mapGeom(a.getGeometry(), 0, true, sursampled);
        a.setGeometry(geom);
      } else {
        a.setGeometry(Extrusion2DObject.convertFromGeometry(a.getGeometry(), DEFAULT_Z, DEFAULT_Z));
      }
    }
  }
}
