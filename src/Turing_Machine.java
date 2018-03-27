import java.util.*;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class Turing_Machine {
	
	private final int LIGNE_ETATS = 0;
	private final int LIGNE_SYMBOLES = 1;
	private final int LIGNE_TABLEAU = 2;
	
	private Ruban ruban;
	private String rubanOriginal = "";
	private ArrayList<String> tableauConversionEtats;
	private ArrayList<String> tableauConversionSymboles;
	
	private int[][][] tableauAction;
	private int etat=0;
	
	Turing_Machine(int[][][] tableauAct, ArrayList<String> tableauConvEtat, ArrayList<String> tableauConvSymbole){
		tableauConversionEtats = tableauConvEtat;
		tableauConversionSymboles = tableauConvSymbole;
		tableauAction = tableauAct;
		}
		
	Turing_Machine(File adresse){
		try{
			List<String> lignes = Files.readAllLines(/*Paths.get(adresse)*/, Charset.defaultCharset());
			
			String ligneSelectionnee;
			ligneSelectionnee = lignes.get(LIGNE_ETATS);
			tableauConversionEtats = new ArrayList<String>(Arrays.asList(ligneSelectionnee.split(" ")));
			ligneSelectionnee = lignes.get(LIGNE_SYMBOLES);
			tableauConversionSymboles = new ArrayList<String>(Arrays.asList(ligneSelectionnee.split(" ")));
			ligneSelectionnee = lignes.get(LIGNE_TABLEAU);
			
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
						System.out.print(tableauAction[i][j][k]+" ");
					}
					System.out.print(" | ");
				}
				System.out.println();
			}
			
			
		}
		catch (Exception e) {
         e.printStackTrace();
        }
		String separateur=" ";
		
		}
	
	
	public static void main(String[] args){
	
		int[][][] tableauAction = {
			{{1,-1,0},{0,-1,1},{2,0,0}}, 
			{{0,-1,1},{1,-1,1},{2,-1,2}},
			{{1,1,3},{0,-1,2},{1,1,3}},
			{{0,1,3},{1,1,3},{2,1,4}},
			{{0,1,4},{1,1,5},{2,0,-1}},
			{{0,1,5},{1,1,5},{2,-1,0}}};
		LinkedList<Integer> ruban = new LinkedList<Integer>(Arrays.asList(2,0,1,0,1,2,0,1,1,2,2));
		System.out.println(ruban);
		int etat=0;
		int curseur=8;
		
		while(etat!=-1){
			int symbole = ruban.get(curseur);
			int[] actions = tableauAction[etat][symbole];
		
			ruban.set(curseur,actions[0]);
			curseur+=actions[1];
			if(curseur>ruban.size()){break;}
			etat=actions[2];
			System.out.println(ruban + "  " + etat);
		}
	}
	
	public void next(){
		
		int symbole = ruban.lecture();
		int[] actions = tableauAction[etat][symbole];
		
		ruban.ecriture(actions[0]);
		ruban.deplacement(actions[1]);
		this.etat = actions[2];
		
	}
	
	public String changerRuban(String ruban){
		ruban = new Ruban(ruban,tableauConversionSymboles);
		}
	
/*	public String lireRuban(){
		return ruban.getRuban();
		}*/
	
	//public enregistrerMachine(Path adresse)
	
	public String lireEtat(){return tableauConversionEtats.get(etat);}
	
	public int[][][] lireTableau(){return tableauAction;}
	
}

