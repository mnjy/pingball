package implementation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import physics.*;

public class SquareBumper extends GameObject {
    //AF: represents a square bumper
    //RI: edges contains the correct 4 line segments to represent the square,  i.e. the line segments are connected and form a
    //square with sides L at the specified location.
    //edgeEndPoints contains the correct 4 circles, i.e. the end-points of the line segments in edges, with radius 0.01

    private final List<LineSegment> edges;
    private final List<Circle> edgeEndPoints;

    /**
     * Create a square-shaped bumper with these parameters:
     * @param name the name of the square bumper
     * @param x the x coordinate of the bumper's origin
     * @param y the y coordinate of the bumper's origin
     */
    public SquareBumper(String name, int x, int y) {
        setPosition(x, y);
        setName(name);
        setCoefficient(1.0);
        
        LineSegment upperEdge = new LineSegment(x, y, x+1, y);
        LineSegment lowerEdge = new LineSegment(x, y+1, x+1, y+1);
        LineSegment rightEdge = new LineSegment(x+1, y, x+1, y+1);
        LineSegment leftEdge = new LineSegment(x, y, x, y+1);
        edges = new ArrayList<LineSegment>(Arrays.asList(upperEdge, lowerEdge, rightEdge, leftEdge));

        Circle upLeftCircle = new Circle(x, y, 0);
        Circle upRightCircle = new Circle(x+1, y, 0);
        Circle downLeftCircle = new Circle(x, y+1, 0);
        Circle downRightCircle = new Circle(x+1, y+1, 0);
        edgeEndPoints = new ArrayList<Circle>(Arrays.asList(upRightCircle, upLeftCircle, downRightCircle, downLeftCircle));
        
        checkRep();
    }

    private void checkRep(){
        assert(edges.size() == 4);
        double x = getX();
        double y = getY();
        
        assert(edges.contains(new LineSegment(x, y, x+1, y)));
        assert(edges.contains(new LineSegment(x, y+1, x+1, y+1)));
        assert(edges.contains(new LineSegment(x+1, y, x+1, y+1)));
        assert(edges.contains(new LineSegment(x, y, x, y+1)));

        assert(edgeEndPoints.contains(new Circle(x, y, 0)));
        assert(edgeEndPoints.contains(new Circle(x+1, y, 0)));
        assert(edgeEndPoints.contains(new Circle(x, y+1, 0)));
        assert(edgeEndPoints.contains(new Circle(x+1, y+1, 0)));
    }

    @Override
    public double timeUntilCollision(Ball ball) {
        double minCollisionTime = Double.POSITIVE_INFINITY;

        for (LineSegment edge : this.edges) {
            double currCollisionTime = Geometry.timeUntilWallCollision(edge, ball.getCircle(), ball.getVelocity());
            minCollisionTime = Math.min(minCollisionTime, currCollisionTime);
        }

        for (Circle circle : this.edgeEndPoints) {
            double currCollisionTime = Geometry.timeUntilCircleCollision(circle, ball.getCircle(), ball.getVelocity());
            minCollisionTime = Math.min(minCollisionTime, currCollisionTime);
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
        return "#";
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
