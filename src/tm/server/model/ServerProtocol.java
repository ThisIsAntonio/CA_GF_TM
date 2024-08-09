package tm.server.model;

import tm.server.controller.ClientHandler;
import tm.server.view.ChatTab;
import tm.server.view.ServerGUI;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * ServerProtocol handles the server operations for the Turing Machine server
 * application. It manages client connections, server socket, and facilitates
 * communication between server and clients.
 */
public class ServerProtocol {
	/**
	 * The GUI component of the server
	 */
	private ServerGUI view;
	/**
	 * The server socket to listen for client connections
	 */
	private ServerSocket serverSocket;
	/**
	 * Flag to control the server's running state
	 */
	private volatile boolean isRunning = true;
	/**
	 * Map of connected clients
	 */
	private Map<String, ClientHandler> connectedClients = ChatTabsManager.getInstance().getClientHandlers();
	/**
	 * Executor service for handling client threads
	 */
	private ExecutorService executorService = Executors.newCachedThreadPool();
	/**
	 * Tab pane for managing chat tabs
	 */
	private JTabbedPane tabPane;
	/**
	 * Resource bundle for internationalization
	 */
	private ResourceBundle bundle;

	/**
	 * Default Constructor (Here is not used it)
	 */
	public ServerProtocol() {
	}

	/**
	 * Constructor for ServerProtocol.
	 *
	 * @param serverView The GUI view of the server.
	 * @param tabbedPane The tab pane for chat tabs.
	 * @param bundle     The resource bundle for localization.
	 */
	public ServerProtocol(ServerGUI serverView, JTabbedPane tabbedPane, ResourceBundle bundle) {
		this.view = serverView;
		this.tabPane = tabbedPane;
		this.bundle = bundle;
	}

