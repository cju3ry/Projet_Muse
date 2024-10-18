package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Exposition {
	private String id;
	private String intitule;
	private String resume;
	private int nbOeuvre;
	private boolean estTemporaire;
	private String[] motCles;
	private Date periodeDebut;
	private Date periodeFin;
	private Date debutExpo;
	private Date finExpo;
	
	public Exposition(String id, String intitule, String resume, int nbOeuvre, boolean estTemporaire, String[] motCles, String periodeDebut, String periodeFin, String debutExpo, String finExpo) throws ExpositionException {
		
		SimpleDateFormat periode = new SimpleDateFormat("yyyy");
		SimpleDateFormat tempsExpo = new SimpleDateFormat("dd/MM/yyyy");
		
		if (id.length() != 7) {
			throw new ExpositionException();
		}
		
		for (Exposition exposition : gestionDonnees.expositions) {
			if (exposition.getId() == id) {
				throw new ExpositionException();
			}
		}
		
		if (intitule == null) {
			throw new ExpositionException();
		}
		
		if (nbOeuvre <= 0) {
			throw new ExpositionException();
		}
		
		if (periodeDebut == null && periodeFin == null) {
			throw new ExpositionException();
		}
		
		if (estTemporaire == true && debutExpo == null && finExpo == null) {
			throw new ExpositionException();
		}
		
		try {
			if (periode.parse(periodeDebut).getTime() > periode.parse(periodeFin).getTime()
				|| estTemporaire == true && periode.parse(debutExpo).getTime() > periode.parse(finExpo).getTime()) {
				throw new ExpositionException();
			}
			
			this.periodeDebut = periode.parse(periodeDebut);
			this.periodeFin = periode.parse(periodeFin);
			
			if (debutExpo != null && finExpo != null) {
				this.debutExpo = tempsExpo.parse(debutExpo);
				this.finExpo = tempsExpo.parse(finExpo);
			} else {
				this.debutExpo = null;
				this.finExpo = null;
			}
			
		} catch (ParseException e) {
			System.out.print("Format de la date incorrect ou inexistante !!!");
		}
		
		this.id = id;
		this.intitule = intitule;
		this.resume = resume;
		this.nbOeuvre = nbOeuvre;
		this.estTemporaire = estTemporaire;
		this.motCles = motCles;
		gestionDonnees.expositions.add(this);
	}
	
	public String getId() {
		return this.id;
	}
	
	public String toString() {
		return "	Exposition : " + this.intitule + "\n";
	}
	
	public static void main (String args[]) throws EmployeException, ExpositionException, ConferencierException, VisiteException {
		gestionDonnees.initialisesDonnees();
		String[] tab = new String[2];
		tab[0] = "dbzayudza";
		tab[1] = "salut";
		Exposition expo1 = new Exposition("E000001", "coucou", "dzayudfgauyfaf", 4, false, tab, "1984", "1987", null, null);
		
		// Exposition expo2 = new Exposition("E000001", "Juery", "Clément", null);
		
		System.out.print(expo1.toString());
//		System.out.print(expo2.toString());
		
		for (Exposition exposition : gestionDonnees.expositions) {
			System.out.print(exposition.toString());
		}
	}
}
