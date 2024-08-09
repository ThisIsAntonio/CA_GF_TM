package tm.client.view;

import javax.swing.*;

import support.HelpTMClient;
import support.LanguageManager;
import tm.client.controller.ClientController;
import tm.client.model.TuringMachineClientModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ResourceBundle;

/**
 * The ClientGUI class provides the user interface for the Turing Machine client
 * application. It allows users to connect to the server, send and receive
 * messages, and control the Turing Machine.
 */
public class ClientGUI extends JFrame {
	/**
	 * Unique serial version identifier for this class.
	 */
	private static final long serialVersionUID = 5L;

	/**
	 * Singleton instance of LanguageManager for handling internationalization.
	 */
	private LanguageManager languageManager = LanguageManager.getInstance();

	/**
	 * Resource bundle for internationalization.
	 */
	private ResourceBundle bundle;

	/**
	 * Controller component managing client-side logic.
	 */
	private ClientController clientController;

	/**
	 * Graphical interface for the Turing Machine functionality.
	 */
	private TuringMachineGUI tmGUI;

	/**
	 * Main panel containing all GUI components.
	 */
	private JPanel mainPanel;

	/**
	 * Text fields for user input such as username, server IP, port, and Turing
	 * machine input.
	 */
	private JTextField usernameTextField, serverIpTextField, portTextField, tmInputField;

	/**
	 * Buttons for initiating various actions like connect, end, create new Turing
	 * machine, send, receive, and run.
	 */
	private JButton connectButton, endButton, newTMButton, sendButton, receiveButton, runButton;

	/**
	 * Labels for displaying connection status and server information.
	 */
	private JLabel connectionStatusLabel, serverInfoLabel;

	/**
	 * Text area for displaying messages from the server.
	 */
	private JTextArea messageTextArea;

	/**
	 * Labels for user inputs.
	 */
	private JLabel userLabel, serverIpLabel, portLabel, tmInputLabel;

	/**
	 * Label for displaying server messages.
	 */
	private JLabel serverMessagesLabel;

	/**
	 * Menu for language selection.
	 */
	private JMenu languageMenu;

	/**
	 * Menu for help-related options.
	 */
	private JMenu helpMenu;

	/**
	 * Menu item under the help menu.
	 */
	private JMenuItem helpMenuItem, aboutUsMenuItem;

	/**
	 * Menu item for language selection.
	 */
	private JMenuItem englishItem, spanishItem;

	/**
	 * Window for displaying help-related information.
	 */
	private HelpTMClient helpWindow = null;

	/**
	 * Window for displaying information about the application.
	 */
	private AboutUs aboutUsWindow = null;

	/**
	 * Link to resources for the application icon.
	 */
	private String icoLink = "resources/AC-logo.png";
	/**
	 * Link to resources for the application images.
	 */
	private String imageLink = "resources/tm-client.png";

	/**
	 * Icon representing connection status.
	 */
	private Icon connectedIcon = createStatusIcon(Color.GREEN);
	/**
	 * Icon representing disconnection status.
	 */
	private Icon disconnectedIcon = createStatusIcon(Color.RED);

	/**
	 * Label to display the connection status icon.
	 */
	private JLabel connectionStatusIconLabel;

	/**
	 * Constructor to initialize the ClientGUI with the specified CSModel. Sets up
	 * the UI components and layout.
	 */
	public ClientGUI() {
		this.bundle = languageManager.getBundle();
		initializeWindow();
		initializeHeader();
		initializeMessageArea();
		initializeStatusPanel();
		add(mainPanel);
		setLocationRelativeTo(null); // Centra la ventana en la pantalla
		setVisible(true); // Hace visible la ventana
		initializeClientComponents();
	}

	/**
	 * Initializes the main window of the client GUI. This includes setting window
	 * properties, creating the main panel, and initializing the language menu.
	 */
	private void initializeWindow() {
		// Set the icon of the window using the image link defined in icoLink
		setIconImage(new ImageIcon(getClass().getClassLoader().getResource(icoLink)).getImage());

		// Set the window to be non-resizable
		setResizable(false);

		// Define the operation that will happen by default when the user initiates a
		// "close" on this frame
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// Set the title of the window from the resource bundle
		setTitle(bundle.getString("clientWindowTitle"));

		// Set the size of the window
		setSize(800, 600);

		// Initialize the main panel with BorderLayout
		mainPanel = new JPanel(new BorderLayout());

		// Initialize the language menu for the window
		initializeLanguageMenu();
	}

