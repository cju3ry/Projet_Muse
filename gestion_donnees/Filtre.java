package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Filtre {
    
    private DonneesApplication donnees = new DonneesApplication();
    
    private ArrayList<Visite> visiteFiltre;
    
    private ArrayList<Exposition> expositionFiltre;
    
    private ArrayList<Conferencier> conferencierFiltre;
    
    public Filtre() {
        visiteFiltre = donnees.getVisites();
        expositionFiltre = donnees.getExpositions();
        conferencierFiltre = donnees.getConferenciers();
    }
    
    public void filtreConferencierNom(String nom, String prenom) {
        String idConferencier = "";
        
        // récupération de l'id du conférencier en fonction de son nom
        for (Conferencier conferencier : donnees.getConferenciers()) {
            if (nom.equals(conferencier.getNom()) && prenom.equals(conferencier.getPrenom())) {
                idConferencier = conferencier.getId();
            }
        }
        
        // mise à jour de la liste des visites par rapport à un conférencier
        Iterator<Visite> iterator = this.visiteFiltre.iterator();
        while (iterator.hasNext()) {
            Visite visite = iterator.next();
            if (!idConferencier.equals(visite.getConferencierId())) {
                iterator.remove();
            }
        }
    }
    
    public void filtreEmployeNom(String nom, String prenom) {
        String idEmploye = "";
        
        // récupération de l'id de l'employé en fonction de son nom
        for (Employe employe : donnees.getEmployes()) {
            if (nom.equals(employe.getNom()) && prenom.equals(employe.getPrenom())) {
                idEmploye = employe.getId();
            }
        }
        
        // mise à jour de la liste des visites par rapport à un employé
        Iterator<Visite> iterator = this.visiteFiltre.iterator();
        while (iterator.hasNext()) {
            Visite visite = iterator.next();
            if (!idEmploye.equals(visite.getEmployeId())) {
                iterator.remove();
            }
        }
    }
    
    public void filtreExpositionIntitule(String intituleExpo) {
        String idExposition = "";
        
        // récupération de l'id de l'exposition en fonction de son intitulé
        for (Exposition exposition : donnees.getExpositions()) {
            if (intituleExpo.equals(exposition.getIntitule())) {
                idExposition = exposition.getId();
            }
        }
        
        // mise à jour de la liste des visites par rapport à une exposition
        Iterator<Visite> iterator = this.visiteFiltre.iterator();
        while (iterator.hasNext()) {
            Visite visite = iterator.next();
            if (!idExposition.equals(visite.getExpositionId())) {
                iterator.remove();
            }
        }
    }
    
    public void filtreVisiteIntitule(String intituleVisite) {
        // Utilisation d'un Iterator pour éviter ConcurrentModificationException
        Iterator<Visite> iterator = this.visiteFiltre.iterator();
        while (iterator.hasNext()) {
            Visite visite = iterator.next();
            if (!intituleVisite.equals(visite.getIntitule())) {
                iterator.remove();
            }
        }
    }
    
    public void filtreVisiteNumTel(String numTel) {
        // Utilisation d'un Iterator pour éviter ConcurrentModificationException
        Iterator<Visite> iterator = this.visiteFiltre.iterator();
        while (iterator.hasNext()) {
            Visite visite = iterator.next();
            if (!numTel.equals(visite.getNumTel())) {
                iterator.remove();
            }
        }
    }
    
    public void filtreDatePrecise(Date datePrecise) {
        Iterator<Visite> iterator = this.visiteFiltre.iterator();
        while (iterator.hasNext()) {
            Visite visite = iterator.next();
            if (!datePrecise.equals(visite.getDateVisite())) {
                iterator.remove();
            }
        }
    }
    
    public void filtreHeurePrecise(String heurePrecise) {
        Iterator<Visite> iterator = this.visiteFiltre.iterator();
        while (iterator.hasNext()) {
            Visite visite = iterator.next();
            if (!heurePrecise.equals(visite.getDateVisite().toString())) {
                iterator.remove();
            }
        }
    }
    
    public void filtreDatePeriode(Date dateDebut, Date dateFin) {
        if (dateDebut.compareTo(dateFin) > 0) {
            throw new IllegalArgumentException("La date de début ne doit pas être supérieure à la date de fin.");
        }
        
        Iterator<Visite> iterator = this.visiteFiltre.iterator();
        while (iterator.hasNext()) {
            Visite visite = iterator.next();
            Date dateVisite = visite.getDateVisite();
            if (dateVisite.before(dateDebut) || dateVisite.after(dateFin)) {
                iterator.remove();
            }
        }
    }
    
    public void filtreHeurePeriode(String dateHeureDebut, String dateHeureFin) throws ParseException {
        SimpleDateFormat formatHeure = new SimpleDateFormat("HH'h'mm");
        
        Date heureDebut = formatHeure.parse(dateHeureDebut);
        Date heureFin = formatHeure.parse(dateHeureFin);
        
        Iterator<Visite> iterator = this.visiteFiltre.iterator();
        while (iterator.hasNext()) {
            Visite visite = iterator.next();
            Date heureVisite = visite.getHeureVisite();
            if (heureVisite.before(heureDebut) || heureVisite.after(heureFin)) {
                iterator.remove();
            }
        }
    }
    
    public void filtreConferencierInterne() {
        ArrayList<String> idConferencier = new ArrayList<>();
        
        // récupération de l'id du conférencier interne
        for (Conferencier conferencier : donnees.getConferenciers()) {
            if (conferencier.getEstEmploye()) {
                idConferencier.add(conferencier.getId());
            }
        }
        
        Iterator<Visite> iterator = this.visiteFiltre.iterator();
        while (iterator.hasNext()) {
            Visite visite = iterator.next();
            if (!idConferencier.contains(visite.getConferencierId())) {
                iterator.remove();
            }
        }
    }
    
    public void filtreConferencierExterne() {
        ArrayList<String> idConferencier = new ArrayList<>();
        
        // récupération de l'id du conférencier externe
        for (Conferencier conferencier : donnees.getConferenciers()) {
            if (!conferencier.getEstEmploye()) {
                idConferencier.add(conferencier.getId());
            }
        }
        
        Iterator<Visite> iterator = this.visiteFiltre.iterator();
        while (iterator.hasNext()) {
            Visite visite = iterator.next();
            if (!idConferencier.contains(visite.getConferencierId())) {
                iterator.remove();
            }
        }
    }
    
    public void filtreExpositionPermanente() {
        ArrayList<String> idExposition = new ArrayList<>();
        
        for (Exposition exposition : donnees.getExpositions()) {
            if (!exposition.estTemporaire()) {
                idExposition.add(exposition.getId());
            }
        }
        
        Iterator<Visite> iterator = this.visiteFiltre.iterator();
        while (iterator.hasNext()) {
            Visite visite = iterator.next();
            if (!idExposition.contains(visite.getExpositionId())) {
                iterator.remove();
            }
        }
    }
    
    public void filtreExpositionTemporaire() {
        ArrayList<String> idExposition = new ArrayList<>();
        
        for (Exposition exposition : donnees.getExpositions()) {
            if (exposition.estTemporaire()) {
                idExposition.add(exposition.getId());
            }
        }
        
        Iterator<Visite> iterator = this.visiteFiltre.iterator();
        while (iterator.hasNext()) {
            Visite visite = iterator.next();
            if (!idExposition.contains(visite.getExpositionId())) {
                iterator.remove();
            }
        }
    }
    
    public ArrayList<Visite> getListeVisite() {
        return this.visiteFiltre;
    }
    
    public ArrayList<Exposition> getListeExposition() {
        return this.expositionFiltre;
    }
    
    public ArrayList<Conferencier> getListeConferencier() {
        return this.conferencierFiltre;
    }
    
    public void filtreExpoVisitePeriode(Date dateDebut, Date dateFin) {
        if (dateDebut.compareTo(dateFin) > 0) {
            throw new IllegalArgumentException("La date de début ne doit pas être supérieure à la date de fin.");
        }
        
        Iterator<Visite> iteratorVisite = donnees.getVisites().iterator();
        while (iteratorVisite.hasNext()) {
            Visite visite = iteratorVisite.next();
            if (!visite.getDateVisite().before(dateDebut) && !visite.getDateVisite().after(dateFin)) {
                Iterator<Exposition> iteratorExpo = this.expositionFiltre.iterator();
                while (iteratorExpo.hasNext()) {
                    Exposition exposition = iteratorExpo.next();
                    if (visite.getExpositionId().equals(exposition.getId())) {
                        iteratorExpo.remove();
                    }
                }
            }
        }
    }
    
    public void filtreExpoVisiteHoraire(String dateHeureDebut, String dateHeureFin) throws ParseException {
        SimpleDateFormat formatHeure = new SimpleDateFormat("HH'h'mm");
        
        Date heureDebut = formatHeure.parse(dateHeureDebut);
        Date heureFin = formatHeure.parse(dateHeureFin);
        
        Iterator<Visite> iteratorVisite = donnees.getVisites().iterator();
        while (iteratorVisite.hasNext()) {
            Visite visite = iteratorVisite.next();
            if (!visite.getHeureVisite().before(heureDebut) && !visite.getHeureVisite().after(heureFin)) {
                Iterator<Exposition> iteratorExpo = this.expositionFiltre.iterator();
                while (iteratorExpo.hasNext()) {
                    Exposition exposition = iteratorExpo.next();
                    if (visite.getExpositionId().equals(exposition.getId())) {
                        iteratorExpo.remove();
                    }
                }
            }
        }
    }
    
    public void filtreConfVisitePeriode(Date dateDebut, Date dateFin) {
        if (dateDebut.compareTo(dateFin) > 0) {
            throw new IllegalArgumentException("La date de début ne doit pas être supérieure à la date de fin.");
        }
        
        Iterator<Visite> iteratorVisite = donnees.getVisites().iterator();
        while (iteratorVisite.hasNext()) {
            Visite visite = iteratorVisite.next();
            if (!visite.getDateVisite().before(dateDebut) && !visite.getDateVisite().after(dateFin)) {
                Iterator<Conferencier> iteratorConf = this.conferencierFiltre.iterator();
                while (iteratorConf.hasNext()) {
                    Conferencier conferencier = iteratorConf.next();
                    if (visite.getConferencierId().equals(conferencier.getId())) {
                    	iteratorConf.remove();
                    }
                }
            }
        }
    }
    
    public void filtreConfVisiteHoraire(String dateHeureDebut, String dateHeureFin) throws ParseException {
        SimpleDateFormat formatHeure = new SimpleDateFormat("HH'h'mm");
        
        Date heureDebut = formatHeure.parse(dateHeureDebut);
        Date heureFin = formatHeure.parse(dateHeureFin);
        
        Iterator<Visite> iteratorVisite = donnees.getVisites().iterator();
        while (iteratorVisite.hasNext()) {
            Visite visite = iteratorVisite.next();
            if (!visite.getHeureVisite().before(heureDebut) && !visite.getHeureVisite().after(heureFin)) {
            	Iterator<Conferencier> iteratorConf = this.conferencierFiltre.iterator();
                while (iteratorConf.hasNext()) {
                	Conferencier conferencier = iteratorConf.next();
                    if (visite.getConferencierId().equals(conferencier.getId())) {
                    	iteratorConf.remove();
                    }
                }
            }
        }
    }
    
    public static void main(String[] args) throws ParseException {
    	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        DonneesApplication donnees = new DonneesApplication();
        Filtre filtre = new Filtre();
        
        Date glaceALaFraise = format.parse("01/11/2024");
        Date diaboloMenthe = format.parse("3/12/2024");
        
        String monaco = "13h00";
        String sandwitchALaFraise = "17h00";
        
        donnees.importerConferenciers(DonneesApplication.LireCsv("conferencier 28_08_24 17_26.csv"));
        donnees.importerEmployes(DonneesApplication.LireCsv("employes 28_08_24 17_26.csv"));
        donnees.importerExpositions(DonneesApplication.LireCsv("expositions 28_08_24 17_26.csv"));
        donnees.importerVisites(DonneesApplication.LireCsv("visites 28_08_24 17_26.csv"));
        
        // Exemple de filtre
        // filtre.filtreConferencierInterne();
        // filtre.filtreDatePeriode(glaceALaFraise, diaboloMenthe);
        // filtre.filtreHeurePeriode(monaco, sandwitchALaFraise);
        // filtre.filtreExpositionPermanente();
        // filtre.filtreExpoVisiteHoraire(monaco, sandwitchALaFraise);
        
//        filtre.filtreExpositionIntitule("Les paysages impressionnistes");
//        filtre.filtreConferencierNom("Lexpert", "Noemie");
//        filtre.filtreDatePrecise(diaboloMenthe);
        
        for (Visite visite : filtre.getListeVisite()) {
            System.out.print(visite.getId() + "\n");
        }
        
//        for (Exposition exposition : filtre.getListeExposition()) {
//            System.out.print(exposition.getId() + "\n");
//        }
    }
}