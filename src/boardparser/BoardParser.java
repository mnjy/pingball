package boardparser;


import implementation.*;
import implementation.Flipper.FlipperType;

import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import keyboard.PingballKeyListener;
import physics.Angle;
import physics.Vect;

/**
 * BoardParser parses files that specify Boards
 * 
 * THREAD SAFETY: BoardParser is thread safe because its only ever accessed by one thread 
 * and it contains no shared mutable data.
 */

public class BoardParser {
    
    /**
     * Map of variables to their default values
     */
    private  Map<String, String> defaultValues;
    
    /**
     * Map of gadget name to the respective gadget objects
     */
    private  Map<String, GameObject>  mapOfGadgets;
    
    /**
     * Map of ball name to ball object 
     */
    private  Map<String, Ball> mapOfBalls;
    
    /**
     * set of names of everything on board
     */
    private  Set<String> setOfNames; 
    
    /**
     * set of names that are disallowed
     */
    private  Set<String> invalidBoardObjectNames;
    
    private final List<Ball> balls;
    private final List<GameObject> nonBallGameObjects; //ALSO includes flippers and portals
    private final List<Flipper> flippers;
    private final List<Portal> portals;
    
    private PingballKeyListener keyListener = new PingballKeyListener();
    
    private final static Map<String,Integer> keyName;
    static {
    Map<String, Integer> map = new HashMap<String, Integer>();
    map.put("a",KeyEvent.VK_A);
    map.put("b", KeyEvent.VK_B);
    map.put("c",KeyEvent.VK_C);
    map.put("d",KeyEvent.VK_D);
    map.put("e", KeyEvent.VK_E);
    map.put("f", KeyEvent.VK_F);
    map.put("g",KeyEvent.VK_G);
    map.put("h", KeyEvent.VK_H);
    map.put("i",KeyEvent.VK_I);
    map.put("j", KeyEvent.VK_J);
    map.put("k",KeyEvent.VK_K);
    map.put("l", KeyEvent.VK_L);
    map.put("m", KeyEvent.VK_M);
    map.put("n", KeyEvent.VK_N);
    map.put("o", KeyEvent.VK_O);
    map.put("p", KeyEvent.VK_P);
    map.put("q", KeyEvent.VK_Q);
    map.put("r", KeyEvent.VK_R);
    map.put("s", KeyEvent.VK_S);
    map.put("t", KeyEvent.VK_T);
    map.put("u", KeyEvent.VK_U);
    map.put("v", KeyEvent.VK_V);
    map.put("w", KeyEvent.VK_W);
    map.put("x", KeyEvent.VK_X);
    map.put("y", KeyEvent.VK_Y);
    map.put("z", KeyEvent.VK_Z);
    map.put("0", KeyEvent.VK_0);
    map.put("1", KeyEvent.VK_1);
    map.put("2", KeyEvent.VK_2);
    map.put("3", KeyEvent.VK_3);
    map.put("4", KeyEvent.VK_4);
    map.put("5", KeyEvent.VK_5);
    map.put("6", KeyEvent.VK_6);
    map.put("7", KeyEvent.VK_7);
    map.put("8", KeyEvent.VK_8);
    map.put("9", KeyEvent.VK_9);
    map.put("shift", KeyEvent.VK_SHIFT);
    map.put("ctrl", KeyEvent.VK_CONTROL);
    map.put("alt", KeyEvent.VK_ALT);
    map.put("meta", KeyEvent.VK_META);
    map.put("space", KeyEvent.VK_SPACE);
    map.put("left", KeyEvent.VK_LEFT);
    map.put("right", KeyEvent.VK_RIGHT);
    map.put("up", KeyEvent.VK_UP);
    map.put("down", KeyEvent.VK_DOWN);
    map.put("minus", KeyEvent.VK_MINUS);
    map.put("equals", KeyEvent.VK_EQUALS);
    map.put("backspace", KeyEvent.VK_BACK_SPACE);
    map.put("openbracket", KeyEvent.VK_OPEN_BRACKET);
    map.put("closebracket", KeyEvent.VK_CLOSE_BRACKET);
    map.put("backslash", KeyEvent.VK_BACK_SLASH);
    map.put("semicolon", KeyEvent.VK_SEMICOLON);
    map.put("quote", KeyEvent.VK_QUOTE);
    map.put("enter", KeyEvent.VK_ENTER);
    map.put("comma", KeyEvent.VK_COMMA);
    map.put("period", KeyEvent.VK_PERIOD);
    map.put("slash", KeyEvent.VK_SLASH);
    keyName = Collections.unmodifiableMap(map);
    }
    
    
    /**
     * Creates an instance of BoardParser 
     */
    public BoardParser(){
        this.defaultValues = makeDefaultValues();
        this.mapOfGadgets = new HashMap<String,GameObject>();
        this.mapOfBalls = new HashMap<String, Ball>();
        this.setOfNames = new HashSet<String>();
        this.invalidBoardObjectNames = invalidBoardObjectNames();
        
        this.balls = new ArrayList<Ball>();
        this.nonBallGameObjects = new ArrayList<GameObject>();
        this.flippers = new ArrayList<Flipper>();
        this.portals = new ArrayList<Portal>();
    }
    
