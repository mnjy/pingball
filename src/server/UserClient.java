package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a client that is connected to a server.
 * It accepts messages from the client and sends them on to the server. 
 */
public class UserClient implements Runnable {
    //AF: Represents a client that is connected to a server.
	//RI: a client is valid iff: 
	// it has a socket that is connected to a user
	// it has a reference to the input queue where it can place input messages to the server. 
	// only the Client can remove objects from its BlockingQueue queue 
    //Thread safety: 
    // uses two different threads: one for output and one for input. Both of these communicate with the
    // main server thread by the use of two blocking queues. 

	/** the main message queue on which to post messages for the server to deal with them. Client is a producer of input messages. */
	private final LinkedBlockingQueue<Message> inputMessageQueue; 

	/** The queue where messages to the user can be put in order to be written to the user*/ 
	private final LinkedBlockingQueue<String> outputMessageQueue; 

	/** The socket that corresponds to this client's input and output*/ 
	private final Socket socketToListenTo; 

	/** a boolean which indicates if this client is connected or not*/ 
	private AtomicBoolean connected; 

	/**
	 * Creates a new Client 
	 * @param queue the input queue where input messages should be stored 
	 * @param socket The socket to the user
	 */
	public UserClient (LinkedBlockingQueue<Message> queue, Socket socket){
		this.inputMessageQueue = queue; 
		this.socketToListenTo = socket; 
		this.connected = new AtomicBoolean(socket.isConnected()); 
		this.outputMessageQueue = new LinkedBlockingQueue<String>(); 
	}
	
	/**
	 * Puts a message onto the UserClient's queue, to be sent to the client.
	 * Is a blocking method.
	 * @param message the message to be sent to the client
	 * @throws InterruptedException if the queue is interrupted
	 */
	public void sendToClient(String message) throws InterruptedException{
	    this.outputMessageQueue.put(message);
	}
	
	/**
	 * Puts a message onto the UserClient's queue, to be sent to the server.
     * Is a blocking method.
	 * @param message the message to be sent to the server
	 * @throws InterruptedException if the queue is interrupted
	 */
	public void sendToServer(String message) throws InterruptedException{
	    this.inputMessageQueue.put(new Message(message, this));
	}

	/**
	 * Looks for input messages and puts them on the input queue 
	 */
	public void run(){
		Thread outputThread = new Thread(new Runnable() {
			/**
			 * Handles the output of messages 
			 */
			@Override 
			public void run(){
				while(connected.get()){
					try {
						PrintWriter outputToServer = new PrintWriter(socketToListenTo.getOutputStream(), true); 
						String fromOutputQueue = outputMessageQueue.take(); //is blocking 
						outputToServer.println(fromOutputQueue); //write output 
					} catch (IOException | InterruptedException e) {
						if (socketToListenTo.isClosed() || !(socketToListenTo.isConnected())){ //otherwise just keep trying.
							disconnect();
						}
					}

				}

			}
		}); 
		outputThread.start(); 

		while(this.connected.get()){ //while this client is still connected then keep listening for messages from the user and keep outputting according to the outputMessageQueue 
			//should always have a message to output after any input 


			try { //try with resources- will close the buffer after we're finished with it 
				InputStreamReader inputStream = new InputStreamReader(this.socketToListenTo.getInputStream()); 
				BufferedReader inputBufferFromClient =  new BufferedReader(inputStream); 
				//get the input bytes from the client- will throw IOException if there are no new bytes to read or if the socket is disconnected or closed  
				String inputString = inputBufferFromClient.readLine(); 

				//put this input string into a message along with the sender and then put that message on the inputMessageQueue; 
                while (inputString != null){
                    //put this input string into a message along with the sender and then put that message on the inputMessageQueue; 
                    Message inputMessage = new Message(inputString, this);
                    inputMessageQueue.put(inputMessage); //will add the element as long as there is space- will block and wait for space to become available if there is no space. 
                    inputString = inputBufferFromClient.readLine(); 
                }
			} catch (IOException | InterruptedException e) {
					this.disconnect();
			}
		}
	}



	/**
	 * Can only be called by the main server thread 
	 * will make this client
	 * stop pushing messages onto the message queue and 
	 */
	void disconnect(){
	    try {
            this.inputMessageQueue.put(new Message("DISCONNECT", this));
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
		this.connected.set(false); 
		try {
			this.socketToListenTo.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
