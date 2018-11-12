package fr.ign.cogit.simplu3d.model;

/**
 * Interface permettant de définir les contraintes réglementaires et les
 * paramètres de règles s'appliquant au niveau d'une zone réglementaire.
 * 
 * 
 * @author mbrasebin
 *
 */
public interface IZoneRegulation {

	/**
	 * 
	 * @return La zone urbaine sur laquelle s'applique les contraintes
	 *         réglementaires définies dans l'interface
	 */
	public UrbaZone getUrbaZone();

	/**
	 * 
	 * @return Une représentation textuelle pour mieux comprendre le contenu de la
	 *         classe implémentée.
	 */
	public String toText();

}