	/**
	 * Initializes the header part of the client GUI. The header includes an image,
	 * text fields for user input, and buttons for interactions.
	 */
	private void initializeHeader() {
		// Create a new panel for the header with a BorderLayout
		JPanel headerPanel = new JPanel(new BorderLayout());

		// Add an image to the top of the header panel
		headerPanel.add(new JLabel(new ImageIcon(getClass().getClassLoader().getResource(imageLink))),
				BorderLayout.NORTH);

		// Add the first row of the header, which includes user input fields and
		// connect/end buttons
		headerPanel.add(createHeaderRow1Panel(), BorderLayout.CENTER);

		// Add the second row of the header, which includes additional input fields and
		// buttons
		headerPanel.add(createHeaderRow2Panel(), BorderLayout.SOUTH);

		// Add the complete header panel to the main panel of the window
		mainPanel.add(headerPanel, BorderLayout.NORTH);
	}

	/**
	 * Creates the first row of the header panel in the client GUI.
	 *
	 * @return JPanel The created panel with input fields and buttons.
	 */
	private JPanel createHeaderRow1Panel() {
		// Create a new panel with centered flow layout
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		// Initialize text fields for username, server IP, and port
		usernameTextField = new JTextField(10); // Field for username input
		serverIpTextField = new JTextField(15); // Field for server IP input
		portTextField = new JTextField(6); // Field for port number input

		// Initialize buttons for connecting to the server and ending the session
		connectButton = new JButton(bundle.getString("connectButtonText"));
		endButton = new JButton(bundle.getString("endButtonText"));

		// Add action listener to the connect button to handle connection process
		connectButton.addActionListener(e -> clientController.connectToServer(usernameTextField.getText(),
				serverIpTextField.getText(), portTextField.getText(), bundle));

		// Add action listener to the end button to handle session termination
		endButton.addActionListener(this::onEndButtonPressed);

		// Initialize labels for each input field
		userLabel = new JLabel(bundle.getString("usernameLabel")); // Label for username
		serverIpLabel = new JLabel(bundle.getString("serverIPLabel")); // Label for server IP
		portLabel = new JLabel(bundle.getString("portLabel")); // Label for port number

		// Add labels and input fields to the panel
		panel.add(userLabel);
		panel.add(usernameTextField);
		panel.add(serverIpLabel);
		panel.add(serverIpTextField);
		panel.add(portLabel);
		panel.add(portTextField);

		// Add connect and end buttons to the panel
		panel.add(connectButton);
		panel.add(endButton);

		return panel; // Return the completed panel
	}

