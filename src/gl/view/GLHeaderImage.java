package gl.view;

import java.io.InputStream;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * The {@code GLHeaderImage} class represents a header view containing a custom image.
 * This view is used as part of the main game layout to display a header image at the top
 * of the game window.
 * 
 * The header image is loaded from a resource and displayed within a {@code StackPane}.
 * 
 * @author David Burchat
 * @author Marcos Astudillo
 */
public class GLHeaderImage {

	/**
	 * The ImageView for displaying the header image.
	 */
	private ImageView headerImageView;

	/**
	 * The ResourceBundle used for localization.
	 */
	private ResourceBundle bundle;

	/**
	 * The width of the game window.
	 */
	private int window_width;

	/**
	 * The fixed height of the header.
	 */
	private final int HEADER_HEIGHT = 100;


    /**
     * Constructs a new {@code GLHeaderImage} object.
     * 
     * @param bundle The ResourceBundle used for localization.
     * @param window_w The width of the game window.
     */
    public GLHeaderImage(ResourceBundle bundle, int window_w) {
        this.bundle = bundle;  // Assign the provided resource bundle for localization
        this.window_width = window_w;
        headerImageView = createHeaderImage(); // Initialize the header image view
    }

    /**
     * Creates and returns the header as a {@code StackPane}.
     * 
     * @return The {@code StackPane} containing the header image.
     */
    public StackPane getHeader() {
        StackPane headerPane = new StackPane();               // Create a new StackPane for the header
        headerPane.getChildren().addAll(headerImageView);     // Add the header image to the pane
        return headerPane;                                    // Return the constructed StackPane
    }

    /**
     * Creates and initializes the header image view.
     * 
     * @return The {@code ImageView} containing the header image.
     */
    private ImageView createHeaderImage() {
        InputStream imgUrl = getClass().getResourceAsStream("/resources/gl.png");  // Retrieve the image URL
        ImageView imageView = new ImageView();    // Create a new image view instance
        if (imgUrl != null) {   // Check if the image URL is valid
            Image image = new Image(imgUrl);  // Create a new Image object from the URL
            imageView.setImage(image);                   // Set the image to the image view
            imageView.setFitWidth(window_width);  // Adjust the width to fit the window
            imageView.setFitHeight(HEADER_HEIGHT); // Adjust the height to match the header's height
            imageView.setPreserveRatio(true);  // Preserve the image's original aspect ratio
        } else {
            System.out.println(bundle.getString("errorLoadImage"));  // Log an error if the image could not be loaded
        }
        return imageView;  // Return the initialized image view
    }
}