    /**
     * Creates the default values for gravity and friction (mu1 and mu2) and returns then
     * @return defaultValues is a hashMap mapping graph properties, including gravity and frictions
     *          to their repsective values 
     */
    private  Map<String, String> makeDefaultValues(){
        Map<String, String> defaultValues = new HashMap<String, String>();
        defaultValues.put("gravity", "25.0");
        defaultValues.put("mu1", "0.025");
        defaultValues.put("mu2", "0.025");
        return defaultValues;
    }
    
    /**
     * Creates a set of names that are invalid for board objects 
     * @return invalidBoardObjectNames is a set containing words that objects cannot be names 
     */
    private  Set<String> invalidBoardObjectNames(){
        Set<String> dontName = new HashSet<String>();
        dontName.add("squareBumper");
        dontName.add("circleBumper");
        dontName.add("triangleBumper");
        dontName.add("flipper");
        dontName.add("absorber");
        dontName.add("portal");
        dontName.add("ball");
        dontName.add("board");
        return dontName;
    }
    
    /**
     * Creates and returns a Board object based on the specification
     * in file f
     * @param file a file containing Board specification
     * @return a Board corresponding to the specification in file
     * @throws IOException if the file is not found
     */
    public Board getBoardFromFile(File file) throws IOException {
        String outString = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while((line = reader.readLine()) != null){
            outString+=line;
            outString+="\n";
        }
        reader.close();
        Board board = getBoardFromString(outString);
        return board;
    }
    
