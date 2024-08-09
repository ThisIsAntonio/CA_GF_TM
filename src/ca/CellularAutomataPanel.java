/*
 * Student Names: David Burchat & Marcos Astudillo Carrasco
 * Student Number: 040513895 & 041057439
 * Course: CST8221 Java Application Programming
 * Assignment: A12
 * Lab Section: 301
 * Program: CET-CS Level 4
 * Professor: Paulo Sousa Ph.D
 * Due Date: Oct/1/2023
 * References: See cs/CSModel.java for Reference List 
 */
package ca;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * CellularAutomataPanel is a custom JPanel that visualizes cellular automaton. 
 * It uses a matrix to represent the state of each cell and updates the matrix based on a given rule. 
 * [1][2][3][4][5][6][7][8][11][13]
 * 
 * @author David Burchat, Marcos Astudillo Carrasco
 * @version 1.0
 * @since 1.8
 * 
 */
class CellularAutomataPanel extends JPanel {

	/**
	 * Unique identifier for the serialized class.
	 */
	private static final long serialVersionUID = 4L;
	
	/**
	 * Define the number of rows for the matrix
	 */
	private int rows = 500;
	
	/**
	 * Define the number of columns for the matrix
	 */
	private int cols = 1000;
	
	/**
	 * 2D matrix to represent the state of each cell
	 */
	private int[][] matrix;					
	
	/**
	 * Binary rule string to determine the next state of each cell
	 */
	private String binaryRule;
	
	/**
	 * Constant to define the size of each cell in pixels
	 */
	private final int CELL_SIZE = 1;						// 

	/**
	 * Constructor initializes the matrix and sets the preferred size of the panel.
	 */
	public CellularAutomataPanel() {
															// Initialize the matrix with default values
		matrix = new int[rows][cols];
		initializeMatrix();
		
		setPreferredSize(new Dimension(cols * CELL_SIZE, rows * CELL_SIZE));					// Set the preferred size of the panel based on the matrix dimensions and cell size
	}

	/**
	 * Initializes the matrix with default values. By default, all cells are set to
	 * 0, except the center cell in the first row which is set to 1.
	 */
	private void initializeMatrix() {
																								// Iterate through each cell in the matrix
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				matrix[i][j] = 0;																// Set default value to 0
			}
		}
		matrix[0][cols / 2] = 1;																// Set the center cell in the first row to 1
	}

	/**
	 * Sets the binary rule for the cellular automaton.
	 * @param binaryRule The binary rule string.
	 */
	public void setRule(String binaryRule) {
		this.binaryRule = binaryRule;
	}

	/**
	 * Updates the matrix based on the binary rule. The next state of each cell is
	 * determined by its current state and the states of its neighbors. 
	 */
	public void updateMatrix() {
																								// Start from the second row since the first row is already initialized
		for (int i = 1; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
																								// Get the states of the left, center, and right neighbors
				int left = j > 0 ? matrix[i - 1][j - 1] : 0;
				int center = matrix[i - 1][j];
				int right = j < cols - 1 ? matrix[i - 1][j + 1] : 0;

																								// Convert the triplet of states to a binary string
				String triplet = left + "" + center + "" + right;

																								// Convert the binary string to an integer index
				int index = Integer.parseInt(triplet, 2);

																								// Update the cell's state based on the binary rule
				matrix[i][j] = binaryRule.charAt(7 - index) == '1' ? 1 : 0;
			}
		}
		
		repaint();																				// Repaint the panel to reflect the updated matrix
	}

	/**
	 * Custom paint method to visualize the matrix on the panel.
	 * @param g Graphics object for drawing.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

																								// Calculate offsets to center the matrix on the panel
		int xOffset = (getWidth() - (cols * CELL_SIZE)) / 2;
		int yOffset = (getHeight() - (rows * CELL_SIZE)) / 2;

																								// Iterate through each cell in the matrix
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
																								// If the cell's state is 1, draw a filled rectangle
				if (matrix[i][j] == 1) {
					g.fillRect(j * CELL_SIZE + xOffset, i * CELL_SIZE + yOffset, CELL_SIZE, CELL_SIZE);
				}
			}
		}
	}
}
