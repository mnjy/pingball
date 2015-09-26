package server;

import java.util.Map;
import java.util.Set;

import physics.Vect;
import boardparser.IncorrectFormatException;

/**
 * Parses messages received by the server and sends them to the appropriate clients, if applicable.
 * MESSAGE PROTOCOL:
 * It accepts the following requests from clients and/or the command line:
 *      Incoming Ball From Wall ::= ball name=NAME x=DOUBLE y=DOUBLE xVelocity=DOUBLE yVelocity=DOUBLE
 *      Incoming Ball From Portal ::= ball name=NAME toBoard=NAME toPortal=NAME xVelocity=DOUBLE yVelocity=DOUBLE returnX=DOUBLE returnY=DOUBLE
 *      Board Name ::= myboardname=NAME
 *      Connect Boards ::= (h NAME_left NAME_right) | (v NAME_top NAME_bottom)
 *      Portal Contained ::= portal name=NAME x=INTEGER y=INTEGER
 *      Disconnect Client ::= DISCONNECT
 * It sends the following replies to clients:
 *      Outgoing Ball ::= ball name=NAME x=DOUBLE y=DOUBLE xVelocity=DOUBLE yVelocity=DOUBLE
 *      Make Wall Invisible ::= makeinvisiblewall UP|DOWN|LEFT|RIGHT NAME
 *      Make Wall Visible ::= makevisiblewall UP|DOWN|LEFT|RIGHT
 * Where:
 *      NAME ::= [A-Za-z_][A-Za-z_0-9]*
 *      INTEGER ::= [0-9]+
 *      DOUBLE ::= [-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?
 */
public class ServerMessageParser {
    //No AF/RI
    //All fields are final and immutable. Thread-safe by immutability.
    
    private final static String NAME = "[A-Za-z_][A-Za-z_0-9]*";
    private final static String INTEGER = "[0-9]+";
    private final static String DOUBLE = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
    private final static String INCOMING_BALL_FROM_WALL_FORMAT = "ball name="+NAME+" x="+DOUBLE+" y="+DOUBLE+" xVelocity="+DOUBLE+" yVelocity="+DOUBLE;
    /*ball name=NAME toBoard=NAME x=FLOAT y=FLOAT xVelocity=FLOAT yVelocity=FLOAT
     *(x, y) is the point of collision with the invisible wall.*/
    private final static String INCOMING_BALL_FROM_PORTAL_FORMAT = "ball name="+NAME+" toBoard="+NAME+" toPortal="+NAME+" xVelocity="+DOUBLE+" yVelocity="+DOUBLE+" returnX="+DOUBLE+" returnY="+DOUBLE;
    /*ball name=NAME toBoard=NAME toPortal=NAME xVelocity=FLOAT yVelocity=FLOAT returnX=FLOAT returnY=FLOAT;
     *if portal name given, it is the portal to which the ball should be sent.*/
    
    private final static String BOARD_NAME_FORMAT = "myboardname="+NAME;
    /*myboardname=NAME */
    
    private final static String WS = "( |\t)+"; //whitespace, to ignore
    private final static String OPT_WS = "( |\t)*"; //extra whitespace, to ignore
    private final static String CONNECT_BOARDS_FORMAT = OPT_WS+"(h|v)"+WS+NAME+WS+NAME+OPT_WS;
    /*h NAME_left NAME_right | v NAME_top NAME_bottom */

    private final static String PORTAL_CONTAINED_FORMAT = "portal name="+NAME+" x="+INTEGER+" y="+INTEGER;
    /*portal name=NAME x=INTEGER y=INTEGER
     * where (x, y) is the location of the portal*/
    
    private final static String DISCONNECT_CLIENT_FORMAT = "DISCONNECT";
    /*DISCONNECT */
    
    private final static double DOUBLE_EQUALITY_THRESHOLD = 0.0001;
    
