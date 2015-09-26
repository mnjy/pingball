package implementation;

import implementation.Board.BoundarySide;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import physics.*;

/**
 * Represents a boundary of the Pingball board
 */
public class Boundary extends GameObject {
    //AF: represents a boundary wall (one side)
    //RI: edge is a line segment of length 20
    //edge is horizontal or vertical (i.e. either start.x == end.x OR start.y == end.y)
    //edgeEndPoints contains the correct 2 circles, i.e. the end-points of boundary.
    //otherBoard is of the form [A-Za-z_][A-Za-z_0-9]*, or the empty string.

    private final BoundarySide side;
    private final LineSegment edge;
    private final List<Circle> edgeEndPoints;
    boolean isInvisible = false;
    private String otherBoard = ""; //name of board that the boundary is connected to

    /**
     * Create an opaque boundary object with these parameters:
     * 
     * @param boundary the line segment that describes a boundary
     */
    public Boundary(BoundarySide side, LineSegment boundary) {
        setCoefficient(1.0);
        
        this.side = side;
        this.edge = boundary;
        Circle boundaryCircleOne = new Circle(boundary.p1(), 0);
        Circle boundaryCircleTwo = new Circle(boundary.p2(), 0);
        this.edgeEndPoints = Arrays.asList(boundaryCircleOne, boundaryCircleTwo);
        
        checkRep();
    }
    
    /**
     * Makes the boundary invisible
     * @param otherBoardName the name of the board to which this boundary is connected
     */
    public void makeInvisible(String otherBoardName){
        this.isInvisible = true;
        this.otherBoard = otherBoardName;
        checkRep();
    }

    /**
     * Makes the boundary visible
     */
    public void makeVisible(){
        this.isInvisible = false;
        this.otherBoard = "";
        checkRep();
    }

    private void checkRep(){
        assert(edge.length() == 20);
        if (side.equals(BoundarySide.TOP) || side.equals(BoundarySide.BOTTOM)){
            assert(edge.p1().y() == edge.p2().y()); //horizontal
        }
        else {
            assert(edge.p1().x() == edge.p2().x()); //vertical
        }
        assert(edge.p1().equals(edgeEndPoints.get(0).getCenter()));
        assert(edge.p2().equals(edgeEndPoints.get(1).getCenter()));
        String nameRegex = "[A-Za-z_][A-Za-z_0-9]*";
        assert(otherBoard.isEmpty() | otherBoard.matches(nameRegex));
    }

    @Override
    public double timeUntilCollision(Ball ball) {
        double minCollisionTime = Geometry.timeUntilWallCollision(edge, ball.getCircle(), ball.getVelocity());

        for (Circle circle : this.edgeEndPoints) {
            double currCollisionTime = Geometry.timeUntilCircleCollision(circle, ball.getCircle(), ball.getVelocity());
            minCollisionTime = Math.min(minCollisionTime, currCollisionTime);
        }
        checkRep();
        return minCollisionTime;
    }

    @Override
    public String reactWhenHit(Ball ball, double time) {
        String resultingMessage = "";
        Circle oldCircle = ball.getCircle();
        boolean hitMinEdge = false;
        double CenterX = ball.getCenterX() + ball.getVelocityX() * time;
        double CenterY = ball.getCenterY() + ball.getVelocityY() * time;
        
        ball.updateCenterX(CenterX);
        ball.updateCenterY(CenterY);
        
        if (this.isInvisible) {
            resultingMessage = generateOutgoingBallMessage(ball);
        }
        
        double currCollisionTime = Geometry.timeUntilWallCollision(edge, oldCircle, ball.getVelocity());
        if (collisionTimesEqual(time, currCollisionTime)) {
            ball.updateVelocityWithGravityAndFriction(Geometry.reflectWall(edge, ball.getVelocity(), this.getCoefficient()), time);
            hitMinEdge = true;
        }

        if (!hitMinEdge) {
            for (Circle circle : this.edgeEndPoints) {
                currCollisionTime = Geometry.timeUntilCircleCollision(circle, oldCircle, ball.getVelocity());
                if (collisionTimesEqual(time, currCollisionTime)) {
                    ball.updateVelocityWithGravityAndFriction(Geometry.reflectCircle(circle.getCenter(), ball.getCircle().getCenter(), ball.getVelocity(), this.getCoefficient()), time);
                }
            }
        }

        //triggers
        for (GameObject obj : this.getTriggers()) {
            obj.doTriggerAction();
        }
        checkRep();
        return resultingMessage;
    }

