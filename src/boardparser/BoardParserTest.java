package boardparser;

import static org.junit.Assert.*;
import implementation.Ball;
import implementation.Board;
import implementation.GameObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import physics.Vect;

public class BoardParserTest {
    /**
     * Testing Strategy
     *      - processString
     *          - 0, 1, >1 multiple new lines
     *          - odd/even number of spaces
     *      - findInt 
     *          - x ?= ?
     *          - with and without space between x and =
     *          - without and without space after = 
     *          - with and without word after int
     *          - digits and letters and combination
     *          - 0, 1, <1 digit
     *      - findString
     *          - name ?= ?
     *          - with and without space between name and = 
     *          - with and without letters after expected name
     *          - digits and letters and combination
     *      - findFloat
     *          - type = gravity, mu1, mu2, other
     *          - with and without space between float and =
     *          - 'float' is double or int
     *          - digits and letters
     *      - findOrientiation
     *          - 90, 180, 270, 390
     *          - other than expected number
     *          - non integer
     *          - extra spaces between space and expected orientation
     *      - parseBall
     *      - parseBoard
     *          - with/without gravity
     *          - with/without friction1
     *          - with/without friction2
     *          - not first uncommented line
     *          - 0, 1, >1 board line
     *      - parseTrigger
     *          - self trigger, other trigger
     *          - other trigger (portals): same board, different board
     *          - portal not commutative trigger
     *      - parsePortal
     *          - portal going to different board, same board
     *      - addGadget
     *          - no gadget, 1, >1 gadget
     *          - square, circle, triangle, absorber, left flipper, right flipper
     *      - addPortal
     *          - 0, 1, >1 portals
     *      - addBall
     *          - 0, 1, >1 balls
     *      - processString
     *          - multiple space characters before line, after line, between words
     *          - 0 lines, 1 line, >1 line
     *      - getBoardFromFile
     *          - file exists, file does not exist
     *      - checkName
     *          - repeated/non-repeated names
     *          - invalid names
     * 
     */

