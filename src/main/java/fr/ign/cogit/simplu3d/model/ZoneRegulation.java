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
public class ZoneRegulation {

	@Id
	@GeneratedValue
	private Long id;

	private int numBand;

	private String insee;

	private String libelleZone;

	private String nomZone;
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
	private int codeImu, dateApprobation, fonctions, topZac, zonageCoherent, correctionZonage, typBande, bande,
			art71, art74, art10top;
	private String libelleDeBase, libelleDeDul;

	private Double art5, art72, art73, art8, art6, art9, art102, art12, art14, art13, art101;

	public ZoneRegulation() {
	}

	public ZoneRegulation(int code_imu, String libelle_zone, String insee, int date_approbation, String libelle_de_base,
			String libelle_de_dul, int fonctions, int top_zac, int zonage_coherent, int correction_zonage,
			int typ_bande, int bande, Double art_5, Double art_6, int art_71, Double art_72, Double art_73, int art_74,
			Double art_8, Double art_9, int art_10_top, Double art_101, Double art_102, Double art_12, Double art_13,
			Double art_14) {
		super();
		this.codeImu = code_imu;
		this.libelleZone = libelle_zone;
		this.insee = insee;
		this.dateApprobation = date_approbation;
		this.libelleDeBase = libelle_de_base;
		this.libelleDeDul = libelle_de_dul;
		this.fonctions = fonctions;
		this.topZac = top_zac;
		this.zonageCoherent = zonage_coherent;
		this.correctionZonage = correction_zonage;
		this.typBande = typ_bande;
		this.bande = bande;
		this.art5 = art_5;
		this.art6 = art_6;
		this.art71 = art_71;
		this.art72 = art_72;
		this.art73 = art_73;
		this.art74 = art_74;
		this.art8 = art_8;
		this.art9 = art_9;
		this.art10top = art_10_top;
		this.art101 = art_101;
		this.art102 = art_102;
		this.art12 = art_12;
		this.art13 = art_13;
		this.art14 = art_14;
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
	public ZoneRegulation(String nom_zone, Double bandIncons, Double empriseSol, Double empriseSurface, Double empLargeurMin,
			Double empriseSolAlt, Double band1, Double alignement, Double reculLatMin, Double reculLatMax,
			Double prospectVoirie1Slope, Double prospectVoirie1Hini, Double largMaxProspect1,
			Double prospectVoirie2Slope, Double prospectVoirie2Hini, Double hauteurMaxFacade, Double band2,
			Double slopeProspectLat, Double hIniProspectLat, Double hauteurMax2) {
		super();
		this.nomZone = nom_zone;
		this.bandIncons = bandIncons;
		this.empriseSol = empriseSol;
		this.empriseSurface = empriseSurface;
		this.empLargeurMin = empLargeurMin;
		this.empriseSolAlt = empriseSolAlt;
		this.band1 = band1;
		this.alignement = alignement;
		this.art9 = empriseSol;
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
	
	public int getNumBand() {
		return numBand;
	}

	public void setNumBand(int numBand) {
		this.numBand = numBand;
	}
	

	public int getCode_imu() {
		return codeImu;
	}

	public String getLibelle_zone() {
		return libelleZone;
	}

	public String getInsee() {
		return insee;
	}

	public void setInsee(String insee) {
		this.insee = insee;
	}

	// DATE_DUL Date d'approbation du PLU
	public int getDateApprobation() {
		return dateApprobation;
	}

	// LIBELLE_DE_BASE Type de zone
	public String getLibelleDeBase() {
		return libelleDeBase;
	}

	// LIBELLE_DE_DUL Type de zone
	public String getLibelledeDul() {
		return libelleDeDul;
	}

	// FONCTIONS Fonction de la zone 0 : logements uniquement ; 1 : activité
	// mais possibilité de logements ; 2 : exclusivement activité
	public int getFonctions() {
		return fonctions;
	}

	// TOP_ZAC Présence d'une ZAC 0 : NON // 1 : OUI
	public int getTopZac() {
		return topZac;
	}

	// ZONAGE_COHERENT Zonage cohérent entre la base et le plan de zonage du DUL
	// 0 : NON // 1 : OUI
	public int getZonageCoherent() {
		return zonageCoherent;
	}

	// CORRECTION_ZONAGE Indicateur du zonage à faire prévaloir en cas
	// d'incohérence des zonages 0 : Conserver le dessin de zonage de Carto PLU
	// 1 : Numeriser le zonage du PLU analysé
	public int getCorrectionZonage() {
		return correctionZonage;
	}

	// TYP_BANDE Information concernant l'existence d'une bande principale ou
	// secondaire 0 : pas de bande // 1 : principale // 2 : secondaire
	public int getTypBande() {
		return typBande;
	}

	// BANDE Profondeur de la bande principale x > 0 profondeur de la bande par
	// rapport à la voirie
	public int getBande() {
		return bande;
	}

	// ART_5 Superficie minimale 88= non renseignable, 99= non réglementé

	public Double getArt5() {
		return art5;
	}

	// ART_6 Distance minimale des constructions par rapport à la voirie imposée
	// en mètre 88= non renseignable, 99= non réglementé
	public Double getArt6() {
		return art6;
	}

	// ART_71 Implantation en limite séparative 0 : non, retrait imposé (cf.72)
	// // 1 : Oui // 2 : Oui, mais sur un côté seulement
	public int getArt_71() {
		return art71;
	}

	// ART_72 Distance minimale des constructions par rapport aux limites
	// séparatives imposée en mètre 88= non renseignable, 99= non réglementé
	public Double getArt72() {
		return art72;
	}

	// ART_73 Distance minimale des constructions par rapport à la limte
	// séparative de fond de parcelle 88= non renseignable, 99= non réglementé
	public Double getArt73() {
		return art73;
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
	public int getArt74() {
		return art74;
	}

	// ART_8 Distance minimale des constructions par rapport aux autres sur une
	// même propriété imposée en mètre 88= non renseignable, 99= non réglementé
	public Double getArt8() {
		return art8;
	}

	// ART_9 Pourcentage d'emprise au sol maximum autorisé Valeur comprise de 0
	// à 1, 88= non renseignable, 99= non réglementé
	public Double getArt9() {
		return art9;
	}

	// ART_10_TOP Indicateur de l'unité de mesure de la hauteur du batiment
	// 1_niveaux ; 2_metres du sol au point le plus haut du batiment ; 3_hauteur
	// plafond ; 4_ point le plus haut ; 5_Hauteur de façade à l'égout, 88= non
	// renseignable ; 99= non règlementé
	public int getArt10top() {
		return art10top;
	}

	// ART_101 Hauteur maximum autorisée 88= non renseignable, 99= non
	// réglementé
	public Double getArt101() {
		return art101;
	}

	// ART_102 Hauteur maximum autorisée En mètres
	public Double getArt102() {
		return art102;
	}

	// ART_12 Nombre de places par logement 88= non renseignable, 99= non
	// réglementé
	public Double getArt12() {
		return art12;
	}

	// ART_13 Part minimale d'espaces libre de toute construction exprimée par
	// rapport à la surface totale de la parcelle Valeur comprise de 0 à 1, 88
	// si non renseignable, 99 si non règlementé
	public Double getArt13() {
		return art13;
	}

	// ART_14 Coefficient d'occupation du sol 88= non renseignable, 99= non
	// réglementé
	public Double getArt14() {
		return art14;
	}

	@Transient
	IMultiSurface<IOrientableSurface> geomBande = null;

	
	@Override
	public String toString() {
		return "Rules [id=" + id + ", numBand=" + numBand + ", insee=" + insee + ", libelleZone=" + libelleZone
				+ ", nomZone=" + nomZone + ", bandIncons=" + bandIncons + ", empriseSol=" + empriseSol
				+ ", empriseSurface=" + empriseSurface + ", empLargeurMin=" + empLargeurMin + ", empriseSolAlt="
				+ empriseSolAlt + ", band1=" + band1 + ", alignement=" + alignement + ", reculLatMin=" + reculLatMin
				+ ", reculLatMax=" + reculLatMax + ", prospectVoirie1Slope=" + prospectVoirie1Slope
				+ ", prospectVoirie1Hini=" + prospectVoirie1Hini + ", largMaxProspect1=" + largMaxProspect1
				+ ", prospectVoirie2Slope=" + prospectVoirie2Slope + ", prospectVoirie2Hini=" + prospectVoirie2Hini
				+ ", hauteurMaxFacade=" + hauteurMaxFacade + ", band2=" + band2 + ", slopeProspectLat="
				+ slopeProspectLat + ", hIniProspectLat=" + hIniProspectLat + ", hauteurMax2=" + hauteurMax2
				+ ", hIniOppositeProspect=" + hIniOppositeProspect + ", slopeOppositeProspect=" + slopeOppositeProspect
				+ ", codeImu=" + codeImu + ", dateApprobation=" + dateApprobation + ", fonctions=" + fonctions
				+ ", topZac=" + topZac + ", zonageCoherent=" + zonageCoherent + ", correctionZonage=" + correctionZonage
				+ ", typBande=" + typBande + ", bande=" + bande + ", art71=" + art71 + ", art74=" + art74
				+ ", art10top=" + art10top + ", libelleDeBase=" + libelleDeBase + ", libelleDeDul=" + libelleDeDul
				+ ", art5=" + art5 + ", art72=" + art72 + ", art73=" + art73 + ", art8=" + art8 + ", art6=" + art6
				+ ", art9=" + art9 + ", art102=" + art102 + ", art12=" + art12 + ", art14=" + art14 + ", art13=" + art13
				+ ", art101=" + art101 + ", geomBande=" + geomBande + ", epsilonBuffer=" + epsilonBuffer
				+ ", jtsGeometry=" + jtsGeometry + "]";
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

	
	public void setEpsilonBuffer(Geometry epsilonBuffer) {
		this.epsilonBuffer = epsilonBuffer;
	}
	
	@JsonIgnore
	public Geometry getJtsGeometry() {
		return jtsGeometry;
	}

	public void setJtsGeometry(Geometry jtsGeometry) {
		this.jtsGeometry = jtsGeometry;
	}

	public static void setGf(GeometryFactory gf) {
		ZoneRegulation.gf = gf;
	}

	public String getNomZone() {
		return nomZone;
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
		return nomZone;
	}

	public Double gethIniOppositeProspect() {
		return hIniOppositeProspect;
	}

	public Double getSlopeOppositeProspect() {
		return slopeOppositeProspect;
	}

	public void setNom_zone(String nom_zone) {
		this.nomZone = nom_zone;
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

	public void setLibelleZone(String libelleZone) {
		this.libelleZone = libelleZone;
	}

	public void setNomZone(String nomZone) {
		this.nomZone = nomZone;
	}

	public void setCodeImu(int codeImu) {
		this.codeImu = codeImu;
	}

	public void setDateApprobation(int dateApprobation) {
		this.dateApprobation = dateApprobation;
	}

	public void setFonctions(int fonctions) {
		this.fonctions = fonctions;
	}

	public void setTopZac(int topZac) {
		this.topZac = topZac;
	}

	public void setZonageCoherent(int zonageCoherent) {
		this.zonageCoherent = zonageCoherent;
	}

	public void setCorrectionZonage(int correctionZonage) {
		this.correctionZonage = correctionZonage;
	}

	public void setTypBande(int typBande) {
		this.typBande = typBande;
	}

	public void setBande(int bande) {
		this.bande = bande;
	}

	public void setArt71(int art71) {
		this.art71 = art71;
	}

	public void setArt74(int art74) {
		this.art74 = art74;
	}

	public void setArt10top(int art10top) {
		this.art10top = art10top;
	}

	public void setLibelleDeBase(String libelleDeBase) {
		this.libelleDeBase = libelleDeBase;
	}

	public void setLibelleDeDul(String libelleDeDul) {
		this.libelleDeDul = libelleDeDul;
	}

	public void setArt5(Double art5) {
		this.art5 = art5;
	}

	public void setArt72(Double art72) {
		this.art72 = art72;
	}

	public void setArt73(Double art73) {
		this.art73 = art73;
	}

	public void setArt8(Double art8) {
		this.art8 = art8;
	}

	public void setArt6(Double art6) {
		this.art6 = art6;
	}

	public void setArt9(Double art9) {
		this.art9 = art9;
	}

	public void setArt102(Double art102) {
		this.art102 = art102;
	}

	public void setArt12(Double art12) {
		this.art12 = art12;
	}

	public void setArt14(Double art14) {
		this.art14 = art14;
	}

	public void setArt13(Double art13) {
		this.art13 = art13;
	}

	public void setArt101(Double art101) {
		this.art101 = art101;
	}
	
	

}
