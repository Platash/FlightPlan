
package flightplan;

/**
 * Class contains info about a line
 * For calculations I use line equation Ax+By+C=0
 * @author Anna Platash
 */

public class Line {
    private double a, b, c;
    
    public Line () {}
    
    public Line (double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    public double getA () {
        return this.a;
    }
    
    public double getB () {
        return this.b;
    }
    
    public double getC () {
        return this.c;
    }
    
    public void setA (double a) {
        this.a = a;
    }
    
    public void setB (double b) {
        this.b = b;
    }
    
    public void setC (double c) {
        this.c = c;
    }
}