    @Test
    public void testFindIntNormal() {
        BoardParser boardParser = new BoardParser();
        String testString = "x=0";
        String regex = "x ?= ?";
        int expected = 0;
        int actual = boardParser.findInt(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindIntSurroudingWords() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x=0 y=2\n";
        String regex = "x ?= ?";
        int expected = 0;
        int actual = boardParser.findInt(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindIntSpaceAfterEquals() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x= 0 y=2\n";
        String regex = "x ?= ?";
        int expected = 0;
        int actual = boardParser.findInt(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindIntSpaceBeforeSpace() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x =0 y=2\n";
        String regex = "x ?= ?";
        int expected = 0;
        int actual = boardParser.findInt(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindIntSpaceBothBeforeAndAfterSpace() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x = 0 y=2\n";
        String regex = "x ?= ?";
        int expected = 0;
        int actual = boardParser.findInt(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFindIntLetters() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x = 0l y=2\n";
        String regex = "x ?= ?";
        int expected = -1;
        int actual = boardParser.findInt(testString, regex);
//        assertEquals(expected,actual);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFindIntNoDigit() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x =  y=2\n";
        String regex = "x ?= ?";
        int actual = boardParser.findInt(testString, regex);
    }
    
    @Test
    public void testFindIntMultipleDigit() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x = 332 y=2\n";
        String regex = "x ?= ?";
        int expected = 332;
        int actual = boardParser.findInt(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindIntFollowedByNewLine() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x = 332 y=2\n";
        String regex = "y ?= ?";
        int expected = 2;
        int actual = boardParser.findInt(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindStringNormal() {
        BoardParser boardParser = new BoardParser();
        String testString = "name=soap";
        String regex = "name ?= ?";
        String expected = "soap";
        String actual = boardParser.findString(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindStringSurroudingWords() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x=0 y=2\n";
        String regex = "name ?= ?";
        String expected = "Square";
        String actual = boardParser.findString(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindStringSpaceAfterEquals() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x= 0 y=2\n";
        String regex = "name ?= ?";
        String expected = "Square";
        String actual = boardParser.findString(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindStringSpaceBeforeSpace() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x =0 y=2\n";
        String regex = "name ?= ?";
        String expected = "Square";
        String actual = boardParser.findString(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindStringSpaceBothBeforeAndAfterSpace() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x = 0 y=2\n";
        String regex = "name ?= ?";
        String expected = "Square";
        String actual = boardParser.findString(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindStringNumbers() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Sq9uare x = 0l y=2\n";
        String regex = "name ?= ?";
        String expected = "Sq9uare";
        String actual = boardParser.findString(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindStringSingleLetter() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=a x = 332 y=2\n";
        String regex = "name ?= ?";
        String expected = "a";
        String actual = boardParser.findString(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindStringFollowedByNewLine() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square\n";
        String regex = "name ?= ?";
        String expected = "Square";
        String actual = boardParser.findString(testString, regex);
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindFloatNormalIntMu1() {
        BoardParser boardParser = new BoardParser();
        String testString = "mu1=0";
        String regex = "mu1 ?= ?";
        Float expected = (float) 0.;
        Float actual = boardParser.findFloat(testString, regex, "mu1");
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindFloatSurroudingWordsMu2Decimal() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x=0 mu2=2.3\n";
        String regex = "mu2 ?= ?";
        Float expected = (float)2.3;
        Float actual = boardParser.findFloat(testString, regex, "mu2");
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindFloatSpaceAfterEqualsGravityNoIntBeforeDecimal() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x= 0 gravity=.4\n";
        String regex = "gravity ?= ?";
        Float expected = (float).4;
        Float actual = boardParser.findFloat(testString, regex, "gravity");
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindFloatSpaceBeforeSpace() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x =3.2 y=2\n";
        String regex = "x ?= ?";
        Float expected = (float) 3.2;
        Float actual = boardParser.findFloat(testString, regex, "none");
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindFloatSpaceBothBeforeAndAfterSpace() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x = 3.2 y=2\n";
        String regex = "x ?= ?";
        Float expected = (float) 3.2;
        Float actual = boardParser.findFloat(testString, regex, "none");
        assertEquals(expected,actual);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFindFloatLetters() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x = 0l y=2\n";
        String regex = "x ?= ?";
        Float actual = boardParser.findFloat(testString, regex, "none");
       
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFindFloatNoDigit() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x =  y=2\n";
        String regex = "x ?= ?";
        Float actual = boardParser.findFloat(testString, regex, "none");
        
    }
    
    @Test
    public void testFindFloatMultipleDigit() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x = 332.8 y=2\n";
        String regex = "x ?= ?";
        Float expected = (float) 332.8;
        Float actual = boardParser.findFloat(testString, regex, "none");
        assertEquals(expected,actual);
    }
    
    @Test
    public void testFindFloatFollowedByNewLine() {
        BoardParser boardParser = new BoardParser();
        String testString = "squareBumper name=Square x = 332 y=2\n";
        String regex = "y ?= ?";
        Float expected = (float) 2;
        Float actual = boardParser.findFloat(testString, regex, "none");
        assertEquals(expected,actual);
    }

    
    
    
    @Test (expected = IllegalArgumentException.class)
    public void testParseBoardNotFirstUncommentedLine(){
        BoardParser boardParser = new BoardParser();
        String testString = "ball name=BallA x=10.25 y=15.25 xVelocity=0 yVelocity=0\n"
                + "board name=Absorber gravity = 25.0\n"
                + "# define a ball\n"
                + "ball name=BallA x=10.25 y=15.25 xVelocity=0 yVelocity=0\n"
                + "ball name=BallB x=19.25 y=3.25 xVelocity=0 yVelocity=0\n"
                + "ball name=BallC x=1.25 y=5.25 xVelocity=0 yVelocity=0\n"
                + "\n";
        try {
            Board testBoard = boardParser.getBoardFromString(testString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testParseBoardNoBoardLine(){
        BoardParser boardParser = new BoardParser();
        String testString = "ball name=BallA x=10.25 y=15.25 xVelocity=0 yVelocity=0\n"
                + "# define a ball\n"
                + "ball name=BallA x=10.25 y=15.25 xVelocity=0 yVelocity=0\n"
                + "ball name=BallB x=19.25 y=3.25 xVelocity=0 yVelocity=0\n"
                + "ball name=BallC x=1.25 y=5.25 xVelocity=0 yVelocity=0\n"
                + "\n";
        try {
            Board testBoard = boardParser.getBoardFromString(testString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testParseBoardMultipleBoardLines(){
        BoardParser boardParser = new BoardParser();
        String testString = "ball name=BallA x=10.25 y=15.25 xVelocity=0 yVelocity=0\n"
                + "board name=Absorber gravity = 25.0\n"
                + "# define a ball\n"
                + "board name=poop gravity = 25.0\n"
                + "ball name=BallB x=19.25 y=3.25 xVelocity=0 yVelocity=0\n"
                + "ball name=BallC x=1.25 y=5.25 xVelocity=0 yVelocity=0\n"
                + "\n";
        try {
            Board testBoard = boardParser.getBoardFromString(testString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testProcessStringOneLine(){
        BoardParser boardParser = new BoardParser();
        String testString = "board name=Absorber gravity = 25.0\n";
        String expectedString = "board name=Absorber gravity = 25.0";
        String[] stringArray = boardParser.processString(testString);
        assertEquals(1, stringArray.length);
        assertTrue(stringArray[0].equals(expectedString));
    }
    
    @Test
    public void testProcessStringEmpty(){
        BoardParser boardParser = new BoardParser();
        String testString = "";
        String expectedString = "";
        String[] stringArray = boardParser.processString(testString);
        assertEquals(1, stringArray.length);
        assertTrue(stringArray[0].equals(expectedString));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testCheckNameSameNameSameGadget(){
        BoardParser boardParser = new BoardParser();
        String testBoard = "board name=Default\n"
                + "# define a ball\n"
                + "ball name=BallA x=1.25 y=1.25 xVelocity=0 yVelocity=0\n"
                + "\n"
                + "# define a series of square bumpers\n"
                + "squareBumper name=SquareA x=0 y=17\n"
                + "squareBumper name=SquareA x=1 y=17\n"
                + "squareBumper name=SquareC x=2 y=17\n"
                + "\n";
        try {
            Board board = boardParser.getBoardFromString(testBoard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testCheckNameSameNameDifferentGadget(){
        BoardParser boardParser = new BoardParser();
        String testBoard = "board name=Default\n"
                + "# define a ball\n"
                + "ball name=BallA x=1.25 y=1.25 xVelocity=0 yVelocity=0\n"
                + "\n"
                + "# define a series of square bumpers\n"
                + "squareBumper name=SquareA x=0 y=17\n"
                + "circleBumper name=SquareA x=1 y=17\n"
                + "squareBumper name=SquareC x=2 y=17\n"
                + "\n";
        try {
            Board board = boardParser.getBoardFromString(testBoard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean doubleEquals(Double double1, Double double2){
        double buffer = 0.00001;
        if (Math.abs(double1-double2)<buffer){
            return true;
        }
        else{
            return false;
        }
    }
    

}
