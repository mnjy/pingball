package implementation;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import boardparser.IncorrectFormatException;
import client.ClientMessageParser;
import client.ClientMessageParser.ClientMessageType;
import physics.LineSegment;
import physics.Vect;
import server.BoardNeighbor;
import server.PortalInfo;

import javax.swing.JPanel; //needs to be here or GameGUI import will not compile

import keyboard.PingballKeyListener;
import ui.GameGUI;

/**
 * Represents a Pingball board. This class is NOT thread-safe.
 */
public class Board{
    //AF: represents a pingball board of the given dimensions and configuration
    //RI: all GameObjects are on the board. boundaries contains top, left, right, bottom
    private String name;
    public final double timeStep = .05;
    private int height = 20;
    private int width = 20;
    private final double DOUBLE_EQUALITY_THRESHOLD = 0.000001;
    private Vect gravity = new Vect(0, 25.0);
    private double mu = 0.025;
    private double mu2 = 0.025;
    
    //for GUI
    private boolean heads=false;
    private PingballKeyListener keyListener;

    //for client-server play
    private boolean isConnectedToClientServer = false;
    
    //for sending to portal on same board: SELF portal name=NAME
    private String selfRegex = "SELF .*";
    
    //objects on the board
    private final List<Ball> balls;
    private final List<GameObject> nonBallGameObjects; //ALSO includes flippers and portals
    private final List<Flipper> flippers;
    private final List<Portal> portals;
    
    //boundaries: having all in a list is convenient for collisions, and having each one is convenient when you want
    //to make one invisible
    public final List<Boundary> boundaries; //accessed and mutated by Pingball
    private Boundary top;
    private Boundary left;
    private Boundary right;
    private Boundary bottom;
    public BoardNeighbor neighbors; //accessed and mutated by Pingball
    
    /**
     * Represents a side of the outer wall of the board
     */
    public enum BoundarySide {
        TOP, BOTTOM, LEFT, RIGHT;
    }
    
    /**
     * Create a game board object with these parameters:
     * @param name the board's name
     * @param nonBallGameObjects a list of all the objects that should go in this board that are not balls (including flippers and portals)
     * @param balls a list of all the Ball objects that should go into this list. 
     * @param flippers a list of flippers
     * @param portals a list of portals
     */
    public Board(String name, List<GameObject> nonBallGameObjects, List<Ball> balls, List<Flipper> flippers, List<Portal> portals) {
        this.name = name;      
        this.nonBallGameObjects = nonBallGameObjects; 
        this.portals = portals;
        this.flippers = flippers;
        this.boundaries = setBoundaries();
        
        this.balls = balls; 
        for (Ball ball : balls){
            ball.setPhysics(gravity, mu, mu2);
        }
        this.neighbors = new BoardNeighbor(name);

        checkRep();
    }
    
    /**
     * Create the boundaries for this board
     */
    private List<Boundary> setBoundaries(){
        this.top = new Boundary(BoundarySide.TOP, new LineSegment(0, 0, width, 0));
        this.bottom = new Boundary(BoundarySide.BOTTOM, new LineSegment(0, height, width, height));
        this.left = new Boundary(BoundarySide.LEFT, new LineSegment(0, 0, 0, height));
        this.right = new Boundary(BoundarySide.RIGHT, new LineSegment(width, 0, width, height));
        
        List<Boundary> boundaries = Arrays.asList(top, bottom, left, right);
        
        return boundaries;
    }
    
    private void checkRep(){
        //Gadgets on board
        for (GameObject gadget: nonBallGameObjects){
            for (Point point: gadget.getDisplayLocations()){
                assert(point.x >= 0 && point.x <= width);
                assert(point.y >= 0 && point.y <= height);
            }
        }
        
        //Balls on board
        for (Ball ball: balls){
            for (Point point: ball.getDisplayLocations()){
                assert(point.x >= 0 && point.x <= width);
                assert(point.y >= 0 && point.y <= height);
            }
        }
        
        //Boundaries correct
        assert(boundaries.size() == 4);
        assert(boundaries.contains(top));
        assert(boundaries.contains(left));
        assert(boundaries.contains(right));
        assert(boundaries.contains(bottom));
    }
     
    /**
     * @return the name of the board
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * @param gadgetName the name of the gadget to return
     * @return the GameObject named by gadgetName, or null if no such GameObject exists
     */
    public GameObject getGadgetFromName(String gadgetName) {
        List<GameObject> allGameObjects = new ArrayList<GameObject>(nonBallGameObjects);
        allGameObjects.addAll(new ArrayList<GameObject>(balls)); 
        
        for (GameObject gadget : allGameObjects){
            if (gadget.getName().equals(gadgetName)){
                return gadget;
            }
        }
        return null;
    }
    
