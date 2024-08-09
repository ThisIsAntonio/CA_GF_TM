package tm.client.controller;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import tm.client.model.ClientProtocol;
import tm.client.model.TuringMachineClientModel;
import tm.client.view.ClientGUI;

/**
 * Controller class for handling the client-side logic of the Turing Machine
 * application. This class connects the view and model, manages user
 * interactions, and communicates with the server.
 */
public class ClientController {
	/**
	 * The Turing Machine client model
	 */
	private TuringMachineClientModel model;
	/**
	 * The client graphical user interface
	 */
	private ClientGUI view;
	/**
	 * The protocol for client-server communication
	 */
	private ClientProtocol protocol;
	/**
	 * Default server IP if not specified
	 */
	private String defaultServer = "localhost";
	/**
	 * Default port if not specified
	 */
	private String defaultPort = "12345";

	/**
	 * Default Constructor  (Here is not used it)
	 */
	public ClientController() {}
	
	/**
	 * Constructs a ClientController with the specified model and view. Initializes
	 * the protocol and sets it in the model.
	 *
	 * @param clientModel The Turing Machine client model.
	 * @param clView      The client graphical user interface.
	 */
	public ClientController(TuringMachineClientModel clientModel, ClientGUI clView) {
		this.model = clientModel;
		this.view = clView;
		this.protocol = new ClientProtocol(clView);
		model.setProtocol(protocol);
	}

	/**
	 * Initiates a connection to the server with the provided details. If server IP
	 * or port is empty, default values are used. Validates the connection details
	 * before connecting.
	 *
	 * @param username The username for the connection.
	 * @param serverIP The IP address of the server.
	 * @param port     The port number of the server.
	 * @param bundle   The resource bundle for localization.
	 */
	public void connectToServer(String username, String serverIP, String port, ResourceBundle bundle) {
	    // Check if the server IP is provided. If not, ask the user if they want to use localhost
	    if (serverIP.isEmpty()) {
	        int confirm = JOptionPane.showConfirmDialog(null, bundle.getString("confirmLocalhostUsage"),
	                bundle.getString("confirmDialogTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

	        if (confirm == JOptionPane.YES_OPTION) {
	            // The user chose to use localhost
	            serverIP = defaultServer;
	        } else {
	            // The user chose not to use localhost, stop the connection process
	            return;
	        }
	    }

	    // Check if the port is provided. If not, use the default port and inform the user
	    if (port.isEmpty()) {
	        port = defaultPort; // Set to default port
	        showInfo(bundle.getString("usingDefaultPort") + port, bundle); // Show information dialog
	    }

	    // Validate the connection details. If valid, proceed to connect to the server
	    if (validateConnectionDetails(username, serverIP, port, bundle)) {
	        protocol.connectToServer(username, serverIP, port, bundle); // Initiate connection to the server
	    }
	}

	/**
	 * Validates the connection details including username and server IP. Shows
	 * error messages if any detail is invalid.
	 *
	 * @param username The username for the connection.
	 * @param serverIP The IP address of the server.
	 * @param port     The port number of the server.
	 * @param bundle   The resource bundle for localization.
	 * @return true if all details are valid, false otherwise.
	 */
	private boolean validateConnectionDetails(String username, String serverIP, String port, ResourceBundle bundle) {
	    // Check if the username is provided. If empty, show an error message and disable some action buttons
	    if (username.isEmpty()) {
	        showError(bundle.getString("errorEmptyUsername"), bundle); // Show error dialog for empty username
	        view.enableActionButtons2(false); // Disable specific action buttons
	        return false; // Return false indicating invalid details
	    }

	    // Check if the server IP is provided. If empty, show an error message and disable some action buttons
	    if (serverIP.isEmpty()) {
	        showError(bundle.getString("errorEmptyServerIP"), bundle); // Show error dialog for empty server IP
	        view.enableActionButtons2(false); // Disable specific action buttons
	        return false; // Return false indicating invalid details
	    }

	    // If username and server IP are provided, enable specific action buttons and return true
	    view.enableActionButtons2(true); // Enable specific action buttons
	    return true; // Return true indicating valid details
	}

	/**
	 * Disconnects the client from the server.
	 * 
	 * @param bundle The resource bundle for localization.
	 */
	public void disconnectFromServer(ResourceBundle bundle) {
		protocol.disconnectFromServer(bundle);
	}

	/**
	 * Sends a message to the server.
	 * 
	 * @param message The message to send.
	 * @param bundle  The resource bundle for localization.
	 */
	public void sendMessage(String message, ResourceBundle bundle) {
		model.sendMessage(message, bundle);
	}

	/**
	 * Requests data from the server.
	 * 
	 * @param bundle The resource bundle for localization.
	 */
	public void receiveData(ResourceBundle bundle) {
		protocol.sendMessageToServer("REQUEST_DATA", true, bundle);
	}

	/**
	 * Handles the 'New Turing Machine' button action. Validates the binary input
	 * and updates the GUI accordingly.
	 *
	 * @param tmInput The Turing Machine input.
	 * @param bundle  The resource bundle for localization.
	 */
	public void handleNewTMButton(String tmInput, ResourceBundle bundle) {
	    // Check if the provided Turing Machine input is a valid binary string
	    if (isValidBinaryInput(tmInput)) {
	        // If valid, show an informational message and enable certain action buttons
	        showInfo(bundle.getString("validBinaryNumber"), bundle); // Display info dialog for valid binary input
	        view.enableActionButtons(true); // Enable specific action buttons
	    } else {
	        // If invalid, show an error message and disable certain action buttons
	        showError(bundle.getString("invalidBinaryNumber"), bundle); // Display error dialog for invalid binary input
	        view.enableActionButtons(false); // Disable specific action buttons
	    }
	}

	/**
	 * Validates if the given input is a valid binary string.
	 *
	 * @param input The binary string to validate.
	 * @return true if the input is a valid binary string, false otherwise.
	 */
	private boolean isValidBinaryInput(String input) {
		String binaryPattern = "^([01]{5} ){3}[01]{5}$";
		return input.matches(binaryPattern);
	}

	/**
	 * Validates that a tape contains only binary characters (0s and 1s).
	 *
	 * @param tape The tape string to validate.
	 * @return true if the tape is valid, false otherwise.
	 */
	public boolean validateTape(String tape) {
		// Verifica que la cinta solo contenga ceros y unos.
		if (tape.matches("^[01]+$")) {
			return true; // Cinta válida
		} else {
			System.out.println("La cinta contiene caracteres no válidos.");
			return false; // Cinta no válida
		}
	}

	/**
	 * Shows an error message dialog.
	 *
	 * @param message The error message to display.
	 * @param bundle The resource bundle for localization.
	 */
	private void showError(String message, ResourceBundle bundle) {
		JOptionPane.showMessageDialog(view, message, bundle.getString("errorTitle"), JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Shows an informational message dialog.
	 *
	 * @param message The informational message to display.
	 * @param bundle The resource bundle for localization.
	 */
	private void showInfo(String message, ResourceBundle bundle) {
		JOptionPane.showMessageDialog(view, message, bundle.getString("infoTitle"), JOptionPane.INFORMATION_MESSAGE);
	}
}
