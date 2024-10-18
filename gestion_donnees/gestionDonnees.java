package gestion_donnees;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

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

	public static String LireCSVConferencier() {
		File fichier = new File("conferenciers.csv");
        String ligne;
		ArrayList<String> conferencier = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fichier), StandardCharsets.UTF_8))) {
			while ((ligne = br.readLine()) != null) {
				conferencier.add(ligne + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Arrays.toString(conferencier.toArray());
    }

	public static String LireCSVEmploye() {
		File fichier = new File("employes.csv");
		String ligne;
		ArrayList<String> employes = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fichier), StandardCharsets.UTF_8))) {
			while ((ligne = br.readLine()) != null) {
				employes.add(ligne + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Arrays.toString(employes.toArray());
	}

	public static String LireCSVexpositions() {
		File fichier = new File("expositions.csv");
		String ligne;
		ArrayList<String> expositions = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fichier), StandardCharsets.UTF_8))) {
			while ((ligne = br.readLine()) != null) {
				expositions.add(ligne + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Arrays.toString(expositions.toArray());
	}

	public static String LireCSVvisites() {
		File fichier = new File("visites.csv");
		String ligne;
		ArrayList<String> visites = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fichier), StandardCharsets.UTF_8))) {
			while ((ligne = br.readLine()) != null) {
				visites.add(ligne + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Arrays.toString(visites.toArray());
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

	public static void main(String[] args) {
		System.out.println(LireCSVEmploye());
	}
}
