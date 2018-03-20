import java.util.*;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class Turing_Machine {
	
	private Ruban ruban;
	private String rubanOriginal = "";
	private ArrayList<String> tableauConversion;
	
	private int[][][] tableauAction;
	private int etat=0;
	
	Turing_Machine(int[][][] tableauAct, ArrayList<String> tableauConv){
		tableauConversion = tableauConv;
		tableauAction = tableauAct;
		}
	
	
	public static void main(String[] args){
	
		int[][][] tableauAction = {
			{{0,0,-1},{0,1,0},{2,1,1}}, //pour l'état 1 ; {},{},{o} le symbole d'indice 2 ; {2 écrit le symbole d'indice 2, 1 déplace toi à droite,1 passe dans l'état 1}
			{{1,1,1},{1,1,1},{0,-1,2}},
			{{0,0,1},{1,-1,2},{0,1,0}}};
		LinkedList<Integer> ruban = new LinkedList<Integer>(Arrays.asList(2,1,1,1,0,1,0,0,1,2));
		System.out.println(ruban);
		int etat=0;
		int curseur=0;
		
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
	
	public void changerRuban(String addresse){
		try{
			List<String> lignes = Files.readAllLines(Paths.get(addresse), Charset.defaultCharset());			
			for (String ligne : lignes){
				rubanOriginal += ligne ;
			}
		}
		catch (Exception e) {
         e.printStackTrace();
        }
		String separateur=" ";
		ruban = new Ruban(rubanOriginal,separateur);
		}
	
	public ArrayList<String> lireRuban(){
		return ruban.getRuban();
		}
	
	public int lireEtat(){return etat;}
	
	public int[][][] lireTableau(){return tableauAction;}
	
}