    /**
     * Changes the gravitational acceleration vector
     * @param newGravity - new gravity y coordinate
     */
    public void setGravity(double newGravity) {
        gravity = new Vect(0, newGravity);
        for (Ball ball : balls){
            ball.setPhysics(gravity, mu, mu2);
        }
        checkRep();
    }
    
    /**
     * Changes the friction coefficient of the board
     * @param newMu - new friction coefficient
     */
    public void setMu(double newMu) {
        mu = newMu;
        for (Ball ball : balls){
            ball.setPhysics(gravity, mu, mu2);
        }
        checkRep();
    }
    
    /**
     * Changes the friction coefficient of ball
     * @param newMu2 - new mu2 coefficient as described in the handout
     */
    public void setMu2(double newMu2) {
       mu2 = newMu2;
       for (Ball ball : balls){
           ball.setPhysics(gravity, mu, mu2);
       }
       checkRep();
    }
    
    /**
     * @return a list of information about portals contained on this board
     */
    public List<PortalInfo> getPortalInfo() {
        List<PortalInfo> info = new ArrayList<PortalInfo>();
        for (Portal portal : portals) {
            info.add(portal.getPortalInfo());
        }
        return info;
    }
    
    /**
     * Adds a ball to the board. Called when a ball is received from
     * a connected board or portal on another board.
     * @param ball the ball that is being added to the board
     */
    public void addBall(Ball ball) {
        ball.setPhysics(gravity, mu, mu2);
        this.balls.add(ball);
    }
    
    /**
     * Removes all balls from the board.
     */
    public void clearBalls() {
        balls.clear();
    }
    
    /**
     * Removes a ball from the board. Called a ball is sent to a
     * connected board or through a portal to another board.
     * @param ball the ball that is being removed from the board
     */
    private void removeBall(Ball ball) {
        this.balls.remove(ball);
    }
    
    /**
     * Makes the board ready for client-server play
     */
    public void connectToClientServer(){
        this.isConnectedToClientServer = true;
    }

    /**
     * Disconnects the board from client-server play
     */
    public void disconnectFromClientServer(){
        this.isConnectedToClientServer = false;
    }
    
    /**
     * Updates the state of all objects on the board through one timeStep.
     * @return a list of ball messages to send to the server. Empty if none.
     */
    public List<String> updateState() {
        List<GameObject> nonBallGameObjectsAndBoundaries = this.nonBallGameObjects;
        nonBallGameObjectsAndBoundaries.addAll(this.boundaries); 
        List<Ball> removedBalls = new ArrayList<Ball>();
        List<String> messagesToSend = new ArrayList<String>();
        
        for (Ball ball : balls) {
            if(ball.inAbsorber()) continue;

            List<GameObject> gameObjects = new ArrayList<GameObject>(nonBallGameObjectsAndBoundaries);
            List<GameObject> currBalls = new ArrayList<GameObject>(balls);
            currBalls.remove(ball);
            gameObjects.addAll(currBalls);

            double deltaT = timeStep;            
            
            while (deltaT > DOUBLE_EQUALITY_THRESHOLD) { //close enough to 0
                GameObject bestGadget = gameObjects.get(0);
                double bestTimeCollision = Float.POSITIVE_INFINITY;
                for (GameObject gadget:gameObjects) {
                    double collisionTime = gadget.timeUntilCollision(ball);
  
                    if(collisionTime < bestTimeCollision) { // && collisionTime != 0) {
                        bestGadget = gadget;
                        bestTimeCollision = collisionTime;
                    }
                }
                
                // Ball is so far from object, that doesn't need to calculate
                // position after reflection
                if (bestTimeCollision > deltaT) {
                    ball.updateCenterX(ball.getCenterX() + ball.getVelocity().x()
                            * deltaT);
                    ball.updateCenterY(ball.getCenterY() + ball.getVelocity().y()
                            * deltaT);
                    ball.updateVelocityWithGravityAndFriction(
                            ball.getVelocity(), deltaT);
                    updateFlippers(deltaT);
                    deltaT = 0;

                    // Ball is near object, so need to calculate position after
                    // reflection
                } else {          
                    String message = bestGadget.reactWhenHit(ball, bestTimeCollision);
                    //if internal message
                    if (message.matches(selfRegex)){
                        //message ::= SELF portal name=NAME
                        String portalToSendTo = message.split(" ")[2].split("=")[1];
                        for (PortalInfo portal : this.getPortalInfo()){
                            if (portal.name().equals(portalToSendTo)){
                                sendToSelfPortal(ball, portal);
                            }
                        }
                    }
                    //if external message
                    else if (this.isConnectedToClientServer && !message.isEmpty()){
                        messagesToSend.add(message);
                        removedBalls.add(ball);
                        break; //this ball is gone now
                    }
                    
                    deltaT -= bestTimeCollision;
                }
            }
        }
        updateFlippers(timeStep);
        
        for (Ball ball : removedBalls) {
            removeBall(ball);
        }
        checkRep();
        return messagesToSend;
    }

