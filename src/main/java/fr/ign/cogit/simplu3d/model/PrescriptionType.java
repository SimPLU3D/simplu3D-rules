package fr.ign.cogit.simplu3d.model;

/*
 * 01 : espace boisé classé (R123-11 a)
02 : secteur avec limitation de la constructibilité ou de 
l’occupation pour des raisons de nuisances ou de 
risques (R123-11b)
03 : secteur avec disposition de 
reconstruction/démolition (L123-1-5 III 4° et R123-11 f)
04 : périmètre issu des PDU sur obligation de 
stationnement ((R123-11 g)
05 : emplacement réservé (L123-1-5 V et R123-11 d)
06 : secteur à densité maximale pour les 
reconstructions ou aménagements de bâtiments 
existants (L123-1-5 5 [abrogé] et R123-11 e)
07 : élément de paysage (bâti et espaces), de 
patrimoine, point de vue à protéger, à mettre en valeur, 
notamment pour la préservation, le maintien ou la 
remise en état des continuités écologiques (L123-1-5 
III 2° et R123-11 h)
08 : terrain cultivé et espaces non bâtis nécessaires au
maintien  des continuités écologiques à protéger à 
protéger en zone urbaine (L123-1-5 III 5° et R123-12 1)
09 : emplacement réservé logement social/mixité 
sociale (L123-2b et R123-12 1)
10 : pré- emplacement réservé pour des équipements 
(R123-11 d) – Attention doublon avec l’occurrence 21. 
Privilégier dorénavant la 21.
11 : limitations particulières d'implantation des 
constructions (bande constructible, marge de recul, 
zone non aedificandi, alignement, emprise des 
constructions...)
12 : secteur de projet, en attente d’un projet 
d’aménagement global (L123-2 a et R123-12 4b)
13 : zone à aménager en vue de la pratique du ski 
(L123-1-5 IV 1° et R123-11 j)
14 : secteur de plan de masse (R123-12 5)
15 : règles d'implantation des constructions par rapport
aux voies, emprises publiques et limites séparatives 
(R123-9 6° et 7°, et R123-11)
16 : secteurs de taille et de capacité d'accueil limitées, 
bâtiment en zone naturelle ou agricole susceptible de 
changer de destination (L 123-1-5 II 6°, et R123-12 2)
17 : secteur à programme de logements mixité sociale 
en Zone U et AU  (L123-1-5 II 4° et R123-12 4f)
18 : secteur comportant des orientations 
d’aménagement et de programmation (L123-1-4 et 
R123-3-1)
19 : secteur protégé en raison de la richesse du sol et 
du sous-sol (R123-11 c)
20 : secteur à transfert de constructibilité en zone N 
(L123-4 et R123-12 3). 
21 : terrain concerné par la localisation d'équipements 
en zone U et AU, voies, ouvrages publics, installations 
d'intérêt général, espaces verts (L123-2c et R123-12 
4d)
22 : secteur de diversité commerciale à protéger (L123-
1-5 II 5°)
 */

/**
 * 
 * 
 * Note that this enumeration corresponds to TYPEPSC2 in CNIG standard for PLU
 * 
 * @author MBorne
 *
 */
public enum PrescriptionType {
	ESPACE_BOISE(1, "Espace boisé classé (R123-11 a)"), 
	NUISANCES_RISQUES(2, "secteur avec limitation de la constructibilité ou de l’occupation pour des raisons de nuisances ou de risques"),
	ZONE_DEMOLITION(3,"Zone de démolition préalable"),
	EMPLACEMENT_RESERVE(5, "Emplacement réservé"),
	ELEMENT_PAYSAGE(7, "Elément de paysage (bâti et espaces), à mettre en valeur"), 
	JARDIN(8,"Protection de jardins à cultiver"),
	RECOIL(	11, "Limitations particulières d'implantation des constructions"), 
	SECTEUR_MIXITE(17, "secteur à programme de logements mixité sociale "),
	FACADE_ALIGNMENT(12, "Alignement de façade"), 
	TVB(25,"Espaces et secteurs contribuant aux continuités écologiques et à la trame verte et bleue: Bois, haie, bosquet, à mettre en valeur au titre de l'article R 123-11-i"),
	UNKNOWN(99, "Inconnu");

	private String text = "";
	private int id;

	PrescriptionType(int id, String text) {
		this.text = text;
		this.id = id;
	}

	public String toString() {
		return text;
	}

	public int getId() {
		return id;
	}

	public static PrescriptionType getPrescriptionById(int id) {

		PrescriptionType[] pTArray = PrescriptionType.values();
		int length = pTArray.length;
		for (int i = 0; i < length; i++) {
			PrescriptionType pTTemp = pTArray[i];

			if (pTTemp.getId() == id) {
				return pTTemp;
			}

		}

		return PrescriptionType.UNKNOWN;
	}
}
