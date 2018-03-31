import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

public class Turing_Machine {

	private final int LIGNE_ETATS = 0;
	private final int LIGNE_SYMBOLES = 1;
	private final int LIGNE_TABLEAU = 2;
	private final int DEPLACEMENT_GAUCHE = -1;
	private final int NE_PAS_BOUGER = 0;
	private final int DEPLACEMENT_DROITE = 1;
	private final int NOUVEAU_SYMBOLE = 0;
	private final int NOUVEAU_DEPLACEMENT = 1;
	private final int NOUVEL_ETAT = 2;
	

	private Ruban ruban;
	private String rubanOriginal = "";
	private ArrayList<String> tableauConversionEtats;
	private ArrayList<String> tableauConversionSymboles;

	private int[][][] tableauAction;
	private int etat = 0;
	/**
	 * Constructeur de la machine à partir de chaque élément
	 * @param tableauAct         Le tableau d'action normalisé
	 * @param tableauConvEtat    L'alphabet des états, ou chaque String est associé à son indice
	 * @param tableauConvSymbole L'alphabet des symboles, ou chaque String est associé à son indice
	 */
	public Turing_Machine(int[][][] tableauAct, ArrayList<String> tableauConvEtat, ArrayList<String> tableauConvSymbole){
		tableauConversionEtats = tableauConvEtat;
		tableauConversionSymboles = tableauConvSymbole;
		tableauAction = tableauAct;
	}
	/**
	 * Constructeur de la machine à partir d'un fichier texte
	 * @param adresse Le fichier dans lequel est stocké la machine de Turing
	 */
	public Turing_Machine(File adresse){
		try{
			List<String> lignes = Files.readAllLines(adresse.toPath(), Charset.defaultCharset());

			String ligneSelectionnee;
			ligneSelectionnee = lignes.get(LIGNE_ETATS);
			if(!ligneSelectionnee.matches("(\\S+ )?\\S"))
				throw new IllegalArgumentException("Tableau des etats n'est pas bon");
			tableauConversionEtats = new ArrayList<String>(Arrays.asList(ligneSelectionnee.split(" ")));
			ligneSelectionnee = lignes.get(LIGNE_SYMBOLES);
			if(!ligneSelectionnee.matches("(\\S+ )?\\S"))
				throw new IllegalArgumentException("Tableau des symboles n'est pas bon");
			tableauConversionSymboles = new ArrayList<String>(Arrays.asList(ligneSelectionnee.split(" ")));
			ligneSelectionnee = lignes.get(LIGNE_TABLEAU);
			if(!ligneSelectionnee.matches("((((((-?\\d)+:)?-?\\d),)?(((-?\\d)+:)?-?\\d))/)?(((((-?\\d)+:)?-?\\d),)?(((-?\\d)+:)?-?\\d))"))
				throw new IllegalArgumentException("Tableau des actions n'est pas bon");

			//Construction du tableau

			tableauAction = new int[tableauConversionEtats.size()][tableauConversionSymboles.size()][3];

			String[] etats = ligneSelectionnee.split("/");
			String[] symboles;
			String[] actions;
			for(int i=0;i<etats.length;i++){
				symboles = etats[i].split(",");
				for(int j=0;j<symboles.length;j++){
					actions = symboles[j].split(":");
					for(int k=0;k<3;k++){
						tableauAction[i][j][k] = Integer.parseInt(actions[k]);
						
					}
				}
			}
		}
		catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
        	}
	}
	
	public static void main(String[] args){
		Turing_Machine machine = new Turing_Machine(new File("../exemplesTA/Turing_Machine_Add"));
	}
	
	/**
	 * Avance d'une action dans la machine de Turing
	 */
	public void next(){

		int symbole = ruban.lecture();
		int[] actions = tableauAction[etat][symbole];

		ruban.ecriture(actions[NOUVEAU_SYMBOLE]);
		ruban.deplacement(actions[NOUVEAU_DEPLACEMENT]);
		this.etat = actions[NOUVEL_ETAT];

	}
	
	/**
	 * Crée un nouveau ruban
	 * @param  la chaîne de caractère contenant les symboles du ruban
	 */
	public void changerRuban(String ruban){
		this.ruban = new Ruban(ruban,tableauConversionSymboles);
	}
	
	public String afficherRuban(){
		return ruban.toString();
	}

	public void enregistrerMachine(Path adresse) throws IOException{
		try (PrintWriter fichier = new PrintWriter(adresse.toFile())) {
			String texte = "";
			for(String unEtat : tableauConversionEtats){
				texte+= unEtat + " ";
			}
			texte+= "\n";
			for(String unSymbole : tableauConversionSymboles){
				texte+= unSymbole + " ";
			}
			texte+= "\n";
			
			for(int i=0;i<tableauAction.length;i++){
				for(int j=0;j<tableauAction[i].length;j++){
					for(int k=0;k<3;k++){
						texte+=tableauAction[i][j][k]+":";
					}
					texte += ",";
				}
				texte += "/";
			}
			
			fichier.print(texte);
		}
	}

	public String getEtat(){
		return tableauConversionEtats.get(etat);
	}
	
	public int[][][] getTableau(){
		return tableauAction;
	}
	public List<String> getTCEtats(){
		return Collections.unmodifiableList(tableauConversionEtats);
	}
	public List<String> getTCSymboles(){
		return Collections.unmodifiableList(tableauConversionSymboles);
	}

}
