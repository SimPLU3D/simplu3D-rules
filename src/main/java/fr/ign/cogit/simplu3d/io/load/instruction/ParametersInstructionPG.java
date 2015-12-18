package fr.ign.cogit.simplu3d.io.load.instruction;

// Ensemble des paramètres nécessaires à l'export vers et depuis PostGis
public class ParametersInstructionPG {

  // Paramètres PostGis pour la table Zone Urba
  public final static String TABLE_ZONE_URBA = "zone_urba";

  public final static String ATT_ZONE_URBA_ID = "zu_id";
  public final static String ATT_ZONE_URBA_LIBELLE = "zu_libelle";
  public final static String ATT_ZONE_URBA_LIBELONG = "zu_libelong";
  public final static String ATT_ZONE_URBA_TYPEZONE = "zu_typezone";
  public final static String ATT_ZONE_URBA_DESTDOMI = "zu_destdomi";
  public final static String ATT_ZONE_URBA_NOMFIC = "zu_nomfic";
  public final static String ATT_ZONE_URBA_URLFIC = "zu_urlfic";
  public final static String ATT_ZONE_URBA_INSEE = "zu_insee";
  public final static String ATT_ZONE_URBA_DATE_APPRO = "zu_date_appro";
  public final static String ATT_ZONE_URBA_DATE_VALID = "zu_date_valid";
  public final static String ATT_ZONE_URBA_COMMENTAIRE = "zu_commentaire";
  public final static String ATT_ZONE_URBA_ID_PLU = "zu_id_plu";
  public final static String ATT_ZONE_URBA_GEOM = "the_geom";

  // Paramètres PostGis pour la table PLU
  public final static String TABLE_DOC_URBA = "doc_urba";

  public final static String ATT_DOC_URBA_ID = "docu_id";
  public final static String ATT_DOC_URBA_ID_URBA = "docu_id_urba";
  public final static String ATT_DOC_URBA_TYPE_DOC = "docu_type_doc";
  public final static String ATT_DOC_URBA_DATE_APPRO = "docu_date_appro";
  public final static String ATT_DOC_URBA_DATE_FIN = "docu_date_fin";
  public final static String ATT_DOC_URBA_INTERCO = "docu_interco";
  public final static String ATT_DOC_URBA_SIREN = "docu_siren";
  public final static String ATT_DOC_URBA_ETAT = "docu_etat";
  public final static String ATT_DOC_URBA_NOM_REG = "docu_nom_reg";
  public final static String ATT_DOC_URBA_URL_REG = "docu_url_reg";
  public final static String ATT_DOC_URBA_NOM_PLAN = "docu_nom_plan";
  public final static String ATT_DOC_URBA_URL_PLAN = "docu_url_plan";
  public final static String ATT_DOC_URBA_SITE = "docu_site";
  public final static String ATT_DOC_URBA_TYPE_REF = "docu_type_ref";
  public final static String ATT_DOC_URBA_DATE_REF = "docu_date_ref";

  // Paramètres PostGis pour la table Sub Parcels
  public final static String TABLE_SUB_PARCEL = "sub_parcel";

  public final static String ATT_SUB_PARCEL_ID = "subpar_id";
  public final static String ATT_SUB_PARCEL_ID_CADPAR = "subpar_id_cadpar";
  public final static String ATT_SUB_PARCEL_ID_ZU = "subpar_id_zu";
  public final static String ATT_SUB_PARCEL_AVG_SLOPE = "subpar_avg_slope";
  public final static String ATT_SUB_PARCEL_SURF = "subpar_surf";
  public final static String ATT_SUB_PARCEL_GEOM = "the_geom";

  // Paramètres PostGis pour la table Parcelle cadastrale
  public final static String TABLE_CADASTRAL_PARCEL = "cadastral_parcel";

  public final static String ATT_CAD_PARCEL_ID = "cadpar_id";
  public final static String ATT_CAD_PARCEL_NUM = "cadpar_num";
  public final static String ATT_CAD_PARCEL_SURF = "cadpar_surf";
  public final static String ATT_CAD_PARCEL_ID_BPU = "cadpar_id_bpu";
  public final static String ATT_CAD_PARCEL_GEOM = "the_geom";

  // Paramètres PostGis pour la table Basic Property Unit
  public final static String TABLE_BASIC_PROPERTY_UNIT = "basic_property_unit";

  public final static String ATT_BPU_ID = "bpu_id";
  public final static String ATT_BPU_GEOM = "the_geom";

