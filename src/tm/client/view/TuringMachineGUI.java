package tm.client.view;

import tm.client.model.TuringMachineClientModel;
import tm.client.controller.ClientController;
import tm.client.model.TransitionRule;

import javax.swing.*;

import support.HelpTM;
import support.LanguageManager;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The TuringMachineGUI class is a graphical user interface for interacting with
 * a Turing machine. It allows users to input transition rules, control the
 * Turing machine's operations, and view its state.
 */
public class TuringMachineGUI extends JFrame {
	/**
	 * Serial version UID for serialization.
	 */
	private static final long serialVersionUID = 6L;
	/**
	 * Reference of the Turing machine client Model Class
	 */
	private TuringMachineClientModel turingMachine;
	/**
	 * Reference of the Client Controller Class
	 */
	private ClientController controller;
	/**
	 * GUI component: Area for displaying game state messages
	 */
	private JTextArea gameStateTextArea;
	/**
	 * GUI component: Field for inputting transition rules
	 */
	private JTextField transitionRuleTextField;
	/**
	 * GUI component: Field for inputting the tape
	 */
	private JTextField tapeInputField;
	/**
	 * Thread for running the Turing machine
	 */
	private Thread runThread;
	/**
	 * Flag to indicate if the machine is running
	 */
	private volatile boolean running = false;
	/**
	 * Resource bundle for internationalization
	 */
	private ResourceBundle bundle;
	/**
	 * Language manager instance
	 */
	private LanguageManager languageManager = LanguageManager.getInstance();
	/**
	 * Link to icon resource
	 */
	private String icoLink = "resources/AC-logo.png";
	/**
	 * Link to image resource
	 */
	private String imageLink = "resources/tm.png";
	/**
	 * GUI component: Set Button
	 */
	private JButton setButton;
	/**
	 * GUI component: Step Button
	 */
	private JButton stepButton;
	/**
	 * GUI component: Reset Button
	 */
	private JButton resetButton;
	/**
	 * GUI component: Run Button
	 */
	private JButton runButton;
	/**
	 * GUI component: Stop Button
	 */
	private JButton stopButton;
	/**
	 * GUI component: Clean Button
	 */
	private JButton cleanButton;
	/**
	 * GUI component of the Transition Label
	 */
	private JLabel transitionRuleLabel;
	/**
	 * GUI component: Tape Label
	 */
	private JLabel tapeLabel;
	/**
	 * GUI component: Language Menu
	 */
	private JMenu languageMenu;
	/**
	 * GUI component: Help Menu
	 */
	private JMenu helpMenu;
	/**
	 * GUI component: Help Menu Item
	 */
	private JMenuItem helpMenuItem;
	/**
	 * GUI component: About Us Menu Item
	 */
	private JMenuItem aboutUsMenuItem;
	/**
	 * GUI component: English Item
	 */
	private JMenuItem englishItem;
	/**
	 * GUI component: English Item
	 */
	private JMenuItem spanishItem;
	/**
	 * Holds the help window object
	 */
	private HelpTM helpWindow = null;

	/**
	 * Holds the about us window object
	 */
	private AboutUsTM aboutUsWindow = null;

	/**
	 * Default Constructor (Here is not used it)
	 */
	public TuringMachineGUI() {
	}

	/**
	 * Constructor that initializes the GUI with specified transition rules and a
	 * controller.
	 *
	 * @param transitionRule The initial transition rule.
	 * @param controller     The controller for Turing machine operations.
	 */
	public TuringMachineGUI(String transitionRule, ClientController controller) {
		this.controller = controller;
		bundle = languageManager.getBundle();
		// Initialize the Turing machine with an empty tape and initial state
		turingMachine = new TuringMachineClientModel("", 0);

		// Set the state listener using an anonymous class instead of a lambda
		// expression
		turingMachine.setStateListener(new TuringMachineClientModel.TuringMachineStateListener() {
			@Override
			public void onStateUpdated(String tape, int headPosition, int currentState) {
				updateGameState(); // Updates the text area with the current tape state
			}

			@Override
			public void onFinalStateReached(String tape, int headPosition, int currentState) {
				// This method is no longer needed if we are updating the final state only
				// when the stop button is pressed.
				// It can be left empty or removed if we are not using now, but if we need to
				// change the code, we can modify it.
			}
		});

		initializeWindow(transitionRule); // Initialize the GUI window with the given transition rule
	}

