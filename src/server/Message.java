package server;


/**
 * Class that stores a message, together with the sender of it. This class is not thread-safe.
 *
 */
public class Message {
    //AF: holds a message and its sender
    //RI: Message not null
    
    private final String message;
    private final UserClient sender;
    
    /**
     * Create a Message object containing a string message and the sender thread object
     * @param message the message string
     * @param sender the sender's thread object
     */
    public Message(String message, UserClient sender) {
        this.message = message;
        this.sender = sender;
        checkRep();
    }
    
    private void checkRep(){
        assert(message != null);
    }
    
    /**
     * 
     * @return a String representation of the message contained
     */
    public String message() {
        return this.message;
    }
    
    /**
     * @return the sender of this message
     */
    public UserClient sender() {
        return this.sender;
    }
}
