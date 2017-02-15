package fr.ign.cogit.simplu3d.model;

import java.util.logging.Logger;

/**
 * 
 * The side of the boundary (relative to the front of the parcel)
 * 
 * @author MBrasebin
 *
 */
public enum ParcelBoundarySide {
	LEFT(0), RIGHT(1), UNKNOWN(99);

	private final static Logger logger = Logger.getLogger(ParcelBoundarySide.class.getName());

	private int value;

	private ParcelBoundarySide(int type) {
		value = type;
	}

	public int getValueType() {
		return value;
	}

	public static ParcelBoundarySide getTypeFromInt(int type) {
		ParcelBoundarySide[] val = ParcelBoundarySide.values();
		for (int i = 0; i < val.length; i++) {
			if (val[i].getValueType() == type) {
				return val[i];
			}
		}
		
		logger.

		return null;
	}
}