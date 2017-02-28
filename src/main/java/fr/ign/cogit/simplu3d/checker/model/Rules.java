package fr.ign.cogit.simplu3d.checker.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import fr.ign.cogit.simplu3d.model.Regulation;

public class Rules extends Regulation {

	private String nom_zone;
	private double bandIncons; // Largeur de la bande non constructible (6 m)
	private double empriseSol; // CES de base (0.5)
	private double empriseSurface; // condition de surface du terrain pour le
									// CES
									// de base (500 m²)
	private double empLargeurMin; // condition de largeur des façades pour le
									// CES
									// de base (16 m)
	private double empriseSolAlt; // CES si les conditions ne sont pas vérifiées
									// (0.4)
	private double band1; // Largeur pour la bande 1 à partir de la voirie (
							// 16.5)
	private double alignement; // Alignement par rapport à la voirie en bande 1
								// (0)
	private double reculLatMin; // Recul maximal en bande 1 par rapport aux
								// limites latérales (0)
	private double reculLatMax; // /Recul minimal en bande 1 par rapport aux
								// limites latérales (4)
	private double prospectVoirie1Slope; // Prospect (pente) de base par rapport
											// au côté oppposé de la voirie (1)
	private double prospectVoirie1Hini; // Prospect (hIni) de base par rapport
										// au
										// côté oppposé de la voirie (1)
	private double largMaxProspect1; // Largeur de la route à ne pas dépasser
										// pour
										// avoir le prospect de base (11 m)
	private double prospectVoirie2Slope; // Prospect alternatif (pente) par
											// rapport au côté oppposé de la
											// voirie
											// (1)
	private double prospectVoirie2Hini; // Prospect alternatif (hIni) par
										// rapport
										// au côté oppposé de la voirie (3)
	private double hauteurMaxFacade; // Hauteur maximale du bâtiment par rapport
										// à
										// la facade (6 m)
	private double band2; // Taille de la bande 2 à partir de la bande 1 (8.5 m
							// )
	private double slopeProspectLat; // Pente de prospect bande 2 par rapport
										// aux
										// limites latérales (1)
	private double hIniProspectLat; // Hauteur initiale de prospect bande 2 par
									// rapport aux limites latérales (3.5)
	private double hauteurMax2; // Hauteur maximale des bâtiments en bande 2.

	private double hIniOppositeProspect; // Paramètres de hauteur par rapport au
											// côté opposé de la voirie
	private double slopeOppositeProspect; //

