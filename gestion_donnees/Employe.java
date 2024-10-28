package gestion_donnees;

/**
 * Classe employé.
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
     * @throws IllegalArgumentException Si l'id n'a pas une longueur de 7 caractères ou si le numéro de téléphone n'a pas une longueur de 4 caractères.
     */
    public Employe(String id, String nom, String prenom, String numTel) {

        // Vérifie que l'ID n'est pas null et a exactement 7 caractères
        if (id == null || id.length() != 7 ) {
            throw new IllegalArgumentException("L'identifiant doit être de 7 caractères.");
        }

        // Si le numéro de téléphone n'est pas null, on vérifie qu'il a 4 caractères
        if (numTel != null && numTel.length() != 4) {
            throw new IllegalArgumentException("Le numéro de téléphone doit être de 4 caractères.");
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
     * Retourne le numéro de téléphone de l'employé.
     * 
     * @return Le numéro de téléphone de l'employé.
     */
    public String getNumTel() {
        return numTel;
    }

    /**
     * Retourne le nom et le prénom de l'employé et le numéro de téléphone sous la forme :
     * "Employé(e) :
     * Nom : [nom]
     * Prénom : [prénom]
     * Numéro de téléphone : [numTel]"
     * 
     * @return Une chaîne de caractères représentant l'employé.
     */
    @Override
    public String toString() {
        return "\tEmployé(e) : " + "\n" +
               "\tNom : " + nom + "\n" +
               "\tPrénom : " + prenom + "\n" +
               "\tNuméro de téléphone : " + this.numTel + "\n";
    }
}
