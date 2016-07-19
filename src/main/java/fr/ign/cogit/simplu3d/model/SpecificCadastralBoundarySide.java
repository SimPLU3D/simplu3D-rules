package fr.ign.cogit.simplu3d.model;

public enum SpecificCadastralBoundarySide{
	LEFT(0),
	RIGHT(1),
	UNKNOWN(99);
	
	private int value;
	
	private SpecificCadastralBoundarySide(int type){
		value = type;
	}
	public int getValueType(){
		return value;
	}
	
	public static SpecificCadastralBoundarySide getTypeFromInt(int type){
		SpecificCadastralBoundarySide[] val = SpecificCadastralBoundarySide.values();
		for(int i=0; i <val.length; i++){
			if(val[i].getValueType() == type)
			{
				return val[i];
			}
		}
		
		return null;
	}
}