	/**
	 * Initializes the language and help menus for the client GUI. This method
	 * creates menu items for selecting language preferences and accessing help
	 * resources, and adds action listeners to handle user interactions with these
	 * menu items.
	 */
	private void initializeLanguageMenu() {
		// Create the main menu bar
		JMenuBar menuBar = new JMenuBar();

		// Create and set up the language menu
		languageMenu = new JMenu(bundle.getString("languageMenu"));
		englishItem = new JMenuItem(bundle.getString("englishBar"));
		spanishItem = new JMenuItem(bundle.getString("spanishBar"));

		// Add action listeners to the language menu items for changing language
		englishItem.addActionListener(e -> changeLanguage("English"));
		spanishItem.addActionListener(e -> changeLanguage("Español"));

		// Add the language items to the language menu
		languageMenu.add(englishItem);
		languageMenu.add(spanishItem);

		// Create and set up the help menu
		helpMenu = new JMenu(bundle.getString("helpBar"));

		// Add help menu items for Help and About Us sections
		helpMenuItem = new JMenuItem(bundle.getString("helpBar"));
		aboutUsMenuItem = new JMenuItem(bundle.getString("aboutUsBar"));

		// Add action listeners for the help menu items
		helpMenuItem.addActionListener(e -> {
			// Open the help window only once at a time
			if (helpWindow == null) {
				helpWindow = new HelpTMClient(languageManager.getCurrentLocale());
				helpWindow.setVisible(true);
				helpWindow.addWindowListener(new WindowAdapter() {
					// Reset the help window reference when it's closed
					@Override
					public void windowClosed(WindowEvent e) {
						helpWindow = null;
					}
				});
			}
		});

		aboutUsMenuItem.addActionListener(e -> {
			// Open the about us window only once at a time
			if (aboutUsWindow == null) {
				aboutUsWindow = new AboutUs(languageManager.getCurrentLocale());
				aboutUsWindow.setVisible(true);
				aboutUsWindow.addWindowListener(new WindowAdapter() {
					// Reset the about us window reference when it's closed
					@Override
					public void windowClosed(WindowEvent e) {
						aboutUsWindow = null;
					}
				});
			}
		});

		// Add the help sub-menu items to the help menu
		helpMenu.add(helpMenuItem);
		helpMenu.add(aboutUsMenuItem);

		// Add the language and help menus to the menu bar
		menuBar.add(languageMenu);
		menuBar.add(helpMenu);

		// Set the menu bar for the GUI window
		setJMenuBar(menuBar);
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
		connectButton.setText(bundle.getString("connectButtonText"));
		endButton.setText(bundle.getString("endButtonText"));
		setTitle(bundle.getString("clientWindowTitle"));

		userLabel.setText(bundle.getString("usernameLabel"));
		serverIpLabel.setText(bundle.getString("serverIPLabel"));
		portLabel.setText(bundle.getString("portLabel"));

		newTMButton.setText(bundle.getString("newTMButtonText"));
		sendButton.setText(bundle.getString("sendButtonText"));
		receiveButton.setText(bundle.getString("receiveButtonText"));
		runButton.setText(bundle.getString("runButtonText"));

		tmInputLabel.setText(bundle.getString("tmInputLabel"));
		serverMessagesLabel.setText(bundle.getString("serverMessagesLabel"));
		connectionStatusLabel.setText(bundle.getString("notConnectedLabel"));

		languageMenu.setText(bundle.getString("languageMenu"));
		englishItem.setText(bundle.getString("englishBar"));
		spanishItem.setText(bundle.getString("spanishBar"));
		helpMenuItem.setText(bundle.getString("helpBar"));
		helpMenu.setText(bundle.getString("helpBar"));
		aboutUsMenuItem.setText(bundle.getString("aboutUsBar"));
	}

