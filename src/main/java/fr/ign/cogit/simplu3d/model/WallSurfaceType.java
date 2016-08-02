package fr.ign.cogit.simplu3d.model;

public enum WallSurfaceType {
	BOT(0), LAT(1), UNKNOWN(99), ROAD(2);

	private int value;

	private WallSurfaceType(int type) {
		value = type;
	}

	public int getValueType() {
		return value;
	}

	public static WallSurfaceType getTypeFromInt(int type) {
		WallSurfaceType[] val = WallSurfaceType.values();
		for (int i = 0; i < val.length; i++) {
			if (val[i].getValueType() == type) {
				return val[i];
			}
		}

		return null;
	}

}