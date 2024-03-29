
package flightplan;

/**
 * Two coordinates X and Y making a Point
 * @author Anna Platash
 */
public class Point {
    private double x, y;
    
    public Point (double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Point () {       
    }
    
    public double getX () {
        return this.x;
    }
    
    public double getY () {
        return this.y;
    }
    
    public void setX (double x) {
        this.x = x;
    }
    
    public void setY (double y) {
        this.y = y;
    }
}
