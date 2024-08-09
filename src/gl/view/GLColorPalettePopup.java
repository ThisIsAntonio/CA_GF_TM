package gl.view;

import java.io.InputStream;
import java.util.ResourceBundle;

import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The {@code GLColorPalettePopup} class represents a popup window for selecting
 * colors from a predefined palette or using a custom color picker.
 * 
 * This class provides a graphical user interface for users to choose a color.
 * 
 * @author David Burchat
 * @author Marcos Astudillo
 */
public class GLColorPalettePopup {

	/**
	 * The selected color used in the application.
	 */
	private Color selectedColor = Color.WHITE;

	/**
	 * The size of rectangles used in the application.
	 */
	private int rectangleSize = 25;

	/**
	 * The ResourceBundle used for localization.
	 */
	private ResourceBundle bundle;


    /**
     * Constructs a new GLColorPalettePopup.
     * 
     * @param bundle The ResourceBundle used for localization.
     */
    public GLColorPalettePopup(ResourceBundle bundle) {
        this.bundle = bundle; // Assign the provided resource bundle for localization
    }

    /**
     * Opens a color palette for the user to select a color.
     * 
     * @param initialColor The initially selected color when the palette opens.
     * @return The color selected by the user.
     */
    public Color openColorPalette(Color initialColor) {
        Stage colorStage = new Stage();   // Create a new stage for the popup
        colorStage.initModality(Modality.APPLICATION_MODAL);  // Set modality to block other windows
        colorStage.initStyle(StageStyle.UTILITY); // Set simple style without decorations

        // Set icon and title for the popup
        InputStream iconImageU = getClass().getResourceAsStream("/resources/AC-logo.png");
		if (iconImageU != null) {
			Image iconImage = new Image(iconImageU);
			colorStage.getIcons().add(iconImage);
		}
        colorStage.setTitle(bundle.getString("colorPaletteTitle"));

        GridPane grid = new GridPane(); // Create a grid layout for color rectangles

        // Predefined set of colors for the palette
		Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.PINK, Color.LIGHTBLUE, Color.GREY,
				Color.ORANGE, Color.MEDIUMPURPLE, Color.BLACK, Color.BROWN, Color.CYAN, Color.DARKBLUE, Color.DARKCYAN,
				Color.DARKGOLDENROD, Color.DARKGRAY, Color.DARKGREEN, Color.DARKKHAKI, Color.DARKMAGENTA,
				Color.DARKOLIVEGREEN, Color.DARKORANGE, Color.DARKORCHID, Color.AQUA, Color.DARKSALMON,
				Color.DARKSEAGREEN, Color.DARKSLATEBLUE, Color.DARKSLATEGRAY, Color.DARKOLIVEGREEN, Color.DARKVIOLET,
				Color.GREENYELLOW, Color.DEEPSKYBLUE, Color.DIMGRAY, Color.DODGERBLUE, Color.FIREBRICK, Color.ROSYBROWN,
				Color.FORESTGREEN, Color.FUCHSIA, Color.GAINSBORO, Color.GHOSTWHITE, Color.GOLD, Color.GOLDENROD };

		int col = 0;
        int row = 0;
        for (Color color : colors) {
            Rectangle rect = new Rectangle(rectangleSize, rectangleSize, color);  // Create color rectangle
            rect.setStroke(Color.BLACK);  // Set rectangle border color
            rect.setStrokeWidth(1);  // Set rectangle border width
            rect.setOnMouseClicked(e -> {
                selectedColor = color;  // Update the selected color
                colorStage.close();     // Close the popup
            });

            grid.add(rect, col, row);    // Add rectangle to the grid
            col++;  // Move to next column
            if (col == 6) {  // Check if end of row is reached
                col = 0;     // Reset to first column
                row++;       // Move to next row
            }
        }

        // Add custom color picker for additional color choices
        ColorPicker customColorPicker = new ColorPicker();
        customColorPicker.setPromptText(bundle.getString("customColorPrompt"));  // Set placeholder text
        customColorPicker.setValue(initialColor);  // Set initial color for the picker
        customColorPicker.setOnAction(e -> {
            selectedColor = customColorPicker.getValue(); // Update the selected color
            colorStage.close();    // Close the popup
        });

        VBox root = new VBox(10);     // Create root layout with vertical spacing
        root.getChildren().add(grid); // Add color grid to root layout
        root.getChildren().add(customColorPicker); // Add color picker to root layout

        Scene scene = new Scene(root);    // Create a new scene with the root layout
        colorStage.setScene(scene);   // Set scene for the popup stage
        colorStage.sizeToScene();     // Adjust the popup size
        colorStage.showAndWait();     // Show the popup and wait

        return selectedColor; // Return the selected color
    }
}
