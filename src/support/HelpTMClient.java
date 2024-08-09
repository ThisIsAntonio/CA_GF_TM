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

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
/**
 * Help class represents a JFrame that provides to the user information about how this app works.
 * 
 * @author David Burchat, Marcos Astudillo Carrasco
 * @version 1.0
 * @since 1.8
 * 
 */
public class HelpTMClient extends JFrame {
	
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
	 * Constructor for the Help class. Initializes the UI components and sets up
	 * event listeners.
	 * @param locale The current language being used by the application
	 */
	public HelpTMClient(Locale locale) {
        // Load the appropriate ResourceBundle
	    if (locale == null) {											// check if the locale language is null
	        locale = languageManager.getCurrentLocale();				// If null, set the language in English
	    }

		bundle = languageManager.getBundle();							// bundle stores current language selected
        
        // Load the swing window
        setTitle(bundle.getString("titleHelpLabel"));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));				// Provide padding for window content
		setResizable(false); 											// doesn't allow the user maximize the window
		
		
		// Add AC logo to image icon
		ImageIcon icon = new ImageIcon("img/AC-logo.png");
		setIconImage(icon.getImage());
		
		// Help information
        JEditorPane helpText = new JEditorPane();						// JEditorPane, this works similar like a HTML
        																// And the text can be configured how a HTML document
        helpText.setContentType("text/html");
        helpText.setEditable(false);									// Text field is for information purposes. Not editable.
        helpText.setText("<html><body style='text-align: justify;'>" + bundle.getString("helpInfoTMClient") + "</body></html>");
        
        // Set size of JScrollPane
        JScrollPane jscHelp = new JScrollPane(helpText);			// Insert aboutText information in a ScrollPane
        jscHelp.setPreferredSize(new Dimension(400,240));
        contentPane.add(jscHelp, BorderLayout.CENTER);

		setContentPane(contentPane);
	}

}