	/**
	 * Initializes the GUI window with the specified transition rule.
	 * 
	 * @param transitionRule The transition rule to be displayed in the GUI.
	 */
	private void initializeWindow(String transitionRule) {
		// Configure the GUI window
		setIconImage(new ImageIcon(getClass().getClassLoader().getResource(icoLink)).getImage()); // Set the window icon
		setTitle(bundle.getString("windowTitle")); // Set the window title
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set close operation
		setSize(580, 600); // Set the size of the window
		getContentPane().setLayout(new BorderLayout()); // Set the layout of the content pane

		initializeMenu(); // Initialize the language menu

		// Create header panel
		JPanel header = new JPanel(new BorderLayout());

		// Initialize the header with the image, transition rule label and input field,
		// and tape label and input field
		JPanel header1 = initializeHeader(transitionRule);
		header.add(header1, BorderLayout.CENTER); // Add to the center of the header panel

		// Create and add the button panel with "Set", "Step", "Reset", "Run", and
		// "Stop" buttons
		JPanel buttonPanel = createButtonPanel();
		header.add(buttonPanel, BorderLayout.SOUTH); // Add to the bottom of the header panel

		// Create and configure the gameStateTextArea for displaying the state of the
		// Turing Machine
		gameStateTextArea = new JTextArea(20, 50);
		gameStateTextArea.setEditable(false); // Set to non-editable
		gameStateTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Set the font
		JScrollPane gameStateScrollPane = new JScrollPane(gameStateTextArea); // Wrap in a scroll pane

		// Add the header and gameState scroll pane to the frame
		getContentPane().add(header, BorderLayout.NORTH); // Add header to the top
		getContentPane().add(gameStateScrollPane, BorderLayout.CENTER); // Add gameState scroll pane to the center

		setVisible(true); // Make the window visible
	}

	/**
	 * Initializes the header panel of the Turing Machine GUI. This method sets up
	 * the top part of the GUI, including images and input fields for transition
	 * rules.
	 *
	 * @param transitionRule The initial transition rule to be displayed in the GUI.
	 * @return A JPanel representing the header of the GUI.
	 */
	private JPanel initializeHeader(String transitionRule) {
		JPanel headerPanel = new JPanel(new BorderLayout()); // Create a new JPanel with BorderLayout

		// Add the Turing Machine image to the top (North) of the header panel
		headerPanel.add(new JLabel(new ImageIcon(getClass().getClassLoader().getResource(imageLink))),
				BorderLayout.NORTH);

		// Add the first row of the header, containing the transition rule label and
		// input field, to the center
		headerPanel.add(headerRow1(transitionRule), BorderLayout.CENTER);

		// Add the second row of the header (implemented in another method) to the
		// bottom (South)
		headerPanel.add(headerRow2(), BorderLayout.SOUTH);

		return headerPanel; // Return the fully constructed header panel
	}

	/**
	 * Creates the first row of the header panel for the Turing Machine GUI. This
	 * row includes a label and a text field for entering the transition rule.
	 *
	 * @param transitionRule The initial transition rule to be displayed in the text
	 *                       field.
	 * @return A JPanel representing the first row of the header.
	 */
	private JPanel headerRow1(String transitionRule) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Create a new panel with centered flow layout

		// Create and configure the transition rule label
		transitionRuleLabel = new JLabel(bundle.getString("tRuleLabel")); // Initialize the label with localized text
		transitionRuleLabel.setHorizontalAlignment(SwingConstants.LEFT); // Align the label text to the left

		// Create and configure the transition rule text field
		transitionRuleTextField = new JTextField(transitionRule, 20); // Initialize the text field with the transition
																		// rule

		// Add the label and text field to the panel
		panel.add(transitionRuleLabel);
		panel.add(transitionRuleTextField);