    /**
     * Creates and returns a Board Object from the filename
     * @param fileName String representing the file name
     * @return Board as specified in the file
     * @throws IOException if file is not found
     */
    public Board getBoardFromFileName(String fileName) throws IOException {
        String outString = "";
        String filePath = getResourcePath(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while((line = reader.readLine()) != null){
            outString+=line;
            outString+="\n";
        }
        Board board = getBoardFromString(outString);
        return board;
    }
    
    /**
     * Return the absolute path of the specified file resource on the classpath.
     * @throws IOException if a valid path to an existing file cannot be returned
     */
    public static String getResourcePath(String fileName) throws IOException {
        URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
        if (url == null) {
            //This might be an absolute filename 
            File file = new File(fileName); 
            if (file.isFile()){
                return file.getAbsolutePath();
            }
          else{
              throw new IOException("Failed to locate resource " + fileName);
          }
        }
        File file;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException urise) {
            throw new IOException("Invalid URL: " + urise);
        }

        String path = file.getAbsolutePath();
        if ( ! file.exists()) {
            throw new IOException("File " + path + " does not exist");
        }
        return path;
    }

    
    /**
     * Creates and returns a Board object based on the specification
     * in string boardString
     * @param boardString   a string containing Board specification
     * @return  a Board corresponding to the specification in str
     */
    protected Board getBoardFromString(String boardString) throws IOException {

        Pattern Board = Pattern.compile("^board");
        Pattern Ball = Pattern.compile("^ball");
        Pattern Gadget = Pattern.compile("^squareBumper|^circleBumper|^triangleBumper"
                                        + "|^rightFlipper|^leftFlipper|^absorber");
        Pattern Portal = Pattern.compile("^portal");
        Pattern Trigger = Pattern.compile("^fire"); 
        Pattern Comment = Pattern.compile("^#");
        Pattern keyEvent = Pattern.compile("^keyup|^keydown");
        
        String[] boardStringArray = processString(boardString);
        
        List<Float> boardProps = new ArrayList<>();
        String boardName = "";
        Board board = new Board("",new ArrayList<GameObject>(), new ArrayList<Ball>(), new ArrayList<Flipper>(), new ArrayList<Portal>());
        boolean changedBoard = false;
        int uncommentedLineNumber = 0;
        for (String line: boardStringArray){
             Matcher boardMatch = Board.matcher(line);
             Matcher gadgetMatch = Gadget.matcher(line);
             Matcher portalMatch = Portal.matcher(line);
             Matcher ballMatch = Ball.matcher(line);
             Matcher triggerMatch = Trigger.matcher(line);
             Matcher commentMatch = Comment.matcher(line);
             Matcher keyMatch = keyEvent.matcher(line);
             
             uncommentedLineNumber ++;
             if(boardMatch.find()){
                 if(uncommentedLineNumber>1 | changedBoard == true){
                     throw new IllegalArgumentException("no board description");
                 }
                 else{
                     boardName = findString(line, "name ?= ?");
                     boardProps = parseBoard(line);
                     changedBoard = true;
                 }
             }
             else if(gadgetMatch.find()){                
                 if(changedBoard){
                     parseGadget(line);
                 }
             }
             else if(portalMatch.find()){
                 if (changedBoard){
                     parsePortal(line, board);
                 }
             }
             else if(ballMatch.find()){
                 if(changedBoard){
                     parseBall(line);
                 }
             }
             else if(triggerMatch.find()){
                 if(changedBoard){                     
                     parseTrigger(line);
                 }
             }
             else if (keyMatch.find()){
                 if(changedBoard){
                     parseKey(line);
                 }
             }
             else if (commentMatch.find()){
                 uncommentedLineNumber --;
             }
        }
        if (changedBoard == false){
            throw new IllegalArgumentException("no board description");
        }
        Float gravity = boardProps.get(0);
        Float mu1 = boardProps.get(1);
        Float mu2 = boardProps.get(2);
        
        board = new Board(boardName, nonBallGameObjects, balls, flippers, portals);
        board.setGravity(gravity);
        board.setMu(mu1);
        board.setMu2(mu2);
        board.addKeyListener(keyListener);
        return board;
    }

    /**
     * Helper function that tokenises a string so it is ready for use 
     * @param boardString to be tokenised 
     * @return boardStringArray is an array that stores individual words from boardString
     */
    protected  String[] processString(String boardString){
        String[] boardStringArray = boardString.split("\\n");
        
        for (int i = 0; i<boardStringArray.length; i++){
            boardStringArray[i] = boardStringArray[i].replaceAll("\\s+", " ").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
        }
        return boardStringArray;
    }
    
