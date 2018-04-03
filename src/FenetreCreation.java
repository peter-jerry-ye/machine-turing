import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.CyclicBarrier;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 * Le class qui cree la machine
 *
 * @author WONG Teck
 */
public class FenetreCreation extends JFrame {

	private JLabel labelEtat, labelSym, labelDon;
	private JButton ok, annuler, ajoutSym, ajoutEtat, suppSym, suppEtat, parcourir;
	private JTextField textEtat, textSym, textPar;

	private JList<String> etats, symboles;
	private DefaultListModel<String> modelEtat, modelSym;
	private JScrollPane scrollEtat, scrollSym, scrollTab;

	private JPanel mainPanel, entryPanel, boutonEtat, boutonSym, boutonVal;
	private GridBagConstraints c;

	private JTable tab;
	private JComboBox<String> selSym, selEtat, selDeplace;
	private ArrayList<String> tabSym, tabEtat;
	private DefaultTableModel emptyMod;
	private HashMap<String, DefaultTableModel> tabModel;

	private int[][][] tabAct;

	private static CyclicBarrier barrier = new CyclicBarrier(2);
	private static Turing_Machine machine = null;

	/**
	 * Cree une nouvelle machine de Turing
	 *
	 * @return une machine turing ou null a la presence d'erreur
	 */
	public static Turing_Machine initialiseMachine() {
		new FenetreCreation();
		try {
			barrier.await();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		Turing_Machine tmp = machine;
		machine = null;
		return tmp;
	}

	private FenetreCreation() {
		super("Machine de Turing - Creation");
		setSize(1000, 450);
		setLocation(100, 100);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		labelEtat = new JLabel("Etats :");
		labelSym = new JLabel("Symboles :");
		labelDon = new JLabel("Donnee :");

		ok = new JButton("OK");
		annuler = new JButton("Annuler");
		ajoutEtat = new JButton("+");
		suppEtat = new JButton("-");
		ajoutSym = new JButton("+");
		suppSym = new JButton("-");

		textEtat = new JTextField();
		textSym = new JTextField();

		modelEtat = new DefaultListModel<String>();
		etats = new JList<String>(modelEtat);
		etats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		etats.setVisibleRowCount(10);
		scrollEtat = new JScrollPane(etats);

		modelSym = new DefaultListModel<String>();
		symboles = new JList<String>(modelSym);
		symboles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		symboles.setVisibleRowCount(10);
		scrollSym = new JScrollPane(symboles);

		selSym = new JComboBox<String>();
		selEtat = new JComboBox<String>();
		selDeplace = new JComboBox<String>();
		selEtat.addItem("eFinale");
		selSym.addItem("#");
		selDeplace.addItem("Gauche");
		selDeplace.addItem("Droite");
		selDeplace.addItem("Statique");

		tabModel = new HashMap<String, DefaultTableModel>();
		String[] colId = {"Etat", "Vue", "Changer à", "Prochain Etat", "Deplacement"};
		emptyMod = new DefaultTableModel(0, 5);
		emptyMod.setColumnIdentifiers(colId);
		tab = new JTable(emptyMod);
		scrollTab = new JScrollPane(tab);

		tabSym = new ArrayList<String>();
		tabEtat = new ArrayList<String>();
		
		tabSym.add("#");

		ajoutEtat.addActionListener(e -> {
			String data = textEtat.getText();
			if (!modelEtat.contains(data) && !data.isEmpty() && !data.contains(" ") && !data.equals("eFinale")) {
				modelEtat.addElement(data);
				tabEtat.add(data);
				tabModel.put(data, new DefaultTableModel(0, 4) {
					@Override
					public boolean isCellEditable(int row, int column) {
						return !(column == 0 || column == 1);
					}
				});
				tabModel.get(data).setColumnIdentifiers(colId);
				tabModel.get(data).addRow(new Object[]{data, "", "", "", ""});
				for (int i = 0; i < modelSym.size(); i++) {
					if (i == 0) {
						tabModel.get(data).setValueAt(modelSym.get(i), i, 1);
					} else {
						tabModel.get(data).addRow(new Object[]{"", modelSym.get(i), "", "", ""});
					}
				}
				selEtat.addItem(data);
			}
			textEtat.setText(null);
		});

		suppEtat.addActionListener(e -> {
			int index = etats.getSelectedIndex();
			if (index != -1) {
				String data = etats.getSelectedValue();
				modelEtat.remove(index);
				tabEtat.remove(index);
				tabModel.remove(data);
				selEtat.removeItem(data);
			}
		});

		ajoutSym.addActionListener(e -> {
			String data = textSym.getText();
			if(modelSym.isEmpty()){
				modelSym.addElement("#");
			}
			if (!modelSym.contains(data) && !data.isEmpty() && !data.contains(" ") && !data.equals("#")) {
				modelSym.addElement(data);
				tabSym.add(data);
				if (modelSym.size() == 1) {
					for (String s : tabModel.keySet()) {
						tabModel.get(s).setValueAt(data, 0, 1);
					}
				} else {
					for (String s : tabModel.keySet()) {
						tabModel.get(s).addRow(new Object[]{"", data, "", "", ""});
					}
				}
				selSym.addItem(data);
			}
			textSym.setText(null);
		});

		suppSym.addActionListener(e -> {
			int index = symboles.getSelectedIndex();
			if (index != -1 && !modelSym.getElementAt(index).equals("#")) {
				selSym.removeItem(modelSym.getElementAt(index));
				tabSym.remove(index);
				modelSym.remove(index);
				for (DefaultTableModel d : tabModel.values()) {
					d.removeRow(index);
					if (index == 0 && d.getRowCount() == 0) {
						d.addRow(new Object[]{etats.getSelectedValue(), "", "", "", ""});
					} else if (index == 0) {
						d.setValueAt(etats.getSelectedValue(), 0, 0);
					}
				}
			}
		});

		annuler.addActionListener(e -> {
			try {
				barrier.await();
			} catch (Exception ex) {
			}
			this.dispose();
		});

		ok.addActionListener(e -> {
			try {
				if (tabEtat.isEmpty() || tabSym.isEmpty()) {
					throw new Exception();
				} else {
					tabAct = new int[tabEtat.size()][tabSym.size()][3];
					for (int i = 0; i < tabAct.length; i++) {
						String etat = tabEtat.get(i);
						for (int j = 0; j < tabAct[i].length; j++) {
							String newSym = (String) tabModel.get(etat).getValueAt(j, 2);
							String newEtat = (String) tabModel.get(etat).getValueAt(j, 3);
							String dep = (String) tabModel.get(etat).getValueAt(j, 4);

							if (newSym == "" || newEtat == "" || dep == "") {
								throw new Exception();
							}

							tabAct[i][j][0] = tabSym.indexOf(newSym);

							if (newEtat.equals("eFinale")) {
								tabAct[i][j][1] = -1;
							} else {
								tabAct[i][j][1] = tabEtat.indexOf(newEtat);
							}


							if (dep.equals("Gauche")) {
								tabAct[i][j][2] = Ruban.GAUCHE;
							} else if (dep.equals("Droite")) {
								tabAct[i][j][2] = Ruban.DROITE;
							} else if (dep.equals("Statique")) {
								tabAct[i][j][2] = Ruban.RESTER;
							}
						}
					}
					this.dispose();
					machine = new Turing_Machine(tabAct, tabEtat, tabSym);
					barrier.await();
				}
			} catch (Exception n) {
				JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs de saisie!", "", JOptionPane.WARNING_MESSAGE);
			}
		});

		etats.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				if (!evt.getValueIsAdjusting()) {
					if (!modelEtat.isEmpty() && etats.getSelectedIndex() != -1) {
						tab.setModel(tabModel.get(etats.getSelectedValue()));
					} else {
						tab.setModel(emptyMod);
					}
				}
				tab.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(selSym));
				tab.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(selEtat));
				tab.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(selDeplace));
			}
		});

		entryPanel = new JPanel(new GridBagLayout());
		mainPanel = new JPanel(new BorderLayout());
		boutonVal = new JPanel(new GridLayout(1, 2, 0, 0));
		boutonEtat = new JPanel(new GridLayout(1, 2, 10, 0));
		boutonSym = new JPanel(new GridLayout(1, 2, 10, 0));

		boutonEtat.add(ajoutEtat);
		boutonEtat.add(suppEtat);
		boutonSym.add(ajoutSym);
		boutonSym.add(suppSym);

		c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 10, 5, 10);
		c.fill = GridBagConstraints.BOTH;

		entryPanel.add(labelDon, c);

		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 3;
		entryPanel.add(textPar, c);

		c.gridx = 3;
		entryPanel.add(parcourir, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		entryPanel.add(labelEtat, c);

		c.gridx = 2;
		entryPanel.add(labelSym, c);

		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 2;
		entryPanel.add(boutonEtat, c);

		c.gridx = 2;
		entryPanel.add(boutonSym, c);

		c.gridy = 4;
		c.gridx = 0;
		entryPanel.add(textEtat, c);

		c.gridy = 5;
		entryPanel.add(scrollEtat, c);

		c.gridx = 2;
		c.gridy = 4;
		entryPanel.add(textSym, c);

		c.gridy = 5;
		entryPanel.add(scrollSym, c);

		boutonVal.add(ok);
		boutonVal.add(annuler);
		mainPanel.add(entryPanel, BorderLayout.WEST);
		mainPanel.add(scrollTab, BorderLayout.EAST);
		mainPanel.add(boutonVal, BorderLayout.SOUTH);
		setContentPane(mainPanel);
		pack();
		setVisible(true);
	}
}

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 * Le class qui cree la machine
 *
 * @author WONG Teck
 */
