package fr.ign.cogit.simplu3d.model;

/**
 * Type de document d'urbanisme (DOC_URBA.TYPEDOC)
 * FIXME ADD TEST
 */
public enum UrbaDocumentType {
	PLU(0), POS(1), OTHER(99), PSMV(2);

	private final int value;

	UrbaDocumentType(int type) {
		value = type;
	}

	public int getValueType() {
		return value;
	}

	public static UrbaDocumentType getTypeFromInt(int type) {
		UrbaDocumentType[] val = UrbaDocumentType.values();
        for (UrbaDocumentType urbaDocumentType : val) {
            if (urbaDocumentType.getValueType() == type) {
                return urbaDocumentType;
            }
        }
		return null;
	}
}