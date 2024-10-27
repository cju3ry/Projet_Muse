package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * La classe Exposition représente une exposition avec ses informations détaillées.
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
	 * @throws IllegalArgumentException si l'identifiant n'a pas une longueur de 7 caractères ou si l'intitulé est null.
	 */
	public Exposition(String id, String intitule) {
		if (intitule == null || id == null || id.length() != 7 ) {
			throw new IllegalArgumentException("L'identifiant doit être de 7 caractères et l'intitulé ne doit pas être null.");
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
	 * @throws IllegalArgumentException si l'identifiant n'a pas une longueur de 7 caractères, si l'intitulé est null, ou si les dates sont invalides.
	 */
	public Exposition(String id, String intitule, String debutExpo, String finExpo) {
		SimpleDateFormat tempsExpo = new SimpleDateFormat("dd/MM/yyyy");
		tempsExpo.setLenient(false);

		if (intitule == null || id == null || id.length() != 7 ) {
			throw new IllegalArgumentException("L'identifiant doit être de 7 caractères et l'intitulé ne doit pas être null.");
		}

		try {
			Date dateDebut = tempsExpo.parse(debutExpo);
			Date dateFin = tempsExpo.parse(finExpo);
			if (dateDebut.after(dateFin)) {
				throw new IllegalArgumentException("La date de début doit être antérieure à la date de fin.");
			}
			this.debutExpo = dateDebut;
			this.finExpo = dateFin;
		} catch (ParseException e) {
			throw new IllegalArgumentException("Les dates doivent être au format 'dd/MM/yyyy'.");
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
	 * @throws IllegalArgumentException si les années sont invalides ou si la période de début est après la période de fin.
	 */
	public void setPeriode(String periodeDebut, String periodeFin) {
		SimpleDateFormat formatAnnee = new SimpleDateFormat("yyyy");
		formatAnnee.setLenient(false);

		try {
			Date dateDebut = formatAnnee.parse(periodeDebut);
			Date dateFin = formatAnnee.parse(periodeFin);
			if (dateDebut.after(dateFin)) {
				throw new IllegalArgumentException("L'année de début doit être antérieure ou égale à l'année de fin.");
			}
			this.periodeDebut = dateDebut;
			this.periodeFin = dateFin;
		} catch (ParseException e) {
			throw new IllegalArgumentException("Les années de la période doivent être au format 'yyyy'.");
		}
	}

	/**
	 * Définit le nombre d'œuvres de l'exposition.
	 *
	 * @param nbOeuvre le nombre d'œuvres (doit être supérieur à 0).
	 * @throws IllegalArgumentException si le nombre d'œuvres est inférieur ou égal à 0.
	 */
	public void setNbOeuvre(int nbOeuvre) {
		if (nbOeuvre <= 0) {
			throw new IllegalArgumentException("Le nombre d'œuvres doit être supérieur à 0.");
		}
		this.nbOeuvre = nbOeuvre;
	}

	/**
	 * Définit les mots-clés de l'exposition.
	 *
	 * @param motCles un tableau de mots-clés (doit contenir entre 1 et 10 éléments).
	 * @throws IllegalArgumentException si le tableau de mots-clés est vide ou contient plus de 10 éléments.
	 */
	public void setMotCles(String[] motCles) {
		if (motCles == null || motCles.length == 0 || motCles.length > 10) {
			throw new IllegalArgumentException("Le tableau de mots-clés doit contenir entre 1 et 10 éléments.");
		}
		this.motCles = motCles;
	}

	/**
	 * Définit le résumé de l'exposition.
	 *
	 * @param resume le résumé de l'exposition.
	 * @throws IllegalArgumentException si le résumé est null.
	 */
	public void setResume(String resume) {
		if (resume == null) {
			throw new IllegalArgumentException("Le résumé ne doit pas être null.");
		}
		this.resume = resume;
	}

	@Override
	public String toString() {
		SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
	    SimpleDateFormat formatAnnee = new SimpleDateFormat("yyyy");
		return "\tExposition : " + this.intitule + "\n" +
		           "\tRésumé : " + this.resume + "\n" +
		           "\tNombre d'œuvres : " + this.nbOeuvre + "\n" +
		           "\tTemporaire : " + (this.estTemporaire ? "Oui" : "Non") + "\n" +
		           "\tMots-clés : " + String.join(", ", this.motCles) + "\n" +
		           "\tPériode : " + formatAnnee.format(this.periodeDebut) + " - " + formatAnnee.format(this.periodeFin)+ "\n" +
		           "\tDébut de l'exposition : " + (this.debutExpo != null ? formatDate.format(this.debutExpo) : "Non spécifiée") + "\n" +
		           "\tFin de l'exposition : " + (this.finExpo != null ? formatDate.format(this.finExpo) : "Non spécifiée") + "\n";
	}

	// Méthode pour vérifier si une exposition est temporaire
	public boolean estTemporaire() {
		return this.estTemporaire;
	}

	public Date getPeriodeDebut() {
		return this.periodeDebut;
	}

	public Date getPeriodeFin() {
		return this.periodeFin;
	}
}
