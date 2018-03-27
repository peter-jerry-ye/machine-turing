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

	public Fenetre(){
		super("Machine de Turing");

		// Initialization des composants
		table = new JTable(){
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		status = new JLabel();
		speed = new JLabel("Vitesse: " + interval);
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
		accelerate.setEnabled(false);
		decelerate.setEnabled(false);
		
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
		this.add(panel, BorderLayout.SOUTH);
		this.add(table, BorderLayout.CENTER);
		
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
					interval += 100;
					if(interval > 1000)
						accelerate.setEnabled(false);
					if(interval > 100)
						decelerate.setEnabled(true);
					if(timer != null){
						timer.setDelay(interval);
					}
					speed.setText("Vitesse: " + interval);
				});
		decelerate.addActionListener( event -> {
					interval -= 100;
					if(interval < 1100)
						accelerate.setEnabled(true);
					if(interval < 200)
						decelerate.setEnabled(false);
					if(timer != null){
						timer.setDelay(interval);
					}
					speed.setText("Vitesse: " + interval);
				});

		// Creation de Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFichier = new JMenu("Fichier");
		JMenu menuAide = new JMenu("Aide");
		JMenuItem newMachine = new JMenuItem("Nouvelle Machine", 'N');
		JMenuItem newRuban = new JMenuItem("Nouveau Ruban", 'R');
		JMenuItem readMachine = new JMenuItem("Ouvrir Machine", 'O');
		JMenuItem readRuban = new JMenuItem("Ouvrir Ruban", 'u');
		JMenuItem writeMachine = new JMenuItem("Enregistrer Machine", 'E');
		JMenuItem writeRuban = new JMenuItem("Enregistrer Ruban", 'r');
		JMenuItem about = new JMenuItem("A propos", 'A');

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

		// Actions des menus
		// TODO: actions des menus
		// TODO: when adding a machine, enable all the buttons
		// TODO: ajouter des cas des exceptions
		newMachine.addActionListener(event -> {
		});
		newRuban.addActionListener(event -> {
			try {
				String input = JOptionPane.showInputDialog(Fenetre.this,
						"Entrez votre ruban.\n" + 
						"Le ruban commence par un chiffre qui signfie " +
						"la position initiale de la tete\n" + 
						"Les reste sont separer par espace",
						"");
				input += " ";
				if(!input.matches("\\d+ (\\w+ )+")){
					throw new IllegalArgumentException("Il faut suivre les consignes");
				}
				// TODO: cree Ruban
			} catch (Exception e) {
				JOptionPane.showMessageDialog(Fenetre.this,
						e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		});
		readMachine.addActionListener(event -> {
		});
		readRuban.addActionListener(event -> {
					timer.stop();
					stop.setEnabled(false);
					start.setEnabled(true);
					JFileChooser chooser = new JFileChooser();
					int returnVal = chooser.showOpenDialog(Fenetre.this);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						machine.changerRuban(chooser.getSelectedFile().getAbsolutePath());
					}
				});
		writeMachine.addActionListener(event -> {
		});
		writeRuban.addActionListener(event -> {
			timer.stop();
			stop.setEnabled(false);
			start.setEnabled(true);
			JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showSaveDialog(Fenetre.this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				try (PrintWriter writer = new PrintWriter(
						new FileWriter(chooser.getSelectedFile()))){
					// TODO: get Ruban
					Ruban ban = null;
					List<String> ruban = ban.getRuban();
					writer.print(ruban.size() + " ");
					for(String s : ruban){
						writer.print(s + " ");
					}
					writer.println();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(Fenetre.this, 
							e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
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
			String result = "";
			// TODO: get ruban
			return result;
		}
		@Override
		public void done(){
			try {
				ruban.setText(get());
				ruban.setScrollOffset(offset);
				// update status
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private class ChangeTable extends SwingWorker<DefaultTableModel, Object>{
		@Override
		public DefaultTableModel doInBackground(){
			// TODO: changer des variables
			List<String> symbols = null;
			List<String> status = null;
			final int move_Left = -1;
			final int stay = 0;
			final int move_Right = 1;
			int[][][] tableAction = null;
			Object[][] excel = new Object[symbols.size()*status.size()][5];
			// tableAction premier indice: etat, deuxieme indice: symbol
			for(int i = 0; i < symbols.size(); i++){
				for(int j = 0; j < status.size(); j++){
					int[] translation = tableAction[j][i];
					excel[i * status.size() + j][0] = status.get(j);
					excel[i * status.size() + j][1] = symbols.get(i);
					excel[i * status.size() + j][2] = status.get(translation[0]);
					excel[i * status.size() + j][3] = status.get(translation[2]);
					switch(translation[1]){
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
