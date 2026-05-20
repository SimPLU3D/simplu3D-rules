package fr.ign.cogit.simplu3d.util;

import java.util.ArrayList;
import java.util.List;

/*
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
*/
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;
import fr.ign.common.conversion.FromGeomToLineString;
import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.extrusion.Extrusion2DObject;
import fr.ign.common.util.GeomUtils;
import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.*;

public class SimpleBandProduction {

  List<MultiPolygon> lOut = new ArrayList<>();
  MultiLineString iMSRoad = null;
  private MultiLineString lineRoad = null;

  public SimpleBandProduction(BasicPropertyUnit bPU, ZoneRegulation r1,
      ZoneRegulation r2) {

    // On récupère le polygone surlequel on va faire la découpe
    Polygon pol_BPU = bPU.getPol2D();

    // On créé la géométrie des limites donnant sur la voirie

    List<ParcelBoundary> lBordureVoirie = bPU.getCadastralParcels().get(0).getBoundariesByType(ParcelBoundaryType.ROAD);
    List<LineString> lineStrings = new ArrayList<>();
    for (ParcelBoundary sc : lBordureVoirie) {
      lineStrings.add((LineString) sc.getGeometry());
    }
    iMSRoad = pol_BPU.getFactory().createMultiLineString(lineStrings.toArray(new LineString[0]));

    // System.out.println("size road" + iMSRoad.size());

    double profBande = r1.getBande();
    // BANDE Profondeur de la bande principale x > 0 profondeur de la bande
    // par rapport à la voirie
    MultiPolygon iMSBande1;
    if (profBande == 0) {
      iMSBande1 = FromGeomToSurface.convertMSGeom(pol_BPU);
    } else {
      iMSBande1 = FromGeomToSurface.convertMSGeom(pol_BPU.intersection(iMSRoad.buffer(profBande)));
    }

    MultiPolygon iMSBande2 = null;

    // Idem s'il y a un règlement de deuxième bande
    if (r2 != null && !iMSRoad.isEmpty()) {

      iMSBande2 = FromGeomToSurface
          .convertMSGeom(pol_BPU.difference(iMSRoad.buffer(profBande)));

      r2.setGeomBande(iMSBande2);

    }

    Geometry geom = null;
    if (!iMSBande1.isEmpty()) {
      geom = Extrusion2DObject.convertFromMultiPolygon(iMSBande1, 0, 0);
    }

    Geometry geom2 = null;
    if (iMSBande2 != null) {
      geom2 = Extrusion2DObject.convertFromMultiPolygon(iMSBande2, 0, 0);
    }

    // 2 bandes
    assert geom != null;
    lOut.add(FromGeomToSurface.convertMSGeom(geom));
    assert geom2 != null;
    lOut.add(FromGeomToSurface.convertMSGeom(geom2));

    r1.setGeomBande(iMSBande1);

    // Si l'article 6 demande qu'un alignementsoit respecté, on l'active
    double rArt6 = r1.getArt6();
    if (rArt6 != 99.0 && rArt6 != 88.0) {

      if (rArt6 == 0) {
        // Soit le long de la limite donnant sur la voirie
        lineRoad = (MultiLineString) (iMSRoad.copy());
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
   */
  private MultiLineString shiftRoad(BasicPropertyUnit bPU, double valShiftB) {
    List<LineString> lineStrings = new ArrayList<>();
    Coordinate centroidParcel = bPU.getPol2D().getCentroid().getCoordinate();
    for (int index = 0; index < iMSRoad.getNumGeometries(); index++) {
      LineString oC = (LineString) iMSRoad.getGeometryN(index);
      if (oC.isEmpty()) {
        continue;
      }
      Coordinate centroidGeom = oC.getCoordinateN(0);
      Vecteur v = new Vecteur(centroidParcel, centroidGeom);
      Vecteur v2 = new Vecteur(oC.getCoordinateN(0),
          oC.getCoordinateN(oC.getCoordinates().length - 1));
      v2.setZ(0);
      v2.normalise();
      Vecteur vOut = v2.prodVectoriel(new Vecteur(0, 0, 1));
      Geometry geom = oC.copy();
      if (v.prodScalaire(vOut) < 0) {
        vOut = vOut.multConstante(-1);
      }
      Geometry geom2 = GeomUtils.translate(geom,valShiftB * vOut.getX(),
          valShiftB * vOut.getY(), 0);
      assert geom2 != null;
      if (!geom2.intersects(bPU.getGeometry())) {
        geom2 = GeomUtils.translate(geom, -valShiftB * vOut.getX(),
            -valShiftB * vOut.getY(), 0);
      }
      lineStrings.addAll(FromGeomToLineString.convert(geom2));
    }
    return bPU.getGeometry().getFactory().createMultiLineString(lineStrings.toArray(new LineString[0]));
  }

  public MultiLineString getLineRoad() {
    return this.lineRoad;
  }
}
