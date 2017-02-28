package fr.ign.cogit.simplu3d.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;

/**
 * Define rule parameters
 * 
 * 
 * @author Brasebin Mickaël
 */
@Entity
@Table(name = "simplu_regulation")
public class Rules {

	@Id
	@GeneratedValue
	private Long id;

	private int numBand;

	public int getNumBand() {
		return numBand;
	}

	public void setNumBand(int numBand) {
		this.numBand = numBand;
	}

	private String insee;

	private String libelle_zone;

	private String nom_zone;
	private Double bandIncons; // Largeur de la bande non constructible (6 m)
	private Double empriseSol; // CES de base (0.5)
	private Double empriseSurface; // condition de surface du terrain pour le
									// CES
									// de base (500 m²)
	private Double empLargeurMin; // condition de largeur des façades pour le
									// CES
									// de base (16 m)
	private Double empriseSolAlt; // CES si les conditions ne sont pas vérifiées
									// (0.4)
	private Double band1; // Largeur pour la bande 1 à partir de la voirie (
							// 16.5)
	private Double alignement; // Alignement par rapport à la voirie en bande 1
								// (0)
	private Double reculLatMin; // Recul maximal en bande 1 par rapport aux
								// limites latérales (0)
	private Double reculLatMax; // /Recul minimal en bande 1 par rapport aux
								// limites latérales (4)
	private Double prospectVoirie1Slope; // Prospect (pente) de base par rapport
											// au côté oppposé de la voirie (1)
	private Double prospectVoirie1Hini; // Prospect (hIni) de base par rapport
										// au
										// côté oppposé de la voirie (1)
	private Double largMaxProspect1; // Largeur de la route à ne pas dépasser
										// pour
										// avoir le prospect de base (11 m)
	private Double prospectVoirie2Slope; // Prospect alternatif (pente) par
											// rapport au côté oppposé de la
											// voirie
											// (1)
	private Double prospectVoirie2Hini; // Prospect alternatif (hIni) par
										// rapport
										// au côté oppposé de la voirie (3)
	private Double hauteurMaxFacade; // Hauteur maximale du bâtiment par rapport
										// à
										// la facade (6 m)
	private Double band2; // Taille de la bande 2 à partir de la bande 1 (8.5 m
							// )
	private Double slopeProspectLat; // Pente de prospect bande 2 par rapport
										// aux
										// limites latérales (1)
	private Double hIniProspectLat; // Hauteur initiale de prospect bande 2 par
									// rapport aux limites latérales (3.5)
	private Double hauteurMax2; // Hauteur maximale des bâtiments en bande 2.

	private Double hIniOppositeProspect; // Paramètres de hauteur par rapport au
											// côté opposé de la voirie
	private Double slopeOppositeProspect; //

	// Les intitulés des colonnes
	private int code_imu, date_approbation, fonctions, top_zac, zonage_coherent, correction_zonage, typ_bande, bande,
			art_71, art_74, art_10_top;
	private String libelle_de_base, libelle_de_dul;

	private Double art_5, art_72, art_73, art_8, art_6, art_9, art_102, art_12, art_14, art_13, art_101;

	public Rules() {
	}

	public Rules(int code_imu, String libelle_zone, String insee, int date_approbation, String libelle_de_base,
			String libelle_de_dul, int fonctions, int top_zac, int zonage_coherent, int correction_zonage,
			int typ_bande, int bande, Double art_5, Double art_6, int art_71, Double art_72, Double art_73, int art_74,
			Double art_8, Double art_9, int art_10_top, Double art_101, Double art_102, Double art_12, Double art_13,
			Double art_14) {
		super();
		this.code_imu = code_imu;
		this.libelle_zone = libelle_zone;
		this.insee = insee;
		this.date_approbation = date_approbation;
		this.libelle_de_base = libelle_de_base;
		this.libelle_de_dul = libelle_de_dul;
		this.fonctions = fonctions;
		this.top_zac = top_zac;
		this.zonage_coherent = zonage_coherent;
		this.correction_zonage = correction_zonage;
		this.typ_bande = typ_bande;
		this.bande = bande;
		this.art_5 = art_5;
		this.art_6 = art_6;
		this.art_71 = art_71;
		this.art_72 = art_72;
		this.art_73 = art_73;
		this.art_74 = art_74;
		this.art_8 = art_8;
		this.art_9 = art_9;
		this.art_10_top = art_10_top;
		this.art_101 = art_101;
		this.art_102 = art_102;
		this.art_12 = art_12;
		this.art_13 = art_13;
		this.art_14 = art_14;
	}

