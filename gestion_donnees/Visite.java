package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Visite {
	private String id;
	private String intitule;
	private Date dateVisite;
	private Date heureVisite;
	private String telephone;
	private String exposition;
	private String employe;
	private String conferencier;
	
	public Visite(String idVisite, String intitule, String dateVisite, String heureVisite, String expositionId, String employeId, String conferencierId, String numTel) throws VisiteException, EmployeException {
		
		SimpleDateFormat jourVisite = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat heureDebut = new SimpleDateFormat("HH'h'mm");
		heureDebut.setLenient(false);
		jourVisite.setLenient(false);
		
		
		if (idVisite.length() != 7 || idVisite == null) {
			throw new VisiteException();
		} else {
			for (Visite visite : gestionDonnees.visites) {
				if (visite.getId() == id) {
					throw new VisiteException();
				}
			}
			this.id = idVisite;
		}
		
		
		if (employeId.length() != 7 || employeId == null) {
			throw new VisiteException();
		} else {
			Employe employeTrouvé = null;
			for (Employe employe : gestionDonnees.employes) {
				if (employe.getId() == employeId) {
					employeTrouvé = employe;
				}
			}
			if (employeTrouvé != null) {
			     this.employe = employeId;
			 } else {
			     throw new VisiteException();
			 }
		}
		
		
		
		if (expositionId.length() != 7 || expositionId == null) {
			throw new VisiteException();
		} else {
			Exposition expositionTrouvée = null;
			
			for (Exposition exposition : gestionDonnees.expositions) {
			        if (exposition.getId() == expositionId) {
			            expositionTrouvée = exposition;
			        }
			    }
			    
			 if (expositionTrouvée != null) {
			     this.exposition = employeId;
			 } else {
			     throw new VisiteException();
			 }
		}
		
		if (conferencierId.length() != 7 || conferencierId == null) {
			throw new VisiteException();
		} else {
			Conferencier conferencierTrouvé = null;
			
			for (Conferencier conferenciers : gestionDonnees.conferenciers) {
			        if (conferenciers.getId() == conferencierId) {
			            conferencierTrouvé = conferenciers;
			        }
			    }
			    
			 if (conferencierTrouvé != null) {
				 this.conferencier = conferencierId;
			 } else {
			     throw new VisiteException();
			 }
		}
		
		
		if (numTel.length() != 9 || numTel == null) {
			throw new VisiteException();
		}
		
		if(intitule == null) {
			throw new VisiteException();
		}
		
		if(dateVisite != null && heureVisite != null) {
			try {
				this.dateVisite = jourVisite.parse(dateVisite);
				this.heureVisite = heureDebut.parse(heureVisite);
			} catch (ParseException e) {
				throw new VisiteException();
			}
		} else {
			throw new VisiteException();
		}
			
		this.intitule = intitule;
		this.telephone = numTel;
		gestionDonnees.visites.add(this);
	}
	
	public String getId() {
		return this.id;
	}
	
	public String toString() {
		return "	Visite : " + this.intitule;
	}
	
	public static void main(String args[]) throws VisiteException, EmployeException, ExpositionException, ConferencierException {

	    gestionDonnees.initialisesDonnees();
	    
	    String[] tab = new String[10];
		tab[0] = "dbzayudza";
		tab[1] = "salut";
		
		String[] specialites = {"Java", "AI", "Data Science"};
		
		ArrayList<Date> indisponibilite = new ArrayList<>();
        indisponibilite.add(new Date()); 
	    

	    Employe employeLiée = new Employe("1111111", "Clement", "juery", "1234");
	    

	    Exposition expositionLiée = new Exposition("E000002", "salut", "dzayudfgauyfaf", 6, true, tab, "1984", "1987", "12/10/2012", "12/12/2024");
	    
	    		
	    Conferencier conferencier = new Conferencier("ABC1234","Dupont","Jean",true,indisponibilite, specialites,"0612345678");

	    String idVisite = "VST1234"; 
	    String intitule = "Visite guidée des chefs-d'œuvre";
	    String dateVisite = "18/12/2020";  
	    String numTel = "111111111";
	    String heureVisite = "15h00";
	    
	    Visite visite1 = new Visite(idVisite, intitule, dateVisite, heureVisite, "E000002", "1111111", "ABC1234", numTel);
	        
	    System.out.println(visite1.toString());
	  
	}

}
