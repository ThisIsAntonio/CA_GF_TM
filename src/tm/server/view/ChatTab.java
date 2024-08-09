package tm.server.view;

import java.awt.BorderLayout;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * The ChatTab class represents a chat panel for a specific client in the server
 * GUI. It is used to display chat messages and handle chat interactions with
 * the client.
 */
public class ChatTab extends JPanel {
	/**
	 * Serial version UID for serialization.
	 */
	private static final long serialVersionUID = 7L;
	/**
	 * Text area for displaying chat messages
	 */
	private JTextArea chatTextArea;
	/**
	 * Username of the client
	 */
	private String clientUser;
	/**
	 * Tabbed pane to which this chat tab belongs
	 */
	private JTabbedPane tabbedPane;
	/**
	 * Resource bundle for localization
	 */
	private ResourceBundle bundle;

	/**
	 * Default Constructor (Here is not used it)
	 */
	public ChatTab() {
	}

	/**
	 * Constructs a new ChatTab for a given user.
	 *
	 * @param user   The username of the client this chat tab is associated with.
	 * @param bundle The resource bundle for localization.
	 */
	public ChatTab(String user, ResourceBundle bundle) {
		this.clientUser = user;
		this.bundle = bundle;

		setLayout(new BorderLayout());
		chatTextArea = new JTextArea(20, 40);
		chatTextArea.setEditable(false);
		JScrollPane chatScrollPane = new JScrollPane(chatTextArea);
		add(chatScrollPane, BorderLayout.CENTER);

		appendToChat(bundle.getString("connectedTo") + " " + clientUser);
		;
	}

	/**
	 * Sets the tabbed pane that this chat tab belongs to.
	 *
	 * @param tab The JTabbedPane instance.
	 */
	public void setTabbePane(JTabbedPane tab) {
		this.tabbedPane = tab;
	}

	/**
	 * Appends a given message to the chat area.
	 *
	 * @param message The message to be appended to the chat.
	 */
	public void appendToChat(String message) {
		System.out.println(message);
		SwingUtilities.invokeLater(() -> {
			chatTextArea.append(message + "\n");
			chatTextArea.setCaretPosition(chatTextArea.getDocument().getLength());
			chatTextArea.revalidate();
			chatTextArea.repaint();
		});
	}

	/**
	 * Handles the disconnection of the client associated with this chat tab. It
	 * displays a disconnection message and removes the tab from the tabbed pane.
	 */
	public void handleClientDisconnection() {
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(this, bundle.getString("clientDisconnected") + " " + clientUser);
			if (tabbedPane != null) {
				tabbedPane.remove(this);
			}
		});
	}
}
