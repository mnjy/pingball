package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;



/**
 * A class that represents the Client's interface with the server.
 * It accepts messages from the server and sends them on to the client.
 */
public class ClientMessagePassingThread implements Runnable{
    //AF: Represents the client's interface with the server.
	//RI: socket is not null 
    //Thread-safety: this class uses four fields:
    //  inputQueue -- a thread-safe queue
    //  outputQueue -- a thread-safe queue
    //  socket -- final and immutable
    //  connected -- an atomic boolean, a thread-safe data-type
	
	/** holds input messages from the server to the client */ 
	private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();

	/** holds output messages from the client to the server*/ 
	private BlockingQueue<String> outputQueue = new LinkedBlockingQueue<>();

	/** socket where the client is connected to the server */ 
	private final Socket socket; 

	/** tells whether this client is connected or not */ 
	private AtomicBoolean connected; 

	/**
	 * Creates a new ClientMessagePassing object
	 * @param socket that connects the client to the server
	 * @param input where to put messages incoming from the server 
	 * @param output messages on this queue will be sent to the server 
	 */
	public ClientMessagePassingThread(Socket socket, LinkedBlockingQueue<String> input, LinkedBlockingQueue<String> output) {
		this.inputQueue = input; 
		this.outputQueue = output; 
		this.connected = new AtomicBoolean(socket.isConnected()); 
		this.socket = socket; 
		checkRep();
	}

	private void checkRep(){
	    assert(this.socket != null);
	}
	
	/**
	 * handles the input and output of messages
	 */
	@Override
	public void run() {
		Thread outputThread = new Thread(new Runnable() {
			/**
			 * Handles the output of messages 
			 */
			@Override 
			public void run(){
				while(connected.get()){
					try {
						PrintWriter outputToServer = new PrintWriter(socket.getOutputStream(), true); 
						String fromOutputQueue = outputQueue.take(); //is blocking 
						outputToServer.println(fromOutputQueue); //write output 
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}

				}

			}
		}); 
		outputThread.start(); 


		//Now process regular input and output messages 
		while(this.connected.get()){ //while this client is still connected then keep listening for messages from the user and keep outputting according to the outputMessageQueue 

		    try { //try with resources- will close the buffer after we're finished with it 
		        BufferedReader inputBufferFromClient =  new BufferedReader(new InputStreamReader(this.socket.getInputStream())); 
		        //get the input bytes from the client- will throw IOException if there are no new bytes to read or if the socket is disconnected or closed  
		        String inputString = inputBufferFromClient.readLine(); 
		        while (inputString != null){
		            //put this input string into a message along with the sender and then put that message on the inputMessageQueue; 
		            inputQueue.put(inputString); //will add the element as long as there is space- will block and wait for space to become available if there is no space. 
		            inputString = inputBufferFromClient.readLine(); 
		        }

		        if ( inputString == null){ //make sure that we're still connected if we get null out of the input string.
		            this.disconnect();
		        }


		    } catch (IOException | InterruptedException e) {
					this.disconnect();
			}
		}
	}



	/**
	 * make the client disconnect from the server
	 */
	void disconnect(){
	    try {
            this.inputQueue.put("DISCONNECT");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		this.connected.set(false); 
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
