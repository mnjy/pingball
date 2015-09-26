package server;

/**
 * Immutable class that stores information about portals required by the server
 */
public class PortalInfo {
	//AF: represents information about a portal object 
	//RI: name is of the form [A-Za-z][A-Za-z0-9_]* or an empty string
    //    x and y are positive and inside the board
    //Thread safety: all fields final and immutable.

    private final String name;
    private final int x;
    private final int y;
    
    /**
     * Creates a PortalInfo object
     * @param name the name of the associated portal
     * @param x the x location of the associated portal
     * @param y the y location of the associated portal
     */
    public PortalInfo(String name, int x, int y){
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    /**
     * @return the name of the associated portal
     */
    public String name(){
        return name;
    }
    
    /**
     * @return the x location of the associated portal
     */
    public int x(){
        return x;
    }
    
    /**
     * @return the y location of the associated portal
     */
    public int y(){
        return y;
    }
}
