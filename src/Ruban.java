import java.util.*;

public class Ruban<Character> extends LinkedList<Character> {
	private int pointeur = 0;
	Ruban() {
		super();
	}
	public void deplacement(int direction) {
		pointeur += direction;
	}
	public void ecriture(char symbole) {
		if(pointeur < 0) {
			addFirst(symbole);
			pointeur = 0;
		} else if(pointeur > this.size()) {
			addLast(symbole);
		} else {
			this.set(pointeur, symbole);
		}
	}
	public int lecture() {
		return this.get(pointeur);
	}
}
