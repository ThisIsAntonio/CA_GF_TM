package tm.server.controller;

import tm.server.model.ServerProtocol;
import tm.server.view.ChatTab;

import java.io.*;
import java.net.Socket;

/**
 * Handles client interactions for a server. Each client is managed in a
 * separate thread using this handler.
 */
public class ClientHandler implements Runnable {
	/**
	 * The client socket representing the connection to the client.
	 */
	private Socket clientSocket;

	/**
	 * BufferedReader to read data from the client.
	 */
	private BufferedReader reader;

	/**
	 * PrintWriter to send data to the client.
	 */
	private PrintWriter writer;

	/**
	 * Server protocol to manage server-client communication.
	 */
	private ServerProtocol protocol;

	/**
	 * Username of the connected client.
	 */
	private String username;

	/**
	 * Reference to the chat tab associated with this client.
	 */
	private ChatTab chatTab;

	/**
	 * Stores the last global message sent to the server.
	 */
	private static String lastGlobalMessage = "";

	/**
	 * Manage the separator symbol to split the client message.
	 */
	private String separatorSymbol = "\\|";

	/**
	 * Default Constructor (Here is not used it)
	 */
	public ClientHandler() {
	}

	/**
	 * Constructor to initialize client handler.
	 *
	 * @param clientSocket   The socket of the connected client.
	 * @param serverProtocol The server protocol to handle communication.
	 * @param username       The username of the connected client.
	 */
	public ClientHandler(Socket clientSocket, ServerProtocol serverProtocol, String username) {
		this.clientSocket = clientSocket;
		this.protocol = serverProtocol;
		this.username = username;
		try {
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			writer = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the ChatTab associated with this client.
	 * 
	 * @param chatTab The ChatTab to be used for displaying messages.
	 */
	public void setChatTab(ChatTab chatTab) {
		this.chatTab = chatTab;
	}

	/**
	 * Retrieves the last global message sent to the server.
	 * 
	 * @return The last global message received.
	 */
	public static String getLastGlobalMessage() {
		return lastGlobalMessage;
	}

	/**
	 * Main execution method for the ClientHandler thread. Continuously listens for
	 * messages from the client and processes them.
	 */
	@Override
	public void run() {
		try {
			String inputLine;
			// Continuously read messages from the client
			while ((inputLine = reader.readLine()) != null) {
				// Process each message and get a response
				String response = processClientMessage(username, inputLine);
				// Send the response back to the client
				writer.println(response);

				// If a chat interface is available, update it with the message
				if (chatTab != null) {
					// Split the message into parts to extract the actual message content
					String[] parts = inputLine.split(separatorSymbol, 3);
					if (parts.length == 3) {
						// The actual message is in the third part
						String message = parts[2];
						// Append the message to the chat interface
						chatTab.appendToChat(username + ": " + message);
					}
				}
			}
		} catch (IOException e) {
			// Handle exceptions in reading from/writing to the client
			e.printStackTrace();
		} finally {
			// Ensure resources are closed when the connection ends
			closeResources();
			// Notify the chat interface of the client disconnection
			if (chatTab != null) {
				chatTab.handleClientDisconnection();
			}
		}
	}

	/**
	 * Processes a message received from the client. Stores or retrieves global
	 * messages based on the message content.
	 * 
	 * @param username The username of the client sending the message.
	 * @param message  The message sent by the client.
	 * @return Response based on the message processing.
	 */
	private String processClientMessage(String username, String message) {
		// Split the incoming message into parts based on a separator symbol
		String[] parts = message.split(separatorSymbol, -1);

		// Check if the message is not a data request
		if (!"REQUEST_DATA".equals(parts[2])) {
			// If not a data request, store the message as the last global message
			lastGlobalMessage = parts[2];
			// Return a status code indicating that the message is stored
			return "MESSAGE_STORED";
		}

		// If the message is a data request
		if ("REQUEST_DATA".equals(parts[2])) {
			// Check if there's no last global message stored
			// Return a status indicating either no value is registered or return the last
			// message
			return lastGlobalMessage.isEmpty() ? "NO_VALUE_REGISTERED" : lastGlobalMessage;
		}

		// Default return status indicating the message was received
		return "MESSAGE_RECEIVED";
	}

	/**
	 * Sends a message to the client connected through this handler.
	 * 
	 * @param message The message to be sent to the client.
	 */
	public void sendMessage(String message) {
		if (writer != null) {
			writer.println(message);
		} else {
			System.out.println("Error: PrintWriter no está inicializado en ClientHandler");
		}
	}

	/**
	 * Closes resources associated with the client, including socket and streams.
	 * Also removes the client from the server's active clients list.
	 */
	private void closeResources() {
		try {
			reader.close();
			writer.close();
			clientSocket.close();
			protocol.removeClient(username);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
