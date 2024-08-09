package gl.view;

import java.io.InputStream;
import java.util.ResourceBundle;

import gl.controller.GLController;
import gl.model.GLModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import support.LanguageManager;

/**
 * The GLView class is responsible for creating and displaying the main user
 * interface of the Game of Life application. It includes the game board, menu
 * bar, header, footer, and handles user interactions.
 * 
 * @author David Burchat
 * @author Marcos Astudillo
 */
public class GLView {
	/**
	 * The language manager instance for handling localization.
	 */
	private LanguageManager languageManager = LanguageManager.getInstance();

	/**
	 * The resource bundle for localization.
	 */
	private ResourceBundle bundle;

	/**
	 * The header view for the Game of Life application.
	 */
	private GLHeaderImage headerView;

	/**
	 * The footer view for the Game of Life application.
	 */
	private GLFooter footerView;

	/**
	 * The width of the game board.
	 */
	private int board_width;

	/**
	 * The height of the game board.
	 */
	private int board_height;

	/**
	 * The width of the game window.
	 */
	int WINDOW_WIDTH = 525;

	/**
	 * The height of the game window.
	 */
	int WINDOW_HEIGHT = 730;

	/**
	 * The grid pane used for displaying the game board.
	 */
	GridPane boardPane = new GridPane();

	/**
	 * The controller for managing the Game of Life application.
	 */
	private GLController gameController;

	/**
	 * The model for the Game of Life application.
	 */
	private GLModel gameModel;

	/**
	 * The menu bar view for the Game of Life application.
	 */
	private GLMenuBar menuBarView;

	/**
	 * The primary stage of the Game of Life application.
	 */
	private Stage primaryStage;

	/**
	 * The cell size used for rendering the game board.
	 */
	double CELL_SIZE;


    /**
     * Constructs a GLView instance with the specified primary stage and board dimensions.
     * 
     * @param primaryStage The primary stage of the application.
     * @param board_width  The width of the game board.
     * @param board_height The height of the game board.
     */
    public GLView(Stage primaryStage, int board_width, int board_height) {
        // Create a bundle for the languages, default on launch is English
        bundle = languageManager.getBundle();
        this.primaryStage = primaryStage;
        this.board_width = board_width;
        this.board_height = board_height;
        this.primaryStage.setTitle(bundle.getString("mainTitle"));

        CELL_SIZE = Math.min((WINDOW_WIDTH - 20.0) / board_width, (WINDOW_HEIGHT - 250.0) / board_height);

        headerView = new GLHeaderImage(bundle, WINDOW_WIDTH);
        footerView = new GLFooter(this, bundle, board_height, board_height, board_width, board_height);
        gameModel = new GLModel(board_width, board_height, bundle, this);
        menuBarView = new GLMenuBar(primaryStage, bundle, this, gameModel, footerView, gameController);
        this.gameController = new GLController(gameModel, this, bundle, footerView);
    }

    /**
     * Creates and displays the main game user interface.
     */
    public void createAndShowView() {
        Scene scene = new Scene(createGameUI(), WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createGameUI() {
        BorderPane root = new BorderPane();

        InputStream  iconImageU = getClass().getResourceAsStream("/resources/AC-logo.png");
		if (iconImageU != null) {
			Image iconImage = new Image(iconImageU);
			primaryStage.getIcons().add(iconImage);
		}

        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(menuBarView.createMenuBar(), headerView.getHeader());
        root.setTop(topContainer);

        StackPane boardContainer = new StackPane();
        boardContainer.setAlignment(Pos.CENTER);
        boardPane.setPrefSize(CELL_SIZE * board_width, CELL_SIZE * board_height);
        boardContainer.getChildren().add(boardPane);
        BorderPane.setMargin(boardContainer, new Insets(10));

        VBox footerContainer = new VBox();
        VBox footer = footerView.createFooter();
        root.setCenter(boardContainer);
        footerContainer.getChildren().add(footer);
        root.setBottom(footerContainer);

        ScrollPane scrollPane = new ScrollPane(boardPane);
        root.setCenter(scrollPane);

        return root;
    }

    /**
     * Changes the user interface language based on the provided resource bundle.
     * 
     * @param newBundle The new resource bundle for localization.
     */
    public void changeLanguage(ResourceBundle newBundle) {
        this.bundle = newBundle;
        this.primaryStage.setTitle(bundle.getString("mainTitle"));
    }

    /**
     * Displays an alert with an invalid input message to the user.
     * 
     * @param message The message to display in the alert.
     */
    public void showInvalidInputAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(bundle.getString("invalidInputTitle"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets the LanguageManager instance for this class.
     * 
     * @param languageManager The LanguageManager instance.
     */
    public void setLanguageManager(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }
}