    /**
     * Enumerates the types of messages the server can receive
     */
    public enum ServerMessageType {
        INCOMING_BALL_FROM_WALL, INCOMING_BALL_FROM_PORTAL, BOARD_NAME, CONNECT_BOARDS, PORTAL_CONTAINED, DISCONNECT_CLIENT;
    }
    
    /**
     * Given a message from a client, returns the type of message
     * @param input the message from the client
     * @return the type of message
     * @throws IncorrectFormatException if message does not follow the message protocol
     */
    public static ServerMessageType parse(String input) throws IncorrectFormatException {
        if (input.matches(INCOMING_BALL_FROM_WALL_FORMAT)){return ServerMessageType.INCOMING_BALL_FROM_WALL;}
        else if (input.matches(INCOMING_BALL_FROM_PORTAL_FORMAT)){return ServerMessageType.INCOMING_BALL_FROM_PORTAL;}
        else if (input.matches(BOARD_NAME_FORMAT)){return ServerMessageType.BOARD_NAME;}
        else if (input.matches(CONNECT_BOARDS_FORMAT)){return ServerMessageType.CONNECT_BOARDS;}
        else if (input.matches(PORTAL_CONTAINED_FORMAT)){return ServerMessageType.PORTAL_CONTAINED;}
        else if (input.matches(DISCONNECT_CLIENT_FORMAT)){return ServerMessageType.DISCONNECT_CLIENT;}
        else {throw new IncorrectFormatException();}
    }
    
    /**
     * Parses input to send a ball to the appropriate board, or does nothing if the board doesn't exist (in that case,
     * the ball is lost)
     * @param input the outgoing ball from the client. requires that the board is connected along the wall that was collided with.
     * @param neighbors the neighbors of the board from which the message came
     * @param boardToClient map of board names to UserClients running the boards
     * @throws IncorrectFormatException if input does not match ball from wall message protocol
     */
    public static void parseIncomingBallFromWall(String input, BoardNeighbor neighbors, Map<String, UserClient> boardToClient) throws IncorrectFormatException {        
        if ( ! input.matches(INCOMING_BALL_FROM_WALL_FORMAT)){
            throw new IncorrectFormatException();
        }
        String boardToSendTo = "";
        String ballname = "";
        double x = 0;
        double y = 0;
        double xVelocity = 0;
        double yVelocity = 0;
        
        String[] expressions = input.split(" ");
        for (String expression : expressions){
            String[] tokens = expression.split("=");
            if (tokens[0].equals("name")){ballname = tokens[1];}
            else if (tokens[0].equals("x")){
                double prevX = Double.parseDouble(tokens[1]);
                if (Math.abs(prevX - 19.75) < DOUBLE_EQUALITY_THRESHOLD){ 
                    //collided with right wall --> going to left
                    x = 0.25;
                    boardToSendTo = neighbors.right();
                }
                else if (Math.abs(prevX) - 0.25 < DOUBLE_EQUALITY_THRESHOLD){
                    //collided with left wall --> going to right
                    x = 19.75;
                    boardToSendTo = neighbors.left();
                }
                else {x = prevX;}
            }
            else if (tokens[0].equals("y")){
                Double prevY = Double.parseDouble(tokens[1]);
                if (Math.abs(prevY - 19.75) < DOUBLE_EQUALITY_THRESHOLD){ 
                    //collided with bottom wall --> going to top
                    y = 0.25;
                    boardToSendTo = neighbors.down();
                }
                else if (Math.abs(prevY) - 0.25 < DOUBLE_EQUALITY_THRESHOLD){
                    //collided with top wall --> going to bottom
                    y = 19.75;
                    boardToSendTo = neighbors.up();
                }
                else {y = prevY;}
            }
            else if (tokens[0].equals("xVelocity")){xVelocity = Double.parseDouble(tokens[1]);}
            else if (tokens[0].equals("yVelocity")){yVelocity = Double.parseDouble(tokens[1]);} 
            
        }
        String outgoingMessage = "ball name="+ballname+" x="+x+" y="+y+" xVelocity="+xVelocity+" yVelocity="+yVelocity;
        UserClient clientToSendTo = boardToClient.get(boardToSendTo);
        if (clientToSendTo != null){
            try {
                clientToSendTo.sendToClient(outgoingMessage);
            } catch (InterruptedException e) {
                //do nothing --> this only happens in the case where the neighboring board just disconnected,
                //in which case it's ok for the ball to be "lost".
            }
        }
    }
    

