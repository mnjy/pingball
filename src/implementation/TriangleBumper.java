package implementation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import physics.*;

public class TriangleBumper extends GameObject {
    //AF: represents a triangle bumper
    //RI: edges contains the correct 3 line segments to represent the triangle, i.e. the line segments are connected and form a
    //triangle with sides L, of the specified orientation and at the specified location.
    //edgeEndPoints contains the correct 3 circles, i.e. the end-points of the line segments in edges, with radius 0.01

    private final List<LineSegment> edges;
    private final List<Circle> edgeEndPoints;
    private Angle orientation;

    /**
     * @param name the name of the triangle bumper
     * @param x the x coordinate of the bumper's origin
     * @param y the y coordinate of the bumper's origin
     * @param orientation the orientation of the triangle bumper as described in the Pingball handout, and
     * must be equivalent to 0, 90, 180, or 270 deg.
     */
    public TriangleBumper(String name, int x, int y, Angle orientation) {
        setPosition(x, y);
        setName(name);
        setCoefficient(1.0);
        this.orientation = orientation;
        
        LineSegment upperEdge = new LineSegment(x, y, x+1, y);
        LineSegment lowerEdge = new LineSegment(x, y+1, x+1, y+1);
        LineSegment rightEdge = new LineSegment(x+1, y, x+1, y+1);
        LineSegment leftEdge = new LineSegment(x, y, x, y+1);
        LineSegment forwardDiagonal = new LineSegment(x, y+1, x+1, y);
        LineSegment backwardDiagonal = new LineSegment(x, y, x+1, y+1);

        Circle upLeftCircle = new Circle(x, y, 0);
        Circle upRightCircle = new Circle(x+1, y, 0);
        Circle downLeftCircle = new Circle(x, y+1, 0);
        Circle downRightCircle = new Circle(x+1, y+1, 0);
        
        if (orientation.equals(Angle.ZERO)){
            edges = new ArrayList<LineSegment>(Arrays.asList(upperEdge, forwardDiagonal, leftEdge));
            edgeEndPoints = new ArrayList<Circle>(Arrays.asList(upLeftCircle, upRightCircle, downLeftCircle));
        } else if (orientation.equals(Angle.DEG_90)){
            edges = new ArrayList<LineSegment>(Arrays.asList(upperEdge, backwardDiagonal, rightEdge));
            edgeEndPoints = new ArrayList<Circle>(Arrays.asList(upLeftCircle, upRightCircle, downRightCircle));
        } else if (orientation.equals(Angle.DEG_180)){
            edges = new ArrayList<LineSegment>(Arrays.asList(lowerEdge, forwardDiagonal, rightEdge));
            edgeEndPoints = new ArrayList<Circle>(Arrays.asList(upRightCircle, downRightCircle, downLeftCircle));
        } else if (orientation.equals(Angle.DEG_270)) {
            edges = new ArrayList<LineSegment>(Arrays.asList(lowerEdge, backwardDiagonal, leftEdge));
            edgeEndPoints = new ArrayList<Circle>(Arrays.asList(downRightCircle, upLeftCircle, downLeftCircle));
        } else {
            throw new IllegalArgumentException();
        }
        
        
        checkRep();
    }

