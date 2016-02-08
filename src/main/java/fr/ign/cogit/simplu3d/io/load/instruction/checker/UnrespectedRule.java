package fr.ign.cogit.simplu3d.io.load.instruction.checker;

import fr.ign.cogit.geoxygene.api.feature.IFeature;

public class UnrespectedRule {
	String message; IFeature feat1; IFeature feat2;





	public UnrespectedRule(String message, IFeature feat1, IFeature feat2) {
		super();
		this.message = message;
		this.feat1 = feat1;
		this.feat2 = feat2;
	}





	public String getMessage() {
		return message;
	}





	public IFeature getFeat1() {
		return feat1;
	}





	public IFeature getFeat2() {
		return feat2;
	}
	
	
	
	public String toString(){
		String str = this.getMessage() + " ";
		str += feat1;
		if(this.getFeat2() != null){
			str += "  -  " + this.getFeat2();
		}
		return str;
		
	}

}
