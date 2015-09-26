package implementation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import physics.*;

public class Absorber extends GameObject {
    //AF: represents an absorber
    //RI: edges contains the correct 4 line segments to represent the absorber,  i.e. the line segments are connected and form a
    //rectangle with sides kL and mL in the x and y direction, respectively, at the specified location.
    //edgeEndPoints contains the correct 4 circles, i.e. the end-points of the line segments in edges, with radius 0.01
    //k and m must both be positive

    private final int m;
    private final int k;
    private final List<Ball> loadedBalls;
    private final Vect SHOOTING_VELOCITY = new Vect(0.0, -50.0);

    private final List<LineSegment> edges;
    private final List<Circle> edgeEndPoints;
        
    private final List<Point> displayLocations = new ArrayList<Point>(); //for getDisplayLocations
    
    /**
     * @param name the name of the absorber
     * @param x x coordinate
     * @param y y coordinate
     * @param kMultiplier width of absorber. Requires positive.
     * @param mMultiplier height of absorber. Requires positive.
     */
    public Absorber(String name, int x, int y, int kMultiplier, int mMultiplier) {

        setPosition(x, y);
        setName(name);
        setCoefficient(1);

        this.m = mMultiplier;
        this.k = kMultiplier;
        this.loadedBalls = new ArrayList<Ball>();
        
        LineSegment top = new LineSegment(x, y, x+k, y);
        LineSegment bottom = new LineSegment(x, y+m, x+k, y+m);
        LineSegment left = new LineSegment(x, y, x, y+m);
        LineSegment right = new LineSegment(x+k, y, x+k, y+m);
        this.edges = new ArrayList<LineSegment>(Arrays.asList(top, bottom, right, left));
      
        Circle upLeftCircle = new Circle(x, y, 0);
        Circle upRightCircle = new Circle(x+k, y, 0);
        Circle downLeftCircle = new Circle(x, y+m, 0);
        Circle downRightCircle = new Circle(x+k, y+m, 0);
        this.edgeEndPoints = new ArrayList<Circle>(Arrays.asList(upRightCircle, upLeftCircle, downRightCircle, downLeftCircle));
        
        //just make display locations once and store it as a field, instead of making it again every time
        //we need it
        for (int i = x; i <= x + kMultiplier - 1; i++){
            for (int j = (int)y; j <= y + mMultiplier - 1; j++){
                displayLocations.add(new Point(i, j));
            }
        }
        
        checkRep();
    }

    private void checkRep(){
        assert(edges.size() == 4);
        assert(k > 0);
        assert(m > 0);
        
        double startX = getX();
        double startY = getY();
        double endX = getX() + k;
        double endY = getY() + m;
        
        assert(edges.contains(new LineSegment(startX, startY, endX, startY)));
        assert(edges.contains(new LineSegment(startX, endY, endX, endY)));
        assert(edges.contains(new LineSegment(startX, startY, startX, endY)));
        assert(edges.contains(new LineSegment(endX, startY, endX, endY)));
        
        assert(edgeEndPoints.contains(new Circle(startX, startY, 0)));
        assert(edgeEndPoints.contains(new Circle(startX, endY, 0)));
        assert(edgeEndPoints.contains(new Circle(endX, startY, 0)));
        assert(edgeEndPoints.contains(new Circle(endX, endY, 0)));
    }
   

    @Override
    public double timeUntilCollision(Ball ball) {
        double minCollisionTime = Double.POSITIVE_INFINITY;

        for (LineSegment edge : this.edges) {
            minCollisionTime = Math.min(minCollisionTime, Geometry
                    .timeUntilWallCollision(edge, ball.getCircle(),
                            ball.getVelocity()));
        }
        
        for (Circle circle : this.edgeEndPoints) {
            minCollisionTime = Math.min(minCollisionTime, Geometry
                    .timeUntilCircleCollision(circle, ball.getCircle(),
                            ball.getVelocity()));
        }
        checkRep();
        return minCollisionTime;
    }
    
    @Override
    public String reactWhenHit(Ball ball, double time) {
        String returnMessage = "";
        final double BALL_RADIUS = 0.25;

        this.loadedBalls.add(ball);
        ball.updateCenterX(this.getX() + k - BALL_RADIUS);
        ball.updateCenterY(this.getY() + m - BALL_RADIUS);
        ball.updateVelocity(new Vect(0.0, 0.0));
        ball.toggleInAbsorber();

        //triggers
        for(GameObject obj:this.getTriggers()) {
            obj.doTriggerAction();
        }
        checkRep();
        
        return returnMessage;
    }

    /**
     * @return whether or not there is a ball in the absorber
     */
    private boolean isLoaded(){
        return loadedBalls.size() != 0;
    }
    
    @Override
    public void doTriggerAction() {
        final double BALL_RADIUS = 0.25;
        if (this.isLoaded()) {
            Ball firstLoadedBall = loadedBalls.remove(0);
            firstLoadedBall.updateVelocity(SHOOTING_VELOCITY);
            //set location to the top of the absorber
            firstLoadedBall.updateCenterX(this.getX() + k - BALL_RADIUS);
            firstLoadedBall.updateCenterY(this.getY() - BALL_RADIUS);
            firstLoadedBall.toggleInAbsorber();
        }
        checkRep();
    }
    
    @Override
    public List<Point> getDisplayLocations(){
        return displayLocations;
    }
    
    @Override
    public String getSymbol() {
        return "=";
    }
    
    @Override
    public void drawComponent(Graphics2D g, double L, double offset, boolean heads){
        g.setColor(DEFAULT_COLOR);
        g.setStroke(new BasicStroke(DEFAULT_STROKE_WIDTH));
        
        // draw the absorber
        // multiply by L to get the right dimensions in pixels
        g.drawRect((int) (getX()*L + offset), (int) (getY()*L + offset), (int) (k*L + offset), (int) (m*L + offset));
    }
}
