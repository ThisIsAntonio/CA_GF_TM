package gl.view;

/**
 * References:
 * [1] 	J. Jenkov, "Creating and Starting Threads," 09 Mar 2021. [Online]. 
 * 		Available: https://jenkov.com/tutorials/java-concurrency/creating-and-starting-threads.html. [Accessed 22 Oct 2023].
 * [2] 	TutorialsPoint Staff, "Creating and Using a Run Configuration," [Online]. 
 *  	Available: https://www.tutorialspoint.com/eclipse/eclipse_run_configuration.htm. [Accessed 20 Oct 2023].
 * [3] 	E. Eden-Rump, "Creating new Windows in JavaFX," 26 Jun 2021. [Online]. 
 *		Available: https://edencoding.com/new-windows-stage/. [Accessed 20 Oct 2023].
 * [4] 	"Iniciar una segunda ventana javaFx," 26 Aug 2019. [Online]. 
 *		Available: https://es.stackoverflow.com/questions/283058/iniciar-una-segunda-ventana-javafx. [Accessed 24 Oct 2023].
 * 
 */

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main class for launching the Game of Life application. This class extends
 * JavaFX's Application class and is responsible for starting the application.
 * 
 * The Game of Life application is launched by calling the main method or the
 * {@code launchGL} method to ensure it is started only once.
 * 
 * [1] [2] [3] [4]
 * 
 * @author David Burchat
 * @author Marcos Astudillo
 * 
 */
public class GLMain extends Application {
	
	/**
	 * The primary stage of the Game of Life application.
	 */
	private Stage primaryStage;

	/**
	 * The main view of the Game of Life application.
	 */
	private GLView glView;

	/**
	 * A flag indicating whether the Game of Life application has been launched.
	 */
	private static boolean glLaunched = false;


    /**
     * The main method that launches the application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(GLMain.class, args);
    }

    /**
     * The overridden start method from JavaFX's Application class.
     * 
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Game of Life");

        int board_width = 40;
        int board_height = 40;

        // Create and show the GLView, which contains the Game of Life simulation.
        glView = new GLView(primaryStage, board_width, board_height);
        glView.createAndShowView();
    }

    /**
     * Gets the primary stage of the application.
     *
     * @return The primary stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Launches the Game of Life application if it has not already been launched.
     * This method ensures that the application is started only once to prevent
     * multiple instances.
     */
    public static void launchGL() {
        // Check if the GL application has already been launched.
        if (!glLaunched) {
            // Launch the GL application.
            Application.launch(GLMain.class);
            glLaunched = true; // Mark that the GL application has been launched.
        }
    }
}
