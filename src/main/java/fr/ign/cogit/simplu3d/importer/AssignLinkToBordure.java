/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
package fr.ign.cogit.simplu3d.importer;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.Road;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundaryType;


public class AssignLinkToBordure {

  public static void process(IFeatureCollection<CadastralParcel> cadastralParcels,
      IFeatureCollection<Road> voiries) {

    if (!cadastralParcels.hasSpatialIndex()) {
      cadastralParcels.initSpatialIndex(Tiling.class, false);
    }
    if (!voiries.hasSpatialIndex()) {
      voiries.initSpatialIndex(Tiling.class, false);
    }

    //TODO only find roads for boundaries of type ROAD
    for (CadastralParcel sP : cadastralParcels) {

      List<SpecificCadastralBoundary> bordures = sP.getBoundaries();

      for (SpecificCadastralBoundary b : bordures) {

        // 2 cas : c'est une bordure avec voirie
        if (b.getType() == SpecificCadastralBoundaryType.ROAD) {

          b.setRoad(retrieveVoirie(b, voiries));
          continue;
        }

      }

    }

  }

  private static Road retrieveVoirie(SpecificCadastralBoundary b,
      IFeatureCollection<Road> voiries) {

    IGeometry geomB = b.getGeom();

    IGeometry buffer = geomB.buffer(10);

    Collection<Road> collVoirie = voiries.select(buffer);

    if (collVoirie.size() == 0) {
      return null;
    }

    if (collVoirie.size() == 1) {

      return collVoirie.iterator().next();
    }

    IDirectPositionList dpl = geomB.coord();

    Vecteur v = new Vecteur(dpl.get(0), dpl.get(dpl.size() - 1));

    v.normalise();

    Iterator<Road> it = collVoirie.iterator();

    Road bestR = null;
    double bestScore = Double.NEGATIVE_INFINITY;

    while (it.hasNext()) {

      Road vCandidate = it.next();
      IGeometry geomB2 = b.getGeom();
      IDirectPositionList dpl2 = geomB2.coord();

      Vecteur v2 = new Vecteur(dpl2.get(0), dpl2.get(dpl2.size() - 1));
      v2.normalise();

      double cos = Math.abs(v.prodScalaire(v2));

      if (cos > Math.cos(Math.PI / 5)) {

        // à voir .....
        if (cos > bestScore) {
          bestR = vCandidate;
          bestScore = cos;

        }

      }

    }

    return bestR;

  }

}
