package fr.ign.cogit.simplu3d.io.structDatabase.postgis;

// Ensemble des paramètres nécessaires à l'export vers et depuis PostGis
public class ParametersInstructionPG {

	public static String host = "localhost";
	public static String port = "5432";
	public static String user = "postgres";
	public static String pw = "postgres";
	public static String database = "simplu3d-test";

	// Paramètres PostGis pour la table Zone Urba
	public static String TABLE_ZONE_URBA = "zone_urba";

	public static String ATT_ZONE_URBA_ID = "zu_id";
	public static String ATT_ZONE_URBA_LIBELLE = "zu_libelle";
	public static String ATT_ZONE_URBA_LIBELONG = "zu_libelong";
	public static String ATT_ZONE_URBA_TYPEZONE = "zu_typezone";
	public static String ATT_ZONE_URBA_DESTDOMI = "zu_destdomi";
	public static String ATT_ZONE_URBA_NOMFIC = "zu_nomfic";
	public static String ATT_ZONE_URBA_URLFIC = "zu_urlfic";
	public static String ATT_ZONE_URBA_INSEE = "zu_insee";
	public static String ATT_ZONE_URBA_DATE_APPRO = "zu_date_appro";
	public static String ATT_ZONE_URBA_DATE_VALID = "zu_date_valid";
	public static String ATT_ZONE_URBA_COMMENTAIRE = "zu_commentaire";
	public static String ATT_ZONE_URBA_ID_PLU = "zu_id_plu";
	public static String ATT_ZONE_URBA_GEOM = "the_geom";

	// Paramètres PostGis pour la table PLU
	public static String TABLE_DOC_URBA = "doc_urba";

	public static String ATT_DOC_URBA_ID = "docu_id";
	public static String ATT_DOC_URBA_ID_URBA = "docu_id_urba";
	public static String ATT_DOC_URBA_TYPE_DOC = "docu_type_doc";
	public static String ATT_DOC_URBA_DATE_APPRO = "docu_date_appro";
	public static String ATT_DOC_URBA_DATE_FIN = "docu_date_fin";
	public static String ATT_DOC_URBA_INTERCO = "docu_interco";
	public static String ATT_DOC_URBA_SIREN = "docu_siren";
	public static String ATT_DOC_URBA_ETAT = "docu_etat";
	public static String ATT_DOC_URBA_NOM_REG = "docu_nom_reg";
	public static String ATT_DOC_URBA_URL_REG = "docu_url_reg";
	public static String ATT_DOC_URBA_NOM_PLAN = "docu_nom_plan";
	public static String ATT_DOC_URBA_URL_PLAN = "docu_url_plan";
	public static String ATT_DOC_URBA_SITE = "docu_site";
	public static String ATT_DOC_URBA_TYPE_REF = "docu_type_ref";
	public static String ATT_DOC_URBA_DATE_REF = "docu_date_ref";

	// Paramètres PostGis pour la table Sub Parcels
	public static String TABLE_SUB_PARCEL = "sub_parcel";

	public static String ATT_SUB_PARCEL_ID = "subpar_id";
	public static String ATT_SUB_PARCEL_ID_CADPAR = "subpar_id_cadpar";
	public static String ATT_SUB_PARCEL_ID_ZU = "subpar_id_zu";
	public static String ATT_SUB_PARCEL_AVG_SLOPE = "subpar_avg_slope";
	public static String ATT_SUB_PARCEL_SURF = "subpar_surf";
	public static String ATT_SUB_PARCEL_GEOM = "the_geom";

	// Paramètres PostGis pour la table Parcelle cadastrale
	public static String TABLE_CADASTRAL_PARCEL = "cadastral_parcel";

	public static String ATT_CAD_PARCEL_ID = "cadpar_id";
	public static String ATT_CAD_PARCEL_NUM = "cadpar_num";
	public static String ATT_CAD_PARCEL_SURF = "cadpar_surf";
	public static String ATT_CAD_PARCEL_ID_BPU = "cadpar_id_bpu";
	public static String ATT_CAD_PARCEL_GEOM = "the_geom";

