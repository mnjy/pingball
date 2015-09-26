package implementation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import physics.*;

public class CircleBumper extends GameObject {    
    //AF: represents a circle bumper
    //RI: getCircle returns a circle with radius 0.5 and center at (x+0.5, y+0.5)
    
    private final double radius = 0.5;
    private final Circle edge;

    /**
     * Create a square-shaped bumper with these parameters:
     * @param name the name of the circle bumper
     * @param x the x coordinate of the bumper's origin
     * @param y the y coordinate of the bumper's origin
     */
    public CircleBumper(String name, int x, int y) {
        setPosition(x, y);
        setCoefficient(1.0);
        setName(name);
        edge = new Circle(x + radius, y + radius, radius);
        
        checkRep();
    }

    private void checkRep(){
        double x = getX();
        double y = getY();
        assert(edge.equals(new Circle(x+radius, y+radius, radius)));
    }
    
    @Override
    public double timeUntilCollision(Ball ball) {
        double collisionTime = Geometry.timeUntilCircleCollision(
                this.edge, ball.getCircle(), ball.getVelocity());
        checkRep();
        return collisionTime;
    }

    @Override
    public String reactWhenHit(Ball ball, double time) {
        String returnMessage = "";
        
        double centerX = ball.getCenterX() + ball.getVelocity().x() * time;
        double centerY = ball.getCenterY() + ball.getVelocity().y() * time;
        
        ball.updateCenterX(centerX);
        ball.updateCenterY(centerY);
        
        ball.updateVelocityWithGravityAndFriction(Geometry.reflectCircle(
                this.edge.getCenter(), ball.getCircle().getCenter(),
                ball.getVelocity(), this.getCoefficient()), time);
        
        //triggers
        for (GameObject obj : this.getTriggers()) {
            obj.doTriggerAction();
        }
        checkRep();
        
        return returnMessage;
    }
    
    @Override
    public String getSymbol() {
        return "O";
    }
    
    @Override
    public void drawComponent(Graphics2D g, double L, double offset, boolean heads){
        g.setColor(DEFAULT_COLOR);
        g.setStroke(new BasicStroke(DEFAULT_STROKE_WIDTH));
        
        // get dimensions
        double x = getX();
        double y = getY();
        double size = radius*2;

        // multiply by L to get the right dimensions in pixels
        int Lx = (int) (x*L + offset);
        int Ly = (int) (y*L + offset);
        int Lsize = (int) (size*L);
        
        // draw the circle
        g.drawOval(Lx, Ly, Lsize, Lsize);
    }
}
