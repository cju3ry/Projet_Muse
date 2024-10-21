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
	
	public Exposition(String id, String intitule) throws ExpositionException {  
		
		if (id.length() != 7 || intitule == null) {
			throw new ExpositionException();
		}
		
		this.id = id;
		this.intitule = intitule;
	}
	
	public Exposition(String id, String intitule, String debutExpo, String finExpo) throws ExpositionException {  
		SimpleDateFormat tempsExpo = new SimpleDateFormat("dd/MM/yyyy");
		tempsExpo.setLenient(false);
		
		if (id.length() != 7 || intitule == null) {
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
			if (this.estTemporaire && tempsExpo.parse(debutExpo).getTime() > tempsExpo.parse(finExpo).getTime()) {
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
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setPeriode (String periodeDebut, String periodeFin) throws ExpositionException {
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
	
	public void setNbOeuvre(int nbOeuvre) throws ExpositionException {
		if (nbOeuvre <= 0) {
			throw new ExpositionException();
		}
		
		this.nbOeuvre = nbOeuvre;
	}
	
	public void setMotCles(String[] motCles) throws ExpositionException {
		if (motCles.length > 10 || motCles.length <= 0) {
			throw new ExpositionException();
		}
		
		this.motCles = motCles;
	}
	
	public void setResume(String resume) throws ExpositionException {
		if (resume == null) {
			throw new ExpositionException();
		}
		
		this.resume = resume;
	}
	
	public String toString() {
		return "	Exposition : " + this.intitule + "\n";
	}
	
	public boolean estTemporaire() {
		if (this.estTemporaire) {
			return true;
		} else {
			return false;
		}
	}
}
