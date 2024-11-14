package gestion_donnees;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * La classe Visite représente une visite avec ses informations détaillées.
 * Elle permet de gérer les visites, y compris l'identifiant, l'intitulé, la date, l'heure, le téléphone, et les identifiants des expositions, employés et conférenciers associés.
 */
public class Visite implements Serializable {
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
	 * @throws IllegalArgumentException si l'identifiant, le numéro de téléphone, l'intitulé, la date ou l'heure sont invalides.
	 */
	public Visite(String idVisite, String dateVisite, String heureVisite, String intitule, String numTel) {
		SimpleDateFormat jourVisite = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat heureDebut = new SimpleDateFormat("HH'h'mm");
		jourVisite.setLenient(false);
		heureDebut.setLenient(false);

		if (idVisite == null || idVisite.length() != 7) {
			throw new IllegalArgumentException("L'identifiant de la visite doit être de 7 caractères.");
		}
		if (numTel == null || numTel.length() != 10) {
			throw new IllegalArgumentException("Le numéro de téléphone doit être de 10 caractères.");
		}
		if (intitule == null) {
			throw new IllegalArgumentException("L'intitulé de la visite ne peut pas être nul.");
		}
		if (dateVisite == null || heureVisite == null) {
			throw new IllegalArgumentException("La date et l'heure de la visite doivent être spécifiées.");
		}
		try {
			this.dateVisite = jourVisite.parse(dateVisite);
			this.heureVisite = heureDebut.parse(heureVisite);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Le format de la date doit être 'dd/MM/yyyy' et celui de l'heure 'HH'h'mm'.");
		}

		this.id = idVisite;
		this.intitule = intitule;
		this.telephone = numTel;
	}

	// Retourne l'identifiant de la visite
	public String getId() {
		return this.id;
	}

	// Retourne l'intitule de la visite
	public String getIntitule() {
		return this.intitule;
	}

	// Retourne le numéro de téléphone de la visite
	public String getNumTel() {
		return this.telephone;
	}

	// Retourne la date de la visite
	public Date getDateVisite() {
		return this.dateVisite;
	}

	// Retourne l'heure de la visite
	public Date getHeureVisite() {
		return this.heureVisite;
	}

	/**
	 * Retourne une représentation sous forme de chaîne de caractères de la Visite.
	 * La chaîne contient :
	 * - L'intitulé de la visite
	 * - La date de la visite formatée
	 * - L'heure de la visite formatée
	 * - Le numéro de téléphone associé	 
	 *
	 * @return une chaîne de caractères représentant les détails de la visite
	 */
	@Override
	public String toString() {
		SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatHeure = new SimpleDateFormat("HH'h'mm");
		return "\tVisite : " + this.intitule + "\n" +
				"\tDate de la visite : " + formatDate.format(this.dateVisite) + "\n" +
				"\tHeure de la visite : " + formatHeure.format(this.heureVisite) + "\n" +
				"\tTéléphone : " + this.telephone + "\n";
	}


	// Définit l'identifiant de l'exposition associée à la visite
	public void setExpositionId(String expositionId) {
		if (expositionId == null || expositionId.length() != 7) {
			throw new IllegalArgumentException("L'identifiant de l'exposition doit être de 7 caractères.");
		}
		this.expositionId = expositionId;
	}

	// Définit l'identifiant du conférencier associé à la visite
	public void setConferencierId(String conferencierId) {
		if (conferencierId == null || conferencierId.length() != 7) {
			throw new IllegalArgumentException("L'identifiant du conférencier doit être de 7 caractères.");
		}
		this.conferencierId = conferencierId;
	}

	// Définit l'identifiant de l'employé associé à la visite
	public void setEmployeId(String employeId) {
		if (employeId == null || employeId.length() != 7) {
			throw new IllegalArgumentException("L'identifiant de l'employé doit être de 7 caractères.");
		}
		this.employeId = employeId;
	}

	// Retourne l'identifiant de l'exposition lié
	public String getExpositionId() {
		return this.expositionId;
	}

	// Retourne l'identifiant du conférencier lié
	public String getConferencierId() {
		return this.conferencierId;
	}

	// Retourne l'identifiant de l'employé lié
	public String getEmployeId() {
		return this.employeId;
	}
}
