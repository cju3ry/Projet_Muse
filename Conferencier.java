package gestion_donnees;

import java.util.ArrayList;
import java.util.Date;

public class Conferencier {
	private String id;
	private String nom;
	private String prenom;
	private boolean estEmploye;
	private ArrayList<Date> indisponibilite;
	private String[] specialite;
	private String numTel;
	
	public Conferencier(String id, String nom, String prenom, boolean estEmploye, ArrayList<Date> indisponibilite, String[] specialite, String numTel) throws ConferencierException {
		if (id.length() != 7) {
			throw new ConferencierException();
		}
		
		for (Conferencier conferencier : gestionDonnees.conferenciers) {
			if (conferencier.getId() == id) {
				throw new ConferencierException();
			}
			
			if (conferencier.getPrenom() == prenom) {
				if (conferencier.getNom() == nom) {
					throw new ConferencierException();
				}
			}
		}
		
		if (numTel.length() != 10) {
			throw new ConferencierException();
		}
		
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.estEmploye = estEmploye;
		this.indisponibilite = indisponibilite;
		this.specialite = specialite;
		gestionDonnees.conferenciers.add(this);
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getNom() {
		return this.nom;
	}
	
	public String getPrenom() {
		return this.prenom;
	}
	
	public String toString() {
		return "	Conférencier/Conférencière : " + this.nom + " " + this.prenom;
	}
}
