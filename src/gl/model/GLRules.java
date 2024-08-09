package gl.model;

/**
 * References:
 * [1] 	C. Lipa, "Conway's Game of Life," [Online]. 
 * 		Available: https://pi.math.cornell.edu/~lipa/mec/lesson6.html. [Accessed 20 Oct 2023].
 * [2] 	StackOverFlow users, "Conways Game of Life in JavaFX," 17 Jun 2020. [Online]. 
 * 		Available: https://stackoverflow.com/questions/62430724/conways-game-of-life-in-javafx. [Accessed 21 Oct 2023].
 * [3] 	S. O., "Conway's game of life implementation in javafx," 4 Apr 2018. [Online]. 
 * 		Available: https://steemit.com/programming/@satoshio/chttps://steemit.com/programming/@satoshio/conway-s-game-of-life-implementation-in-javafx. [Accessed 20 Oct 2023].
 * [4] 	Wikipedia, "Wikipedia - Game of Life," [Online]. 
 * 		Available: https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life. [Accessed 21 Oct 2023].
 * [5] 	M. Gardner, "MATHEMATICAL GAMES The fantastic combinations of John Conway's new solitaire game "life"," Oct 1970. [Online]. 
 * 		Available: https://web.stanford.edu/class/sts145/Library/life.pdf. [Accessed 21 Oct 2023].
 */

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import support.LanguageManager;

/**
 * The {@code GLRules} class represents the rules of the Game of Life. It
 * allows setting and retrieving rules and determining the next state of a cell
 * based on these rules.
 * 
 * This class provides the functionality to manage the rules used in the Game of Life,
 * including parsing the binary rule string and determining the next state of a cell.
 * 
 * [1] [2] [3] [4] [5]
 * 
 * @author David Burchat
 * @author Marcos Astudillo
 * 
 */
public class GLRules {
	/**
	 * A list of Boolean values representing the binary rules for the Game of Life.
	 */
	private List<Boolean> rules;

	/**
	 * The ResourceBundle used for localization.
	 */
	private ResourceBundle bundle = LanguageManager.getInstance().getBundle();

	/**
	 * The message to display for an invalid binary rule based on the current language.
	 */
	private String invalidBinaryRuleMessage = bundle.getString("invalidBinaryRuleMessage");


    /**
     * Constructs a new GLRules object with the provided binary rule string.
     *
     * @param binaryRule The binary rule string representing the rules.
     * @throws IllegalArgumentException if the binary rule is invalid.
     */
    public GLRules(String binaryRule) {
        // Check if the binaryRule is null, has an invalid length, or contains
        // non-binary characters.
        if (binaryRule == null || binaryRule.length() != 18 || !binaryRule.matches("[01]*")) {
            // Print an error message to the console.
            System.out.println(invalidBinaryRuleMessage);

            // Throw an IllegalArgumentException with a localized error message.
            throw new IllegalArgumentException(bundle.getString("invalidBinaryRuleMessage"));
        }

        // Initialize the list of rules to store the parsed binary values.
        rules = new ArrayList<>();

        // Iterate through each character in the binaryRule string.
        for (char c : binaryRule.toCharArray()) {
            // Add a Boolean value to the rules list based on whether the character is '1'.
            rules.add(c == '1');
        }
    }

    /**
     * Determines whether a cell should be alive in the next generation based on the
     * current rules.
     *
     * @param isAlive         The current state of the cell.
     * @param aliveNeighbours The number of alive neighbors of the cell.
     * @return True if the cell should be alive in the next generation, false
     *         otherwise.
     * @throws IllegalArgumentException  if the number of alive neighbors is not
     *                                   between 0 and 8.
     * @throws IndexOutOfBoundsException if the rule index is out of bounds.
     */
    public boolean shouldCellBeAliveNextGeneration(boolean isAlive, int aliveNeighbours) {
        // Check if the number of alive neighbors is within the valid range (0 to 8).
        if (aliveNeighbours < 0 || aliveNeighbours > 8) {
            throw new IllegalArgumentException(
                    bundle.getString("invalidAliveNeighborsMessage") + " " + aliveNeighbours);
        }

        // Calculate the index in the rules list based on the current cell state and the
        // number of alive neighbors.
        int index = isAlive ? 9 + aliveNeighbours : aliveNeighbours;

        // Check if the calculated index is within the bounds of the rules list.
        if (index >= rules.size()) {
            throw new IndexOutOfBoundsException(bundle.getString("ruleIndexOutOfBoundsMessage") + " " + index);
        }

        // Return the rule value (true for alive, false for dead) for the next
        // generation.
        return rules.get(index);
    }

    /**
     * Gets the current rule as a binary string.
     *
     * @return The current rule as a binary string.
     */
    public String getCurrentRule() {
        // Initialize a StringBuilder to construct the binary rule string.
        StringBuilder rule = new StringBuilder();

        // Iterate through the list of rule values (true or false) and append "1" for
        // true or "0" for false.
        for (Boolean value : rules) {
            rule.append(value ? "1" : "0");
        }

        // Convert the StringBuilder to a string and return the current rule.
        return rule.toString();
    }
}