    /**
     * Parses input to and sends a ball to the appropriate board
     * @param input the outgoing ball from the client. requires that the board and portal being sent to exist.
     * @param sender The client that sent this message
     * @param boardToClient map of board names to UserClients running the boards
     * @param clientPortals map of UserClients to the portals their boards contain
     * @throws IncorrectFormatException if input does not match ball from portal message protocol
     * @throws InterruptedException if both client queues are interrupted
     */
    public static void parseIncomingBallFromPortal(String input, UserClient sender, Map<String, UserClient> boardToClient, Map<UserClient, Set<PortalInfo>> clientPortals) throws IncorrectFormatException, InterruptedException {
        String boardToSendTo = "";
        UserClient clientToSendTo = null;
        boolean portalExists = true;
        String ballname = "";
        double x = 0;
        double y = 0;
        double xVelocity = 0;
        double yVelocity = 0;
        double returnX = 0;
        double returnY = 0;
        
        if ( ! input.matches(INCOMING_BALL_FROM_PORTAL_FORMAT)){
            throw new IncorrectFormatException();
        }
        String[] expressions = input.split(" ");
        for (String expression : expressions){
            String[] tokens = expression.split("=");
            if (tokens[0].equals("name")){ballname = tokens[1];}
            else if (tokens[0].equals("toBoard")){
                boardToSendTo = tokens[1];
                clientToSendTo = boardToClient.get(boardToSendTo);
            }
            else if (tokens[0].equals("toPortal")){
                String portalToSendTo = tokens[1];
                if (clientToSendTo == null){
                    portalExists = false;
                } else {
                    Set<PortalInfo> allPortals = clientPortals.get(clientToSendTo);
                    if (allPortals == null){
                        portalExists = false;
                    } else {
                        for (PortalInfo portal : allPortals){
                            if (portal.name().equals(portalToSendTo)){
                                x = portal.x();
                                y = portal.y();
                            }
                        }
                    }
                }
            }
            else if (tokens[0].equals("xVelocity")){xVelocity = Double.parseDouble(tokens[1]);}
            else if (tokens[0].equals("yVelocity")){yVelocity = Double.parseDouble(tokens[1]);}       
            else if (tokens[0].equals("returnX")){returnX = Double.parseDouble(tokens[1]);}
            else if (tokens[0].equals("returnY")){returnY = Double.parseDouble(tokens[1]);}
        }
        if (portalExists){
            //set the ball to the edge of the new portal
            double portalCenterX = x + 0.5;
            double portalCenterY = y + 0.5;
            Vect velocity = new Vect(xVelocity, yVelocity);
            x = portalCenterX + velocity.unitSize().x()/2;
            y = portalCenterY + velocity.unitSize().y()/2;
            
            //send the ball on
            String outgoingMessage = "ball name="+ballname+" x="+x+" y="+y+" xVelocity="+xVelocity+" yVelocity="+yVelocity;
            try{
            clientToSendTo.sendToClient(outgoingMessage);
            } catch (InterruptedException e){
                portalExists = false; 
            }
        } 
        if (!portalExists) {
            //send the ball back
            String returnMessage = "ball name="+ballname+" x="+returnX+" y="+returnY+" xVelocity="+xVelocity+" yVelocity="+yVelocity;
            sender.sendToClient(returnMessage);
        }
    }

    
    /**
     * Connects two boards, as specified in input, and updates this information in boardsNeighbors. 
     * Also sends a message to each of the clients, telling them to connect their boards.
     * @param input a request to connect two boards, as specified by the message protocol. Requires that
     * both boards exist on the server
     * @param boardToClient map of board names to UserClients running the boards
     * @param boardsNeighbors map of clients to the names of their board's neighbors. MUTATED BY THIS METHOD
     * @throws IncorrectFormatException if input does not match board connecting message protocol
     */
    public static void parseConnectRequest(String input, Map<String, UserClient> boardToClient, Map<UserClient, BoardNeighbor> boardsNeighbors) throws IncorrectFormatException {
        if ( ! input.matches(CONNECT_BOARDS_FORMAT)){
            throw new IncorrectFormatException();
        }
        String[] tokens = input.split(WS);
        String board2 = tokens[1];
        String board1 = tokens[2];
        UserClient client2 = boardToClient.get(board2);
        UserClient client1 = boardToClient.get(board1);
        if (tokens[0].equals("h")){
            UserClient right = client1;
            UserClient left = client2;
            if (right != null && left != null){ //check both boards exist
                String rightName = boardsNeighbors.get(right).name();
                String leftName = boardsNeighbors.get(left).name();
                try{
                    //send messages to each of the boards to connect
                    right.sendToClient("makeinvisiblewall LEFT "+leftName);
                    left.sendToClient("makeinvisiblewall RIGHT "+rightName);

                    //if any previous connections, break them
                    String prevLeft = boardsNeighbors.get(right).left();
                    if (!prevLeft.isEmpty()){
                        UserClient prevLeftClient = boardToClient.get(prevLeft);
                        prevLeftClient.sendToClient("makevisiblewall RIGHT");
                    }
                    String prevRight = boardsNeighbors.get(left).right();
                    if (!prevRight.isEmpty()){
                        UserClient prevRightClient = boardToClient.get(prevRight);
                        prevRightClient.sendToClient("makevisiblewall LEFT");
                    }

                    //store (and change) board neighbor info in the server
                    boardsNeighbors.get(right).changeLeft(leftName);
                    boardsNeighbors.get(left).changeRight(rightName);
                } catch (InterruptedException e){
                    throw new IncorrectFormatException();
                }
            } else {
                throw new IncorrectFormatException();
            }
        } else {
            UserClient bottom = client1;
            UserClient top = client2;
            if (bottom != null && top != null){ //check both boards exist
                String bottomName = boardsNeighbors.get(bottom).name();
                String topName = boardsNeighbors.get(top).name();
                try{
                    //send messages to each of the boards to connect
                    bottom.sendToClient("makeinvisiblewall UP "+topName);
                    top.sendToClient("makeinvisiblewall DOWN "+bottomName);

                    //if any previous connections, break them
                    String prevUp = boardsNeighbors.get(bottom).up();
                    if (!prevUp.isEmpty()){
                        UserClient prevUpClient = boardToClient.get(prevUp);
                        prevUpClient.sendToClient("makevisiblewall DOWN");
                    }
                    String prevDown = boardsNeighbors.get(top).down();
                    if (!prevDown.isEmpty()){
                        UserClient prevDownClient = boardToClient.get(prevDown);
                        prevDownClient.sendToClient("makevisiblewall UP");
                    }

                    //store (and change) board neighbor info in the server
                    boardsNeighbors.get(bottom).changeUp(topName);
                    boardsNeighbors.get(top).changeDown(bottomName);
                    
                } catch (InterruptedException e){
                    //don't do anything
                }
            } else {
                throw new IncorrectFormatException();
            }
        }
    }
    
