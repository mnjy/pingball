package implementation;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import physics.Geometry;
import physics.Vect;

public class BallTest {
    // Testing strategy
    //
    // getTimeUntilCollision (with other ball)
    // the two balls will collide
    // - time until collision > 0
    // - time until collision = 0
    // the two balls will not collide
    //
    //
    // getSymbol
    // must return "*" for ball
    //
    // setVelocity and getVelocity
    // `set velocity, then get velocity, the two must equal
    //
    // changePosition(time)
    // time = 0
    // velocity = 0
    // velocity.x > 0, velocity.y = 0
    // velocty.x = 0, velocity.y < 0
    //
    // accelerateDueToGravity
    //
    //
    // hashCode
    // two equal balls
    //
    // equals
    // two equal balls
    // two not equal balls

    // //////////////////////////////////////////////////////////////////////////////////
    // TESTS FOR GetTimeUntilCollision
    // //////////////////////////////////////////////////////////////////////////////////

    // Tests that two balls bound to collide given nothing else returns
    // valid time until collision
    @Test
    public void testGetTimeUntilCollisionBallsWillCollide() {
        Ball ball = new Ball("ball1", 5, 5, 0, 1);
        Ball otherBall = new Ball("ball2", 5, 10,0,0);
        double time = otherBall.timeUntilCollision(ball);
        assertTrue(time > 0);
        assertFalse(Double.isInfinite(time));
    }

    // Tests that two balls that are touching have zero time
    // until collision
    @Test
    public void testGetTimeUntilCollisionIsZero() {
        Ball ball = new Ball("ball1", 5, 5, 1,0);
        double ballRadius = 0.25;
        Ball otherBall = new Ball("ball2", 5 + 2 * ballRadius, 5,0,0);
        double time = ball.timeUntilCollision(otherBall);
        assertTrue(time < 2 * Double.MIN_VALUE);
    }

    // Tests that two balls that won't collide have infinite time
    // till collision
    @Test
    public void testGetTimeUntilCollisionNoCollision() {
        Ball ball = new Ball("ball1", 5, 5, 0,1);
        Ball otherBall = new Ball("ball2", 10, 10,0,0);
        double time = otherBall.timeUntilCollision(ball);
        assertTrue(Double.isInfinite(time));
    }


    // //////////////////////////////////////////////////////////////////////////////////
    // TESTS FOR getSymbol
    // //////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testGetSymbol() {
        Ball ball = new Ball("ball1", 5, 5, 0,1);
        String symbol = ball.getSymbol();
        assertEquals("*", symbol);
    }

    // //////////////////////////////////////////////////////////////////////////////////
    // TESTS for setVelocity and getVelocity
    // //////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testGetAndSetVelocity() {
        Ball ball = new Ball("ball1", 5, 5, 0, 0);
        Vect newVelocity = new Vect(3.5, -2.5);
        ball.updateVelocity(newVelocity);
        assertEquals(newVelocity,new Vect( ball.getVelocityX(), ball.getVelocityY()));
    }

    // //////////////////////////////////////////////////////////////////////////////////
    // TESTS for changePosition
    // //////////////////////////////////////////////////////////////////////////////////

    // Tests that the position change is computed correctly
    @Test
    public void testChangePositionXAndYVelocities() {
        Ball ball = new Ball("ball1", 10, 10,1,-1);
        Ball otherball=new Ball("ball2", 10, 10, 1, 1);
        ball.reactWhenHit(otherball,0.5);
        double coordinatex = ball.getCenterX();
        double coordinatey=ball.getCenterY();
        assertEquals(10.5, coordinatex, 0.0001);
        assertEquals(9.5, coordinatey, 0.00001);
    }

    // //////////////////////////////////////////////////////////////////////////////////
    // TESTS for accelerateDueToGravity
    // //////////////////////////////////////////////////////////////////////////////////

    // Tests gravity in opposite direction of motion
    @Test
    public void testAccelerateDueToGravityDecreasingVelocity() {
        double startVelocityX = 0;
        double startVelocityY = -50;
        Ball ball = new Ball("ball1", 10, 10, 0, -50);
        ball.updateVelocityWithGravityAndFriction(new Vect(startVelocityX, startVelocityY), 2);

        // default gravity is 25L/sec^2, but this should be configurable
        double gravity = 25.0;
        double time = 2;
        double predictedVelocityY = startVelocityY + gravity * time;

        assertEquals(predictedVelocityY, ball.getVelocity().y(), 0.00001);
    }

    // Tests gravity in same direction of motion
    @Test
    public void testAccerelateDueToGravityIncreasingVelocity() {
        double startVelocityX = 0;
        double startVelocityY = 1;
        Ball ball = new Ball("ball1", 10, 10, 0, 1);;
        ball.updateVelocityWithGravityAndFriction(new Vect(startVelocityX, startVelocityY), .1);

        double gravity = 25.0;
        double time = 0.1;
        double predictedVelocityY = startVelocityY + gravity * time;

        assertEquals(predictedVelocityY, ball.getVelocity().y(), 0.05);
    }


    // //////////////////////////////////////////////////////////////////////////////////
    // TESTS for hashCode
    // //////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testHashCodeBallsEqualByObservation() {
        Ball ball1 = new Ball("ball1", 5, 5,5,5);
        Ball ball2 = new Ball("ball1", 5, 5,5,5);
        assertEquals(ball1.hashCode(), ball2.hashCode());
    }

    // //////////////////////////////////////////////////////////////////////////////////
    // TESTS for equals
    // //////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testEqualsByObservation() {
        Ball ball1 = new Ball("ball1", 5, 5,5,5);
        Ball ball2 = new Ball("ball1", 5, 5,5,5);
        assertTrue(ball1.equals(ball2));
    }
}