    /**
     * Creates and returns a Gadget object from a string representing a gadget
     * @param gadgetString String containing information about the gadget 
     *          (in the form of the descriptions of gadgets in the sample board files)
     * @return a Gadget that corresponds to the string
     */
    protected  void parseGadget(String gadgetString){
        
        Pattern square = Pattern.compile("^squareBumper");
        Pattern circle = Pattern.compile("^circleBumper");
        Pattern triangle = Pattern.compile("^triangleBumper");
        Pattern rightFlipper = Pattern.compile("^rightFlipper");
        Pattern leftFlipper = Pattern.compile("^leftFlipper");
        Pattern absorber = Pattern.compile("^absorber");
        
        Matcher squareMatch = square.matcher(gadgetString);
        Matcher circleMatch = circle.matcher(gadgetString);
        Matcher triangleMatch = triangle.matcher(gadgetString);
        Matcher rightFlipperMatch = rightFlipper.matcher(gadgetString);
        Matcher leftFlipperMatch = leftFlipper.matcher(gadgetString);
        Matcher absorberMatch = absorber.matcher(gadgetString);
        
        String name = findString(gadgetString, "name ?= ?"); 
        int x = findInt(gadgetString, "x ?= ?");
        int y = findInt(gadgetString, "y ?= ?");
        if (checkName(name)){
            if (squareMatch.find()){
                SquareBumper newGameObject = new SquareBumper(name, x, y);
                mapOfGadgets.put(name, newGameObject);
                setOfNames.add(name);
                nonBallGameObjects.add(newGameObject);
            }
            else if (circleMatch.find()){
                CircleBumper newGameObject = new CircleBumper(name, x, y);
                mapOfGadgets.put(name, new CircleBumper(name, x, y));
                setOfNames.add(name);
                nonBallGameObjects.add(newGameObject);
            }
            else if(triangleMatch.find()){
                int orientation = findInt(gadgetString, "orientation ?= ?");
                Angle angle = findOrientation(orientation);
                TriangleBumper newGameObject = new TriangleBumper(name,x,y,angle);
                mapOfGadgets.put(name, newGameObject);
                setOfNames.add(name);
                nonBallGameObjects.add(newGameObject);
            }
            else if(rightFlipperMatch.find()){
                int orientation = findInt(gadgetString, "orientation ?= ?");;
                Angle angle = findOrientation(orientation);
                Flipper newGameObject = new Flipper(name,x,y, FlipperType.RIGHT, angle);
                mapOfGadgets.put(name, newGameObject);
                setOfNames.add(name);
                nonBallGameObjects.add(newGameObject);
                flippers.add(newGameObject);
            }
            else if(leftFlipperMatch.find()){
                int orientation = findInt(gadgetString, "orientation ?= ?");
                Angle angle = findOrientation(orientation);
                Flipper newGameObject = new Flipper(name, x, y,FlipperType.LEFT, angle);
                mapOfGadgets.put(name, newGameObject);
                setOfNames.add(name);
                nonBallGameObjects.add(newGameObject);
                flippers.add(newGameObject);
            }
            else if(absorberMatch.find()){
                int width = findInt(gadgetString, "width ?= ?");
                int height = findInt(gadgetString, "height ?= ?");
                Absorber newGameObject = new Absorber(name, x, y, width, height);
                mapOfGadgets.put(name,newGameObject);
                setOfNames.add(name);
                nonBallGameObjects.add(newGameObject);
            }
        }
        else{
            throw new IllegalArgumentException("Game Object Improperly Formatted");
        }
    }
    
    /**
     * Checks to see whether name already exists or is an invalid name option
     * @param name is a string that the function checks is valid or not 
     * @return true if name is valid, false if name is invalid (already exists or is invalid)
     */
    protected  boolean checkName(String name){
        if (setOfNames.contains(name)|| invalidBoardObjectNames.contains(name)){
            throw new IllegalArgumentException("Invalid Name");
        }
        else{
            return true;
        }
    }
    
    /**
     * Adds Portal object to a board form a string representing a portal
     * @param portalString String containing information about the portal. 
     *          (in the format of the descriptions of the portals in sample board files)
     * @param board that portal should be added to
     * @return Portal object with the specifications described in the portalString
     */
    protected  Portal parsePortal(String portalString, Board board){
        String name = findString(portalString, "name ?= ?");
        if (checkName(name)){
            int x = findInt(portalString, "x ?= ?");
            int y = findInt(portalString, "y ?= ?");
            String otherBoard = findString(portalString, "otherBoard ?= ?");
            String otherPortal = findString(portalString, "otherPortal ?= ?");
            Portal portal = new Portal(name, x, y, otherBoard, otherPortal);
            setOfNames.add(name);
            nonBallGameObjects.add(portal);
            portals.add(portal);
            return portal;
        }
        else{
            throw new IllegalArgumentException("Invalid Portal Description Format");
        }
    }
    
