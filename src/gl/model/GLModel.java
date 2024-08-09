package gl.model;

import java.util.Random;
import java.util.ResourceBundle;
import java.util.Stack;

import gl.controller.GLUpdateListener;
import gl.view.GLView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import support.LanguageManager;

/**
 * The {@code GLModel} class represents the model of the Game of Life.
 * It manages the game board, game rules, and provides methods for controlling
 * the game.
 * 
 * This class is responsible for maintaining the state of the Game of Life,
 * including the game board, game rules, and game execution.
 * 
 * @author David Burchat
 * @author Marcos Astudillo
 */
public class GLModel {
	/**
	 * The GLBoard instance responsible for managing the game board.
	 */
	private GLBoard board;

	/**
	 * The GLView instance associated with the game controller.
	 */
	private GLView gameView;

	/**
	 * A flag indicating whether the game simulation is currently running.
	 */
	private boolean running;

	/**
	 * A stack that stores previous states of the game board for undo functionality.
	 */
	private Stack<GLCell[][]> previousStates;

	/**
	 * The timeline for the game loop that updates the simulation.
	 */
	private Timeline gameLoop;

	/**
	 * The listener responsible for updating the user interface based on game events.
	 */
	private GLUpdateListener listener;

	/**
	 * The rules engine used to determine the next state of cells in the game.
	 */
	private GLRules gameRules; // Added to handle custom rules

	/**
	 * A flag indicating whether the game is in manual mode (step-by-step).
	 */
	private boolean manualMode = false;

	/**
	 * The current step in the manual mode of the game.
	 */
	private int currentStep;

	/**
	 * The width of the game board.
	 */
	private int boardWidth;

	/**
	 * The height of the game board.
	 */
	private int boardHeight;

	/**
	 * The ResourceBundle used for localization.
	 */
	private ResourceBundle bundle = LanguageManager.getInstance().getBundle();
	
	/**
	 * A string used for alert messages related to the game model.
	 */
	private String alertModel = bundle.getString("invalidInputMessage");


	/**
	 * Constructs a new GameOfLifeModel with the specified number of rows and
	 * columns.
	 *
	 * @param rows The number of rows in the game board.
	 * @param cols The number of columns in the game board.
	 * @param bundle The ResourceBundle for localization.
     * @param glView The GLFooter instance to update with game changes.
	 */
	public GLModel(int rows, int cols, ResourceBundle bundle, GLView glView) {
		this.board = new GLBoard(rows, cols); // Initialize the game board with the specified dimensions
		this.running = false;
		this.previousStates = new Stack<>();
		this.gameRules = new GLRules("000100000001100000"); // Initialize with default Conway's rules
		this.currentStep = 1; // Initialize step counter to 1
		this.boardWidth = rows;
		this.boardHeight = cols;
		this.bundle = bundle;
		this.gameView = glView;
	}

	/**
	 * Checks if the game is currently running.
	 *
	 * @return True if the game is running, false otherwise.
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Sets a listener for game update events.
	 *
	 * @param listener The listener to set.
	 */
	public void setGameUpdateListener(GLUpdateListener listener) {
		this.listener = listener;
	}

	/**
	 * Gets the current step in the game.
	 *
	 * @return The current step.
	 */
	public int getCurrentStep() {
		return currentStep;
	}
	
	/**
	 * Sets the current step in the game.
	 */
	public void setCurrentStep(int step) {
		this.currentStep = step;
	}

	/**
	 * Sets the rules of the game based on a binary string.
	 *
	 * @param binaryRule The binary string representing the rules.
	 */
	public void setRules(String binaryRule) {
		this.gameRules = new GLRules(binaryRule);
	}

	/**
	 * Notifies the game update listener if it is set.
	 */
	private void notifyGameUpdateListener() {
		if (listener != null) {
			listener.onGameUpdate();
		}
	}

	/**
	 * Fills the game board with random cell states.
	 */
	public void fillRandomly() {
		// Create a random number generator
		Random random = new Random();
		// Get the 2D array of cells from the game board
		GLCell[][] cells = board.getCells();
		// Iterate through all rows and columns of the board
		for (int i = 0; i < getBoardWidth(); i++) {
			for (int j = 0; j < getBoardHeight(); j++) {
				// Generate a random boolean value to represent the cell's state
				boolean randomState = random.nextBoolean();
				// Set the state of the cell in the array to the random state
				cells[i][j].setAlive(randomState);
			}
		}
		// Notify the game update listener that the board has been updated
		notifyGameUpdateListener();
	}

