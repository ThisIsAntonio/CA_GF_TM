package gl.model;

import java.util.Random;

/**
 * Represents the game board for the Game of Life.
 * 
 * This class manages the cells on the game board and provides methods for
 * accessing and manipulating them.
 * 
 * @author David Burchat
 * @author Marcos Astudillo
 */
public class GLBoard {
	/**
	 * The number of rows in the grid.
	 */
	private int rows;

	/**
	 * The number of columns in the grid.
	 */
	private int cols;

	/**
	 * A two-dimensional array representing the grid cells.
	 */
	private GLCell[][] cells;


    /**
     * Constructs a new GameBoard with the specified number of rows and columns.
     *
     * @param rows The number of rows in the game board.
     * @param cols The number of columns in the game board.
     */
    public GLBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new GLCell[rows][cols];
        
        // Initialize each cell in the board.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new GLCell();
            }
        }
    }
    
    /**
     * Gets the 2D array representing the cells on the game board.
     *
     * @return The 2D array of cells.
     */
    public GLCell[][] getCells() {
        return cells;
    }

    /**
     * Sets the cells on the game board with a new 2D cell array.
     *
     * @param cells The new 2D array of cells.
     * @throws IllegalArgumentException If the dimensions of the provided cell array do not match the board's dimensions.
     */
    public void setCells(GLCell[][] cells) {
        if (cells == null || cells.length != rows || cells[0].length != cols) {
            throw new IllegalArgumentException("Invalid cell array dimensions");
        }
        this.cells = cells;
    }

    /**
     * Randomly sets the state (alive or dead) of each cell on the game board.
     */
    public void randomizeState() {
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j].setAlive(random.nextBoolean());
            }
        }
    }
    
    /**
     * Resizes the game board to the specified new width and height.
     *
     * @param newWidth  The new width for the game board.
     * @param newHeight The new height for the game board.
     */
    public void resize(int newWidth, int newHeight) {
        // Create a new matrix of cells with the new dimensions.
        GLCell[][] newCells = new GLCell[newWidth][newHeight];

        // Copy the existing cells to the new matrix (adjusting as necessary).
        for (int i = 0; i < Math.min(rows, newWidth); i++) {
            for (int j = 0; j < Math.min(cols, newHeight); j++) {
                newCells[i][j] = cells[i][j];
            }
        }

        // Update the dimensions and cell matrix.
        rows = newWidth;
        cols = newHeight;
        cells = newCells;
    }
}
