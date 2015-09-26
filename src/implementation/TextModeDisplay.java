package implementation;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import server.BoardNeighbor;

/**
 * Text mode representation of a board
 */
public class TextModeDisplay {
	//AF: Represents the board as a 2D array of String. 
    //RI: A valid Display has height = 20 and width = 20 and a 2D array of strings. 

    private final int height;
    private final int width;
    private String[][] board;
    private final String empty = " ";
    private BoardNeighbor neighbors;
    
    /**
     * Initializes the board display as a 2D array of the "empty" String
     * @param height of board
     * @param width of board
     */
    public TextModeDisplay(int width, int height) {
        this.height = height;
        this.width = width;
        this.board = new String[width][height];
        for (int i=0; i<width; i++) {Arrays.fill(board[i], empty);}
        this.neighbors = new BoardNeighbor("");
    }
    
    /**
     * Adds the GameObject symbol to the board display.
     * @param gadget the GameObject to add. Requires gadget to be
     * within the board dimensions.
     */
    public void add(GameObject gadget) {
        List<Point> locations = gadget.getDisplayLocations();
        for (Point point: locations){
            board[point.y][point.x] = gadget.getSymbol();
        }
    }
    
    /**
     * Set the board names of the boards connected to this
     * @neighbors gives the names of the existent connected boards to this
     */
    public void setBoardNeighbors(BoardNeighbor neighbors){
        this.neighbors = neighbors;
    }
    
    /**
     * @return a display to match current positions of all gadgets and balls
     */
    public String getDisplay() {
        String display = new String();
        int lengthOfLeftName = this.neighbors.left().length();
        int lengthOfRightName = this.neighbors.right().length();
        int lengthOfUpName = this.neighbors.up().length();
        int lengthOfDownName = this.neighbors.down().length();
        
        for (int i = 0; i < height + 2; i++) {
            if (i >= lengthOfUpName){
                display += ".";
            } else {
                display += neighbors.up().charAt(i);
            }
        }
        display += "\n";

        for (int i = 0; i < this.height; i++) {
            if(i >= lengthOfLeftName) {
                display += ".";
            } else {
                display += neighbors.left().charAt(i);
            }

            for (int j = 0; j < this.width; j++) {
                display += board[i][j];
            }

            if(i >= lengthOfRightName) {
                display += ".";
            } else {
                display += neighbors.right().charAt(i);
            }

            display += "\n";
        }

        for (int i = 0; i < height + 2; i++) {
            if (i >= lengthOfDownName){
                display += ".";
            } else {
                display += neighbors.down().charAt(i);
            }
        }
        return display;
    }
    
    @Override
    public String toString(){
        return getDisplay();
    }
}
