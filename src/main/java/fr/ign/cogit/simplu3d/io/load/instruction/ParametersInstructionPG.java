package fr.ign.cogit.simplu3d.io.load.instruction;

// Ensemble des paramètres nécessaires à l'export vers PostGis
public class ParametersInstructionPG {
  
  /// Paramètres PostGis pour la table Zone Urba
  public final static String TABLE_ZONE_URBA = "zone_urba";
  
  public final static String ATT_ZONE_URBA_ID = "zu_id";
  public final static String ATT_ZONE_URBA_NOM = "zu_nom";
  public final static String ATT_ZONE_URBA_DATE_DEBUT = "zu_date_debut";
  public final static String ATT_ZONE_URBA_DATE_FIN = "zu_date_fin";
  public final static String ATT_ZONE_URBA_COMMENTAIRE = "zu_commentaire";
  public final static String ATT_ZONE_URBA_ID_PLU = "zu_id_plu";
  public final static String ATT_ZONE_URBA_GEOM = "the_geom";
  
  
  /// Paramètres PostGis pour la table PLU
  public final static String TABLE_PLU = "plu";
  
  public final static String ATT_PLU_ID = "plu_id";
  public final static String ATT_PLU_NOM_AGGLO = "plu_nom_agglo";
  public final static String ATT_PLU_DATE_DEBUT = "plu_date_debut";
  public final static String ATT_PLU_DATE_FIN = "plu_date_fin";
  
  
  /// Paramètres PostGis pour la table Sub Parcels
  public final static String TABLE_SUB_PARCEL = "sub_parcel";
  
  public final static String ATT_SUB_PARCEL_ID = "sp_id";
  public final static String ATT_SUB_PARCEL_ID_CADPAR = "sp_id_cadpar";
  public final static String ATT_SUB_PARCEL_ID_ZU = "sp_id_zu";
  public final static String ATT_SUB_PARCEL_GEOM = "the_geom";
  
  
  /// Paramètres PostGis pour la table Zone Urba
  public final static String TABLE_CADASTRAL_PARCEL = "cadastral_parcel";
  
  public final static String ATT_CAD_PARCEL_ID = "cadpar_id";
  public final static String ATT_CAD_PARCEL_ID_BPU = "cadpar_id_bpu";
  public final static String ATT_CAD_PARCEL_GEOM = "the_geom";
  
  
  /// Paramètres PostGis pour la table Basic Property Unit
  public final static String TABLE_BASIC_PROPERTY_UNIT = "basic_property_unit";
  
  public final static String ATT_BPU_ID = "bpu_id";
  public final static String ATT_BPU_GEOM = "the_geom";
  
  
  /// Paramètres PostGis pour la table Specific CBoundary
  public final static String TABLE_SPECIFIC_CBOUNDARY = "specific_boundary";
  
  public final static String ATT_SPECIFIC_CBOUNDARY_ID = "scb_id";
  public final static String ATT_SPECIFIC_CBOUNDARY_TYPE = "scb_type";
  public final static String ATT_SPECIFIC_CBOUNDARY_SIDE = "scb_side";
  public final static String ATT_SPECIFIC_CBOUNDARY_ID_VOIRIE = "scb_id_voirie";
  public final static String ATT_SPECIFIC_CBOUNDARY_TYPE_VOIRIE = "scb_type_voirie";
  public final static String ATT_SPECIFIC_CBOUNDARY_GEOM = "the_geom";
  
  
  /// Paramètres PostGis pour la table Building Part
  public final static String TABLE_BUILDING_PART = "building_part";
  
  public final static String ATT_BUILDING_PART_ID = "buildp_id";
  public final static String ATT_BUILDING_PART_ID_BUILD = "buildp_id_build";
  public final static String ATT_BUILDING_PART_ID_SUBPAR = "buildp_id_subpar";
  public final static String ATT_BUILDING_PART_GEOM = "the_geom";
  
  
  /// Paramètres PostGis pour la table Building
  public final static String TABLE_BUILDING = "building";
  
  public final static String ATT_BUILDING_ID = "build_id";
  
  
  /// Paramètres PostGIS pour la table Roof
  public final static String TABLE_ROOF = "roof";
  
  public final static String ATT_ROOF_ID = "rood_id";
  public final static String ATT_ROOF_ANGLE_MIN = "roof_angle_min";
  public final static String ATT_ROOF_ANGLE_MAX = "roof_angle_max";
  public final static String ATT_ROOF_BUILDPART = "roof_id_buildp";
  public final static String ATT_ROOF_GEOM = "the_geom";
  
  
  /// Paramètres PostGis pour la table Wall Surface
  public final static String TABLE_WALL_SURFACE = "wall_surface";
  
  public final static String ATT_WALL_SURFACE_ID = "wall_id";
  public final static String ATT_WALL_SURAFCE_ID_BUILDP = "wall_id_buildp";
  public final static String ATT_WALL_SURFACE_GEOM = "the_geom";
  
  
  /// Paramètres PostGis pour la table Road
  public final static String TABLE_ROAD = "road";
  
  public final static String ATT_ROAD_ID = "road_id";
  public final static String ATT_ROAD_NOM = "road_nom";
  public final static String ATT_ROAD_TYPE = "road_type";
  public final static String ATT_ROAD_AXE = "road_axe";
  public final static String ATT_ROAD_SURFACE = "road_surf";
  public final static String ATT_ROAD_GEOM = "the_geom";
  
  
  /// Paramètres PostGis pour la table Public Space
  public final static String TABLE_PUBLIC_SPACE = "public_space";
 
  public final static String ATT_PUBLIC_SPACE_ID = "pubs_id";
  public final static String ATT_PUBLIC_SPACE_NOM = "pubs_nom";
  public final static String ATT_PUBLIC_SPACE_GEOM = "the_geom";
  
}
