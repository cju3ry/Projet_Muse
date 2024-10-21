
package gestion_donnees;

import java.util.ArrayList;

public class Employe {
	private String id;
	private String nom;
	private String prenom;
	private String numTel;
	
	public Employe(String id, String nom, String prenom, String numTel) throws EmployeException {
		if (id.length() != 7 || id == null) {
			throw new EmployeException();
		}
		
		for (Employe employe : DonneesApplication.employes) {
			if (employe.getId() == id) {
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
		donnees.ajoutEmploye.add(this);
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
