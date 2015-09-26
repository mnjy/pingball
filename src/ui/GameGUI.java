package ui;

import implementation.*;

import javax.swing.JPanel;
import javax.swing.Timer;

import server.BoardNeighbor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import keyboard.PingballKeyListener;

/**
 * GameGUI is a user interface representing a game of pingball. Objects in the game,
 * including bumpers, balls, walls, absorbers, and flippers, are represented using
 * shapes corresponding with the objects size and shape. Ball representation can 
 * also be switched to be represented by prof. miller's and goldman's faces.
 *
 */
public class GameGUI extends JPanel implements ActionListener {  

    private static final long serialVersionUID = 1L; //default, required by Java
    Color backgroundColor = Color.white;
    public double sideLength;
    private BoardNeighbor neighbors;
    private Board board;
    private static final double DEFAULT_WALL_WIDTH = 5; //in pixels
    private double wallWidth = DEFAULT_WALL_WIDTH; //in pixels
    private boolean disco = false;
    private List<String> messagesToSendToServer = new ArrayList<String>();
    
    /**
     * Make a square GameGUI
     * @param sideLength the length of a side in pixels
     */
    public GameGUI(int sideLength) {
        this.setPreferredSize(new Dimension(sideLength, sideLength));
        this.sideLength = sideLength;
        
        setFocusable(true);
        requestFocusInWindow();
    }
    
    /**
     * Sets the board to be played
     * @param board the board to be played
     */
    public void setBoard(Board board){
        this.board = board;
        setKeyListener();
    }
    
    /**
     * Set the board names of the boards connected to this
     * @param neighbors gives the names of the existent connected boards to this
     */
    public void setBoardNeighbors(BoardNeighbor neighbors){
        this.neighbors = neighbors;
    }
    
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     * In case of overlapping gadgets, gadgets added to the GUI later will be
     * printed preferentially.
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        fillWindow(g2);
        board.makeGUIDisplay(g2, sideLength, wallWidth);
    }
    
    /**
     * Make the drawing buffer entirely filled with one color.
     */
    private void fillWindow(final Graphics2D g) {
        g.setColor(backgroundColor);
        g.fillRect(0,  0,  getWidth(), getHeight());
    }
    
    /**
     * Changes the background color of the game board
     * @param color the new color
     */
    public void changeColor(Color color){
        backgroundColor = color;
        this.repaint();
    }
    
    /**
     * @return the delay corresponding to the frame rate of the board
     */
    public int getDelay(){
        return (int) (board.timeStep * 1000);
    }
    
    /**
     * @return the keyboard listener associated with the board
     * being played, or null if there is none.
     */
    public PingballKeyListener getKeyListener(){
        return board.getKeyListener();
    }   
    
    /**
     * @return a list of messages to send to server for this timestep
     * When this method is called, the GameGUI loses these messages
     */
    public List<String> getMessages(){
        List<String> messages = new ArrayList<String>();
        messages.addAll(messagesToSendToServer);
        messagesToSendToServer.clear();
        return messages;
    }
    
    // extra features
    
    /**
     * Changes whether head should appear
     * @param heads whether or not the heads should appear instead of balls
     */
    public void setHeads(boolean heads){
        board.setHeads(heads);
    }
    
    /**
     * @return whether or not heads should appear instead of heads
     */
    public boolean getHeads(){
        return board.getHeads();
    }    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (board == null){return;}
        if (disco){disco();}
        List<String> possibleMessages = board.updateState();
        messagesToSendToServer.addAll(possibleMessages);
        this.repaint();
    } 
    
    /**
     * Changes whether disco mode is on
     * @param disco whether or not disco mode is on
     */
    public void setDisco(boolean disco){
        this.disco = disco;
        if (!disco){
            changeColor(Color.WHITE);
        }
    }
    
    /**
     * @return whether or not disco mode is on
     */
    public boolean getDisco(){
        return disco;
    }
    
    /**
     * Changes background color to a random color
     */
    private void disco(){
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        this.changeColor(randomColor);
    }
    
    /**
     * @return a list of the keyboard commands that the client can use,
     * or "None" if there are none.
     */
    public String getKeyboardCommands(){
        return board.getKeyListener().getCommands();
    }

    /**
     * adds a key listener in the GameGUI to respond to the keyboard commands 
     * specified in the board file
     */

    public void setKeyListener(){
        this.addKeyListener((KeyListener) board.getKeyListener());
    }
    
}

