package fr.ign.cogit.simplu3d.model;

public enum SpecificCadastralBoundaryType {
	BOT(0),
	LAT(1),
	UNKNOWN(99),
	INTRA(3),
	ROAD(4),
	LATERAL_TEMP(98),
	PUB(5);
	
	private int value;
	
	private SpecificCadastralBoundaryType(int type){
		value = type;
	}
	public int getValueType(){
		return value;
	}
	
	public static SpecificCadastralBoundaryType getTypeFromInt(int type){
		SpecificCadastralBoundaryType[] val = SpecificCadastralBoundaryType.values();
		for(int i=0; i <val.length; i++){
			if(val[i].getValueType() == type)
			{
				return val[i];
			}
		}
		
		return null;
	}
	
}