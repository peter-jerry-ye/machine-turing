import java.util.*;

public class Ruban {
	private int pointeur = 0;
	private LinkedList<Character> r;
	Ruban() {
		super();
	}
	public void deplacement(int direction) {
		pointeur += direction;
	}
	public void ecriture(char symbole) {
		if(pointeur < 0) {
			r.addFirst(symbole);
			pointeur = 0;
		} else if(pointeur > r.size()) {
			r.addLast(symbole);
		} else {
			r.set(pointeur, symbole);
		}
	}
	public char lecture() {
		return r.get(pointeur);
	}
}
