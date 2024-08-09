package gl.view;

import javafx.stage.Stage;

/**
 * A custom stage class that extends JavaFX's Stage class. It adds additional
 * functionality for debugging purposes, such as printing messages when the hide
 * and close methods are called.
 * 
 * This class is designed for debugging purposes and can be used to log
 * information about when the hide and close methods are called.
 * 
 * @author David Burchat
 * @author Marcos Astudillo
 */
public class GLDebugStage extends Stage {

	/**
	 * Overrides the hide method of the Stage class and adds a custom message print.
	 */
	@Override
	public void hide() {
		super.hide();
		// Uncomment the line below to print the stack trace for debugging.
		// new Exception().printStackTrace();
		// System.out.println("Hide was called!");
	}

	/**
	 * Overrides the close method of the Stage class. Note: The original print
	 * message has been commented out.
	 */
	@Override
	public void close() {
		super.close();
		// Uncomment the line below to add a custom message when close is called.
		// System.out.println("Close was called!");
	}
}
