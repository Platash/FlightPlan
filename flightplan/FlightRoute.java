
package flightplan;

import java.text.ParseException;

/**
 * Class contains info about current flight route
 * I consider a flight as a vector with it's starting point at (0, 0) coordinates.
 * Than depending on it's finish point's coordinates I'm getting a quadrant
 * in which this vector lies. 
 * This information is needed to select emergency airfields whith projections on flight line
 * lying only between start and finish points
 * For calculations I use line equation Ax+By+C=0
 * @author Anna Platash
 */

public class FlightRoute extends Line{
    private int quadrant;
    private String startTime;
    private String finishTime;
    private double speed;
    private double maxDistance;
    private Field startField;
    private Field finishField;
    
    //Using line equation here (y1-y2)*x+(x2-x1)*y+(x1y2-x2y1)=0
    public FlightRoute (Field startField, Field finishField, double maxDistance, 
                        double speed, String startTime) throws ParseException {
        super ();
        this.maxDistance = maxDistance;
        this.speed = speed;
        this.startTime = startTime;
        this.startField = startField;
        this.finishField = finishField;
        this.finishTime = Calc.getArrivalTime(startTime, Calc.getDistance(startField.getCoords(), 
                                                finishField.getCoords()));
        this.startField.setPassTime(startTime);
        double flightDist = (Calc.getDistance(startField.getCoords(), finishField.getCoords()));
        double flightDuration = Calc.getFlightDuration(speed, flightDist);
        this.finishField.setPassTime(Calc.getArrivalTime(startTime, flightDuration));
        
        setA(startField.getCoords().getY() - finishField.getCoords().getY());
        setB(finishField.getCoords().getX() - startField.getCoords().getX());
        setC(startField.getCoords().getX() * finishField.getCoords().getY() - 
                finishField.getCoords().getX() * startField.getCoords().getY());
        
        if (startField.getCoords().getX() == finishField.getCoords().getX() && 
                startField.getCoords().getY() == finishField.getCoords().getY()) {
            quadrant = 0;
            System.out.printf("You've chosen the same field twiсe. Are you sure that's what you want?");
        } else if (startField.getCoords().getX() <= finishField.getCoords().getX() 
                && startField.getCoords().getY() <= finishField.getCoords().getY()) {
            quadrant = 1;
        } else if (startField.getCoords().getX() >= finishField.getCoords().getX() 
                && startField.getCoords().getY() <= finishField.getCoords().getY()) {
            quadrant = 2;
        } else if (startField.getCoords().getX() >= finishField.getCoords().getX() 
                && startField.getCoords().getY() >= finishField.getCoords().getY()) {
            quadrant = 3;
        } else if (startField.getCoords().getX() <= finishField.getCoords().getX() 
                && startField.getCoords().getY() <= finishField.getCoords().getY()) {
            quadrant = 4;
        }
    }
    
    public String getFinishTime () {
        return this.finishTime;
    }
    
    public String getStartTime () {
        return this.startTime;
    }
    
    public double getSpeed () {
        return this.speed;
    }
    
    public double getMaxDistance () {
        return this.maxDistance;
    }
 
    public Point getStartPoint () {
        return this.startField.getCoords();
    }
    
    public Point getFinishPoint () {
        return this.finishField.getCoords();
    }
    
    public Field getStartField () {
        return this.startField;
    }
    
    public Field getFinishField () {
        return this.finishField;
    }
    
    public int getQuadrant () {
        return this.quadrant;
    }
    
    public void setFinishTime (String time) {
        this.finishTime = time;
    }
    
    public void setQuadrant (int quad) {
        this.quadrant = quad;
    }
}
