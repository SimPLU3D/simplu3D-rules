package fr.ign.cogit.simplu3d.checker;

import java.io.File;
import java.util.List;

import fr.ign.cogit.simplu3d.importer.AssignBuildingPartToSubParcel;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import junit.framework.TestCase;

public class CheckerFunctionalTest extends TestCase {

	public void testDemo01(){
		CadastralParcelLoader.TYPE_ANNOTATION = 1;

		AssignBuildingPartToSubParcel.RATIO_MIN = 0.8;
		AssignBuildingPartToSubParcel.ASSIGN_METHOD = 0;
		
		try {
			File path = new File(
				getClass().getClassLoader().getResource("demo-01").getPath()
			);
			Environnement env = LoaderSHP.loadNoDTM(path);
			BasicPropertyUnit bPU = env.getBpU().get(20);
			List<UnrespectedRule> lSp = Checker.check(bPU, new Rules("UB2,6,0.5,500,16,0.4,16.5,0,0,4,1,1,11,1,3,6,8.5,1,3.5,7"));
			assertEquals(3, lSp.size());
			//TODO complete test
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
