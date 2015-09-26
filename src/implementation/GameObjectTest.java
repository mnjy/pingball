package implementation;

import static org.junit.Assert.*;
import implementation.Board.BoundarySide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import physics.Angle;
import physics.Vect;
import physics.LineSegment;

public class GameObjectTest {

	///////////////////TESTING STRATEGY////////////////////
	/*
	 * Testing the following classes:
	 * 1. Ball
	 * 2. CircleBumper
	 * 3. SquareBumper
	 * 4. Absorber
	 * 5. TriangleBumper
	 * 6. Flipper 
	 * 7. Boundary
	 * 
	 * Correct behavior is determined by physics calculations
	 */

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	////////////////////////BALL TESTS//////////////////////
	/*
	 * Ball Testing Strategy:
	 * We partitioned inputs by
	 * 1. UpdateCenter
	 * 2. UpdateVelocity: positive, negative
	 * 3. toString
	 * 4. TimeUntilCollision: Infinity, 0, ~5
	 * 5. ReachWhenHit
	 * Correct behaviour is determined by physics calculations
	 */

	@Test
	public void testBallUpdateCenter()  {
		Ball ball = new Ball("ball1", 5, 5, 0.3, 0.1);
		ball.updateCenterX(5.5);
		assertTrue(ball.getCenterX()==5.5);
		ball.updateCenterY(6);
		assertTrue(ball.getCenterY()==6);
	}

	@Test
	public void testBallUpdateVelocity() {
		Ball ball = new Ball("ball1", 5, 5, 0.3, 0.1);
		ball.updateVelocity(new Vect(0.4, 0.2));
		assertEquals(ball.getVelocity(), ball.getVelocity());
	}

	@Test
	public void testBallUpdateVelocityNegative()  {
		Ball ball = new Ball("ball1", 5, 5, -0.3, 0.1);
		ball.updateVelocity(new Vect(0.0, 0.2));
		assertEquals(ball.getVelocity(), ball.getVelocity());
	}

	@Test
	public void testBallToString()  {
		Ball ball = new Ball("ball1", 5, 5, 0.3, 0.1);
		assertEquals(ball.getSymbol(), "*");
	}

	@Test
	public void testBallTimeUntilCollisionInfinity()  {
		Ball ball1 = new Ball("ball1", 5, 5, -0.3, 0.1);
		Ball ball2 = new Ball("ball2", 5, 6, 0.4, 0.5);
		double infinity = Double.POSITIVE_INFINITY;
		assertTrue(ball1.timeUntilCollision(ball2)==infinity);
	}

	@Test
	public void testBallTimeUntilCollisionAdjacent()  {
		Ball ball1 = new Ball("ball1", 5.25, 5, 0.4, 0.1);
		Ball ball2 = new Ball("ball2", 5.75, 5, -0.4,0.1);
		assertTrue(ball1.timeUntilCollision(ball2)==0.0);
	}


	@Test
	public void testBallTimeUntilCollisionClose()  {
		Ball ball1 = new Ball("ball1", 5, 5, 0.4,0.1);
		Ball ball2 = new Ball("ball2", 10, 5, -0.4, 0.1);
		double collisionTime = ball1.timeUntilCollision(ball2);
		assertTrue(collisionTime>4.0 && collisionTime<6.0);
	}

