import java.util.*;
import java.lang.Math;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;

public class Ruban {
	private int pointeur;
	private ArrayList<Integer> ruban;
	private ArrayList<String> tConversion;

	public static final String separateur = " ";
	public static final int gauche = -1;
	public static final int droite = 1;

	/**
	 * Constructeur de ruban
	 * @param adresse      Adresse du fichier texte contenant les informations sur le ruban
	 * @param tableauConversion    Tableau de conversion des symboles contenus dans le ruban en entier
	 */
	public Ruban(String adresse, ArrayList<String> tableauConversion) {
		int indice;
		String texte = "";
		tConversion = tableauConversion;
		// LECTURE DU FICHIER TEXTE
		try {
			List<String> lignes = Files.readAllLines(Paths.get(adresse), Charset.defaultCharset());
			for(String ligne : lignes) {
				texte += ligne;
			}
		} catch (Exception e) {
        	e.printStackTrace();
			System.out.println("Impossible de lire le fichier contenant les informations du ruban.");
        }
		// DECOUPAGE DU TEXTE EN TABLEAU
		String[] donnees = texte.split(separateur);
		// OBTENTION DE LA POSITION DE LA TETE
		try {
			pointeur = Integer.parseInt(donnees[0]);
		}
		catch(NumberFormatException e) {
			e.printStackTrace();
			System.out.println("La premiere valeur du fichier texte contenant les informations du ruban n'est pas un entier, impossible de recuperer la position de la tete.");
		}
		// OBTENTION DES SYMBOLES DU RUBAN
		ruban = new ArrayList<Integer>(donnees.length); // initialisaton de la capacité du ruban
		for(int i = 1; i < donnees.length; i ++) {
			indice = tConversion.indexOf(donnees[i]);
			if(indice != -1) {
				ruban.add(tConversion.indexOf(donnees[i])); // ajout de l'entier correspondant au symbole
			} else {
				System.out.println("Le symbole " + (i + 1) + " du fichier texte contenant les informations du ruban n'appartient pas a l'alphabet de la machine, impossible de creer le ruban.");
			}
		}
	}
	/**
	 * Déplacer le pointeur d'une case (vers la gauche ou vers la droite)
	 * @param direction Entier indiquant dans quel sens le pointeur se déplace
	 */
	public void deplacement(int direction) {
		if(direction == gauche) {
			pointeur -= 1;
		} else if(direction == droite) {
			pointeur += 1;
		}
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
	 * Renvoyer la position de la tête et le contenu du pointeur
	 * @return String contenant la position du pointeur entre parenthèses puis les symmboles du ruban séparés par des espaces
	 */
	public String toString() {
		String symbole = "";
		String symboles = "(" + pointeur + ") ";
		for(int i = 0; i < ruban.size(); i ++) {
			try {
				symbole = tConversion.get(ruban.get(i)); // obtention du caractère à partir du tableau de conversion
			} catch(IndexOutOfBoundsException e) {
				e.printStackTrace();
				System.out.println("Probleme lors de la conversion des indices du ruban aux entiers correspondants.");
			}
			symboles += symbole + separateur;
		}
		return symboles;
	}
}
