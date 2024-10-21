
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
	private String expositionId;
	private String employeId;
	private String conferencierId;
	// String expositionId, String conferencierId, String employeId, 
	public Visite(String idVisite, String dateVisite, String heureVisite, String intitule, String numTel) throws VisiteException, EmployeException {
		
		SimpleDateFormat jourVisite = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat heureDebut = new SimpleDateFormat("HH'h'mm");
		heureDebut.setLenient(false);
		jourVisite.setLenient(false);
		
		
		if (idVisite.length() != 7 || idVisite == null) {
			throw new VisiteException();
		}
			
		if (numTel.length() != 10 || numTel == null) {
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
		
		this.id = idVisite;
		this.intitule = intitule;
		this.telephone = numTel;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String toString() {
		return "	Visite : " + this.intitule;
	}
	
	public void setExpositionId(String expositionId) throws VisiteException {
		if (expositionId.length() != 7 || expositionId == null) {
			throw new VisiteException();
		}
		this.expositionId = expositionId;
	}
	
	public void setConferencierId(String conferencierId) throws VisiteException {
		if (conferencierId.length() != 7 || conferencierId == null) {
			throw new VisiteException();
		}
			this.conferencierId = conferencierId;
	}
	
	public void setEmployeId(String employeId) throws VisiteException {
		if (employeId.length() != 7 || employeId == null) {
			throw new VisiteException();
		} else {
			this.employeId = employeId;
		}
	}
}
