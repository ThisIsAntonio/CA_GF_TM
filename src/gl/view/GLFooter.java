package gl.view;

import java.util.ResourceBundle;

import gl.controller.GLController;
import gl.controller.GLUpdateListener;
import gl.model.GLCell;
import gl.model.GLModel;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import support.LanguageManager;

/**
 * The {@code GLFooter} class represents the footer of the Game of Life
 * application. It provides user interface components and controls for
 * interacting with the game. This class is responsible for managing buttons,
 * input fields, and displaying game information to the user.
 * 
 * It also implements the {@code GLUpdateListener} interface to receive updates
 * from the game model and update the user interface accordingly.
 * 
 * @author David Burchat
 * @author Marcos Astudillo
 */
public class GLFooter implements GLUpdateListener {

	/**
	 * The GLController responsible for managing game logic.
	 */
	private GLController gameController;

	/**
	 * The GLModel representing the game's model.
	 */
	private GLModel gameModel;

	/**
	 * The GLView representing the game's view.
	 */
	private GLView glView;

	/**
	 * The ResourceBundle used for localization.
	 */
	private ResourceBundle bundle;

	/**
	 * The LanguageManager instance for language localization.
	 */
	private LanguageManager languageManager = LanguageManager.getInstance();

	/**
	 * The height of the footer in the UI.
	 */
	private final int FOOTER_HEIGHT = 50;

	/**
	 * The width of the game window.
	 */
	private int window_width;

	/**
	 * The height of the game window.
	 */
	private int window_height;

	/**
	 * The width of the game board.
	 */
	private int board_width;

	/**
	 * The height of the game board.
	 */
	private int board_height;

	/**
	 * Flag indicating whether multicolor mode is enabled.
	 */
	boolean multicolorEnabled = false;

	/**
	 * The color selected for cells.
	 */
	private Rectangle[][] grid;

	/**
	 * The selected cell color.
	 */
	Color selectedCellColor = Color.BLACK;

	/**
	 * Label displaying the current step in the game.
	 */
	public Label currentStepLbl;

	/**
	 * Button for generating a random initial game state.
	 */
	private Button btnRandom;

	/**
	 * Button for entering manual cell configuration mode.
	 */
	private Button btnManual;

	/**
	 * Label displaying the model information.
	 */
	private Label modelLabel;

	/**
	 * Text field for entering a custom model configuration.
	 */
	private TextField modelInput;

	/**
	 * Button for navigating to the previous step.
	 */
	private Button btnPrev;

	/**
	 * Button for starting the game simulation.
	 */
	private Button btnStart;

	/**
	 * Label displaying information about the number of steps.
	 */
	private Label stepsLabel;

	/**
	 * Text field for entering the number of steps.
	 */
	private TextField stepsInput;

	/**
	 * Button for stopping the game simulation.
	 */
	private Button btnStop;

	/**
	 * Button for navigating to the next step.
	 */
	private Button btnNext;

	/**
	 * A message with advice for the user.
	 */
	private String adviceMessage;

	/**
	 * A message for displaying length-related errors.
	 */
	private String lengthErrorMessage;

	/**
	 * The default message.
	 */
	private String defaultMessage;
	
	/**
	 * The default error Alert
	 */
	private String errorAlert;

	/**
	 * Constructs a new {@code GLFooter} object with the specified parameters.
	 * 
	 * @param glView   The main view of the Game of Life application.
	 * @param bundle   The ResourceBundle used for localization.
	 * @param window_w The width of the application window.
	 * @param window_h The height of the application window.
	 * @param b_width  The width of the game board.
	 * @param b_height The height of the game board.
	 */
	public GLFooter(GLView glView, ResourceBundle bundle, int window_w, int window_h, int b_width, int b_height) {
		this.glView = glView;
		this.bundle = bundle;
		this.window_width = window_w;
		this.window_height = window_h;
		this.board_width = b_width;
		this.board_height = b_height;

		this.gameModel = new GLModel(window_width, window_height, bundle, glView);
		this.gameController = new GLController(gameModel, glView, bundle, this);

		bundle = languageManager.getBundle();
		errorAlert = bundle.getString("inputErrorBoardSize");

		stepsLabel = new Label();
		stepsInput = new TextField();
		btnRandom = new Button();
		btnManual = new Button();
		modelLabel = new Label();
		modelInput = new TextField();
		btnPrev = new Button();
		btnStart = new Button();
		stepsLabel = new Label();
		stepsInput = new TextField();
		currentStepLbl = new Label();
		btnStop = new Button();
		btnNext = new Button();
		initBoardUI();
	}

