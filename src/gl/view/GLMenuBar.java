package gl.view;

import java.io.InputStream;
import java.util.ResourceBundle;

import cs.CSModel;
import gl.controller.GLController;
import gl.model.GLModel;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import support.LanguageManager;

/**
 * The GLMenuBar class provides the functionality for creating and updating a
 * menu bar in the Game of Life application. The menu bar includes options for
 * game preferences, language settings, and application information.
 * 
 * @author David Burchat
 * @author Marcos Astudillo
 */
public class GLMenuBar {

	/**
	 * The controller for managing the Game of Life application.
	 */
	private GLController gameController;

	/**
	 * The main view of the Game of Life application.
	 */
	private GLView gameView;

	/**
	 * The model for the Game of Life application.
	 */
	private GLModel gameModel;

	/**
	 * The footer view for the Game of Life application.
	 */
	private GLFooter footerView;

	/**
	 * The popup for changing the board size in the Game of Life application.
	 */
	private GLChangeBoardSizePopUp boardSizePopUp;

	/**
	 * The primary stage of the Game of Life application.
	 */
	private Stage primaryStage;

	/**
	 * The resource bundle for localization.
	 */
	private ResourceBundle bundle;

	/**
	 * The language manager instance for handling localization.
	 */
	private LanguageManager languageManager = LanguageManager.getInstance();

	/**
	 * The menu bar in the Game of Life application.
	 */
	private MenuBar menuBar;

	/**
	 * The preferences menu in the Game of Life application.
	 */
	private Menu preferencesMenu;

	/**
	 * The menu item for changing the board size in preferences.
	 */
	private MenuItem changeBoardSize;

	/**
	 * The menu item for changing cell color in preferences.
	 */
	private MenuItem changeCellColor;

	/**
	 * The multicolor option in preferences.
	 */
	private CheckMenuItem multicolorOption;

	/**
	 * The language menu in the Game of Life application.
	 */
	private Menu languageMenu;

	/**
	 * The Spanish language option in language settings.
	 */
	private MenuItem spanishOption;

	/**
	 * The English language option in language settings.
	 */
	private MenuItem englishOption;

	/**
	 * The help menu bar in the Game of Life application.
	 */
	private Menu helpBar;

	/**
	 * The help menu item in help menu.
	 */
	private MenuItem helpOption;

	/**
	 * The about us menu item in help menu.
	 */
	private MenuItem aboutUsOption;

	/**
	 * The game menu in the Game of Life application.
	 */
	private Menu gameMenu;

	/**
	 * The menu item for starting a new game.
	 */
	private MenuItem newGameMenuItem;

	/**
	 * The menu item for displaying a solution.
	 */
	private MenuItem solutionMenuItem;

	/**
	 * The menu item for returning to the main menu.
	 */
	private MenuItem returnMenuItem;

	/**
	 * The title for the help dialog.
	 */
	private String helpDialogTitle;

	/**
	 * The header for the help dialog.
	 */
	private String helpHeader;

	/**
	 * The information text for the help dialog.
	 */
	private String helpInfo;

	/**
	 * The title for the "About Us" dialog.
	 */
	private String aboutUsDialogTitle;

	/**
	 * The header for the "About Us" dialog.
	 */
	private String aboutUsHeader;

	/**
	 * The information text for the "About Us" dialog.
	 */
	private String aboutUsInfo;

	/**
	 * The button for closing dialogs.
	 */
	private Button closeButton;

	/**
	 * The height of the icons used in the menu bar.
	 */
	private final int ICO_HEIGHT = 30;

	/**
	 * The width of the icons used in the menu bar.
	 */
	private final int ICO_WIDTH = 35;

	/**
	 * Constructor initializes the GLMenuBar instance with provided parameters.
	 * 
	 * @param primaryStage The primary stage of the application.
	 * @param bundle       The resource bundle for localization.
	 * @param gameView     The game view instance.
	 * @param gameModel    The game model instance.
	 */
	public GLMenuBar(Stage primaryStage, ResourceBundle bundle, GLView gameView, GLModel gameModel, GLFooter footerV,
			GLController gameController) {
		this.bundle = bundle;
		this.primaryStage = primaryStage;
		this.gameView = gameView;
		this.gameModel = gameModel;
		this.footerView = footerV;
		bundle = languageManager.getBundle();
		closeButton = new Button(bundle.getString("btnClose"));
		boardSizePopUp = new GLChangeBoardSizePopUp(bundle, primaryStage, footerView);

		setupStage();
	}

