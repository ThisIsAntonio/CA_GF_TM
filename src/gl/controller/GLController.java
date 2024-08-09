package gl.controller;

import java.util.Optional;
import java.util.ResourceBundle;

import gl.model.GLModel;
import gl.view.GLFooter;
import gl.view.GLView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * The {@code GLController} class is responsible for handling user interactions and
 * controlling the Game of Life application.
 * 
 * @author David Burchat
 * @author Marcos Astudillo
 */
public class GLController {
	/**
	 * The GLModel instance associated with the game controller.
	 */
	private GLModel gameModel;

	/**
	 * The GLView instance associated with the game controller.
	 */
	private GLView gameView;

	/**
	 * The GLFooter instance associated with the game controller.
	 */
	private GLFooter footerView;

	/**
	 * The ResourceBundle used for localization.
	 */
	private ResourceBundle bundle;

	/**
	 * A string representing an error message for when there are no matches in the game rules.
	 */
	private String notMatchesError;

	/**
	 * A string representing an error message for when the input length is too long.
	 */
	private String lengthToLongError;

	/**
	 * The title for dialog boxes used by the controller.
	 */
	private String dialogTitle;


    /**
     * Constructs a GLController with the specified model, view, and resource bundle.
     *
     * @param model The GLModel instance to control.
     * @param view The GLView instance to update with game changes.
     * @param bundle The ResourceBundle for localization.
     * @param footerV The GLFooter instance to update with game changes.
     */
    public GLController(GLModel model, GLView view, ResourceBundle bundle, GLFooter footerV) {
        this.gameModel = model;
        this.gameView = view;
        this.bundle = bundle;
        this.footerView = footerV;
        this.gameModel.setGameUpdateListener(footerView);
    }

    /**
     * Starts the game with the specified number of steps or infinitely if steps is empty.
     *
     * @param steps The number of steps to run the game, or an empty string for infinite mode.
     */
    public void startGame(String steps) {
        gameModel.startGame(steps);
    }

    /**
     * Stops the game.
     */
    public void stopGame() {
        gameModel.stopGame();
    }

    /**
     * Fills the game board with random cell states.
     */
    public void fillRandomly() {
        gameModel.fillRandomly();
    }

    /**
     * Advances the game by one step.
     */
    public void nextStep() {
        gameModel.nextStep();
    }

    /**
     * Reverts the game by one step.
     */
    public void prevStep() {
        gameModel.prevStep();
    }

    /**
     * Restarts the game by stopping it and clearing the board.
     */
    public void restartGame() {
        gameModel.restartGame();
    }

    /**
     * Validates and sets the new rule input for the game.
     *
     * @param newValue The new rule input string.
     * @return An error message if the input is invalid, otherwise null.
     */
    public String checkInput(String newValue) {
        if (newValue == null || newValue.isEmpty()) {
            gameModel.setRules("000100000001100000"); // Conway's rules
            return null; // No errors
        } else if (!newValue.matches("[01]+")) {
            notMatchesError = bundle.getString("notMatchesError");
            return notMatchesError;
        } else if (newValue.length() > 18) {
            lengthToLongError = bundle.getString("lengthToLongError");
            return lengthToLongError;
        }
        return null; // No errors
    }

    /**
     * Toggles between manual and automatic game modes.
     */
    public void toggleManualMode() {
        boolean currentMode = gameModel.isManualMode();
        System.out.println(currentMode);
        gameModel.setManualMode(!currentMode);
        footerView.updateUIFromBoardState();
    }

    /**
     * Sets the state of the cell at the specified coordinates when in manual mode.
     *
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     */
    public void setCellState(int x, int y) {
        if (gameModel.isManualMode()) {
            gameModel.getBoardState()[x][y].toggleAlive();
            footerView.updateUIFromBoardState();
        }
    }

    /**
     * Handles the execution of a user-specified number of steps in the game.
     * Shows a dialog for the user to input the number of steps, then executes the
     * specified number of steps in the game. If no input is provided, it executes 100 steps.
     */
    public void handleSolution() {
        // Create a dialog to get user input for the number of steps, with a default value of "100".
        TextInputDialog dialog = new TextInputDialog("100");

        // Set the dialog title using the localized value from the resource bundle.
        dialogTitle = bundle.getString("solutionDialogTitle");

        // Get the stage of the dialog and add an application icon to it.
        Stage alertStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image("resources/AC-logo.png"));

        // Set the title, header text, and content text of the dialog using localized values.
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText(bundle.getString("solutionHeaderText"));
        dialog.setContentText(bundle.getString("solutionContentText"));

        // Show the dialog and wait for user input.
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                // Parse the user input into an integer (number of steps).
                int numSteps = Integer.parseInt(result.get());

                // Execute the specified number of steps in the game model.
                gameModel.executeSolution(numSteps);
                gameModel.setCurrentStep(numSteps);
            } catch (NumberFormatException e) {
                // Handle the case where the user input is not a valid integer.
                gameView.showInvalidInputAlert(bundle.getString("solutionErrorText"));
            }
        } else {
            // If no input is provided, execute 100 steps in the game model.
            gameModel.executeSolution(100);
        }
    }

    /**
     * Changes the language resource bundle for the controller.
     *
     * @param newBundle The new language resource bundle.
     */
    public void changeLanguage(ResourceBundle newBundle) {
        this.bundle = newBundle;
    }

    /**
     * Handles the click event on a cell in the game board.
     *
     * @param x The x-coordinate of the clicked cell.
     * @param y The y-coordinate of the clicked cell.
     */
    public void handleCellClick(int x, int y) {
        setCellState(x, y);
    }
}
