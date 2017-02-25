package fr.ign.cogit.simplu3d.util;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.convert.FromGeomToLineString;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.Regulation;

public class SimpleBandProduction {

  List<IMultiSurface<IOrientableSurface>> lOut = new ArrayList<>();
  IMultiCurve<IOrientableCurve> iMSRoad = new GM_MultiCurve<>();
  private IMultiCurve<IOrientableCurve> lineRoad = null;

  @SuppressWarnings("unchecked")
  public SimpleBandProduction(BasicPropertyUnit bPU, Regulation r1,
      Regulation r2) {

    // On récupère le polygone surlequel on va faire la découpe
    IPolygon pol_BPU = bPU.getpol2D();

    // On créé la géométrie des limites donnant sur la voirie

    List<ParcelBoundary> lBordureVoirie = bPU.getCadastralParcels().get(0)
        .getBoundariesByType(ParcelBoundaryType.ROAD);
    for (ParcelBoundary sc : lBordureVoirie) {
      iMSRoad.add((IOrientableCurve) sc.getGeom());
    }

    // System.out.println("size road" + iMSRoad.size());

    double profBande = r1.getBande();
    // BANDE Profondeur de la bande principale x > 0 profondeur de la bande
    // par rapport à la voirie
    IMultiSurface<IOrientableSurface> iMSBande1;
    if (profBande == 0) {
      iMSBande1 = FromGeomToSurface.convertMSGeom(pol_BPU);
    } else {
      iMSBande1 = FromGeomToSurface
          .convertMSGeom(pol_BPU.intersection(iMSRoad.buffer(profBande)));
    }

    IMultiSurface<IOrientableSurface> iMSBande2 = null;

    // Idem s'il y a un règlement de deuxième bande
    if (r2 != null && !iMSRoad.isEmpty()) {

      iMSBande2 = FromGeomToSurface
          .convertMSGeom(pol_BPU.difference(iMSRoad.buffer(profBande)));

      r2.setGeomBande(iMSBande2);

    }

    IGeometry geom = null;
    if (!iMSBande1.isEmpty()) {
      geom = Extrusion2DObject.convertFromMultiPolygon(iMSBande1, 0, 0);
    }

    IGeometry geom2 = null;
    if (iMSBande2 != null) {
      geom2 = Extrusion2DObject.convertFromMultiPolygon(iMSBande2, 0, 0);
    }

    // 2 bandes
    lOut.add(FromGeomToSurface.convertMSGeom(geom));
    lOut.add(FromGeomToSurface.convertMSGeom(geom2));

    r1.setGeomBande(iMSBande1);

    // Si l'article 6 demande qu'un alignementsoit respecté, on l'active
    double rArt6 = r1.getArt_6();
    if (rArt6 != 99.0 && rArt6 != 88.0) {

      if (rArt6 == 0) {
        // Soit le long de la limite donnant sur la voirie
        lineRoad = (IMultiCurve<IOrientableCurve>) (iMSRoad.clone());
      } else {
        // Soit en appliquant un petit décalage
        lineRoad = shiftRoad(bPU, rArt6);
      }
    } else {

    }
  }

  /**
   * Méthode permettant de produire une multicurve en reculant vers l'intérieur
   * de l'unité foncière (bPU) les limites donnant sur la voirie d'une distance
   * (valShiftB)
   * 
   * @param bPU
   * @param valShiftB
   * @return
   */
  private IMultiCurve<IOrientableCurve> shiftRoad(BasicPropertyUnit bPU,
      double valShiftB) {

    IMultiCurve<IOrientableCurve> iMS = new GM_MultiCurve<>();

    IDirectPosition centroidParcel = bPU.getpol2D().centroid();

    for (IOrientableCurve oC : iMSRoad) {

      if (oC.isEmpty()) {
        continue;
      }

      IDirectPosition centroidGeom = oC.coord().get(0);
      Vecteur v = new Vecteur(centroidParcel, centroidGeom);

      Vecteur v2 = new Vecteur(oC.coord().get(0),
          oC.coord().get(oC.coord().size() - 1));
      v2.setZ(0);
      v2.normalise();

      Vecteur vOut = v2.prodVectoriel(new Vecteur(0, 0, 1));

      IGeometry geom = ((IGeometry) oC.clone());

      if (v.prodScalaire(vOut) < 0) {
        vOut = vOut.multConstante(-1);
      }

      IGeometry geom2 = geom.translate(valShiftB * vOut.getX(),
          valShiftB * vOut.getY(), 0);

      if (!geom2.intersects(bPU.getGeom())) {
        geom2 = geom.translate(-valShiftB * vOut.getX(),
            -valShiftB * vOut.getY(), 0);
      }

      iMS.addAll(FromGeomToLineString.convert(geom2));

    }

    return iMS;

  }

  public IMultiCurve<IOrientableCurve> getLineRoad() {
    return this.lineRoad;
  }

}
