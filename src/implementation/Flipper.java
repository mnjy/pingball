package implementation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import physics.*;

public class Flipper extends GameObject{
    //AF: represents a flipper
    //RI: border contains the correct line segment to represent the flipper, i.e. it is 
    //of length 2L, at the specified orientation and at the specified location.

    private LineSegment edge;
    private List<Circle> edgeEndPoints;
    private final FlipperType type;
    private final Angle orientation;

    //rotation of flipper
    private final Vect centerOfRotation;
    private final double ANGULAR_SPEED = 1080.0;
    protected double currentRotatedAngle = 0.0; //ranges from 0 to 90. 0 in initial state (absolute position depends on orientation)
    private int currentDirectionOfRotation = 0; //RIGHT: 1=CW, -1=ACW, 0=not rotating, LEFT: 1=ACW, -1=CW
    
    public enum FlipperType {
        LEFT, RIGHT
    }
    
    /**
     * Initializes a flipper, to be placed at (x,y) on a board
     * @param name of flipper
     * @param x top left corner of bounding box 
     * @param y top left corner of bounding box
     * @param type options are LEFT and RIGHT
     * @param orientation the initial orientation of the flipper rotated
     *        around the center of rotation (NOT the origin of bounding box).
     *        Requires orientation is ZERO, DEG_90, DEG_180 or DEG_270
     */
    public Flipper(String name, int x, int y, FlipperType type, Angle orientation){ 
        setPosition(x, y);
        final double DEFAULT_COEF = 0.95;
        setCoefficient(DEFAULT_COEF);
        setName(name);
        this.orientation = orientation;
        
        this.type = type;
        if(type == FlipperType.RIGHT){
            if (orientation.equals(Angle.DEG_90)){
                centerOfRotation = new Vect(x+2, y+2);
                edge = new LineSegment(x, y+2, x+2, y+2);
                }
            else if (orientation.equals(Angle.DEG_180)){
                centerOfRotation = new Vect(x, y+2);
                edge = new LineSegment(x, y, x, y+2);
            }
            else if (orientation.equals(Angle.DEG_270)){
                centerOfRotation = new Vect(x, y);
                edge = new LineSegment(x, y, x+2, y);
            }
            else {
                centerOfRotation = new Vect(x+2, y);
                edge = new LineSegment(x+2, y, x+2, y+2);
            }
        }
        else{ //FlipperType.LEFT
            if (orientation.equals(Angle.DEG_90)){
                centerOfRotation = new Vect(x+2, y);
                edge = new LineSegment(x, y, x+2, y);
                }
            else if (orientation.equals(Angle.DEG_180)){
                centerOfRotation = new Vect(x+2, y+2);
                edge = new LineSegment(x+2, y, x+2, y+2);
            }
            else if (orientation.equals(Angle.DEG_270)){
                centerOfRotation = new Vect(x, y+2);
                edge = new LineSegment(x, y+2, x+2, y+2);
            }
            else {
                centerOfRotation = new Vect(x, y);
                edge = new LineSegment(x, y, x, y+2);
            }
        }
        
        Circle endCircleOne = new Circle(edge.p1(), 0);
        Circle endCircleTwo = new Circle(edge.p2(), 0);
        this.edgeEndPoints = Arrays.asList(endCircleOne, endCircleTwo);
        checkRep();
    }
    
    private void checkRep(){
        final double THRESHOLD = 0.00001;
        assert(Math.abs(edge.length() - 2) < THRESHOLD);
        int x = getX();
        int y = getY();
        if (type == FlipperType.RIGHT){
            if (orientation.equals(Angle.DEG_90)){assert(centerOfRotation.equals(new Vect(x+2, y+2)));}
            else if (orientation.equals(Angle.DEG_180)){assert(centerOfRotation.equals(new Vect(x, y+2)));}
            else if (orientation.equals(Angle.DEG_270)){assert(centerOfRotation.equals(new Vect(x, y)));}
            else {assert(centerOfRotation.equals(new Vect(x+2, y)));}        
        } else {
            if (orientation.equals(Angle.DEG_90)){assert(centerOfRotation.equals(new Vect(x+2, y)));}
            else if (orientation.equals(Angle.DEG_180)){assert(centerOfRotation.equals(new Vect(x+2, y+2)));}
            else if (orientation.equals(Angle.DEG_270)){assert(centerOfRotation.equals(new Vect(x, y+2)));}
            else {assert(centerOfRotation.equals(new Vect(x, y)));}    
        }
    }
    
    @Override
    public double timeUntilCollision(Ball ball){
        Double minCollisionTime = Geometry.timeUntilRotatingWallCollision(
                edge,centerOfRotation, Math.toRadians(ANGULAR_SPEED*currentDirectionOfRotation), ball.getCircle(), ball.getVelocity());
        
        
        for (Circle circle : this.edgeEndPoints) {
            double currCollisionTime = Geometry.timeUntilRotatingCircleCollision(circle, centerOfRotation, 
                    Math.toRadians(ANGULAR_SPEED*currentDirectionOfRotation), ball.getCircle(), ball.getVelocity());
            minCollisionTime = Math.min(minCollisionTime, currCollisionTime);
        }
        checkRep();
        return minCollisionTime;
    }
    
