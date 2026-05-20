package fr.ign.cogit.simplu3d.model;

public enum WallSurfaceType {
	BOT(0), LAT(1), UNKNOWN(99), ROAD(2);

	private final int value;

	WallSurfaceType(int type) {
		value = type;
	}

	public int getValueType() {
		return value;
	}

	public static WallSurfaceType getTypeFromInt(int type) {
		WallSurfaceType[] val = WallSurfaceType.values();
        for (WallSurfaceType wallSurfaceType : val) {
            if (wallSurfaceType.getValueType() == type) {
                return wallSurfaceType;
            }
        }
		return null;
	}
}