package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	public Exposition(String id, String intitule, String periodeDebut, String periodeFin, int nbOeuvre, String[] motCles, String resume, String debutExpo, String finExpo) throws ExpositionException {
		
		SimpleDateFormat periode = new SimpleDateFormat("yyyy");
		SimpleDateFormat tempsExpo = new SimpleDateFormat("dd/MM/yyyy");
		
		periode.setLenient(false);
		tempsExpo.setLenient(false);
		
		if (id.length() != 7 || intitule == null || resume == null
			|| nbOeuvre <= 0 || periodeDebut == null && periodeFin == null
			|| motCles.length > 10) {
			throw new ExpositionException();
		}
		
		for (Exposition exposition : gestionDonnees.expositions) {
			if (exposition.getId() == id) {
				throw new ExpositionException();
			}
		}
		
		if (Integer.parseInt(periodeDebut) > 2024 || Integer.parseInt(periodeFin) > 2024) {
			throw new ExpositionException();
		}
		
		if (debutExpo != null && finExpo != null) {
			this.estTemporaire = true;
		} else if (debutExpo == null && finExpo == null) {
			this.estTemporaire = false;
		} else {
			throw new ExpositionException();
		}
		
		// vérification de l'année seulement
		if (this.estTemporaire && debutExpo.substring(6,debutExpo.length()).length() > 4
			|| this.estTemporaire && finExpo.substring(6,finExpo.length()).length() > 4) {
			throw new ExpositionException();
		}
		
		try {
			if (periode.parse(periodeDebut).getTime() > periode.parse(periodeFin).getTime()
				|| this.estTemporaire && tempsExpo.parse(debutExpo).getTime() > tempsExpo.parse(finExpo).getTime()) {
				throw new ExpositionException();
			}
			
			if (periodeDebut != null && periodeFin != null) {
				this.periodeDebut = periode.parse(periodeDebut);
				this.periodeFin = periode.parse(periodeFin);
			} else { 
				throw new ExpositionException();
			}
			
			if (this.estTemporaire) {
				this.debutExpo = tempsExpo.parse(debutExpo);
				this.finExpo = tempsExpo.parse(finExpo);
			}
			
		} catch (ParseException e) {
			throw new ExpositionException();
		}
		
		this.id = id;
		this.intitule = intitule;
		this.resume = resume;
		this.nbOeuvre = nbOeuvre;
		this.motCles = motCles;
		gestionDonnees.expositions.add(this);
	}
	
	public String getId() {
		return this.id;
	}
	
	public String toString() {
		return "	Exposition : " + this.intitule + "\n";
	}
	
	public static void main (String args[]) throws EmployeException, ExpositionException, ConferencierException, VisiteException {
		gestionDonnees.initialisesDonnees();
		String[] tab = new String[10];
		tab[0] = "dbzayudza";
		tab[1] = "salut";
		Exposition expo1 = new Exposition("E000001", "coucou", "1984", "1988", 12, tab, "dzayudfgauyfaf", null, null);
		
		Exposition expo2 = new Exposition("E000002", "salut", "1984", "1988", 6, tab, "dzayudfgauyfaf", "12/12/2012", "12/12/2012");
		
		for (Exposition exposition : gestionDonnees.expositions) {
			System.out.print(exposition.toString());
		}
	}
}
