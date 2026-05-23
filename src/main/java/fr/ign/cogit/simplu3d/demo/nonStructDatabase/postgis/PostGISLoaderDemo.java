package fr.ign.cogit.simplu3d.demo.nonStructDatabase.postgis;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.io.feature.FeatureType;
import fr.ign.cogit.simplu3d.io.feature.ParcelBoundaryReader;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.postgis.LoaderPostGIS;
import fr.ign.cogit.simplu3d.model.*;
import fr.ign.common.util.GeomUtils;
import fr.ign.common.io.ShapefileWriter;
import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

/**
 *
 *        This software is released under the licence CeCILL
 * <p>
 *        see LICENSE.TXT
 * <p>
 *        <a href="http://www.cecill.info/">...</a>
 * <p>
 *
 *
 * copyright IGN
 *
 * @author Brasebin Mickaël
 *
 * @version 1.0
 **/
public class PostGISLoaderDemo {
  private static final Logger logger = Logger.getLogger(PostGISLoaderDemo.class.getName());
  public static void main(String[] args) throws Exception {
    String folder = "./ProjectT3/";//FIXME
    String folderOut = folder + "out/";
    double valShiftB = 1.5;
    LoaderPostGIS lP = new LoaderPostGIS(
            "localhost",
            "5432",
            "gtru",
            "postgres",
            "postgres");

    Environnement env = lP.load();

    UrbaDocument plu = env.getUrbaDocument();

    logger.info("Nombre de zones dans le PLU : " + env.getUrbaZones().count());

    Collection<ParcelBoundary> bordures_translated = new ArrayList<>();
    Collection<ParcelBoundary> bordures = new ArrayList<>();

    logger.info("nb Parcelles : " + env.getCadastralParcels().count());
    for (BasicPropertyUnit bPU : env.getBpU().findAll()) {
      for (CadastralParcel sp : bPU.getCadastralParcels()) {
        Coordinate centroidParcel = sp.getGeometry().getCentroid().getCoordinate();
        sp.setAttribute("NBBord", sp.getBoundaries().size());
        sp.setAttribute("NBBat", sp.getbPU().getBuildings().size());
        for (ParcelBoundary b : sp.getBoundaries()) {
          bordures.add(b);
          b.setAttribute("Type", b.getParcelBoundaryType());//FIXME use setParcelBoundaryType?
          b.setAttribute("IDPar", sp.getId());
          if (b.getFeatAdj() != null) {
            if (b.getFeatAdj() instanceof CadastralParcel) {
              b.setAttribute("Adj", ((CadastralParcel) b.getFeatAdj()).getId());
            } else if (b.getFeatAdj() instanceof Road) {
              b.setAttribute("Adj", ((Road) b.getFeatAdj()).getId());
            }
          } else {
            b.setAttribute("Adj", 0);
          }
          Coordinate centroidGeom = b.getGeometry().getCoordinates()[0];
          Vecteur v = new Vecteur(centroidParcel, centroidGeom);
          Vecteur v2 = new Vecteur(
                  b.getGeometry().getCoordinates()[0],
                  b.getGeometry().getCoordinates()[b.getGeometry().getCoordinates().length - 1]);
          v2.setZ(0);
          v2.normalise();
          Vecteur vOut = v2.prodVectoriel(new Vecteur(0, 0, 1));
          Geometry geom = b.getGeometry().copy();
          if (v.prodScalaire(vOut) < 0) {
            vOut = vOut.multConstante(-1);
          }
          Geometry geom2 = GeomUtils.translate(geom, valShiftB * vOut.getX(), valShiftB
              * vOut.getY(), 0);
          if (!geom2.intersects(sp.getGeometry())) {
            geom2 = GeomUtils.translate(geom, -valShiftB * vOut.getX(),
                    -valShiftB * vOut.getY(), 0);
          }
          ParcelBoundary feat = new ParcelBoundary(geom2);
          feat.setAttribute("Type", b.getParcelBoundaryType());
          bordures_translated.add(feat);
        }
      }
    }
    logger.info("Nombre de bordures" + bordures.size());
    // Export des parcelles
    // add the missing attributes
    env.getCadastralParcels().getFeatureType().addAttribute("NBBord", "Integer");
    env.getCadastralParcels().getFeatureType().addAttribute("NBBat", "Integer");
    ShapefileWriter.write(env.getCadastralParcels(), folderOut + "parcelles.shp");

    SpatialRepository<ParcelBoundary> boundaries = new ParcelBoundaryReader().create(bordures, env.getCadastralParcels().getCoordinateReferenceSystem());
    // add the missing attributes
    boundaries.getFeatureType().addAttribute("Type", "Integer");
    boundaries.getFeatureType().addAttribute("IDPar", "Integer");
    boundaries.getFeatureType().addAttribute("Adj", "Integer");
    ShapefileWriter.write(boundaries, folderOut + "bordures.shp");
    SpatialRepository<ParcelBoundary> boundaries_translated = new ParcelBoundaryReader().create(bordures_translated, null);
    ShapefileWriter.write(boundaries_translated, folderOut + "bordures_translated.shp");
    //ShapefileWriter.write(featC, folderOut + "buffer.shp");
    logger.info("Nombre de bpu : " + env.getBpU().count());
    ShapefileWriter.write(env.getBpU(), folderOut + "bpu.shp");
    logger.info("Sous Parcelles  " + env.getSubParcels().count());
    for (SubParcel sp : env.getSubParcels().findAll()) {
      sp.setAttribute("Test", 0);
      sp.setAttribute("NB Bat", sp.getBuildingsParts().size());
    }
    // add missing attributes
    env.getSubParcels().getFeatureType().addAttribute("Test", "Integer");
    env.getSubParcels().getFeatureType().addAttribute("NBBat", "Integer");
    ShapefileWriter.write(env.getSubParcels(), folderOut + "sousParcelles.shp");
    //roofs
    Collection<AbstractUrbanFeature> featToits = new ArrayList<>();
    logger.info("NB emprise " + env.getBuildings().count());
    for (Building b : env.getBuildings().findAll()) {
      AbstractUrbanFeature roof = new AbstractUrbanFeature();
      roof.setGeometry(b.getFootprint());
      featToits.add(roof);//TODO better class?
    }
    FeatureType roofFT = new FeatureType("Roof");
    roofFT.setGeometryType("Polygon");
    ShapefileWriter.write(featToits, roofFT, env.getBuildings().getCoordinateReferenceSystem(), folderOut + "emprise.shp");
    Collection<AbstractUrbanFeature> featFaitage = new ArrayList<>();
    for (Building b : env.getBuildings().findAll()) {
      AbstractUrbanFeature faitage = new AbstractUrbanFeature();
      faitage.setGeometry(b.getRoof().getRoofing());
      featFaitage.add(faitage);//TODO better class?
    }
    logger.info("Faitages : " + featFaitage.size());
    FeatureType ridgeFT = new FeatureType("Ridge");
    roofFT.setGeometryType("MultiLineString");
    ShapefileWriter.write(featFaitage, ridgeFT, env.getBuildings().getCoordinateReferenceSystem(), folderOut + "pignon.shp");
    ShapefileWriter.write(env.getRoads(), folderOut + "roads.shp");
    Collection<AbstractUrbanFeature> featOutTestCons = new ArrayList<>();
    for (CadastralParcel sp : env.getCadastralParcels().findAll()) {
      AbstractUrbanFeature feat = new AbstractUrbanFeature();
      feat.setGeometry(sp.getConsLine());
      featOutTestCons.add(feat);
      logger.info("Boundaries " + sp.getBoundaries().size());
    }
    FeatureType consFT = new FeatureType("ConsLine");
    consFT.setGeometryType("MultiLineString");
    ShapefileWriter.write(featOutTestCons, consFT, env.getCadastralParcels().getCoordinateReferenceSystem(), folderOut + "featConsF.shp");
    logger.info("Chat qui râle");
  }
}
