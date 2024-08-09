package gl.view;

import java.io.InputStream;
import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The {@code GLChangeBoardSizePopUp} class represents a dialog for changing the
 * board size in the Game of Life application. It allows the user to input new
 * width and height for the board.
 * 
 * This class handles the creation and functionality of the change board size
 * dialog.
 * 
 * @author David Burchat
 * @author Marcos Astudillo
 */
public class GLChangeBoardSizePopUp {

	/**
	 * The footer view instance associated with the main view.
	 */
	private GLFooter footerView;

	/**
	 * The ResourceBundle used for localization.
	 */
	private ResourceBundle bundle;

	/**
	 * The submit button used in the change board size dialog.
	 */
	private Button submitButton;

	/**
	 * The title for the change board size dialog.
	 */
	private String changeBoardSizeDialog;

	/**
	 * The primary stage of the application.
	 */
	private Stage primaryStage;


	/**
	 * Constructs a new GLChangeBoardSizePopUp.
	 *
	 * @param bundle       The ResourceBundle for localization.
	 * @param primaryStage The primary Stage of the application.
	 * @param glFooter     The GLFooter instance for changing the board size.
	 */
	public GLChangeBoardSizePopUp(ResourceBundle bundle, Stage primaryStage, GLFooter glFooter) {
		this.bundle = bundle;
		this.primaryStage = primaryStage;
		this.footerView = glFooter;
	}

	/**
	 * Opens a dialog to change the board size.
	 */
	public void openChangeBoardSizeDialog() {
		StackPane dialogContainer = new StackPane();
		BorderPane borderPane = new BorderPane();
		Stage changeBoardSizeStage = new Stage();

		changeBoardSizeDialog = bundle.getString("changeBoardSizeDialogTitle");

		changeBoardSizeStage.initModality(Modality.APPLICATION_MODAL);

		InputStream iconImageU = getClass().getResourceAsStream("/resources/AC-logo.png");
		if (iconImageU != null) {
			Image iconImage = new Image(iconImageU);
			changeBoardSizeStage.getIcons().add(iconImage);
		}

		changeBoardSizeStage.initOwner(primaryStage);
		changeBoardSizeStage.setTitle(changeBoardSizeDialog);

		Label title = new Label(changeBoardSizeDialog);
		title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		title.setAlignment(Pos.CENTER);
		title.setPadding(new Insets(5));

		TextField widthField = new TextField();
		widthField.setPromptText(bundle.getString("widthLabel"));
		widthField.setMaxWidth(60);
		TextField heightField = new TextField();
		heightField.setPromptText(bundle.getString("heightLabel"));
		heightField.setMaxWidth(60);

		submitButton = new Button(bundle.getString("btnConfirm"));
		submitButton.setOnAction(e -> {
			try {
				int newWidth = Integer.parseInt(widthField.getText());
				int newHeight = Integer.parseInt(heightField.getText());
				if (newWidth > 0 && newHeight > 0) {
					changeBoardSizeStage.close();
					footerView.changeBoardSize(newWidth, newHeight);
				}
			} catch (NumberFormatException ex) {
				// Handle invalid input (e.g., non-integer input)
				System.out.println(ex);
			}
		});

		HBox inputField = new HBox(10);
		inputField.setAlignment(Pos.CENTER);
		inputField.getChildren().addAll(widthField, heightField);

		VBox content = new VBox(12);
		content.setAlignment(Pos.CENTER);
		content.getChildren().addAll(inputField, submitButton);

		borderPane.setTop(title);
		borderPane.setCenter(content);

		dialogContainer.getChildren().add(borderPane);
		Scene scene = new Scene(dialogContainer, 250, 100);
		changeBoardSizeStage.setScene(scene);
		changeBoardSizeStage.showAndWait();
	}
}
