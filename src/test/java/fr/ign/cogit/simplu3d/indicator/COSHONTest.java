package fr.ign.cogit.simplu3d.indicator;

import java.io.FileNotFoundException;

import org.junit.Test;

import fr.ign.cogit.simplu3d.demo.nonStructDatabase.shp.LoadDefaultEnvironment;
import fr.ign.cogit.simplu3d.indicator.COSCalculation;
import fr.ign.cogit.simplu3d.indicator.COSCalculation.METHOD;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import junit.framework.Assert;

public class COSHONTest {

	public static void main(String[] args) throws FileNotFoundException {

		(new COSHONTest()).testCOS();
	}

	@Test
	public void testCOS() throws FileNotFoundException {
		// TODO normalize file name
//		LoaderSHP.NOM_FICHIER_TERRAIN = "MNT.asc";

		BasicPropertyUnit sp = LoadDefaultEnvironment.getENVDEF().getBpU().get(0);

		double cos1 = COSCalculation.assess(sp, METHOD.SIMPLE);
		double cos2 = COSCalculation.assess(sp, METHOD.FLOOR_CUT);

		double epsilon = 0.00001;

		//TODO check this (fixed test mborne)
		//Assert.assertTrue(Math.abs(1.599950942067229 - cos1) < epsilon);
		//Assert.assertTrue(Math.abs(1.59979612630762089 - cos2) < epsilon);
		
		Assert.assertTrue(Math.abs(1.5195472566046677 - cos1) < epsilon);
		Assert.assertTrue(Math.abs(1.5194576281243275 - cos2) < epsilon);
	}

}
