import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.io.*;
/**
 * Le fenetre pour la machine de Turing.
 * @author YE Zihang
 */
public class Fenetre extends JFrame{
	private JTable table;
	private JLabel status;
	private JLabel speed;
	private JTextField ruban;
	private JButton start;
	private JButton stop;
	private JButton accelerate;
	private JButton decelerate;
	private Timer timer;
	// interval d'execution de machine. En millisecond
	private int interval = 200; 
	private Turing_Machine machine;
	private String[] columnTitle = {"Current Status", "Symbol read"
			, "New Status", "Symbol written", "Action"};

	private JMenuItem newMachine = new JMenuItem("Nouvelle Machine", 'N');
	private JMenuItem newRuban = new JMenuItem("Nouveau Ruban", 'R');
	private JMenuItem readMachine = new JMenuItem("Ouvrir Machine", 'O');
	private JMenuItem readRuban = new JMenuItem("Ouvrir Ruban", 'u');
	private JMenuItem writeMachine = new JMenuItem("Enregistrer Machine", 'E');
	private JMenuItem writeRuban = new JMenuItem("Enregistrer Ruban", 'r');
	private JMenuItem about = new JMenuItem("A propos", 'A');
		
	public Fenetre(){
		super("Machine de Turing");

		// Initialization des composants
		table = new JTable(){
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		status = new JLabel("Etat: ");
		speed = new JLabel("Interval: " + interval);
		ruban = new JTextField("");
		ruban.setEditable(false);
		start = new JButton(
				new ImageIcon("../images/Play_128px.png"));
		start.setToolTipText("Play");
		stop = new JButton(
				new ImageIcon("../images/Pause_128px.png"));
		stop.setToolTipText("Pause");
		accelerate = new JButton(
				new ImageIcon("../images/Fast_forward_128px.png"));
		accelerate.setToolTipText("Accelerate");
		decelerate = new JButton(
				new ImageIcon("../images/Rewind_128px.png"));
		decelerate.setToolTipText("Decelerate");
		start.setEnabled(false);
		stop.setEnabled(false);
		
		// Layout pour le fenetre
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(1, 5));
		controlPanel.add(decelerate);
		controlPanel.add(stop);
		controlPanel.add(start);
		controlPanel.add(accelerate);
		JPanel infoPanel = new JPanel(new GridLayout(1, 2));
		infoPanel.add(speed);
		infoPanel.add(status);
		controlPanel.add(infoPanel);
		JScrollPane scrollPane = new JScrollPane(ruban);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		panel.add(controlPanel);
		panel.add(scrollPane);
		JScrollPane tablePane = new JScrollPane(table);
		this.add(panel, BorderLayout.SOUTH);
		this.add(tablePane, BorderLayout.CENTER);
		
		// Actions des buttons
		start.addActionListener( event -> {
			if(timer == null){
				timer = new Timer(
						interval, e -> {
							machine.next();
							new Update().execute();
						});
				timer.start();
			}
			else{
				timer.setDelay(interval);
				timer.start();
			}
			start.setEnabled(false);
			stop.setEnabled(true);
		});
		stop.addActionListener( event -> {
			timer.stop();
			stop.setEnabled(false);
			start.setEnabled(true);
		});
		accelerate.addActionListener( event -> {
			interval -= 100;
			if(interval >= 1000)
				decelerate.setEnabled(false);
			if(interval > 100)
				accelerate.setEnabled(true);
			if(timer != null){
				timer.setDelay(interval);
			}
			speed.setText("Interval: " + interval);
		});
		decelerate.addActionListener( event -> {
			interval += 100;
			if(interval < 1000)
				decelerate.setEnabled(true);
			if(interval <= 100)
				accelerate.setEnabled(false);
			if(timer != null){
				timer.setDelay(interval);
			}
			speed.setText("Interval: " + interval);
		});

		// Creation de Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFichier = new JMenu("Fichier");
		JMenu menuAide = new JMenu("Aide");

		menuFichier.setMnemonic('F');
		menuAide.setMnemonic('A');

		menuFichier.add(newMachine);
		menuFichier.add(newRuban);
		menuFichier.add(readMachine);
		menuFichier.add(readRuban);
		menuFichier.add(writeMachine);
		menuFichier.add(writeRuban);
		menuAide.add(about);
		menuBar.add(menuFichier);
		menuBar.add(menuAide);
		this.setJMenuBar(menuBar);

		newRuban.setEnabled(false);
		readRuban.setEnabled(false);
		writeMachine.setEnabled(false);
		writeRuban.setEnabled(false);

		// Actions des menus
		newMachine.addActionListener(event -> {
			if(timer != null && timer.isRunning()){
				timer.stop();
				stop.setEnabled(true);
			}
			try {
				new ChangeMachine().execute();
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(Fenetre.this,
						e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		});
		newRuban.addActionListener(event -> {
			if(timer != null && timer.isRunning()){
				timer.stop();
				stop.setEnabled(true);
			}
			try {
				String input = JOptionPane.showInputDialog(Fenetre.this,
						"Entrez votre ruban.\n" + 
						"Les symboles sont separer par espace.\n" + 
						"La position initiale est entoure par crochet.\n" + 
						"ex. 1 1 1 + 0 0 [1]",
						"");
				if(input == null || input.isEmpty()){
					start.setEnabled(machine != null && machine.hasRuban());
					writeRuban.setEnabled(machine != null && machine.hasRuban());
					return;
				}
				else if(!input.matches("(\\S+ )*\\S+") || !input.contains("[") || !input.contains("]")){
					throw new IllegalArgumentException("Il faut suivre les consignes");
				}
				machine.changerRuban(input);
				ruban.setText(input);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(Fenetre.this,
						e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			}
			start.setEnabled(machine != null && machine.hasRuban());
			writeRuban.setEnabled(machine != null && machine.hasRuban());
		});
		readMachine.addActionListener(event -> {
			if(timer != null && timer.isRunning()){
				timer.stop();
				stop.setEnabled(true);
			}
			try {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(Fenetre.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					machine = new Turing_Machine(chooser.getSelectedFile());
					new ChangeTable().execute();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(Fenetre.this,
						e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			}
			start.setEnabled(machine != null && machine.hasRuban());
			
			newRuban.setEnabled(machine != null);
			readRuban.setEnabled(machine != null);
			writeMachine.setEnabled(machine != null);
		});
		readRuban.addActionListener(event -> {
			if(timer != null && timer.isRunning()){
				timer.stop();
				stop.setEnabled(true);
			}
			try {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(Fenetre.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					BufferedReader reader = new BufferedReader(new FileReader(chooser.getSelectedFile()));
					String s = reader.readLine();
					machine.changerRuban(s);
					ruban.setText(s);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(Fenetre.this,
						e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			}
			start.setEnabled(machine != null && machine.hasRuban());

			writeRuban.setEnabled(machine != null && machine.hasRuban());
		});
		writeMachine.addActionListener(event -> {
			if(timer != null && timer.isRunning()){
				timer.stop();
				stop.setEnabled(true);
			}
			try {
					timer.stop();
					stop.setEnabled(true);
					start.setEnabled(true);
					JFileChooser chooser = new JFileChooser();
					int returnVal = chooser.showSaveDialog(Fenetre.this);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						machine.enregistrerMachine(chooser.getSelectedFile().toPath());
					}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(Fenetre.this,
						e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			}
			start.setEnabled(machine != null && machine.hasRuban());
		});
		writeRuban.addActionListener(event -> {
			if(timer != null && timer.isRunning()) {
				timer.stop();
				stop.setEnabled(true);
			}
			JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showSaveDialog(Fenetre.this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				try (PrintWriter writer = new PrintWriter(
						new FileWriter(chooser.getSelectedFile()))){
					writer.println(machine.afficherRuban());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(Fenetre.this, 
							e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
			start.setEnabled(machine != null && machine.hasRuban());
		});
		about.addActionListener(event -> {
			JOptionPane.showMessageDialog(Fenetre.this, 
					"Machine de Turing\nMini Projet de cours INFO\n" + 
					"Cree par Zihang YE, Teck Wong, Gabriel WANTZ, " +
					"Corentin VANNIER",
					"A propos", JOptionPane.INFORMATION_MESSAGE);
		});

		// Parametre de Fenetre
		this.setSize(1000, 700);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String args[]){
		new Fenetre();
	}

	private class Update extends SwingWorker<String, Object>{
		private int offset = 0;
		@Override
		public String doInBackground(){
			offset = ruban.getScrollOffset();
			String result = machine.afficherRuban();
			return result;
		}
		@Override
		public void done(){
			try {
				ruban.setText(get());
				ruban.setScrollOffset(offset);
				status.setText("Etat: " + machine.getEtat());
				if(machine.getEtat().equals("Etat finale")){
					timer.stop();
					start.setEnabled(false);
					stop.setEnabled(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private class ChangeTable extends SwingWorker<DefaultTableModel, Object>{
		@Override
		public DefaultTableModel doInBackground(){
			List<String> symbols = machine.getTCSymboles();
			List<String> status = machine.getTCEtats();
			final int move_Left = Ruban.GAUCHE;
			final int stay = Ruban.RESTER;
			final int move_Right = Ruban.DROITE;
			int[][][] tableAction = machine.getTableau();
			Object[][] excel = new Object[symbols.size() * status.size()][5];
			// tableAction premier indice: etat, deuxieme indice: symbol
			// TODO pensez au etat/symbol -1
			for(int i = 0; i < tableAction.length; i++){
				for(int j = 0; j < tableAction[i].length; j++){
					int[] translation = tableAction[i][j];
					excel[i * status.size() + j][0] = status.get(i);
					excel[i * status.size() + j][1] = symbols.get(j);
					excel[i * status.size() + j][2] = translation[1] != -1 ? status.get(translation[1]) : "Etat Finale";
					excel[i * status.size() + j][3] = symbols.get(translation[0]);
					switch(translation[2]){
						case move_Left:
							excel[i * status.size() + j][4] = "Deplacer vers gauche";
							break;
						case move_Right:
							excel[i * status.size() + j][4] = "Deplacer vers droite";
							break;
						case stay:
							excel[i * status.size() + j][4] = "Ne pas bouger";
							break;
					}
				}
			}
			return new DefaultTableModel(excel, columnTitle);
		}
		@Override
		public void done(){
			try {
				table.setModel(get());
				ruban.setText("");
				status.setText("Etat: " + machine.getEtat());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private class ChangeMachine extends SwingWorker<Void, Object>{
		private Turing_Machine turingMachine = null;
		@Override
		public Void doInBackground(){
			try {
				turingMachine = FenetreCreation.initialiseMachine();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(Fenetre.this,
						e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			}
			return null;
		}
		@Override
		public void done(){
			if(turingMachine != null) {
				machine = turingMachine;
				new ChangeTable().execute();
			}
			start.setEnabled(machine != null && machine.hasRuban());
			
			newRuban.setEnabled(machine != null);
			readRuban.setEnabled(machine != null);
			writeMachine.setEnabled(machine != null);
		}
	}
}
