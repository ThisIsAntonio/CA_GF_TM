/*
 * Student Names: David Burchat & Marcos Astudillo Carrasco
 * Student Number: 040513895 & 041057439
 * Course: CST8221 Java Application Programming
 * Assignment: A12
 * Lab Section: 301
 * Program: CET-CS Level 4
 * Professor: Paulo Sousa Ph.D
 * Due Date: Oct/1/2023
 * References: 
 * 	[1] Wikipedia, "Rule 30," 18 Aug 2023. [Online]. Available: https://en.wikipedia.org/wiki/Rule_30. 
 * 	    [Accessed Sep 15 2023]
 *  [2] E. W. Weisstein, ""Rule 90." From MathWorld--A Wolfram Web Resource.," [Online]. 
 *      Available: https://mathworld.wolfram.com/Rule90.html. [Accessed Sep 15 2023]
 *  [3] E. W. Weisstein, ""Elementary Cellular Automaton." From MathWorld--A Wolfram Web Resource.," [Online]. 
 *      Available: https://mathworld.wolfram.com/ElementaryCellularAutomaton.html. [Accessed Sep 15 2023]
 *  [4] R. Sedgewick and K. Wayne, "Cellular.java Rule 90," 10 Aug 2022. [Online]. 
 *      Available: https://introcs.cs.princeton.edu/java/53universality/Cellular.java.html. [Accessed Sep 15 2023]
 *  [5] P. Suraj, "Geeks for Geeks," 09 May 2022. [Online]. 
 *      Available: https://www.geeksforgeeks.org/cellular-automaton-discrete-model/. [Accessed Sep 15 2023]
 *  [6] A. Bhayani, "Pseudorandom numbers using Rule 30," 13 Feb 2020. [Online]. 
 *      Available: https://www.codementor.io/@arpitbhayani/pseudorandom-numbers-using-rule-30-13lgj35dfu. [Accessed Sep 15 2023]
 *  [7] J. Nazario, "Reddit," 05 Jul 2015. [Online]. 
 *      Available: https://www.reddit.com/r/dailyprogrammer/comments/3jz8tt/20150907_challenge_213_easy_cellular_automata/. 
 *      [Accessed Sep 15 2023]
 *  [8] G. H. Barrionuevo, "wolfram-ca - Git Hub," 13 May 2019. [Online]. 
 *      Available: https://github.com/gustavohb/wolfram-ca/blob/master/src/WolframCAViewer.java. [Accessed Sep 15 2023]
 *  [9] "Como evitar que JFrame se abra mas de un vez - Foros del Web," 24 Aug 2013. [Online]. 
 *      Available: https://www.forosdelweb.com/f45/como-evitar-que-jframe-abra-mas-vez-1070644/. [Accessed Sep 15 2023]
 *  [10]L. Hernández, G. Martínez and S. Paredes , "Crear ventanas de diálogo utilizando librerías Java," 18 Sep 2017. [Online]. 
 *      Available: https://webs.um.es/ldaniel/iscyp17-18/07-funcionesInput.html. [Accessed Sep 15 2023]
 *  [11]J. Giesen, "Wolfram's Rule 30 Cellular Automaton," 2018. [Online]. 
 *      Available: http://www.jgiesen.de/rule30/index.html. [Accessed Sep 15 2023]
 *  [12]B. Tech, "Cómo saber si un JFrame está abierto en Java NetBeans," [Online]. 
 *      Available: https://byspel.com/como-saber-si-un-jframe-esta-abierto-en-java-netbeans/. [Accessed Sep 15 2023]
 *  [13]D. Shiffman, "The Nature of Code - Chapter 7. Cellular Automata," 13 Dec 2012. [Online]. 
 *      Available: https://natureofcode.com/book/chapter-7-cellular-automata/. [Accessed Sep 14 2023]
 *  [14] J. Henly, "How to capture a JFrame's close button click event? - Stackoverflow," 12 Dec 2017. [Online]. 
 *       Available: https://stackoverflow.com/questions/9093448/how-to-capture-a-jframes-close-button-click-event.
 *       [Accessed Sep 24 2023]
 */

package cs;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.CA;
import gl.view.GLMain;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import support.AboutUs;
import support.HelpCA;
import support.LanguageManager;
import tm.client.view.ClientGUI;
import tm.server.view.ServerGUI;

/**
 * This class is the starting point for the program. It holds the selection box,
 * allowing the user to choose what program / game to run.
 * 
 * @author David Burchat, Marcos Astudillo Carrasco
 * @version 1.0
 * @since 1.8
 */
public class CSModel extends JFrame {
	/**
	 * Unique identifier for the serialized clsss.
	 */
	private static final long serialVersionUID = 4L;

	/**
	 * Holds an instance of the Language Manager
	 */
	private LanguageManager languageManager = LanguageManager.getInstance();;

