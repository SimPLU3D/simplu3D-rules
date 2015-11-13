package fr.ign.cogit.simplu3d.test.io.load.application;

import java.io.FileNotFoundException;

import junit.framework.Assert;

import org.junit.Test;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.simplu3d.exe.LoadDefaultEnvironment;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.PLU;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;

/**
 * 
 * @author MBrasebin
 * 
 */
public class LoaderSimpluSHPTest {

  @Test
  public void testImport() throws FileNotFoundException {

    Environnement env = LoadDefaultEnvironment.getENVDEF();

    PLU plu = env.getPlu();

    Assert.assertNotNull(plu);
    Assert.assertEquals(1, plu.getlUrbaZone().size());

    IFeatureCollection<SpecificCadastralBoundary> bordures = new FT_FeatureCollection<SpecificCadastralBoundary>();

    int count = 0;
    Assert.assertEquals("Toutes les parcelles sont chargées.", 19, env
        .getParcelles().size());

    Assert.assertNotNull(env.getBpU());

    Assert.assertEquals("Toutes les unités foncières sont chargées.", 19, env
        .getBpU().size());

    for (BasicPropertyUnit bPU : env.getBpU()) {

      Assert.assertNotNull(bPU.getCadastralParcel());
      Assert.assertFalse(bPU.getCadastralParcel().isEmpty());

      for (CadastralParcel sp : bPU.getCadastralParcel()) {

        count = count + sp.getSpecificCadastralBoundary().size();

        Assert.assertNotNull(sp.getSpecificCadastralBoundary());
        Assert.assertFalse(sp.getSpecificCadastralBoundary().isEmpty());

        for (SpecificCadastralBoundary b : sp.getSpecificCadastralBoundary()) {
          bordures.add(b);

        }

      }
    }

    Assert.assertEquals("Toutes les limites séparatives sont chargées.", 140,
        count);

    Assert.assertEquals("Toutes les sous parcelles sont chargées.", 19, env
        .getSubParcels().size());

    IFeatureCollection<IFeature> featToits = new FT_FeatureCollection<IFeature>();

    Assert.assertEquals("Les emprises sont générées.", 40, env.getBuildings()
        .size());

    for (AbstractBuilding b : env.getBuildings()) {
      featToits.add(new DefaultFeature(b.getFootprint()));
    }

    IFeatureCollection<IFeature> featFaitage = new FT_FeatureCollection<IFeature>();
    for (AbstractBuilding b : env.getBuildings()) {
      featFaitage.add(new DefaultFeature(b.getToit().getRoofing()));
    }

    Assert.assertEquals("Les faîtages sont générés.", 40, featFaitage.size());

    Assert.assertNotNull(env.getRoads());
    Assert.assertFalse(env.getRoads().isEmpty());

    Assert.assertTrue("Le test est un succès", true);
  }

}
