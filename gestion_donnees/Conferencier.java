
package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Conferencier {
	
	public DonneesApplication donnees = new DonneesApplication();
	private String id;
	private String nom;
	private String prenom;
	private boolean estEmploye;
	private ArrayList<String> indisponibilite;
	private String[] specialite;
	private String numTel;
	
	public Conferencier(String id, String nom, String prenom) throws ConferencierException {
		if (id.length() != 7 || id == null) {
			throw new ConferencierException();
		}
		
		if(donnees.idExistantConferenciers(id)) {
			throw new ConferencierException();
		}
		
		if(donnees.homonymeEmplpoye(nom, prenom)) {
			throw new ConferencierException();
		}
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
	}
	
	public void setNumTel(String numTel) throws ConferencierException {
		if (numTel.length() != 10 || numTel == null) {
			throw new ConferencierException();
		}
		this.numTel = numTel;
	}
	
	public void setSpecialitees(String[] specialite) throws ConferencierException {
		if (specialite.length > 6) {
			throw new ConferencierException();
		}
		this.specialite = specialite;
	}
		
	public void setEstEmploye(boolean estEmploye) {
		this.estEmploye = estEmploye;
	}
	
	public void setIndisponibilite(ArrayList<String> indisponibilite) throws ConferencierException {
		if(!indisponibiliteOk(indisponibilite)) {
			throw new ConferencierException();
		}
		this.indisponibilite = indisponibilite;
	}
	
	private boolean indisponibiliteOk(ArrayList<String> indisponibilite)  {
		
		int increment = 1;
		SimpleDateFormat formatIndisponibilite = new SimpleDateFormat("dd/MM/yyyy");
		formatIndisponibilite.setLenient(false);
		
		if (indisponibilite.size() == 0) {
			return true;
		}
		
		for(int j = 0; j < indisponibilite.size(); j += 2 ) {
			try {
				if (formatIndisponibilite.parse(indisponibilite.get(j)).getTime() > formatIndisponibilite.parse(indisponibilite.get(increment)).getTime()) {
					return false;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			increment += 2;
		}
		return true;
		
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
	
    public static void main(String[] args) throws EmployeException, ExpositionException, ConferencierException, VisiteException {
    	//gestionDonnees.initialisesDonnees();
    	
    	DonneesApplication donneees = new DonneesApplication();
       
        ArrayList<String> indisponibilite = new ArrayList<>();

        String[] specialites = {"Java", "AI", "Data Science"};

        Conferencier conferencier = new Conferencier("ABC1236","Dupont","Jean");
        conferencier.setNumTel("0612345678");
        conferencier.setEstEmploye(true);
        conferencier.setIndisponibilite(indisponibilite);
        conferencier.setSpecialitees(specialites);
        
        donneees.ajoutConferencier(conferencier);
        
        Conferencier conferencier2 = new Conferencier("ABC1236","Dupond","Jean");
        conferencier2.setNumTel("0612345678");
        conferencier2.setEstEmploye(true);
        conferencier2.setIndisponibilite(indisponibilite);
        conferencier2.setSpecialitees(specialites);
        
        donneees.ajoutConferencier(conferencier2);
        
        //System.out.println(conferencier.toString());
        
        for (Conferencier conferencier25 :donneees.getConferenciers()) {
        	System.out.print(conferencier25.toString());
        }
            
        
    }
}