	/**
	 * Creates and configures the user interface components for the footer.
	 * 
	 * @return A VBox containing the configured UI components for the footer.
	 */
	public VBox createFooter() {
		VBox footer = new VBox(6);
		HBox buttonsTopRow = new HBox(5);
		HBox buttonsBottomRow = new HBox(5);

		btnRandom = new Button(bundle.getString("btnRandom"));
		btnRandom.setOnAction(e -> gameController.fillRandomly());
		btnManual = new Button(bundle.getString("btnManual"));
		btnManual.setOnAction(e -> gameController.toggleManualMode());

		modelLabel = new Label(bundle.getString("modelLabel"));
		modelInput = createModelInput();
		modelInput.setPromptText(bundle.getString("modelInputPrompt"));

		buttonsTopRow.getChildren().addAll(btnRandom, modelLabel, modelInput, btnManual);

		buttonsTopRow.setAlignment(Pos.CENTER);

		btnPrev = new Button(bundle.getString("btnPrev"));
		btnPrev.setOnAction(e -> gameController.prevStep());

		defaultMessage = bundle.getString("emptyRuleMessage");
		lengthErrorMessage = bundle.getString("invalidRuleLengthMessage");
		adviceMessage = bundle.getString("ruleAdviceMessage");

		btnStart = new Button(bundle.getString("btnStart"));
		btnStart.setOnAction(e -> {
			String rule = modelInput.getText();
			if (rule.isEmpty()) {
				rule = "000100000001100000";
				glView.showInvalidInputAlert(defaultMessage + rule);
			} else if (rule.length() != 18) {
				glView.showInvalidInputAlert(lengthErrorMessage);
				return;
			} else {
				glView.showInvalidInputAlert(adviceMessage + rule);
			}
			String errorMessage = gameController.checkInput(rule);
			if (errorMessage != null) {
				glView.showInvalidInputAlert(errorMessage);
				return;
			}
			gameModel.setRules(rule);
			String stepsText = stepsInput.getText();
			gameController.startGame(stepsText);
		});

		stepsLabel = new Label(bundle.getString("stepsLabel"));
		stepsInput = new TextField();
		stepsInput.setPromptText(bundle.getString("stepsInputPrompt"));
		currentStepLbl = new Label(bundle.getString("currentStepLbl"));
		currentStepLbl.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
		currentStepLbl.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		btnStop = new Button(bundle.getString("btnStop"));
		btnStop.setOnAction(e -> gameController.stopGame());

		btnNext = new Button(bundle.getString("btnNext"));
		btnNext.setOnAction(e -> gameController.nextStep());

		buttonsBottomRow.getChildren().addAll(btnPrev, btnStart, stepsLabel, stepsInput, currentStepLbl, btnStop,
				btnNext);
		buttonsBottomRow.setAlignment(Pos.CENTER);

		footer.getChildren().addAll(buttonsTopRow, buttonsBottomRow);
		footer.setPadding(new Insets(0, 0, 10, 0));
		footer.setPrefWidth(window_width);
		footer.setPrefHeight(FOOTER_HEIGHT);
		footer.setAlignment(Pos.CENTER);

		return footer;
	}

	/**
	 * Initializes the user interface for the game board.
	 */
	public void initBoardUI() {
		glView.boardPane.getChildren().clear();
		grid = new Rectangle[board_width][board_height];

		for (int i = 0; i < board_height; i++) {
			for (int j = 0; j < board_width; j++) {
				final int y = j;
				final int x = i;
				Rectangle rect = new Rectangle(glView.CELL_SIZE, glView.CELL_SIZE, Color.WHITE);
				rect.setOnMouseClicked(e -> gameController.handleCellClick(x, y));
				rect.setStroke(Color.LIGHTGRAY);
				grid[j][i] = rect;
				glView.boardPane.add(rect, j, i);
			}
		}
		gameModel.resizeBoard(board_width, board_height);
	}

