import java.util.*;
import java.lang.Math;

public class Ruban {
	private int pointeur;
	private ArrayList<Integer> ruban;
	private ArrayList<String> tConversion;
	/**
	 * Constructeur de ruban
	 * @param texte      Chaîne de caractères contenant la position initiale du pointeur et le contenu du ruban
	 * @param separateur Caractère séparant les différentes valeurs de la variable texte
	 * @param tableau    Tableau de conversion des symboles contenus dans le ruban en entier
	 */
	Ruban(String texte, String separateur, ArrayList<String> tableau) {
		String[] donnees = texte.split(separateur);
		tConversion = tableau;
		pointeur = Integer.parseInt(donnees[0]); // la première donnée est la position initiale du pointeur
		ruban = new ArrayList<Integer>(donnees.length); // initialisaton de la capacité du ruban
		for(int i = 1; i < donnees.length; i ++) {
			 ruban.add(tConversion.indexOf(donnees[i])); // ajout de l'entier correspondant au symbole
		}
	}
	/**
	 * Déplacer le pointeur d'une case (vers la gauche ou vers la droite)
	 * @param direction Entier valant -1 pour un déplacement vers la gauche ou 1 pour un déplacement vers la droite
	 */
	public void deplacement(int direction) {
		pointeur += direction;
	}
	/**
	 * Écrire un symbole à l'emplacement actuel du pointeur
	 * @param symbole Symbole à écrire
	 */
	public void ecriture(int symbole) {
		if(pointeur > ruban.size()) {
			ruban.add(symbole);
		} else {
			// direction vaut 1 ou -1, donc au minimum pointeur vaut -1
			pointeur = Math.max(pointeur, 0);
			ruban.set(pointeur, symbole);
		}
	}
	/**
	 * Renvoyer le symbole écrit à la position actuelle du pointeur
	 * @return Symbole écrit à la position
	 */
	public int lecture() {
		return ruban.get(pointeur);
	}
	/**
	 * Renoyer la position actuelle du pointeur
	 * @return Valeur de la variable pointeur
	 */
	public int getPointeur() {
		return pointeur;
	}
	/**
	 * Renvoyer le ruban pour l'affichage
	 * @return ArrayList contenant les symboles du ruban
	 */
	public ArrayList<String> getRuban() {
		ArrayList<String> caracteres = new ArrayList<String>(ruban.size());
		String caractere;
		for(int i = 0; i < ruban.size(); i ++) {
			caractere = tConversion.get(ruban.get(i)); // obtention du caractère à partir du tableau de conversion
			caracteres.set(i, caractere);
		}
		return caracteres;
	}
}