	/**
	 * Handles the action performed when the 'End' button is pressed in the client
	 * GUI. This method prompts the user for confirmation to close the client and,
	 * if confirmed, disconnects from the server, opens a new CSModel window, and
	 * disposes of the current window.
	 *
	 * @param e The ActionEvent triggered by pressing the 'End' button.
	 */
	private void onEndButtonPressed(ActionEvent e) {
		// Prompt the user for confirmation to close the client
		int confirm = JOptionPane.showConfirmDialog(this, bundle.getString("confirmCloseMessage"),
				bundle.getString("confirmCloseTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		// Check if the user confirmed the action
		if (confirm == JOptionPane.YES_OPTION) {
			// Disconnect from the server
			clientController.disconnectFromServer(bundle);

			// Dispose of the current client window, effectively closing it
			dispose();
		}
	}

	/**
	 * Creates the second row of the header panel in the client GUI. This method
	 * sets up various buttons and a text field for interacting with the Turing
	 * Machine.
	 *
	 * @return A JPanel containing the second row of the header.
	 */
	private JPanel createHeaderRow2Panel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		// Initialize buttons and text field with labels from the resource bundle
		newTMButton = new JButton(bundle.getString("newTMButtonText"));
		tmInputField = new JTextField("00000 01000 10010 11000", 15);
		sendButton = new JButton(bundle.getString("sendButtonText"));
		receiveButton = new JButton(bundle.getString("receiveButtonText"));
		runButton = new JButton(bundle.getString("runButtonText"));

		// Add an ActionListener to the new Turing Machine button to handle input
		newTMButton.addActionListener(e -> {
			String tmInput = tmInputField.getText();
			clientController.handleNewTMButton(tmInput, bundle);
		});

		// Add an ActionListener to the run button to open a new Turing Machine GUI
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tmGUI == null || !tmGUI.isVisible()) {
					tmGUI = new TuringMachineGUI(tmInputField.getText(), clientController);
					tmGUI.setVisible(true);
				}
			}
		});

		// Initially disable some buttons as they require a connection or other state to
		// be enabled
		sendButton.setEnabled(false);
		runButton.setEnabled(false);
		newTMButton.setEnabled(false);
		receiveButton.setEnabled(false);

		// Add ActionListeners to the send and receive buttons for communication
		sendButton.addActionListener(e -> clientController.sendMessage(tmInputField.getText(), bundle));
		receiveButton.addActionListener(e -> clientController.receiveData(bundle));

		// Add components to the panel
		panel.add(newTMButton);
		tmInputLabel = new JLabel(bundle.getString("tmInputLabel"));
		panel.add(tmInputLabel);
		panel.add(tmInputField);
		panel.add(sendButton);
		panel.add(receiveButton);
		panel.add(runButton);

		return panel;
	}

	/**
	 * Updates the Turing Machine input field with a new message. This method sets
	 * the content of the Turing Machine input field in the GUI.
	 *
	 * @param message The message to be displayed in the Turing Machine input field.
	 */
	public void tmInputMessage(String message) {
		tmInputField.setText(message);
	}

	/**
	 * Initializes the area for displaying server messages. This method creates a
	 * JTextArea within a JScrollPane for displaying messages received from the
	 * server.
	 */
	private void initializeMessageArea() {
		messageTextArea = new JTextArea(10, 40); // TextArea with 10 rows and 40 columns
		messageTextArea.setEditable(false); // Makes the text area read-only
		JScrollPane messageScrollPane = new JScrollPane(messageTextArea); // Adds scroll functionality

		JPanel messagePanel = new JPanel(new BorderLayout());
		serverMessagesLabel = new JLabel(bundle.getString("serverMessagesLabel")); // Label for the message area
		messagePanel.add((serverMessagesLabel), BorderLayout.NORTH); // Adds the label to the top
		messagePanel.add(messageScrollPane, BorderLayout.CENTER); // Adds the TextArea in the center
		mainPanel.add(messagePanel, BorderLayout.CENTER); // Adds the whole panel to the main panel
	}

	/**
	 * Initializes the status panel of the GUI. This method creates and configures a
	 * panel showing the connection status and server information.
	 */
	private void initializeStatusPanel() {
		JPanel statusP = new JPanel(new BorderLayout());
		connectionStatusLabel = new JLabel(bundle.getString("notConnectedLabel")); // Label for connection status
		connectionStatusIconLabel = new JLabel(disconnectedIcon); // Icon for connection status
		serverInfoLabel = new JLabel(""); // Label for displaying server information

		JPanel statusPanel = new JPanel(new BorderLayout());
		statusP.add(connectionStatusLabel, BorderLayout.WEST); // Adds the connection status label to the left
		statusP.add(connectionStatusIconLabel, BorderLayout.CENTER); // Adds the status icon in the center
		statusPanel.add(statusP, BorderLayout.WEST); // Adds the left part to the status panel
		statusPanel.add(serverInfoLabel, BorderLayout.EAST); // Adds server info label to the right
		mainPanel.add(statusPanel, BorderLayout.SOUTH); // Adds the status panel to the bottom of the main panel
	}

	/**
	 * Creates an icon representing a status indicator. This method generates a
	 * small circular icon with the specified color to represent different statuses
	 * (e.g., connected, disconnected).
	 *
	 * @param color The color of the status icon, typically green for connected and
	 *              red for disconnected.
	 * @return An ImageIcon object representing the status icon.
	 */
	private Icon createStatusIcon(Color color) {
		BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(color);
		g2d.fillOval(0, 0, 10, 10);
		g2d.dispose();
		return new ImageIcon(image);
	}

	/**
	 * Updates the connection status icon based on the connection state. This method
	 * changes the icon displayed next to the connection status label to reflect
	 * whether the client is connected or disconnected.
	 *
	 * @param isConnected A boolean indicating the connection state - true if
	 *                    connected, false otherwise.
	 */
	public void updateConnectionStatus(boolean isConnected) {
		if (isConnected) {
			connectionStatusIconLabel.setIcon(connectedIcon);
		} else {
			connectionStatusIconLabel.setIcon(disconnectedIcon);
		}
	}

	/**
	 * Sets an ActionListener for the connect button. This method allows external
	 * classes to define the behavior when the connect button is clicked.
	 *
	 * @param listener The ActionListener to be added to the connect button.
	 */
	public void setConnectButtonListener(ActionListener listener) {
		connectButton.addActionListener(listener);
	}

	/**
	 * Sets an ActionListener for the end button. This method allows external
	 * classes to define the behavior when the end button is clicked.
	 *
	 * @param listener The ActionListener to be added to the end button.
	 */
	public void setEndButtonListener(ActionListener listener) {
		endButton.addActionListener(listener);
	}

	/**
	 * Sets an ActionListener for the new TM button. This method allows external
	 * classes to define the behavior when the new TM button is clicked.
	 *
	 * @param listener The ActionListener to be added to the new TM button.
	 */
	public void setNewTMButtonListener(ActionListener listener) {
		newTMButton.addActionListener(listener);
	}

	/**
	 * Retrieves the text input from the Turing Machine input field. This method is
	 * typically used to get the current state or input for the Turing Machine.
	 *
	 * @return A String representing the input from the Turing Machine input field.
	 */
	public String getTMInput() {
		return tmInputField.getText();
	}

	/**
	 * Sets an ActionListener for the send button. This method allows external
	 * classes to define the behavior when the send button is clicked.
	 *
	 * @param listener The ActionListener to be added to the send button.
	 */
	public void setSendButtonListener(ActionListener listener) {
		sendButton.addActionListener(listener);
	}

	/**
	 * Sets an ActionListener for the receive button. This method allows external
	 * classes to define the behavior when the receive button is clicked.
	 *
	 * @param listener The ActionListener to be added to the receive button.
	 */
	public void setReceiveButtonListener(ActionListener listener) {
		receiveButton.addActionListener(listener);
	}

	/**
	 * Sets an ActionListener for the run button. This method allows external
	 * classes to define the behavior when the run button is clicked.
	 *
	 * @param listener The ActionListener to be added to the run button.
	 */
	public void setRunButtonListener(ActionListener listener) {
		runButton.addActionListener(listener);
	}

	/**
	 * Sets the text of the connection status label. This method updates the
	 * connection status label to display the current connection status.
	 *
	 * @param status The connection status message to be displayed.
	 */
	public void setConnectionStatus(String status) {
		connectionStatusLabel.setText(status);
	}

	/**
	 * Sets the text of the server info label. This method updates the server info
	 * label to display information about the server.
	 *
	 * @param info The server information message to be displayed.
	 */
	public void setServerInfo(String info) {
		serverInfoLabel.setText(info);
	}

	/**
	 * Displays a message received from the server in the message text area. This
	 * method appends the username and message to the message text area and scrolls
	 * to the latest message.
	 *
	 * @param username The username of the sender of the message.
	 * @param message  The message received from the server.
	 */
	public void showMessageFromServer(String username, String message) {
		messageTextArea.append(username + ": " + message + "\n");
		messageTextArea.setCaretPosition(messageTextArea.getDocument().getLength());
	}

	/**
	 * Initializes the components related to the client's functionality. This method
	 * creates a new model and controller for the client, and sets up the
	 * controller.
	 */
	private void initializeClientComponents() {
		TuringMachineClientModel model = new TuringMachineClientModel("", 0);
		ClientController clientController = new ClientController(model, this);
		setClientController(clientController);
	}

	/**
	 * Enables or disables the action buttons based on the specified state. This
	 * method is typically called to enable or disable the send and run buttons.
	 *
	 * @param enable A boolean indicating whether to enable (true) or disable
	 *               (false) the buttons.
	 */
	public void enableActionButtons(boolean enable) {
		sendButton.setEnabled(enable);
		runButton.setEnabled(enable);
	}

	/**
	 * Enables or disables additional action buttons based on the specified state.
	 * This method is typically called to enable or disable the new TM and receive
	 * buttons.
	 *
	 * @param enable A boolean indicating whether to enable (true) or disable
	 *               (false) the buttons.
	 */
	public void enableActionButtons2(boolean enable) {
		newTMButton.setEnabled(enable);
		receiveButton.setEnabled(enable);
	}

	/**
	 * Sets the client controller for this GUI. This method allows setting a custom
	 * controller for managing client operations.
	 *
	 * @param controller The ClientController to be used by this GUI.
	 */
	public void setClientController(ClientController controller) {
		this.clientController = controller;
	}

}
