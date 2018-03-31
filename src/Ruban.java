import java.util.*;
import java.lang.Math;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;

public class Ruban {
	private int pointeur;
	private ArrayList<Integer> ruban;
	private ArrayList<String> tConversion;

	public static final String SEPARATEUR = " ";
	public static final int GAUCHE = -1;
	public static final int DROITE = 1;
	public static final int RESTER = 0;

	/**
	 * Constructeur de ruban
	 * @param adresse      Texte contenant les informations sur le ruban
	 * @param tableauConversion    Tableau de conversion des symboles contenus dans le ruban en entier
	 */
	public Ruban(String texte, ArrayList<String> tableauConversion) {
		tConversion = tableauConversion;
		if(!texte.matches("(\\S+ )*\\S+") || !texte.contains("[") || !texte.contains("]"))
			throw new IllegalArgumentException("Le ruban n'a pas de bon format");
		String[] donnees = texte.split(SEPARATEUR);

		// OBTENTION DES SYMBOLES DU RUBAN
		// TODO: remove try catch
		int indice;
		ruban = new ArrayList<Integer>(); // initialisaton de la capacité du ruban
		for(int i = 0; i < donnees.length; i ++) {
			if(donnees[i].matches("\\[\\S+\\]")){
				indice = tConversion.indexOf(donnees[i].substring(1, donnees[i].length() - 1));
				if(indice != -1) {
					ruban.add(indice);
					pointeur = ruban.size() - 1;
					continue;
				}
				else {
					throw new IllegalArgumentException("Le symbole " + donnees[i] + " n'est pas un symboles de cette machine");
				}
			}
			else {
				indice = tConversion.indexOf(donnees[i]);
				if(indice != -1) {
					ruban.add(indice);// ajout de l'entier correspondant au symbole
				} else {				
					throw new IllegalArgumentException("Le symbole " + donnees[i] + " n'est pas un symboles de cette machine");
				}
			}
		}
	}
	/**
	 * Déplacer le pointeur d'une case (vers la gauche ou vers la droite)
	 * @param direction Entier indiquant dans quel sens le pointeur se déplace
	 */
	public void deplacement(int direction) {
		if(direction == GAUCHE) {
			pointeur -= 1;
		} else if(direction == DROITE) {
			pointeur += 1;
		}
		if(pointeur < 0 || pointeur >= ruban.size()) {
			ecriture(0);
		}

	}
	/**
	 * Écrire un symbole à l'emplacement actuel du pointeur
	 * @param symbole Symbole à écrire
	 */
	public void ecriture(int symbole) {
		if(pointeur >= ruban.size()) {
			ruban.add(symbole);
		} 
		else if(pointeur < 0) {
			pointeur = 0;
			ruban.add(pointeur, symbole);
		}
		else {
			// direction vaut 1 ou -1, donc au minimum pointeur vaut -1
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
	 * Renvoyer le ruban sous la forme de String
	 * @return String qui répresent cette ruban
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < ruban.size(); i++){
			if(i != pointeur) {
				builder.append(tConversion.get(ruban.get(i)) + " ");
			}
			else {
				builder.append("[" + tConversion.get(ruban.get(i)) + "] ");
			}
		}
		builder.deleteCharAt(builder.length() - 1); // enleve l'espace
		return builder.toString();
	}
}