    /**
     * Parses Key Command and adds it to the key listener 
     * @param itemString a string that contains information regarding the key command
     */
    protected void parseKey(String itemString){
        Pattern keydown = Pattern.compile("^keydown");
        Pattern keyup = Pattern.compile("^keyup");
        
        Matcher keydownMatch = keydown.matcher(itemString);
        Matcher keyupMatch = keyup.matcher(itemString);
        
        String key = findKeyname(itemString, "key ?= ?");
        String action = findString(itemString, "action ?= ?");
        GameObject triggeredGadget = mapOfGadgets.get(action);
        int keyCode = keyName.get(key);
        if (keydownMatch.find()){
            keyListener.addCommand(triggeredGadget, keyCode, PingballKeyListener.KeyEnum.KEYDOWN);
        }
        else if (keyupMatch.find()){
            keyListener.addCommand(triggeredGadget, keyCode, PingballKeyListener.KeyEnum.KEYUP);
        }
        
    }
    
    /**
     * Given the itemString and regex, finds the keyname property 
     * @param itemString is a string from which we want to find the keyname
     * @param regex is a string that specifies the property we are looking for 
     * @return a string that is value of the property
     */
    protected  String findKeyname(String itemString, String regex){
        Pattern nameSignal = Pattern.compile(regex);
        Matcher nameSignalMatch = nameSignal.matcher(itemString);
        Pattern Name = Pattern.compile("(^ ?+)([A-Za-z_0-9]+)( |\n|$)");
        while (nameSignalMatch.find()){
            int start = nameSignalMatch.end();
            String findName = itemString.substring(start);
            Matcher nameMatch = Name.matcher(findName);
            if (nameMatch.find()){
                String foundName = nameMatch.group(2);
                return foundName;
            }
            else{
                return "";
            }
        }
        return "";
    }
    
    /**
     * Parses trigger and adds it to list of triggered gadgets if both trigger and trigee exist
     * @param itemString a string that contains information regarding gadget trigger and action
     */
    protected  void parseTrigger(String itemString){

        String trigger = findString(itemString, "trigger ?= ?");
        String action = findString(itemString, "action ?= ?");
        GameObject triggerGadget = null;
        GameObject actionGadget = null;
        
        for (GameObject gadget : nonBallGameObjects){
            if (gadget.getName().equals(trigger)){
                triggerGadget = gadget;
            }
            if (gadget.getName().equals(action)){
                actionGadget = gadget;
            }
        }
        triggerGadget.setTrigger(actionGadget);
    }
     
    /**
     * Parses a string representation of a board and instantiates it 
     * @param boardString is a string representation of a board 
     * @return List of board properties [gravity, friction1, friction2] 
     */

    protected  List<Float> parseBoard(String boardString){
        Float gravity = findFloat(boardString, "gravity ?= ?", "gravity");
        Float mu1 = findFloat(boardString, "friction1 ?= ?", "mu1");
        Float mu2 = findFloat(boardString, "friction2 ?= ?", "mu2");
        return new ArrayList<Float>(Arrays.asList(gravity, mu1, mu2));
    }
    
    /**
     * Creates and returns a Ball object from a string representing a ball
     * @param ballString String containing information about the ball
     * @return Ball object with the specifications described in the ballString
     */
    protected  void parseBall(String ballString){
        String name = findString(ballString, "name ?= ?");
        if (checkName(name)){
            Float x = findFloat(ballString, "x ?= ?","other");
            Float y = findFloat(ballString, "y ?= ?", "other");
            Float xVelocity = findFloat(ballString, "xVelocity ?= ?", "other");
            Float yVelocity = findFloat(ballString, "yVelocity ?= ?", "other");
            Ball newBall = new Ball(name, x, y, xVelocity, yVelocity);
            mapOfBalls.put(name, newBall);
            setOfNames.add(name);
            balls.add(newBall);
        }
        else{
            throw new IllegalArgumentException("Ball improperly formatted");
        }
    }
      
    /**
     * Given the itemString and regex, finds the integer property 
     * @param itemString is a string from which we want to find the integer 
     * @param regex is a string that specifies the property we are looking for 
     * @return an integer that is value of the property
     */
    protected  int findInt(String itemString, String regex){
        Pattern INTEGER = Pattern.compile("(^ ?+)([0-9]+)( |\n|$)");
        Pattern intSignal = Pattern.compile(regex);
        Matcher intSignalMatch = intSignal.matcher(itemString);
        while (intSignalMatch.find()){
            int start = intSignalMatch.end();
            String findInt = itemString.substring(start);
            Matcher intMatch = INTEGER.matcher(findInt);
            if (intMatch.find()){
                String foundInt = intMatch.group(2);
                return Integer.parseInt(foundInt);
            }
            else{
                throw new IllegalArgumentException("Improperly Formatted - Missing Integer"); 
            }
        }
        throw new IllegalArgumentException("Improperly Formatted - Missing Integer");
    }
        
