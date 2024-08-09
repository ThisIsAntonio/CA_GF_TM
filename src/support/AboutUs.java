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
package support;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 * AboutUs class represents a JFrame that provides to the user information about (programmers).
 * 
 * @author David Burchat, Marcos Astudillo Carrasco
 * @version 1.0
 * @since 1.8
 * 
 */
public class AboutUs extends JFrame {

	/**
	 * Unique identifier for the serialized class.
	 */
	private static final long serialVersionUID = 3L;

	/**
	 * Holds a content pane from JPanel
	 */
	private JPanel contentPane;
	
	/**
	 * Holds a language dictionary
	 */
	private ResourceBundle bundle;

	/**
	 * Holds an instance of the Language Manager
	 */
	private LanguageManager languageManager = LanguageManager.getInstance();


	/**
	 * Constructor for the AboutUs class. Initializes the UI components and sets up event listeners.
	 * @param locale The current language being used by the application
	 */
	public AboutUs(Locale locale) {
		// Load the appropriate ResourceBundle
		if (locale == null) { // check if the locale language is null
			locale = languageManager.getCurrentLocale(); // If null, set the language in English
		}

		bundle = languageManager.getBundle(); // Get the language that is selected

		// Load the swing window
		setTitle(bundle.getString("titleAboutUsLabel"));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Create padding for window content
		setResizable(false); // doesn't allow the user maximize the window

		// About us information
		JEditorPane aboutText = new JEditorPane(); // JEditorPane, this works similar like a HTML
													// And the text can be configured how a HTML document
		aboutText.setContentType("text/html");
		aboutText.setEditable(false);				// Cannot edit text box, for display purposes only
		aboutText.setText(
				"<html><body style='text-align: justify;'>" + bundle.getString("aboutUsInfoTMServer") + "</body></html>");

		// Set size of JScrollPane
		JScrollPane jscAboutUs = new JScrollPane(aboutText); // Insert aboutText information in a ScrollPane
		jscAboutUs.setPreferredSize(new Dimension(400, 240));
		contentPane.add(jscAboutUs, BorderLayout.CENTER);

		setContentPane(contentPane);
	}

}
