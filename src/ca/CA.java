/*
 * Student Names: David Burchat & Marcos Astudillo Carrasco
 * Student Number: 040513895 & 041057439
 * Course: CST8221 Java Application Programming
 * Assignment: A12
 * Lab Section: 301
 * Program: CET-CS Level 4
 * Professor: Paulo Sousa Ph.D
 * Due Date: Oct/1/2023
 * References: See cs/CSModel.java for Reference List 
 */
package ca;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import cs.CSModel;
import support.HelpCA;
import support.LanguageManager;

/**
 * This class holds the window / gui for cellular automata operations. 
 * It allows users to input binary rules, view the results, and change the language of the interface.
 * 
 * @author David Burchat, Marcos Astudillo Carrasco
 * @version 1.0
 * @since 1.8
 * 
 */
public class CA extends JFrame {

	/**
	 * Unique identifier for the serialized class.
	 */
	private static final long serialVersionUID = 3L;

	/**
	 * Holds an instance of the Language Manager
	 */
	private LanguageManager languageManager = LanguageManager.getInstance();;

	/**
	 * Holds a content pane from JPanel
	 */
	private JPanel contentPane;
	
	/**
	 * Holds the header pane for header content
	 */
	private JPanel headerPanel;
	
	/**
	 * Holds the title image at the top of the window
	 */
	private JLabel imageLabel;
	
	/**
	 * Holds the panel that will display the cellular automata
	 */
	private CellularAutomataPanel cellularAutomataPanel;
	
	/**
	 * Holds the text field where the user can input a binary number
	 */
	private JTextField binaryInput;
	
	/**
	 * Holds the set button object
	 */
	private JButton btnSet;
	
	/**
	 * Hold the results panel
	 */
	private JPanel resultPanel;
	
	/**
	 * A Label to hold the number and to show a border
	 */
	private JLabel modelLabel;
	
	/**
	 * To store the decimal version of the binary number selected
	 */
	private JLabel numberLabel;
	
	/**
	 * Label to identify the area to enter binary number
	 */
	private JLabel binaryRuleLabel;
	
	/**
	 * Holds the return button object
	 */
	private JButton btnReturn;
	
	/**
	 * Holds a language dictionary
	 */
	private ResourceBundle bundle;
	
	/**
	 * Holds the decrement button object
	 */
	private JButton btnDecrement;
	
	/**
	 * Holds the increment button object
	 */
	private JButton btnIncrement;
	
	/**
	 * Holds the menu bar object
	 */
	private JMenuBar menuBar;
	
	/**
	 * Holds the main menu object
	 */
	private JMenu mainMenu;
	
	/**
	 * Holds the language sub menu
	 */
	private JMenu languageSubMenu;
	
	/**
	 * Holds the help sub menu
	 */
	private JMenu helpMenu;
	
	/**
	 * Holds the English Menu Item
	 */
	private JMenuItem englishMenuItem;
	
	/**
	 * Holds the Spanish Menu Item
	 */
	private JMenuItem spanishMenuItem;
	
	/**
	 * Holds the About us Menu Item
	 */
	private JMenuItem aboutUsMenuItem;
	
	/**
	 * Holds the help menu item
	 */
	private JMenuItem helpMenuItem;

	/**
	 * Holds the help window object
	 */
	private HelpCA helpWindow = null;
	
	/**
	 * Holds the about us window object
	 */
	private AboutUs aboutUsWindow = null;

