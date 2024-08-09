package tm.client.model;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import tm.client.view.ClientGUI;

/**
 * This class handles the client-side protocol for communication with the server.
 */
public class ClientProtocol {
	 /**
	  *  The client GUI interface
	  */
	private ClientGUI view;
	 /**
	  *  Socket for communication with the server
	  */
	private Socket socket;
	 /**
	  *  Writer to send messages to the server
	  */
	private PrintWriter serverWriter;
	 /**
	  *  Username of the client
	  */
	private String clientUsername;
	 /**
	  *  IP address of the client
	  */
	private String clientIP;
	/**
	 *  Default separator for message parts
	 */
	private final String defaultSeparator = "|"; 

	/**
	 * Default Constructor  (Here is not used it)
	 */
	public ClientProtocol() {}
	
    /**
     * Constructor for ClientProtocol.
     * @param clientView The client GUI interface.
     */
	public ClientProtocol(ClientGUI clientView) {
		this.view = clientView;

	}

    /**
     * Connects to the server using the provided username, server IP, and port.
     * @param username The username of the client.
     * @param serverIP The IP address of the server.
     * @param portStr The port number as a string.
     * @param bundle The resource bundle for localization.
     */
	public void connectToServer(String username, String serverIP, String portStr, ResourceBundle bundle) {
	    // Validates that the provided port is a valid number
	    int port;
	    try {
	        port = Integer.parseInt(portStr);
	    } catch (NumberFormatException e) {
	        // Handle the error if the port is not a valid number
	        view.setConnectionStatus(bundle.getString("portError"));
	        return;
	    }

	    try {
	        // Establishes a connection with the server
	        socket = new Socket(serverIP, port);
	        this.clientUsername = username;

	        // Creates a PrintWriter object to send messages to the server
	        serverWriter = new PrintWriter(socket.getOutputStream(), true);
	        this.clientIP = serverIP;

	        // Sends the username to the server as the initial message
	        String message = username + defaultSeparator + serverIP + defaultSeparator + bundle.getString("helloFromClient");
	        serverWriter.println(message);

	        // Updates the GUI with the connection status
	        view.setConnectionStatus(bundle.getString("connected"));
	        view.setServerInfo(bundle.getString("server") + ": " + serverIP);

	        // Starts a new thread to receive messages from the server
	        Thread messageReceiverThread = new Thread(() -> receiveMessageFromServer(bundle));
	        messageReceiverThread.start();

	        // Updates the connection status in the GUI
	        view.updateConnectionStatus(true);
	    } catch (IOException e) {
	        // Handles error in case of connection problems
	        view.setConnectionStatus(bundle.getString("connectionError"));
	    }
	}

	
    /**
     * Disconnects from the server and updates the GUI.
     * @param bundle The resource bundle for localization.
     */
	public void disconnectFromServer(ResourceBundle bundle) {
	    try {
	        // Implements the logic for disconnecting from the server
	        if (socket != null && !socket.isClosed()) {
	            // Closes the connection with the server
	            socket.close();

	            // Updates the GUI to reflect the disconnected status
	            view.setConnectionStatus(bundle.getString("disconnected"));
	            view.updateConnectionStatus(false);
	            view.setServerInfo("");

	        }
	    } catch (IOException e) {
	        // Handles any exceptions that occur during disconnection
	        view.setConnectionStatus(bundle.getString("disconnectError"));
	    }
	}


    /**
     * Sends a message to the server.
     * @param message The message to send.
     * @param isReceiveRequest A flag indicating if the message is a request for data.
     * @param bundle The resource bundle for localization.
     */
	public void sendMessageToServer(String message, boolean isReceiveRequest, ResourceBundle bundle) {
	    // Check if the serverWriter (PrintWriter) is initialized
	    if (serverWriter != null) {

	        // If the message is a request to receive data, modify the message accordingly
	        if (isReceiveRequest) {
	            String addMessage = "REQUEST_DATA";
	            message = addMessage;
	        }

	        // Send the message to the server using the PrintWriter
	        // The format includes the client's username, IP, and the actual message
	        serverWriter.println(clientUsername + defaultSeparator + clientIP + defaultSeparator  + message);

	    } else {
	        // If the PrintWriter is not initialized, log or handle this issue
	        System.out.println(bundle.getString("printWriterNotInitialized"));

	    }
	}


    /**
     * Receives messages from the server and handles them.
     * @param bundle The resource bundle for localization.
     */
	private void receiveMessageFromServer(ResourceBundle bundle) {
	    // Initialize a BufferedReader for reading messages from the server
	    BufferedReader serverReader = null;
	    try {
	        serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        String serverMessage;

	        // Continuously read messages from the server
	        while ((serverMessage = serverReader.readLine()) != null) {
	            // Log or handle the received message
	            System.out.println(serverMessage);

	            // Check for a specific server shutdown message
	            if ("SERVER_SHUTDOWN".equals(serverMessage)) {
	                // If received, notify the user and exit the application
	                SwingUtilities.invokeLater(() -> {
	                    JOptionPane.showMessageDialog(null, bundle.getString("serverMessageStop"), bundle.getString("serverMessageStop2"), JOptionPane.INFORMATION_MESSAGE);
	                    view.dispose();
	                });
	                break; // Exit the loop on server shutdown
	            }

	            // Additional logic to handle specific types of messages
	            // For example, updating the GUI based on the message content
	            if (serverMessage.substring(0,1).matches("[0-1]*")) {
	                view.tmInputMessage(serverMessage);
	            }

	            // Delegate the handling of the message to another method
	            handleMessageFromServer("Server", serverMessage);
	        }
	    } catch (SocketException e) {
	        // Log or handle the case where the socket is closed
	        System.out.println(bundle.getString("socketClosed"));
	    } catch (IOException e) {
	        // Handle other IO exceptions, such as connection loss
	        view.setConnectionStatus(bundle.getString("lostConnection"));
	        e.printStackTrace();
	    } finally {
	        // Ensure the BufferedReader is closed properly
	        try {
	            if (serverReader != null) {
	                serverReader.close();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}


    /**
     * Handles a message received from the server.
     * @param username The username of the sender.
     * @param message The received message.
     */
	private void handleMessageFromServer(String username, String message) {
		view.showMessageFromServer(username, message);
	}

    /**
     * Sends the Turing Machine input to the server.
     * @param message The Turing Machine input.
     */
	public void sendTMInput(String message) {
		if (serverWriter != null) {
			serverWriter.println(message);
	    } else {
	        System.out.println("Error: PrintWriter no está inicializado / isn't initialized");
	    }
	}
}
