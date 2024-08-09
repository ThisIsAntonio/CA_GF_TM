package gl.model;

/**
 * Represents a single cell in the Game of Life grid.
 * 
 * This class represents an individual cell within the Game of Life grid and
 * provides methods for checking and manipulating its state.
 * 
 * @author David Burchat
 * @author Marcos Astudillo
 */
public class GLCell {
	/**
	 * A boolean flag indicating whether the cell is alive (true) or dead (false).
	 */
	private boolean alive;


    /**
     * Constructs a new cell with the initial state set to dead (not alive).
     */
    public GLCell() {
        this.alive = false;
    }
    
    /**
     * Checks if the cell is alive.
     *
     * @return true if the cell is alive, false otherwise.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Sets the cell's alive state.
     *
     * @param alive true to set the cell as alive, false to set it as dead.
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
    /**
     * Toggles the cell's alive state. If it's alive, it becomes dead, and vice versa.
     */
    public void toggleAlive() {
        alive = !alive;
    }
}
