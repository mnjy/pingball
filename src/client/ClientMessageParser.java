package client;

import server.BoardNeighbor;
import boardparser.IncorrectFormatException;
import implementation.Ball;
import implementation.Board.BoundarySide;

/**
 * Parses messages received by the client
 * MESSAGE PROTOCOL:
 * It accepts the following messages from the server:
 *      Incoming Ball ::= ball name=NAME x=DOUBLE y=DOUBLE xVelocity=DOUBLE yVelocity=DOUBLE
 *      Make Wall Invisible ::= makeinvisiblewall UP|DOWN|LEFT|RIGHT NAME
 *      Make Wall Visible ::= makevisiblewall UP|DOWN|LEFT|RIGHT
 *      Disconnect Client ::= DISCONNECT
 * Where:
 *      NAME ::= [A-Za-z_][A-Za-z_0-9]*
 *      DOUBLE ::= [-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?
 */
public class ClientMessageParser {
    //No AF/RI
    //All fields are final and immutable. Thread-safe by immutability.

    private final static String NAME = "[A-Za-z_][A-Za-z_0-9]*";
    private final static String DOUBLE = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
    private final static String INCOMING_BALL_FORMAT = "ball name="+NAME+" x="+DOUBLE+" y="+DOUBLE+" xVelocity="+DOUBLE+" yVelocity="+DOUBLE;
    /*ball name=NAME x=FLOAT y=FLOAT xVelocity=FLOAT yVelocity=FLOAT*/
    private final static String WALL_INVISIBILITY_FORMAT = "makeinvisiblewall (UP|DOWN|RIGHT|LEFT) "+NAME;
    /*makeinvisiblewall (UP|DOWN|LEFT|RIGHT) NAME*/
    private final static String WALL_VISIBILITY_FORMAT = "makevisiblewall (UP|DOWN|RIGHT|LEFT)";
    /*makevisiblewall (UP|DOWN|RIGHT|LEFT)*/
    private final static String DISCONNECT_CLIENT_FORMAT = "DISCONNECT";
    /*DISCONNECT */
   
    /**
     * Enumerates the types of messages the client can receive
     */
    public enum ClientMessageType {
        INCOMING_BALL, WALL_INVISIBILITY, WALL_VISIBILITY, DISCONNECT_CLIENT;
    }
    
    /**
     * Given a message from a server, returns the type of message
     * @param input the message from the server
     * @return the type of message
     * @throws IncorrectFormatException if message does not follow the message protocol
     */
    public static ClientMessageType parse(String input) throws IncorrectFormatException {
        if (input.matches(INCOMING_BALL_FORMAT)){return ClientMessageType.INCOMING_BALL;}
        else if (input.matches(WALL_INVISIBILITY_FORMAT)){return ClientMessageType.WALL_INVISIBILITY;}
        else if (input.matches(WALL_VISIBILITY_FORMAT)){return ClientMessageType.WALL_VISIBILITY;}
        else if (input.matches(DISCONNECT_CLIENT_FORMAT)){return ClientMessageType.DISCONNECT_CLIENT;}
        else {throw new IncorrectFormatException();}
    }
    
    /**
     * Parses input to return a ball at the appropriate location and velocity.
     * @param input ball message coming from the server
     * @return a ball object
     * @throws IncorrectFormatException if input does not match message protocol
     */
    public static Ball parseIncomingBall(String input) throws IncorrectFormatException {
        String name = "";
        double x = 0;
        double y = 0;
        double xVelocity = 0;
        double yVelocity = 0;
        
        if ( ! input.matches(INCOMING_BALL_FORMAT)){
            throw new IncorrectFormatException();
        }
        String[] expressions = input.split(" ");
        for (String expression : expressions){
            String[] tokens = expression.split("=");
            if (tokens[0].equals("name")){name = tokens[1];}
            else if (tokens[0].equals("x")){x = Double.parseDouble(tokens[1]);}
            else if (tokens[0].equals("y")){y = Double.parseDouble(tokens[1]);}
            else if (tokens[0].equals("xVelocity")){xVelocity = Double.parseDouble(tokens[1]);}
            else if (tokens[0].equals("yVelocity")){yVelocity = Double.parseDouble(tokens[1]);}            
        }
        return new Ball(name, x, y, xVelocity, yVelocity);
    }
    
    /**
     * Parses input to return a BoundarySide that should be made invisible. Also updates the
     * BoardNeighbor of the board with the relevant neighbor name
     * @param input wall invisibility message coming from server
     * @param neighbors information about the neighbors of the board. MUTATED BY THIS METHOD.
     * @return the BoundarySide to be made invisible
     * @throws IncorrectFormatException if input does not match message protocol
     */
    public static BoundarySide parseWallInvisibility(String input, BoardNeighbor neighbors) throws IncorrectFormatException {
        if ( ! input.matches(WALL_INVISIBILITY_FORMAT)){
            throw new IncorrectFormatException();
        }
        String[] tokens = input.split(" ");
        String side = tokens[1];
        String neighborName = tokens[2];
        if (side.equals("UP")){
            neighbors.changeUp(neighborName);
            return BoundarySide.TOP;
        } else if (side.equals("DOWN")){
            neighbors.changeDown(neighborName);
            return BoundarySide.BOTTOM;
        } else if (side.equals("RIGHT")){
            neighbors.changeRight(neighborName);
            return BoundarySide.RIGHT;
        } else if (side.equals("LEFT")){
            neighbors.changeLeft(neighborName);
            return BoundarySide.LEFT;
        } else {
            throw new IncorrectFormatException(); //never reached --> threw it already
        }
    }
    
    /**
     * Parses input to return a BoundarySide that should be made visible. Also updates the
     * BoardNeighbor of the board to reflect this change.
     * @param input wall visibility message coming from server
     * @param neighbors information about the neighbors of the board. MUTATED BY THIS METHOD.
     * @return the BoundarySide to be made visible
     * @throws IncorrectFormatException if input does not match message protocol
     */
    public static BoundarySide parseWallVisibility(String input, BoardNeighbor neighbors) throws IncorrectFormatException {
        if ( ! input.matches(WALL_VISIBILITY_FORMAT)){
            throw new IncorrectFormatException();
        }
        String[] tokens = input.split(" ");
        String side = tokens[1];
        if (side.equals("UP")){
            neighbors.changeUp("");
            return BoundarySide.TOP;
        } else if (side.equals("DOWN")){
            neighbors.changeDown("");
            return BoundarySide.BOTTOM;
        } else if (side.equals("RIGHT")){
            neighbors.changeRight("");
            return BoundarySide.RIGHT;
        } else if (side.equals("LEFT")){
            neighbors.changeLeft("");
            return BoundarySide.LEFT;
        } else {
            throw new IncorrectFormatException(); //never reached --> threw it already
        }
    }
}