    @Override
    public String reactWhenHit(Ball ball, double time){
        
        String returnMessage = "";
        
        Circle oldCircle = ball.getCircle();
        boolean hitMinEdge = false;
        double CenterX = ball.getCenterX() + ball.getVelocityX() * time;
        double CenterY = ball.getCenterY() + ball.getVelocityY() * time;
        
        ball.updateCenterX(CenterX);
        ball.updateCenterY(CenterY);
        
        double currCollisionTime = Geometry.timeUntilRotatingWallCollision(edge, centerOfRotation, 
                Math.toRadians(ANGULAR_SPEED*currentDirectionOfRotation), oldCircle, ball.getVelocity());
        if (collisionTimesEqual(time, currCollisionTime)) {
            ball.updateVelocityWithGravityAndFriction(Geometry.reflectRotatingWall(edge, centerOfRotation,
                    Math.toRadians(ANGULAR_SPEED*currentDirectionOfRotation), oldCircle, ball.getVelocity(), this.getCoefficient()), time);
            hitMinEdge = true;
        }
        
        if (!hitMinEdge) {
            for (Circle circle : this.edgeEndPoints) {
                currCollisionTime = Geometry.timeUntilRotatingCircleCollision(circle, centerOfRotation,
                        Math.toRadians(ANGULAR_SPEED*currentDirectionOfRotation), oldCircle, ball.getVelocity());
                if (collisionTimesEqual(time, currCollisionTime)) {
                    ball.updateVelocityWithGravityAndFriction(Geometry.reflectRotatingCircle(circle, centerOfRotation,
                        Math.toRadians(ANGULAR_SPEED*currentDirectionOfRotation), oldCircle, ball.getVelocity()), time);
                }
            }
        }
        
        //triggers
        for (GameObject obj : this.getTriggers()) {
            obj.doTriggerAction();
        }
        checkRep();
        
        return returnMessage;
    }
    
    /**
     * This method is called every frame to update the angle
     * of the flipper, if it is in the middle of a triggered
     * motion
     * @param time the time over which to move the flipper
     */
    public void updateAngle(double time){
        if(currentDirectionOfRotation==0 ){ //not rotating
            checkRep();
            return;
        }
                
        //calculate angle to rotate by
        double timeTillHit = time;
        
        double newAngle=0.0;
        if (currentDirectionOfRotation==1){ 
            timeTillHit =  (90.0-currentRotatedAngle) / ANGULAR_SPEED; //done moving to horizontal position
            if (timeTillHit<time){
                newAngle = 90.0;
                currentDirectionOfRotation=0;
            }
        } else if (currentDirectionOfRotation==-1){
            timeTillHit =  currentRotatedAngle / ANGULAR_SPEED; //done moving to vertical position
            if (timeTillHit<time){
                newAngle = 0.0;
                currentDirectionOfRotation=0;
            }
        }
        
        if (timeTillHit>time){
            newAngle = currentRotatedAngle + ANGULAR_SPEED*currentDirectionOfRotation*time;
        }
        
        //rotate line segment (including end points)
        double deltaAngle = newAngle - currentRotatedAngle;
        if(type == FlipperType.RIGHT){
            edge = Geometry.rotateAround(edge,centerOfRotation, new Angle(Math.toRadians(deltaAngle)));
        }
        if(type == FlipperType.LEFT){
            edge = Geometry.rotateAround(edge,centerOfRotation, new Angle(-Math.toRadians(deltaAngle)));
        }
        Circle endCircleOne = new Circle(edge.p1(), 0.01);
        Circle endCircleTwo = new Circle(edge.p2(), 0.01);
        this.edgeEndPoints = Arrays.asList(endCircleOne, endCircleTwo);
        
        currentRotatedAngle = newAngle;
       
        checkRep();
        return;
        
    }
    
    @Override
    public void doTriggerAction() {
        if (currentRotatedAngle==90.0){
            currentDirectionOfRotation = -1;
        } else {
            currentDirectionOfRotation = 1;
        }
        checkRep();
    }
 
    @Override
    public List<Point> getDisplayLocations() {
        int x = getX();
        int y = getY();
        Vect end1 = edge.p1();
        Vect end2 = edge.p2();
        //horizontal
        if(Math.abs(end2.x()-end1.x()) > Math.abs(end2.y()-end1.y())){
            checkRep();
            return Arrays.asList(new Point(x,y), new Point(x+1, y));
        }
        //vertical
        else{
            checkRep();
            return Arrays.asList(new Point(x,y), new Point(x, y+1));
            
        }
    }

    @Override
    public String getSymbol() {
        Vect end1 = edge.p1();
        Vect end2 = edge.p2();
        if(Math.abs(end2.x()-end1.x()) > Math.abs(end2.y()-end1.y())){ //more towards horizontal
            return "-";
        } else {
            return "|";
        }
    }
    
    @Override
    public void drawComponent(Graphics2D g, double L, double offset, boolean heads){
        g.setColor(DEFAULT_COLOR);
        g.setStroke(new BasicStroke(DEFAULT_STROKE_WIDTH));
        
        // multiply by L to get the right dimensions in pixels
        int x1 = (int) (edge.p1().x()*L + offset);
        int y1 = (int) (edge.p1().y()*L + offset);
        int x2 = (int) (edge.p2().x()*L + offset);
        int y2 = (int) (edge.p2().y()*L + offset);
        
        //draw the edge
        g.drawLine(x1, y1, x2, y2);
    }
}