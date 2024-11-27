
package gestion_donnees;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;


/**
 * La classe DonneesApplication gère les données de l'application, y compris les conférenciers, employés, expositions et visites.
 * @author ADRIAN CAZOR--BONNET
 *         QUENTIN CHESNIER
 *         CLÉMENT JUERY
 *         BAPTISTE LADUREAU
 */
public class DonneesApplication {
	private static ArrayList<Conferencier> conferenciers;
	private static ArrayList<Employe> employes;
	private static ArrayList<Exposition> expositions;
	private static ArrayList<Visite> visites;
	
	
	/**
	 * Constructeur de la classe DonneesApplication.
	 * Initialise les listes de conférenciers, employés, expositions et visites.
	 */
	public DonneesApplication() {
		this.conferenciers = new ArrayList<Conferencier>();
		this.employes = new ArrayList<Employe>();
		this.expositions = new ArrayList<Exposition>();
		this.visites = new ArrayList<Visite>();
	}
	
	/**
	 * Lit un fichier CSV et retourne son contenu sous forme de liste de chaînes.
	 *
	 * @param cheminFichier le chemin du fichier CSV à lire.
	 * @return une liste de chaînes représentant le contenu du fichier CSV.
	 */
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
	
	/**
	 * Importe les employés à partir d'une liste de chaînes.
	 *
	 * @param arraysEmployes la liste de chaînes représentant les employés.
	 * @throws EmployeException si une erreur survient lors de l'importation.
	 */
	public static void importerEmployes(ArrayList<String> arraysEmployes) {
		String[] verifEntete = arraysEmployes.get(0).split(";");
		System.out.println(verifEntete[0]);
		System.out.println(verifEntete[1]);
		System.out.println(verifEntete[2]);
		System.out.println(verifEntete[3]);
		if (verifEntete.length == 4 && verifEntete[0].trim().equalsIgnoreCase("Ident") && verifEntete[1].trim().equalsIgnoreCase("Nom") && verifEntete[2].trim().equalsIgnoreCase("Prenom") && verifEntete[3].trim().equalsIgnoreCase("Telephone")) {
			//Empty body car si la ligne est correcte, on ne fait rien
		} else {
			throw new IllegalArgumentException("Erreur dans la ligne 1: l'entete n'est pas correcte");
		}
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
    				throw new IllegalArgumentException("L'id de l'employe existe déja ou il y a un homonymes");
    			}
    			
    			Employe employe = new Employe(id, nom, prenom, numTel);

    			employes.add(employe);

