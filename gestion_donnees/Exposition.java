package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * La classe Exposition représente une exposition avec ses informations détaillées.
 * Elle permet de gérer les expositions temporaires et permanentes, ainsi que leurs périodes, nombre d'œuvres, mots-clés et résumés.
 */
public class Exposition {
	private String id;
	private String intitule;
	private String resume;
	private int nbOeuvre;
	private boolean estTemporaire;
	private String[] motCles;
	private Date periodeDebut;
	private Date periodeFin;
	private Date debutExpo;
	private Date finExpo;

	/**
	 * Constructeur de la classe Exposition.
	 *
	 * @param id l'identifiant unique de l'exposition (doit être de 7 caractères).
	 * @param intitule l'intitulé de l'exposition.
	 * @throws ExpositionException si l'identifiant n'a pas une longueur de 7 caractères ou si l'intitulé est null.
	 */
	public Exposition(String id, String intitule) throws ExpositionException {
		if (id.length() != 7 || intitule == null) {
			throw new ExpositionException();
		}
		this.id = id;
		this.intitule = intitule;
	}

	/**
	 * Constructeur de la classe Exposition pour les expositions temporaires.
	 *
	 * @param id l'identifiant unique de l'exposition (doit être de 7 caractères).
	 * @param intitule l'intitulé de l'exposition.
	 * @param debutExpo la date de début de l'exposition (format "dd/MM/yyyy").
	 * @param finExpo la date de fin de l'exposition (format "dd/MM/yyyy").
	 * @throws ExpositionException si l'identifiant n'a pas une longueur de 7 caractères, si l'intitulé est null, ou si les dates sont invalides.
	 */
	public Exposition(String id, String intitule, String debutExpo, String finExpo) throws ExpositionException {
		SimpleDateFormat tempsExpo = new SimpleDateFormat("dd/MM/yyyy");
		tempsExpo.setLenient(false);

		if (id.length() != 7 || intitule == null) {
			throw new ExpositionException();
		}

		// vérification de l'année seulement
		if (debutExpo.substring(7).length() > 4
				|| finExpo.substring(7).length() > 4) {
			throw new ExpositionException();
		}

		try {
			if (tempsExpo.parse(debutExpo).getTime() > tempsExpo.parse(finExpo).getTime()) {
				throw new ExpositionException();
			}

			this.debutExpo = tempsExpo.parse(debutExpo);
			this.finExpo = tempsExpo.parse(finExpo);

		} catch (ParseException e) {
			throw new ExpositionException();
		}

		this.id = id;
		this.intitule = intitule;
	}

	/**
	 * Retourne l'identifiant de l'exposition.
	 *
	 * @return l'identifiant de l'exposition.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Définit la période de l'exposition.
	 *
	 * @param periodeDebut l'année de début de l'exposition (format "yyyy").
	 * @param periodeFin l'année de fin de l'exposition (format "yyyy").
	 * @throws ExpositionException si les années sont invalides ou si la période de début est après la période de fin.
	 */
	public void setPeriode(String periodeDebut, String periodeFin) throws ExpositionException {
		SimpleDateFormat periode = new SimpleDateFormat("yyyy");
		periode.setLenient(false);

		if (periodeDebut == null || periodeFin == null) {
			throw new ExpositionException();
		}

		if (Integer.parseInt(periodeDebut) > 2024 || Integer.parseInt(periodeFin) > 2024) {
			throw new ExpositionException();
		}

		try {
			if (periode.parse(periodeDebut).getTime() > periode.parse(periodeFin).getTime()) {
				throw new ExpositionException();
			}

			if (periodeDebut != null && periodeFin != null) {
				this.periodeDebut = periode.parse(periodeDebut);
				this.periodeFin = periode.parse(periodeFin);
			} else {
				throw new ExpositionException();
			}

		} catch (ParseException e) {
			throw new ExpositionException();
		}
	}

	/**
	 * Définit le nombre d'œuvres de l'exposition.
	 *
	 * @param nbOeuvre le nombre d'œuvres (doit être supérieur à 0).
	 * @throws ExpositionException si le nombre d'œuvres est inférieur ou égal à 0.
	 */
	public void setNbOeuvre(int nbOeuvre) throws ExpositionException {
		if (nbOeuvre <= 0) {
			throw new ExpositionException();
		}

		this.nbOeuvre = nbOeuvre;
	}

	/**
	 * Définit les mots-clés de l'exposition.
	 *
	 * @param motCles un tableau de mots-clés (doit contenir entre 1 et 10 éléments).
	 * @throws ExpositionException si le tableau de mots-clés est vide ou contient plus de 10 éléments.
	 */
	public void setMotCles(String[] motCles) throws ExpositionException {
		if (motCles.length > 10 || motCles.length <= 0) {
			throw new ExpositionException();
		}

		this.motCles = motCles;
	}

	/**
	 * Définit le résumé de l'exposition.
	 *
	 * @param resume le résumé de l'exposition.
	 * @throws ExpositionException si le résumé est null.
	 */
	public void setResume(String resume) throws ExpositionException {
		if (resume == null) {
			throw new ExpositionException();
		}

		this.resume = resume;
	}

	/**
	 * Retourne une représentation sous forme de chaîne de caractères de l'exposition.
	 *
	 * @return une chaîne de caractères représentant l'exposition.
	 */
	public String toString() {
		return "	Exposition : " + this.intitule + "\n";
	}

	/**
	 * Vérifie si l'exposition est temporaire.
	 *
	 * @return true si l'exposition est temporaire, false sinon.
	 */
	public boolean estTemporaire() {
		return this.estTemporaire;
	}
}