	/**
	 * Constructeur de Regulation conçu pour l'expérimentation de Rennes
	 * Métropole
	 * 
	 * @param nom_zone
	 * @param bandIncons
	 * @param empriseSol
	 * @param empriseSurface
	 * @param empLargeurMin
	 * @param empriseSolAlt
	 * @param band1
	 * @param alignement
	 * @param reculLatMin
	 * @param reculLatMax
	 * @param prospectVoirie1Slope
	 * @param prospectVoirie1Hini
	 * @param largMaxProspect1
	 * @param prospectVoirie2Slope
	 * @param prospectVoirie2Hini
	 * @param hauteurMaxFacade
	 * @param band2
	 * @param slopeProspectLat
	 * @param hIniProspectLat
	 * @param hauteurMax2
	 */
	public Rules(String nom_zone, Double bandIncons, Double empriseSol, Double empriseSurface, Double empLargeurMin,
			Double empriseSolAlt, Double band1, Double alignement, Double reculLatMin, Double reculLatMax,
			Double prospectVoirie1Slope, Double prospectVoirie1Hini, Double largMaxProspect1,
			Double prospectVoirie2Slope, Double prospectVoirie2Hini, Double hauteurMaxFacade, Double band2,
			Double slopeProspectLat, Double hIniProspectLat, Double hauteurMax2) {
		super();
		this.nom_zone = nom_zone;
		this.bandIncons = bandIncons;
		this.empriseSol = empriseSol;
		this.empriseSurface = empriseSurface;
		this.empLargeurMin = empLargeurMin;
		this.empriseSolAlt = empriseSolAlt;
		this.band1 = band1;
		this.alignement = alignement;
		this.art_9 = empriseSol;
		this.reculLatMin = reculLatMin;
		this.reculLatMax = reculLatMax;
		this.prospectVoirie1Slope = prospectVoirie1Slope;
		this.prospectVoirie1Hini = prospectVoirie1Hini;
		this.largMaxProspect1 = largMaxProspect1;
		this.prospectVoirie2Slope = prospectVoirie2Slope;
		this.prospectVoirie2Hini = prospectVoirie2Hini;
		this.hauteurMaxFacade = hauteurMaxFacade;
		this.band2 = band2;
		this.slopeProspectLat = slopeProspectLat;
		this.hauteurMax2 = hauteurMax2;
		this.hIniProspectLat = hIniProspectLat;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCode_imu() {
		return code_imu;
	}

	public String getLibelle_zone() {
		return libelle_zone;
	}

	public String getInsee() {
		return insee;
	}

	public void setInsee(String insee) {
		this.insee = insee;
	}

	// DATE_DUL Date d'approbation du PLU
	public int getDate_approbation() {
		return date_approbation;
	}

	// LIBELLE_DE_BASE Type de zone
	public String getLibelle_de_base() {
		return libelle_de_base;
	}

	// LIBELLE_DE_DUL Type de zone
	public String getLibelle_de_dul() {
		return libelle_de_dul;
	}

	// FONCTIONS Fonction de la zone 0 : logements uniquement ; 1 : activité
	// mais possibilité de logements ; 2 : exclusivement activité
	public int getFonctions() {
		return fonctions;
	}

	// TOP_ZAC Présence d'une ZAC 0 : NON // 1 : OUI
	public int getTop_zac() {
		return top_zac;
	}

	// ZONAGE_COHERENT Zonage cohérent entre la base et le plan de zonage du DUL
	// 0 : NON // 1 : OUI
	public int getZonage_coherent() {
		return zonage_coherent;
	}

	// CORRECTION_ZONAGE Indicateur du zonage à faire prévaloir en cas
	// d'incohérence des zonages 0 : Conserver le dessin de zonage de Carto PLU
	// 1 : Numeriser le zonage du PLU analysé
	public int getCorrection_zonage() {
		return correction_zonage;
	}

	// TYP_BANDE Information concernant l'existence d'une bande principale ou
	// secondaire 0 : pas de bande // 1 : principale // 2 : secondaire
	public int getTyp_bande() {
		return typ_bande;
	}

	// BANDE Profondeur de la bande principale x > 0 profondeur de la bande par
	// rapport à la voirie
	public int getBande() {
		return bande;
	}

	// ART_5 Superficie minimale 88= non renseignable, 99= non réglementé

	public Double getArt_5() {
		return art_5;
	}

	// ART_6 Distance minimale des constructions par rapport à la voirie imposée
	// en mètre 88= non renseignable, 99= non réglementé
	public Double getArt_6() {
		return art_6;
	}

	// ART_71 Implantation en limite séparative 0 : non, retrait imposé (cf.72)
	// // 1 : Oui // 2 : Oui, mais sur un côté seulement
	public int getArt_71() {
		return art_71;
	}

	// ART_72 Distance minimale des constructions par rapport aux limites
	// séparatives imposée en mètre 88= non renseignable, 99= non réglementé
	public Double getArt_72() {
		return art_72;
	}

	// ART_73 Distance minimale des constructions par rapport à la limte
	// séparative de fond de parcelle 88= non renseignable, 99= non réglementé
	public Double getArt_73() {
		return art_73;
	}

	// ART_74 Distance minimum des constructions par rapport aux limites
	// séparatives, exprimée par rapport à la hauteur du bâtiment 0 : NON // 1 :
	// Retrait égal à la hauteur // 2 : Retrait égal à la hauteur divisé par
	// deux // 3 : Retrait égal à la hauteur divisé par trois // 4 : Retrait
	// égal à la hauteur divisé par quatre // 5 : Retrait égal à la hauteur
	// divisé par cinq // 6 : Retrait égal à la hauteur divisé par deux moins
	// trois mètres // 7 : Retrait égal à la hauteur moins trois mètres divisé
	// par deux // 8 : retrait égal à la hauteur divisé par deux moins un mètre
	// // 9 : retrait égal aux deux tiers de la hauteur // 10 : retrait égal aux
	// trois quarts de la hauteur
	public int getArt_74() {
		return art_74;
	}

	// ART_8 Distance minimale des constructions par rapport aux autres sur une
	// même propriété imposée en mètre 88= non renseignable, 99= non réglementé
	public Double getArt_8() {
		return art_8;
	}

	// ART_9 Pourcentage d'emprise au sol maximum autorisé Valeur comprise de 0
	// à 1, 88= non renseignable, 99= non réglementé
	public Double getArt_9() {
		return art_9;
	}

	// ART_10_TOP Indicateur de l'unité de mesure de la hauteur du batiment
	// 1_niveaux ; 2_metres du sol au point le plus haut du batiment ; 3_hauteur
	// plafond ; 4_ point le plus haut ; 5_Hauteur de façade à l'égout, 88= non
	// renseignable ; 99= non règlementé
	public int getArt_10_top() {
		return art_10_top;
	}

	// ART_101 Hauteur maximum autorisée 88= non renseignable, 99= non
	// réglementé
	public Double getArt_101() {
		return art_101;
	}

	// ART_102 Hauteur maximum autorisée En mètres
	public Double getArt_102() {
		return art_102;
	}

	// ART_12 Nombre de places par logement 88= non renseignable, 99= non
	// réglementé
	public Double getArt_12() {
		return art_12;
	}

	// ART_13 Part minimale d'espaces libre de toute construction exprimée par
	// rapport à la surface totale de la parcelle Valeur comprise de 0 à 1, 88
	// si non renseignable, 99 si non règlementé
	public Double getArt_13() {
		return art_13;
	}

	// ART_14 Coefficient d'occupation du sol 88= non renseignable, 99= non
	// réglementé
	public Double getArt_14() {
		return art_14;
	}

	@Transient
	IMultiSurface<IOrientableSurface> geomBande = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Regulation [id=" + id + ", numBand=" + numBand + ", insee=" + insee + ", libelle_zone=" + libelle_zone
				+ ", nom_zone=" + nom_zone + ", bandIncons=" + bandIncons + ", empriseSol=" + empriseSol
				+ ", empriseSurface=" + empriseSurface + ", empLargeurMin=" + empLargeurMin + ", empriseSolAlt="
				+ empriseSolAlt + ", band1=" + band1 + ", alignement=" + alignement + ", reculLatMin=" + reculLatMin
				+ ", reculLatMax=" + reculLatMax + ", prospectVoirie1Slope=" + prospectVoirie1Slope
				+ ", prospectVoirie1Hini=" + prospectVoirie1Hini + ", largMaxProspect1=" + largMaxProspect1
				+ ", prospectVoirie2Slope=" + prospectVoirie2Slope + ", prospectVoirie2Hini=" + prospectVoirie2Hini
				+ ", hauteurMaxFacade=" + hauteurMaxFacade + ", band2=" + band2 + ", slopeProspectLat="
				+ slopeProspectLat + ", hIniProspectLat=" + hIniProspectLat + ", hauteurMax2=" + hauteurMax2
				+ ", hIniOppositeProspect=" + hIniOppositeProspect + ", slopeOppositeProspect=" + slopeOppositeProspect
				+ ", code_imu=" + code_imu + ", date_approbation=" + date_approbation + ", fonctions=" + fonctions
				+ ", top_zac=" + top_zac + ", zonage_coherent=" + zonage_coherent + ", correction_zonage="
				+ correction_zonage + ", typ_bande=" + typ_bande + ", bande=" + bande + ", art_71=" + art_71
				+ ", art_74=" + art_74 + ", art_10_top=" + art_10_top + ", libelle_de_base=" + libelle_de_base
				+ ", libelle_de_dul=" + libelle_de_dul + ", art_5=" + art_5 + ", art_72=" + art_72 + ", art_73="
				+ art_73 + ", art_8=" + art_8 + ", art_6=" + art_6 + ", art_9=" + art_9 + ", art_102=" + art_102
				+ ", art_12=" + art_12 + ", art_14=" + art_14 + ", art_13=" + art_13 + ", art_101=" + art_101
				+ ", geomBande=" + geomBande + ", epsilonBuffer=" + epsilonBuffer + ", jtsGeometry=" + jtsGeometry
				+ "]";
	}

	/**
	 * @return the geomBande
	 */
	@JsonIgnore
	public IMultiSurface<IOrientableSurface> getGeomBande() {
		return geomBande;
	}

	@JsonIgnore
	public Geometry getEpsilonBuffer() {

		if (epsilonBuffer == null) {
			epsilonBuffer = this.getJTSBand().buffer(0.5);
		}

		return epsilonBuffer;
	}

	@Transient
	Geometry epsilonBuffer = null;

	/**
	 * @param geomBande
	 *            the geomBande to set
	 */
	public void setGeomBande(IMultiSurface<IOrientableSurface> geomBande) {
		this.geomBande = geomBande;
		this.jtsGeometry = null;
		this.epsilonBuffer = null;
	}

	@Transient
	Geometry jtsGeometry = null;

	private static GeometryFactory gf = new GeometryFactory();

	@JsonIgnore
	public Geometry getJTSBand() {

		if (jtsGeometry == null) {
			try {
				jtsGeometry = AdapterFactory.toGeometry(gf, this.getGeomBande());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jtsGeometry;
	}

	public void setLibelle_zone(String libelle_zone) {
		this.libelle_zone = libelle_zone;
	}

	public void setCode_imu(int code_imu) {
		this.code_imu = code_imu;
	}

	public void setDate_approbation(int date_approbation) {
		this.date_approbation = date_approbation;
	}

	public void setFonctions(int fonctions) {
		this.fonctions = fonctions;
	}

	public void setTop_zac(int top_zac) {
		this.top_zac = top_zac;
	}

	public void setZonage_coherent(int zonage_coherent) {
		this.zonage_coherent = zonage_coherent;
	}

	public void setCorrection_zonage(int correction_zonage) {
		this.correction_zonage = correction_zonage;
	}

	public void setTyp_bande(int typ_bande) {
		this.typ_bande = typ_bande;
	}

	public void setBande(int bande) {
		this.bande = bande;
	}

	public void setArt_71(int art_71) {
		this.art_71 = art_71;
	}

	public void setArt_74(int art_74) {
		this.art_74 = art_74;
	}

	public void setArt_10_top(int art_10_top) {
		this.art_10_top = art_10_top;
	}

	public void setLibelle_de_base(String libelle_de_base) {
		this.libelle_de_base = libelle_de_base;
	}

	public void setLibelle_de_dul(String libelle_de_dul) {
		this.libelle_de_dul = libelle_de_dul;
	}

	public void setArt_5(Double art_5) {
		this.art_5 = art_5;
	}

	public void setArt_72(Double art_72) {
		this.art_72 = art_72;
	}

	public void setArt_73(Double art_73) {
		this.art_73 = art_73;
	}

	public void setArt_8(Double art_8) {
		this.art_8 = art_8;
	}

	public void setArt_6(Double art_6) {
		this.art_6 = art_6;
	}

	public void setArt_9(Double art_9) {
		this.art_9 = art_9;
	}

	public void setArt_102(Double art_102) {
		this.art_102 = art_102;
	}

	public void setArt_12(Double art_12) {
		this.art_12 = art_12;
	}

	public void setArt_14(Double art_14) {
		this.art_14 = art_14;
	}

	public void setArt_13(Double art_13) {
		this.art_13 = art_13;
	}

	public void setArt_101(Double art_101) {
		this.art_101 = art_101;
	}

	public void setEpsilonBuffer(Geometry epsilonBuffer) {
		this.epsilonBuffer = epsilonBuffer;
	}

	public void setJtsGeometry(Geometry jtsGeometry) {
		this.jtsGeometry = jtsGeometry;
	}

	public static void setGf(GeometryFactory gf) {
		Rules.gf = gf;
	}

	public String getNomZone() {
		return nom_zone;
	}

	public Double getBandIncons() {
		return bandIncons;
	}

	public Double getEmpriseSol() {
		return empriseSol;
	}

	public Double getEmpriseSurface() {
		return empriseSurface;
	}

	public Double getEmpLargeurMin() {
		return empLargeurMin;
	}

	public Double getEmpriseSolAlt() {
		return empriseSolAlt;
	}

	public Double getBand1() {
		return band1;
	}

	public Double getAlignement() {
		return alignement;
	}

	public Double getReculLatMin() {
		return reculLatMin;
	}

	public Double getReculLatMax() {
		return reculLatMax;
	}

	public Double getProspectVoirie1Slope() {
		return prospectVoirie1Slope;
	}

	public Double getProspectVoirie1Hini() {
		return prospectVoirie1Hini;
	}

	public Double getLargMaxProspect1() {
		return largMaxProspect1;
	}

	public Double getProspectVoirie2Slope() {
		return prospectVoirie2Slope;
	}

	public Double getProspectVoirie2Hini() {
		return prospectVoirie2Hini;
	}

	public Double getHauteurMaxFacade() {
		return hauteurMaxFacade;
	}

	public Double getBand2() {
		return band2;
	}

	public Double getSlopeProspectLat() {
		return slopeProspectLat;
	}

	public Double getHauteurMax2() {
		return hauteurMax2;
	}

	public Double gethIniProspectLat() {
		return hIniProspectLat;
	}

	public String getNom_zone() {
		return nom_zone;
	}

	public Double gethIniOppositeProspect() {
		return hIniOppositeProspect;
	}

	public Double getSlopeOppositeProspect() {
		return slopeOppositeProspect;
	}

	public void setNom_zone(String nom_zone) {
		this.nom_zone = nom_zone;
	}

	public void setBandIncons(Double bandIncons) {
		this.bandIncons = bandIncons;
	}

	public void setEmpriseSol(Double empriseSol) {
		this.empriseSol = empriseSol;
	}

	public void setEmpriseSurface(Double empriseSurface) {
		this.empriseSurface = empriseSurface;
	}

	public void setEmpLargeurMin(Double empLargeurMin) {
		this.empLargeurMin = empLargeurMin;
	}

	public void setEmpriseSolAlt(Double empriseSolAlt) {
		this.empriseSolAlt = empriseSolAlt;
	}

	public void setBand1(Double band1) {
		this.band1 = band1;
	}

	public void setAlignement(Double alignement) {
		this.alignement = alignement;
	}

	public void setReculLatMin(Double reculLatMin) {
		this.reculLatMin = reculLatMin;
	}

	public void setReculLatMax(Double reculLatMax) {
		this.reculLatMax = reculLatMax;
	}

	public void setProspectVoirie1Slope(Double prospectVoirie1Slope) {
		this.prospectVoirie1Slope = prospectVoirie1Slope;
	}

	public void setProspectVoirie1Hini(Double prospectVoirie1Hini) {
		this.prospectVoirie1Hini = prospectVoirie1Hini;
	}

	public void setLargMaxProspect1(Double largMaxProspect1) {
		this.largMaxProspect1 = largMaxProspect1;
	}

	public void setProspectVoirie2Slope(Double prospectVoirie2Slope) {
		this.prospectVoirie2Slope = prospectVoirie2Slope;
	}

	public void setProspectVoirie2Hini(Double prospectVoirie2Hini) {
		this.prospectVoirie2Hini = prospectVoirie2Hini;
	}

	public void setHauteurMaxFacade(Double hauteurMaxFacade) {
		this.hauteurMaxFacade = hauteurMaxFacade;
	}

	public void setBand2(Double band2) {
		this.band2 = band2;
	}

	public void setSlopeProspectLat(Double slopeProspectLat) {
		this.slopeProspectLat = slopeProspectLat;
	}

	public void sethIniProspectLat(Double hIniProspectLat) {
		this.hIniProspectLat = hIniProspectLat;
	}

	public void setHauteurMax2(Double hauteurMax2) {
		this.hauteurMax2 = hauteurMax2;
	}

	public void sethIniOppositeProspect(Double hIniOppositeProspect) {
		this.hIniOppositeProspect = hIniOppositeProspect;
	}

	public void setSlopeOppositeProspect(Double slopeOppositeProspect) {
		this.slopeOppositeProspect = slopeOppositeProspect;
	}

}