	// Paramètres PostGis pour la table Basic Property Unit
	public static String TABLE_BASIC_PROPERTY_UNIT = "basic_property_unit";

	public static String ATT_BPU_ID = "bpu_id";
	public static String ATT_BPU_GEOM = "the_geom";

	// Paramètres PostGis pour la table Specific CBoundary
	public static String TABLE_SPECIFIC_CBOUNDARY = "specific_cboundary";

	public static String ATT_SPECIFIC_CBOUNDARY_ID = "scb_id";
	public static String ATT_SPECIFIC_CBOUNDARY_TYPE = "scb_type";
	public static String ATT_SPECIFIC_CBOUNDARY_SIDE = "scb_side";
	public static String ATT_SPECIFIC_CBOUNDARY_ID_SUB_PAR = "scb_id_subpar";
	public static String ATT_SPECIFIC_CBOUNDARY_ID_ADJ = "scb_id_adj";
	public static String ATT_SPECIFIC_CBOUNDARY_TABLE_REF = "scb_table_ref";
	public static String ATT_SPECIFIC_CBOUNDARY_GEOM = "the_geom";

	// Paramètres PostGis pour la table Building Part
	public static String TABLE_BUILDING_PART = "building_part";

	public static String ATT_BUILDING_PART_ID = "buildp_id";
	public static String ATT_BUILDING_PART_ID_BUILD = "buildp_id_build";
	public static String ATT_BUILDING_PART_ID_SUBPAR = "buildp_id_subpar";
	public static String ATT_BUILDING_PART_ID_VERSION = "buildp_id_version";
	public static String ATT_BUILDING_PART_GEOM = "buildp_footprint";

	// Paramètres PostGis pour la table Building
	public static String TABLE_BUILDING = "building";

	public static String ATT_BUILDING_ID = "build_id";
	public static String ATT_BUILDING_GEOM = "the_geom";

	// Paramètres PostGIS pour la table Roof
	public static String TABLE_ROOF = "roof";

	public static String ATT_ROOF_ID = "roof_id";
	public static String ATT_ROOF_ANGLE_MIN = "roof_angle_min";
	public static String ATT_ROOF_ANGLE_MAX = "roof_angle_max";
	public static String ATT_ROOF_ID_BUILDPART = "roof_id_buildp";
	public static String ATT_ROOF_GEOM = "the_geom";

	// Paramètres PostGIS pour la table Roofing
	public static String TABLE_ROOFING = "roofing";

	public static String ATT_ROOFING_ID = "roofi_id";
	public static String ATT_ROOFING_ID_ROOF = "roofi_id_roof";
	public static String ATT_ROOFING_GEOM = "the_geom";

	// Paramètres PostGIS pour la table Gutter
	public static String TABLE_GUTTER = "gutter";

	public static String ATT_GUTTER_ID = "gut_id";
	public static String ATT_GUTTER_ID_ROOF = "gut_id_roof";
	public static String ATT_GUTTER_GEOM = "the_geom";

	// Paramètres PostGIS pour la table Gable
	public static String TABLE_GABLE = "gable";

	public static String ATT_GABLE_ID = "gab_id";
	public static String ATT_GABLE_ID_ROOF = "gab_id_roof";
	public static String ATT_GABLE_GEOM = "the_geom";

	// Paramètres PostGis pour la table Wall Surface
	public static String TABLE_WALL_SURFACE = "wall_surface";

	public static String ATT_WALL_SURFACE_ID = "wall_id";
	public static String ATT_WALL_SURFACE_ID_BUILDP = "wall_id_buildp";
	public static String ATT_WALL_SURFACE_GEOM = "the_geom";

	// Paramètres PostGis pour la table Road
	public static String TABLE_ROAD = "road";

	public static String ATT_ROAD_ID = "road_id";
	public static String ATT_ROAD_NOM = "road_nom";
	public static String ATT_ROAD_TYPE = "road_type";
	public static String ATT_ROAD_LARGEUR = "road_largeur";
	public static String ATT_ROAD_AXE = "road_axe";
	public static String ATT_ROAD_SURFACE = "road_surf";
	public static String ATT_ROAD_GEOM_SURF = "road_surf";