	/**
	 * Starts the game with the specified number of steps or infinitely if steps is
	 * empty.
	 *
	 * @param steps The number of steps to run the game, or an empty string for
	 *              infinite mode.
	 */
	public void startGame(String steps) {
		int stepsInt;
		resetStepCounter();
		if (steps.isEmpty()) {
			// If no value is entered for steps, run the game infinitely
			stepsInt = Integer.MAX_VALUE;
		} else {
			try {
				// Parse the entered steps as an integer
				stepsInt = Integer.parseInt(steps);
			} catch (NumberFormatException e) {
				// Show an invalid input alert and exit the method if parsing fails
				gameView.showInvalidInputAlert(alertModel);
				return;
			}
		}

		if (isRunning()) {
			return; // If the game is already running, do nothing
		}
		// Create a game loop using JavaFX Timeline
		gameLoop = new Timeline(new KeyFrame(Duration.millis(500), e -> {
			// Save the current state of the board
			saveCurrentState();
			// Evolve the board to the next generation based on the game rules
			evolve();
			// Notify the game update listener
			notifyGameUpdateListener();
			// Increment the step counter
			currentStep++;
			// Check if the desired number of steps has been reached or if the game is over
			if (currentStep == stepsInt || isGameOver()) {
				// Stop the game if the specified step count is reached or if there are no live
				// cells left
				stopGame();
			}
		}));
		gameLoop.setCycleCount(Timeline.INDEFINITE);
		// Start the game loop
		gameLoop.play();
		running = true;
	}

	/**
	 * Stops the game if it is running.
	 */
	public void stopGame() {
		if (gameLoop != null) {
			gameLoop.stop();
		}
		running = false;
	}

	/**
	 * Advances the game by one step (generation).
	 */
	public void nextStep() {
		saveCurrentState();
		evolve();
		notifyGameUpdateListener();
	}

	/**
	 * Reverts the game to the previous step (generation) if available.
	 */
	public void prevStep() {
		if (!previousStates.isEmpty()) {
			GLCell[][] lastState = previousStates.pop();
			board.setCells(lastState);
			notifyGameUpdateListener();
		}
	}

