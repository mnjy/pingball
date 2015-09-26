package implementation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represent objects in the playing area that interact with the game. GameObjects and subclasses are NOT thread-safe.
 */

public abstract class GameObject {
    //AF: superclass that represents a gadget
    //RI: originX, originY >= 0, name is of the form [A-Za-z_][A-Za-z_0-9]*, or empty
    
    public static final int DEFAULT_STROKE_WIDTH = 3;
    public static final Color DEFAULT_COLOR = Color.BLACK;
    
    /* The name of the gameObject */
    private String name = "";
    
    /* X coordinate of origin of gameObject */
    private int originX;
    /* Y coordinate of origin of gameObject */
    private int originY;
    
    /* Multiplier applied to the magnitude of the ball's velocity after it bounces off the gadget
     * Coefficient of Reflection 1.0: means two colliding objects leave the collision with the same velocity
     *                              in a different direction
     * Coefficient of Reflection < 1.0 damp ball's velocity
     * Coefficient of Reflection > 1.0 increase ball's velocity */
    private double reflectionCoef;
    
    /* List of GameObjects whose actions are hooked up to this gadget's trigger */
    private List<GameObject> triggersThis = new ArrayList<GameObject>();

    private void checkRep(){
        assert(originX >= 0);
        assert(originY >= 0);
        assert(name.isEmpty() | name.matches("[A-Za-z_][A-Za-z_0-9]*"));
    }
    
    /**
     * Adds a triggered objects to this's list of triggered objects
     * @param trigger a game object that is triggered by this
     */
    public void setTrigger(GameObject trigger) {
        this.triggersThis.add(trigger);
        checkRep();
    }
    
    /**
     * @return a list of all the game objects that this triggers
     */
    public List<GameObject> getTriggers() {
        return Collections.unmodifiableList(triggersThis);
    }
    
    /**
     * Sets initial position of game object
     * @param xCoord = x coordinate of origin
     * @param yCoord = y coordinate of origin
     */
    public void setPosition(int xCoord, int yCoord) {
        this.originX = xCoord;
        this.originY = yCoord;
        checkRep();
    }
    
    /**
     * Sets the reflection coefficient of a game object to reflection 
     * @param reflection the reflection coefficient
     */
    public void setCoefficient(double reflection) {
        this.reflectionCoef = reflection;
        checkRep();
    }
    
    /**
     * Set the name of the game object
     * @param name - name of game object
     */
    public void setName(String name) {
        this.name = name;
        checkRep();
    }

    /**
     * @return the X coordinate of this game object
     */
    public int getX() {
        return originX;
    }

    /**
     * 
     * @return the Y coordinate of this game object
     */
    public int getY() {
        return originY;
    }
    
    /**
     * @return the reflection coefficient of this game object
     */
    public double getCoefficient() {
        return reflectionCoef;
    }
    
    /**
     * @return the character that this Gadget is represented by in the text-mode display.
     */
    public abstract String getSymbol();
    
    /**
     * 
     * @return the name of the game object
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * 
     * @param newX the new X coordinate of the origin of this game object
     */
    public void setX(int newX) {
        this.originX = newX;
        checkRep();
    }

    /**
     * 
     * @param newY the Y coordinate of the origin of this game object
     */
    public void setY(int newY) {
        this.originY = newY;
        checkRep();
    }
    
    /**
     * @param ball is the ball that collides with this game object
     * @return the time that the ball takes to collide with this game object
     */
    public abstract double timeUntilCollision(Ball ball);
    
    /**
     * updates the location and velocity of the ball to that at the point
     * of collision of the ball and the game object, and generates the trigger (if any).
     * @param ball is the ball that collides with this game object
     * @param time the time that the ball takes to collide with this game object
     * @return a message to send to the server, if any, or the empty string if there is no message
     */
    public abstract String reactWhenHit(Ball ball, double time);
    
    /**
     * Called when this gadget is triggered. Performs
     * triggered action (if any).
     */
    public void doTriggerAction(){
    }
    
    /**
     * @return list of board locations that the gadget occupies,
     * where (0,0) is the top left corner; x increases right; y increases down
     */
    public List<Point> getDisplayLocations(){
        List<Point> displayLocations = new ArrayList<Point>();
        displayLocations.add(new Point(this.getX(), this.getY()));
        return displayLocations;
    }
    
    /**
     * Checks if two collision times are equal within some threshold
     * @param time1 collision time one
     * @param time2 collision time two
     * @return true iff the collision times are approximately equal
     */
    public static boolean collisionTimesEqual(double time1, double time2) {
        final double THRESHOLD = 0.000000000001;
        return Math.abs(time1-time2) < THRESHOLD;
    }
    
    /**
     * Draws the component onto the graphic
     * @param g the graphic to draw onto
     * @param offset the offset due to the width of the wall
     * @param L the length in pixels of 1L
     */
    public abstract void drawComponent(Graphics2D g, double L, double offset, boolean heads);
}
