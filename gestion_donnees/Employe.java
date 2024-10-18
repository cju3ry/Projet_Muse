package gestion_donnees;

import java.util.ArrayList;

public class Employe {
	private String id;
	private String nom;
	private String prenom;
	private String numTel;
	
	public Employe(String id, String nom, String prenom, String numTel) throws EmployeException {
		if (id == null || id.length() != 7) {
			throw new EmployeException();
		}
		
		for (Employe employe : gestionDonnees.employes) {
			if (employe.getId().equals(id)) {
				throw new EmployeException();
			}
			
			if (employe.getPrenom() == prenom) {
				if (employe.getNom() == nom) {
					throw new EmployeException();
				}
			}
		}
		
		if (numTel != null) {
			if (numTel.length() != 4) {
				throw new EmployeException();
			}
		}
		
		
		
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.numTel = numTel;
		gestionDonnees.employes.add(this);
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
		return "	Employé(e) : " + this.nom + " " + this.prenom + "\n";
	}
}
