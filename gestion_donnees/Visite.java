package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * La classe Visite représente une visite avec ses informations détaillées.
 * Elle permet de gérer les visites, y compris l'identifiant, l'intitulé, la date, l'heure, le téléphone, et les identifiants des expositions, employés et conférenciers associés.
 */
public class Visite {
	private String id;
	private String intitule;
	private Date dateVisite;
	private Date heureVisite;
	private String telephone;
	private String expositionId;
	private String employeId;
	private String conferencierId;

	/**
	 * Constructeur de la classe Visite.
	 *
	 * @param idVisite l'identifiant unique de la visite (doit être de 7 caractères).
	 * @param dateVisite la date de la visite (format "dd/MM/yyyy").
	 * @param heureVisite l'heure de la visite (format "HH'h'mm").
	 * @param intitule l'intitulé de la visite.
	 * @param numTel le numéro de téléphone associé à la visite (doit être de 10 caractères).
	 * @throws VisiteException si l'identifiant, le numéro de téléphone, l'intitulé, la date ou l'heure sont invalides.
	 */
	public Visite(String idVisite, String dateVisite, String heureVisite, String intitule, String numTel) throws VisiteException {
		SimpleDateFormat jourVisite = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat heureDebut = new SimpleDateFormat("HH'h'mm");
		heureDebut.setLenient(false);
		jourVisite.setLenient(false);

		if (idVisite == null || idVisite.length() != 7) {
			throw new VisiteException();
		}

		if (numTel == null || numTel.length() != 10) {
			throw new VisiteException();
		}

		if (intitule == null) {
			throw new VisiteException();
		}

		if (dateVisite != null && heureVisite != null) {
			try {
				this.dateVisite = jourVisite.parse(dateVisite);
				this.heureVisite = heureDebut.parse(heureVisite);
			} catch (ParseException e) {
				throw new VisiteException();
			}
		} else {
			throw new VisiteException();
		}

		this.id = idVisite;
		this.intitule = intitule;
		this.telephone = numTel;
	}

	/**
	 * Retourne l'identifiant de la visite.
	 *
	 * @return l'identifiant de la visite.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Retourne une représentation sous forme de chaîne de caractères de la visite.
	 *
	 * @return une chaîne de caractères représentant la visite.
	 */
	@Override
	public String toString() {
		return "	Visite : " + this.intitule + "\n";
	}

	/**
	 * Définit l'identifiant de l'exposition associée à la visite.
	 *
	 * @param expositionId l'identifiant de l'exposition (doit être de 7 caractères).
	 * @throws VisiteException si l'identifiant est invalide.
	 */
	public void setExpositionId(String expositionId) throws VisiteException {
		if (expositionId == null || expositionId.length() != 7) {
			throw new VisiteException();
		}
		this.expositionId = expositionId;
	}

	/**
	 * Définit l'identifiant du conférencier associé à la visite.
	 *
	 * @param conferencierId l'identifiant du conférencier (doit être de 7 caractères).
	 * @throws VisiteException si l'identifiant est invalide.
	 */
	public void setConferencierId(String conferencierId) throws VisiteException {
		if (conferencierId == null || conferencierId.length() != 7) {
			throw new VisiteException();
		}
		this.conferencierId = conferencierId;
	}

	/**
	 * Définit l'identifiant de l'employé associé à la visite.
	 *
	 * @param employeId l'identifiant de l'employé (doit être de 7 caractères).
	 * @throws VisiteException si l'identifiant est invalide.
	 */
	public void setEmployeId(String employeId) throws VisiteException {
		if (employeId == null || employeId.length() != 7) {
			throw new VisiteException();
		} else {
			this.employeId = employeId;
		}
	}
}