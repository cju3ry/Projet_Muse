package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
	 * @throws ConferencierException si l'identifiant n'est pas de 7 caractères ou est null.
	 */
	public Conferencier(String id, String nom, String prenom) throws ConferencierException {
		if (id == null || id.length() != 7) {
			throw new ConferencierException();
		}
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
	}

	/**
	 * Définit le numéro de téléphone du conférencier.
	 *
	 * @param numTel le numéro de téléphone (doit être de 10 caractères).
	 * @throws ConferencierException si le numéro de téléphone n'est pas de 10 caractères ou est null.
	 */
	public void setNumTel(String numTel) throws ConferencierException {
		if (numTel == null || numTel.length() != 10) {
			throw new ConferencierException();
		}
		this.numTel = numTel;
	}

	/**
	 * Définit les spécialités du conférencier.
	 *
	 * @param specialite un tableau de spécialités (ne doit pas dépasser 6 éléments).
	 * @throws ConferencierException si le tableau de spécialités dépasse 6 éléments.
	 */
	public void setSpecialitees(String[] specialite) throws ConferencierException {
		if (specialite.length > 6) {
			throw new ConferencierException();
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
	 * @throws ConferencierException si les indisponibilités ne sont pas valides.
	 */
	public void setIndisponibilite(ArrayList<String> indisponibilite) throws ConferencierException {
		if (!indisponibiliteOk(indisponibilite)) {
			throw new ConferencierException();
		}
		this.indisponibilite = indisponibilite;
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
				if (formatIndisponibilite.parse(indisponibilite.get(j)).getTime() > formatIndisponibilite.parse(indisponibilite.get(increment)).getTime()) {
					return false;
				}
			} catch (ParseException e) {
				System.err.println("Erreur de parsing de la date : " + e.getMessage());
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
	 */
	public String toString() {
		return "	Conférencier/Conférencière : " + this.nom + " " + this.prenom + "\n";
	}
}