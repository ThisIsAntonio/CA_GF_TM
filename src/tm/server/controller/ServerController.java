package tm.server.controller;

import java.util.ResourceBundle;
import javax.swing.JTabbedPane;
import tm.server.model.ServerProtocol;

/**
 * Controller class for the server application. Handles the interactions between
 * the server's UI and its underlying model.
 */
public class ServerController {
	/**
	 * The protocol for managing server operations.
	 */
	private ServerProtocol protocol;

	/**
	 * A JTabbedPane used in the server's UI.
	 */
	private JTabbedPane tabbedPane;

	/**
	 * Default Constructor (Here is not used it)
	 */
	public ServerController() {
	}

	/**
	 * Constructs a ServerController with a reference to a JTabbedPane.
	 *
	 * @param jtabbedPanne The JTabbedPane instance used for managing chat tabs.
	 */
	public ServerController(JTabbedPane jtabbedPanne) {
		this.tabbedPane = jtabbedPanne;
	}

	/**
	 * Sets the ServerProtocol for this controller.
	 *
	 * @param serverProtocol The ServerProtocol instance to be used by this
	 *                       controller.
	 */
	public void setProtocol(ServerProtocol serverProtocol) {
		this.protocol = serverProtocol;
	}

	/**
	 * Sets the JTabbedPane for this controller.
	 *
	 * @param tabbedPane The JTabbedPane instance to be used by this controller.
	 */
	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	/**
	 * Starts the server with the specified IP address and port.
	 *
	 * @param ip     The IP address to start the server on.
	 * @param port   The port number to start the server on.
	 * @param bundle The resource bundle for internationalization.
	 */
	public void startServer(String ip, int port, ResourceBundle bundle) {
		try {
			// Initiate server start-up using the ServerProtocol instance
			protocol.startServer(ip, port, bundle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops the server.
	 */
	public void stopServer() {
		// Stop the server using the ServerProtocol instance
		protocol.stopServer();
	}

	/**
	 * Removes a client from the server.
	 *
	 * @param username The username of the client to remove.
	 */
	public void removeClient(String username) {
		// Remove the client using the ServerProtocol and close its chat tab
		protocol.removeClient(username);
		closeChatTab(username);
	}

	/**
	 * Closes the chat tab associated with a specific client.
	 *
	 * @param clientUser The username of the client whose chat tab needs to be
	 *                   closed.
	 */
	public void closeChatTab(String clientUser) {
		// Iterate through the tabs to find and close the specific chat tab
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			String tabTitle = tabbedPane.getTitleAt(i);
			if (tabTitle.equals(clientUser)) {
				tabbedPane.removeTabAt(i);
				break; // Exit the loop once the tab is found and removed
			}
		}
	}

	/**
	 * Displays the transition rule set on the server.
	 *
	 * @param bundle The resource bundle for internationalization.
	 */
	public void showTransitionRule(ResourceBundle bundle) {
		// Display the transition rule using the ServerProtocol instance
		protocol.showTransitionRule(bundle);
	}
}