	/**
	 * Holds a content pane from JPanel
	 */
	private JPanel contentPane;

	/**
	 * Holds the close button object
	 */
	private JButton btnClose;

	/**
	 * Holds the confirm button object
	 */
	private JButton btnConfirm;

	/**
	 * Holds the combo box for program selection
	 */
	JComboBox<AssignmentItem> assignmentBox;

	/**
	 * Holds a language dictionary
	 */
	private ResourceBundle bundle;

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

	// ==================================================================================================

	/**
	 * The main method to launch the Main JFrame.
	 *
	 * @param args Command-line arguments (not used).
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CSModel frame = new CSModel(); // Will instantiate a new CSModel which is first window in the
													// program
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor for CSModel. Initializes the UI components and sets up event
	 * listeners.
	 */
	public CSModel() {
		// Initialize JavaFX Toolkit
		new JFXPanel();
		// create a bundle for the languages, default on launch is English
		bundle = languageManager.getBundle();

		// JFrame Main Window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Program will close if 'X' button is hit
		setBounds(100, 100, 410, 225);
		setResizable(false); // doesn't allow the user maximize the window, window size is locked
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Allows some padding for the window content

		setContentPane(contentPane);
		contentPane.setLayout(null); // initialize an empty content pane to use

		// Add AC logo to image icon "favicon"
		URL imgUrl = getClass().getClassLoader().getResource("resources/AC-logo.png");
		ImageIcon icon = new ImageIcon(imgUrl);
		setIconImage(icon.getImage());

		// Setup program image on the left hand side
		JLabel lblIco = new JLabel("");
		lblIco.setBounds(10, 10, 155, 110);

		// Try-catch to load the Image
		try {
			// Load the image directly from the path
			URL imgUrlLabel = getClass().getClassLoader().getResource("resources/CSmin.png");
			ImageIcon ico = new ImageIcon(imgUrlLabel);
			// Resize the logo
			ImageIcon img = new ImageIcon(
					ico.getImage().getScaledInstance(lblIco.getWidth(), lblIco.getHeight(), Image.SCALE_SMOOTH));
			// Set the resized logo to the JLabel
			lblIco.setIcon(img);
		} catch (Exception e) {
			System.err.println("Error loading image: " + e.getMessage());
		}

		// Add the program image to the main content pane
		contentPane.add(lblIco);

		// Setup the assignment box
		assignmentBox = new JComboBox<>();
		assignmentBox.setBounds(175, 48, 209, 48);
		updateAssignmentBox(); // This method adds the options to the assignment box
		contentPane.add(assignmentBox); // Add combo box to the main content pane

		// Buttons Pane
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(50, 120, 300, 50); // Coordinates and size of the button pane
		// Confirm Button
		btnConfirm = new JButton(bundle.getString("btnConfirm"));
		btnConfirm.setBounds(54, 131, 89, 23);
		btnConfirm.addActionListener(new ActionListener() { // Event for when the button is pressed
			@Override
			public void actionPerformed(ActionEvent e) {
				AssignmentItem selectedItem = (AssignmentItem) assignmentBox.getSelectedItem();
				String selection = selectedItem.getKey();
				// Check the user selection and open the project
				switch (selection) {
				case "CA":
					//Open Assignment A22 - Cellular Automata
					new CA().setVisible(true);
					break;
				// <========================== added =====================
				case "GL":
					// Open Assignment A22 - Game OF Live
					openGLWindow("GL");
					break;
				// <========================== added =====================
				case "TMS":
					// Open Assignment A32 - TM Server
					new ServerGUI().setVisible(true);
					break;
				// <========================== added =====================
				case "TMC":
					// Open Assignment A32 - TM Client
					new ClientGUI().setVisible(true);
					break;
				}
				if (selection == "00") {
					// Showing a message dialog
					JOptionPane.showMessageDialog(null, bundle.getString("messageDialogNoAssgSelected")); // [10]
				} else if (selection.equals("TMS") || selection.equals("TMC")) {
				} else {
					dispose(); // Close the window
				}
			}
		});

		buttonPane.add(btnConfirm); // Add confirmation button to button pane

		/*
		 * Cancel Button
		 */
		btnClose = new JButton(bundle.getString("btnClose"));
		btnClose.setBounds(155, 131, 89, 23);
		// waits for user action to close the window later
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				dispose(); // Close the window
			}
		});
		buttonPane.add(btnClose); // Add the close button to the button pane
		contentPane.add(buttonPane); // Add button pane to the main content pane

		// Menu bar
		menuBar = new JMenuBar();

		// Main Menu
		mainMenu = new JMenu(bundle.getString("menuBar"));
		menuBar.add(mainMenu);

		// Language Sub-Menu
		languageSubMenu = new JMenu(bundle.getString("languageBar"));
		mainMenu.add(languageSubMenu);

		// English item (inside the Language sub-menu)
		englishMenuItem = new JMenuItem(bundle.getString("englishBar"));
		englishMenuItem.addActionListener(e -> {
			languageManager.setLanguage("English");
			bundle = languageManager.getBundle(); // Update language bundle to English
			refreshUI();
		});
		languageSubMenu.add(englishMenuItem); // add item to menu

		// Spanish item (inside the Language sub-menu)
		spanishMenuItem = new JMenuItem(bundle.getString("spanishBar"));
		spanishMenuItem.addActionListener(e -> {
			languageManager.setLanguage("Español");
			bundle = languageManager.getBundle(); // Update language bundle to Spanish
			refreshUI();
		});
		languageSubMenu.add(spanishMenuItem); // add item to menu

		// Help Menu
		helpMenu = new JMenu(bundle.getString("helpBar"));
		menuBar.add(helpMenu); // add item to menu

		// Help Sub-Menu
		helpMenuItem = new JMenuItem(bundle.getString("helpBar"));
		helpMenuItem.addActionListener(e -> {
			// Only one help window will be open at one time [9][12]
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
		helpMenu.add(helpMenuItem); // add menu item to menu

		// About us sub-menu
		aboutUsMenuItem = new JMenuItem(bundle.getString("aboutUsBar"));
		aboutUsMenuItem.addActionListener(e -> {
			// Open about us window only one time [9][12]
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
		helpMenu.add(aboutUsMenuItem); // add menu item to menu

		setJMenuBar(menuBar);

		updateTooltips(); // Initialize tool tips
		refreshUI(); // Initialize UI and Language
	}

	/**
	 * Populates the Assignment JComboBox language based on the current locale /
	 * bundle.
	 */
	private void updateAssignmentBox() {
		// Clean JComboBox
		assignmentBox.removeAllItems();

		// Add the elements with the current language
		assignmentBox.addItem(new AssignmentItem("00", bundle.getString("selectAssignment")));
		assignmentBox.addItem(new AssignmentItem("CA", bundle.getString("CA"))); // Celular Automata
		assignmentBox.addItem(new AssignmentItem("GL", bundle.getString("GL"))); // Game of Life
		assignmentBox.addItem(new AssignmentItem("TMS", bundle.getString("TM"))); // Turing Machine Server
		assignmentBox.addItem(new AssignmentItem("TMC", bundle.getString("TMC"))); // TM Client
	}

	/**
	 * Refreshes the user interface elements to reflect the current language
	 * settings.
	 */
	private void refreshUI() {
		// Title
		setTitle(bundle.getString("windowTitle"));
		// Buttons
		btnClose.setText(bundle.getString("btnClose"));
		btnConfirm.setText(bundle.getString("btnConfirm"));
		// ComboBox and tool tip
		updateAssignmentBox();
		updateTooltips();
		// Menu Items
		mainMenu.setText(bundle.getString("menuBar"));
		languageSubMenu.setText(bundle.getString("languageBar"));
		englishMenuItem.setText(bundle.getString("englishBar"));
		spanishMenuItem.setText(bundle.getString("spanishBar"));
		helpMenu.setText(bundle.getString("helpBar"));
		helpMenuItem.setText(bundle.getString("helpBar"));
		aboutUsMenuItem.setText(bundle.getString("aboutUsBar"));
	}

	/**
	 * Updates the tool tips for various UI components to provide additional
	 * information to the user. Will update if language bundle is changed.
	 */
	private void updateTooltips() {
		assignmentBox.setToolTipText(bundle.getString("tooltipAssignmentBox"));
		btnConfirm.setToolTipText(bundle.getString("tooltipBtnConfirm"));
		btnClose.setToolTipText(bundle.getString("tooltipBtnCancel"));
		englishMenuItem.setToolTipText(bundle.getString("tooltipEnglishMenuItem"));
		spanishMenuItem.setToolTipText(bundle.getString("tooltipSpanishMenuItem"));
		aboutUsMenuItem.setToolTipText(bundle.getString("tooltipAboutUsMenuItem"));
		helpMenuItem.setToolTipText(bundle.getString("tooltipHelpMenuItem"));
	}

	// <========================== added
	// ========================================================
	/**
	 * Opens the OpenGL window and ensures it is executed on the JavaFX application
	 * thread.
	 * 
	 * @param id The identifier for the OpenGL window.
	 */
	private void openGLWindow(String id) {
		// Check if we are on the JavaFX application thread
		if (Platform.isFxApplicationThread()) {
			// If we are not on the JavaFX thread, use Platform.runLater to execute the code
			// on that thread
			Platform.runLater(() -> GLMain.launchGL()); // Call method in GL to display GLView window
		} else {
			// Execute the JavaFX code directly on this thread
			GLMain.launchGL(); // Call method in GL to display GLView window
		}
	}

}
