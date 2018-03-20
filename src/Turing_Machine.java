import java.util.*;

public class Turing_Machine {
	
	private LinkedList<Integer> ruban;
	
	private int[][][] tableauAction;
	private int etat;
	
	Turing_Machine(int[][][] tableauAct , Ruban rub){}
	
	Turing_Machine(int[][][] tableauAct){}
	
	public static void main(String[] args){
	
		int[][][] tableauAction = {
			{{0,0,-1},{0,1,0},{2,1,1}},
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
		
		ruban.write(actions[0]);
		ruban.move(actions[1]);
		this.etat = actions[2];
		
	}
	
	public void changerRuban(Ruban rub){}
	
	public void lireRuban(int start, int end){}
	
	public int lireEtat(){return etat;}
	
	//public int[][][] lireTableau(){}
	
}

