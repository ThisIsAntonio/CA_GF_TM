package gl.controller;

/**
 * The {@code GLUpdateListener} interface provides a callback method for notifying
 * listeners when there's an update in the game state.
 * 
 * @author David Burchat
 * @author Marcos Astudillo
 */
public interface GLUpdateListener {
    /**
     * Called to notify listeners about a game state update.
     */
    void onGameUpdate();
}
