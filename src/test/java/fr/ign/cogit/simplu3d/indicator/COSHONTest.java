package fr.ign.cogit.simplu3d.indicator;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.junit.Test;

import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.indicator.COSCalculation.METHOD;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import junit.framework.Assert;
import junit.framework.TestCase;

public class COSHONTest extends TestCase {

	private static Logger logger = Logger.getLogger(COSHONTest.class);
	
	@Test
	public void testCOS() throws FileNotFoundException {
		BasicPropertyUnit sp = DemoEnvironmentProvider.getDefaultEnvironment().getBpU().get(0);

		double cos1 = COSCalculation.assess(sp, METHOD.SIMPLE);
		double cos2 = COSCalculation.assess(sp, METHOD.FLOOR_CUT);

		double epsilon = 0.00001;

		Assert.assertTrue(Math.abs(1.5195472566046677 - cos1) < epsilon);
		Assert.assertTrue(Math.abs(1.5194576281243275 - cos2) < epsilon);
	}

}
