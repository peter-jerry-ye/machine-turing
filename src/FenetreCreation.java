import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class FenetreCreation extends JFrame implements ActionListener, FocusListener{
	
	private static final long serialVersionUID = 1L;

	JPanel mainPanel;
	JTextField text1, text2, textBrowse;
	JList<String> states, symbols;
	JScrollPane scrollStates, scrollSymbols;
	JButton ok, cancel, add1, add2, del1, del2, browse;
	JLabel label1, label2, labelData;
	JFileChooser fc;
	FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files","txt");
	DefaultListModel<String> listModel1, listModel2;
	
	public FenetreCreation(){
		super("Machine de Turing - Creation");
		setSize(500,530);
		setLocation(100,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		setResizable(false);
		
		label1 = new JLabel("States :");
		label1.setBounds(15,30,120,20);
		
		label2 = new JLabel("Symbols :");
		label2.setBounds(150,30,120,20);
		
		listModel1 = new DefaultListModel<String>();
		states = new JList<String>(listModel1);
		states.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		states.setVisibleRowCount(10);
		
		scrollStates = new JScrollPane(states);
		scrollStates.setBounds(15,50,120,200);
		
		listModel2 = new DefaultListModel<String>();
		symbols = new JList<String>(listModel2);
		symbols.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		symbols.setVisibleRowCount(10);
		
		scrollSymbols = new JScrollPane(symbols);
		scrollSymbols.setBounds(150,50,120,200);
		
		text1 = new JTextField();
		text1.setBounds(15, 255, 120, 25);
		
		text2 = new JTextField();
		text2.setBounds(150, 255, 120, 25);
		
		ok = new JButton("OK");
		ok.setBounds(300,455,80,30);
		
		cancel = new JButton("Cancel");
		cancel.setBounds(395,455,80,30);
		
		add1 = new JButton("+");
		add1.setBounds(15,285,55,25);
		add1.addActionListener(this);
		
		add2 = new JButton("+");
		add2.setBounds(150,285,55,25);
		add2.addActionListener(this);
		
		del1 = new JButton("-");
		del1.setBounds(80,285,55,25);
		del1.addActionListener(this);
		
		del2 = new JButton("-");
		del2.setBounds(215,285,55,25);
		del2.addActionListener(this);
		
		labelData = new JLabel("Data :");
		labelData.setBounds(15, 325, 200, 25);		
		
		textBrowse = new JTextField();
		textBrowse.setBounds(15, 350, 155, 25);
		
		browse = new JButton("Browse...");
		browse.setBounds(180, 350, 90, 25);
		browse.addActionListener(this);
		
		fc = new JFileChooser();
		fc.setFileFilter(filter);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBounds(0, 0, 500, 400);
		//mainPanel.setBackground(Color.BLACK);
		mainPanel.add(scrollStates);
		mainPanel.add(scrollSymbols);
		mainPanel.add(ok);
		mainPanel.add(cancel);
		mainPanel.add(add1);
		mainPanel.add(add2);
		mainPanel.add(del1);
		mainPanel.add(del2);
		mainPanel.add(label1);
		mainPanel.add(label2);
		mainPanel.add(text1);
		mainPanel.add(text2);
		mainPanel.add(textBrowse);
		mainPanel.add(browse);
		mainPanel.add(labelData);
		setContentPane(mainPanel);
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==add1) {
			listModel1.addElement(text1.getText());
			text1.setText(null);
		}
		if(e.getSource()==add2) {
			listModel2.addElement(text2.getText());
			text2.setText(null);
		}
		if(e.getSource()==del1) {
			if(states.getSelectedIndex()!=-1) {
				listModel1.remove(states.getSelectedIndex());
			}
		}
		if(e.getSource()==del2) {
			if(symbols.getSelectedIndex()!=-1) {
				listModel2.remove(symbols.getSelectedIndex());
			}
		}
		if(e.getSource()==browse) {
			int returnVal = fc.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				textBrowse.setText(file.getAbsolutePath());
			}
		}
		
	}
	
	public void focusGained(FocusEvent e) {
		if(e.getSource()==text1 && text1.getText().isEmpty()) {
			
		}
		if(e.getSource()==text2 && text2.getText().isEmpty()) {
			
		}
	}
	
	public void focusLost(FocusEvent e) {
		
	}
}
