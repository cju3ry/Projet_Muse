package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Statistiques {

    Filtre filtres;
    SimpleDateFormat format;

    DonneesApplication donnees;

    ArrayList<Visite> visiteFiltre;

    public Statistiques() {
        donnees = new DonneesApplication();
        filtres = new Filtre();
        format = new SimpleDateFormat("dd/MM/yyyy");
        donnees.importerConferenciers(donnees.LireCsv("conferencier.csv"));
        donnees.importerVisites(donnees.LireCsv("visites.csv"));
        donnees.importerExpositions(donnees.LireCsv("expositions.csv"));
        visiteFiltre = donnees.getVisites();
    }

    public void conferencierInterne() {

        ArrayList<String> idConferencier = new ArrayList<>();

        for (Conferencier conferencier : donnees.getConferenciers()) {
            if (conferencier.getEstEmploye()) {
                idConferencier.add(conferencier.getId());
            }
        }

        this.visiteFiltre.removeIf(visite -> !idConferencier.contains(visite.getConferencierId()));
    }



    public void conferencierExterne() {
        ArrayList<String> idConferencier = new ArrayList<>();

        for (Conferencier conferencier : donnees.getConferenciers()) {
            if (!conferencier.getEstEmploye()) {
                idConferencier.add(conferencier.getId());
            }
        }

        this.visiteFiltre.removeIf(visite -> !idConferencier.contains(visite.getConferencierId()));
    }

    // Méthode pour filtrer les visites pour ne conserver que celles ayant eu lieu dans une période donnée
    public void expoVisitePeriode(Date dateDebut, Date dateFin) {
        // Vérification des dates de début et de fin
        if (dateDebut.compareTo(dateFin) > 0) {
            throw new IllegalArgumentException("La date de début ne doit pas être supérieure à la date de fin.");
        }

        // Filtrer les visites par période
        this.visiteFiltre.removeIf(visite ->
                visite.getDateVisite().before(dateDebut) || visite.getDateVisite().after(dateFin)
        );
    }

    /**
     * Méthode pour obtenir le nombre de visites pour chaque exposition de la liste des visites filtrées
     * @return une map avec l'ID de l'exposition comme clé et le nombre de visites comme valeur
     */
    public Map<String, Integer> getNbVisitesParExposition() {
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
    public Map<String, Double> getPourcentageVisitesParExposition() {
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
    public Map<String, Double> getPourcentageVisitesParConferencier() {
        Map<String, Integer> visitesParConferencier = new HashMap<>();
        for (Visite visite : visiteFiltre) {
            String conferencierId = visite.getConferencierId();
            visitesParConferencier.put(conferencierId, visitesParConferencier.getOrDefault(conferencierId, 0) + 1);
        }

        Map<String, Double> pourcentageParConferencier = new HashMap<>();
        int totalVisites = visiteFiltre.size();

        for (Map.Entry<String, Integer> entry : visitesParConferencier.entrySet()) {
            double pourcentage = (entry.getValue() * 100.0) / totalVisites;
            pourcentageParConferencier.put(entry.getKey(), pourcentage);
        }

        return pourcentageParConferencier;
    }

    public void afficherPourcentageVisitesParConferencier() {
        // Obtenir le pourcentage de visites par conférencier
        Map<String, Double> pourcentageParConferencier = getPourcentageVisitesParConferencier();

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

        System.out.println("Pourcentage de visites par conférenciers internes: " + pourcentageInternes + "%");
        System.out.println("Pourcentage de visites par conférenciers externes: " + pourcentageExternes + "%");
    }

    public void enleveVisitesLiensExpoTemporaire() {
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

    public void enleveVisitesLiensExpoPermanante() {
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

    public StringBuilder affichagepourcentageVisitesParExposition() {
        StringBuilder pourcentageVisitesParExposition = new StringBuilder();
        Map<String, Double> pourcentageParExposition = getPourcentageVisitesParExposition();
        for (Map.Entry<String, Double> entry : pourcentageParExposition.entrySet()) {
            String expositionId = entry.getKey();
            double pourcentage = entry.getValue();
            pourcentageVisitesParExposition.append("Exposition ")
                                           .append(expositionId)
                                           .append(": ")
                                           .append(getExpositionIntituleById(expositionId))
                                           .append(": ")
                                           .append(pourcentage)
                                           .append("%\n");
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
    public static void main(String[] args) throws ParseException {
        Statistiques stats = new Statistiques();
        //stats.conferencierInterne();
//        stats.conferencierExterne();
//        for (Visite visite : stats.visiteFiltre) {
//            System.out.println(visite.toString());
//        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Date dateDebut = new Date();
        Date dateFin = new Date();

        dateDebut = format.parse("8/10/2024");
        dateFin = format.parse("13/10/2024");

        //stats.expoVisitePeriode(dateDebut, dateFin);

        System.out.println("taille liste " + stats.visiteFiltre.size());

        //stats.enleveVisitesLiensExpoTemporaire();
        for (Visite visite : stats.visiteFiltre) {
            System.out.println(visite.getId() + " | " + visite.getExpositionId() + " | " + visite.getConferencierId());
        }

        // affiche la map des visites par exposition
        // System.out.println(stats.getPourcentageVisitesParExposition());

        //System.out.println(stats.getPourcentageVisitesParConferencier());

        //stats.afficherPourcentageVisitesParConferencier();

        System.out.print(stats.affichagepourcentageVisitesParExposition());


    }
}