	@Test
	public void testBallReactWhenHit()  {
		Ball thisBall = new Ball("ball1", 5, 5, 0, 0);
		Vect otherVelocity = new Vect(0,1);
		Ball otherBall =new Ball("ball2", 5, 3, 0, 1);
		try {
			thisBall.reactWhenHit(otherBall, thisBall.timeUntilCollision(otherBall));
			assertTrue(Math.abs(thisBall.getVelocityX()-otherVelocity.x())<0.1);
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	/////////////////////TESTING CIRCLEBUMPER////////////////
	/*
	 * Testing Strategy for CircleBumper:
	 * 1. toString()
	 * 2. TimeUntilCollision: Infinity, zero, close
	 * 3. ReactWhenHit: Perpendicular to Tangent, 45 degrees to Tangent
	 *
	 * Correct behaviour is determined by physics calculations
	 * 	 */

	@Test
	public void testCircleBumperToString()  {
		CircleBumper cb = new CircleBumper("circle1", 5,5);
		assertEquals(cb.getSymbol(), "O");
	}

	@Test
	public void testCircleBumperTimeUntilCollisionInfinity()  {
		CircleBumper cb = new CircleBumper("circle1", 5,5);
		Ball ball2 = new Ball("ball1", 10, 5, 0.4, 0.1);
		double infinity = Double.POSITIVE_INFINITY;
		assertTrue(cb.timeUntilCollision(ball2)==infinity);	
	}

	@Test
	public void testCircleBumperTimeUntilCollisionAdjacent()  {
		CircleBumper cb = new CircleBumper("circle1", 5,5);
		Ball ball2 = new Ball("ball1", 6, 5, -0.1, 0);
		assertTrue(cb.timeUntilCollision(ball2)==0.0);
	}

	@Test
	public void testCircleBumperTimeUntilCollisionClose()  {
		CircleBumper cb = new CircleBumper("circle1", 5, 5);
		Ball ball2 = new Ball("ball1", 10, 5, -1, 0);
		double ETA = cb.timeUntilCollision(ball2);
		assertTrue(ETA<6.0 && ETA>3.0);
	}

	@Test
	public void testCircleBumperReactWhenHitPerpendicularToTangent()  {
		CircleBumper cb = new CircleBumper("circle1", 5, 5);
		Ball ball2 = new Ball("ball2", 6, 3, 0, 1);
		try {
			int initialVelocityY = 1;
			int gravity = 25;
			double finalVelocityY = initialVelocityY+gravity*cb.timeUntilCollision(ball2);
			cb.reactWhenHit(ball2, cb.timeUntilCollision(ball2));
			assertTrue(ball2.getVelocityY()<0);
			assertTrue(ball2.getVelocityY()<-finalVelocityY);
		} catch (Exception e) {
			throw new UnsupportedOperationException();
		}
	}

	@Test
	public void testCircleBumperReactWhenHit45degreesToTangentFromTop()  {
		CircleBumper cb = new CircleBumper("circle1", 5, 5);
		Ball ball2 = new Ball("ball2", 4, 4, 1, 1);
		try {
			int initialVelocityY = 1;
			int gravity = 25;
			double finalVelocity = initialVelocityY+gravity*cb.timeUntilCollision(ball2);
			cb.reactWhenHit(ball2, cb.timeUntilCollision(ball2));
			assertTrue(ball2.getVelocityX()<1);
			assertTrue(ball2.getVelocityY()<Math.abs(finalVelocity));

		} catch (Exception e) {
			throw new UnsupportedOperationException();
		}
	}

	///////////////////////TESTING SQUARE BUMPER////////////////////
	/*
	 * Testing Strategy for Square Bumper:
	 * 1. toString()
	 * 2. TimeUntilCollision: Infinity, zero, close
	 * 3. ReactWhenHit: Perpendicular to Tangent, 45 degrees to Tangent
	 * 
	 * Correct behaviour is determined by physics calculations
	 */
	@Test
	public void testSquareBumperToString()  {
		SquareBumper sb = new SquareBumper("square", 5, 5);
		assertEquals(sb.getSymbol(), "#");
	}

	@Test
	public void testSquareBumperTimeUntilCollisionInfinity()  {
		SquareBumper sb = new SquareBumper("square", 5, 5);
		Ball ball2 = new Ball("ball2", 10, 5, 0.4, 0.1);
		double infinity = Double.POSITIVE_INFINITY;
		assertTrue(sb.timeUntilCollision(ball2)==infinity);	
	}

	@Test
	public void testSquareBumperTimeUntilCollisionAdjacent()  {
		SquareBumper sb = new SquareBumper("square", 5, 5);
		Ball ball2 = new Ball("ball", 5.25, 5, -1, 0);
		assertTrue(sb.timeUntilCollision(ball2)==0.0);
	}

	@Test
	public void testSquareBumperTimeUntilCollisionClose()  {
		SquareBumper sb = new SquareBumper("square", 3, 5);
		Ball OneDimensionalSphere = new Ball("ball", 3.25, 5, -1, 0);
		assertTrue(sb.timeUntilCollision(OneDimensionalSphere)==0.0);
	}

	@Test
	public void testSquareBumperReactWhenHit90degreesFromSide()  {
		SquareBumper sb = new SquareBumper("square", 5, 5);
		Vect oldVelocity2 = new Vect(-10.0, 0);
		Ball ball2 = new Ball("ball2", 8, 6, -10, 0);
		try {
			sb.reactWhenHit(ball2, sb.timeUntilCollision(ball2));
			assertTrue(Math.abs(ball2.getVelocityX()-(-1.0*oldVelocity2.x()))<1.0);
			assertTrue(ball2.getVelocityY()>0 && ball2.getVelocityY()<10);

		} catch (Exception e) {
			throw new UnsupportedOperationException();
		}
	}

	@Test
	public void testSquareBumperReactWhenHitAbout45degreesFromSide()  {
		SquareBumper sb = new SquareBumper("square", 5,5);
		Vect oldVelocity = new Vect(-1.0, -1.0);
		Ball ball = new Ball("ball", 7, 7, -1, -1);
		try {
			double initialVelocity=oldVelocity.y();
			int gravity=25;
			double finalVelocityY=initialVelocity+gravity*sb.timeUntilCollision(ball);
			sb.reactWhenHit(ball, sb.timeUntilCollision(ball));
			assertTrue(ball.getVelocityX()>0);
			assertTrue(Math.abs(ball.getVelocityY())<finalVelocityY && ball.getVelocityY()>0);
		} catch(Exception e) {
			throw new UnsupportedOperationException();
		}
	}

	///////////////////////TESTING ABSORBER////////////////////
	/*
	 * Testing Strategy: Absorber
	 * 1. timeUntilCollision: Close, adjacent, infinity
	 * 2. ReactWhenHit: perpendicular, at an angle 
	 * 
	 * Correct behaviour is determined by physics calculations
	 */
	@Test
	public void testAbsorberToString()  {
		Absorber a = new Absorber("absorber", 5, 5, 1, 1);
		assertEquals(a.getSymbol(), "=");
	}

	@Test
	public void testAbsorberTimeUntilCollisionClose() {
		Absorber a = new Absorber("absorber", 5, 5, 3, 3);
		Ball ball = new Ball("ball", 6, 2, 0, 1);
		double ETA = a.timeUntilCollision(ball);
		assertTrue(ETA<3.0 && ETA>1.0);
	}

	@Test
	public void testAbsorberTimeUntilCollisionAdjacent()  {
		Absorber a = new Absorber("absrober", 5, 5, 3, 3);
		Ball ball = new Ball("ball", 6, 4, 0, 1);
		double ETA = a.timeUntilCollision(ball);
		assertTrue(ETA==0.75);
	}

	@Test
	public void testAbsorberTimeUntilCollisionINFINITY()  {
		Absorber a = new Absorber("absorber", 5, 5, 3, 3);
		Ball ball = new Ball("ball", 2, 2, 0, 1);
		double ETA = a.timeUntilCollision(ball);
		Double infinity = Double.POSITIVE_INFINITY;
		assertTrue(ETA==infinity);
	}

	@Test
	public void testAbsorberReactWhenHitPerpendicular()  {
		Absorber a = new Absorber("absorber", 5,5,3,3);
		Ball ball = new Ball("ball", 6, 2, 0, 1);
		try {
			a.reactWhenHit(ball, a.timeUntilCollision(ball));
			assertTrue(ball.getCenterX()==7.75);
			assertTrue(ball.getCenterY()==7.75);
			assertTrue(ball.getVelocityX()==0.0 && ball.getVelocityY()==0.0);
		} catch (Exception e) {
			throw new UnsupportedOperationException();
		}
	}

	@Test
	public void testAbsorberReactWhenHitAtAnAngle()  {
		Absorber a = new Absorber("absorber", 5, 5, 3, 3);
		Ball ball = new Ball("ball", 6, 3, 1, 1);
		try {
			a.reactWhenHit(ball, a.timeUntilCollision(ball));
			assertTrue(ball.getCenterX()==7.75);
			assertTrue(ball.getCenterY()==7.75);
			assertTrue(ball.getVelocityX()==0.0 && ball.getVelocityY()==0.0);
		} catch (Exception e) {
			throw new UnsupportedOperationException();
		}
	}

	/////////////////////TRIANGLE BUMPER TESTS////////////////
	/*
	 * Testing Strategy: TriangleBumper
	 * 1. toString: upleft, upright, downleft, downright
	 * 2. timeUntilCollision: Close, adjacent, infinity
	 * 3. ReactWhenHit: perpendicular, at an angle
	 * 
	 * Correct behaviour is determined by physics calculations
	 */

	@Test
	public void testTriangleBumperToString()  {
		TriangleBumper tb = new TriangleBumper("Tri1", 5, 5, Angle.ZERO);
		TriangleBumper tb1 = new TriangleBumper("Tri2", 5, 5, Angle.DEG_90);
		TriangleBumper tb2 = new TriangleBumper("Tri3", 5, 5, Angle.DEG_180);
		TriangleBumper tb3 = new TriangleBumper("Tri4", 5, 5, Angle.DEG_270);
		assertEquals(tb.getSymbol(),"/");
		assertEquals(tb1.getSymbol(),"\\");
		assertEquals(tb2.getSymbol(),"/");
		assertEquals(tb3.getSymbol(),"\\");

	}

	@Test
	public void testTriangleBumperTimeUntilCollisionClose()  {
		TriangleBumper tb = new TriangleBumper("Tri1", 5,5,Angle.ZERO);
		Ball ball = new Ball("ball", 5, 3, 0, 1);
		double ETC = tb.timeUntilCollision(ball);
		assertTrue(ETC==1.75);
	}

	@Test
	public void testTriangleBumperTimeUntilCollisionAdjacent()  {
		TriangleBumper tb = new TriangleBumper("Tri", 5, 5, Angle.ZERO);
		Ball ball = new Ball("ball", 5, 5,0, 1);
		double ETC = tb.timeUntilCollision(ball);
		assertTrue(ETC<0.751);
	}

	@Test
	public void testTriangleBumperTimeUntilCollisionINFINITY()  {
		TriangleBumper tb = new TriangleBumper("ball", 5, 5,Angle.ZERO);
		Ball ball = new Ball("ball", 7, 7, 0, 1);
		double ETC = tb.timeUntilCollision(ball);
		double infinity = Double.POSITIVE_INFINITY;
		assertTrue(ETC==infinity);
	}

	@Test
	public void testTriangleBumperReactWhenHit()  {
		TriangleBumper tb = new TriangleBumper("Tri", 5, 5, Angle.DEG_180);
		Ball ball = new Ball("ball", 6, 3, 0, 1);
		try {
			double gravity=25;
			double initialV=1;
			double finalV=initialV+gravity*tb.timeUntilCollision(ball);
			tb.reactWhenHit(ball, tb.timeUntilCollision(ball));
			assertTrue(ball.getVelocityY()<0);
			assertTrue(Math.abs(ball.getVelocityY()) < finalV);

		} catch (Exception e) {
			assertFalse(false);
			System.out.print(e);
		}
	}

	////////////////////////FLIPPER TESTS/////////////////////
	/*
	 * Testing Strategy for Flipper:
	 * 1. toString: left, right - position up
	 * 2. togglePosition: position up, position down
	 * 3. timeUntilCollision: Close, adjacent, infinity
	 * 4. reactWhenHit: from top, bottom, sides
	 * 
	 * Correct behaviour is determined by physics calculations
	 */

	@Test
	public void testFlipperToStringLeftRightANDPositionUP()  {
		Flipper fLeft = new Flipper("Flip1", 5, 5, Flipper.FlipperType.LEFT,
				Angle.DEG_90);
		Flipper fRight = new Flipper("Flip2", 5,5,Flipper.FlipperType.RIGHT,
				Angle.DEG_90);
		assertEquals(fLeft.getSymbol(), "-");
		assertEquals(fRight.getSymbol(), "-");
	}

	@Test
	public void testFlipperToStringTogglePositionDOWN()  {
		Flipper fLeft = new Flipper("flip1", 5, 5,Flipper.FlipperType.LEFT,
				Angle.ZERO);
		Flipper fRight = new Flipper("flip2", 5, 5, Flipper.FlipperType.RIGHT,
				Angle.ZERO);
		double angle = 9./108.;
		fLeft.updateAngle(angle);

		assertEquals(fLeft.getSymbol(), "|");
		fRight.updateAngle(angle);
		assertEquals(fRight.getSymbol(), "|");

	}

	@Test
	public void testFlipperTimeUntilCollisionClose()  {
		Flipper f = new Flipper("Flip1", 5,5,Flipper.FlipperType.LEFT,
				Angle.ZERO);
		Ball ball = new Ball("ball1", 5, 3, 0, 1);
		assertTrue(Math.abs(f.timeUntilCollision(ball)-1.0)<0.751);
	}

	@Test
	public void testFlipperTimeUntilCollisionAdjacent()  {
		Flipper f = new Flipper("Flip", 5,5,Flipper.FlipperType.LEFT,
				Angle.ZERO);
		Ball ball = new Ball("Ball", 5, 4, 0, 1);
		assertTrue(Math.abs(f.timeUntilCollision(ball)-0.0)<0.751);	
	}

	@Test
	public void testFlipperTimeUntilCollisionInfinity()  {
		Flipper f = new Flipper("flip", 5, 5,Flipper.FlipperType.LEFT,
				Angle.ZERO);
		Ball ball = new Ball("ball", 6, 4, 1, 1);
		Double infinity = Double.POSITIVE_INFINITY;
		assertTrue(f.timeUntilCollision(ball)== infinity);	
	}

	@Test
	public void testFlipperReactWhenHitFromTop()  {
		Flipper f = new Flipper("flip", 5,5,Flipper.FlipperType.LEFT,
				Angle.ZERO);	
		Ball ball = new Ball("ball", 5, 4, 0, 1);
		try {
			f.reactWhenHit(ball,f.timeUntilCollision(ball));
			assertTrue(ball.getVelocityX()==0.0);
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	@Test
	public void testFlipperReactWhenHitFromBottom()  {
		Flipper f = new Flipper("Flip", 5,5,Flipper.FlipperType.LEFT,
				Angle.ZERO);
		Vect oldVelocity = new Vect(0,-10);
		Ball ball = new Ball("ball", 5, 7, 0, -10);
		try {
			f.reactWhenHit(ball, f.timeUntilCollision(ball));
			assertTrue(ball.getVelocityX()==0);
			assertTrue(ball.getVelocityY()>0);
			assertTrue(Math.abs(ball.getVelocityY()-(-1.0*oldVelocity.y()))<5);
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	//	
	//	///////////////////////////BOUNDARY TESTS///////////////////////
	//	/*
	//	 * Testing Strategy for Boundary:
	//	 * 1.toString
	//	 * 2.timeUntilCollision:Infinity, adjacent, close
	//	 * 3.ReactWhenHit: 45 degrees, 90 degrees
	//	 * 
	//	 * Correct behaviour is determined by physics calculations
	//	 */
	//	
	@Test
	public void testBoundaryToString()  {
		Boundary b = new Boundary(BoundarySide.TOP, new LineSegment(new Vect(0,0), new Vect(20,0))); 
		assertTrue(b.getSymbol().equals("."));
	}

	@Test
	public void testBoundaryTimeUntiCollisionInfinity()  {
		Boundary b = new Boundary(BoundarySide.TOP, new LineSegment(new Vect(0,0), new Vect(20,0)));
		Ball ball = new Ball("ball", 3.0,3.0, 0.25, 0.0);
		Double infinity = Double.POSITIVE_INFINITY;
		assertTrue(b.timeUntilCollision(ball)==infinity);
	}

	@Test
	public void testBoundaryTimeUntilCollisionClose()  {
		Boundary b = new Boundary(BoundarySide.BOTTOM, new LineSegment(new Vect(0,20), new Vect(20,20)));
		Ball ball = new Ball("ball", 6.0,3.0,  0.0, 1.0);
		assertTrue(b.timeUntilCollision(ball)==16.75);
	}

	@Test
	public void testBoundaryTimeUntilCollisionAdjacent()  {
		Boundary b = new Boundary(BoundarySide.TOP, new LineSegment(new Vect(0,0), new Vect(20,0)));
		Ball ball = new Ball("ball", 6.0, 4.0, 0.0,1.0);
		assertTrue(b.timeUntilCollision(ball)== Double.POSITIVE_INFINITY);
	}

	@Test
	public void testBoundaryReactWhenHit90degreesFromTop()  {
		Boundary b = new Boundary(BoundarySide.BOTTOM, new LineSegment(new Vect(0,20), new Vect(20,20)));
		Ball ball = new Ball("ball", 6.0, 3.0, 0.0, 1.0);
		try {
			int initialVelocity = 1;
			int gravity = 25;
			double finalVelocity = initialVelocity+gravity*b.timeUntilCollision(ball);
			b.reactWhenHit(ball, b.timeUntilCollision(ball));
			assertTrue(ball.getVelocityX()==0);
			assertTrue(ball.getVelocityY()<0 && ball.getVelocityY()<Math.abs(finalVelocity));
		} catch (Exception e) {
			System.out.print(e);
		}
	}


	/*
	 * Portal tests: 
	 * Have a ball drop on top of a portal that is directed at another portal on the same board- make sure it comes out
	 * Have a portal that is not connected to anything. Make sure that the ball passes through 
	 */

	@Test
	public void testPortalsBallTeleports() throws InterruptedException {
		List<Ball> balls = new ArrayList<Ball>(Arrays.asList(new Ball("ball1", 5, 5, 0.4, 0.1), new Ball("ball2", 3.0, 3.0, 0.0, 0.0)));
		Portal yummy =  new Portal("YummyBear", 10, 5, "nothing"); 
		Portal gummy = new Portal("GummyBear", 3, 16, "YummyBear"); 
		Board board = new Board("Board", new ArrayList<GameObject>(Arrays.asList(gummy, yummy)), balls,
				new ArrayList<Flipper>(),
				new ArrayList<Portal>(Arrays.asList(gummy, yummy)));

		final int timestepsUntilBallComesOutOfPortals = 50; 
		for( int i= 0; i < timestepsUntilBallComesOutOfPortals; i++){
			board.updateState();
		}

		assertEquals("ball2", balls.get(1).getName());
		assertEquals(10.5, balls.get(1).getCenterX(), .01);
	}
	
	@Test
	public void testPortalsBallsDoesntTeleport() throws InterruptedException {
		List<Ball> balls = new ArrayList<Ball>(Arrays.asList(new Ball("ball1", 5, 5, 0.4, 0.1), new Ball("ball2", 3.0, 3.0, 0.0, 0.0)));
		Portal yummy =  new Portal("YummyBear", 10, 5, "nothing"); 
		Portal gummy = new Portal("GummyBear", 3, 16, "nothing"); 
		Board board = new Board("Board", new ArrayList<GameObject>(Arrays.asList(gummy, yummy)), balls,
				new ArrayList<Flipper>(),
				new ArrayList<Portal>(Arrays.asList(gummy, yummy)));

		final int timestepsUntilBallComesOutOfPortals = 50; 
		for( int i= 0; i < timestepsUntilBallComesOutOfPortals; i++){
			board.updateState();
		}

		//make sure that the ball doesn't disappear 
		assertEquals("ball2", balls.get(1).getName());
		assertEquals(3.0, balls.get(1).getCenterX(), .01);
	}
}

