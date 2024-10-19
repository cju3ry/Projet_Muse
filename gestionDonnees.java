package gestion_donnees;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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
	
	public static ArrayList<String> LireCsv(String cheminFichier) {
	    File fichier = new File(cheminFichier);
	    String ligne;
	    ArrayList<String> contenuCsv = new ArrayList<String>();
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fichier), StandardCharsets.UTF_8))) {
	       while ((ligne = br.readLine()) != null) {
	          contenuCsv.add(ligne + "\n");
	       }
	    } catch (IOException e) {
	       e.printStackTrace();
	    }
	    return contenuCsv;
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
	
	public static void importerEmployes(ArrayList<String> arraysEmployes) throws EmployeException, ExpositionException, ConferencierException, VisiteException {
		initialisesDonnees();
	    for (int i = 1; i < arraysEmployes.size(); i++) {
	        String[] tempEmployes = arraysEmployes.get(i).split(";");

	        // Vérification que la ligne contient bien 4 éléments
	        if (tempEmployes.length == 4) {
	        	
	            String id = tempEmployes[0].trim();
	            String nom = tempEmployes[1].trim();
	            String prenom = tempEmployes[2].trim();
	            String numTel = tempEmployes[3].trim();

	            Employe employe = new Employe(id, nom, prenom, numTel);

	            System.out.println("Employé ajouté : " + employe);
	        } else {
	            System.out.println("Erreur dans la ligne " + (i + 1) + ": Format incorrect");
	        }
	    }
	}
	
	public static void importerConferenciers(ArrayList<String> arraysConferenciers) throws EmployeException, ExpositionException, ConferencierException, VisiteException {
		initialisesDonnees();
		boolean estEmployes = false;
	    for (int i = 1; i < arraysConferenciers.size(); i++) {
	        String[] tempConferenciers = arraysConferenciers.get(i).split(",");

	        // Vérification que la ligne contient bien 7 éléments
	        if (tempConferenciers.length == 6) {
	        	
	            String id = tempConferenciers[0].trim();
	            String nom = tempConferenciers[1].trim();
	            String prenom = tempConferenciers[2].trim();
	            String specialite[] = {tempConferenciers[3].trim()};
	            String numTel = tempConferenciers[4].trim();
	            if (tempConferenciers[5].trim() == "oui") { estEmployes = true;}
	            ArrayList<Date> indisponibilite = null;            
               Conferencier conferencier = new Conferencier( id,  nom,  prenom, estEmployes,indisponibilite,specialite, numTel);
	            System.out.println("Conferencier ajouté : " + conferencier);
	        } else {
	            System.out.println("Erreur dans la ligne " + (i + 1) + ": Format incorrect");
	        }
	    }
	}
	

	
	public static void importerExpositions(ArrayList<String> arraysExpositions) throws EmployeException, ExpositionException, ConferencierException, VisiteException {
		initialisesDonnees();
		boolean estEmployes = false;
	    for (int i = 1; i < arraysExpositions.size(); i++) {
	        String[] tempExpositions = arraysExpositions.get(i).split(";");

	        // Vérification que la ligne contient bien 9 éléments
	        if (tempExpositions.length == 9) {
	        	String id = tempExpositions[0].trim();
	        	String intitule = tempExpositions[1].trim();
	        	String periodeDebut = tempExpositions[2].trim();
	        	String periodeFin = tempExpositions[3].trim();
	        	int nbOeuvre = Integer.parseInt(tempExpositions[4].trim());
	        	String[] motCles = tempExpositions[5].split(","); // resoudre le # en debut de ligne faire un substring probablement
	        	String resume = tempExpositions[6];
	        	String debutExpo = tempExpositions[7];
	        	String finExpo = tempExpositions[8];
	        	System.out.print(tempExpositions[8] + "/");
	        	boolean estTemporaire = false;
	        	Exposition expo = new Exposition(id, intitule, resume, nbOeuvre, estTemporaire, motCles,periodeDebut,  periodeFin, debutExpo, finExpo);

	        	
	            System.out.println("Expositions ajouté : " + expo);
	        } else {
	            System.out.println("Erreur dans la ligne " + (i + 1) + ": Format incorrect");
	        }
	    }
	}
	
	public static void importerVisites() {
		
	}
	
	public static void main(String[] args) throws EmployeException, ExpositionException, ConferencierException, VisiteException, ParseException {
//		importerEmployes(LireCsv("employes.csv"));
//		System.out.print(employes.toString());
		importerExpositions(LireCsv("expositions.csv"));
	}
	
	
}
