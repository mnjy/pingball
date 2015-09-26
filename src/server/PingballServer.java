package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import boardparser.IncorrectFormatException;
import server.ServerMessageParser.ServerMessageType;

/**
 * PingballServer is a server that handles games of Pingball involving multiple clients. This class is thread-safe.
 * It accepts the following requests from clients:
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
 *      
 *      
 */
public class PingballServer {
	//AF: represents a pingball server
	//RI: serverSocket is not null
    
    /*
     * System description:
     * Our system implements the main server in three threads: the main server thread, the new client handler thread, and the console reading thread.
     * In addition, the connection with each client is handled by a UserClient class which runs in a separate thread.
     *      Main Server thread:
     *         runs PingballServer, which uses ServerMessageParser to do parsing operations and calculations for ball positions. It
     *         takes messages from the inputQueue (blocking until one is available) and puts messages on the outputQueues of the
     *         specific UserClient
     *      New Client Handler thread:
     *         runs NewClientHandler, which blocks on the ServerSocket and accepts new clients. It makes a new thread running a
     *         UserClient for each connection.
     *      Console Read Thread:
     *         reads from System.in and puts it on the inputQueue
     *      UserClient:
     *           the server's interface with each client. It runs two threads, one which blocks on the inputMessageQueue to the server, and one
     *           which blocks on the outputMessagwQueue to the client. All UserClients share the inputMessageQueue on which they put messages
     *           for the server, but each one has its own outputMessageQueue on which it receives messages to send to its client.
     */
    
	/* System level thread safety argument: 
	 *      By confinement:
	 *         PingballServer and NewClientHandler are both confined to single threads.
	 *         Fields boardToClient, clientPortals, and boardsNeigbors in PingballServer are all confined to main server thread.
	 *         Each UserClient is confined to its own threads (input and output).
	 *      By threadsafe data-structures:
	 *         The only data shared across multiple threads is the ServerSocket (shared between the main thread and the new client
	 *         handler thread), the inputMessageQueue (shared between all threads), and the outputMessageQueue (shared between the
	 *         main thread and the particular UserClient thread). All of these objects are thread-safe data types.
	 *         Messages are only put onto the inputMessageQueue by the UserClient's input thread and are only taken off inside 
	 *         PingballServer's serve() method, so there can be no deadlock here.
	 *         Messages are only put onto the outputMessageQueue by the serve() method (using ServerMessageParser) and are only
	 *         taken off the queue by the UserClient's output thread, so they cannot deadlock.
	 */

	/** Default server port. */	
	private static final int DEFAULT_PORT = 10987;
	/** Maximum port number as defined by ServerSocket. */
	private static final int MAXIMUM_PORT = 65535;

	/** Socket for receiving incoming connections. */
	private final ServerSocket serverSocket;

	private final LinkedBlockingQueue<Message> inputMessageQueue;
	private Map<String, UserClient> boardToClient = new HashMap<>();
	private Map<UserClient, Set<PortalInfo>> clientPortals = new HashMap<>();
	private Map<UserClient, BoardNeighbor> boardsNeighbors = new HashMap<>();


	/**
	 * Handles the instantiation of a new PingballServer 
	 * @param args The port on which to run the port, default is 10987
	 */
	public static void main(String[] args){
		int port = DEFAULT_PORT; 
		Queue<String> arguments = new LinkedList<String>(Arrays.asList(args)); 

		try {
			while ( ! arguments.isEmpty()) {
				String flag = arguments.remove();
				try {
					if (flag.equals("--port")) {
						port = Integer.parseInt(arguments.remove());
						if (port < 0 || port > MAXIMUM_PORT) {
							throw new IllegalArgumentException("port " + port + " out of range");
						}
					}
				} catch (NoSuchElementException nsee) {
					throw new IllegalArgumentException("missing argument for " + flag);
				} catch (NumberFormatException nfe) {
					throw new IllegalArgumentException("unable to parse number for " + flag);
				}
			}
		} catch (IllegalArgumentException iae) {
			System.err.println(iae.getMessage());
			System.err.println("usage: PingballServer [--port PORT]");
			return;
		}

		try {
			runPingballServer(port);
		} catch (IOException | InterruptedException ioe) {
			throw new RuntimeException(ioe);
		}



	}
	/**
	 * Make a PingballServer that listens for connections on port.
	 * 
	 * @param port port number, requires 0 to port to 65535
	 * @throws IOException if an error occurs opening the server socket
	 */
	public PingballServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		this.inputMessageQueue = new LinkedBlockingQueue<Message>(); 

