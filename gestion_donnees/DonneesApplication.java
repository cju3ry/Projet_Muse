
package gestion_donnees;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class DonneesApplication {
	private static ArrayList<Conferencier> conferenciers;
	private static ArrayList<Employe> employes;
	private static ArrayList<Exposition> expositions;
	private static  ArrayList<Visite> visites;
	
	public DonneesApplication() {
		this.conferenciers = new ArrayList<Conferencier>();
		this.employes = new ArrayList<Employe>();
		this.expositions = new ArrayList<Exposition>();
		this.visites = new ArrayList<Visite>();
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
	
	public static void importerEmployes(ArrayList<String> arraysEmployes) throws EmployeException {
		//initialisesDonnees();
	    for (int i = 1; i < arraysEmployes.size(); i++) {
	        String[] tempEmployes = arraysEmployes.get(i).split(";");

	        // Vérification que la ligne contient bien 4 éléments
	        if (tempEmployes.length == 4) {
	        	
	            String id = tempEmployes[0].trim();
	            String nom = tempEmployes[1].trim();
	            String prenom = tempEmployes[2].trim();
	            String numTel = tempEmployes[3].trim();
	            
	            if (idExistantEmployes(id) 
    				|| homonymeEmployes(nom, prenom)) {
    				throw new EmployeException();
    			}
    			
    			Employe employe = new Employe(id, nom, prenom, numTel);

    			ajoutEmploye(employe);

	            System.out.println("Employé ajouté : " + employe);
	        } else {
				//TODO mettre une exception avec un message explicite.
	            System.out.println("Erreur dans la ligne " + (i + 1) + ": Format incorrect");
	        }
	    }
	}
	
	public void importerConferenciers(ArrayList<String> arraysConferenciers) throws ConferencierException {
		//initialisesDonnees();
		boolean estEmployes = false;
	    for (int i = 1; i < arraysConferenciers.size(); i++) {
	        String[] tempConferenciers = arraysConferenciers.get(i).split(";");

			String id = tempConferenciers[0].trim();
			String nom = tempConferenciers[1].trim();
			String prenom = tempConferenciers[2].trim();
			String[] specialite = {tempConferenciers[3].trim()};
			String numTel = tempConferenciers[4].trim();
			if (tempConferenciers[5].trim().equals("oui")) {
				estEmployes = true;
			} else if (tempConferenciers[5].trim().equals("non")) {
				estEmployes = false;
			} else {
				throw new IllegalArgumentException("Erreur dans le fichier .csv : la colonne estEmploye doit contenir 'oui' ou 'non'");
			}
			ArrayList<String> indisponibilite = new ArrayList<String>();
			//indisponibilite.add(new Date("27/07/25"));
			for (int j = 6; j < tempConferenciers.length; j++) {
				if (tempConferenciers[j].isBlank() || tempConferenciers[j].isEmpty()) {
					//Empty body car si pas de date, on ne fait rien
				} else {
					//System.out.println(nom + " " + tempConferenciers[j].trim());
					indisponibilite.add(tempConferenciers[j].trim());
				}
			}
			
			if (idExistantConferenciers(id) 
				|| homonymeConferenciers(nom, prenom)) {
				throw new ConferencierException();
			}
			
			Conferencier conferencier = new Conferencier(id, nom, prenom);
			conferencier.setNumTel(numTel);
			conferencier.setEstEmploye(estEmployes);
			conferencier.setIndisponibilite(indisponibilite);
			conferencier.setSpecialitees(specialite);

			ajoutConferencier(conferencier);
				
			System.out.println("Conferencier ajouté : " + conferencier);
	    }
	}

	public static void importerExpositions(ArrayList<String> arraysExpositions) throws ExpositionException {
		//initialisesDonnees();
		boolean estTemporaire = false;
	    for (int i = 1; i < arraysExpositions.size(); i++) {
	        String[] tempExpositions = arraysExpositions.get(i).split(";");

	        // Vérification que la ligne contient bien 9 éléments
	        if (tempExpositions.length == 9) {
                String id = tempExpositions[0].trim();
                String intitule = tempExpositions[1].trim();
                String periodeDebut = tempExpositions[2].trim();
                String periodeFin = tempExpositions[3].trim();
                int nbOeuvre = Integer.parseInt(tempExpositions[4].trim());
                String[] motCles = tempExpositions[5].split(","); // Résoudre le # en debut de ligne faire un substring probablement
                String resume = tempExpositions[6];
                String debutExpo = "";
                String finExpo = "";
                if (tempExpositions[7].isEmpty() || tempExpositions[7].isBlank()
                        && tempExpositions[8].isEmpty() || tempExpositions[8].isBlank()) {
                    //tempExpositions[7] = null;
                    //tempExpositions[8] = null;
                    estTemporaire = false;
                } else if (!tempExpositions[7].isEmpty() || !tempExpositions[7].isBlank()
                        && !tempExpositions[8].isEmpty() || !tempExpositions[8].isBlank()) {
                    debutExpo = tempExpositions[7];
                    finExpo = tempExpositions[8];
                    estTemporaire = true;
                } else {
                    throw new ExpositionException();
                }
                
                if (idExistantExpositions(id)) {
    				throw new ExpositionException();
    			}

                //System.out.print(tempExpositions[7] + "   " + tempExpositions[8]);
                //System.out.print(tempExpositions[8] + "/");
                if (estTemporaire) {
                    Exposition expoTemp = new Exposition(id, intitule, debutExpo, finExpo);
                    expoTemp.setPeriode(periodeDebut, periodeFin);
                    expoTemp.setNbOeuvre(nbOeuvre);
                    expoTemp.setMotCles(motCles);
                    expoTemp.setResume(resume);
					System.out.println("Expositions ajouté : " + expoTemp);
                } else {
					Exposition expo = new Exposition(id, intitule);
                    expo.setPeriode(periodeDebut, periodeFin);
                    expo.setNbOeuvre(nbOeuvre);
                    expo.setMotCles(motCles);
                    expo.setResume(resume);
					System.out.println("Expositions ajouté : " + expo);
                }
                //Exposition expo = new Exposition(id, intitule,periodeDebut,periodeFin, nbOeuvre, motCles,resume , debutExpo, finExpo);


            } else {
				//TODO mettre une exception avec un message explicite.
	            System.out.println("Erreur dans la ligne " + (i + 1) + ": Format incorrect");
	        }
	    }
	}
	
	public static void importerVisites(ArrayList<String> arraysVisites) throws VisiteException, EmployeException {
		//initialisesDonnees();
		boolean	estVisite = false;
		for (int i = 1; i < arraysVisites.size(); i++) {
			String[] tempVisites = arraysVisites.get(i).split(";");

			// Vérification que la ligne contient bien 10 éléments
			if (tempVisites.length == 10) {
				String idVisites = tempVisites[0].trim();
				String idExposition = tempVisites[1].trim();
				String idConferencier = tempVisites[2].trim();
				String idEmploye = tempVisites[3].trim();
				String dateVisite = tempVisites[4].trim();
				String heureVisite = tempVisites[5].trim();
				String intitule = tempVisites[6].trim();
				String numTel = tempVisites[7].trim();
				
				if (!idExistantExpositions(idExposition) 
					&& !idExistantEmployes(idEmploye) 
					&& !idExistantConferenciers(idConferencier)) {
					throw new VisiteException();
    			}
				
				if (idExistantVisites(idVisites)) {
    				throw new VisiteException();
    			}
				
				Visite visite = new Visite(idVisites, dateVisite, heureVisite, intitule, numTel);
				visite.setEmployeId(idEmploye);
				visite.setConferencierId(idConferencier);
				visite.setExpositionId(idExposition);
				System.out.println("Visite ajouté : " + visite);
			} else {

				//TODO mettre une exception avec un message explicite.

				System.out.println("Erreur dans la ligne " + (i + 1) + ": Format incorrect");
			}
		}
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
	
	public static void ajoutEmploye(Employe employe) {
		employes.add(employe);
	}
	
	public static void ajoutConferencier(Conferencier conferencier) {
		conferenciers.add(conferencier);
	}
	
	public static void ajoutExposition(Exposition exposition) {
		expositions.add(exposition);
	}
	
	public static void ajoutVisite(Visite visite) {
		visites.add(visite);
	}
	
	public static boolean idExistantEmployes(String id) {
		for (Employe employe : employes) {
			if (employe.getId().equals(id)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean idExistantConferenciers(String id) {
		for (Conferencier conferencier : conferenciers) {
			if (conferencier.getId().equals(id)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean homonymeEmployes(String nom, String prenom) {
		for (Employe employe : employes) {
			if (employe.getPrenom().equals(prenom)) {
				if (employe.getNom().equals(nom)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean homonymeConferenciers(String nom, String prenom) {
		for (Conferencier conferencier : conferenciers) {
			if (conferencier.getPrenom().equals(prenom)) {
				if (conferencier.getNom().equals(nom)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean idExistantExpositions(String id) {
		for (Exposition exposition : expositions) {
			if (exposition.getId().equals(id)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean idExistantVisites(String id) {
		for (Visite visite : visites) {
			if (visite.getId().equals(id)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static void main(String[] args) throws EmployeException, ExpositionException, ConferencierException, VisiteException, ParseException {
		/*
		 * 1 - initialiserDonnes();
		 * 2 - importerEmployes(LireCsv("employes.csv"));
		 * 3 - importerExpositions(LireCsv("expositions.csv"));
		 * 4 - importerConferenciers(LireCsv("conferenciers.csv"));
		 * 5 - importerVisites(LireCsv("visites.csv"));
		 */

		DonneesApplication donnees = new DonneesApplication();
		
		donnees.importerEmployes(LireCsv("employes.csv"));
		donnees.importerExpositions(LireCsv("expositions.csv"));
		donnees.importerConferenciers(LireCsv("conferenciers.csv"));
		donnees.importerVisites(LireCsv("visites.csv"));
	}
}
