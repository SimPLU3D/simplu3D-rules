package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.util.Iterator;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;

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
 **/
public class AssignBuildingPartToSubParcel {

    /**
     * Aire minimale pour considérée un polygone comme attaché à une parcelle
     */
    public static double RATIO_MIN = 0.8;

    public static int ASSIGN_METHOD = 0;

    public static void assign(IFeatureCollection<Building> buildings,
            IFeatureCollection<BasicPropertyUnit> collBPU) {

        for (Building b : buildings) {

            IGeometry poly = b.getFootprint();

            // Etape3 : on associe le bâtiment à la sous parcelles
            IFeatureCollection<IFeature> featTemp = new FT_FeatureCollection<IFeature>();

            for (BasicPropertyUnit bPU : collBPU) {

                IMultiSurface<IOrientableSurface> iMSTemp = new GM_MultiSurface<IOrientableSurface>();

                for (CadastralParcel bP : bPU.getCadastralParcel()) {
                    iMSTemp.addAll(FromGeomToSurface.convertGeom(bP.getGeom()));

                }
                featTemp.add(new DefaultFeature(iMSTemp));
            }

            if (!featTemp.hasSpatialIndex()) {

                featTemp.initSpatialIndex(Tiling.class, false);

            }

            assignSimpleMethod(b, featTemp, collBPU);

        }
    }

    private static boolean assignSimpleMethod(Building b,
            IFeatureCollection<IFeature> featTemp,
            IFeatureCollection<BasicPropertyUnit> collBPU) {

        // On récupère l'emprise du bâtiment
        IPolygon poly = (IPolygon) b.getFootprint();

        // On trouve les BPU qui l'intersecte dans la liste temporaire
        Iterator<IFeature> itSP = featTemp.select(poly).iterator();

        boolean isAttached = false;

        // On peut avoir plusieurs objets qui intersectent la géométrie du
        // bâtiment
        while (itSP.hasNext()) {
            IFeature sp = itSP.next();

            // en commentaire avant
            double aireEmprise = poly.area();

            // en commentaire avant
            double area = (poly.intersection(sp.getGeom())).area();

            // en commentaire avant
            if (area / aireEmprise > RATIO_MIN) {

                int index = featTemp.getElements().indexOf(sp);

                collBPU.get(index).getBuildings().add(b);
                collBPU.get(index).getCadastralParcel().get(0).getSubParcel()
                        .get(0).getBuildingsParts().add(b);

                isAttached = true;

            }

        }

        return isAttached;

    }

    private static boolean assignCompleteMethod(Building b,
            IFeatureCollection<IFeature> featTemp,
            IFeatureCollection<BasicPropertyUnit> collBPU) {
        // On récupère l'emprise du bâtiment
        IPolygon poly = (IPolygon) b.getFootprint();

        // On trouve les BPU qui l'intersecte dans la liste temporaire
        Iterator<IFeature> itSP = featTemp.select(poly).iterator();

        boolean isAttached = false;

        // On peut avoir plusieurs objets qui intersectent la géométrie du
        // bâtiment
        while (itSP.hasNext()) {
            
            //On traite chaque BPU (en gros parcelle) qui intersecte le bâtiment
            IFeature sp = itSP.next();

            // en commentaire avant
            double aireEmprise = poly.area();

            // en commentaire avant
            double area = (poly.intersection(sp.getGeom())).area();

            // en commentaire avant
            if (area / aireEmprise > RATIO_MIN) {

                
                
                int index = featTemp.getElements().indexOf(sp);
                //Assignation bâtiment BDPU
                collBPU.get(index).getBuildings().add(b);
                
                //Quand on va assigner les sous parties, il va falloir découper le bâtiment
                
                //Puis on assigne
                collBPU.get(index).getCadastralParcel().get(0).getSubParcel()
                        .get(0).getBuildingsParts().add(b);
                
                //Il faut vérifier et recompter le nombre de partie par bâtiment

                isAttached = true;

            }

        }

        return isAttached;
    }

}
