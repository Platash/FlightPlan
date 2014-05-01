package flightplan;

import java.text.DecimalFormat;

/**
 * Class contains info about an airfield
 * @author Anna Platash
 */
public class Field {
    private String city;
    private String country;
    private String iata;
    private Point coords;
    private Point projOnFR;
    private double distTotal;
    private String passTime;
    
    public Field (String city, String country, String iata, Point coords) {
        this.city = city;
        this.country = country;
        this.iata = iata;
        this.coords = coords;
    }
    
    public Field (String city, String country, String iata, Point coords, String passTime) {
        this.city = city;
        this.country = country;
        this.iata = iata;
        this.passTime = passTime;
    }
    
    public Field () {
    }
    
   public String getCity () {
        return this.city;
    }
    
    public String getCountry () {
        return this.country;
    }
    
    public String getIata () {
        return this.iata;
    }
    
    public Point getCoords () {
        return this.coords;
    }
    
    /**
    * @return Point airfields projection on the flight route 
    */
    public Point getProjOnFR () {
        return this.projOnFR;
    }
    
    /**
    * @return Double distance from the airfield to flight route in km
    */
    public double getDistFromFR () {
        double distance = Calc.getDistance(coords, projOnFR);
        return distance;
    }
    
    /**
    * @return Double distance from start point airfield to current airfield in km
    */
    public double getDistTotal () {
        return this.distTotal;
    }
    
    /**
    * @return Double distance from start point airfield to current airfield's projection
    * on flight route in km
    */
    public double getDistStartProj () {
        double distance = distTotal - Calc.getDistance(coords, projOnFR);
        return distance;
    }
    
    /**
    * @return String time of passing the projection of current airfield
    */
    public String getPassTime () {
        return this.passTime;
    }
    
    public void setCity (String city) {
        this.city = city;
    }
    
    public void setCountry (String country) {
        this.country = country;
    }
    
    public void setIata (String iata) {
        this.iata = iata;
    }
    
    public void setCoords (Point coords) {
        this.coords = coords;
    }
    
    public void setProjOnFR (Point projection) {
        this.projOnFR = projection;
    }
    
    public void setDistTotal (double distance) {
        this.distTotal = distance;
    }
    
    public void setPassTime (String time) {
        this.passTime = time;
    }
    
    @Override
    public String toString () {
        DecimalFormat df = new DecimalFormat("#.####");
        return this.iata + " " + this.city + " " + this.country + " " 
                    + df.format( this.coords.getY()) + " " 
                    + df.format(this.coords.getX()) + " " + this.passTime;
    }
}
