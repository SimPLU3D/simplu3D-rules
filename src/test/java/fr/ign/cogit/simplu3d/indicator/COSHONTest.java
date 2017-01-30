package fr.ign.cogit.simplu3d.indicator;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.junit.Test;

import fr.ign.cogit.geoxygene.sig3d.indicator.COSCalculation;
import fr.ign.cogit.geoxygene.sig3d.indicator.COSCalculation.METHOD;
import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import junit.framework.Assert;
import junit.framework.TestCase;

public class COSHONTest extends TestCase {

	private static Logger logger = Logger.getLogger(COSHONTest.class);
	
	@Test
	public void testCOS() throws FileNotFoundException {
		BasicPropertyUnit sp = DemoEnvironmentProvider.getDefaultEnvironment().getBpU().get(0);

		double cos1 = COSCalculation.assess(sp.getBuildings(), METHOD.SIMPLE, sp.getArea());
		double cos2 = COSCalculation.assess(sp.getBuildings(), METHOD.FLOOR_CUT, sp.getArea());

		double epsilon = 0.00001;

		Assert.assertTrue(Math.abs(1.5195472566046677 - cos1) < epsilon);
		Assert.assertTrue(Math.abs(1.5194576281243275 - cos2) < epsilon);
	}

}
