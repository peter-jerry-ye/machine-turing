import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;

public class Turing_Machine {

	private final int LIGNE_ETATS = 0;
	private final int LIGNE_SYMBOLES = 1;
	private final int LIGNE_TABLEAU = 2;

	private Ruban ruban;
	private String rubanOriginal = "";
	private ArrayList<String> tableauConversionEtats;
	private ArrayList<String> tableauConversionSymboles;

	private int[][][] tableauAction;
	private int etat = 0;
	/**
	 * [Turing_Machine description]
	 * @param tableauAct         [description]
	 * @param tableauConvEtat    [description]
	 * @param tableauConvSymbole [description]
	 */
	public Turing_Machine(int[][][] tableauAct, ArrayList<String> tableauConvEtat, ArrayList<String> tableauConvSymbole){
		tableauConversionEtats = tableauConvEtat;
		tableauConversionSymboles = tableauConvSymbole;
		tableauAction = tableauAct;
	}
	/**
	 * [Turing_Machine description]
	 * @param adresse [description]
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
						// TODO: remove sysout
						System.out.print(tableauAction[i][j][k]+" ");
					}
					System.out.print(" | ");
				}
				System.out.println();
			}
		}
		catch (IOException e) {
			throw e;
        	}
	}
	/**
	 * [main description]
	 * @param args [description]
	 */
	public static void main(String[] args){
		Turing_Machine machine = new Turing_Machine(new File("../examplesTA/Turing_Machine_Add"));
	}
	/**
	 * [next description]
	 */
	public void next(){

		int symbole = ruban.lecture();
		int[] actions = tableauAction[etat][symbole];

		ruban.ecriture(actions[0]);
		ruban.deplacement(actions[1]);
		this.etat = actions[2];

	}
	/**
	 * [changerRuban description]
	 * @param  ruban [description]
	 */
	public void changerRuban(String ruban){
		this.ruban = new Ruban(ruban,tableauConversionSymboles);
	}
	public String afficherRuban(){
		return ruban.toString();
	}

	public void enregistrerMachine(Path adresse) throws IOException{
		//TODO:
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
