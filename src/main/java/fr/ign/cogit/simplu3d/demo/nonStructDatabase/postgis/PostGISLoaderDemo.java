package fr.ign.cogit.simplu3d.demo.nonStructDatabase.postgis;

import fr.ign.cogit.simplu3d.io.nonStructDatabase.postgis.LoaderPostGIS;
import fr.ign.cogit.simplu3d.model.*;
import fr.ign.common.io.AttributeManager;
import fr.ign.common.util.GeomUtils;
import fr.ign.common.io.ShapefileWriter;
import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;

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

    logger.info("Nombre de zones dans le PLU : " + env.getUrbaZones().size());

    Collection<ParcelBoundary> bordures_translated = new ArrayList<>();
    Collection<ParcelBoundary> bordures = new ArrayList<>();

    int count = 0;
    logger.info("nb Parcelles : " + env.getCadastralParcels().size());
    for (BasicPropertyUnit bPU : env.getBpU()) {
      for (CadastralParcel sp : bPU.getCadastralParcels()) {
        count = count + sp.getBoundaries().size();
        Coordinate centroidParcel = sp.getGeometry().getCentroid().getCoordinate();

        AttributeManager.addAttribute(sp, "ID", sp.getId(), "Integer");//FIXME
        AttributeManager.addAttribute(sp, "NBBord", sp.getBoundaries().size(), "Integer");
        AttributeManager.addAttribute(sp, "NBBat", bPU.getBuildings().size(), "Integer");
        for (ParcelBoundary b : sp.getBoundaries()) {
          bordures.add(b);
          AttributeManager.addAttribute(b, "ID", b.getId(), "Integer");//FIXME
          AttributeManager.addAttribute(b, "Type", b.getParcelBoundaryType(), "Integer");
          AttributeManager.addAttribute(b, "IDPar", sp.getId(), "Integer");//FIXME
          if (b.getFeatAdj() != null) {
            if (b.getFeatAdj() instanceof CadastralParcel) {
              AttributeManager.addAttribute(b, "Adj", ((CadastralParcel) b.getFeatAdj()).getId(), "Integer");//FIXME
            } else if (b.getFeatAdj() instanceof Road) {
              AttributeManager.addAttribute(b, "Adj", ((Road) b.getFeatAdj()).getId(), "Integer");//FIXME
            }
          } else {
            AttributeManager.addAttribute(b, "Adj", 0, "Integer");
          }
          Coordinate centroidGeom = b.getGeometry().getCoordinates()[0];
          Vecteur v = new Vecteur(centroidParcel, centroidGeom);
          Vecteur v2 = new Vecteur(b.getGeometry().getCoordinates()[0], b.getGeometry()
              .getCoordinates()[b.getGeometry().getCoordinates().length - 1]);
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
          AttributeManager.addAttribute(feat, "Type", b.getParcelBoundaryType(), "Integer");
          bordures_translated.add(feat);
        }
      }
    }
    logger.info("Nombre de bordures" + count);
    // Export des parcelles
    ShapefileWriter.write(env.getCadastralParcels(), folderOut + "parcelles.shp");
    ShapefileWriter.write(bordures, folderOut + "bordures.shp");
    ShapefileWriter.write(bordures_translated, folderOut + "bordures_translated.shp");
    //ShapefileWriter.write(featC, folderOut + "buffer.shp");
    logger.info("Nombre de bpu : " + env.getBpU().size());
    ShapefileWriter.write(env.getBpU(), folderOut + "bpu.shp");
    /*
     * IFeatureCollection<Alignement> featAL = new
     * FT_FeatureCollection<Alignement>();
     * 
     * 
     * 
     * for (Alignement a : env.getAlignements()) {
     * 
     * if (a != null) { featAL.add(a);
     * 
     * AttributeManager.addAttribute(a, "ID", a.getId(), "Integer"); }
     */
    logger.info("Sous Parcelles  " + env.getSubParcels().size());
    for (SubParcel sp : env.getSubParcels()) {
      AttributeManager.addAttribute(sp, "Test", 0, "Integer");
      AttributeManager.addAttribute(sp, "NB Bat", sp.getBuildingsParts().size(), "Integer");
    }
    ShapefileWriter.write(env.getSubParcels(), folderOut + "sousParcelles.shp");
    Collection<BuildingPart> featToits = new ArrayList<>();
    logger.info("NB emprise " + env.getBuildings().size());
    for (Building b : env.getBuildings()) {
      BuildingPart roof = new BuildingPart();
      roof.setGeometry(b.getFootprint());
      featToits.add(roof);//TODO better class?
    }
    ShapefileWriter.write(featToits, folderOut + "emprise.shp");
    Collection<BuildingPart> featFaitage = new ArrayList<>();
    for (Building b : env.getBuildings()) {
      BuildingPart faitage = new BuildingPart();
      faitage.setGeometry(b.getRoof().getRoofing());
      featFaitage.add(faitage);//TODO better class?
    }
    logger.info("Faitages : " + featFaitage.size());
    ShapefileWriter.write(featFaitage, folderOut + "pignon.shp");
    Collection<Road> featRoute = new ArrayList<>();
    for (Road r : env.getRoads()) {
      AttributeManager.addAttribute(r, "Nom", r.getRoadName(), "String");
      featRoute.add(r);
    }
    ShapefileWriter.write(featRoute, folderOut + "roads.shp");
    Collection<CadastralParcel> featOutTestCons = new ArrayList<>();
    for (CadastralParcel sp : env.getCadastralParcels()) {
      featOutTestCons.add(new CadastralParcel((MultiPolygon) sp.getConsLine()));//FIXME
      logger.info("Boundaries " + sp.getBoundaries().size());
    }
    ShapefileWriter.write(featOutTestCons, folderOut + "featConsF.shp");
    logger.info("Chat qui râle");
  }
  /*
   * 
   * 
   * 
   * 
   * System.out.println("Nombre d'alignements concernés" + featAL.size());
   * ShapefileWriter.write(featAL, folderOut + "alignements.shp");
   */
}