	/**
	 * Restarts the game by stopping it, clearing the board, and resetting the step
	 * counter.
	 */
	public void restartGame() {
		// Stop the game if it's currently running
		stopGame();
		// Get the 2D array of cells from the game board
		GLCell[][] cells = board.getCells();
		// Iterate through all rows and columns of the board
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				// Set the state of each cell to false (dead)
				cells[i][j].setAlive(false);
			}
		}
		// Reset the step counter to 1
		resetStepCounter();
		// Clear the stack of previous states
		previousStates.clear();
		// Notify the game update listener that the board has been updated
		notifyGameUpdateListener();
	}

	/**
	 * Resets the step counter to 1.
	 */
	public void resetStepCounter() {
		currentStep = 0;
	}

	/**
	 * Gets the current state of the game board.
	 *
	 * @return The 2D array representing the current state of the game board.
	 */
	public GLCell[][] getBoardState() {
		return board.getCells();
	}

	/**
	 * Evolves the game board to the next generation based on the current rules.
	 */
	private void evolve() {
		// Get the current state of the board
		GLCell[][] cells = board.getCells();
		// Create a new matrix to save the board for the next step (next generation)
		GLCell[][] nextGeneration = new GLCell[cells.length][cells[0].length];

		// Iterate through each cell of the board
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				// Count how many neighbors are alive for the current cell
				int aliveNeighbors = countAliveNeighbors(i, j, cells);
				// Determine whether the current cell should be alive or dead in the next
				// generation
				boolean nextState = gameRules.shouldCellBeAliveNextGeneration(cells[i][j].isAlive(), aliveNeighbors);
				// Create a new cell for the next generation and set its state (alive or dead)
				nextGeneration[i][j] = new GLCell();
				nextGeneration[i][j].setAlive(nextState);
			}
		}
		// Update the game board with the new state for the next generation
		board.setCells(nextGeneration);
	}

	/**
	 * n s=∑ i=a Counts the number of alive neighbors for a cell at the specified
	 * coordinates.
	 *
	 * @param x     The x-coordinate of the cell.
	 * @param y     The y-coordinate of the cell.
	 * @param cells The game board cells.
	 * @return The number of alive neighbors.
	 */
	private int countAliveNeighbors(int x, int y, GLCell[][] cells) {
		// Initialize the count of alive neighbors to 0
		int count = 0;
		// Iterate over the neighbors
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				// Exclude the current cell by skipping the position (0,0) (current cell)
				if (i == 0 && j == 0)
					continue;
				// Calculate new x and y coordinates for the current neighbor
				int newX = x + i;
				int newY = y + j;
				// Check if the coordinates are within the board's boundaries and if the
				// neighbor cell is alive
				if (newX >= 0 && newX < cells.length && newY >= 0 && newY < cells[newX].length
						&& cells[newX][newY].isAlive()) {
					// Increment the count if the neighbor cell is alive
					count++;
				}
			}
		}
		// Return the total count of alive neighbors
		return count;
	}

	/**
	 * Saves the current state of the game board to the stack of previous states.
	 */
	private void saveCurrentState() {
		GLCell[][] currentState = board.getCells();
		GLCell[][] copiedState = copyState(currentState);
		previousStates.push(copiedState);
	}

	/**
	 * Copies the state of the game board to a new 2D array.
	 *
	 * @param original The original game board state.
	 * @return A copy of the game board state.
	 */
	private GLCell[][] copyState(GLCell[][] original) {
		int rows = original.length;
		int cols = original[0].length;
		GLCell[][] copy = new GLCell[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				copy[i][j] = new GLCell();
				copy[i][j].setAlive(original[i][j].isAlive());
			}
		}
		return copy;
	}

	/**
	 * Sets manual mode for the game.
	 *
	 * @param manualMode True to enable manual mode, false otherwise.
	 */
	public void setManualMode(boolean manualMode) {
		this.manualMode = manualMode;
	}

	/**
	 * Checks if the game is in manual mode.
	 *
	 * @return True if manual mode is enabled, false otherwise.
	 */
	public boolean isManualMode() {
		return manualMode;
	}

	/**
	 * Checks if the game is over (no live cells remaining).
	 *
	 * @return True if the game is over, false otherwise.
	 */
	public boolean isGameOver() {
		// Get the current state of the game board
		GLCell[][] cells = board.getCells();
		// Iterate through all rows and columns of the board
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				// Check if the current cell is alive
				if (cells[i][j].isAlive()) {
					// If at least one cell is alive, the game is not over
					return false;
				}
			}
		}
		// If no live cells were found, the game is considered over
		return true;
	}

	/**
	 * Gets the current rule for the Game of Life.
	 *
	 * @return The current rule as a binary string.
	 */
	public String getCurrentRule() {
		return gameRules.getCurrentRule();
	}

	/**
	 * Gets the width of the game board.
	 *
	 * @return The width of the game board.
	 */
	public int getBoardWidth() {
		return boardWidth;
	}

	/**
	 * Gets the height of the game board.
	 *
	 * @return The height of the game board.
	 */
	public int getBoardHeight() {
		return boardHeight;
	}

	/**
	 * Sets the width of the game board and reconfigures the board accordingly.
	 *
	 * @param width The new width for the game board.
	 */
	public void setBoardWidth(int width) {
		boardWidth = width;
		this.board = new GLBoard(boardWidth, boardHeight);
	}

	/**
	 * Sets the height of the game board and reconfigures the board accordingly.
	 *
	 * @param height The new height for the game board.
	 */
	public void setBoardHeight(int height) {
		boardHeight = height;
		this.board = new GLBoard(boardWidth, boardHeight);
	}

	/**
	 * Resizes the game board to the specified new width and height.
	 *
	 * @param newWidth  The new width for the game board.
	 * @param newHeight The new height for the game board.
	 */
	public void resizeBoard(int newWidth, int newHeight) {
		board.resize(newWidth, newHeight);
		setBoardHeight(newHeight);
		setBoardWidth(newWidth);
		if (running) {
			stopGame();
		}
		notifyGameUpdateListener();
	}

	/**
	 * Toggles the state (alive or dead) of a cell at the specified coordinates.
	 *
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 */
	public void toggleCellState(int x, int y) {
		GLCell[][] cells = board.getCells();
		if (x >= 0 && x < boardWidth && y >= 0 && y < boardHeight) {
			cells[x][y].toggleAlive();
			notifyGameUpdateListener();
		}
	}

	/**
	 * Executes the specified number of steps in the game.
	 *
	 * @param numSteps The number of steps to execute.
	 */
	public void executeSolution(int numSteps) {
		// Iterate for the specified number of steps
		for (int i = 0; i < numSteps; i++) {
			// Check if the game is currently running and stop it if so
			if (isRunning()) {
				
				stopGame(); // Stop the game if it's currently running
			}
			nextStep(); // Execute the next step in the game
		}
		// Notify the controller or view about the current state after executing the
		// solution
		notifyGameUpdateListener();
	}

	/**
	 * Calculates the number of live neighbors (adjacent cells) for a given cell at
	 * the specified coordinates.
	 *
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 * @return The number of live neighbors for the cell.
	 */
	public int getNumNeighbours(int x, int y) {
		int numNeighbours = 0;
		GLCell[][] cells = board.getCells(); // Get the cell array of the board

		// Define relative offsets for neighboring cells
		int[][] neighborOffsets = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
				{ 1, 1 } };

		// Iterate through the offsets for neighboring cells
		for (int[] offset : neighborOffsets) {
			int newX = x + offset[0]; // Calculate the new X coordinate
			int newY = y + offset[1]; // Calculate the new Y coordinate

			// Check if the coordinates are within the board's boundaries
			if (newX >= 0 && newX < boardWidth && newY >= 0 && newY < boardHeight) {
				// Check if the neighboring cell is alive
				if (cells[newX][newY].isAlive()) {
					numNeighbours++; // Increment the count of live neighbors
				}
			}
		}

		return numNeighbours; // Return the number of live neighbors
	}

    /**
     * Changes the language resource bundle for the controller.
     *
     * @param newBundle The new language resource bundle.
     */
	public void changeLanguage(ResourceBundle newBundle) {
		this.bundle = newBundle;
		
		alertModel = bundle.getString("invalidInputMessage");
	}
}
