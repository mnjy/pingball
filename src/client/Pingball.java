package client;

import implementation.Ball;
import implementation.Board;
import implementation.Boundary;
import implementation.Board.BoundarySide;
import implementation.GameObject;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import server.PortalInfo;
import ui.PingballGUI;
import client.ClientMessageParser.ClientMessageType;
import boardparser.BoardParser;
import boardparser.IncorrectFormatException;

/**
 * Creates a Pingball game simulation, that may be connected to a server. If it is connected:
 * It sends the following requests to the server:
 *      Outgoing Ball From Wall ::= ball name=NAME x=DOUBLE y=DOUBLE xVelocity=DOUBLE yVelocity=DOUBLE
 *      Outgoing Ball From Portal ::= ball name=NAME toBoard=NAME toPortal=NAME xVelocity=DOUBLE yVelocity=DOUBLE returnX=DOUBLE returnY=DOUBLE
 *      Board Name ::= myboardname=NAME
 *      Portal Contained ::= portal name=NAME x=INTEGER y=INTEGER
 * It accepts the following messages from the server:
 *      Incoming Ball ::= ball name=NAME x=DOUBLE y=DOUBLE xVelocity=DOUBLE yVelocity=DOUBLE
 *      Make Wall Invisible ::= makeinvisiblewall UP|DOWN|LEFT|RIGHT name
 *      Make Wall Visible ::= makevisiblewall UP|DOWN|LEFT|RIGHT
 *      Invalid Board Name ::= invalid board name: NAME
 * Where:
 *      NAME ::= [A-Za-z_][A-Za-z_0-9]*
 *      INTEGER ::= [0-9]+
 *      DOUBLE ::= [-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?
 */
public class Pingball {
    //AF: represents a pingball client
    //RI: none
    
    /*
     * System description:
     * Our system implements the client in three threads: 
     * 1. the main client thread, where the Pingball action from phase1 is happening.
     * 2. the input thread, implemented in ClientMessagePassingThread, is a thread that handles input messages. It blocks on inputQueue.
     * 3. the output thread, named outputThread inside ClientMessagePassingThread, handles output messages. It blocks on outputQueue.
     * 
     * These threads communicate between each other with the use of threadsafe datatypes, specifically, blocking queues.
     * Let's see how these pieces come together. First, the input thread, ClientMessagePassingThread, receives input messages from
     * the socket. As soon as a new message arrives, the thread puts the message on the inputQueue, which is a blocking queue
     * taking messages from the input thread to the main client thread. 
     * 
     * Next, in the main thread, a message is pulled off the queue at every board time step by Board. Poll() is not blocking, but
     * it tries to pop something from the queue, and if it doesn't succeed, it returns null. The board in the main thread calls 
     * ClientMessageParser to parse the message and make the required modifications to the board. 
     * Now if there are any messages that the main thread needs to send to the server (eg: ball hit invisible wall or portal),
     * then it puts the message on the outputQueue, which is also a blocking queue, communicating with the output thread. 
     * The outputThread pops the message and immediately writes it to the socket.
     */
    
    /* System level thread safety argument:
     *      By confinement:
     *          Each client is confined to its own three threads. 
     *          The board is confined to the main client thread.
     *      By threadsafe data-structures:
     *         The only data shared across multiple threads is the inputQueue (shared between main thread and input thread) and the
     *         outputQueue (shared between main thread and the output thread). Both of these objects are thread-safe data types.
     *         Since we are only putting things onto the inputQueue in the input thread and taking them out in the main client 
     *         thread, we are safe from deadlock. Similarly, the output is written to only by the main client thread and read from
     *         the output thread, avoiding deadlock.
     */

    private LinkedBlockingQueue<String> inputQueue; 
    private LinkedBlockingQueue<String> outputQueue; 
    private boolean isConnectedToClientServer = false;
    private Board board;
    
    public Pingball(Board boardToPlay) {
        this.board = boardToPlay;
    }
    
    /**
     * @param args :
     * Pingball [FILE] -- creates and runs the board defined by FILE and runs single-machine play. 
     *                     If FILE not specified, runs default board
     * Pingball [--host HOST] [--port PORT] FILE -- creates and runs the board defined by FILE and runs client-server play, 
     *                     connecting to the server with the given port and host.
     *                     If no host provided, starts single-machine play.
     *                     If no port provided, uses default port 10987.
     * @throws IOException if running client-server play and unable to connect to socket,
     *                     or if a specified file is not found
     * @throws InterruptedException if running client-server play and queues are interrupted
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        
        /** Default server port. */
        int port = 10987;
        /** Maximum port number as defined by ServerSocket. */
        int MAXIMUM_PORT = 65535;
        /** Host name as provided in the handout. */
        String host = "";
        /** The default board to run */
        String fileName = "resources/empty.pb";
        
        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));     
        
        try {
            while ( ! arguments.isEmpty()) {
                String flag = arguments.remove();
                try {
                    if (flag.equals("--host")) {
                        host = arguments.remove();
                    } else if (flag.equals("--port")) {
                        port = Integer.parseInt(arguments.remove());
                        if (port < 0 || port > MAXIMUM_PORT) {
                            throw new IllegalArgumentException("port " + port + " out of range");
                        }
                    } else { //any other flag has to be a fileName
                        fileName = flag;
                    }
                } catch (NoSuchElementException nsee) {
                    throw new IllegalArgumentException("missing argument for " + flag);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("unable to parse number for " + flag);
                }
            }
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.err.println("usage: Pingball [--host HOST] [--port PORT] FILE, or Pingball [FILE]");
            return;
        }
        
        BoardParser boardParser = new BoardParser();
        Board boardToPlay = boardParser.getBoardFromFileName(fileName);
        Pingball pingball = new Pingball(boardToPlay);
        
        if(!host.isEmpty()) { //server-client play         
            pingball.connectToClientAndServer(host, port);
        }

        PingballGUI.playPingball(pingball);