	// Paramètres PostGis pour la table Axe
	public static String TABLE_AXE = "axe";

	public static String ATT_AXE_ID = "axe_id";
	public static String ATT_AXE_ID_ROAD = "axe_id_road";
	public static String ATT_AXE_GEOM = "axe_geom";

	// Paramètres PostGis pour la table Public Space
	public static String TABLE_PUBLIC_SPACE = "public_space";

	public static String ATT_PUBLIC_SPACE_ID = "pubs_id";
	public static String ATT_PUBLIC_SPACE_NOM = "pubs_nom";
	public static String ATT_PUBLIC_SPACE_GEOM = "the_geom";

	// Paramètres PostGis pour la table Version
	public static String TABLE_VERSION = "version";

	public static String ATT_VERSION_ID = "vers_id";
	public static String ATT_VERSION_ID_VERSION_BUILD = "vers_id_vers_build";
	public static String ATT_VERSION_ID_BUILD_DEL = "vers_id_build_del";

	// Paramètres PostGis pour la table User
	public static String TABLE_USER = "utilisateur";

	public static String ATT_USER_ID = "user_id";
	public static String ATT_USER_LOGIN = "user_login";
	public static String ATT_USER_PASSWORD = "user_pw";

	// Paramètres PostGis pour la table User/Version
	public static String TABLE_USER_VERSION = "user_version";

	public static String ATT_USER_VERS_ID = "usv_id";
	public static String ATT_USER_VERS_ID_USER = "usv_id_user";
	public static String ATT_USER_VERS_ID_VERSION = "usv_id_version";
	public static String ATT_USER_VERS_NOM_VERSION = "usv_nom_version";

	// Paramètres PostGis pour la table Rules
	public static String TABLE_RULES = "rules";

	public static String ATT_RULES_ID = "rul_id";
	public static String ATT_RULES_NOM_ZONE = "rul_nom_zone";
	public static String ATT_RULES_BANDE_INCONS = "rul_bande_incons";
	public static String ATT_RULES_EMP_SOL = "rul_emp_sol";
	public static String ATT_RULES_EMP_SURF_MIN = "rul_emp_surf_mini";
	public static String ATT_RULES_EMP_LARG_MIN = "rul_emp_larg_mini";
	public static String ATT_RULES_EMP_SOL_ALT = "rul_emp_sol_alt";
	public static String ATT_RULES_BANDE_1 = "rul_bande_1";
	public static String ATT_RULES_ALIGNEMENT = "rul_alignement";
	public static String ATT_RULES_RECUL_LAT_MINI = "rul_recul_lat_mini";
	public static String ATT_RULES_RECUL_LAT = "rul_recul_lat";
	public static String ATT_RULES_PROSPECT_VOIRIE_1_SLOPE = "rul_prospect_voirie1_slope";
	public static String ATT_RULES_PROSPECT_VOIRIE_1_HINI = "rul_prospect_voirie1_hini";
	public static String ATT_RULES_LARG_MAX_PROSPECT_1 = "rul_larg_max_prospect1";
	public static String ATT_RULES_PROSPECT_VOIRIE_2_SLOPE = "rul_prospect_voirie2_slope";
	public static String ATT_RULES_PROSPECT_VOIRIE_2_HINI = "rul_prospect_voirie2_hini";
	public static String ATT_RULES_HAUTEUR_MAXI_FACADE = "rul_hauteur_maxi_facade";
	public static String ATT_RULES_BANDE_2 = "rul_bande_2";
	public static String ATT_RULES_SLOPE_PROSPECT_LAT_SLOPE = "rul_slope_prospect_lat_slope";
	public static String ATT_RULES_SLOPE_PROSPECT_LAT_HINI = "rul_slope_prospect_lat_hini";
	public static String ATT_RULES_HAUTEUR_MAX = "rul_hauteur_max";

	public final static String DATE_FORMAT_ZU = "d-MM-yyyy";
	public final static String DATE_FORMAT_DU1 = "yyyyMMd";
	public final static String DATE_FORMAT_DU2 = "yyyy";
	public final static String DATE_FORMAT_YYYY_MM_D = "yyyy-MM-d";

	public static int ID_VERSION = -1;

}
