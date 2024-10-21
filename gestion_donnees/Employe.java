/*
 * Date       21/10/2024
 * IUT de Rodez
 * @copyright Tous droits réservés. Ce fichier est soumis aux termes du droit d'auteur et 
 * ne peut être reproduit, distribué, ou modifié sans l'autorisation expresse du détenteur du droit d'auteur.
 */

package gestion_donnees;

/**
 * Classe employé.
 * @author Cazor--Bonnet Adrian
 * @author Chesnier Quentin
 * @author Juery Clément
 * @author Ladureau Baptiste
 */
public class Employe {
   
    private String id;  
    private String nom;      
    private String prenom;   
    private String numTel;   

    /**
     * Constructeur de la classe Employe.
     * 
     * @param id L'identifiant unique de l'employé (doit être de 7 caractères).
     * @param nom Le nom de l'employé.
     * @param prenom Le prénom de l'employé.
     * @param numTel Le numéro de téléphone de l'employé (doit être de 4 caractères ou null).
     * @throws EmployeException Si l'id n'a pas une longueur de 7 caractères ou si le numéro de téléphone n'a pas une longueur de 4 caractères.
     */
    public Employe(String id, String nom, String prenom, String numTel) throws EmployeException {
    	
        // Vérifie que l'ID n'est pas null et a exactement 7 caractères
        if (id == null || id.length() != 7 ) {
            throw new EmployeException();
        }

        // Si le numéro de téléphone n'est pas null, on vérifie qu'il a 4 caractères
        if (numTel != null) {
            if (numTel.length() != 4) {
                throw new EmployeException();
            }
        }

        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.numTel = numTel;
    }

    /**
     * Retourne l'identifiant unique de l'employé.
     * 
     * @return L'identifiant de l'employé (String de 7 caractères).
     */
    public String getId() {
        return this.id;
    }

    /**
     * Retourne le nom de l'employé.
     * 
     * @return Le nom de l'employé.
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Retourne le prénom de l'employé.
     * 
     * @return Le prénom de l'employé.
     */
    public String getPrenom() {
        return this.prenom;
    }
    
    /**
     * Retourne le numéro de téléphonne de l'employé.
     * 
     * @return Le téléphonne de l'employé.
     */
    public String getNumTel() {
		return numTel;
	}

    /**
     * Retourne le nom et le prenom de l'employé sous la forme :
     * "Employé(e) : [nom] [prénom]".
     * 
     * @return Une chaîne de caractères représentant l'employé.
     */
    @Override
    public String toString() {
        return "	Employé(e) : " + this.nom + " " + this.prenom + "\n";
    }
	
}
