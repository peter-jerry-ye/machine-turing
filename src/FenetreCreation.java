import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import javax.swing.event.*;

public class FenetreCreation extends JFrame{
	
	private static final long serialVersionUID = 1L;

	private JPanel mainPanel, entryPanel, buttonState, buttonSymbol, buttonValidation;
	private JTextField text1, text2, textBrowse;
	private JList<String> states, symbols;
	private JScrollPane scrollStates, scrollSymbols, scrollTable;
	private JButton ok, cancel, add1, add2, del1, del2, browse;
	private JLabel label1, label2, labelData;
	private JFileChooser fc;
	private FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files","txt");
	private DefaultListModel<String> modelStates, modelSymbols;
	private GridBagConstraints c;
	private JTable table;
	private File file;
	private DefaultTableModel tableModel;
	private String FileDirectory;
	private JComboBox<String> selNext, selState;
	
	public static void initialiseMachine(){
		new FenetreCreation();
		
	}
	
	
	private FenetreCreation(){
		super("Machine de Turing - Creation");
		setSize(1000,450);
		setLocation(100, 100);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		label1 = new JLabel("States :");
		label2 = new JLabel("Symbols :");
		
		String[] colNames = {"State", "Seen", "Change to", "NewState", "Move"};
		
		modelStates = new DefaultListModel<String>();
		states = new JList<String>(modelStates);
		states.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		states.setVisibleRowCount(10);
		scrollStates = new JScrollPane(states);
		
		modelSymbols = new DefaultListModel<String>();
		symbols = new JList<String>(modelSymbols);
		symbols.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		symbols.setVisibleRowCount(10);
		scrollSymbols = new JScrollPane(symbols);
		
		selNext = new JComboBox<String>();
		selState = new JComboBox<String>();
		
		states.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt){
				if(!evt.getValueIsAdjusting()){
					if(modelSymbols.size()!=0){
						tableModel.setRowCount(modelSymbols.size());
						tableModel.setValueAt(states.getSelectedValue(), 0, 0);
						for(int i = 0; i<modelSymbols.size(); i++){
							tableModel.setValueAt((String)modelSymbols.getElementAt(i), i, 1);
						}
						table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(selNext));
						table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(selState));
					} else {
						tableModel.setRowCount(1);
						tableModel.setValueAt(states.getSelectedValue(), 0, 0);
					}
				}
			}			
		});
		
		modelSymbols.addListDataListener(new ListDataListener() {
			public void intervalAdded(ListDataEvent evt) {
				tableModel.setRowCount(modelSymbols.size());
				for(int i = 0; i<modelSymbols.size(); i++){
					tableModel.setValueAt((String)modelSymbols.getElementAt(i), i, 1);
				}
				table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(selNext));
				table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(selState));
			}
			
			public void intervalRemoved(ListDataEvent evt) {
				tableModel.setRowCount(modelSymbols.size());
				for(int i = 0; i<modelSymbols.size(); i++){
					tableModel.setValueAt((String)modelSymbols.getElementAt(i), i, 1);
				}
				table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(selNext));
				table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(selState));
			}
			
			public void contentsChanged(ListDataEvent evt) {
				tableModel.setRowCount(modelSymbols.size());
				for(int i = 0; i<modelSymbols.size(); i++){
					tableModel.setValueAt((String)modelSymbols.getElementAt(i), i, 1);
				}
				table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(selNext));
				table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(selState));
			}
		});
		
		
		
		text1 = new JTextField();		
		text2 = new JTextField();
		
		ok = new JButton("OK");
		
		cancel = new JButton("Cancel");
		
		add1 = new JButton("+");
		add1.addActionListener( e -> {
			modelStates.addElement(text1.getText());
			selState.addItem(text1.getText());
			text1.setText(null);
		});
		
		add2 = new JButton("+");
		add2.addActionListener( e -> {
			modelSymbols.addElement(text2.getText());
			selNext.addItem(text2.getText());
			text2.setText(null);
		});
		
		del1 = new JButton("-");
		del1.addActionListener( e -> {
			if(states.getSelectedIndex()!=-1) {
				selState.removeItem(modelStates.getElementAt(states.getSelectedIndex()));
				modelStates.remove(states.getSelectedIndex());
				}
		});
		
		del2 = new JButton("-");
		del2.addActionListener( e -> {
			if(symbols.getSelectedIndex()!=-1) {
				selNext.removeItem(modelSymbols.getElementAt(symbols.getSelectedIndex()));
				modelSymbols.remove(symbols.getSelectedIndex());
			}
		});
		
		buttonState = new JPanel(new GridLayout(1, 2, 10, 0));
		buttonState.add(add1);
		buttonState.add(del1);
		
		buttonSymbol = new JPanel(new GridLayout(1, 2, 10, 0));
		buttonSymbol.add(add2);
		buttonSymbol.add(del2);
		
		labelData = new JLabel("Data :");	
		textBrowse = new JTextField();
		textBrowse.setEditable(false);
		textBrowse.setBackground(Color.white);
		
		browse = new JButton("Browse...");
		browse.addActionListener( e -> {
			int returnVal = fc.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				textBrowse.setText(file.getAbsolutePath());
				FileDirectory = file.getAbsolutePath();
			}
		});
		
		fc = new JFileChooser();
		fc.setFileFilter(filter);
		
		table = new JTable(0,5);
		tableModel = (DefaultTableModel) table.getModel();
		tableModel.setColumnIdentifiers(colNames);
		
		scrollTable = new JScrollPane(table);		
		
		entryPanel = new JPanel(new GridBagLayout());
		
		c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 10, 5, 10);
		c.fill = GridBagConstraints.BOTH;
		
		entryPanel.add(labelData,c);
		
		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 3;
		entryPanel.add(textBrowse,c);
		
		c.gridx = 3;
		entryPanel.add(browse,c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		entryPanel.add(label1,c);
		
		c.gridx = 2;
		entryPanel.add(label2, c);
		
		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 2;
		entryPanel.add(buttonState,c);
		
		c.gridx = 2;
		entryPanel.add(buttonSymbol,c);
		
		c.gridy = 4;
		c.gridx = 0;
		entryPanel.add(text1,c);
		
		c.gridy = 5;
		entryPanel.add(scrollStates,c);
		
		c.gridx = 2;
		c.gridy = 4;
		entryPanel.add(text2,c);
		
		c.gridy = 5;
		entryPanel.add(scrollSymbols,c);
		
		buttonValidation = new JPanel(new GridLayout(1, 2, 10, 0));
		buttonValidation.add(ok);
		buttonValidation.add(cancel);
		
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(entryPanel, BorderLayout.WEST);	
		mainPanel.add(scrollTable, BorderLayout.EAST);
		mainPanel.add(buttonValidation, BorderLayout.SOUTH);
		setContentPane(mainPanel);
		pack();
		setVisible(true);
	}
}