		return panel; // Return the panel containing the transition rule label and text field
	}

	/**
	 * Creates the second row of the header panel for the Turing Machine GUI. This
	 * row includes a label and a text field for entering the tape input.
	 *
	 * @return A JPanel representing the second row of the header.
	 */
	private JPanel headerRow2() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Create a new panel with centered flow layout

		// Create and configure the tape input label
		tapeLabel = new JLabel(bundle.getString("tapeInputLabel")); // Initialize the label with localized text for the
																	// tape input

		// Create and configure the tape input text field
		tapeInputField = new JTextField("0000000000000000000000000", 25); // Initialize the text field with default tape
																			// content

		// Add the label and text field to the panel
		panel.add(tapeLabel); // Add the tape input label to the panel
		panel.add(tapeInputField); // Add the tape input text field to the panel

		return panel; // Return the panel containing the tape input label and text field
	}

	/**
	 * Creates a panel containing control buttons for the Turing Machine GUI. This
	 * panel includes buttons for setting up the machine, stepping through
	 * execution, resetting, running, stopping, and cleaning the state display.
	 *
	 * @return A JPanel containing the control buttons.
	 */
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(); // Initialize a new panel for buttons

		// Initialize buttons with labels from the resource bundle
		setButton = new JButton(bundle.getString("setButtonLabel"));
		stepButton = new JButton(bundle.getString("stepButtonLabel"));
		resetButton = new JButton(bundle.getString("resetButtonLabel"));
		runButton = new JButton(bundle.getString("runButtonLabel"));
		stopButton = new JButton(bundle.getString("stopButtonLabel"));
		cleanButton = new JButton(bundle.getString("cleanButtonLabel"));

		// Add action listeners to each button
		setButton.addActionListener(e -> {
			// Handle setting up new Turing Machine configurations
			controller.handleNewTMButton(transitionRuleTextField.getText(), bundle);
			String tape = tapeInputField.getText();
			if (!controller.validateTape(tape)) {
				// Show error message if tape is invalid
				JOptionPane.showMessageDialog(this, bundle.getString("tapeErrorMessage"),
						bundle.getString("tapeErrorMessage2"), JOptionPane.ERROR_MESSAGE);
			} else {
				// Apply transition rule, reset tape, initialize game state, and enable buttons
				applyTransitionRule();
				resetTape();
				turingMachine.setStepCount(0);
				initializeGameState();
				enableActionButtons(true);
			}
		});

		stepButton.addActionListener(e -> {
			// Execute one step of the Turing Machine and update game state
			turingMachine.executeStep();
			updateGameState();
		});

		resetButton.addActionListener(e -> {
			// Stop running, reset tape, clean game state area, and disable action buttons
			stopRunning();
			resetTape();
			cleanGameStateArea();
			enableActionButtons(false);
		});

		runButton.addActionListener(e -> startRunning()); // Start continuous execution
		stopButton.addActionListener(e -> stopRunning()); // Stop continuous execution
		cleanButton.addActionListener(e -> cleanGameStateArea()); // Clean the game state display

		// Add buttons to the panel
		panel.add(setButton);
		panel.add(runButton);
		panel.add(stepButton);
		panel.add(stopButton);
		panel.add(resetButton);
		panel.add(cleanButton);

		enableActionButtons(false); // Initially disable some action buttons
		return panel; // Return the panel with all control buttons
	}

	/**
	 * Initializes the language selection and help menus in the Turing Machine GUI.
	 * This method sets up menus for choosing the application's language and
	 * accessing help resources.
	 */
	private void initializeMenu() {
		JMenuBar menuBar = new JMenuBar(); // Initialize a menu bar

		// Language Menu
		languageMenu = new JMenu(bundle.getString("languageMenu")); // Create a language menu

		// Create menu items for English and Spanish language options
		englishItem = new JMenuItem("English");
		spanishItem = new JMenuItem("Español");

		// Add action listeners to change the application's language
		englishItem.addActionListener(e -> changeLanguage("English"));
		spanishItem.addActionListener(e -> changeLanguage("Español"));

		// Add language options to the language menu
		languageMenu.add(englishItem);
		languageMenu.add(spanishItem);
		menuBar.add(languageMenu); // Add the language menu to the menu bar

		// Help Menu
		helpMenu = new JMenu(bundle.getString("helpBar")); // Create a help menu

		// Help menu item for general help
		helpMenuItem = new JMenuItem(bundle.getString("helpBar"));
		helpMenuItem.addActionListener(e -> {
			// Open the help window only once; create new if it doesn't exist
			if (helpWindow == null) {
				helpWindow = new HelpTM(languageManager.getCurrentLocale());
				helpWindow.setVisible(true);
				// Add a listener to reset the helpWindow reference when closed
				helpWindow.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						helpWindow = null;
					}
				});
			}
		});
		helpMenu.add(helpMenuItem); // Add the help item to the help menu

		// About Us menu item
		aboutUsMenuItem = new JMenuItem(bundle.getString("aboutUsBar"));
		aboutUsMenuItem.addActionListener(e -> {
			// Open the About Us window only once; create new if it doesn't exist
			if (aboutUsWindow == null) {
				aboutUsWindow = new AboutUsTM(languageManager.getCurrentLocale());
				aboutUsWindow.setVisible(true);
				// Add a listener to reset the aboutUsWindow reference when closed
				aboutUsWindow.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						aboutUsWindow = null;
					}
				});
			}
		});
		helpMenu.add(aboutUsMenuItem); // Add the About Us item to the help menu
		menuBar.add(helpMenu); // Add the help menu to the menu bar

		setJMenuBar(menuBar); // Set the menu bar to the JFrame
	}

	/**
	 * Changes the application's language and refreshes the user interface. This
	 * method sets the new language using the LanguageManager and updates the user
	 * interface to reflect the change.
	 *
	 * @param language The new language to be set (e.g., "English" or "Español").
	 */
	private void changeLanguage(String language) {
		languageManager.setLanguage(language); // Sets the new language using the LanguageManager
		bundle = languageManager.getBundle(); // Retrieves the resource bundle for the new language
		refreshUI(); // Refreshes the user interface to reflect the language change
	}

	/**
	 * Refreshes the user interface elements to reflect the current language
	 * settings. This method updates the texts of all components that require
	 * internationalization, ensuring that the interface aligns with the selected
	 * language.
	 */
	private void refreshUI() {
		setTitle(bundle.getString("windowTitle"));
		transitionRuleLabel.setText(bundle.getString("tRuleLabel"));
		tapeLabel.setText(bundle.getString("tapeInputLabel"));
		setButton.setText(bundle.getString("setButtonLabel"));
		stepButton.setText(bundle.getString("stepButtonLabel"));
		resetButton.setText(bundle.getString("resetButtonLabel"));
		runButton.setText(bundle.getString("runButtonLabel"));
		stopButton.setText(bundle.getString("stopButtonLabel"));
		cleanButton.setText(bundle.getString("cleanButtonLabel"));

		languageMenu.setText(bundle.getString("languageMenu"));
		englishItem.setText(bundle.getString("englishBar"));
		spanishItem.setText(bundle.getString("spanishBar"));
		helpMenuItem.setText(bundle.getString("helpBar"));
		helpMenu.setText(bundle.getString("helpBar"));
		aboutUsMenuItem.setText(bundle.getString("aboutUsBar"));
	}

	/**
	 * Clears the game state text area. This method is used to reset the display
	 * area for the Turing machine's state, removing any previous content.
	 */
	private void cleanGameStateArea() {
		gameStateTextArea.setText(""); // Clears the game state text area.
	}

	/**
	 * Applies the transition rules entered by the user. This method reads the
	 * transition rules from the input field, converts them into a list of
	 * TransitionRule objects, and then applies these rules to the Turing machine.
	 * It also resets the tape and sets the initial state of the Turing machine to
	 * reflect these changes.
	 */
	private void applyTransitionRule() {
		String rulesText = transitionRuleTextField.getText();
		List<TransitionRule> rules = TransitionRule.fromString(rulesText);
		turingMachine.setTransitionRules(rules);
		turingMachine.resetTape(tapeInputField.getText());
		turingMachine.setCurrentState(1); // Sets the initial state to 1
		updateGameState(); // Updates the GUI to reflect the new state
	}

	/**
	 * Resets the tape of the Turing machine. This method takes the initial tape
	 * content from the tape input field and applies it to the Turing machine,
	 * effectively resetting its tape to the specified content.
	 */
	private void resetTape() {
		String initialTape = tapeInputField.getText();
		turingMachine.resetTape(initialTape);
	}

	/**
	 * Initializes the game state. This method sets up the initial state of the game
	 * by fetching the transition rule and the initial tape configuration, and then
	 * displaying these details in the game state text area.
	 */
	private void initializeGameState() {
		String card = transitionRuleTextField.getText();
		String initialTape = turingMachine.getTapeWithHeadPosition();
		gameStateTextArea.setText("Card: " + card + "\nInitial tape (head position between brackets):\n" + initialTape
				+ "\n\nGame started\n");
	}

	/**
	 * Updates the game state area with each step of the Turing machine. This method
	 * is invoked to reflect the current state of the Turing machine in the GUI. It
	 * shows the current tape configuration, head position, current step, and other
	 * relevant details in the game state text area.
	 */
	private void updateGameState() {
		SwingUtilities.invokeLater(() -> {
			int headPosition = turingMachine.getHeadPosition();
			String tapeWithHead = turingMachine.getTapeWithHeadPosition();
			int currentStep = turingMachine.getCurrentStep();
			// Fetching the symbols at the current head position
			char a = turingMachine.getSymbolAt(headPosition);
			char b = turingMachine.getSymbolAt(headPosition);
			char c = turingMachine.getSymbolAt(headPosition);
			char d = turingMachine.getSymbolAt(headPosition);
			char e = turingMachine.getSymbolAt(headPosition);

			// Formatting the state information to be displayed in the GUI
			String stateInfo = String.format(
					"Step=%d, Tapepos=%d, FinalState=%d: a[%d]=%c, b[%d]=%c, c[%d]=%c, d[%d]=%c, e[%d]=%c", currentStep,
					headPosition, turingMachine.getCurrentState(), headPosition, a, headPosition, b, headPosition, c,
					headPosition, d, headPosition, e);

			gameStateTextArea.append(stateInfo + "\n");
			gameStateTextArea.append(tapeWithHead + "\n");
			gameStateTextArea.setCaretPosition(gameStateTextArea.getDocument().getLength());
		});
	}

	/**
	 * Starts continuous execution of the Turing machine. This method is invoked to
	 * run the Turing machine in a separate thread, allowing it to execute multiple
	 * steps automatically without user intervention. The Turing machine will
	 * continue to execute steps until the `stopRunning` method is called or the
	 * thread is interrupted.
	 */
	private void startRunning() {
		// Only start the thread if it's not already running
		if (!running) {
			running = true; // Mark the Turing machine as running
			// Create and start a new thread for the continuous execution
			runThread = new Thread(() -> {
				while (running) {
					// Execute a single step of the Turing machine
					turingMachine.executeStep();
					// Update the GUI to reflect the new state
					updateGameState();
					try {
						// Pause the execution for 1 second to allow the user to observe changes
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// Exit the loop if the thread is interrupted (e.g., when stopping execution)
						break;
					}
				}
			});
			runThread.start(); // Start the thread
		}
	}

	/**
	 * Stops the continuous execution of the Turing machine. This method is invoked to interrupt the
	 * execution thread, effectively halting the automatic step execution initiated by the startRunning method.
	 */
	private void stopRunning() {
	    if (running == true) {
	    	updateFinalStateDisplay(); // Update the GUI to display the final tape configuration
	    }
		running = false; // Mark the Turing machine as not running
	    if (runThread != null) {
	        runThread.interrupt(); // Interrupt the execution thread
	    }
	}

	/**
	 * Updates the GUI to display the final configuration of the tape. This method is called when the Turing
	 * machine stops running, either automatically when a final state is reached or manually by the user.
	 */
	private void updateFinalStateDisplay() {
	    SwingUtilities.invokeLater(() -> {
	        String finalTapeConfig = turingMachine.getTapeWithHeadPosition(); // Get the final tape configuration
	        gameStateTextArea.append("Final tape config is:\n" + finalTapeConfig + "\n"); // Append the final tape config to the text area
	        gameStateTextArea.setCaretPosition(gameStateTextArea.getDocument().getLength()); // Scroll to the bottom of the text area
	    });
	}

	/**
	 * Enables or disables action buttons based on the specified parameter.
	 * This method is used to control the state of the GUI buttons, typically
	 * to prevent the user from performing certain actions while the Turing machine
	 * is running.
	 *
	 * @param enable A boolean value indicating whether to enable or disable the buttons.
	 */
	public void enableActionButtons(boolean enable) {
	    runButton.setEnabled(enable);    // Enable or disable the "Run" button
	    stepButton.setEnabled(enable);   // Enable or disable the "Step" button
	    stopButton.setEnabled(enable);   // Enable or disable the "Stop" button
	    resetButton.setEnabled(enable);  // Enable or disable the "Reset" button
	    cleanButton.setEnabled(enable);  // Enable or disable the "Clean" button
	}

}
