package tm.server.model;

import java.util.HashMap;
import java.util.Map;
import tm.server.controller.ClientHandler;
import tm.server.view.ChatTab;

/**
 * ChatTabsManager is a singleton class responsible for managing the chat tabs and client handlers in the server.
 * It provides a centralized point of access to these resources, ensuring that only one instance of this manager exists.
 */
public class ChatTabsManager {
    /**
     *  Singleton instance of ChatTabsManager
     */
    private static ChatTabsManager instance;
    /**
     *  Map to store chat tabs keyed by usernames
     */
    private final Map<String, ChatTab> chatTabs;
    /**
     *  Map to store client handlers keyed by usernames
     */
    private final Map<String, ClientHandler> clientHandlers;

    /**
     * Private constructor for ChatTabsManager.
     * Initializes the maps for chat tabs and client handlers.
     */
    private ChatTabsManager() {
        chatTabs = new HashMap<>();
        clientHandlers = new HashMap<>();
    }

    /**
     * Gets the singleton instance of ChatTabsManager.
     * If the instance doesn't exist, it creates a new one.
     *
     * @return The singleton instance of ChatTabsManager.
     */
    public static ChatTabsManager getInstance() {
        if (instance == null) {
            instance = new ChatTabsManager();
        }
        return instance;
    }

    /**
     * Gets the map of chat tabs.
     *
     * @return A map of chat tabs, keyed by usernames.
     */
    public Map<String, ChatTab> getChatTabs() {
        return chatTabs;
    }

    /**
     * Gets the map of client handlers.
     *
     * @return A map of client handlers, keyed by usernames.
     */
    public Map<String, ClientHandler> getClientHandlers() {
        return clientHandlers;
    }
}