    /**
     * Converts integers into Angle objects
     * @param angleInt integer representing angle
     * @return Angle object representing orientation
     * @throws IllegalArgumentException
     */
    protected Angle findOrientation(int angleInt) throws IllegalArgumentException{
        List<Integer> accpetedOrientation = new ArrayList<Integer>(Arrays.asList(90,180,270,0));
        if (accpetedOrientation.contains(angleInt)){
            if (angleInt == 0){
                return Angle.ZERO;
            }
            else if (angleInt == 90){
                return Angle.DEG_90;
            }
            else if (angleInt == 180){
                return Angle.DEG_180;
            }
            else{
                return Angle.DEG_270;
            }
        }
        else{throw new IllegalArgumentException();}
    }
  
    /**
     * Given the itemString and regex, finds the string property 
     * @param itemString is a string from which we want to find the string
     * @param regex is a string that specifies the property we are looking for 
     * @return a string that is value of the property
     */
    protected  String findString(String itemString, String regex){
        Pattern nameSignal = Pattern.compile(regex);
        Matcher nameSignalMatch = nameSignal.matcher(itemString);
        Pattern Name = Pattern.compile("(^ ?+)([A-Za-z_][A-Za-z_0-9]*)( |\n|$)");
        while (nameSignalMatch.find()){
            int start = nameSignalMatch.end();
            String findName = itemString.substring(start);
            Matcher nameMatch = Name.matcher(findName);
            if (nameMatch.find()){
                String foundName = nameMatch.group(2);
                return foundName;
            }
            else{
                return "";
            }
        }
        return "";
    }
  
    /**
     * Given the itemString and regex, finds the float property 
     * @param itemString is a string from which we want to find the float
     * @param regex is a string that specifies the property we are looking for 
     * @return a Float that is value of the property
     */
    protected  float findFloat(String itemString, String regex, String type){
        Pattern FLOAT = Pattern.compile("(^ ?+)(-?([0-9]+.[0-9]*|.?[0-9]+))( |\n|$)");
        Pattern floatSignal = Pattern.compile(regex);
//      Pattern ySignal = Pattern.compile("y ?= ?");
        Matcher floatSignalMatch = floatSignal.matcher(itemString);
        while (floatSignalMatch.find()){
            int start = floatSignalMatch.end();
            String findFloat = itemString.substring(start);
            Matcher floatMatch = FLOAT.matcher(findFloat);
            if (floatMatch.find()){
                String foundFloat = floatMatch.group(2);
                return Float.parseFloat(foundFloat);
            }
            else{
                throw new IllegalArgumentException("Improperly Formatted - Missing Float"); 
            }
        }
        if (type.equals("gravity")){
            return Float.parseFloat(defaultValues.get("gravity"));
        }
        else if (type.equals("mu1")){
            return Float.parseFloat(defaultValues.get(type));
        }
        else if(type.equals("mu2")){
            return Float.parseFloat(defaultValues.get("mu2"));
        }
        else{
            throw new IllegalArgumentException("Improperly Formatted - Missing Float"); 
        }
    }
    
    

    /**
     * Returns mapOfBalls
     * @return mapOfBalls is a map of ball names to their Ball
     */
    protected  Map<String, Ball> getMapOfBalls(){
        return mapOfBalls;
    }
    

    /**
     * An ImproperlyFormattedFileException is an unchecked exception thrown
     * when a file or string given to BoardParser does not match the proper
     * grammar for specifying a Board.
     */
    protected class ImproperlyFormattedFileException extends RuntimeException {
        private  final long serialVersionUID = 1L;

        /**
         * Create an ImproperlyFormattedFileException with a message
         */
        public ImproperlyFormattedFileException(String message) {
            super(message);
        }

        /**
         * Create an ImproperlyFormattedFileException with a message and a cause
         */
        public ImproperlyFormattedFileException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}











