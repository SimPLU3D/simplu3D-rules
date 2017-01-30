package fr.ign.cogit.simplu3d.model;

/**
 * Type de document d'urbanisme (DOC_URBA.TYPEDOC)
 * 
 */

public enum UrbaDocumentType {
	PLU(0), POS(1), OTHER(99), PSMV(2);

	private int value;

	private UrbaDocumentType(int type) {
		value = type;
	}

	public int getValueType() {
		return value;
	}

	public static UrbaDocumentType getTypeFromInt(int type) {
		UrbaDocumentType[] val = UrbaDocumentType.values();
		for (int i = 0; i < val.length; i++) {
			if (val[i].getValueType() == type) {
				return val[i];
			}
		}

		return null;
	}

}