    /**
     * Disconnects a client from the server by removing all references to that client and its board in the server's fields.
     * Also sends messages to any boards connected to the client to make their walls visible, and updates their BoardNeighbor
     * in the server.
     * @param input a disconnect request from a client
     * @param thisClient the client to be disconnected
     * @param boardToClient map of board names to UserClients running the boards. MUTATED BY THIS METHOD.
     * @param clientPortals map of UserClients to the portals their boards contain. MUTATED BY THIS METHOD
     * @param boardsNeighbors map of clients to the names of their board's neighbors. MUTATED BY THIS METHOD
     * @throws IncorrectFormatException if input does not match disconnecting message protocol
     */
    public static void parseDisconnectClient(String input, UserClient thisClient, Map<String, UserClient> boardToClient, Map<UserClient, Set<PortalInfo>> clientPortals, Map<UserClient, BoardNeighbor> boardsNeighbors) throws IncorrectFormatException {
        if ( ! input.matches(DISCONNECT_CLIENT_FORMAT)){
            throw new IncorrectFormatException();
        }
        BoardNeighbor boardAndNeighbors = boardsNeighbors.get(thisClient);
        if (boardAndNeighbors == null){ //this board was never added to the server -- disconnected because initialization error
            return;
        }
        String thisBoard = boardAndNeighbors.name();
        boardToClient.remove(thisBoard);
        clientPortals.remove(thisClient);
        boardsNeighbors.remove(thisClient);
        String left = boardAndNeighbors.left();
        String right = boardAndNeighbors.right();
        String up = boardAndNeighbors.up();
        String down = boardAndNeighbors.down();
        try{
            if (!left.isEmpty() && left != thisBoard){
                UserClient leftClient = boardToClient.get(left);
                boardsNeighbors.get(leftClient).changeRight("");
                leftClient.sendToClient("makevisiblewall RIGHT");
            }
        } catch (InterruptedException e) {
            //do nothing. It'll send its own disconnect message, if need be.
        }
        try{
            if (!right.isEmpty() && right != thisBoard){
                UserClient rightClient = boardToClient.get(right);
                boardsNeighbors.get(rightClient).changeLeft("");
                rightClient.sendToClient("makevisiblewall LEFT");
            }
        } catch (InterruptedException e) {
        }
        try{
            if (!up.isEmpty() && up != thisBoard){
                UserClient upClient = boardToClient.get(up);
                boardsNeighbors.get(upClient).changeDown("");
                upClient.sendToClient("makevisiblewall DOWN");
            }
        } catch (InterruptedException e) {
        }
        try{
            if (!down.isEmpty() && down != thisBoard){
                UserClient downClient = boardToClient.get(down);
                boardsNeighbors.get(downClient).changeUp("");
                downClient.sendToClient("makevisiblewall UP");
            }
        } catch (InterruptedException e) {
        }
    }
    