	/**
	 * Creates and configures an input field for specifying the cellular automaton
	 * rule.
	 * 
	 * @return A TextField for entering the rule.
	 */
	private TextField createModelInput() {
		TextField modelInput = new TextField();
		modelInput.textProperty().addListener((observable, oldValue, newValue) -> {
			String errorMessage = gameController.checkInput(newValue);
			if (errorMessage != null) {
				modelInput.setText(oldValue);
			}
		});
		return modelInput;
	}

	/**
	 * Changes the language of the user interface elements based on the provided
	 * ResourceBundle.
	 * 
	 * @param newBundle The ResourceBundle containing the updated language strings.
	 */
	public void changeLanguage(ResourceBundle newBundle) {
		this.bundle = newBundle;

		modelLabel.setText(bundle.getString("modelLabel"));
		modelInput.setPromptText(bundle.getString("modelInputPrompt"));
		btnRandom.setText(bundle.getString("btnRandom"));
		btnManual.setText(bundle.getString("btnManual"));

		stepsLabel.setText(bundle.getString("stepsLabel"));
		stepsInput.setPromptText(bundle.getString("stepsInputPrompt"));
		currentStepLbl.setText(bundle.getString("currentStepLbl"));
		btnStop.setText(bundle.getString("btnStop"));
		btnNext.setText(bundle.getString("btnNext"));
		btnPrev.setText(bundle.getString("btnPrev"));
		btnStart.setText(bundle.getString("btnStart"));
		defaultMessage = bundle.getString("emptyRuleMessage");
		lengthErrorMessage = bundle.getString("invalidRuleLengthMessage");
		adviceMessage = bundle.getString("ruleAdviceMessage");
		errorAlert = bundle.getString("inputErrorBoardSize");

		updateUIFromBoardState();
	}

	/**
	 * Updates the user interface to reflect the current state of the game board.
	 */
	public void updateUIFromBoardState() {
		GLCell[][] cells = gameModel.getBoardState();
		for (int i = 0; i < board_height; i++) {
			for (int j = 0; j < board_width; j++) {
				if (cells[i][j].isAlive()) {
					if (multicolorEnabled) {
						int numNeighbours = gameModel.getNumNeighbours(i, j);
						Color cellColor = getRandomColor(numNeighbours);
						grid[j][i].setFill(cellColor);
					} else {
						grid[j][i].setFill(selectedCellColor);
					}
				} else {
					grid[j][i].setFill(Color.WHITE);
				}
			}
		}
		int currentStep = gameModel.getCurrentStep();
		currentStepLbl.setText(bundle.getString("currentStepLbl") + " " + currentStep);
	}

	/**
	 * Returns a random color based on the number of neighbors.
	 * 
	 * @param numNeighbours The number of neighboring cells.
	 * @return A color based on the number of neighbors.
	 */
	private Color getRandomColor(int numNeighbours) {
		switch (numNeighbours) {
		case 0:
			return Color.RED;
		case 1:
			return Color.GREEN;
		case 2:
			return Color.BLUE;
		case 3:
			return Color.YELLOW;
		case 4:
			return Color.PINK;
		case 5:
			return Color.LIGHTBLUE;
		case 6:
			return Color.GREY;
		case 7:
			return Color.ORANGE;
		case 8:
			return Color.MEDIUMPURPLE;
		default:
			return Color.BLACK;
		}
	}

	/**
	 * Restarts the game.
	 */
	public void restartItem() {
		gameController.restartGame();
	}

	/**
	 * Handles the solution of the game.
	 */
	public void solutionItem() {
		gameController.handleSolution();
	}

	/**
	 * Updates the board's dimensions.
	 * 
	 * @param newWidth  The new width for the board.
	 * @param newHeight The new height for the board.
	 */
	public void changeBoardSize(int newWidth, int newHeight) {
		if (newWidth <= 100 || newHeight <= 100) {
			board_width = newWidth;
			board_height = newHeight;

			glView.CELL_SIZE = Math.min((glView.WINDOW_WIDTH - 20.0) / board_width,
					(glView.WINDOW_HEIGHT - 250.0) / board_height);

			gameModel.resizeBoard(newWidth, newHeight);
			initBoardUI();
		} else {
			glView.showInvalidInputAlert(errorAlert);
		}
	}

	@Override
	public void onGameUpdate() {
		Platform.runLater(() -> updateUIFromBoardState());
	}
}
