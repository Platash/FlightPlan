
package flightplan;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Class responsible for common calculations.
 * All functions are static
 * @author Anna Platash
 */
public class Calc {
    //1 degree = 111 km
    public static final double SCALE = 111;
    
    
    public static double getFlightDuration (double speed, double distance) {
        double duration = distance / speed;
        return duration;
    }
    
    //Calculates arrival time. Flight duration must be given in hours
    public static String getArrivalTime (String time, double flightDuration) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date date = df.parse(time);
        calendar.setTime(date);
        int sec = (int) Math.round(flightDuration * 3600);
        calendar.add(Calendar.SECOND, sec);
        String arrivalTime = df.format(calendar.getTime());
        return arrivalTime; 
    }
    
    public static double getDistance (Point start, Point finish) {
        double startX = start.getX();
        double startY = start.getY();
        double finishX = finish.getX();
        double finishY = finish.getY();
        double distance;
        distance = Math.abs(Math.sqrt(Math.pow(finishY - startY, 2) + Math.pow(finishX - startX, 2))) * SCALE;
        return distance;
    }
    
    public static double getDistance (FlightRoute line, Point point) {
        double x = point.getX();
        double y = point.getY();
        double a = line.getA();
        double b = line.getB();
        double c = line.getC();
        double distance;
        distance = Math.abs(a * x + b * y + c) / (Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2))) * SCALE;
        return distance;
    }
    
    
    public static Line getNormal (FlightRoute line, Point point) {
        double aNormal = line.getB();
        double bNormal = - line.getA();
        double cNormal = - point.getX() * aNormal - point.getY() * bNormal;
        Line normal = new Line (aNormal, bNormal, cNormal);
        return normal;
    }
    
    //Finding an intersection point of the lines described by these equations a1*x+b1*y+c1=0 and a2*x+b2*y+c2=0
    public static Point getProjection (FlightRoute line, Point point) {
        Line normal = getNormal(line, point);
        double a = line.getA();
        double b = line.getB();
        double c = line.getC();
        double aN = normal.getA();
        double bN = normal.getB();
        double cN = normal.getC();
        //Using Cramer's rule here to solve a system of linear equations
        //and find the Projection point
        double x =  (-c * bN + cN * b) / (a * bN - b * aN);
        double y = (-cN * a + c * aN) / (a * bN - b * aN);
        Point projection = new Point (x, y);
        return projection;
    }
    
    public static void sortByDistTotal (ArrayList <Field> array) {
        Collections.sort(array, new Comparator<Field>() {
            @Override
            public int compare(Field a, Field b) {
                return Double.compare(a.getDistTotal(), b.getDistTotal());
            }
        });
    }
     
    public static void sortByDistFieldFR (ArrayList <Field> array) {
        Collections.sort(array, new Comparator<Field>() {
            @Override
            public int compare(Field a, Field b) {
                return Double.compare(a.getDistFromFR(), b.getDistFromFR());
            }
        });
    }
        
}