    /**
     * Parses input to obtain the board name of the client's board
     * @param input a board name message from the client
     * @return the client's board name
     * @throws IncorrectFormatException if input does not match board name message protocol
     */
    public static String parseBoardName(String input) throws IncorrectFormatException {
        if ( ! input.matches(BOARD_NAME_FORMAT)){
            throw new IncorrectFormatException();
        }
        String[] tokens = input.split("=");
        String boardname = tokens[1];
        return boardname;
    }
    
    /**
     * Parses input to obtain a PortalInfo object containing relevant information
     * about the sent portal
     * @param input a portal contained message from the client
     * @return information about the portal specified
     * @throws IncorrectFormatException if input does not match portal contained message protocol
     */
    public static PortalInfo parsePortalsContained(String input) throws IncorrectFormatException {
        if ( ! input.matches(PORTAL_CONTAINED_FORMAT)){
            throw new IncorrectFormatException();
        }
        String name = "";
        int x = 0; //need to initialize with random value
        int y = 0; //need to initialize with random value
        String[] expressions = input.split(" ");
        for (String expression : expressions){
            String[] tokens = expression.split("=");
            if (tokens[0].equals("name")){name = tokens[1];}
            else if (tokens[0].equals("x")){x = Integer.parseInt(tokens[1]);}
            else if (tokens[0].equals("y")){y = Integer.parseInt(tokens[1]);}
        }
        return new PortalInfo(name, x, y); 
    }
}
