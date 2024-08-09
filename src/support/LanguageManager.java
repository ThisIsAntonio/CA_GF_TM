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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * LanguageManager is a Singleton class responsible for managing the language
 * settings of the application. It provides functionalities to set and update
 * the language, and to retrieve the current locale and resource bundle.
 *
 * @author David Burchat, Marcos Astudillo Carrasco
 * @version 1.0
 * @since 1.8
 */
public class LanguageManager {
	
	/**
	 * Singleton instance of the LanguageManager
	 */
	private static LanguageManager instance;
	
	/**
	 * Resource bundle for language-specific strings
	 */
	private ResourceBundle bundle;
	
	/**
	 * Current locale setting (What language is currently being used)
	 */
	private Locale currentLocale;
	
	/**
	 * Private constructor to ensure only one instance is created. Initializes the language settings.
	 */
	private LanguageManager() {
		updateLanguage();
	}

	/**
	 * Provides access to the singleton instance of LanguageManager. If the instance
	 * doesn't exist, it initializes it.
	 *
	 * @return The singleton instance of LanguageManager.
	 */
	public static LanguageManager getInstance() {
		if (instance == null) {
			instance = new LanguageManager();
		}
		return instance;
	}

	/**
	 * Sets the language of the interface based on the user's selection.
	 *
	 * @param language The language that the user has selected.
	 */
	public void setLanguage(String language) {
		if ("Español".equals(language)) {
			Locale.setDefault(new Locale("es")); // Set default locale to Spanish
		} else {
			Locale.setDefault(Locale.ENGLISH); // Set default locale to English
		}
		updateLanguage();
	}

	/**
	 * Updates the language settings based on the current locale. Loads the appropriate resource bundle file.
	 */
	private void updateLanguage() {
		currentLocale = Locale.getDefault();
		try {
			// Load the resource bundle file based on the current language
			String resourceName = "resources/messages_" + currentLocale.getLanguage() + ".properties";
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);
			if (inputStream == null) {
				throw new FileNotFoundException("Resource not found" + resourceName);
			}
			bundle = new PropertyResourceBundle(inputStream);
		} catch (IOException e) {
			e.printStackTrace(); // Print the exception stack trace
		}
	}

	/**
	 * Retrieves the current resource bundle.
	 *
	 * @return The current resource bundle.
	 */
	public ResourceBundle getBundle() {
		return bundle;
	}

	/**
	 * Retrieves the current locale setting.
	 *
	 * @return The current locale.
	 */
	public Locale getCurrentLocale() {
		return currentLocale;
	}
}