    private void checkRep(){
        assert(edges.size() == 3);
        assert(edgeEndPoints.size() == 3);
        double x = getX();
        double y = getY();
        
        LineSegment upperEdge = new LineSegment(x, y, x+1, y);
        LineSegment lowerEdge = new LineSegment(x, y+1, x+1, y+1);
        LineSegment rightEdge = new LineSegment(x+1, y, x+1, y+1);
        LineSegment leftEdge = new LineSegment(x, y, x, y+1);
        LineSegment forwardDiagonal = new LineSegment(x, y+1, x+1, y);
        LineSegment backwardDiagonal = new LineSegment(x, y, x+1, y+1);

        Circle upLeftCircle = new Circle(x, y, 0);
        Circle upRightCircle = new Circle(x+1, y, 0);
        Circle downLeftCircle = new Circle(x, y+1, 0);
        Circle downRightCircle = new Circle(x+1, y+1, 0);
        
        if (orientation.equals(Angle.ZERO)){
            assert(edges.contains(upperEdge));
            assert(edges.contains(forwardDiagonal));
            assert(edges.contains(leftEdge));
            
            assert(edgeEndPoints.contains(upLeftCircle));
            assert(edgeEndPoints.contains(upRightCircle));
            assert(edgeEndPoints.contains(downLeftCircle));

        } else if (orientation.equals(Angle.DEG_90)){
            assert(edges.contains(upperEdge));
            assert(edges.contains(backwardDiagonal));
            assert(edges.contains(rightEdge));
            
            assert(edgeEndPoints.contains(upLeftCircle));
            assert(edgeEndPoints.contains(upRightCircle));
            assert(edgeEndPoints.contains(downRightCircle));
            
        } else if (orientation.equals(Angle.DEG_180)){
            assert(edges.contains(lowerEdge));
            assert(edges.contains(forwardDiagonal));
            assert(edges.contains(rightEdge));
            
            assert(edgeEndPoints.contains(upRightCircle));
            assert(edgeEndPoints.contains(downRightCircle));
            assert(edgeEndPoints.contains(downLeftCircle));
            
        } else { //DEG_270
            assert(edges.contains(lowerEdge));
            assert(edges.contains(backwardDiagonal));
            assert(edges.contains(leftEdge));
            
            assert(edgeEndPoints.contains(downRightCircle));
            assert(edgeEndPoints.contains(upLeftCircle));
            assert(edgeEndPoints.contains(downLeftCircle));
        }
    }
    
    @Override
    public double timeUntilCollision(Ball ball) {
        double minCollisionTime = Double.POSITIVE_INFINITY;

        for (LineSegment edge : this.edges) {
            minCollisionTime = Math.min(minCollisionTime, Geometry.timeUntilWallCollision(edge, ball.getCircle(), ball.getVelocity()));
        }
            
        for (Circle circle : this.edgeEndPoints) {
            minCollisionTime = Math.min(minCollisionTime, Geometry.timeUntilCircleCollision(circle, ball.getCircle(), ball.getVelocity()));
        }
        checkRep();
        return minCollisionTime;
    }

    @Override
    public String reactWhenHit(Ball ball, double time) {
        
        String returnMessage = "";
        Circle oldCircle = ball.getCircle();
        boolean hitMinEdge = false;

        double CenterX = ball.getCenterX() + ball.getVelocity().x() * time;
        double CenterY = ball.getCenterY() + ball.getVelocity().y() * time;
        
        ball.updateCenterX(CenterX);
        ball.updateCenterY(CenterY);
        
        for (LineSegment edge : this.edges) {
            double currCollisionTime = Geometry.timeUntilWallCollision(edge, oldCircle, ball.getVelocity());
            if (collisionTimesEqual(time, currCollisionTime)) {
                ball.updateVelocityWithGravityAndFriction(Geometry.reflectWall(edge, ball.getVelocity(), this.getCoefficient()), time);
                hitMinEdge = true;
            }
        }

        if (!hitMinEdge) {
            for (Circle circle : this.edgeEndPoints) {
                double currCollisionTime = Geometry.timeUntilCircleCollision(circle, oldCircle, ball.getVelocity());
                if (collisionTimesEqual(time, currCollisionTime)) {
                    ball.updateVelocityWithGravityAndFriction(Geometry.reflectCircle(circle.getCenter(), ball.getCircle().getCenter(), ball.getVelocity(), this.getCoefficient()), time);
                }
            }
        }

        //trigger
        for (GameObject obj : this.getTriggers()) {
            obj.doTriggerAction();
        }
        
        checkRep();
        
        return returnMessage;
    }
    
    @Override
    public String getSymbol() {
        if (orientation.equals(Angle.ZERO) || orientation.equals(Angle.DEG_180)){
            return "/";
        } else { //if 90 or 270
            return "\\";
        }
    }
    
    @Override
    public void drawComponent(Graphics2D g, double L, double offset, boolean heads){
        g.setColor(DEFAULT_COLOR);
        g.setStroke(new BasicStroke(DEFAULT_STROKE_WIDTH));
        
        for (LineSegment edge : edges){
            // multiply by L to get the right dimensions in pixels
            int x1 = (int) (edge.p1().x()*L + offset);
            int y1 = (int) (edge.p1().y()*L + offset);
            int x2 = (int) (edge.p2().x()*L + offset);
            int y2 = (int) (edge.p2().y()*L + offset);
            
            //draw the edge
            g.drawLine(x1, y1, x2, y2);
        }
    }

}