		NewClientHandler acceptNewClients = new NewClientHandler(inputMessageQueue, serverSocket); 
		Thread acceptClientHandlerThread = new Thread(acceptNewClients); 
		acceptClientHandlerThread.start(); 

		//Create a new thread to handle reading from the console 
		Thread consoleReadThread = new Thread(){
			public void run(){
				while(true){
					//reads from console and then puts message on the input queue. 
					BufferedReader readConsole = new BufferedReader(new InputStreamReader(System.in));
					String stringCommand = null;
					try {
						stringCommand = readConsole.readLine();
					} catch (IOException e1) {
						e1.printStackTrace();
					} 
					if ( stringCommand != null){
						try {
							inputMessageQueue.put(new Message( stringCommand, null));
						} catch (InterruptedException e) {
							e.printStackTrace();
						} 
					}
				}
			}
		}; 
		consoleReadThread.start();
		checkRep();
	}

	/**
	 * Start a PingballServer running on the specified port.
	 * @param port The network port on which the server should listen.
	 * @throws IOException if a network error occurs
	 * @throws InterruptedException if the main input queue is interrupted
	 */
	public static void runPingballServer(int port) throws IOException, InterruptedException {
		PingballServer server = new PingballServer(port);
		server.serve(); 
	}

	private void checkRep() {
		assert(this.serverSocket != null);
	}

	/**
	 * Run the server, receiving messages from clients and the command line and dealing with them.
	 * Never returns unless an exception is thrown.
	 * 
	 * @throws InterruptedException if the main input queue is interrupted
	 *                     (IOExceptions from individual clients do *not* terminate serve())
	 */
	public void serve() throws InterruptedException {
		while (true) {
		    // block until gets an input message     
			Message inputMessage = inputMessageQueue.take();
			
			try {
				//Deal with input and output messages
				UserClient thisClient = inputMessage.sender();
				String inputString = inputMessage.message();
				ServerMessageType messageType = ServerMessageParser.parse(inputString);

				if (messageType == ServerMessageType.BOARD_NAME){
					String boardname = ServerMessageParser.parseBoardName(inputString);
					if(boardToClient.containsKey(boardname)) { //if boards with same name, just disconnect
						thisClient.disconnect();
					} else {
						boardToClient.put(boardname, thisClient);
						//also make new BoardNeighbors object for this client
						boardsNeighbors.put(thisClient, new BoardNeighbor(boardname));
					}
				}
				else if (messageType == ServerMessageType.PORTAL_CONTAINED){
					PortalInfo thisPortal = ServerMessageParser.parsePortalsContained(inputString);
					if (clientPortals.containsKey(thisClient)){
						clientPortals.get(thisClient).add(thisPortal);
					} else {
						Set<PortalInfo> portals = new HashSet<PortalInfo>();
						portals.add(thisPortal);
						clientPortals.put(thisClient, portals);
					}
				}
				else if (messageType == ServerMessageType.INCOMING_BALL_FROM_WALL){
					BoardNeighbor neighbors = boardsNeighbors.get(thisClient);
					ServerMessageParser.parseIncomingBallFromWall(inputString, neighbors, boardToClient);
				}

				else if (messageType == ServerMessageType.INCOMING_BALL_FROM_PORTAL){
					ServerMessageParser.parseIncomingBallFromPortal(inputString, thisClient, boardToClient, clientPortals);
					//throws error if both clients don't exist, in which case the ball is lost
				}

				else if (messageType == ServerMessageType.CONNECT_BOARDS){
					try {
						ServerMessageParser.parseConnectRequest(inputString, boardToClient, boardsNeighbors);
					} catch (IncorrectFormatException e){
						System.out.println("error -- could not execute command ["+inputString+"].");
					}
				}

				else if (messageType == ServerMessageType.DISCONNECT_CLIENT){
					ServerMessageParser.parseDisconnectClient(inputString, thisClient, boardToClient, clientPortals, boardsNeighbors);
				}

			} catch (IncorrectFormatException ioe) {
				// IncorrectFormatException -- if received message does not follow protocol
				// this exception wouldn't terminate serve()
				System.out.println("error -- command unknown.");
			}
		}
	}
}
