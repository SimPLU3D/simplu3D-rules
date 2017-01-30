package fr.ign.cogit.simplu3d.model;

/**
 * 
 * The side of the boundary (relative to the front of the parcel)
 * 
 * @author MBrasebin
 *
 */
public enum ParcelBoundarySide {
	LEFT(0),
	RIGHT(1),
	UNKNOWN(99);
	
	private int value;
	
	private ParcelBoundarySide(int type){
		value = type;
	}
	public int getValueType(){
		return value;
	}
	
	public static ParcelBoundarySide getTypeFromInt(int type){
		ParcelBoundarySide[] val = ParcelBoundarySide.values();
		for(int i=0; i <val.length; i++){
			if(val[i].getValueType() == type)
			{
				return val[i];
			}
		}
		
		return null;
	}
}