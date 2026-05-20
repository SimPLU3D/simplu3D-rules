package fr.ign.cogit.simplu3d.model;

import java.util.logging.Logger;

/**
 * 
 * Represents the kind of ParcelBoundary
 *
 * @author MBrasebin
 *
 */
public enum ParcelBoundaryType {
	BOT(0),
	LAT(1),
	UNKNOWN(99),
	/**
	 * Represent a boundary between SubParcel in a CadastralParcel
	 * warning unused (boundaries are computed on CadastralParcels)
	 */
	INTRA(3),
	ROAD(4),
	LATERAL_TEMP(98),
	PUB(5);
	
	
	private final static Logger logger = Logger.getLogger(ParcelBoundaryType.class.getName());
	
	private final int value;
	
	ParcelBoundaryType(int type){
		value = type;
	}
	public int getValueType(){
		return value;
	}
	
	public static ParcelBoundaryType getTypeFromInt(int type){
		ParcelBoundaryType[] val = ParcelBoundaryType.values();
        for (ParcelBoundaryType parcelBoundaryType : val) {
            if (parcelBoundaryType.getValueType() == type) {
                return parcelBoundaryType;
            }
        }
		logger.warning("ParcelBoundaryType : VALUE NOT FOUND : value " + type);
		return null;
	}
	
}