	/**
	 * Constructor for the CA class. Initializes the UI components and sets up event listeners.
	 */
	public CA() {
		// create a bundle
		bundle = languageManager.getBundle();

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Disable default 'X' functionality [14]
		addWindowListener(new WindowAdapter() { // 
			@Override
			public void windowClosing(WindowEvent e) { // In the event the window is closed, close this window
				dispose();
				new CSModel().setVisible(true);			// But make visible the main menu window
			}
		});
		setBounds(100, 100, 1200, 640);
		setResizable(false); // doesn't allow the user maximize the window
		// setUndecorated(true); // doesn't show the close buttons and minimize buttons
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Provide padding for the window content
		contentPane.setLayout(new BorderLayout());

		// Add AC logo to image icon
		URL imgUrl = getClass().getClassLoader().getResource("resources/AC-logo.png");
		ImageIcon icon = new ImageIcon(imgUrl);
		setIconImage(icon.getImage());

		// Menu Bar
		menuBar = new JMenuBar();

		// Main Menu
		mainMenu = new JMenu("Menu");
		menuBar.add(mainMenu);

		// Language Sub-Menu
		languageSubMenu = new JMenu("Language");
		mainMenu.add(languageSubMenu);

		// Language Menu Items
		englishMenuItem = new JMenuItem("English");
		englishMenuItem.addActionListener(e -> {
			languageManager.setLanguage("English");
			bundle = languageManager.getBundle(); // Update ResourceBundle
			refreshUI();
		});
		languageSubMenu.add(englishMenuItem);

		spanishMenuItem = new JMenuItem("Español");
		spanishMenuItem.addActionListener(e -> {
			languageManager.setLanguage("Español");
			bundle = languageManager.getBundle(); // Update ResourceBundle
			refreshUI();
		});
		languageSubMenu.add(spanishMenuItem);

		// Help Menu
		helpMenu = new JMenu("helpBar");
		menuBar.add(helpMenu);

		// Help Sub-Menu Items
		helpMenuItem = new JMenuItem("helpBar");
		helpMenuItem.addActionListener(e -> {
			// Open the help window only one time
			if (helpWindow == null) {
				helpWindow = new HelpCA(languageManager.getCurrentLocale());
				helpWindow.setVisible(true);
				helpWindow.addWindowListener(new WindowAdapter() {
					// set to null the aboutUs window event
					@Override
					public void windowClosed(WindowEvent e) {
						helpWindow = null;
					}
				});
			}
		});
		helpMenu.add(helpMenuItem);

		aboutUsMenuItem = new JMenuItem("aboutUsBar");
		aboutUsMenuItem.addActionListener(e -> {
			// Open about us window only one time
			if (aboutUsWindow == null) {
				aboutUsWindow = new AboutUs(languageManager.getCurrentLocale());
				aboutUsWindow.setVisible(true);
				aboutUsWindow.addWindowListener(new WindowAdapter() {
					// set to null the aboutUs window event
					@Override
					public void windowClosed(WindowEvent e) {
						aboutUsWindow = null;
					}
				});

			}
		});
		helpMenu.add(aboutUsMenuItem);

		setJMenuBar(menuBar);

		// Header
		headerPanel = new JPanel();
		headerPanel.setBackground(Color.gray);
		URL imgUrlLabel = getClass().getClassLoader().getResource("resources/ca.png");
		imageLabel = new JLabel(new ImageIcon(imgUrlLabel)); // Image from resources folder
		headerPanel.add(imageLabel);
		contentPane.add(headerPanel, BorderLayout.NORTH);

		// Main Panel
		cellularAutomataPanel = new CellularAutomataPanel();
		contentPane.add(cellularAutomataPanel, BorderLayout.CENTER);

		// Footer
		JPanel footerPanel = new JPanel();
		binaryInput = new JTextField(8);
		btnSet = new JButton();

		// Result Panel
		resultPanel = new JPanel();
		resultPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		resultPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// New model Label to show the information
		modelLabel = new JLabel();
		modelLabel.setBackground(new Color(0, 0, 0));
		modelLabel.setMinimumSize(new Dimension(100, 30));
		modelLabel.setText(bundle.getString("modelLabel"));
		// Insert it into the Result Panel
		resultPanel.add(modelLabel);
		// New number label that shows the decimal number on the screen
		numberLabel = new JLabel();
		// Insert the number label into the Result Panel
		resultPanel.add(numberLabel);

		// Set Button
		btnSet.addActionListener(e -> {
			String binaryRule = binaryInput.getText();
			if (binaryRule.matches("^[01]{8}$")) { // Rule to check if the input is only 8 characters and those are 0
													// and 1
				cellularAutomataPanel.setRule(binaryRule);
				cellularAutomataPanel.updateMatrix();
				numberLabel.setText(Integer.toString(Integer.parseInt(binaryRule, 2)));
			} else {
				JOptionPane.showMessageDialog(null, bundle.getString("invalidBinaryMessage")); // [10]
			}
		});

		// Return button
		btnReturn = new JButton(bundle.getString("btnReturn"));
		btnReturn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				CSModel mainFrame = new CSModel();
				mainFrame.setVisible(true);
			}
		});

		// Increment button
		btnIncrement = new JButton("+");
		btnIncrement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				incrementBinaryValue();						// Increments by one
			}
		});

		// Decrement button
		btnDecrement = new JButton("-");
		btnDecrement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				decrementBinaryValue();						// decrements by one
			}
		});

		// binary rule label
		binaryRuleLabel = new JLabel(bundle.getString("binaryRuleLabel"));

		// Adding the elements into the footer Panel
		footerPanel.setLayout(new BorderLayout());
		// New JPanel to add the buttons and all the components to calculate the model
		JPanel calculusComponent = new JPanel();
		calculusComponent.add(binaryRuleLabel);
		calculusComponent.add(binaryInput);
		calculusComponent.add(btnIncrement);
		calculusComponent.add(btnDecrement);
		calculusComponent.add(btnSet);
		calculusComponent.add(resultPanel);
		JPanel returnComponent = new JPanel();
		// Add the Return button on the footer and insert it on the right side of the
		// screen
		returnComponent.add(btnReturn);
		footerPanel.add(returnComponent, BorderLayout.EAST);
		// Add the others buttons on the footer
		footerPanel.add(calculusComponent, BorderLayout.CENTER);
		contentPane.add(footerPanel, BorderLayout.SOUTH);

		setContentPane(contentPane);
		pack();

		// Initialize tool tip and UI
		updateTooltips();
		refreshUI();

	}

	/**
	 * Increments the binary number provided by the user by 1.
	 */
	private void incrementBinaryValue() {
		try {
			int currentValue = Integer.parseInt(binaryInput.getText(), 2);
			currentValue++;
			if (currentValue > 255) {
				currentValue = 255;
				JOptionPane.showMessageDialog(null, bundle.getString("greaterBinaryMessage"));
			}
			String binaryValue = Integer.toBinaryString(currentValue);
			while (binaryValue.length() < 8 ) {
				binaryValue = "0" + binaryValue;
			}
			binaryInput.setText(binaryValue);
			updateResult(binaryValue);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, bundle.getString("invalidBinaryMessage"));
		}
	}

	/**
	 * Decrements the binary number provided by the user by 1.
	 */
	private void decrementBinaryValue() {
		try {
			int currentValue = Integer.parseInt(binaryInput.getText(), 2);
			currentValue--;
			if (currentValue < 0) {
				currentValue = 0;
				JOptionPane.showMessageDialog(null, bundle.getString("lesserBinaryMessage"));
			}
			String binaryValue = Integer.toBinaryString(currentValue);
			while (binaryValue.length() < 8) {
				binaryValue = "0" + binaryValue;
			}
			binaryInput.setText(binaryValue);
			updateResult(binaryValue);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, bundle.getString("invalidBinaryMessage"));
		}
	}

	/**
	 * Updates the cellularAutomata Panel based on the rule and the information provided by the user
	 * 
	 * @param binaryValue The binary value representing the rule to be applied.
	 */
	private void updateResult(String binaryValue) {
		cellularAutomataPanel.setRule(binaryValue);			// Sends the binary value to the CA Panel
		cellularAutomataPanel.updateMatrix();				// Panel updates based on value provided
		numberLabel.setText(Integer.toString(Integer.parseInt(binaryValue, 2))); // Provides decimal representation of binary number
	}

	/**
	 * Refreshes the user interface elements to reflect the current language settings.
	 */
	private void refreshUI() {
		if (bundle != null) {
			// Title
			setTitle(bundle.getString("titleLabel"));

			// Footer
			btnSet.setText(bundle.getString("setButton"));
			modelLabel.setText(bundle.getString("modelLabel"));
			btnReturn.setText(bundle.getString("btnReturn"));
			binaryRuleLabel.setText(bundle.getString("binaryRuleLabel"));

			// Menu Items
			mainMenu.setText(bundle.getString("menuBar"));
			languageSubMenu.setText(bundle.getString("languageBar"));
			helpMenu.setText(bundle.getString("helpBar"));
			englishMenuItem.setText(bundle.getString("englishBar"));
			spanishMenuItem.setText(bundle.getString("spanishBar"));
			helpMenuItem.setText(bundle.getString("helpBar"));
			aboutUsMenuItem.setText(bundle.getString("aboutUsBar"));

			// Tool tip
			updateTooltips();
		}

	}

	/**
	 * Updates the tooltips for various UI components to provide additional information to the user.
	 */
	private void updateTooltips() {

		btnIncrement.setToolTipText(bundle.getString("tooltipBtnIncrement"));
		btnDecrement.setToolTipText(bundle.getString("tooltipBtnDecrement"));
		btnSet.setToolTipText(bundle.getString("tooltipBtnSet"));
		btnReturn.setToolTipText(bundle.getString("tooltipBtnReturn"));
		languageSubMenu.setToolTipText("tooltipLanguageSubMenuItem");
		englishMenuItem.setToolTipText(bundle.getString("tooltipEnglishMenuItem"));
		spanishMenuItem.setToolTipText(bundle.getString("tooltipSpanishMenuItem"));
		aboutUsMenuItem.setToolTipText(bundle.getString("tooltipAboutUsMenuItem"));
		helpMenuItem.setToolTipText(bundle.getString("tooltipHelpMenuItem"));

	}


}

