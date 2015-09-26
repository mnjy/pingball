package implementation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import physics.Circle;
import physics.Geometry;
import physics.Vect;



public class Ball extends GameObject {
    //AF: represents a pingball ball
    //RI: circle radius = 0.25

    private Circle circle;
    private Vect velocity;
    private boolean inAbsorber = false;
    private Vect gravity = new Vect(0, 25.0);
    private double mu = 0.025;
    private double mu2 = 0.025;
    private BufferedImage head;
    

    
    /**
     * Creates a ball object with these parameters:
     * @param name the name of the ball
     * @param x the x coordinate of the ball
     * @param y the y coordinate of the ball
     * @param xVelocity - the x velocity of the ball
     * @param yVelocity - the y velocity of the ball
     */
    public Ball(String name, double x, double y, double xVelocity, double yVelocity) {
        this.circle = new Circle(x, y, 0.25);
        this.velocity = new Vect(xVelocity, yVelocity);
        setName(name);
        setPosition((int) Math.floor(x), (int) Math.floor(y));
        checkRep();
        Random generator = new Random();
        int i = generator.nextInt(10);
        if (i<=5){
            try {
                head = ImageIO.read(getClass().getResource("/resources/miller.png"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                head = ImageIO.read(getClass().getResource("/resources/goldman.png"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    
    private void checkRep() {
        assert(circle.getRadius() == 0.25);
    }
    
    /**
     * Called when the ball is added to a board. Sets the physics of the ball to the physics of the board
     * @param gravity
     * @param mu
     * @param mu2
     */
    protected void setPhysics(Vect gravity, double mu, double mu2){
        this.gravity = gravity;
        this.mu = mu;
        this.mu2 = mu2;
    }
    
    /**
     * Check if the ball is held static in the absorber
     * @return boolean with whether or not the ball is captured
     */
    public boolean inAbsorber() {
        return this.inAbsorber;
    }
    
    /**
     * Function that switches the state of the ball:
     *  - if the ball is in the absorber, then toggle it to not be in the absorber anymore
     *  - if the ball is not in the absorber, then toggle it to be there
     */
    public void toggleInAbsorber() {
        this.inAbsorber = !this.inAbsorber;
        checkRep();
    }
    
    /**
     * @return the radius of the ball
     */
    public double radius() {
        return circle.getRadius();
    }
    
    /**
     * Update x coordinate of center
     * @param newX - new x coordinate of center
     */
    public void updateCenterX(double newX) {
        if (newX < 0.25)
            newX = 0.25;
        if (newX > 19.75)
            newX = 19.75;
        this.circle = new Circle(newX, this.circle.getCenter().y(),
                this.circle.getRadius());
        setX((int) Math.floor(newX));
        checkRep();
    }

    /**
     * Update y coordinate of center
     * @param newY - new y coordinate of center
     */
    public void updateCenterY(double newY) {
        if (newY < 0.25)
            newY = 0.25;
        if (newY > 19.75)
            newY = 19.75;
        this.circle = new Circle(this.circle.getCenter().x(), newY,
                this.circle.getRadius());
        setY((int) Math.floor(newY));
        checkRep();
    }

    /**
     * Get x coordinate of center
     * @return x coordinate of center
     */
    public double getCenterX() {
        return circle.getCenter().x();
    }

    /**
     * Get y coordinate of center
     * @return y coordinate of center
     */
    public double getCenterY() {
        return circle.getCenter().y();
    }

    /**
     * Get x coordinate of velocity
     * @return x coordinate of velocity
     */
    public double getVelocityX() {
        return this.velocity.x();
    }

    /**
     * Get y coordinate of velocity
     * @return y coordinate of velocity
     */
    public double getVelocityY() {
        return this.velocity.y();
    }

    /**
     * Update velocity of ball, applying gravity and friction
     * @param velocity - new velocity of ball (unmodified by forces yet)
     * @param time - time spanned in the course of this motion
     */
    public void updateVelocityWithGravityAndFriction(Vect velocity, double time) {
        this.velocity = velocity.plus(gravity.times(time));
        this.velocity = this.velocity.times(1 - mu * time
                - mu2 * this.velocity.length() * time);
        checkRep();
    }  
    
    /**
     * Update velocity of ball without gravity or friction
     * @param velocity - new velocity of ball
     */
    public void updateVelocity(Vect velocity) {
        this.velocity = velocity;
        checkRep();
    }  

    /**
     * Get velocity of ball
     * @return velocity of ball
     */
    public Vect getVelocity() {
        return new Vect(this.velocity.angle(), this.velocity.length());
    }

    /**
     * @return circle representation of ball
     */
    public Circle getCircle() {
        return new Circle(this.circle.getCenter().x(), this.circle.getCenter()
                .y(), this.circle.getRadius());
    }
    
    @Override
    public String getSymbol() {
        return "*";
    }
    
    /**
     * @param other other object
     * @return true if other represents the same ball as this ball
     */
    @Override
    public boolean equals(Object other) {
    	if (!(other instanceof Ball)) return false;
    	Ball otherBall = (Ball) other;
    	return this.circle.equals(otherBall.circle) && this.velocity.equals(otherBall.velocity)
    			&& this.inAbsorber==otherBall.inAbsorber;
    }
    
    @Override
    public int hashCode(){
        return (int) (this.getCenterX()+this.getCenterY()+this.getVelocityX()+ this.getVelocityY());
    }

    @Override
    public double timeUntilCollision(Ball ball) {
        double time = Geometry.timeUntilBallBallCollision(this.getCircle(),
                this.getVelocity(), ball.getCircle(), ball.getVelocity());
        checkRep();
        return time;
    }

    @Override
    public String reactWhenHit(Ball ball, double time) {      
        
        String returnMessage = "";
        
        double thisBallCenterX = this.getCenterX() + this.getVelocityX() * time;
        double thisBallCenterY = this.getCenterY() + this.getVelocityY() * time;
        
        double otherBallCenterX = ball.getCenterX() + ball.getVelocityX() * time;
        double otherBallCenterY = ball.getCenterY() + ball.getVelocityY() * time;
        
        this.updateCenterX(thisBallCenterX);
        this.updateCenterY(thisBallCenterY);
        
        ball.updateCenterX(otherBallCenterX);
        ball.updateCenterY(otherBallCenterY);
        
        Geometry.VectPair velocities = Geometry.reflectBalls(this.getCircle().getCenter(),
                1, this.getVelocity(), ball.getCircle().getCenter(), 1,
                ball.getVelocity());
        
        this.updateVelocityWithGravityAndFriction(velocities.v1, time);
        ball.updateVelocityWithGravityAndFriction(velocities.v2, time); 
        
        for (GameObject obj : this.getTriggers()) {
            obj.doTriggerAction();
        }
        checkRep();
        
        return returnMessage;
    }


    @Override
    public void drawComponent(Graphics2D g, double L, double offset, boolean heads){

        // get dimensions
        double x = this.getCenterX() - this.radius();
        double y = this.getCenterY() - this.radius();
        double size = radius()*2;

        // multiply by L to get the right dimensions in pixels
        int Lx = (int) (x*L + offset);
        int Ly = (int) (y*L + offset);
        int Lsize = (int) (size*L);

        if (!heads){
            // draw the ball
            g.setColor(DEFAULT_COLOR);
            g.setStroke(new BasicStroke(DEFAULT_STROKE_WIDTH));
            g.drawOval(Lx, Ly, Lsize, Lsize);
        }else{
            // draw fun ball!
            float[] scales = { 1f, 1f, 1f, 0.8f };
            float[] offsets = new float[4];
            RescaleOp rop = new RescaleOp(scales, offsets, null);
            g.drawImage(head, rop, Lx, Ly);

        }
    }

}