    /**
     * Generates a message to send the ball to the board this boundary is connected to
     * @param ball the ball to be sent
     * @return an outgoing ball message following the message protocol
     */
    private String generateOutgoingBallMessage(Ball ball) {
        String name = ball.getName();
        String x = String.valueOf(ball.getCenterX());
        String y = String.valueOf(ball.getCenterY());
        String xVelocity = String.valueOf(ball.getVelocityX());
        String yVelocity = String.valueOf(ball.getVelocityY());
        String message = "ball name="+name +" x="+x+" y="+y+" xVelocity="+xVelocity+" yVelocity="+yVelocity;
        return message;
    }
    
    /**
     * Empty method because boundary objects should not exist in the display
     * It does nothing for boundary, because boundary is not part of the board per se
     * @return empty list
     */
    @Override
    public List<Point> getDisplayLocations() {
        return new ArrayList<Point>();
    }

    @Override
    public String getSymbol() {
        return ".";
    }
    
    /**
     * Creates the correct length of text to be used for the drawComponent when wall is invisible
     * @param x1 the starting x-coordinate
     * @param y1 the starting y-coordinate
     * @param x2 the ending x-coordinate
     * @param y2 the ending y-coordinate
     * @return string for invisible wall representation for drawComponent
     */
    private String createTextforDrawing(int x1, int y1, int x2, int y2){
        int lengthOfText;
        if (x1==x2){
            lengthOfText=y2-y1;
        }else{
            lengthOfText=x2-x1;
        }
        int lengthOfBoardName=otherBoard.length();
        if (lengthOfBoardName==0){
            lengthOfBoardName=1;
        }
        int multiplicationFactor=lengthOfText/lengthOfBoardName;
        String textForDrawing="";
        for (int i=0; i<=multiplicationFactor; i++){
            textForDrawing+=otherBoard+" ";
        }
        return textForDrawing;
    }

    @Override
    public void drawComponent(Graphics2D g, double L, double offset, boolean heads){
        g.setColor(DEFAULT_COLOR);
        g.setStroke(new BasicStroke((int) (2*offset)));
        // multiply by L to get the right dimensions in pixels
        double wallOffset = .0; 
        if (!isInvisible){
            //if right or bottom wall, needs to be offset
            if (side.equals(BoundarySide.RIGHT) || side.equals(BoundarySide.BOTTOM)){
                wallOffset = .5;
            }

        }
        else{
            wallOffset=.5;
        }
        int x1 = (int) ((edge.p1().x()+wallOffset)*(L));
        int y1 = (int) ((edge.p1().y()+wallOffset)*(L));
        int x2 = (int) ((edge.p2().x()+wallOffset)*(L));
        int y2 = (int) ((edge.p2().y()+wallOffset)*(L));

        if (side.equals(BoundarySide.TOP)){
            x2+=offset;
        }

        if (side.equals(BoundarySide.BOTTOM)){
            x1-=offset;
        }

        if (!isInvisible){
            g.drawLine(x1, y1, x2, y2);
        }else{
            String textName=createTextforDrawing(x1, y1, x2, y2);
            if (side.equals(BoundarySide.BOTTOM)|| side.equals(BoundarySide.TOP)){
                g.drawString(textName, x1, y1);
            }

            if (side.equals(BoundarySide.LEFT) || side.equals(BoundarySide.RIGHT)){
                final int spacing=15;
                for (int i=0; i<textName.length(); i++){
                    g.drawString(textName.substring(i,i+1),x1-7, y1+(i*spacing));
                }
            }
        }
    }
}