	/**
	 * Starts the server and listens for client connections.
	 *
	 * @param ip      The IP address to bind the server to.
	 * @param port    The port number for the server.
	 * @param rBundle The resource bundle for localization.
	 */
	public void startServer(String ip, int port, ResourceBundle rBundle) {
		try {
			// Determine the IP address to bind the server to
			InetAddress bindAddress = ip.equals("localhost") ? InetAddress.getLoopbackAddress()
					: InetAddress.getByName(ip);
			serverSocket = new ServerSocket(port, 50, bindAddress);

			// Notify the server GUI that the server has started
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(view,
					rBundle.getString("serverStarted") + " " + ip + ":" + port, rBundle.getString("serverStartedTitle"),
					JOptionPane.INFORMATION_MESSAGE));

			// Update server status in the GUI
			view.setConnectionStatus(rBundle.getString("serverRunningStatus"));
			view.updateConnectionStatus(true);
			view.setViewButtonVisible(true);

			// Continuously listen for client connections as long as the server is running
			while (isRunning) {
				try {
					// Check if the server socket is still open
					if (!serverSocket.isClosed()) {
						// Accept a client connection
						Socket clientSocket = serverSocket.accept();
						BufferedReader clientReader = new BufferedReader(
								new InputStreamReader(clientSocket.getInputStream()));

						// Read the initial message from the client
						String clientMessage = clientReader.readLine();
						String[] parts = clientMessage.split("\\|");
						String username = parts[0]; // Extract the username

						// Handle client connection in the GUI thread
						SwingUtilities.invokeLater(() -> {
							view.showClientConnectedMessage(username, clientSocket.getInetAddress().toString());
							manageClientConnection(username, clientSocket);
						});
					} else {
						// Exit the loop if the server socket is closed
						break;
					}
				} catch (SocketException e) {
					// Handle stopping of the server
					if (!isRunning) {
						System.out.println(rBundle.getString("serverStopping"));
						view.updateConnectionStatus(false);
						break;
					} else {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// Handle any other exceptions and update server status
			e.printStackTrace();
			JOptionPane.showMessageDialog(view, bundle.getString("serverStartError") + ": " + e.getMessage(),
					bundle.getString("errorTitle"), JOptionPane.ERROR_MESSAGE);
			view.updateConnectionStatus(false);
		}
	}

	/**
	 * Manages a new client connection by setting up a chat tab and client handler.
	 *
	 * @param username     The username of the client.
	 * @param clientSocket The socket for client-server communication.
	 */
	private void manageClientConnection(String username, Socket clientSocket) {
		addChatTab(username);

		ChatTab chat = ChatTabsManager.getInstance().getChatTabs().get(username);
		String serverMessage = bundle.getString("helloFromServer");

		ClientHandler clientHandler = new ClientHandler(clientSocket, this, username);
		clientHandler.setChatTab(chat);
		clientHandler.sendMessage(serverMessage);
		executorService.execute(clientHandler);

		connectedClients.put(username, clientHandler);
	}

	/**
	 * Adds a new chat tab for a connected client.
	 *
	 * @param clientName The name of the client for which to add the chat tab.
	 */
	public void addChatTab(String clientName) {
		// Get the instance of ChatTabsManager to manage chat tabs
		ChatTabsManager chatTabsManager = ChatTabsManager.getInstance();

		// Check if a chat tab for the given client name doesn't already exist
		if (!chatTabsManager.getChatTabs().containsKey(clientName)) {
			// Create a new ChatTab object for the client
			ChatTab chatTab = new ChatTab(clientName, bundle);
			// Add the new chat tab to the chatTabsManager
			chatTabsManager.getChatTabs().put(clientName, chatTab);

			// Update the GUI in the Event Dispatch Thread (EDT)
			SwingUtilities.invokeLater(() -> {
				try {
					// Add the new chat tab to the tabbed pane
					tabPane.addTab(clientName, chatTab);
					// Find the index of the new tab and set it as the selected tab
					int index = tabPane.indexOfComponent(chatTab);
					tabPane.setSelectedIndex(index);
					// Set the tabbed pane of the chat tab
					chatTab.setTabbePane(this.tabPane);

					// Log a message indicating a new tab has been added
					System.out.println(bundle.getString("tabAddedFor") + " " + clientName);
				} catch (Exception e) {
					// Print any exceptions to the console
					e.printStackTrace();
				}
			});
		} else {
			// Log a message if a tab for this client already exists
			System.out.println(bundle.getString("userAlreadyHasChatTab") + " " + clientName);
		}
	}

	/**
	 * Stops the server and closes all connections.
	 */
	public void stopServer() {
		try {
			// Check if the server socket is initialized and not closed
			if (serverSocket != null && !serverSocket.isClosed()) {

				// Iterate through all connected clients and send a server shutdown message
				for (ClientHandler client : connectedClients.values()) {
					client.sendMessage("SERVER_SHUTDOWN");
				}
				// Wait for a short time to ensure all messages are sent
				Thread.sleep(500);

				// Close the server socket
				serverSocket.close();

				// Show a message to the server administrator indicating the server is stopped
				JOptionPane.showMessageDialog(view, bundle.getString("serverStopped"),
						bundle.getString("serverStoppedTitle"), JOptionPane.INFORMATION_MESSAGE);
				// Update the connection status in the GUI to indicate the server is not
				// connected
				view.updateConnectionStatus(false);
			}

			// Set the flag to indicate the server is no longer running
			isRunning = false;
			// Update the server status in the GUI
			view.setConnectionStatus(bundle.getString("serverStoppedStatus"));
			view.updateConnectionStatus(false);
			view.setViewButtonVisible(false);

			// Shutdown the executor service to stop processing any further client requests
			executorService.shutdownNow();
		} catch (Exception e) {
			// In case of any exception, print the stack trace for debugging
			e.printStackTrace();
		}
	}

	/**
	 * Removes a client and its corresponding chat tab from the server.
	 *
	 * @param username The username of the client to remove.
	 */
	public void removeClient(String username) {
		ChatTab chatTab = ChatTabsManager.getInstance().getChatTabs().remove(username);
		if (chatTab != null) {
			chatTab.handleClientDisconnection();
		}
	}

	/**
	 * Displays the last global message received by the server.
	 *
	 * @param bundle The resource bundle for localization.
	 */
	public void showTransitionRule(ResourceBundle bundle) {
		// Get the last global message from the client handler.
		String message = ClientHandler.getLastGlobalMessage();

		// If the message is blank, get a resource string as a fallback.
		if (message.isBlank()) {
			message = bundle.getString("transitionRuleEmpty");
		}

		// Declare a final variable for use within SwingUtilities.invokeLater
		final String finalMessage = message;
		// Message displayed on the Swing Event Dispatch Thread (EDT).
		SwingUtilities.invokeLater(() -> {
			// Display the message in a dialog box.
			JOptionPane.showMessageDialog(view, finalMessage, bundle.getString("transitionRuleMessage"),
					JOptionPane.INFORMATION_MESSAGE);
		});
	}
}
