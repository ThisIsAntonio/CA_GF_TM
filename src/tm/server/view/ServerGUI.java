package tm.server.view;

import javax.swing.*;

import support.HelpTMServer;
import support.LanguageManager;
import tm.server.controller.ServerController;
import tm.server.model.ServerProtocol;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

/**
 * The ServerGUI class provides the user interface for the server application.
 * It allows users to start and stop the server, view transition rules, and
 * manage language settings.
 */
public class ServerGUI extends JFrame {
	/**
	 * Serial version UID for serialization.
	 */
	private static final long serialVersionUID = 6L;

	/**
	 * Instance of the Language Manager to handle language settings.
	 */
	private LanguageManager languageManager = LanguageManager.getInstance();

	/**
	 * Resource bundle for storing language-specific strings.
	 */
	private ResourceBundle bundle;

	/**
	 * Main panel of the server GUI.
	 */
	private JPanel mainPanel;

	/**
	 * Text field for inputting the server port number.
	 */
	private JTextField portTextField;

	/**
	 * Button to start the server.
	 */
	private JButton startButton;

	/**
	 * Button to stop the server.
	 */
	private JButton stopButton;

	/**
	 * Button to view the current transition rule.
	 */
	private JButton viewButton;

	/**
	 * Label displaying the server's IP address.
	 */
	private JLabel ipLabel;

	/**
	 * Label displaying the server's connection status.
	 */
	private JLabel connectionStatusLabel;

	/**
	 * Label for the port number input field.
	 */
	private JLabel portLabel;

	/**
	 * Controller to manage server operations.
	 */
	private ServerController serverController;

	/**
	 * Server protocol for managing server-client communication.
	 */
	private ServerProtocol protocol;

	/**
	 * Tabbed pane for managing chat tabs.
	 */
	private JTabbedPane tabbedPane;

	/**
	 * Default port number for the server.
	 */
	private int defaultPort = 12345;

	/**
	 * Menu for language settings.
	 */
	private JMenu languageMenu;

	/**
	 * Menu for help and information.
	 */
	private JMenu helpMenu;

	/**
	 * Menu item for help information.
	 */
	private JMenuItem helpMenuItem;

	/**
	 * Menu item for about us information.
	 */
	private JMenuItem aboutUsMenuItem;

	/**
	 * Menu item for English language selection.
	 */
	private JMenuItem englishItem;

	/**
	 * Menu item for Spanish language selection.
	 */
	private JMenuItem spanishItem;

	/**
	 * Link to the server application icon.
	 */
	private String icoLink = "resources/AC-logo.png";

	/**
	 * Link to the server application image.
	 */
	private String imageLink = "resources/tm-server.png";

	/**
	 * Help window object for providing help information to the user.
	 */
	private HelpTMServer helpWindow = null;

	/**
	 * About Us window object for displaying information about the application.
	 */
	private AboutUs aboutUsWindow = null;

	/**
	 * Icon representing a connected server status.
	 */
	private Icon connectedIcon = createStatusIcon(Color.GREEN);

	/**
	 * Icon representing a disconnected server status.
	 */
	private Icon disconnectedIcon = createStatusIcon(Color.RED);

	/**
	 * Label for displaying the connection status icon.
	 */
	private JLabel connectionStatusIconLabel;

	/**
	 * Constructs the ServerGUI object and initializes the user interface
	 * components.
	 */
	public ServerGUI() {
		// create a bundle
		this.bundle = languageManager.getBundle();
		initializeWindow();
		initializeServerComponents();
	}

	/**
	 * Initializes the main window properties and layout.
	 */
	private void initializeWindow() {
		setIconImage(new ImageIcon(getClass().getClassLoader().getResource(icoLink)).getImage());
		setResizable(false);
		setTitle(bundle.getString("serverTitle"));
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainPanel = new JPanel(new BorderLayout());
		tabbedPane = new JTabbedPane();
		mainPanel.add(tabbedPane, BorderLayout.CENTER);

		JPanel headerPanel = createHeaderPanel();
		mainPanel.add(headerPanel, BorderLayout.NORTH);

		JPanel statusPanel = createStatusPanel();
		mainPanel.add(statusPanel, BorderLayout.SOUTH);

		add(mainPanel);
		setLocationRelativeTo(null);
		setVisible(true);

		initializeLanguageMenu();
	}

