package gestion_donnees;

import java.util.Date;

public class Visite {
	private String id;
	private String intitule;
	private Date dateVisite;
	private Date heureVisite;
	private String telephone;
	private Exposition exposition;
	private Employe employe;
	private Conferencier conferencier;
	
	public Visite(String idVisite, String intitule, Date dateVisite, Date heureVisite, String telephone, Exposition exposition, Employe employe, Conferencier conferencier) throws VisiteException {
		
		if (id.length() != 7 || idVisite == null) {
			throw new VisiteException();
		}
		
		
		this.id = id;
		this.intitule = intitule;
		this.dateVisite = dateVisite;
		this.heureVisite = heureVisite;
		this.telephone = telephone;
		this.exposition = exposition;
		this.employe = employe;
		this.conferencier = conferencier;
		gestionDonnees.visites.add(this);
	}
	
	public String getId() {
		return this.id;
	}
	
	public String toString() {
		return "	Visite : " + this.intitule;
	}
}