	public Rules(String nom_zone, double bandIncons, double empriseSol, double empriseSurface, double empLargeurMin,
			double empriseSolAlt, double band1, double alignement, double reculLatMin, double reculLatMax,
			double prospectVoirie1Slope, double prospectVoirie1Hini, double largMaxProspect1,
			double prospectVoirie2Slope, double prospectVoirie2Hini, double hauteurMaxFacade, double band2,
			double slopeProspectLat, double hIniProspectLat, double hauteurMax2) {
		super();
		this.nom_zone = nom_zone;
		this.bandIncons = bandIncons;
		this.empriseSol = empriseSol;
		this.empriseSurface = empriseSurface;
		this.empLargeurMin = empLargeurMin;
		this.empriseSolAlt = empriseSolAlt;
		this.band1 = band1;
		this.alignement = alignement;
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

	public Rules(String str) {
		this(str.split(","));
	}
	
	public Rules(){
		
	}

	public Rules(String[] tabS) {
		this(

				tabS[0], // nomZone
				Double.parseDouble(tabS[1]), Double.parseDouble(tabS[2]), Double.parseDouble(tabS[3]),
				Double.parseDouble(tabS[4]), Double.parseDouble(tabS[5]), Double.parseDouble(tabS[6]),
				Double.parseDouble(tabS[7]), Double.parseDouble(tabS[8]), Double.parseDouble(tabS[9]),
				Double.parseDouble(tabS[10]), Double.parseDouble(tabS[11]), Double.parseDouble(tabS[12]),
				Double.parseDouble(tabS[13]), Double.parseDouble(tabS[14]), Double.parseDouble(tabS[15]),
				Double.parseDouble(tabS[16]), Double.parseDouble(tabS[17]), Double.parseDouble(tabS[18]),
				Double.parseDouble(tabS[19])

		);

	}

	public static Map<String, Rules> loadRules(String path) throws IOException {

		// On initialise la map
		Map<String, Rules> table = new Hashtable<>();

		// On charge le fichier CSV avec modèle-Rennes
		File f = new File(path);
		if (!f.exists()) {
			return table;
		}

		// On lit le fichier
		BufferedReader in = new BufferedReader(new FileReader(f));
		// On saute la première ligne car c'est une en-tête
		String line = in.readLine();
		// On traite chaque ligne
		while ((line = in.readLine()) != null) {

			// On instancier la réglementation
			Rules r = new Rules(line);
			// On regarde si le code imu a été rencontré auparavant
			String nom_Zone = r.getNomZone();
			table.put(nom_Zone, r);

		}

		in.close();

		return table;
	}

	public String getNomZone() {
		return nom_zone;
	}

	public double getBandIncons() {
		return bandIncons;
	}

	public double getEmpriseSol() {
		return empriseSol;
	}

	public double getEmpriseSurface() {
		return empriseSurface;
	}

	public double getEmpLargeurMin() {
		return empLargeurMin;
	}

	public double getEmpriseSolAlt() {
		return empriseSolAlt;
	}

	public double getBand1() {
		return band1;
	}

	public double getAlignement() {
		return alignement;
	}

	public double getReculLatMin() {
		return reculLatMin;
	}

	public double getReculLatMax() {
		return reculLatMax;
	}

	public double getProspectVoirie1Slope() {
		return prospectVoirie1Slope;
	}

	public double getProspectVoirie1Hini() {
		return prospectVoirie1Hini;
	}

	public double getLargMaxProspect1() {
		return largMaxProspect1;
	}

	public double getProspectVoirie2Slope() {
		return prospectVoirie2Slope;
	}

	public double getProspectVoirie2Hini() {
		return prospectVoirie2Hini;
	}

	public double getHauteurMaxFacade() {
		return hauteurMaxFacade;
	}

	public double getBand2() {
		return band2;
	}

	public double getSlopeProspectLat() {
		return slopeProspectLat;
	}

	public double getHauteurMax2() {
		return hauteurMax2;
	}

	public double gethIniProspectLat() {
		return hIniProspectLat;
	}
	
	

	public String getNom_zone() {
		return nom_zone;
	}

	public double gethIniOppositeProspect() {
		return hIniOppositeProspect;
	}

	public double getSlopeOppositeProspect() {
		return slopeOppositeProspect;
	}

	
	
	
	public String toString() {
		return "Nom zone " + this.getNomZone() + " - bandIncons " + this.getBandIncons() + " - EmpriseSol "
				+ this.getEmpriseSol() + " - EmpSurfaceMin " + this.getEmpriseSurface() + " - EmpLargeurMin "
				+ this.getEmpLargeurMin() + " - EmpriseSolAlt " + this.getEmpriseSolAlt() + " - Band1 "
				+ this.getBand1() + " - Alignement " + this.getAlignement() + " - ReculLateralMin "
				+ this.getReculLatMin() + " - ReculLateralMax " + this.getReculLatMax() + " - prospectVoirie1Slope "
				+ this.getProspectVoirie1Slope() + " - prospectVoirie1Hini " + this.getProspectVoirie1Hini()
				+ " - largMaxProspect1 " + this.getLargMaxProspect1() + " - prospectVoirie2Slope "
				+ this.getProspectVoirie2Slope() + " - prospectVoirie2Hini " + this.getProspectVoirie2Hini()
				+ " - hauteurMaxFacade " + this.getHauteurMaxFacade() + " - Band2 " + this.getBand2()
				+ " - slopeProspectLatSlope " + this.getSlopeProspectLat() + " - hIniProspectLat "
				+ this.gethIniProspectLat() + " - hauteurMax2 " + this.getHauteurMax2();

	}

}