	/**
	 * Initializes the language menu with available language options.
	 */
	private void initializeLanguageMenu() {
		JMenuBar menuBar = new JMenuBar();
		languageMenu = new JMenu(bundle.getString("languageMenu"));

		englishItem = new JMenuItem(bundle.getString("englishBar"));
		spanishItem = new JMenuItem(bundle.getString("spanishBar"));

		englishItem.addActionListener(e -> changeLanguage("English"));
		spanishItem.addActionListener(e -> changeLanguage("Español"));

		languageMenu.add(englishItem);
		languageMenu.add(spanishItem);
		menuBar.add(languageMenu);

		// Help Menu
		helpMenu = new JMenu(bundle.getString("helpBar"));

		// Help Sub-Menu Items
		helpMenuItem = new JMenuItem(bundle.getString("helpBar"));
		helpMenuItem.addActionListener(e -> {
			// Open the help window only one time
			if (helpWindow == null) {
				helpWindow = new HelpTMServer(languageManager.getCurrentLocale());
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

		aboutUsMenuItem = new JMenuItem(bundle.getString("aboutUsBar"));
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
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
	}

	/**
	 * Changes the application's language based on user selection.
	 *
	 * @param language The language to switch to.
	 */
	private void changeLanguage(String language) {
		languageManager.setLanguage(language);
		bundle = languageManager.getBundle();
		refreshUI();
	}

	/**
	 * Refreshes the user interface components to reflect the current language
	 * settings.
	 */
	private void refreshUI() {
		languageMenu.setText(bundle.getString("languageMenu"));
		setTitle(bundle.getString("serverTitle"));
		startButton.setText(bundle.getString("startServer"));
		stopButton.setText(bundle.getString("stopServer"));
		viewButton.setText(bundle.getString("viewButton"));
		ipLabel.setText(bundle.getString("ipLabel") + " " + getLocalIPAddress());
		connectionStatusLabel.setText(bundle.getString("connectionStatusLabel"));
		portLabel.setText(bundle.getString("portLabel"));

		englishItem.setText(bundle.getString("englishBar"));
		spanishItem.setText(bundle.getString("spanishBar"));
		helpMenuItem.setText(bundle.getString("helpBar"));
		helpMenu.setText(bundle.getString("helpBar"));
		aboutUsMenuItem.setText(bundle.getString("aboutUsBar"));
	}

	/**
	 * Creates the header panel containing server control buttons and displays.
	 *
	 * @return JPanel The constructed header panel.
	 */
	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.add(new JLabel(new ImageIcon(getClass().getClassLoader().getResource(imageLink))),
				BorderLayout.NORTH);
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		ipLabel = new JLabel(bundle.getString("ipLabel") + " " + getLocalIPAddress());
		portLabel = new JLabel(bundle.getString("portLabel"));
		portTextField = new JTextField(6);
		startButton = new JButton(bundle.getString("startServer"));
		stopButton = new JButton(bundle.getString("stopServer"));

		startButton.addActionListener(this::startServer);
		stopButton.addActionListener(e -> serverController.stopServer());

		viewButton = new JButton(bundle.getString("viewButton"));
		viewButton.addActionListener(e -> serverController.showTransitionRule(bundle));
		setViewButtonVisible(false);
		buttonPanel.add(ipLabel);
		buttonPanel.add(portLabel);
		buttonPanel.add(portTextField);
		buttonPanel.add(startButton);
		buttonPanel.add(stopButton);
		buttonPanel.add(viewButton);

		headerPanel.add(buttonPanel, BorderLayout.SOUTH);
		return headerPanel;
	}

	/**
	 * Sets the visibility of the view button. This method allows for dynamic
	 * control over the display of the view button within the user interface.
	 * 
	 * @param b A boolean value where 'true' makes the view button visible and
	 *          'false' hides it.
	 */
	public void setViewButtonVisible(boolean b) {
		viewButton.setVisible(b);
	}

	/**
	 * Creates the status panel showing the server connection status.
	 *
	 * @return JPanel The constructed status panel.
	 */
	private JPanel createStatusPanel() {
		JPanel statusPanel = new JPanel(new BorderLayout());
		connectionStatusLabel = new JLabel(bundle.getString("connectionStatusLabel"));
		JPanel statusP = new JPanel(new BorderLayout());
		connectionStatusIconLabel = new JLabel(disconnectedIcon);
		statusP.add(connectionStatusLabel, BorderLayout.WEST);
		statusP.add(connectionStatusIconLabel, BorderLayout.CENTER);
		statusPanel.add(statusP, BorderLayout.WEST);
		return statusPanel;
	}

	/**
	 * Creates an icon representing the server's connection status.
	 *
	 * @param color The color indicating the status.
	 * @return Icon The created status icon.
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
	 * Updates the server connection status icon based on the current connection
	 * state.
	 *
	 * @param isConnected true if the server is connected, false otherwise.
	 */
	public void updateConnectionStatus(boolean isConnected) {
		if (isConnected) {
			connectionStatusIconLabel.setIcon(connectedIcon);
		} else {
			connectionStatusIconLabel.setIcon(disconnectedIcon);
		}
	}

	/**
	 * Starts the server using the IP and port specified in the GUI.
	 *
	 * @param e The action event triggering the server start.
	 */
	private void startServer(ActionEvent e) {
		// Asks the user whether to use 'localhost' or the current computer's IP address
		int option = JOptionPane.showOptionDialog(this, bundle.getString("ipQuestion"),
				bundle.getString("selectIpQuestion"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				new String[] { bundle.getString("localhost"), bundle.getString("currentIP") }, "Localhost");

		// Initiates a SwingWorker to execute server startup in the background
		SwingWorker<Void, Void> worker = new SwingWorker<>() {
			@Override
			protected Void doInBackground() throws Exception {
				String ip;
				if (option == JOptionPane.YES_OPTION) {
					// User chose 'localhost'
					ip = "localhost";
				} else {
					// User chose the current IP
					ip = getLocalIPAddress();
				}

				// Fetches the port number from the port text field, defaults to a predefined
				// port if empty
				int port = !portTextField.getText().isEmpty() ? Integer.parseInt(portTextField.getText()) : defaultPort;
				// Calls the method to start the server with the chosen IP and port
				serverController.startServer(ip, port, bundle);
				return null;
			}
		};
		// Executes the SwingWorker
		worker.execute();
	}

	/**
	 * Initializes the components related to the server's functionality.
	 */
	private void initializeServerComponents() {
		serverController = new ServerController(tabbedPane);
		protocol = new ServerProtocol(this, tabbedPane, bundle);

		serverController.setProtocol(protocol);
	}

	/**
	 * Retrieves the text from the port text field.
	 * 
	 * @return The text currently in the port text field.
	 */
	public String getPortText() {
		return portTextField.getText();
	}

	/**
	 * Retrieves the start button component.
	 * 
	 * @return The start button.
	 */
	public JButton getStartButton() {
		return startButton;
	}

	/**
	 * Retrieves the stop button component.
	 * 
	 * @return The stop button.
	 */
	public JButton getStopButton() {
		return stopButton;
	}

	/**
	 * Retrieves the tabbed pane component.
	 * 
	 * @return The tabbed pane.
	 */
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	/**
	 * Sets the ActionListener for the start button.
	 * 
	 * @param listener The ActionListener to attach to the start button.
	 */
	public void setStartButtonListener(ActionListener listener) {
		startButton.addActionListener(listener);
	}

	/**
	 * Sets the ActionListener for the stop button.
	 * 
	 * @param listener The ActionListener to attach to the stop button.
	 */
	public void setStopButtonListener(ActionListener listener) {
		stopButton.addActionListener(listener);
	}

	/**
	 * Sets the text of the connection status label.
	 * 
	 * @param status The connection status message to display.
	 */
	public void setConnectionStatus(String status) {
		connectionStatusLabel.setText(status);
	}

	/**
	 * Displays a message dialog showing that a client is connected.
	 * 
	 * @param username The username of the connected client.
	 * @param clientIP The IP address of the connected client.
	 */
	public void showClientConnectedMessage(String username, String clientIP) {
		JOptionPane.showMessageDialog(this,
				username + " " + bundle.getString("clientConnectedMessage") + " " + clientIP,
				bundle.getString("clientConnectedTitle"), JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Retrieves the local IP address of the server.
	 * 
	 * @return The local IP address or an error message if unable to determine.
	 */
	public String getLocalIPAddress() {
		try {
			InetAddress localhost = InetAddress.getLocalHost();
			return localhost.getHostAddress();
		} catch (UnknownHostException ex) {
			return bundle.getString("ipError");
		}
	}

	/**
	 * Sets the server controller.
	 * 
	 * @param controller The ServerController to be used by this GUI.
	 */
	public void setServerController(ServerController controller) {
		this.serverController = controller;
	}

	/**
	 * Sets the server protocol.
	 * 
	 * @param sProtocol The ServerProtocol to be used by this server.
	 */
	public void setProtocol(ServerProtocol sProtocol) {
		this.protocol = sProtocol;
	}

	/**
	 * Main method to launch the server GUI.
	 *
	 * @param args Command-line arguments.
	 */
	/*
	 * public static void main(String[] args) {
	 * SwingUtilities.invokeLater(ServerGUI::new); }
	 */
}