  // Paramètres PostGis pour la table Specific CBoundary
  public final static String TABLE_SPECIFIC_CBOUNDARY = "specific_cboundary";

  public final static String ATT_SPECIFIC_CBOUNDARY_ID = "scb_id";
  public final static String ATT_SPECIFIC_CBOUNDARY_TYPE = "scb_type";
  public final static String ATT_SPECIFIC_CBOUNDARY_SIDE = "scb_side";
  public final static String ATT_SPECIFIC_CBOUNDARY_ID_SUB_PAR = "scb_id_subpar";
  public final static String ATT_SPECIFIC_CBOUNDARY_ID_ADJ = "scb_id_adj";
  public final static String ATT_SPECIFIC_CBOUNDARY_TABLE_REF = "scb_table_ref";
  public final static String ATT_SPECIFIC_CBOUNDARY_GEOM = "the_geom";

  // Paramètres PostGis pour la table Building Part
  public final static String TABLE_BUILDING_PART = "building_part";

  public final static String ATT_BUILDING_PART_ID = "buildp_id";
  public final static String ATT_BUILDING_PART_ID_BUILD = "buildp_id_build";
  public final static String ATT_BUILDING_PART_ID_SUBPAR = "buildp_id_subpar";
  public final static String ATT_BUILDING_PART_GEOM = "buildp_footprint";

  // Paramètres PostGis pour la table Building
  public final static String TABLE_BUILDING = "building";

  public final static String ATT_BUILDING_ID = "build_id";
  public final static String ATT_BUILDING_GEOM = "the_geom";

  // Paramètres PostGIS pour la table Roof
  public final static String TABLE_ROOF = "roof";

  public final static String ATT_ROOF_ID = "roof_id";
  public final static String ATT_ROOF_ANGLE_MIN = "roof_angle_min";
  public final static String ATT_ROOF_ANGLE_MAX = "roof_angle_max";
  public final static String ATT_ROOF_ID_BUILDPART = "roof_id_buildp";
  public final static String ATT_ROOF_GEOM = "the_geom";

  // Paramètres PostGIS pour la table Roofing
  public final static String TABLE_ROOFING = "roofing";

  public final static String ATT_ROOFING_ID = "roofi_id";
  public final static String ATT_ROOFING_ID_ROOF = "roofi_id_roof";
  public static final String ATT_ROOFING_GEOM = "the_geom";

  // Paramètres PostGIS pour la table Gutter
  public final static String TABLE_GUTTER = "gutter";

  public final static String ATT_GUTTER_ID = "gut_id";
  public final static String ATT_GUTTER_ID_ROOF = "gut_id_roof";
  public static final String ATT_GUTTER_GEOM = "the_geom";

  // Paramètres PostGIS pour la table Gable
  public final static String TABLE_GABLE = "gable";

  public final static String ATT_GABLE_ID = "gab_id";
  public final static String ATT_GABLE_ID_ROOF = "gab_id_roof";
  public static final String ATT_GABLE_GEOM = "the_geom";

  // Paramètres PostGis pour la table Wall Surface
  public final static String TABLE_WALL_SURFACE = "wall_surface";

  public final static String ATT_WALL_SURFACE_ID = "wall_id";
  public final static String ATT_WALL_SURFACE_ID_BUILDP = "wall_id_buildp";
  public final static String ATT_WALL_SURFACE_GEOM = "the_geom";

  // Paramètres PostGis pour la table Road
  public final static String TABLE_ROAD = "road";

  public final static String ATT_ROAD_ID = "road_id";
  public final static String ATT_ROAD_NOM = "road_nom";
  public final static String ATT_ROAD_TYPE = "road_type";
  public final static String ATT_ROAD_LARGEUR = "road_largeur";
  public final static String ATT_ROAD_AXE = "road_axe";
  public final static String ATT_ROAD_SURFACE = "road_surf";
  public final static String ATT_ROAD_GEOM_SURF = "road_surf";

  // Paramètres PostGis pour la table Axe
  public final static String TABLE_AXE = "axe";

  public final static String ATT_AXE_ID = "axe_id";
  public final static String ATT_AXE_ID_ROAD = "axe_id_road";
  public final static String ATT_AXE_GEOM = "axe_geom";

  // Paramètres PostGis pour la table Public Space
  public final static String TABLE_PUBLIC_SPACE = "public_space";

  public final static String ATT_PUBLIC_SPACE_ID = "pubs_id";
  public final static String ATT_PUBLIC_SPACE_NOM = "pubs_nom";
  public final static String ATT_PUBLIC_SPACE_GEOM = "the_geom";

}
