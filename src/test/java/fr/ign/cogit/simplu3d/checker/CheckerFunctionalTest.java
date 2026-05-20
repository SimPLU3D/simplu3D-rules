package fr.ign.cogit.simplu3d.checker;

import java.net.URL;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.experiments.RennesChecker;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.importer.AssignBuildingPartToSubParcel;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.io.regulation.RennesRegulationReader;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;
import junit.framework.TestCase;

public class CheckerFunctionalTest extends TestCase {

	public void testDemo01() {
		CadastralParcelLoader.TYPE_ANNOTATION = 1;

		AssignBuildingPartToSubParcel.RATIO_MIN = 0.8;
		AssignBuildingPartToSubParcel.ASSIGN_METHOD = 0;

		try {
			URL url = getClass().getClassLoader().getResource("demo-01");
			Environnement env = LoaderSHP.loadNoDTM(url);
			BasicPropertyUnit bPU = env.getBpU().toArray(new BasicPropertyUnit[0])[20];//FIXME
			RuleContext context = new RuleContext();
			ZoneRegulation r = RennesRegulationReader.regulationFromLine("UB2,6,0.5,500,16,0.4,16.5,0,0,4,1,1,11,1,3,6,8.5,1,3.5,7");
			List<UnrespectedRule> lSp = RennesChecker.check(bPU, r, context);
			assertEquals(3, lSp.size());
			// TODO complete test
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