public class FenetreCreation extends JFrame {

	private JLabel labelEtat, labelSym, labelDon;
	private JButton ok, annuler, ajoutSym, ajoutEtat, suppSym, suppEtat, parcourir;
	private JTextField textEtat, textSym, textPar;

	private JList<String> etats, symboles;
	private DefaultListModel<String> modelEtat, modelSym;
	private JScrollPane scrollEtat, scrollSym, scrollTab;

	private JPanel mainPanel, entryPanel, boutonEtat, boutonSym, boutonVal;
	private GridBagConstraints c;

	private JTable tab;
	private JComboBox<String> selSym, selEtat, selDeplace;
	private ArrayList<String> tabSym, tabEtat;
	private DefaultTableModel emptyMod;
	private HashMap<String, DefaultTableModel> tabModel;

	private int[][][] tabAct;

	private static CyclicBarrier barrier = new CyclicBarrier(2);
	private static Turing_Machine machine = null;

	/**
	 * Cree une nouvelle machine de Turing
	 *
	 * @return une machine turing ou null a la presence d'erreur
	 */
	public static Turing_Machine initialiseMachine() {
		new FenetreCreation();
		try {
			barrier.await();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		Turing_Machine tmp = machine;
		machine = null;
		return tmp;
	}

	private FenetreCreation() {
		super("Machine de Turing - Creation");
		setSize(1000, 450);
		setLocation(100, 100);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		labelEtat = new JLabel("Etats :");
		labelSym = new JLabel("Symboles :");
		labelDon = new JLabel("Donnee :");

		ok = new JButton("OK");
		annuler = new JButton("Annuler");
		ajoutEtat = new JButton("+");
		suppEtat = new JButton("-");
		ajoutSym = new JButton("+");
		suppSym = new JButton("-");

		textEtat = new JTextField();
		textSym = new JTextField();

		modelEtat = new DefaultListModel<String>();
		etats = new JList<String>(modelEtat);
		etats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		etats.setVisibleRowCount(10);
		scrollEtat = new JScrollPane(etats);

		modelSym = new DefaultListModel<String>();
		symboles = new JList<String>(modelSym);
		symboles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		symboles.setVisibleRowCount(10);
		scrollSym = new JScrollPane(symboles);

		selSym = new JComboBox<String>();
		selEtat = new JComboBox<String>();
		selDeplace = new JComboBox<String>();
		selEtat.addItem("eFinale");
		selSym.addItem("#");
		selDeplace.addItem("Gauche");
		selDeplace.addItem("Droite");
		selDeplace.addItem("Statique");

		tabModel = new HashMap<String, DefaultTableModel>();
		String[] colId = {"Etat", "Vue", "Changer à", "Prochain Etat", "Deplacement"};
		emptyMod = new DefaultTableModel(0, 5);
		emptyMod.setColumnIdentifiers(colId);
		tab = new JTable(emptyMod);
		scrollTab = new JScrollPane(tab);

		tabSym = new ArrayList<String>();
		tabEtat = new ArrayList<String>();
		
		tabSym.add("#");

		ajoutEtat.addActionListener(e -> {
			String data = textEtat.getText();
			if (!modelEtat.contains(data) && !data.isEmpty() && !data.contains(" ") && !data.equals("eFinale")) {
				modelEtat.addElement(data);
				tabEtat.add(data);
				tabModel.put(data, new DefaultTableModel(0, 4) {
					@Override
					public boolean isCellEditable(int row, int column) {
						return !(column == 0 || column == 1);
					}
				});
				tabModel.get(data).setColumnIdentifiers(colId);
				tabModel.get(data).addRow(new Object[]{data, "", "", "", ""});
				for (int i = 0; i < modelSym.size(); i++) {
					if (i == 0) {
						tabModel.get(data).setValueAt(modelSym.get(i), i, 1);
					} else {
						tabModel.get(data).addRow(new Object[]{"", modelSym.get(i), "", "", ""});
					}
				}
				selEtat.addItem(data);
			}
			textEtat.setText(null);
		});

		suppEtat.addActionListener(e -> {
			int index = etats.getSelectedIndex();
			if (index != -1) {
				String data = etats.getSelectedValue();
				modelEtat.remove(index);
				tabEtat.remove(index);
				tabModel.remove(data);
				selEtat.removeItem(data);
			}
		});

		ajoutSym.addActionListener(e -> {
			String data = textSym.getText();
			if(modelSym.isEmpty()){
				modelSym.addElement("#");
			}
			if (!modelSym.contains(data) && !data.isEmpty() && !data.contains(" ") && !data.equals("#")) {
				modelSym.addElement(data);
				tabSym.add(data);
				if (modelSym.size() == 1) {
					for (String s : tabModel.keySet()) {
						tabModel.get(s).setValueAt(data, 0, 1);
					}
				} else {
					for (String s : tabModel.keySet()) {
						tabModel.get(s).addRow(new Object[]{"", data, "", "", ""});
					}
				}
				selSym.addItem(data);
			}
			textSym.setText(null);
		});

		suppSym.addActionListener(e -> {
			int index = symboles.getSelectedIndex();
			if (index != -1 && !modelSym.getElementAt(index).equals("#")) {
				selSym.removeItem(modelSym.getElementAt(index));
				tabSym.remove(index);
				modelSym.remove(index);
				for (DefaultTableModel d : tabModel.values()) {
					d.removeRow(index);
					if (index == 0 && d.getRowCount() == 0) {
						d.addRow(new Object[]{etats.getSelectedValue(), "", "", "", ""});
					} else if (index == 0) {
						d.setValueAt(etats.getSelectedValue(), 0, 0);
					}
				}
			}
		});

		annuler.addActionListener(e -> {
			try {
				barrier.await();
			} catch (Exception ex) {
			}
			this.dispose();
		});

		ok.addActionListener(e -> {
			try {
				if (tabEtat.isEmpty() || tabSym.isEmpty()) {
					throw new Exception();
				} else {
					tabAct = new int[tabEtat.size()][tabSym.size()][3];
					for (int i = 0; i < tabAct.length; i++) {
						String etat = tabEtat.get(i);
						for (int j = 0; j < tabAct[i].length; j++) {
							String newSym = (String) tabModel.get(etat).getValueAt(j, 2);
							String newEtat = (String) tabModel.get(etat).getValueAt(j, 3);
							String dep = (String) tabModel.get(etat).getValueAt(j, 4);

							if (newSym == "" || newEtat == "" || dep == "") {
								throw new Exception();
							}

							tabAct[i][j][0] = tabSym.indexOf(newSym);

							if (newEtat.equals("eFinale")) {
								tabAct[i][j][1] = -1;
							} else {
								tabAct[i][j][1] = tabEtat.indexOf(newEtat);
							}


							if (dep.equals("Gauche")) {
								tabAct[i][j][2] = Ruban.GAUCHE;
							} else if (dep.equals("Droite")) {
								tabAct[i][j][2] = Ruban.DROITE;
							} else if (dep.equals("Statique")) {
								tabAct[i][j][2] = Ruban.RESTER;
							}
						}
					}
					this.dispose();
					machine = new Turing_Machine(tabAct, tabEtat, tabSym);
					barrier.await();
				}
			} catch (Exception n) {
				JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs de saisie!", "", JOptionPane.WARNING_MESSAGE);
			}
		});

		etats.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				if (!evt.getValueIsAdjusting()) {
					if (!modelEtat.isEmpty() && etats.getSelectedIndex() != -1) {
						tab.setModel(tabModel.get(etats.getSelectedValue()));
					} else {
						tab.setModel(emptyMod);
					}
				}
				tab.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(selSym));
				tab.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(selEtat));
				tab.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(selDeplace));
			}
		});

		entryPanel = new JPanel(new GridBagLayout());
		mainPanel = new JPanel(new BorderLayout());
		boutonVal = new JPanel(new GridLayout(1, 2, 0, 0));
		boutonEtat = new JPanel(new GridLayout(1, 2, 10, 0));
		boutonSym = new JPanel(new GridLayout(1, 2, 10, 0));

		boutonEtat.add(ajoutEtat);
		boutonEtat.add(suppEtat);
		boutonSym.add(ajoutSym);
		boutonSym.add(suppSym);

		c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 10, 5, 10);
		c.fill = GridBagConstraints.BOTH;

		entryPanel.add(labelDon, c);

		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 3;
		entryPanel.add(textPar, c);

		c.gridx = 3;
		entryPanel.add(parcourir, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		entryPanel.add(labelEtat, c);

		c.gridx = 2;
		entryPanel.add(labelSym, c);

		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 2;
		entryPanel.add(boutonEtat, c);

		c.gridx = 2;
		entryPanel.add(boutonSym, c);

		c.gridy = 4;
		c.gridx = 0;
		entryPanel.add(textEtat, c);

		c.gridy = 5;
		entryPanel.add(scrollEtat, c);

		c.gridx = 2;
		c.gridy = 4;
		entryPanel.add(textSym, c);

		c.gridy = 5;
		entryPanel.add(scrollSym, c);

		boutonVal.add(ok);
		boutonVal.add(annuler);
		mainPanel.add(entryPanel, BorderLayout.WEST);
		mainPanel.add(scrollTab, BorderLayout.EAST);
		mainPanel.add(boutonVal, BorderLayout.SOUTH);
		setContentPane(mainPanel);
		pack();
		setVisible(true);
	}
}

			int index = etats.getSelectedIndex();
			if (index != -1) {
				String data = etats.getSelectedValue();
				modelEtat.remove(index);
				tabEtat.remove(index);
				tabModel.remove(data);
				selEtat.removeItem(data);
			}
		});

		ajoutSym.addActionListener(e -> {
			String data = textSym.getText();
			if (!modelSym.contains(data) && !data.isEmpty() && !data.contains(" ") && !data.equals("#")) {
				modelSym.addElement(data);
				tabSym.add(data);
				if (modelSym.size() == 1) {
					for (String s : tabModel.keySet()) {
						tabModel.get(s).setValueAt(data, 0, 1);
					}
				} else {
					for (String s : tabModel.keySet()) {
						tabModel.get(s).addRow(new Object[]{"", data, "", "", ""});
					}
				}
				selSym.addItem(data);
			}
			textSym.setText(null);
		});

		suppSym.addActionListener(e -> {
			int index = symboles.getSelectedIndex();
			if (index != -1 && !modelSym.getElementAt(index).equals("#")) {
				selSym.removeItem(modelSym.getElementAt(index));
				tabSym.remove(index);
				modelSym.remove(index);
				for (DefaultTableModel d : tabModel.values()) {
					d.removeRow(index);
					if (index == 0 && d.getRowCount() == 0) {
						d.addRow(new Object[]{etats.getSelectedValue(), "", "", "", ""});
					} else if (index == 0) {
						d.setValueAt(etats.getSelectedValue(), 0, 0);
					}
				}
			}
		});

		parcourir.addActionListener(e -> {
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				textPar.setText(file.getAbsolutePath());
				repertoire = file.getAbsolutePath();
			}
		});

		annuler.addActionListener(e -> {
			try {
				barrier.await();
			} catch (Exception ex) {
			}
			this.dispose();
		});

		ok.addActionListener(e -> {
			try {
				if (tabEtat.isEmpty() || tabSym.isEmpty()) {
					throw new Exception();
				} else {
					tabAct = new int[tabEtat.size()][tabSym.size()][3];
					for (int i = 0; i < tabAct.length; i++) {
						String etat = tabEtat.get(i);
						for (int j = 0; j < tabAct[i].length; j++) {
							String newSym = (String) tabModel.get(etat).getValueAt(j, 2);
							String newEtat = (String) tabModel.get(etat).getValueAt(j, 3);
							String dep = (String) tabModel.get(etat).getValueAt(j, 4);

							if (newSym == "" || newEtat == "" || dep == "") {
								throw new Exception();
							}

//							if(newSym.equals("#")) {
//								tabAct[i][j][0] = -1;
//							} else {
							tabAct[i][j][0] = tabSym.indexOf(newSym);
//							}


							if (newEtat.equals("eFinale")) {
								tabAct[i][j][1] = -1;
							} else {
								tabAct[i][j][1] = tabEtat.indexOf(newEtat);
							}


							if (dep.equals("Gauche")) {
								tabAct[i][j][2] = Ruban.GAUCHE;
							} else if (dep.equals("Droite")) {
								tabAct[i][j][2] = Ruban.DROITE;
							} else if (dep.equals("Statique")) {
								tabAct[i][j][2] = Ruban.RESTER;
							}
						}
					}
					this.dispose();
					machine = new Turing_Machine(tabAct, tabEtat, tabSym);
					barrier.await();
				}
			} catch (Exception n) {
				JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs de saisie!", "", JOptionPane.WARNING_MESSAGE);
			}
		});

		etats.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				if (!evt.getValueIsAdjusting()) {
					if (!modelEtat.isEmpty() && etats.getSelectedIndex() != -1) {
						tab.setModel(tabModel.get(etats.getSelectedValue()));
					} else {
						tab.setModel(emptyMod);
					}
				}
				tab.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(selSym));
				tab.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(selEtat));
				tab.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(selDeplace));
			}
		});

		entryPanel = new JPanel(new GridBagLayout());
		mainPanel = new JPanel(new BorderLayout());
		boutonVal = new JPanel(new GridLayout(1, 2, 0, 0));
		boutonEtat = new JPanel(new GridLayout(1, 2, 10, 0));
		boutonSym = new JPanel(new GridLayout(1, 2, 10, 0));

		boutonEtat.add(ajoutEtat);
		boutonEtat.add(suppEtat);
		boutonSym.add(ajoutSym);
		boutonSym.add(suppSym);

		c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 10, 5, 10);
		c.fill = GridBagConstraints.BOTH;

		entryPanel.add(labelDon, c);

		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 3;
		entryPanel.add(textPar, c);

		c.gridx = 3;
		entryPanel.add(parcourir, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		entryPanel.add(labelEtat, c);

		c.gridx = 2;
		entryPanel.add(labelSym, c);

		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 2;
		entryPanel.add(boutonEtat, c);

		c.gridx = 2;
		entryPanel.add(boutonSym, c);

		c.gridy = 4;
		c.gridx = 0;
		entryPanel.add(textEtat, c);

		c.gridy = 5;
		entryPanel.add(scrollEtat, c);

		c.gridx = 2;
		c.gridy = 4;
		entryPanel.add(textSym, c);

		c.gridy = 5;
		entryPanel.add(scrollSym, c);

		boutonVal.add(ok);
		boutonVal.add(annuler);
		mainPanel.add(entryPanel, BorderLayout.WEST);
		mainPanel.add(scrollTab, BorderLayout.EAST);
		mainPanel.add(boutonVal, BorderLayout.SOUTH);
		setContentPane(mainPanel);
		pack();
		setVisible(true);
	}
}
