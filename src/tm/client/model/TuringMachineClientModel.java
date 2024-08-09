package tm.client.model;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TuringMachineClientModel {
	/**
	 * Assuming '0' as the blank symbol on the tape.
	 */
	private static final char BLANK_SYMBOL = '0';
	/**
	 * The tape of the Turing Machine.
	 */
	private String tape;
	/**
	 * The current position of the read/write head on the tape.
	 */
	private int headPosition;
	/**
	 * The current state of the Turing Machine.
	 */
	private int currentState;
	/**
	 * Protocol for communication with the server.
	 */
	private ClientProtocol protocol;
	/**
	 * Transition rules of the Turing Machine.
	 */
	private List<TransitionRule> transitionRules;
	/**
	 * Listener for state updates.
	 */
	private TuringMachineStateListener stateListener;
	/**
	 * Counter for the number of steps executed.
	 */
	private int stepCount = 0;
	/**
	 * Flag indicating if the final state has been reached.
	 */
	private boolean isFinalState = false;

	/**
     * Interface for state update notifications.
     */
	public interface TuringMachineStateListener {
		void onStateUpdated(String tape, int headPosition, int currentState);

		void onFinalStateReached(String tape, int headPosition, int currentState); // Método nuevo para estados finales.
	}

	/**
	 * Default Constructor  (Here is not used it)
	 */
	public TuringMachineClientModel() {}
	
    /**
     * Constructor for TuringMachineClientModel.
     * Initializes the Turing Machine with a given initial tape and head position.
     * @param initialTape The initial tape for the Turing Machine.
     * @param initialHeadPosition The initial position of the head on the tape.
     */
	public TuringMachineClientModel(String initialTape, int initialHeadPosition) {
		tape = initialTape;
		headPosition = initialHeadPosition;
		currentState = 1;
		transitionRules = new ArrayList<>();
	}

    /**
     * Sets the transition rules for the Turing Machine.
     */
	public void setTransitionRules(List<TransitionRule> transitionRules) {
		this.transitionRules = transitionRules;
	}
	
	/**
	 * Sets the current step count of the Turing Machine's execution.
	 * 
	 * @param step The new step count to be set.
	 */
	public void setStepCount(int step) {
	    this.stepCount = step;
	}

	/**
	 * Sets the current state of the Turing Machine.
	 * 
	 * @param state The new state to be set.
	 */
	public void setCurrentState(int state) {
	    this.currentState = state;
	}

	/**
	 * Executes a single step of the Turing Machine, applying a transition rule based on the current state and symbol.
	 * Throws an exception if transition rules are not set. If a rule is applied, it updates the machine's state, 
	 * position of the read/write head, and potentially marks it as a final state.
	 */
	public void executeStep() {
	    // Ensure that transition rules are set before execution
	    if (transitionRules == null || transitionRules.isEmpty()) {
	        throw new IllegalStateException("Transition rules must be set before execution.");
	    }

	    char currentSymbol = getCurrentSymbol();
	    boolean ruleApplied = false;

	    // Debug print current state and symbol
	    System.out.println("Current state: " + currentState + ", Current symbol: " + currentSymbol);

	    // Iterate over transition rules to find a matching rule
	    for (TransitionRule rule : transitionRules) {
	        // Debug print each rule
	        System.out.println("Checking rule: " + rule);
	        if (rule.matches(currentState, currentSymbol)) {
	            // Debug print applied rule
	            System.out.println("Applying rule: " + rule);
	            applyRule(rule);
	            ruleApplied = true;
	            break;
	        }
	    }

	    // Debug print if no rule is applied
	    if (!ruleApplied) {
	        System.out.println("No rule applied.");
	    }

	    // Update step count and check if it's the final state
	    if (ruleApplied) {
	        stepCount++;
	        if (headPosition >= tape.length()) {
	            System.out.println("Read/write head has reached the end of the tape.");
	            isFinalState = true; // Set as a final state
	            notifyFinalStateReached();
	            return; // Stop further execution
	        }
	    }

	    // Notify if a final state is reached or update the listener
	    isFinalState = isFinalState(currentState);
	    if (isFinalState || !ruleApplied) {
	        notifyFinalStateReached();
	    } else {
	        notifyStateListener(); // Notify any state change.
	    }
	}

	/**
	 * Applies a transition rule to the Turing Machine. It writes a symbol to the tape,
	 * updates the head position, and changes the current state.
	 * 
	 * @param rule The transition rule to be applied.
	 */
	private void applyRule(TransitionRule rule) {
	    // Write the symbol specified by the rule to the tape
	    writeSymbolToTape(rule.getWriteSymbol());
	    // Update the head position based on the rule's direction
	    updateHeadPosition(rule.getMoveDirection());
	    // Update the current state to the new state defined in the rule
	    currentState = rule.getNewState(); 
	    // Debugging: Print the new state and the current tape status
	    System.out.println("New state: " + currentState + ", Tape: " + getTapeWithHeadPosition());
	}

	/**
	 * Retrieves the symbol at a specified position on the tape.
	 * If the position is outside the bounds of the tape, it returns a blank symbol.
	 * 
	 * @param position The position on the tape to get the symbol from.
	 * @return The symbol at the specified position or the blank symbol if out of bounds.
	 */
	public char getSymbolAt(int position) {
	    // Check for bounds and return a blank symbol if out of bounds
	    if (position < 0 || position >= tape.length()) {
	        return BLANK_SYMBOL; // Assume blank symbols beyond tape boundaries
	    }
	    return tape.charAt(position);
	}

	/**
	 * Writes a symbol to the tape at the current head position.
	 * Extends the tape if needed.
	 * 
	 * @param symbol The symbol to write to the tape.
	 */
	private void writeSymbolToTape(char symbol) {
	    // Extend the tape if the head is beyond the current length
	    if (headPosition >= tape.length()) {
	        extendTapeIfNeeded();
	    }
	    // Replace the symbol at the current head position
	    tape = tape.substring(0, headPosition) + symbol + tape.substring(headPosition + 1);
	}

	/**
	 * Updates the head position based on the move direction.
	 * Prevents the head from moving beyond the start of the tape.
	 * 
	 * @param moveDirection The direction in which to move the head ('L' for left, 'R' for right).
	 */
	private void updateHeadPosition(char moveDirection) {
	    if (moveDirection == 'L') {
	        // Move head left, ensuring it doesn't go past the start of the tape
	        headPosition = Math.max(0, headPosition - 1);
	    } else if (moveDirection == 'R') {
	        // Move head right and extend the tape if needed
	        headPosition++;
	        extendTapeIfNeeded();
	    }
	}


	/**
	 * Extends the tape with a blank symbol if the head moves beyond the current end of the tape.
	 */
	private void extendTapeIfNeeded() {
	    // Check if the head is at or beyond the end of the tape
	    if (headPosition >= tape.length()) {
	        // Append a blank symbol to the end of the tape
	        tape += BLANK_SYMBOL;
	    }
	}

	/**
	 * Retrieves the current symbol at the head position on the tape.
	 * Returns a blank symbol if the head is beyond the end of the tape.
	 * 
	 * @return The current symbol at the head position or a blank symbol if out of bounds.
	 */
	private char getCurrentSymbol() {
	    // Return the symbol at the head position or a blank symbol if head is beyond tape length
	    return headPosition < tape.length() ? tape.charAt(headPosition) : BLANK_SYMBOL;
	}

	/**
	 * Resets the tape to a new specified tape and reinitializes the head position.
	 * Sets the head position to the middle of the new tape.
	 * 
	 * @param newTape The new tape to be set.
	 */
	public void resetTape(String newTape) {
	    // Debugging: Print a reset step message
	    System.out.println("modelresetStep");
	    // Set the tape to the new specified tape
	    tape = newTape;
	    // Initialize the head position to the middle of the new tape
	    headPosition = tape.length() / 2;
	}


	/**
	 * Notifies the state listener when a final state is reached in the Turing machine.
	 * This method is called when the machine reaches a state where it can no longer proceed.
	 */
	private void notifyFinalStateReached() {
	    // If a state listener is set, notify it with the current tape, head position, and state
	    if (stateListener != null) {
	        stateListener.onFinalStateReached(tape, headPosition, currentState);
	    }
	}

	/**
	 * Notifies the state listener of any updates in the state of the Turing machine.
	 * This method is called whenever the machine's state changes.
	 */
	private void notifyStateListener() {
	    // If a state listener is set, notify it with the current tape, head position, and state
	    if (stateListener != null) {
	        stateListener.onStateUpdated(tape, headPosition, currentState);
	    }
	}

	/**
	 * Sets the protocol for communicating with the server.
	 * 
	 * @param protocol The protocol to be used for server communication.
	 */
	public void setProtocol(ClientProtocol protocol) {
	    this.protocol = protocol;
	}

	/**
	 * Returns the current tape of the Turing machine.
	 * 
	 * @return The current tape as a String.
	 */
	public String getTape() {
	    return tape;
	}

	/**
	 * Returns the current head position on the tape of the Turing machine.
	 * 
	 * @return The current head position as an integer.
	 */
	public int getHeadPosition() {
	    return headPosition;
	}

	/**
	 * Returns the current state of the Turing machine.
	 * 
	 * @return The current state as an integer.
	 */
	public int getCurrentState() {
	    return currentState;
	}

	/**
	 * Returns the current step count of the Turing machine.
	 * This count represents the number of steps the machine has executed.
	 * 
	 * @return The current step count as an integer.
	 */
	public int getCurrentStep() {
	    return stepCount;
	}

	/**
	 * Checks if the Turing machine has reached a final state.
	 * 
	 * @return True if the machine is in a final state, false otherwise.
	 */
	public boolean isFinalState() {
	    return isFinalState;
	}

	/**
	 * Sends a message to the server using the established protocol.
	 * 
	 * @param message The message to be sent to the server.
	 * @param bundle  The resource bundle for localization purposes.
	 */
	public void sendMessage(String message, ResourceBundle bundle) {
	    protocol.sendMessageToServer(message, false, bundle);
	}


	/**
	 * Updates the tape and transition rules of the Turing machine based on input from the GUI.
	 * This method is used to synchronize the model with changes made through the GUI.
	 *
	 * @param newTape  The new tape to be set in the Turing machine.
	 * @param newRules The new set of transition rules to be applied.
	 */
	public void updateFromGUI(String newTape, List<TransitionRule> newRules) {
	    this.tape = newTape; // Update the tape with the new input from GUI.
	    this.transitionRules = newRules; // Update the transition rules.
	    // Set the head position to the middle of the tape or based on some other logic.
	    this.headPosition = tape.length() / 2; 

	    // Notify the GUI about the updated state of the machine.
	    if (stateListener != null) {
	        stateListener.onStateUpdated(tape, headPosition, currentState);
	    }
	}

	/**
	 * Sets the state listener for the Turing machine.
	 * The listener is notified about changes in the machine's state.
	 *
	 * @param listener The listener to be notified about state updates.
	 */
	public void setStateListener(TuringMachineStateListener listener) {
	    this.stateListener = listener; // Set the state listener.
	}

	/**
	 * Returns a string representation of the current tape with the head position highlighted.
	 * This method is useful for visualizing the current state of the tape and the position of the head.
	 *
	 * @return A string representation of the tape with the head position indicated.
	 */
	public String getTapeWithHeadPosition() {
	    // Handle empty tape case.
	    if (tape == null || tape.isEmpty()) {
	        return "[ ]"; // Return a representation for an empty tape.
	    }

	    // Adjust head position if it's out of tape bounds.
	    if (headPosition < 0 || headPosition >= tape.length()) {
	        // Adjust head position to be within the bounds of the tape.
	        headPosition = Math.max(0, Math.min(headPosition, tape.length() - 1));
	    }

	    // Create a string representation of the tape with the head position highlighted.
	    String headSymbol = headPosition < tape.length() ? String.valueOf(tape.charAt(headPosition)) : " ";
	    // Concatenate the tape parts with the head symbol in brackets.
	    return tape.substring(0, headPosition) + "[" + headSymbol + "]" + tape.substring(headPosition + 1);
	}


	/**
	 * Retrieves the list of transition rules currently set for the Turing machine.
	 * 
	 * @return A list of {@link TransitionRule} objects representing the transition rules of the machine.
	 */
	public List<TransitionRule> getTransitionRules() {
	    return transitionRules;
	}

	/**
	 * Provides a string representation of all transition rules of the Turing machine.
	 * This method formats each rule as a string and concatenates them, separating each rule by a newline.
	 * 
	 * @return A string representing all the transition rules in the Turing machine.
	 */
	public String getTransitionRulesAsString() {
	    StringBuilder sb = new StringBuilder();
	    for (TransitionRule rule : transitionRules) {
	        sb.append(rule.toString()).append("\n"); // Append each rule and a newline to the StringBuilder.
	    }
	    return sb.toString().trim(); // Return the concatenated string, trimming any trailing whitespace.
	}

	/**
	 * Determines whether a given state is a final state, acceptance state, or an error state.
	 * This method should be customized based on the specific final states of your Turing machine.
	 * 
	 * @param state The state to be checked.
	 * @return {@code true} if the state is a final state; {@code false} otherwise.
	 */
	private boolean isFinalState(int state) {
	    // Here, states 0, 3, and 4 are considered final states.
	    return state == 0 || state == 3 || state == 4;
	}

}
