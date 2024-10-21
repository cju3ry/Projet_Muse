package gestion_donnees;

import java.util.ArrayList;

public class DonneesApplication {
	private ArrayList<Conferencier> conferenciers;
	private ArrayList<Employe> employes;
	private ArrayList<Exposition> expositions;
	private ArrayList<Visite> visites;
	
	public DonneesApplication() {
		this.conferenciers = new ArrayList<Conferencier>();
		this.employes = new ArrayList<Employe>();
		this.expositions = new ArrayList<Exposition>();
		this.visites = new ArrayList<Visite>();
	}
	
	public ArrayList<Employe> getEmployes() {
		return employes;
	}
	
	public ArrayList<Conferencier> getConferenciers() {
		return conferenciers;
	}
	
	public ArrayList<Exposition> getExpositions() {
		return expositions;
	}
	
	public ArrayList<Visite> getVisites() {
		return visites;
	}
	
	public static void importerConferenciers() {
		
	}
	
	public static void importerEmployes() {
		
	}
	
	public static void importerExpositions() {
		
	}
	
	public static void importerVisites() {
		
	}
	
	public void ajoutEmploye(Employe employe) {
		this.employes.add(employe);
	}
	
	public void ajoutConferencier(Conferencier conferencier) {
		this.conferenciers.add(conferencier);
	}
	
	public void ajoutExposition(Exposition exposition) {
		this.expositions.add(exposition);
	}
	
	public void ajoutVisite(Visite visite) {
		this.visites.add(visite);
	}
	
	public boolean idExistantEmployes(String id) {
		for (Employe employe : employes) {
			if (employe.getId() == id) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean idExistantConferenciers(String id) {
		for (Conferencier conferencier : conferenciers) {
			if (conferencier.getId() == id) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean idExistantExpositions(String id) {
		for (Exposition exposition : expositions) {
			if (exposition.getId() == id) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean idExistantVisites(String id) {
		for (Visite visite : visites) {
			if (visite.getId() == id) {
				return true;
			}
		}
		
		return false;
	}
}
