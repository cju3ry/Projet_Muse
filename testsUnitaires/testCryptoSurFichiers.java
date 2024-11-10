package testsUnitaires;
import application.Crypto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class testCryptoSurFichiers {

    private Crypto crypto;
    private static final String cheminFichierEncrypte = "testsUnitaires/employesCrypte.csv";
    private static final String cheminFichierEncrypteConferencier = "testsUnitaires/conferencierCrypte.csv";
    private static final String cheminFichierEncrypteExpositions = "testsUnitaires/expositionsCrypte.csv";
    private static final String cheminFichierEncrypteVisites = "testsUnitaires/visitesCrypte.csv";

    private final String contenuEmployes = "Ident;Nom;Prenom;Telephone\n" +
            "N000001;Legendre;Hector;2614\n" +
            "N000002;Hugo;Marie;2614\n" +
            "N000003;Dujardin;Marie;2633";

    private final String contenuConferenciers = "Ident;Nom;Prenom;Specialité;Telephone;Employe;Indisponibilite;;;\n" +
            "C000001;Dupont;Pierre;#peinture, sculture#;0600000000;oui;16/10/2024;16/10/2024;;\n" +
            "C000002;Lexpert;Noemie;#peinture, impressionnisme, art contemporain#;0600000001;oui;22/10/2024;26/10/2024;;\n" +
            "C000003;Dujardin;Océane;#art moderne#;0611111111;oui;07/11/2024;07/11/2024;19/11/2024;22/11/2024\n" +
            "C000004;Durand;Bill;#photo#;0622222222;oui;;;;\n" +
            "C000005;Dupont;Max;#photo, peinture#;0633333333;oui;;;;\n" +
            "C000006;Martin;Martin;#peinture, sculture#;0606060606;non;18/10/2024;18/10/2024;;\n" +
            "C000007;Legrand;Jean-Pierre;#art moderne, peinture#;0600000002;non;18/12/2024;18/12/2024;;\n" +
            "C000008;Deneuve;Zoé;#photo, peinture#;0600000003;non;;;;";

    private final String contenuExpositions = "Ident;Intitulé;PériodeDeb;PériodeFin;nombre;motclé;résumé;Début;Fin\n" +
            "E000001;Les paysages impressionnistes;1880;1895;20;#paysage, impressionnisme, Monet, Pissaro, Sysley, Signac#;Très belle exposition mettant en évidence les approches différentes de grands peintres du mouvement impressionniste;05/11/2024;14/12/2024\n" +
            "E000002;Les œuvres majeures de Pierre Soulages;1979;2015;15;#outrenoir, lumière, abstraction, monopigmentaire, art contemporain#;Cette exposition présente des œuvres majeures représentatives de la période outrenoir de Pierre Soulages;;\n" +
            "E000003;Période bleue de Picasso;1901;1904;12;#art moderne, couleurs, bleu, mélancolie#;Tableaux significatifs de cette période, avec aussi 2 œuvres de la période rose qui suivra la période bleue;;\n" +
            "E000004;L'Inde vue par Henri Cartier-Bresson;1947;1951;32;#photo, Inde, reportage#;Très belles photos d'Henri Cartier-Bresson qui montrent l'Inde autour de 1950;10/10/2024;31/12/2024\n" +
            "E000005;Doisneau et la magazine Vogue;1948;1953;35;#photo, reportage, actualité#;Cette exposition montre les photos les plus emblématiques prises par Doisneau lorsqu'il a travaillé pour le magazine Vogue;01/10/2024;07/12/2024\n" +
            "E000006;L'impressionnisme selon Berthe Morisot;1863;1880;14;#impressionnisme, couleurs, paysages, scénes intérieur#;Magnifiques tableaux présentant aussi bien des scénes de vie familiale que des paysages;02/10/2024;05/04/2025";

    private final String contenuVisites = "Ident;Exposition;Conférencier;Employé;date;heuredebut;Intitulé;Téléphone;;\n" +
            "R000001;E000002;C000003;N000001;08/11/2024;10h00;Mme Noémie Legendre;0600000000;;\n" +
            "R000002;E000002;C000003;N000002;23/11/2024;15h00;M. Leroux association Temps Libre 12;0611111111;;\n" +
            "R000003;E000003;C000001;N000001;23/11/2024;15h00;Le club des peintres amateurs - Rodez;0600000001;;\n" +
            "R000004;E000003;C000008;N000001;23/11/2024;16h15;M. Leroux association Temps Libre 12;0611111111;;\n" +
            "R000005;E000002;C000007;N000002;08/10/2024;17h00;Mme Noémie Legendre;0600000000;;\n" +
            "R000006;E000002;C000003;N000002;08/11/2024;10h30;Agence de voyages Air Libre;0699999999;;\n" +
            "R000007;E000004;C000004;N000003;10/10/2024;15h00;Mme Dubourg Ecole Victor Hugo;0622222222;;\n" +
            "R000008;E000001;C000001;N000001;03/12/2024;10h00;Département informatique IUT;0565771080;;\n" +
            "R000009;E000004;C000004;N000002;11/10/2024;11h30;Mme Dubourg Ecole Victor Hugo;0622222222;;\n" +
            "R000010;E000004;C000004;N000002;09/10/2024;10h00;M. Leroux association Temps Libre 12;0611111111;;\n" +
            "R000011;E000005;C000005;N000001;09/10/2024;11h30;Agence de voyages Air Libre;0699999999;;\n" +
            "R000012;E000001;C000002;N000001;03/12/2024;10h10;Agence de voyages Air Libre;0699999999;;\n" +
            "R000013;E000005;C000005;N000001;10/10/2024;12h00;M. Leroux association Temps Libre 12;0611111111;;\n" +
            "R000014;E000001;C000002;N000003;05/11/2024;10h30;M. Leroux association Temps Libre 12;0611111111;;\n" +
            "R000015;E000006;C000002;N000003;10/10/2024;10h00;Mme Dubourg Ecole Victor Hugo;0622222222;;\n" +
            "R000016;E000001;C000006;N000003;03/12/2024;10h30;Le club des peintres amateurs - Rodez;0600000001;;\n" +
            "R000017;E000001;C000008;N000001;03/12/2024;11h00;Le club des peintres amateurs - Rodez;0600000001;;\n" +
            "R000018;E000006;C000002;N000001;10/10/2024;13h00;Département informatique IUT;0565771080;;";

    private final String donneesCrypteesEmployes = "\u0082\u0096\u009B§¦q\u0087¡£t\u0082¨\u009E ¥¦m\u008A\u009E\u009E\u009B©\u009A¥§\u0097@\u0087bfibfjm" +
            "\u0082\u009E\u0099\u009B§\u0096¨\u009Em~\u009E\u0095ª¨¤qkhgm<\u0084ibfibhtz« ¡q\u0086\u0093¨¢\u0097qkhgm<" +
            "\u0084ibfibitv«£\u0093¨\u009D\u009B¤t\u007F\u0097«\u009B\u009Btdlle";

    private final String donneesCrypteesConferenciers = "\u0082\u0096\u009B§¦q\u0087¡£t\u0082¨\u009E ¥¦m\u0089©\u0097\u0099¢\u0093¢¢¦\u001Ft\u0086\u009B¥" +
            "\u0097¦¡¡¤\u009Em{¦¢¢¨«\u009Bt{¤\u009D\u009B©©¡¤¢\u0094\u009F¥\u009Bª\u009Emqt<yibfibgtv«©¡¤\u00ADm\u0086¢\u0097¨«\u0097q\\¢\u009B¢ ª®¤\u009BeR©" +
            "\u009C§¢\u00AD§¨\u009EUqihfibfibfim¥®\u009Bqjhejbekbhmmgoagiahidjtm@|bfibfkm\u0082\u009Eª¦\u009E¤ªt\u0080¥\u009E\u009F\u009F\u009EmY©\u0097\u009F§¦««" +
            "\u0097bY\u009B£©¤\u009B¬¥\u009F¨ ¤¢¥£\u009E^V\u009A¤ªY\u0095¥§¦\u009B¦¢¥«\u0093\u009F§Uqihfibfibfjm¥®\u009Bqkdejbekbhmmhoagiahidjtm@|bfibflmz®" +
            "\u009C\u0097«\u0096\u009F§m\u0085\u009C\u001B\u0097§\u0097q\\\u0093¨\u00ADR£¨\u0096\u009B« \u009B\\" +
            "mfocgjcgjcgt¡«¢mfpagjahidjtbmhcghdfkfqjkejcekbhmmhkagjahidjCufibfifq}§¨\u009A \u009Att\u009F¥\u009Eq\\¢\u009E¨¦¥\\" +
            "mfodhkdhkdht¡«¢mqtm@|bfibfnmz®¢¥§¦q\u0086\u0093®tU¦¡¡ª¨^V©\u0097\u009F§¦««\u0097Ytblleileileq¨§\u009Ftmqt<yibfiblt\u007F\u0097«¦\u009F§m" +
            "\u0083\u009A¤ª¢ q\\¢\u009B¢ ª®¤\u009BeR©\u009C§¢\u00AD§¨\u009EUqihfoblihfom¤¨ qjjejbekbhmmgqagiahidjtm@|bfibfpm\u0082\u009E\u0099¨" +
            "\u009A \u009At|\u009B\u009A c\u0089\u009B\u009B«¤\u009BtU\u0097«¦V¦¡\u009A\u009E¤¤\u009E^V©\u0097\u009F§¦««" +
            "\u0097Ytblibfibfidq§¡¤tcnhchhdfkfqjjejdekbhmmqCufibfijq}\u0097¤\u009E§¬\u009Em\u0090¨\u001Bq\\¢\u009E¨¦¥eR¦" +
            "\u009E\u009B¤\u00AD§¨\u009EUqihfibfibflm¤¨ qtmq";

    private final String donneesCrypteesExpositions = "\u0082\u0096\u009B§¦q\u0082 ª¢¦«¥\u001Bq\u0089\u001B¨¢¡\u009A\u009Ev\u009B\u009Bm\u0086\"¤\u009F¨" +
            "\u0096\u009B\u007F\u009B¤t ¥¦\u0094¨\u009Em£¨¦\u0099¥\u001Bq«\u001B©®\u009F\u001Ftv\u001F\u009B§ªtx\u009F§<{ibfibgt~\u009B¬R¦\u009A«©\u009A\u0099" +
            "\u009B¬R\u009F¦¢¨\u009E¥©¢¡¤§\u009B©\u00AD\u0097©tcnqbqjjonmhimY©\u0093¯¬\u0093\u009D\u009E^V¢\u009F¦«\u0097©¬\u009B¥§ \u009F¬\u009F\u009BeR" +
            "\u0083¨ \u009B\u00AD^V\u0089\u009B©¬\u0093¨¨^V\u008C«©¥\u0097¯eR\u0089¢\u0099¤\u009A\u0095Yt\u0086¨!¥V\u009B\u0097¢¥\u0097V\u009Eª¦¨¥\u009F" +
            "\u00AD\u009B¥§R£\u009E¦ª\u009A ªY\u0097¤Y\u001B¬¢\u0096\u009B§\u0095\u009BY\u009E\u009B¬R\u0097©¢¨¨\u0095\u009E\u009E¥V\u009D\u009B\u009C\u009F" +
            "\u001B¨\u009E ª\u009E¥V\u009D\u0097V ¤\u0097§\u0096©Y¢\u009B¢ ª«\u0097©Y\u0096«Y\u009F¥®¨\u009B¦\u0097¤\u00ADR\u009F¦¢¨\u009E¥©¢¡¤§\u009B©" +
            "\u00AD\u0097qigejcekbhmmgmagkahidjCwfibfidq\u0085\u0097©Y\u0085«¯¤\u009B¬R£\u009A\u009C\u009B®¤\u009B¬R\u009A\u009ER\u0086¢\u0097¨«\u0097V\u008C¡«¥" +
            "\u0093\u009D\u009E¥qjkmrmhicktcktU¥®¦¨\u009E ¥¢¤bY\u009E«¦\u009B\u001E«\u0097bY\u0093\u0098¬¦¨\u009A\u0095ª¢¡¤eR£¨ ¥©\u009B\u009D¦\u0097¤\u00AD" +
            "\u0093\u009F«\u0097bY\u0093¨\u00ADR\u0099¨ ª\u009E\u009F¦¨¤\u0097¢ Ytu\u009B\u00AD¦\u009BY\u0097®©¡©¢¦\u009F¨ V©¤\u001F¬\u0097¤\u00AD\u0097V" +
            "\u009D\u0097©Y\u0085«¯¤\u009B¬R£\u009A\u009C\u009B®¤\u009B¬R¨\u009E¢¨\"¥\u009B§¦\u0097\u00AD\u009B¬\u009E¥V\u009D\u0097V¥\u0093V©\u001B¨¢¡\u009A" +
            "\u009ER¥®¦¨\u009E ¥¢¤V\u009D\u0097V\u0089\u009B\u009B«¤\u009BY\u0085¥®\u009E\u0097 \u0097©tm@~bfibflm\u0086\"¤\u009F¨\u0096\u009BY\u0094¢" +
            "\u009E§\u009BY\u0096\u009BY\u0082\u009F\u009C\u0093©¬¡qjkfjmgrbjtchtU\u0097«¦V¦¡\u009A\u009E¤¤\u009E^V\u009C¡«¥\u0097««¥bY\u0094¢\u009E§bY\u009F" +
            "\u001F¥\u0093¤\u009C¡¢¢\u0097Yt\u0086\u0097\u009B\u009E\u009B\u009A§®Y¥\u009F  \u009F\u009F\u009B\u0099\u009A¦\u009F\u009F¥V\u009D\u0097V" +
            "\u009C\u0097ª\u00AD\u0097V©\u001B¨¢¡\u009A\u009E^V\u009A¨\u009B\u009CR\u0097®¥©¢RhY\u0085«¯¤\u009B¬R\u009A\u009ER¢\u009AR¦\"¤\u009F¨\u0096" +
            "\u009BY¤¥¬\u0097Vª§\u009FY¥«¢¨¨\u009AR¢\u009AR¦\"¤\u009F¨\u0096\u009BY\u0094¢\u009E§\u009Btm@~bfibfmm\u0082`{¤\u009D\u0097V¯§\u009BY¢\u0097«R~" +
            "\u009E ¨¢Ry\u009A¤ª¢\u0097¨ft¨\u009E¥©¨ qjkjpmgrggtehtU¦¡¡ª¨^V\u0082 \u009A\u009E^V«\u0097¦¨¤ª\u009A\u0099\u009B\\m\u008A«\u001A©Y" +
            "\u0094\u009B¥\u009E\u009B¬R¦¡¡ª¨¥V\u009DY~\u009E ¨¢Ry\u009A¤ª¢\u0097¨ft¨\u009E¥©¨ Vª§\u009FY\u009F¥§¦¨\u009E ªY\u009E]\u0082 \u009A" +
            "\u009ER\u0097®¦¥®¤V\u009D\u0097Vjkkimgiagiahidjteghchhdfkf@~bfibfnmz¨\u009B©§\u0097\u0097®R\u009B\u00ADR¢\u009AR£\u009A\u0099\u0097³\u009B¤" +
            "\u009ER\u008C¨\u0099«\u009Emgrfntconeqlgq\\¢\u009E¨¦¥eR¨\u009E¢¥«¦\u0097 \u0097bY\u0093\u0099\u00AD§\u0097¥\u009Bª\"Uq|\u0097ª\u00AD\u0097V" +
            "\u009Eª¦¨¥\u009F\u00AD\u009B¥§R£¨ ª«\u0097V¥\u0097©Y¢\u009E¨¦¥¬R¢\u009E¥V©\u009E«¬R\u009B¦\u0094¢\"\u009F\u0097\u00AD\u009B§®\u0097©Y¢¨¢¥" +
            "\u009B¬R¦\u009A¤V}¡\u009F¬ \u009B\u009A§V¥¡¨¬£«`\u009B¢Y\u0093V\u00AD¤\u0097¯\u0093\u009F¥\u009E\u001FY¢¥®¤V¥\u0097V¦\u0093\u009D\u009A¬" +
            "\u009F§\u0097V\u008F¡\u009D®\u0097qicejbekbhmmfpagkahidjCwfibfihq\u0085Y\u009F¦¢¨\u009E¥©¢¡¤§\u009B©¦\u0097V¬\u0097¢¨ V{\u0097¨\u00AD\u009A" +
            "\u009BY\u007F¥«\u009B©¨¦qjjllmgqjftcjtU\u009F¦¢¨\u009E¥©¢¡¤§\u009B©¦\u0097bY\u0095¥®\u009E\u009B®¤©eR¦\u009A«©\u009A\u0099\u009B¬^V¬\u0095" +
            "\u001F§\u0097©Y\u009B¤\u00AD\u001B¨¢\u0097««Uq\u0086\u0093\u009D§\u009B\u009C¢£«\u009E¥V\u00AD\u0093\u0098¥\u0097\u0097®ªV©¤\u001F¬\u0097¤" +
            "\u00AD\u0093¤\u00ADR\u0097®¥©¢R\u0098¢\u0097¤Y\u0096\u009B¬R©\u009C\u001B¤\u009E¥V\u009D\u0097V¯\u009B\u009BY\u0098\u0097¦\u009B¢¢\u0093¢" +
            "\u009ER§®\u0097V\u009D\u0097©Y¢\u0097²¥\u0097 \u0097©tbhhcfhdfkfqigeifekbhn";

    private final String donneesCrypteesVisites = "\u0082\u0096\u009B§¦q~ª¦¨¥\u009F\u00AD\u009B¥§my¨ \u009C\"¤\u009B§\u0095\u009F\u009E¤q~" +
            "\u009F¦¥¡¯\"m\u009A\u009A¦\u009Bt\u009A\u009B®¤\u009B\u009D\u0097\u0098®¦q\u0082 ª¢¦«¥\u001Bq\u008D\u001B¢\"¢\u009E¨ \u009Btm@" +
            "\u008Bbfibfjm{ibfibhtufibfieq\u0087bfibfjmfqagjahidjtcf¡bft\u007F£\u009ER\u0084¨\u001B£¢\u0097V\u0085\u0097\u009D\u009E \u009A«" +
            "\u0097qihfibfibfimqC\u0084fibfidq~bfibfkmyibfibit\u0080fibfidqkeejcekbhmmgn\u009Afim\u0083gR\u0082\u009E¤¥®ªV\u009A¥©¨\u0095\u009F\u009A¦" +
            "\u009F¨ V\u008D\u0097£©¥V\u0085\u009B\u0098«\u0097VjdqihgjcgjcgjmqC\u0084fibfieq~bfibflmyibfibgt\u0080fibficqkeejcekbhmmgn\u009Afim" +
            "\u0082\u009ER\u0099¥§\u0098Y\u0096\u009B¬R¦\u009E\u009B¤\u00AD¤\u009B¬R\u0097¦\u0093ª\u009E§¨¬RcY\u0084¥\u009D\u0097°tblibfibficqt<" +
            "\u0088ibfibjtwfibfieq|bfibfqm\u0084ibfibgtdihcghdfkfqjh\u009Ejgq\u0086`V\u0085\u0097¨¨§®Y\u0093©¬¡\u0099¢\u0093ª¢¡¤Y\u0086\u009B¦¢©Y~" +
            "\u009F\u009B¤\u009BYchtbljcgjcgjcqt<\u0088ibfibktwfibfidq|bfibfpm\u0084ibfibhtbnhcfhdfkfqji\u009Eibq\u0086\u009F\u009BY\u0080¥\"\u009F" +
            "\u009F\u009ER\u0082\u009E\u0099\u009B§\u0096¨\u009Emfobfibfibftm@\u008Bbfibfom{ibfibhtufibfieq\u0087bfibfkmfqagjahidjtcf¡efts\u009D" +
            "\u009E \u0099\u009ER\u009A\u009ER¬¨«\u0097 \u0097©Ys\u009F«R\u0082¢\u0094¨\u009Emfokorkorkotm@\u008Bbfibfpm{ibfibjtufibfifq" +
            "\u0087bfibflmgiagiahidjtck¡bft\u007F£\u009ERz®\u0094¥®¤\u009DYw\u0099¨\u009E\u009BY\u0088\u009F\u009C¦¥«R~®\u0099¥tblkdhkdhkdqt<" +
            "\u0088ibfibntwfibficq|bfibfjm\u0084ibfibgtbihchhdfkfqjb\u009Eibq}\u001B¦\u009A¤ª\u009E\u009F\u009B§¦V¢ \u009C¨¤£\u009A¦\u009Fª§" +
            "\u009BY{\u008B\u008Dmfnhkpigijftm@\u008Bbfibfrm{ibfibjtufibfifq\u0087bfibfkmgjagiahidjtcg¡eft\u007F£\u009ERz®\u0094¥®¤\u009DYw\u0099¨" +
            "\u009E\u009BY\u0088\u009F\u009C¦¥«R~®\u0099¥tblkdhkdhkdqt<\u0088ibficftwfibfifq|bfibfmm\u0084ibfibhtbohcfhdfkfqjb\u009Eibq\u0086`V" +
            "\u0085\u0097¨¨§®Y\u0093©¬¡\u0099¢\u0093ª¢¡¤Y\u0086\u009B¦¢©Y~\u009F\u009B¤\u009BYchtbljcgjcgjcqt<\u0088ibficgtwfibfigq|bfibfnm" +
            "\u0084ibfibgtbohcfhdfkfqjc\u009Elbqz\u0099\u009B§\u0095\u009BY\u0096\u009BY¨¥²\u0093\u009D\u009E¥Vz\u009B¨Y~\u009F\u009B¤" +
            "\u009Btblrkorkorkqt<\u0088ibfichtwfibficq|bfibfkm\u0084ibfibgtbihchhdfkfqjb\u009Ejbqz\u0099\u009B§\u0095\u009BY\u0096\u009BY¨¥²" +
            "\u0093\u009D\u009E¥Vz\u009B¨Y~\u009F\u009B¤\u009Btblrkorkorkqt<\u0088ibficitwfibfigq|bfibfnm\u0084ibfibgtcfhcfhdfkfqjd\u009Eibq" +
            "\u0086`V\u0085\u0097¨¨§®Y\u0093©¬¡\u0099¢\u0093ª¢¡¤Y\u0086\u009B¦¢©Y~\u009F\u009B¤\u009BYchtbljcgjcgjcqt<\u0088ibficjtwfibficq|bfibfkm" +
            "\u0084ibfibitbkhcghdfkfqjb\u009Elbq\u0086`V\u0085\u0097¨¨§®Y\u0093©¬¡\u0099¢\u0093ª¢¡¤Y\u0086\u009B¦¢©Y~\u009F\u009B¤\u009BYchtbljcgjcgjcqt<" +
            "\u0088ibficktwfibfihq|bfibfkm\u0084ibfibitcfhcfhdfkfqjb\u009Eibq\u0086\u009F\u009BYv«\u009B¡««\u0099V~\u0095¥¥\u0097V\u008F\u009B\u0099" +
            "\u00AD¡¨Yz« ¡qihhkdhkdhkmqC\u0084fibfjhq~bfibfjmyibfiblt\u0080fibfieqieejdekbhmmgi\u009Aiim\u0082\u009ER\u0099¥§\u0098Y\u0096\u009B¬R¦" +
            "\u009E\u009B¤\u00AD¤\u009B¬R\u0097¦\u0093ª\u009E§¨¬RcY\u0084¥\u009D\u0097°tblibfibficqt<\u0088ibficmtwfibficq|bfibfqm\u0084ibfibgtbihchhdfkfqjc" +
            "\u009Eibq\u0085\u0097V\u009C\u009E«\u009BR\u009A\u009E¥V©\u0097\u009F§¦¨\u009E¥V\u009A\u009F\u0097\u00AD\u0097««¥VfR\u0088¨\u0096\u009B³mfobfibfibgtm@" +
            "\u008Bbfibgqm{ibfibltufibfidq\u0087bfibfjmgiagiahidjtci¡bftv\u001F©\u0093¨\u00AD\u0097£\u009E ªY\u009B¤\u009F¡¨¦\u0093ª¢£«\u009ER\u007F\u008E" +
            "\u0086qiglnimjbnimq";


    @BeforeEach
    public void setUp() throws IOException {
        crypto = new Crypto();
        crypto.setCleCommune(926);
    }

    @Test
    public void testCryptage() throws IOException {

        // Crypte le fichier Employes.csv
        String contenuEmployesCrypte = crypto.chiffrerVigenere(contenuEmployes);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierEncrypte))) {
            writer.write(contenuEmployesCrypte);
        }

        // Crypte le fichier Conferenciers.csv
        String contenuConferenciersCrypte = crypto.chiffrerVigenere(contenuConferenciers);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierEncrypteConferencier))) {
            writer.write(contenuConferenciersCrypte);
        }

        // Crypte le fichier Expositions.csv
        String contenuExpositionsCrypte = crypto.chiffrerVigenere(contenuExpositions);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierEncrypteExpositions))) {
            writer.write(contenuExpositionsCrypte);
        }

        // Crypte le fichier Visites.csv
        String contenuVisitesCrypte = crypto.chiffrerVigenere(contenuVisites);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierEncrypteVisites))) {
            writer.write(contenuVisitesCrypte);
        }


        // Lit le contenu du fichier employes crypté
        String contenuCrypte;
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichierEncrypte))) {
            contenuCrypte = reader.readLine();
        }

        // Lit le contenu du fichier conferenciers crypté
        String contenuCrypteConferenciers;
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichierEncrypteConferencier))) {
            contenuCrypteConferenciers = reader.readLine();
        }

        // Lit le contenu du fichier expositions crypté
        String contenuCrypteExpositions;
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichierEncrypteExpositions))) {
            contenuCrypteExpositions = reader.readLine();
        }

        // Lit le contenu du fichier visites crypté
        String contenuCrypteVisites;
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichierEncrypteVisites))) {
            contenuCrypteVisites = reader.readLine();
        }


        // Compare le contenu du fichier employes crypté avec le contenu attendu
        assertEquals(contenuCrypte, donneesCrypteesEmployes);

        // Compare le contenu du fichier conferenciers crypté avec le contenu attendu
        assertEquals(contenuCrypteConferenciers, donneesCrypteesConferenciers);

        // Compare le contenu du fichier expositions crypté avec le contenu attendu
        assertEquals(contenuCrypteExpositions, donneesCrypteesExpositions);

        // Compare le contenu du fichier visites crypté avec le contenu attendu
        assertEquals(contenuCrypteVisites, donneesCrypteesVisites);

    }


    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Fichier supprimé avec succès : " + filePath);
            } else {
                System.out.println("Erreur fichier non supprimé : " + filePath);
            }
        } else {
            System.out.println("Fichier non trouvé : " + filePath);
        }
    }

    @AfterAll
    public static void unSetUp() {
        deleteFile(cheminFichierEncrypte);
        deleteFile(cheminFichierEncrypteConferencier);
        deleteFile(cheminFichierEncrypteExpositions);
        deleteFile(cheminFichierEncrypteVisites);
    }
}

