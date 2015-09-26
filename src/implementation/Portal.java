package implementation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import physics.Circle;
import physics.Geometry;
import server.PortalInfo;

/**
 * Class that represents a portal object as described in the handout
 * otherBoard is optional; if otherBoard was not specified, it is just going to be the empty string
 *
 */
public class Portal extends GameObject{
    //RI: radius = 0.5; otherPortal is not the empty string
    //AF: represents a portal object as described in the notes
    
    private final double radius = 0.5;
    private final Circle edge;
    private final String otherBoard; //empty if same board
    private final String otherPortal;
    private final PortalInfo info;
    
    /**
     * Constructs a new portal object that sends a ball to a portal on a different board
     * @param name is name of the portal
     * @param x is x coordinate of the portal origin
     * @param y is the y coordinate of the portal origin
     * @param otherBoard is the target board name, if portal is on the other
     * @param otherPortal is the target portal name
     */
    public Portal(String name, int x, int y, String otherBoard, String otherPortal) {
        setName(name);
        setPosition(x, y);
        this.edge = new Circle(x + radius, y + radius, radius);
        this.otherBoard = otherBoard;
        this.otherPortal = otherPortal;
        this.info = new PortalInfo(name, x, y);
    }
    
    /**
     * Constructs a new portal object that sends a ball to a portal on the same board
     * @param name is name of the portal
     * @param x is x coordinate of the portal origin
     * @param y is the y coordinate of the portal origin
     * @param otherPortal is the target portal name
     */
    public Portal(String name, int x, int y, String otherPortal) {
        setName(name);
        setPosition(x, y);
        this.edge = new Circle(x + radius, y + radius, radius);
        this.otherBoard = "";
        this.otherPortal = otherPortal;
        this.info = new PortalInfo(name, x, y);
    }
    
    @Override
    public String getSymbol() {
        return "o";
    }
    
    @Override 
    public double timeUntilCollision(Ball ball) {
        double collisionTime = Geometry.timeUntilCircleCollision(
                this.edge, ball.getCircle(), ball.getVelocity());
        return collisionTime;
    }
    
    @Override
    public String reactWhenHit(Ball ball, double time){
        String returnMessage = "";
        //move ball to the portal
        double centerX = ball.getCenterX() + ball.getVelocity().x() * time;
        double centerY = ball.getCenterY() + ball.getVelocity().y() * time;
        
        //move ball across the portal, i.e. by a unit vector in the same direction
        double newCenterX = centerX + ball.getVelocity().unitSize().x();
        double newCenterY = centerY + ball.getVelocity().unitSize().y();
        
        ball.updateCenterX(newCenterX);
        ball.updateCenterY(newCenterY);
        
        if (otherBoard.isEmpty()){
            String message = "SELF " + "portal name=" + this.otherPortal;
            return message;
        } else {
            returnMessage = generateOutgoingBallMessage(ball);
        }
        return returnMessage;
    }
    
    /**
     * Generates a message to send the ball to otherPortal on otherBoard
     * @param ball the ball to be sent
     * @return an outgoing ball message following the message protocol
     */
    private String generateOutgoingBallMessage(Ball ball) {
        String name = ball.getName();
        String xVelocity = String.valueOf(ball.getVelocityX());
        String yVelocity = String.valueOf(ball.getVelocityY());
        String message = "ball name="+name +" toBoard="+otherBoard +" toPortal="+otherPortal+" xVelocity="+xVelocity+" yVelocity="+yVelocity+" returnX="+ball.getCenterX()+" returnY="+ball.getCenterY();
        return message;
    }
    
    /**
     * @return a PortalInfo object containing relevant information about this portal
     */
    public PortalInfo getPortalInfo() {
        return info;
    }
 
    @Override
    public void drawComponent(Graphics2D g, double L, double offset, boolean heads){
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(DEFAULT_STROKE_WIDTH));
        
        // get dimensions
        double x = edge.getCenter().x() + radius;
        double y = edge.getCenter().y() + radius;
        double size = radius*2;

        // multiply by L to get the right dimensions in pixels
        int Lx = (int) (x*L + offset);
        int Ly = (int) (y*L + offset);
        int Lsize = (int) (size*L);
        
        // draw the circle
        g.fillOval(Lx, Ly, Lsize, Lsize);
    }
}
