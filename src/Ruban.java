import java.util.*;
import java.lang.Math;

public class Ruban {
	private int pointeur;
	private ArrayList<Integer> ruban;
	private ArrayList<String> tConversion;
	Ruban(String texte, String separateur, ArrayList<String> tableau) {
		String[] donnees = texte.split(separateur);
		tConversion = tableau;
		pointeur = Integer.parseInt(donnees[0]); // la premiere donnee est la position initiale du pointeur
		ruban = new ArrayList<Integer>(donnees.length); // initialisaton de la capacite du ruban
		for(int i = 1; i < donnees.length; i ++) {
			 ruban.add(tConversion.indexOf(donnees[i]));
		}
	}
	public void deplacement(int direction) {
		pointeur += direction;
	}
	public void ecriture(int symbole) {
		if(pointeur > ruban.size()) {
			ruban.add(symbole);
		} else {
			pointeur = Math.max(pointeur, 0);
			ruban.set(pointeur, symbole);
		}
	}
	public int lecture() {
		return ruban.get(pointeur);
	}
	public int getPointeur() {
		return pointeur;
	}
	public ArrayList<String> getRuban() {
		ArrayList<String> caracteres = new ArrayList<String>(ruban.size());
		String caractere;
		for(int i = 0; i < ruban.size(); i ++) {
			caractere = tConversion.get(ruban.get(i));
			caracteres.set(i, caractere);
		}
		return caracteres;
	}
}
