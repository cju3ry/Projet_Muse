package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import application.ControleurImporterLocal;

public class Statistiques {
    
    SimpleDateFormat format;

    DonneesApplication donnees = ControleurImporterLocal.getDonnees();

    final ArrayList<Visite> visiteInitial = donnees.getVisites();

    public ArrayList<Visite> visiteFiltre;

    public Statistiques() {
        format = new SimpleDateFormat("dd/MM/yyyy");
        visiteFiltre = donnees.getVisites();
    }
    
    public void initialiserVisiteFiltre() {
        if (visiteFiltre.isEmpty()) {
            this.visiteFiltre = new ArrayList<>(visiteInitial);
        }
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

    // Méthode pour filtrer les visites pour ne conserver que celles ayant eu lieu dans une période donnée
    public void visitePeriode(Date dateDebut, Date dateFin) {
        initialiserVisiteFiltre();
        // Vérification des dates de début et de fin
        if (dateDebut.compareTo(dateFin) > 0) {
            throw new IllegalArgumentException("La date de début ne doit pas être supérieure à la date de fin.");
        }
        // Filtrer les visites par période
        this.visiteFiltre.removeIf(visite ->
                visite.getDateVisite().before(dateDebut) || visite.getDateVisite().after(dateFin)
        );
    }

    // Methode pour filtrer les visites pour ne conserver que celles ayant eu lieu dans une plage horaire donnée
    public void visitePlageHoraire(String heureDebut, String heureFin) throws ParseException {
        initialiserVisiteFiltre();
        SimpleDateFormat formatHeure = new SimpleDateFormat("HH'h'mm");
        Date heureDebutParse = formatHeure.parse(heureDebut);
        Date heureFinParse = formatHeure.parse(heureFin);

        // Vérification des heures de début et de fin
        if (heureDebutParse.compareTo(heureFinParse) > 0) {
            throw new IllegalArgumentException("L'heure de début ne doit pas être supérieure à l'heure de fin.");
        }
        // Filtrer les visites par plage horaire
        this.visiteFiltre.removeIf(visite ->
                visite.getHeureVisite().before(heureDebutParse) || visite.getHeureVisite().after(heureFinParse)
        );
    }

    /**
     * Méthode pour obtenir le nombre de visites pour chaque exposition de la liste des visites filtrées
     * @return une map avec l'ID de l'exposition comme clé et le nombre de visites comme valeur
     */
    public Map<String, Integer> getNbVisitesParExposition() {
        initialiserVisiteFiltre();
        Map<String, Integer> visitesParExposition = new HashMap<>();
        for (Visite visite : visiteFiltre) {
            String expositionId = visite.getExpositionId();
            visitesParExposition.put(expositionId, visitesParExposition.getOrDefault(expositionId, 0) + 1);
        }
        return visitesParExposition;
    }

    /**
     * Méthode pour obtenir le pourcentage de visites pour chaque exposition de la liste des visites filtrées
     * @return une map avec l'ID de l'exposition comme clé et le pourcentage de visites comme valeur
     */
    public Map<String, Double> getPVisitesExpositions() {
        initialiserVisiteFiltre();
        Map<String, Integer> visitesParExposition = getNbVisitesParExposition();
        Map<String, Double> pourcentageParExposition = new HashMap<>();
        int totalVisites = visiteFiltre.size();

        for (Map.Entry<String, Integer> entry : visitesParExposition.entrySet()) {
            double pourcentage = (entry.getValue() * 100.0) / totalVisites;
            pourcentageParExposition.put(entry.getKey(), pourcentage);
        }

        return pourcentageParExposition;
    }

    /**
     * Méthode pour obtenir le pourcentage de visites effectuées par chaque conférencier
     * @return une map avec l'ID du conférencier comme clé et le pourcentage de visites comme valeur
     */
    public Map<String, Double> getPVisitesConferenciers() {
        initialiserVisiteFiltre();
        Map<String, Integer> visitesParConferencier = new HashMap<>();
        for (Visite visite : visiteFiltre) {
            String conferencierId = visite.getConferencierId();
            visitesParConferencier.put(conferencierId, visitesParConferencier.getOrDefault(conferencierId, 0) + 1);
        }

        Map<String, Double> pourcentageParConferencier = new HashMap<>();
        int totalVisites = visiteFiltre.size();
        System.out.print(visiteFiltre.size());

        for (Map.Entry<String, Integer> entry : visitesParConferencier.entrySet()) {
            double pourcentage = (entry.getValue() * 100.0) / totalVisites;
            pourcentageParConferencier.put(entry.getKey(), pourcentage);
        }

        return pourcentageParConferencier;
    }
    
    public StringBuilder afficherPVisitesConferencier() {
        StringBuilder str = new StringBuilder();
        Map<String, Double> pourcentageParConferencier = getPVisitesConferenciers();
        for (Map.Entry<String, Double> entry : pourcentageParConferencier.entrySet()) {
            str.append("Conférencier ")
                    .append(entry.getKey())
                    .append(": ")
                    .append(getNomPrenomConferencierById(entry.getKey()))
                    .append(": ")
                    .append(Math.round(entry.getValue()))
                    .append("% des visites\n");
        }
        return str;
    }

    public String getNomPrenomConferencierById(String id) {
        for (Conferencier conferencier : donnees.getConferenciers()) {
            if (conferencier.getId().equals(id)) {
                return conferencier.getNom() + " " + conferencier.getPrenom();
            }
        }
        return "Conférencier non trouvé";
    }

    public StringBuilder afficherPVisitesTConferencier() {
        initialiserVisiteFiltre();
        StringBuilder str = new StringBuilder();
        // Obtenir le pourcentage de visites par conférencier
        Map<String, Double> pourcentageParConferencier = getPVisitesConferenciers();

        // Calculer le pourcentage de visites par conférenciers internes et externes
        double totalVisites = visiteFiltre.size();
        double visitesInternes = 0;
        double visitesExternes = 0;

        for (Conferencier conferencier : donnees.getConferenciers()) {
            if (conferencier.getEstEmploye()) {
                visitesInternes += pourcentageParConferencier.getOrDefault(conferencier.getId(), 0.0);
            } else {
                visitesExternes += pourcentageParConferencier.getOrDefault(conferencier.getId(), 0.0);
            }
        }

        double pourcentageInternes = visitesInternes;
        double pourcentageExternes = visitesExternes;
        str.append("Pourcentage de visites effectuées par des conférenciers internes: " + pourcentageInternes + "%\n");
        str.append("Pourcentage de visites effectuées par des conférenciers externes: " + pourcentageExternes + "%");
        return str;
    }

    public void expositionTemporaire() {
        initialiserVisiteFiltre();
        // Obtenir les ID des expositions temporaires
        ArrayList<String> idExpositionsTemporaires = new ArrayList<>();
        for (Exposition exposition : donnees.getExpositions()) {
            if (exposition.estTemporaire()) {
                idExpositionsTemporaires.add(exposition.getId());
            }
        }

        // Enlever les visites liées aux expositions temporaires
        this.visiteFiltre.removeIf(visite -> idExpositionsTemporaires.contains(visite.getExpositionId()));
    }

    public void expositionPermanente() {
        initialiserVisiteFiltre();
        // Obtenir les ID des expositions permanentes
        ArrayList<String> idExpositionsPermanantes = new ArrayList<>();
        for (Exposition exposition : donnees.getExpositions()) {
            if (!exposition.estTemporaire()) {
                idExpositionsPermanantes.add(exposition.getId());
            }
        }

        // Enlever les visites liées aux expositions permanantes
        this.visiteFiltre.removeIf(visite -> idExpositionsPermanantes.contains(visite.getExpositionId()));
    }
    
    public StringBuilder affichagePVisitesExposition() {
        StringBuilder pourcentageVisitesParExposition = new StringBuilder();
        Map<String, Double> pourcentageParExposition = getPVisitesExpositions();
        for (Map.Entry<String, Double> entry : pourcentageParExposition.entrySet()) {
            String expositionId = entry.getKey();
            double pourcentage = entry.getValue();
            pourcentage = Math.round(pourcentage);
            pourcentageVisitesParExposition.append("Exposition ")
                                           .append(expositionId)
                                           .append(": ")
                                           .append(getExpositionIntituleById(expositionId))
                                           .append(": ")
                                           .append(pourcentage)
                                           .append("% des visites\n");
        }
        return pourcentageVisitesParExposition;
    }

    public String getExpositionIntituleById(String id) {
        for (Exposition exposition : donnees.getExpositions()) {
            if (exposition.getId().equals(id)) {
                return exposition.getIntitule();
            }
        }
        return id;
    }

    public void reset() {
        this.visiteFiltre = new ArrayList<>(visiteInitial);
    }
}