	/**
	 * Creates the menu bar for the application.
	 * 
	 * @return The created MenuBar instance.
	 */
	public MenuBar createMenuBar() {
		menuBar = new MenuBar();

		// Game MenuBar section
		gameMenu = new Menu(bundle.getString("gameMenu"));

		// NewGame MenuBar option
		newGameMenuItem = new MenuItem(bundle.getString("newGameMenuItem"));
		InputStream imGI = getClass().getResourceAsStream("/resources/new.gif");
		if (imGI != null) {
			Image nGI = new Image(imGI);
			ImageView nGIV = new ImageView(nGI);
			nGIV.setFitWidth(ICO_HEIGHT);
			nGIV.setFitHeight(ICO_WIDTH);
			nGIV.setPreserveRatio(true);
			newGameMenuItem.setGraphic(nGIV);
		}
		newGameMenuItem.setOnAction(e -> footerView.restartItem());

		// Solution MenuBar option
		solutionMenuItem = new MenuItem(bundle.getString("solutionMenuItem"));
		InputStream imIN = getClass().getResourceAsStream("/resources/solution.gif");
		if (imIN != null) {
			Image nIN = new Image(imIN);
			ImageView nIGV = new ImageView(nIN);
			nIGV.setFitWidth(ICO_HEIGHT);
			nIGV.setFitHeight(ICO_WIDTH);
			nIGV.setPreserveRatio(true);
			solutionMenuItem.setGraphic(nIGV);
		}
		solutionMenuItem.setOnAction(e -> footerView.solutionItem());

		// Exit MenuBar option
		returnMenuItem = new MenuItem(bundle.getString("exitMenuItem"));
		InputStream imIE = getClass().getResourceAsStream("/resources/exit.gif");
		if (imIE != null) {
			Image nIE = new Image(imIE);
			ImageView nIEV = new ImageView(nIE);
			nIEV.setFitWidth(ICO_HEIGHT);
			nIEV.setFitHeight(ICO_WIDTH);
			nIEV.setPreserveRatio(true);
			returnMenuItem.setGraphic(nIEV);
		}
		returnMenuItem.setOnAction(e -> handleExit());

		// Adding newGame, solution and exit to Game section
		gameMenu.getItems().addAll(newGameMenuItem, solutionMenuItem, returnMenuItem);

		// Preferences MenuBar section
		preferencesMenu = new Menu(bundle.getString("preferencesMenu"));

		// Resize MenuBar option
		changeBoardSize = new MenuItem(bundle.getString("changeBoardSize"));
		InputStream imIR = getClass().getResourceAsStream("/resources/resize.gif");
		if (imIR != null) {
			Image nIR = new Image(imIR);
			ImageView nIVR = new ImageView(nIR);
			nIVR.setFitWidth(ICO_HEIGHT);
			nIVR.setFitHeight(ICO_WIDTH);
			nIVR.setPreserveRatio(true);
			changeBoardSize.setGraphic(nIVR);
		}
		changeBoardSize.setOnAction(e -> boardSizePopUp.openChangeBoardSizeDialog());

		// Color Pallete MenuBar option
		changeCellColor = new MenuItem(bundle.getString("changeCellColor"));
		InputStream imIC = getClass().getResourceAsStream("/resources/colorPallete.gif");
		if (imIC != null) {
			Image nIC = new Image(imIC);
			ImageView nICV = new ImageView(nIC);
			nICV.setFitWidth(ICO_HEIGHT);
			nICV.setFitHeight(ICO_WIDTH);
			nICV.setPreserveRatio(true);
			changeCellColor.setGraphic(nICV);
		}
		changeCellColor.setOnAction(e -> {
			footerView.selectedCellColor = openColorPicker(footerView.selectedCellColor);
			footerView.updateUIFromBoardState();
		});

		// Multicolor MenuBar option
		multicolorOption = new CheckMenuItem(bundle.getString("multicolorOption"));
		InputStream imIMC = getClass().getResourceAsStream("/resources/multicolor.gif");
		if (imIMC != null) {
			Image nIMC = new Image(imIMC);
			ImageView nIVMC = new ImageView(nIMC);
			nIVMC.setFitWidth(ICO_HEIGHT);
			nIVMC.setFitHeight(ICO_WIDTH);
			nIVMC.setPreserveRatio(true);
			multicolorOption.setGraphic(nIVMC);
		}
		multicolorOption.setOnAction(e -> {
			footerView.multicolorEnabled = multicolorOption.isSelected();
			footerView.updateUIFromBoardState();
		});
		// Adding changeBoardSize, color pallet and multicolor options to preferences
		// section
		preferencesMenu.getItems().addAll(changeBoardSize, changeCellColor, multicolorOption);

		// Language Menu Bar section
		languageMenu = new Menu(bundle.getString("languageBar"));

		// Spanish MenuBar option
		spanishOption = new MenuItem(bundle.getString("spanishBar"));
		InputStream imISp = getClass().getResourceAsStream("/resources/spanish.gif");
		if (imISp != null) {
			Image nISp = new Image(imISp);
			ImageView nISpV = new ImageView(nISp);
			nISpV.setFitWidth(ICO_HEIGHT);
			nISpV.setFitHeight(ICO_WIDTH);
			nISpV.setPreserveRatio(true);
			spanishOption.setGraphic(nISpV);
		}
		spanishOption.setOnAction(e -> {
			LanguageManager.getInstance().setLanguage("Español");
			updateMenuBarFromLanguageChange();
		});

		// English MenuBar option
		englishOption = new MenuItem(bundle.getString("englishBar"));
		InputStream imIEn = getClass().getResourceAsStream("/resources/englishCanada.gif");
		if (imIEn != null) {
			Image nIEn = new Image(imIEn);
			ImageView nIEnV = new ImageView(nIEn);
			nIEnV.setFitWidth(ICO_HEIGHT);
			nIEnV.setFitHeight(ICO_WIDTH);
			nIEnV.setPreserveRatio(true);
			englishOption.setGraphic(nIEnV);
		}
		englishOption.setOnAction(e -> {
			LanguageManager.getInstance().setLanguage("English");
			updateMenuBarFromLanguageChange();
		});

		// Adding Spanish and English option to language section
		languageMenu.getItems().addAll(spanishOption, englishOption);

		// Help dialog/menu bar
		helpDialogTitle = bundle.getString("titleHelpLabel");
		helpHeader = bundle.getString("helpHeaderGL");
		helpInfo = bundle.getString("helpInfoGL");

		helpBar = new Menu(bundle.getString("helpBar"));

		helpOption = new MenuItem(bundle.getString("titleHelpLabel"));
		InputStream imIH = getClass().getResourceAsStream("/resources/help.gif");
		if (imIH != null) {
			Image nIH = new Image(imIH);
			ImageView nIVH = new ImageView(nIH);
			nIVH.setFitWidth(ICO_HEIGHT);
			nIVH.setFitHeight(ICO_WIDTH);
			nIVH.setPreserveRatio(true);
			helpOption.setGraphic(nIVH);
		}
		helpOption.setOnAction(e -> showHelpDialog());

		// About us dialog/menu bar
		aboutUsDialogTitle = bundle.getString("titleAboutUsLabel");
		aboutUsHeader = bundle.getString("aboutUsHeaderGL");
		aboutUsInfo = bundle.getString("aboutUsInfoGL");

		aboutUsOption = new MenuItem(bundle.getString("titleAboutUsLabel"));
		InputStream imIAU = getClass().getResourceAsStream("/resources/aboutUs.gif");
		if (imIAU != null) {
			Image nIAU = new Image(imIAU);
			ImageView nIVAU = new ImageView(nIAU);
			nIVAU.setFitWidth(ICO_HEIGHT);
			nIVAU.setFitHeight(ICO_WIDTH);
			nIVAU.setPreserveRatio(true);
			aboutUsOption.setGraphic(nIVAU);
		}
		aboutUsOption.setOnAction(e -> showAboutUsDialog());
		helpBar.getItems().addAll(helpOption, aboutUsOption);

		menuBar.getMenus().addAll(gameMenu, preferencesMenu, languageMenu, helpBar);
		return menuBar;
	}

