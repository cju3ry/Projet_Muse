package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * La classe Conferencier représente un conférencier avec ses informations personnelles et ses disponibilités.
 */
public class Conferencier {
	private String id;
	private String nom;
	private String prenom;
	private boolean estEmploye;
	private ArrayList<String> indisponibilite;
	private String[] specialite;
	private String numTel;

	/**
	 * Constructeur de la classe Conferencier.
	 *
	 * @param id l'identifiant du conférencier (doit être de 7 caractères).
	 * @param nom le nom du conférencier.
	 * @param prenom le prénom du conférencier.
	 * @throws IllegalArgumentException si l'identifiant n'est pas de 7 caractères ou est null.
	 */
	public Conferencier(String id, String nom, String prenom) {
		if (id == null) {
			throw new IllegalArgumentException("L'identifiant du conférencier ne peut pas être null.");
		}
		if (id.length() != 7) {
			throw new IllegalArgumentException("L'identifiant du conférencier doit être de 7 caractères.");
		}
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
	}

	/**
	 * Définit le numéro de téléphone du conférencier.
	 *
	 * @param numTel le numéro de téléphone (doit être de 10 caractères).
	 * @throws IllegalArgumentException si le numéro de téléphone n'est pas de 10 caractères ou est null.
	 */
	public void setNumTel(String numTel) {
		if (numTel == null) {
			throw new IllegalArgumentException("Le numéro de téléphone ne peut pas être null.");
		}
		if (numTel.length() != 10) {
			throw new IllegalArgumentException("Le numéro de téléphone doit être de 10 caractères.");
		}
		this.numTel = numTel;
	}

	/**
	 * Définit les spécialités du conférencier.
	 *
	 * @param specialite un tableau de spécialités (ne doit pas dépasser 6 éléments).
	 * @throws IllegalArgumentException si le tableau de spécialités dépasse 6 éléments.
	 */
	public void setSpecialitees(String[] specialite) {
		if (specialite.length > 6) {
			throw new IllegalArgumentException("Le nombre de spécialités ne doit pas dépasser 6 éléments.");
		}
		this.specialite = specialite;
	}

	/**
	 * Définit si le conférencier est un employé.
	 *
	 * @param estEmploye true si le conférencier est un employé, false sinon.
	 */
	public void setEstEmploye(boolean estEmploye) {
		this.estEmploye = estEmploye;
	}

	/**
	 * Définit les indisponibilités du conférencier.
	 *
	 * @param indisponibilite une liste d'indisponibilités.
	 * @throws IllegalArgumentException si les indisponibilités ne sont pas valides.
	 */
	public void setIndisponibilite(ArrayList<String> indisponibilite) {
		if (!indisponibiliteOk(indisponibilite)) {
			throw new IllegalArgumentException("Les dates d'indisponibilité sont invalides ou non cohérentes.");
		}

		// On vérifier que les indisponibilités sont sans duplicata (unique)
		Set<String> uniqueIndisponibilites = new HashSet<>(indisponibilite);
		this.indisponibilite = new ArrayList<>(uniqueIndisponibilites);
	}

	/**
	 * Vérifie si les indisponibilités sont valides.
	 *
	 * @param indisponibilite une liste d'indisponibilités.
	 * @return true si les indisponibilités sont valides, false sinon.
	 */
	private boolean indisponibiliteOk(ArrayList<String> indisponibilite) {
		int increment = 1;
		SimpleDateFormat formatIndisponibilite = new SimpleDateFormat("dd/MM/yyyy");
		formatIndisponibilite.setLenient(false);

		if (indisponibilite.isEmpty()) {
			return true;
		}

		for (int j = 0; j < indisponibilite.size(); j += 2) {
			try {
				if (formatIndisponibilite.parse(indisponibilite.get(j)).getTime() > 
				    formatIndisponibilite.parse(indisponibilite.get(increment)).getTime()) {
					return false;
				}
			} catch (ParseException e) {
				return false;
			}
			increment += 2;
		}
		return true;
	}

	/**
	 * Retourne l'identifiant du conférencier.
	 *
	 * @return l'identifiant du conférencier.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Retourne le nom du conférencier.
	 *
	 * @return le nom du conférencier.
	 */
	public String getNom() {
		return this.nom;
	}

	/**
	 * Retourne le prénom du conférencier.
	 *
	 * @return le prénom du conférencier.
	 */
	public String getPrenom() {
		return this.prenom;
	}

	/**
	 * Retourne une représentation sous forme de chaîne de caractères du conférencier.
	 *
	 * @return une chaîne de caractères représentant le conférencier.
	 * @
	 */
	@Override
	public String toString() {
	    // Conversion du tableau de spécialités dans une string
	    String specialites = (specialite == null) ? "Aucune spécialité" : String.join(", ", specialite);

	    // Conversion de la liste des indisponibilités en string
	    String indisponibilites = (indisponibilite == null || indisponibilite.isEmpty()) 
	        ? "Aucune indisponibilité" 
	        : String.join(", ", indisponibilite); // mettre des virgules entre les indisponibilités

	    return "\tConférencier/Conférencière : \n\tNom : " + nom + " \n\tPrenom : " + prenom + "\n" +
	           "\tEmployé : " + (estEmploye ? "Oui" : "Non") + "\n" +
	           "\tNuméro de téléphone : " + this.numTel + "\n" +
	           "\tSpécialités : " + specialites.substring(1, specialites.length()-1) + "\n" +
	           "\tIndisponibilités : " + indisponibilites + "\n";
	}

	/**
	 * Retourne la liste des indisponibilités du conférencier.
	 *
	 * @return une liste de chaînes représentant les indisponibilités du conférencier.
	 */
	public ArrayList<String> getIndisponibilite() {
		return this.indisponibilite;
	}

	/**
	 * Retourne le tableau des spécialités du conférencier.
	 *
	 * @return un tableau de chaînes représentant les spécialités du conférencier.
	 */
	public String[] getSpecialites() {
		return this.specialite;
	}

	/**
	 * Retourne le numéro de téléphone du conférencier.
	 *
	 * @return une chaîne représentant le numéro de téléphone du conférencier.
	 */
	public String getNumTel() {
		return this.numTel;
	}
	
	/**
	 * Retourne le booléen pour savoir si le conférencier est interne ou externe.
	 *
	 * @return un booléen pour savoir si le conférencier est interne ou externe.
	 */
	public boolean getEstEmploye() {
		return this.estEmploye;
	}
}
