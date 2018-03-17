import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Le fenetre pour la machine de Turing.
 * @author YE Zihang
 */
public class Fenetre extends JFrame{
	private JTable table;
	private JLabel status;
	private JPanel ruban;
	private JButton start;
	private JButton stop;
	private JButton accelerate;
	private JButton decelerate;
	private Timer timer;
	// interval d'execution de machine. En millisecond
	private int interval; 

	public Fenetre(){
		super("Machine de Turing");

		// Initialization des composants
		table = new JTable();
		status = new JLabel();
		ruban = new JPanel();
		start = new JButton();
		stop = new JButton();
		accelerate = new JButton();
		decelerate = new JButton();
		
		// Layout pour le fenetre
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(1, 5));
		controlPanel.add(decelerate);
		controlPanel.add(stop);
		controlPanel.add(start);
		controlPanel.add(accelerate);
		controlPanel.add(status);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		panel.add(controlPanel);
		panel.add(ruban);
		this.add(panel, BorderLayout.SOUTH);
		this.add(table, BorderLayout.CENTER);
		
		// Actions des buttons

		// Creation de Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFichier = new JMenu("Fichier");
		JMenu menuAide = new JMenu("Aide");
		JMenuItem newMachine = new JMenuItem("Nouvelle Machine");
		JMenuItem newRuban = new JMenuItem("Nouveau Ruban");
		JMenuItem readMachine = new JMenuItem("Ouvrir");
		JMenuItem writeMachine = new JMenuItem("Enregistrer Machine");
		JMenuItem writeRuban = new JMenuItem("Enregistrer Ruban");
		JMenuItem about = new JMenuItem("A propos");

		menuFichier.setMnemonic('F');
		menuAide.setMnemonic('A');
		newMachine.setMnemonic('N');
		newRuban.setMnemonic('R');
		readMachine.setMnemonic('O');
		writeMachine.setMnemonic('E');
		writeRuban.setMnemonic('g');
		about.setMnemonic('A');

		menuFichier.add(newMachine);
		menuFichier.add(newRuban);
		menuFichier.add(readMachine);
		menuFichier.add(writeMachine);
		menuFichier.add(writeRuban);
		menuAide.add(about);
		menuBar.add(menuFichier);
		menuBar.add(menuAide);
		this.setJMenuBar(menuBar);

		// Actions des menus


		// Parametre de Fenetre
		this.setSize(1000, 700);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String args[]){
		new Fenetre();
	}
}
