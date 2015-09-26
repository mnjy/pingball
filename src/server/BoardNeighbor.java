package server;

/**
 * Class that represents a board's name and its 4 neighbors. This class is not thread-safe.
 */
public class BoardNeighbor {
    //AF: Represents a board's name and its <= 4 neighbors
    //RI: names of all boards are of the form [A-Za-z_][A-Za-z_0-9]*. Empty string represents no neighbors.
    
    /** the board's name */
    private String name = "";
    
    /** the left side neighboring board  */
    private String left = "";
    
    /** right is the right side neighboring board */
    private String right = "";
    
    /** the upper side neighboring board */
    private String up = "";
    
    /** the lower side neighboring board */
    private String down = "";

    private void checkRep(){
        String nameRegex = "[A-Za-z_][A-Za-z_0-9]*";
        assert(name.isEmpty() | name.matches(nameRegex));
        assert(left.isEmpty() | left.matches(nameRegex));
        assert(right.isEmpty() | right.matches(nameRegex));
        assert(up.isEmpty() | up.matches(nameRegex));
        assert(down.isEmpty() | down.matches(nameRegex));
    }
    
    /**
     * Constructs a new BoardNeighbor object
     * @param name is the name of the new BoardNeighbor object
     */
    public BoardNeighbor(String name) {
        this.name = name;
        checkRep();
    }
    
    /**
     * Changes the left neighboring board 
     * If there is none, left is the empty string
     * @param left the new left neighboring board name
     */
    public void changeLeft(String left) {
        this.left = left;
        checkRep();
    }
    
    /**
     * Changes the right neighboring board 
     * If there is none, right is the empty string
     * @param right the new right neighboring board name
     */
    public void changeRight(String right) {
        this.right = right;
        checkRep();
    }
    
    /**
     * Changes the upper neighboring board 
     * If there is none, up is the empty string
     * @param up the new upper neighboring board name
     */
    public void changeUp(String up) {
        this.up = up;
        checkRep();
    }
    
    /**
     * Changes the lower neighboring board 
     * If there is none, down is the empty string
     * @param down the new lower neighboring board name
     */
    public void changeDown(String down) {
        this.down = down;
        checkRep();
    }
    
    /**
     * @return the name of the board represented by this object, or empty string if none
     */
    public String name() {
        return name;
    }
    
    /**
     * @return the right neighbor, or empty string if none
     */
    public String right() {
        return right;
    }
    /**
     * @return the left neighbor, or empty string if none
     */
    public String left() {
        return left;
    }
    /**
     * @return the upper neighbor, or empty string if none
     */
    public String up() {
        return up;
    }
    /**
     * @return the lower neighbor, or empty string if none
     */
    public String down() {
        return down;
    }
    
    @Override
    public String toString() {
        return "name:"+name+" right:"+right+" left:"+left+" up:"+up+" down:"+down;
    }
}
