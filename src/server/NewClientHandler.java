package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A class that is used to listen to a socket and accept new connections -
 * must be run in its own thread because it can block!
 */
public class NewClientHandler implements Runnable{
    //AF: Represents a class that accepts new clients
    //RI: socketToListenTo is not null, inputMessageQueue is not null
    //Thread-safety: only fields are thread-safe. Makes new UserClients on new threads, and doesn't retain references to the
    //UserClient after the threads are started (while loop moves on).

    private final LinkedBlockingQueue<Message> inputMessageQueue; 
    private final ServerSocket socketToListenTo; 
    
    /**
     * Creates a new client handler that can accept connections from clients 
     * @param queue The input queue where clients can place input messages 
     * @param serverSocket Socket where users can connect to 
     */
    public NewClientHandler(LinkedBlockingQueue<Message> queue, ServerSocket serverSocket){
        this.inputMessageQueue = queue; 
        this.socketToListenTo = serverSocket; 
        checkRep();
    }

    private void checkRep(){
        assert(inputMessageQueue != null);
        assert(socketToListenTo != null);
    }
    
    /**
     * Will accept new sockets and create new clients to handle to sockets as well as start a new client thread for each client object 
     */
    public void run(){
        while(true){
            //this will block until there is a new Socket to accept
            try {
                Socket newSocket = socketToListenTo.accept();

                UserClient newClient = new UserClient(this.inputMessageQueue, newSocket); //make a new client that will listen to this particular new socket

                //start that client's thread
                new Thread(newClient).start(); 
            } catch (IOException e) {
                e.printStackTrace();
            } 
        }
    }
}
