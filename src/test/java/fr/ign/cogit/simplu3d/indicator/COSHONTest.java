package fr.ign.cogit.simplu3d.indicator;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import fr.ign.common.analysis.parcel.COSCalculation;
import fr.ign.common.analysis.parcel.SHONCalculation;
import org.junit.Assert;

import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import junit.framework.TestCase;

public class COSHONTest extends TestCase {

	private static final Logger logger = Logger.getLogger(COSHONTest.class.getName());
	
	public void testCOS() throws FileNotFoundException {
		BasicPropertyUnit sp = DemoEnvironmentProvider.getDefaultEnvironment().getBpU().iterator().next();
		logger.fine("BPU=\n" + sp.getGeometry());
		double cos1 = COSCalculation.assess(sp.getBuildings(), SHONCalculation.METHOD.SIMPLE, sp.getArea());
		double cos2 = COSCalculation.assess(sp.getBuildings(), SHONCalculation.METHOD.FLOOR_CUT, sp.getArea());
		logger.fine("cos1 = " + cos1);
		logger.fine("cos2 = " + cos2);
		logger.fine("cos1 diff = " + Math.abs(cos1 - 1.5195472566046677));
		logger.fine("cos2 diff = " + Math.abs(cos2 - 1.5194576281243275));
		Assert.assertEquals(1.5195472566046677, cos1, 0.002);
		Assert.assertEquals(1.5194576281243275, cos2, 0.004);
	}
}