    /**
     * Updates location of ball to the (edge of the) specified portal on the same board
     * @param ball the ball to move
     * @param portal the portal to move to
     */
    private void sendToSelfPortal(Ball ball, PortalInfo portal){
        double portalCenterX = portal.x() + 0.5;
        double portalCenterY = portal.y() + 0.5;
        double newBallCenterX = portalCenterX + ball.getVelocity().unitSize().x()/2;
        double newBallCenterY = portalCenterY + ball.getVelocity().unitSize().y()/2;
        ball.updateCenterX(newBallCenterX);
        ball.updateCenterY(newBallCenterY);
    }
    
    /**
     * For any flippers that are in motion, rotates the 
     * flipper over the specified time interval
     * @param time the time over which to move the flipper
     */
    private void updateFlippers(double time) {
        for(Flipper flipper: flippers){
            flipper.updateAngle(time);
        }
        checkRep();
    }
    
    
    /**
     * Makes the specified side of the wall invisible
     * @param side the side to make invisible.
     */
    public void makeInvisible(BoundarySide side) {
        if (side.equals(BoundarySide.TOP)){top.makeInvisible(neighbors.up());}
        if (side.equals(BoundarySide.BOTTOM)){bottom.makeInvisible(neighbors.down());}
        if (side.equals(BoundarySide.LEFT)){left.makeInvisible(neighbors.left());}
        if (side.equals(BoundarySide.RIGHT)) {right.makeInvisible(neighbors.right());}
    }
    
    /**
     * Makes the specified side of the wall visible
     * @param side the side to make visible.
     */
    public void makeVisible(BoundarySide side) {
        if (side.equals(BoundarySide.TOP)){top.makeVisible();}
        if (side.equals(BoundarySide.BOTTOM)){bottom.makeVisible();}
        if (side.equals(BoundarySide.LEFT)){left.makeVisible();}
        if (side.equals(BoundarySide.RIGHT)) {right.makeVisible();}
    }
   
    /**
     * GUI representation of the board
     * Draws the board onto the graphics
     * @param g the graphics to draw onto
     * @param length the size in pixels of one side
     * @param wallWidth the width of each wall, in pixels
     */
    public void makeGUIDisplay(Graphics2D g, double length, double wallWidth){
        double L = (length-2*wallWidth)/width; //which is the same as height
        
        for (GameObject gadget : nonBallGameObjects){
            gadget.drawComponent(g, L, wallWidth, heads);
        }
        
        //balls happen afterwards because we prefer displaying balls
        //if they are in the same location as a gadget (e.g. stored
        //in absorber)
        for (Ball ball : balls){
            ball.drawComponent(g, L, wallWidth, heads);
        }
        
        for (Boundary boundary : boundaries){
            boundary.drawComponent(g, L, wallWidth, heads);
        }
                
        checkRep();
    }
    
    /**
     * Changes whether head should appear
     * @param head whether or not the heads should appear instead of balls
     */
    public void setHeads(boolean head){
        heads=head;
    }
    
    /**
     * @return whether or not heads should appear instead of heads
     */
    public boolean getHeads(){
        return heads;
    }
    
    /**
     * @return text-mode representation of the board
     */
    public String makeTextModeDisplay(){
        TextModeDisplay display = new TextModeDisplay(width, height);
        for (GameObject gadget : nonBallGameObjects){
            display.add(gadget);
        }
        
        //balls happen afterwards because we prefer displaying balls
        //if they are in the same location as a gadget
        for (Ball ball : balls){
            display.add(ball);
        }
        
        display.setBoardNeighbors(this.neighbors);
        
        checkRep();
        
        return display.getDisplay();
    }
    
    /**
     * @return string representation of the board
     */
    @Override
    public String toString() {
        return makeTextModeDisplay();
    }
    
    /**
     * Adds a key listener that allows gadgets to be triggered by the keyboard
     * @param keyListener the listener to add
     */
    public void addKeyListener(PingballKeyListener keyListener){
        this.keyListener = keyListener;
    }
    
    /**
     * @return the listener that specifies keyboard control on this board
     */
    public PingballKeyListener getKeyListener(){
        return this.keyListener;
    }
    

}