//        pingball.playBoardInTextMode();
    }
    
    /**
     * @return whether or not the client is connected to a server
     */
    public boolean isConnected(){
        return this.isConnectedToClientServer;
    }

    /**
     * Makes the board ready for Client-Server play by connecting it to a client and server
     * @param host the IP to connect to
     * @param port the port to connect to
     * @throws InterruptedException if there is a problem communicating with the server
     * @throws IOException if there is a problem connecting to the server
     */
    public void connectToClientAndServer(String host, int port) throws InterruptedException, IOException {
        final int timeout = 1000;
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), timeout);
        this.inputQueue = new LinkedBlockingQueue<String>();
        this.outputQueue = new LinkedBlockingQueue<String>();
        ClientMessagePassingThread messagePassingThread = new ClientMessagePassingThread(socket, inputQueue , outputQueue); 
        Thread t = new Thread(messagePassingThread); 
        t.start(); 
        
        String myBoardName = "myboardname="+board.getName();
        outputQueue.put(myBoardName);
        for (PortalInfo portal : board.getPortalInfo()){
            String portalContained = "portal name="+portal.name()+" x="+portal.x()+" y="+portal.y();
            outputQueue.put(portalContained);
        }
        this.isConnectedToClientServer = true;
        board.connectToClientServer();
    }
    
    /**
     * Disconnects the board from the server. Removes all balls and makes all the walls visible.
     */
    public void disconnectFromClientAndServer() {
        //sends disconnect message to server
        this.sendMessages(Arrays.asList("DISCONNECT"));
        
        //disconnect self
        this.isConnectedToClientServer = false;
        board.disconnectFromClientServer();
        this.inputQueue = null;
        this.outputQueue = null;
        
        //make all boundaries visible
        for (Boundary boundary : board.boundaries){
            boundary.makeVisible();
        }
        board.neighbors.changeDown("");
        board.neighbors.changeLeft("");
        board.neighbors.changeRight("");
        board.neighbors.changeUp("");
        
        //remove all balls
        board.clearBalls();
    }
    
    /**
     * If connected to client and server, reads messages from the server and
     * takes the appropriate actions. Else, does nothing. Non-blocking method.
     */
    public void readMessages() {
        if (!this.isConnectedToClientServer){return;}
        String inputMessage = inputQueue.poll();
        if (inputMessage != null){
            try{
                ClientMessageType messageType = ClientMessageParser.parse(inputMessage);
                if (messageType == ClientMessageType.INCOMING_BALL){
                    //parses the message to get a ball, which is then added to the board
                    Ball ball = ClientMessageParser.parseIncomingBall(inputMessage);
                    board.addBall(ball);
                }
                else if (messageType == ClientMessageType.WALL_INVISIBILITY){
                    //parses the messages to find out which boundary to make invisible, and does so
                    BoundarySide whichBoundary = ClientMessageParser.parseWallInvisibility(inputMessage, board.neighbors);
                    board.makeInvisible(whichBoundary);
                }
                else if (messageType == ClientMessageType.WALL_VISIBILITY){
                    //parses the messages to find out which boundary to make visible, and does so
                    BoundarySide whichBoundary = ClientMessageParser.parseWallVisibility(inputMessage, board.neighbors);
                    board.makeVisible(whichBoundary);
                }
                else if (messageType == ClientMessageType.DISCONNECT_CLIENT){
                    disconnectFromClientAndServer();
                }
            } catch (IncorrectFormatException e){
                //don't do anything --> message is not parsed, but play on this board still continues
            }
        }
    }
    
    /**
     * If connected to client and server, sends messages 
     * to the server. Else, does nothing.
     * @param messages the messages to send to the server
     */
    public void sendMessages(List<String> messages) {
        if (!this.isConnectedToClientServer){return;}
        for (String message : messages){
            try {
                outputQueue.put(message);
                break;
                //board is sending a ball message about this ball, which means this ball is leaving the board
            } catch (InterruptedException e) {
                //don't do anything --> queue to server is interrupted, but single-machine play still happens
            }
        }
    }
    
    /**
     * Changes the board to be played
     * @param board the new board to be played
     */
    public void changeBoard(Board board){
        this.board = board;
    }
    
    /**
     * @return the board currently being played
     */
    public Board board(){
        return this.board;
    }
    
    /**
     * @param gadgetName the name of the gadget to return
     * @return the GameObject named by gadgetName, or null if no such GameObject exists
     */
    public GameObject getGadgetFromName(String gadgetName) {
        return board.getGadgetFromName(gadgetName);
    }
    
    /**
     * Runs the board (i.e. updates the state of the board constantly).
     * If connected to client and server, also reads and sends messages to and from
     * the server, and updates board accordingly. Board is displayed in text mode.
     */
    public void playBoardInTextMode() {
        System.out.println(board.makeTextModeDisplay());
        while (true) {
            try {
                int sleepTime = (int)(board.timeStep*1000);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.readMessages();
            List<String> messagesToSend = board.updateState();
            System.out.println(messagesToSend);
            this.sendMessages(messagesToSend);
            System.out.println(board.makeTextModeDisplay());
        }
    }
}