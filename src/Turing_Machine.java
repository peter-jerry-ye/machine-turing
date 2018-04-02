import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * La machine de turing
 *
 * @author WANTZ Gabriel
 */
public class Turing_Machine {

    private final int LIGNE_ETATS = 0;
    private final int LIGNE_SYMBOLES = 1;
    private final int LIGNE_TABLEAU = 2;

    private final int DEPLACEMENT_GAUCHE = -1;
    private final int NE_PAS_BOUGER = 0;
    private final int DEPLACEMENT_DROITE = 1;

    private final int NOUVEAU_SYMBOLE = 0;
    private final int NOUVEL_ETAT = 1;
    private final int NOUVEAU_DEPLACEMENT = 2;

    private final int ETAT_FINAL = -1;


    private Ruban ruban;
    private String rubanOriginal = "";
    private ArrayList<String> tableauConversionEtats;
    private ArrayList<String> tableauConversionSymboles;

    private int[][][] tableauAction;
    private int etat = 0;

    /**
     * Constructeur de la machine à partir de chaque élément
     *
     * @param tableauAct         Le tableau d'action normalisé
     * @param tableauConvEtat    L'alphabet des états, ou chaque String est associé à son indice
     * @param tableauConvSymbole L'alphabet des symboles, ou chaque String est associé à son indice
     */
    public Turing_Machine(int[][][] tableauAct, ArrayList<String> tableauConvEtat, ArrayList<String> tableauConvSymbole) {
        tableauConversionEtats = tableauConvEtat;
        tableauConversionSymboles = tableauConvSymbole;
        tableauAction = tableauAct;
    }

    /**
     * Constructeur de la machine à partir d'un fichier texte
     *
     * @param adresse Le fichier dans lequel est stocké la machine de Turing
     */
    public Turing_Machine(File adresse) {
        try {
            List<String> lignes = Files.readAllLines(adresse.toPath(), Charset.defaultCharset());

            String ligneSelectionnee;
            ligneSelectionnee = lignes.get(LIGNE_ETATS);
            if (!ligneSelectionnee.matches("(\\S+ )*\\S+"))
                throw new IllegalArgumentException("Tableau des etats n'est pas bon");
            tableauConversionEtats = new ArrayList<String>(Arrays.asList(ligneSelectionnee.split(" ")));
            ligneSelectionnee = lignes.get(LIGNE_SYMBOLES);
            if (!ligneSelectionnee.matches("(\\S+ )*\\S+"))
                throw new IllegalArgumentException("Tableau des symboles n'est pas bon");
            tableauConversionSymboles = new ArrayList<String>(Arrays.asList(ligneSelectionnee.split(" ")));
            ligneSelectionnee = lignes.get(LIGNE_TABLEAU);
            if (!ligneSelectionnee.matches("((((((-?\\d)+:)*-?\\d+),)*(((-?\\d)+:)*-?\\d+))/)*(((((-?\\d)+:)*-?\\d+),)*(((-?\\d)+:)*-?\\d+))"))
                throw new IllegalArgumentException("Tableau des actions n'est pas bon");

            //Construction du tableau

            tableauAction = new int[tableauConversionEtats.size()][tableauConversionSymboles.size()][3];

            String[] etats = ligneSelectionnee.split("/");
            String[] symboles;
            String[] actions;
            for (int i = 0; i < etats.length; i++) {
                symboles = etats[i].split(",");
                for (int j = 0; j < symboles.length; j++) {
                    actions = symboles[j].split(":");
                    for (int k = 0; k < 3; k++) {
                        tableauAction[i][j][k] = Integer.parseInt(actions[k]);

                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Turing_Machine machine = new Turing_Machine(new File("../exemplesTA/Turing_Machine_Add"));
    }

    /**
     * Avance d'une action dans la machine de Turing
     */
    public void next() {

        if (etat == ETAT_FINAL) return;

        int symbole = ruban.lecture();
        int[] actions = tableauAction[etat][symbole];

        ruban.ecriture(actions[NOUVEAU_SYMBOLE]);
        ruban.deplacement(actions[NOUVEAU_DEPLACEMENT]);
        this.etat = actions[NOUVEL_ETAT];

    }

    /**
     * Crée un nouveau ruban
     *
     * @param ruban la chaîne de caractère contenant les symboles du ruban
     */
    public void changerRuban(String ruban) {
        this.ruban = new Ruban(ruban, tableauConversionSymboles);
        this.etat = 0;
    }

    /**
     * Est-ce qu'il y a de ruban
     *
     * @return true si il y a, false si il n'y a pas
     */
    public boolean hasRuban() {
        return this.ruban != null;
    }

    /**
     * Afficher le ruban avec un String
     *
     * @return le String qui represente le Ruban
     */
    public String afficherRuban() {
        return ruban.toString();
    }

    /**
     * Enregistrer la machine sous l'adresse donne
     *
     * @param adresse l'adresse ou stocker la machine
     * @throws IOException si il y a des probleme d'ecrire
     */
    public void enregistrerMachine(Path adresse) throws IOException {
        try (PrintWriter fichier = new PrintWriter(adresse.toFile())) {
            StringBuilder texte = new StringBuilder();
            for (String unEtat : tableauConversionEtats) {
                texte.append(unEtat + " ");
            }
            texte.deleteCharAt(texte.length() - 1);
            fichier.println(texte);
            texte.delete(0, texte.length());
            for (String unSymbole : tableauConversionSymboles) {
                texte.append(unSymbole + " ");
            }
            texte.deleteCharAt(texte.length() - 1);
            fichier.println(texte);
            texte.delete(0, texte.length());

            for (int i = 0; i < tableauAction.length; i++) {
                for (int j = 0; j < tableauAction[i].length; j++) {
                    for (int k = 0; k < 3; k++) {
                        texte.append(tableauAction[i][j][k] + ":");
                    }
                    texte.deleteCharAt(texte.length() - 1);
                    texte.append(",");
                }
                texte.deleteCharAt(texte.length() - 1);
                texte.append("/");
            }
            texte.deleteCharAt(texte.length() - 1);
            fichier.print(texte);
        }
    }

    /**
     * Renvoyer l'etat de machine
     *
     * @return l'etat de machine
     */
    public String getEtat() {
        return etat == ETAT_FINAL ? "Etat final" : tableauConversionEtats.get(etat);
    }

    /**
     * Renvoyer le tableau d'action
     *
     * @return le tableau d'action
     */
    public int[][][] getTableau() {
        int[][][] tableau = new int[tableauAction.length][tableauAction[0].length][3];
        for (int i = 0; i < tableauAction.length; i++) {
            for (int j = 0; j < tableauAction[i].length; j++) {
                for (int k = 0; k < 3; k++) {
                    tableau[i][j][k] = tableauAction[i][j][k];
                }
            }
        }
        return tableau;
    }

    /**
     * Renvoyer la liste des etats
     *
     * @return la liste des etats
     */
    public List<String> getTCEtats() {
        return Collections.unmodifiableList(tableauConversionEtats);
    }

    /**
     * Renvoyer la liste des symboles
     *
     * @return la liste des symboles
     */
    public List<String> getTCSymboles() {
        return Collections.unmodifiableList(tableauConversionSymboles);
    }

}
