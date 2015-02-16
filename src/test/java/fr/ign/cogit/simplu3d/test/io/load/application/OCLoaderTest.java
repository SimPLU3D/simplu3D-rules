package fr.ign.cogit.simplu3d.test.io.load.application;

import java.io.File;

import org.junit.Test;

import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.java.internal.modelinstance.JavaModelInstance;
import tudresden.ocl20.pivot.standalone.facade.StandaloneFacade;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ITriangle;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Triangle;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.simplu3d.importer.model.ImportModelInstanceEnvironnement;
import fr.ign.cogit.simplu3d.model.application.SubParcel;

public class OCLoaderTest {

  static File modelFile;

  static File oclConstraints;

  @Test
  public void testImport() {

    oclConstraints = new File(
        "src/test/resources/ocl/ocl/simple_allConstraintsThese.ocl");

    try {

      System.out.println("*******************************************");
      System.out.println("***********Import environnement************");
      System.out.println("*******************************************");

      SubParcel p = new SubParcel();

      IDirectPosition dp1 = new DirectPosition(1, 0, 0);

      IDirectPosition dp2 = new DirectPosition(1, 15, 0);

      IDirectPosition dp3 = new DirectPosition(0, 0, 0);

      IMultiSurface<IOrientableSurface> ims = new GM_MultiSurface<IOrientableSurface>();

      ITriangle t = new GM_Triangle(dp1, dp2, dp3);
      ITriangle t2 = new GM_Triangle(dp1, dp2, dp1);

      ims.add(t);
      ims.add(t2);

      p.setGeom(ims);

      System.out.println("*******************************************");
      System.out.println("************Import modèle******************");
      System.out.println("*******************************************");

      IModel model = ImportModelInstanceEnvironnement
          .getModel("target/classes/fr/ign/cogit/simplu3d/importer/model/ModelProviderClass.class");

      System.out.println("*******************************************");
      System.out.println("****Peuplement du modèle en instances******");
      System.out.println("*******************************************");

      IModelInstance modelInstance = new JavaModelInstance(model);
      modelInstance.addModelInstanceElement(p);
      modelInstance.addModelInstanceElement(t);

      System.out.println("*******************************************");
      System.out.println("****Chargement des contraintes OCL*********");
      System.out.println("*******************************************");

      /* List<Constraint> constraintList = */StandaloneFacade.INSTANCE
          .parseOclConstraints(model, oclConstraints);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
