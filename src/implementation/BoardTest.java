package implementation;

import static org.junit.Assert.*;
import implementation.Flipper.FlipperType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import physics.Angle;
import physics.Vect;

public class BoardTest {
	
	/*
	 * Testing Strategy:
	 * 1. toString: a.first test printing a small board with no gadgets 
	 * 				b.Print normal sized board (22X22) with a few gadgets
	 * 2. populatePointToObject: a. test with empty PointToObject
	 * 							 b. test one key mapping to one object
	 * 							 c. test multiple keys mapping to multiple objects
	 */

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@Test
	public void testToStringNormalSizedBoard() {
		List<Ball> balls = new ArrayList<Ball>(Arrays.asList(new Ball("ball1", 5, 5, 0.0, 0.0)));
		List<Flipper> flippers = new ArrayList<Flipper>();
	    List<Portal> portal = new ArrayList<Portal>();

		List<GameObject> nonBallGameObject = new ArrayList<>(); 
		SquareBumper square = new SquareBumper("squreBumper1", 3,3);
		nonBallGameObject.add(square); 

		Board b = new Board("board",  nonBallGameObject, balls, flippers, portal);
		String expectedBoard = 
				"......................\n"
				+".                    .\n"
				+".                    .\n"
				+".                    .\n"
				+".   #                .\n"
				+".                    .\n"
				+".     *              .\n"
				+".                    .\n"
				+".                    .\n"
				+".                    .\n"
				+".                    .\n"
				+".                    .\n"
				+".                    .\n"
				+".                    .\n"
				+".                    .\n"
				+".                    .\n"
				+".                    .\n"
				+".                    .\n"
				+".                    .\n"
				+".                    .\n"
				+".                    .\n"
				+"......................"; 

		assertEquals(expectedBoard, b.toString());
	}
	

    @Test
    public void testToPrintBoardHasMultipleGadgetsBallBumperAbsorberFlipper() {
        List<GameObject> gadgets = new ArrayList<GameObject>();
        List<Ball> balls= new ArrayList<Ball>();
        List<Flipper> flippers= new ArrayList<Flipper>();
        List<Portal> portals= new ArrayList<Portal>();
        gadgets.add(new SquareBumper("hello",3, 4));
        balls.add(new Ball("hello", 5, 6, -1.9, 4.3));
        balls.add(new Ball("hello", 1, 2, 2.1, 3.2));
        gadgets.add(new CircleBumper("hello", 13, 3));
        gadgets.add(new TriangleBumper("hello", 7, 17, Angle.ZERO));
        // gadgets.add(new SquareBumper(7, 17));
        gadgets.add(new Flipper("hello", 13, 14, FlipperType.LEFT, Angle.ZERO));
        gadgets.add(new Absorber("hello", 0, 19, 20, 1));

        Board theBoard = new Board("board1", gadgets, balls, flippers, portals);

        String answer = "......................\n" + ".                    .\n"
                + ".                    .\n" + ". *                  .\n"
                + ".             O      .\n" + ".   #                .\n"
                + ".                    .\n" + ".     *              .\n"
                + ".                    .\n" + ".                    .\n"
                + ".                    .\n" + ".                    .\n"
                + ".                    .\n" + ".                    .\n"
                + ".                    .\n" + ".             |      .\n"
                + ".             |      .\n" + ".                    .\n"
                + ".       /            .\n" + ".                    .\n"
                + ".====================.\n" + "......................";
        assertEquals(theBoard.makeTextModeDisplay(), answer);
    }

    @Test
    public void testToPrintBoardHasMultipleFlipperTriangleBumperAlternateOrientations() {
        List<GameObject> gadgets = new ArrayList<GameObject>();
        List<Ball> balls= new ArrayList<Ball>();
        List<Flipper> flippers= new ArrayList<Flipper>();
        List<Portal> portals= new ArrayList<Portal>();
        gadgets.add(new SquareBumper("hello", 3, 4));
        balls.add(new Ball("hello", 5, 6, -1.9, 4.3));
        balls.add(new Ball("hello", 1, 2, 2, 1));
        gadgets.add(new CircleBumper("hello", 13, 3));
        gadgets.add(new TriangleBumper("hello", 7, 17, Angle.DEG_90));
        // gadgets.add(new SquareBumper(7, 17));
        flippers.add(new Flipper("hello", 13, 14, FlipperType.LEFT, Angle.DEG_90)); // horizontal?
        gadgets.add(new Flipper("hello", 13, 14, FlipperType.LEFT, Angle.DEG_90)); // horizontal?
        gadgets.add(new Absorber("hello", 0, 19, 20, 1));

        Board theBoard = new Board("board1", gadgets, balls, flippers, portals );

        String answer = "......................\n" + ".                    .\n"
                + ".                    .\n" + ". *                  .\n"
                + ".             O      .\n" + ".   #                .\n"
                + ".                    .\n" + ".     *              .\n"
                + ".                    .\n" + ".                    .\n"
                + ".                    .\n" + ".                    .\n"
                + ".                    .\n" + ".                    .\n"
                + ".                    .\n" + ".             --     .\n"
                + ".                    .\n" + ".                    .\n"
                + ".       \\            .\n" + ".                    .\n"
                + ".====================.\n" + "......................";

        assertEquals(theBoard.makeTextModeDisplay(), answer);
    }

}
