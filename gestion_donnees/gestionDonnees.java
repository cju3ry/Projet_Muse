package gestion_donnees;

import java.util.ArrayList;

public class gestionDonnees {
	public static ArrayList<Conferencier> conferenciers;
	public static ArrayList<Employe> employes;
	public static ArrayList<Exposition> expositions;
	public static ArrayList<Visite> visites;
	
	public static void initialisesDonnees() throws EmployeException, ExpositionException, ConferencierException, VisiteException {
		conferenciers = new ArrayList<Conferencier>();
		employes = new ArrayList<Employe>();
		expositions = new ArrayList<Exposition>();
		visites = new ArrayList<Visite>();
	}
	
	public static ArrayList<Employe> getEmployes() {
		return employes;
	}
	
	public static ArrayList<Conferencier> getConferenciers() {
		return conferenciers;
	}
	
	public static ArrayList<Exposition> getExpositions() {
		return expositions;
	}
	
	public static ArrayList<Visite> getVisites() {
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
}