	            System.out.println("Employé ajouté : " + employe);
	        } else {
	            throw new IllegalArgumentException("Erreur dans la ligne " + (i + 1) + ": Format incorrect");
	        }
	    }
	}
	
	/**
	 * Importe les conférenciers à partir d'une liste de chaînes.
	 *
	 * @param arraysConferenciers la liste de chaînes représentant les conférenciers.
	 * @throws ConferencierException si une erreur survient lors de l'importation.
	 */
	public void importerConferenciers(ArrayList<String> arraysConferenciers) {
		String[] verifEntete = arraysConferenciers.get(0).split(";");
		if (verifEntete.length < 7 || !verifEntete[0].trim().equalsIgnoreCase("Ident")
				|| !verifEntete[1].trim().equalsIgnoreCase("Nom")
				|| !verifEntete[2].trim().equalsIgnoreCase("Prenom")
				|| !verifEntete[3].trim().equalsIgnoreCase("Specialité")
				|| !verifEntete[4].trim().equalsIgnoreCase("Telephone")
				|| !verifEntete[5].trim().equalsIgnoreCase("Employe")
				|| !verifEntete[6].trim().equalsIgnoreCase("Indisponibilite")) {
			throw new IllegalArgumentException("Erreur dans la ligne 1: l'entete n'est pas correcte");
		}

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
			for (int j = 6; j < tempConferenciers.length; j++) {
				if (tempConferenciers[j].isBlank() || tempConferenciers[j].isEmpty()) {
					//Empty body car si pas de date, on ne fait rien
				} else {
					indisponibilite.add(tempConferenciers[j].trim());
				}
			}
			
			if (idExistantConferenciers(id) 
				|| homonymeConferenciers(nom, prenom)) {
				throw new IllegalArgumentException("L'id du conférenciers existe déja ou il y a un homonymes");
			}
			
			Conferencier conferencier = new Conferencier(id, nom, prenom);
			conferencier.setNumTel(numTel);
			conferencier.setEstEmploye(estEmployes);
			conferencier.setIndisponibilite(indisponibilite);
			conferencier.setSpecialitees(specialite);
			
			conferenciers.add(conferencier);
			
			System.out.println("Conferencier ajouté : " + conferencier);
	    }
	}
	
	/**
	 * Importe les expositions à partir d'une liste de chaînes.
	 *
	 * @param arraysExpositions la liste de chaînes représentant les expositions.
	 * @throws ExpositionException si une erreur survient lors de l'importation.
	 */
	public static void importerExpositions(ArrayList<String> arraysExpositions) {
		String[] verifEntete = arraysExpositions.get(0).split(";");
		if (verifEntete.length != 9 || !verifEntete[0].trim().equalsIgnoreCase("Ident")
				|| !verifEntete[1].trim().equalsIgnoreCase("Intitulé")
				|| !verifEntete[2].trim().equalsIgnoreCase("PériodeDeb")
				|| !verifEntete[3].trim().equalsIgnoreCase("PériodeFin")
				|| !verifEntete[4].trim().equalsIgnoreCase("nombre")
				|| !verifEntete[5].trim().equalsIgnoreCase("motclé")
				|| !verifEntete[6].trim().equalsIgnoreCase("résumé")
				|| !verifEntete[7].trim().equalsIgnoreCase("Début")
				|| !verifEntete[8].trim().equalsIgnoreCase("Fin")) {
			throw new IllegalArgumentException("Erreur dans la ligne 1: l'entete n'est pas correcte");
		}
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
                    estTemporaire = false;
                } else if (!tempExpositions[7].isEmpty() || !tempExpositions[7].isBlank()
                        && !tempExpositions[8].isEmpty() || !tempExpositions[8].isBlank()) {
                    debutExpo = tempExpositions[7];
                    finExpo = tempExpositions[8];
                    estTemporaire = true;
                } else {
                    throw new IllegalArgumentException("Erreur de temporalité de l'exposition");
                }
                
                if (idExistantExpositions(id)) {
    				throw new IllegalArgumentException("L'id de l'expositions existe déja");
    			}

                if (estTemporaire) {
                    Exposition expoTemp = new Exposition(id, intitule, debutExpo, finExpo);
                    expoTemp.setPeriode(periodeDebut, periodeFin);
                    expoTemp.setNbOeuvre(nbOeuvre);
                    expoTemp.setMotCles(motCles);
                    expoTemp.setResume(resume);
					System.out.println("Expositions ajouté : " + expoTemp);
					expositions.add(expoTemp);
                } else {
					Exposition expo = new Exposition(id, intitule);
                    expo.setPeriode(periodeDebut, periodeFin);
                    expo.setNbOeuvre(nbOeuvre);
                    expo.setMotCles(motCles);
                    expo.setResume(resume);
					System.out.println("Expositions ajouté : " + expo);
					expositions.add(expo);
                }

            } else {
            	throw new IllegalArgumentException("Erreur dans la ligne " + (i + 1) + ": Format incorrect");
	        }
	    }
	}
	
	/**
	 * Importe les visites à partir d'une liste de chaînes.
	 *
	 * @param arraysVisites la liste de chaînes représentant les visites.
	 * @throws VisiteException si une erreur survient lors de l'importation.
	 * @throws EmployeException si une erreur survient lors de l'importation.
	 */
	public static void importerVisites(ArrayList<String> arraysVisites)  {
		String[] verifEntete = arraysVisites.get(0).split(";");
		if (verifEntete.length != 10
				|| !verifEntete[0].trim().equalsIgnoreCase("Ident")
				|| !verifEntete[1].trim().equalsIgnoreCase("Exposition")
				|| !verifEntete[2].trim().equalsIgnoreCase("Conférencier")
				|| !verifEntete[3].trim().equalsIgnoreCase("Employé")
				|| !verifEntete[4].trim().equalsIgnoreCase("date")
				|| !verifEntete[5].trim().equalsIgnoreCase("heuredebut")
				|| !verifEntete[6].trim().equalsIgnoreCase("Intitulé")
				|| !verifEntete[7].trim().equalsIgnoreCase("Téléphone")
				|| !verifEntete[8].trim().equalsIgnoreCase("")) {
			throw new IllegalArgumentException("Erreur dans la ligne 1: l'entete n'est pas correcte");
		}
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
					throw new IllegalArgumentException("1 ou plusieurs ID n'existent pas");
    			}
				
				if (idExistantVisites(idVisites)) {
    				throw new IllegalArgumentException("L'id viste existe déja");
    			}
				
				Visite visite = new Visite(idVisites, dateVisite, heureVisite, intitule, numTel);
				visite.setEmployeId(idEmploye);
				visite.setConferencierId(idConferencier);
				visite.setExpositionId(idExposition);
				System.out.println("Visite ajouté : " + visite);
				visites.add(visite);
			} else {
				throw new IllegalArgumentException("Erreur dans la ligne " + (i + 1) + ": Format incorrect");
			}
		}
	}
	
	/**
	 * Retourne la liste des employés.
	 *
	 * @return la liste des employés.
	 */
	public ArrayList<Employe> getEmployes() {
		return employes;
	}

	/**
	 * Retourne la liste des conférenciers.
	 *
	 * @return la liste des conférenciers.
	 */
	public ArrayList<Conferencier> getConferenciers() {
		return conferenciers;
	}

	/**
	 * Retourne la liste des expositions.
	 *
	 * @return la liste des expositions.
	 */
	public ArrayList<Exposition> getExpositions() {
		return expositions;
	}

	/**
	 * Retourne la liste des visites.
	 *
	 * @return la liste des visites.
	 */
	public ArrayList<Visite> getVisites() {
		return visites;
	}

	/**
	 * Permet de set des employes dans la liste des Employes
	 */
	public void setEmployes(ArrayList<Employe> employe) {
		employes = employe;
	}

	/**
	 * Permet de set des conferenciers dans la liste des Conferenciers
	 */
	public void setConferenciers(ArrayList<Conferencier> conferencier) {
		conferenciers = conferencier;
	}

	/**
	 * Permet de set des exposition dans la liste des Exposition
	 */
	public void setExpositions(ArrayList<Exposition> exposition) {
		expositions = exposition;
	}

	/**
	 * Permet de set des visites dans la liste des Visites
	 */
	public void setVisites(ArrayList<Visite> visite) {
		visites = visite;
	}

	/**
	 * Ajoute un employé à la liste des employés.
	 *
	 * @param employe l'employé à ajouter.
	 */
	public static void ajoutEmploye(Employe employe) {
		employes.add(employe);
	}

	/**
	 * Ajoute un conférencier à la liste des conférenciers.
	 *
	 * @param conferencier le conférencier à ajouter.
	 */
	public static void ajoutConferencier(Conferencier conferencier) {
		conferenciers.add(conferencier);
	}

	/**
	 * Ajoute une exposition à la liste des expositions.
	 *
	 * @param exposition l'exposition à ajouter.
	 */
	public static void ajoutExposition(Exposition exposition) {
		expositions.add(exposition);
	}

	/**
	 * Ajoute une visite à la liste des visites.
	 *
	 * @param visite la visite à ajouter.
	 */
	public static void ajoutVisite(Visite visite) {
		visites.add(visite);
	}

	/**
	 * Vérifie si un identifiant d'employé existe déjà.
	 *
	 * @param id l'identifiant à vérifier.
	 * @return true si l'identifiant existe, false sinon.
	 */
	public static boolean idExistantEmployes(String id) {
		for (Employe employe : employes) {
			if (employe.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Vérifie si un identifiant de conférencier existe déjà.
	 *
	 * @param id l'identifiant à vérifier.
	 * @return true si l'identifiant existe, false sinon.
	 */
	public static boolean idExistantConferenciers(String id) {
		for (Conferencier conferencier : conferenciers) {
			if (conferencier.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Vérifie si un employé avec le même nom et prénom existe déjà.
	 *
	 * @param nom le nom à vérifier.
	 * @param prenom le prénom à vérifier.
	 * @return true si un employé avec le même nom et prénom existe, false sinon.
	 */
	public static boolean homonymeEmployes(String nom, String prenom) {
		for (Employe employe : employes) {
			if (employe.getPrenom().equals(prenom) && employe.getNom().equals(nom)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Vérifie si un conférencier avec le même nom et prénom existe déjà.
	 *
	 * @param nom le nom à vérifier.
	 * @param prenom le prénom à vérifier.
	 * @return true si un conférencier avec le même nom et prénom existe, false sinon.
	 */
	public static boolean homonymeConferenciers(String nom, String prenom) {
		for (Conferencier conferencier : conferenciers) {
			if (conferencier.getPrenom().equals(prenom) && conferencier.getNom().equals(nom)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Vérifie si un identifiant d'exposition existe déjà.
	 *
	 * @param id l'identifiant à vérifier.
	 * @return true si l'identifiant existe, false sinon.
	 */
	public static boolean idExistantExpositions(String id) {
		for (Exposition exposition : expositions) {
			if (exposition.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Vérifie si un identifiant de visite existe déjà.
	 *
	 * @param id l'identifiant à vérifier.
	 * @return true si l'identifiant existe, false sinon.
	 */
	public static boolean idExistantVisites(String id) {
		for (Visite visite : visites) {
			if (visite.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Permet de reset la liste des Visites
	 */
	public static void clearListVisites() {
		visites.clear();
	}

	/**
	 * Permet de reset la liste des Conferencier
	 */
	public static void clearListConferenciers() {
		conferenciers.clear();
	}

	/**
	 * Permet de reset la liste des Employes
	 */
	public static void clearListEmployes() {
		employes.clear();
	}

	/**
	 * Permet de reset la liste des Expositions
	 */
	public static void clearListExpositions() {
		expositions.clear();
	}
}

