package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Filtre {

    private DonneesApplication donnees = new DonneesApplication();

    private ArrayList<Visite> visiteFiltre;
    private ArrayList<Exposition> expositionFiltre;
    private ArrayList<Conferencier> conferencierFiltre;

    private final ArrayList<Visite> visiteInitial = donnees.getVisites();
    private final ArrayList<Exposition> expositionInitial = donnees.getExpositions();
    private final ArrayList<Conferencier> conferencierInitial = donnees.getConferenciers();

    public Filtre() {
        this.visiteFiltre = new ArrayList<>(visiteInitial);
        this.expositionFiltre = new ArrayList<>(expositionInitial);
        this.conferencierFiltre = new ArrayList<>(conferencierInitial);
    }

    private void initialiserVisiteFiltre() {
        if (visiteFiltre.isEmpty()) {
            this.visiteFiltre = new ArrayList<>(visiteInitial);
        }
    }

    private void initialiserExpositionFiltre() {
        if (expositionFiltre.isEmpty()) {
            this.expositionFiltre = new ArrayList<>(expositionInitial);
        }
    }

    private void initialiserConferencierFiltre() {
        if (conferencierFiltre.isEmpty()) {
            this.conferencierFiltre = new ArrayList<>(conferencierInitial);
        }
    }

    public void conferencierNom(String nom, String prenom) {
        initialiserVisiteFiltre();
        String idConferencier = "";

        for (Conferencier conferencier : donnees.getConferenciers()) {
            if (nom.equals(conferencier.getNom()) && prenom.equals(conferencier.getPrenom())) {
                idConferencier = conferencier.getId();
                break;
            }
        }
        String finalIdConferencier = idConferencier;
        this.visiteFiltre.removeIf(visite -> !finalIdConferencier.equals(visite.getConferencierId()));
    }

    public void employeNom(String nom, String prenom) {
        initialiserVisiteFiltre();
        String idEmploye = "";

        for (Employe employe : donnees.getEmployes()) {
            if (nom.equals(employe.getNom()) && prenom.equals(employe.getPrenom())) {
                idEmploye = employe.getId();
                break;
            }
        }
        String finalIdEmploye = idEmploye;
        this.visiteFiltre.removeIf(visite -> !finalIdEmploye.equals(visite.getEmployeId()));
    }

    public void expositionIntitule(String intituleExpo) {
        initialiserVisiteFiltre();
        String idExposition = "";

        for (Exposition exposition : donnees.getExpositions()) {
            if (intituleExpo.equals(exposition.getIntitule())) {
                idExposition = exposition.getId();
                break;
            }
        }
        String finalIdExposition = idExposition;
        this.visiteFiltre.removeIf(visite -> !finalIdExposition.equals(visite.getExpositionId()));
    }

    public void visiteIntitule(String intituleVisite) {
        initialiserVisiteFiltre();
        this.visiteFiltre.removeIf(visite -> !intituleVisite.equals(visite.getIntitule()));
    }

    public void visiteNumTel(String numTel) {
        initialiserVisiteFiltre();
        this.visiteFiltre.removeIf(visite -> !numTel.equals(visite.getNumTel()));
    }

    public void datePrecise(Date datePrecise) {
        initialiserVisiteFiltre();
        this.visiteFiltre.removeIf(visite -> !datePrecise.equals(visite.getDateVisite()));
    }

    public void heurePrecise(String heurePrecise) throws ParseException {
        initialiserVisiteFiltre();
        SimpleDateFormat format = new SimpleDateFormat("HH'h'mm");
        Date heurePreciseDate = format.parse(heurePrecise);
        this.visiteFiltre.removeIf(visite -> !heurePreciseDate.equals(visite.getHeureVisite()));
    }

    public void datePeriode(Date dateDebut, Date dateFin) {
        initialiserVisiteFiltre();
        this.visiteFiltre.removeIf(visite ->
                visite.getDateVisite().before(dateDebut) || visite.getDateVisite().after(dateFin));
    }

    public void heurePeriode(String dateHeureDebut, String dateHeureFin) throws ParseException {
        initialiserVisiteFiltre();
        SimpleDateFormat format = new SimpleDateFormat("HH'h'mm");
        Date heureDebut = format.parse(dateHeureDebut);
        Date heureFin = format.parse(dateHeureFin);
        this.visiteFiltre.removeIf(visite ->
                visite.getHeureVisite().before(heureDebut) || visite.getHeureVisite().after(heureFin));
    }

    public void conferencierInterne() {
        initialiserVisiteFiltre();
        ArrayList<String> idConferencier = new ArrayList<>();

        for (Conferencier conferencier : donnees.getConferenciers()) {
            if (conferencier.getEstEmploye()) {
                idConferencier.add(conferencier.getId());
            }
        }

        this.visiteFiltre.removeIf(visite -> !idConferencier.contains(visite.getConferencierId()));
    }

    public void conferencierExterne() {
        initialiserVisiteFiltre();
        ArrayList<String> idConferencier = new ArrayList<>();

        for (Conferencier conferencier : donnees.getConferenciers()) {
            if (!conferencier.getEstEmploye()) {
                idConferencier.add(conferencier.getId());
            }
        }

        this.visiteFiltre.removeIf(visite -> !idConferencier.contains(visite.getConferencierId()));
    }

    public void expositionPermanente() {
        initialiserVisiteFiltre();
        ArrayList<String> idExposition = new ArrayList<>();

        for (Exposition exposition : donnees.getExpositions()) {
            if (!exposition.estTemporaire()) {
                idExposition.add(exposition.getId());
            }
        }

        this.visiteFiltre.removeIf(visite -> !idExposition.contains(visite.getExpositionId()));
    }

    public void expositionTemporaire() {
        initialiserVisiteFiltre();
        ArrayList<String> idExposition = new ArrayList<>();

        for (Exposition exposition : donnees.getExpositions()) {
            if (exposition.estTemporaire()) {
                idExposition.add(exposition.getId());
            }
        }

        this.visiteFiltre.removeIf(visite -> !idExposition.contains(visite.getExpositionId()));
    }

    public void reset() {
        this.visiteFiltre = new ArrayList<>(visiteInitial);
        this.expositionFiltre = new ArrayList<>(expositionInitial);
        this.conferencierFiltre = new ArrayList<>(conferencierInitial);
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
}
