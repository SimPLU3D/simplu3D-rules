package fr.ign.cogit.simplu3d.test.checker;

import java.util.ArrayList;

import org.junit.Test;

import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;

import com.ibm.icu.impl.Assert;

import fr.ign.cogit.simplu3d.checker.ExhaustiveChecker;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.test.io.load.application.LoaderSimpluSHPTest;

public class TestOCLConstraint {

  @Test
  public void testInterpretation() {
    Environnement env = LoaderSimpluSHPTest.getENVTest();

    for (BasicPropertyUnit bPU : env.getBpU()) {
      ExhaustiveChecker vFR = new ExhaustiveChecker(bPU);

      vFR.check(new ArrayList<IModelInstanceObject>());

    }

    Assert.assrt(true);
  }
}