	/**
	 * Opens a color picker dialog to choose a color.
	 * 
	 * @param initialColor The initially selected color.
	 * @return The selected color.
	 */
	private Color openColorPicker(Color initialColor) {
		GLColorPalettePopup colorPalettePopup = new GLColorPalettePopup(bundle);
		return colorPalettePopup.openColorPalette(initialColor);
	}

	/**
	 * Displays a help dialog with information about the application.
	 */
	private void showHelpDialog() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		InputStream iconImageU = getClass().getResourceAsStream("/resources/AC-logo.png");
		if (iconImageU != null) {
			Image iconImage = new Image(iconImageU);
			alertStage.getIcons().add(iconImage);
		}
		alert.setTitle(helpDialogTitle);
		alert.setHeaderText(helpHeader);
		alert.setContentText(helpInfo);
		alert.showAndWait();
	}

	/**
	 * Displays an "about us" dialog with information about the application's
	 * developers.
	 */
	private void showAboutUsDialog() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		InputStream iconImageU = getClass().getResourceAsStream("/resources/AC-logo.png");
		if (iconImageU != null) {
			Image iconImage = new Image(iconImageU);
			alertStage.getIcons().add(iconImage);
		}

		alert.setTitle(aboutUsDialogTitle);
		alert.setHeaderText(aboutUsHeader);
		alert.setContentText(aboutUsInfo);
		alert.showAndWait();
	}

	/**
	 * Sets up the game stage by initializing the game controller.
	 */
	private void setupStage() {
		this.gameController = new GLController(gameModel, gameView, bundle, footerView);
	}

	/**
	 * Handles the action of exiting the application.
	 */
	private void handleExit() {
		new CSModel().setVisible(true);
		primaryStage.close();
		Platform.exit();
	}

	/**
	 * Updates the menu bar after a language change.
	 */
	public void updateMenuBarFromLanguageChange() {
		bundle = languageManager.getBundle();

		preferencesMenu.setText(bundle.getString("preferencesMenu"));
		changeBoardSize.setText(bundle.getString("changeBoardSize"));
		changeCellColor.setText(bundle.getString("changeCellColor"));
		multicolorOption.setText(bundle.getString("multicolorOption"));

		languageMenu.setText(bundle.getString("languageBar"));
		spanishOption.setText(bundle.getString("spanishBar"));
		englishOption.setText(bundle.getString("englishBar"));

		helpBar.setText(bundle.getString("helpBar"));
		helpOption.setText(bundle.getString("titleHelpLabel"));
		aboutUsOption.setText(bundle.getString("titleAboutUsLabel"));

		gameMenu.setText(bundle.getString("gameMenu"));
		newGameMenuItem.setText(bundle.getString("newGameMenuItem"));
		solutionMenuItem.setText(bundle.getString("solutionMenuItem"));
		returnMenuItem.setText(bundle.getString("exitMenuItem"));

		helpDialogTitle = bundle.getString("titleHelpLabel");
		helpHeader = bundle.getString("helpHeaderGL");
		helpInfo = bundle.getString("helpInfoGL");

		aboutUsDialogTitle = bundle.getString("titleAboutUsLabel");
		aboutUsHeader = bundle.getString("aboutUsHeaderGL");
		aboutUsInfo = bundle.getString("aboutUsInfoGL");
		closeButton.setText(bundle.getString("btnClose"));

		gameController.changeLanguage(bundle);
		gameModel.changeLanguage(bundle);
		gameView.changeLanguage(bundle);
		footerView.changeLanguage(bundle);
		footerView.updateUIFromBoardState();
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
