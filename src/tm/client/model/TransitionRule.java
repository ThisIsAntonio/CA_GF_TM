package tm.client.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a transition rule in a Turing machine. Each rule specifies the
 * behavior of the machine when it is in a certain state and reads a specific
 * symbol on the tape.
 */
public class TransitionRule {
	/**
	 * Current state of the Turing machine
	 */
	private int currentState;
	/**
	 * Symbol read from the tape
	 */
	private char readSymbol;
	/**
	 * Symbol to write to the tape
	 */
	private char writeSymbol;
	/**
	 * Direction to move the tape head ('L' or 'R')
	 */
	private char moveDirection;
	/**
	 * New state to transition to
	 */
	private int newState;

	/**
	 * Default Constructor (Here is not used it)
	 */
	public TransitionRule() {}
	/**
	 * Constructs a transition rule with specified parameters.
	 * 
	 * @param currentState  The current state of the Turing machine.
	 * @param readSymbol    The symbol read from the tape.
	 * @param writeSymbol   The symbol to write to the tape.
	 * @param moveDirection The direction to move the tape head.
	 * @param newState      The new state of the Turing machine after the
	 *                      transition.
	 * @throws IllegalArgumentException If the move direction is not valid.
	 */
	public TransitionRule(int currentState, char readSymbol, char writeSymbol, char moveDirection, int newState) {
		if (!isValidDirection(moveDirection)) {
			throw new IllegalArgumentException("Dirección de movimiento inválida: " + moveDirection);
		}
		this.currentState = currentState;
		this.readSymbol = readSymbol;
		this.writeSymbol = writeSymbol;
		this.moveDirection = moveDirection;
		this.newState = newState;
	}

	/**
	 * This method checks if the provided direction for tape head movement is valid.
	 *
	 * @param direction The direction character ('L' for left, 'R' for right).
	 * @return {@code true} if the direction is either 'L' or 'R', {@code false} otherwise.
	 */
	private boolean isValidDirection(char direction) {
	    return direction == 'L' || direction == 'R';
	}

	/**
	 * Gets the current state of the Turing machine for this transition rule.
	 *
	 * @return The current state as an integer.
	 */
	public int getCurrentState() {
	    return currentState;
	}

	/**
	 * Gets the symbol read from the tape by this transition rule.
	 *
	 * @return The read symbol as a character.
	 */
	public char getReadSymbol() {
	    return readSymbol;
	}

	/**
	 * Gets the symbol to be written to the tape by this transition rule.
	 *
	 * @return The write symbol as a character.
	 */
	public char getWriteSymbol() {
	    return writeSymbol;
	}

	/**
	 * Gets the direction in which the tape head should move after applying this transition rule.
	 *
	 * @return The move direction as a character ('L' or 'R').
	 */
	public char getMoveDirection() {
	    return moveDirection;
	}

	/**
	 * Gets the new state the Turing machine should transition to after applying this rule.
	 *
	 * @return The new state as an integer.
	 */
	public int getNewState() {
	    return newState;
	}


    /**
     * Checks if the rule matches the given state and symbol.
     * 
     * @param state The current state of the Turing machine.
     * @param symbol The symbol read from the tape.
     * @return {@code true} if the rule matches the state and symbol, {@code false} otherwise.
     */
	public boolean matches(int state, char symbol) {
		return currentState == state && readSymbol == symbol;
	}

	/**
	 * Converts a string representation of transition rules into a list of TransitionRule objects.
	 *
	 * @param rulesText The string containing the transition rules.
	 * @return A list of TransitionRule objects.
	 */
	public static List<TransitionRule> fromString(String rulesText) {
	    List<TransitionRule> rulesList = new ArrayList<>();

	    // Splits the input string into individual rule strings based on whitespace.
	    String[] rules = rulesText.trim().split("\\s+");

	    for (String singleRule : rules) {
	        // Checks if each rule string is of the expected length (5 characters).
	        if (singleRule.length() != 5) {
	            throw new IllegalArgumentException("Invalid transition rule format: " + singleRule);
	        }

	        // Parses individual components of the rule from the string.
	        int currentState = Character.getNumericValue(singleRule.charAt(0));
	        char readSymbol = singleRule.charAt(1);
	        char writeSymbol = singleRule.charAt(3);
	        // Determines the move direction based on the character ('0' for 'L', else 'R').
	        char moveDirection = singleRule.charAt(4) == '0' ? 'L' : 'R';
	        int newState = Character.getNumericValue(singleRule.charAt(2));

	        // Validates the states and symbols to ensure they are within expected ranges/values.
	        if (currentState < 0 || currentState > 9 || newState < 0 || newState > 9) {
	            throw new IllegalArgumentException("Invalid state in transition rule: " + singleRule);
	        }
	        if (readSymbol != '0' && readSymbol != '1' || writeSymbol != '0' && writeSymbol != '1') {
	            throw new IllegalArgumentException("Invalid symbol in transition rule: " + singleRule);
	        }

	        // Adds the constructed TransitionRule object to the list.
	        rulesList.add(new TransitionRule(currentState, readSymbol, writeSymbol, moveDirection, newState));
	    }

	    // Returns the list of transition rules.
	    return rulesList;
	}


    /**
     * Returns a string representation of this transition rule.
     * 
     * @return A string representation of the transition rule.
     */
	@Override
	public String toString() {
		return currentState + " " + readSymbol + " " + newState + " " + writeSymbol + " "
				+ (moveDirection == 'L' ? '0' : '1');
	}
}
