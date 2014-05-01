
package flightplan;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


/**
 * Class contains info about airfields from atteched file
 * @author Anna Platash
 */
public class FieldsData {
    
    private final String DATA_PATH = "airports.dat";
    private ArrayList <Field> fieldsDataArray;
    
    
    public FieldsData () {
        
         fieldsDataArray = new ArrayList <>();
         this.readFile(DATA_PATH);
    }
    
    /**
    * Function opens a file with airfields info, reads it and writes into an ArrayList of Fieds
    * @param path path to a file which is to be open
    */
    public final void readFile (String path) {
        
        File file = new File (path);
        String string;
        Field field;
        Point coords;
        StringTokenizer stringToken;
        BufferedReader bufferReader;
        try {
            bufferReader = new BufferedReader(new FileReader(file));
            outerLoop:
            while ((string = bufferReader.readLine()) != null) {
                String [] tempArray = new String [5];
                stringToken = new StringTokenizer(string, ",", false);
                int i = 0;
                int j = 0;
                while (stringToken.hasMoreTokens()) {
                    String next = stringToken.nextToken();
                    next = next.replaceAll("\"", "");
                    if (i < 2 || i == 5 || i > 7) {
                        i++;
                        continue;
                    } 
                    //If a field with IATA code is empty we consider this airport being non-public and skip whole line 
                    if (i == 4 && next.isEmpty()) {
                        continue outerLoop;
                    } 
                    tempArray[j] = next;
                    j++;
                    i++;
                }
                coords = new Point (Double.parseDouble(tempArray[4]), Double.parseDouble(tempArray[3]));
                field = new Field (tempArray[0], tempArray[1], tempArray[2], coords);
                fieldsDataArray.add(field);
            }
        }
        catch (FileNotFoundException ee) {
            System.out.println("File not found.\n");
        }
        catch (IOException ee) {
        }
    }
    
    /**
    * Function creates a list of potential emergency airfields - with a distance from flight route
    * no more a max distance (a command line argument)
    * @param flightRoute an object containing info about current flight route
    * @return ArrayList<Field> an ArrayList of potential emergency airfields
    * sorted by distance from starting point airfield
    */
    public ArrayList <Field> getPotentFields (FlightRoute flightRoute) {
        
        ArrayList <Field> potentFields = new ArrayList <>();
        Field temp;
        //Coordinates of an airfield
        Point fieldCoords;
        //Coordinates of an airfield's projection on the flight route
        Point projOnFR;
        //Distance from the Flight Route to a field
        double distFieldFR;
        
        double startX = flightRoute.getStartPoint().getX();
        double startY = flightRoute.getStartPoint().getY();
        double finishX = flightRoute.getFinishPoint().getX();
        double finishY = flightRoute.getFinishPoint().getY();
        int size = fieldsDataArray.size();
        
        //Checking all airfields, if distance from a field to flight route is lesser or equal to max distance,
        //adding a field to possibleFields ArrayList.
        for (int i = 0; i < size; i++){
            fieldCoords = getPointByNum(i, fieldsDataArray);
            //Distance from a Field to it's projection on a Flight Route
            distFieldFR = Calc.getDistance(flightRoute, fieldCoords);
            if (distFieldFR <= flightRoute.getMaxDistance()) {
                projOnFR = Calc.getProjection(flightRoute, fieldCoords);
                if (flightRoute.getQuadrant() == 1) {
                    if (projOnFR.getX() >= startX && projOnFR.getX() <= finishX && 
                            projOnFR.getY() >= startY && projOnFR.getY() <= finishY) {
                        temp = fieldsDataArray.get(i);
                        //Projection of a potential emergency field on the flight route
                        temp.setProjOnFR(projOnFR);
                        //Distance from start field to potential field.
                        temp.setDistTotal (Calc.getDistance(flightRoute.getStartPoint(), projOnFR) + distFieldFR);
                        potentFields.add(temp);
                    }
                } else if (flightRoute.getQuadrant() == 2) {
                    if (projOnFR.getX() <= startX && projOnFR.getX() >= finishX && 
                            projOnFR.getY() >= startY && projOnFR.getY() <= finishY) {
                        temp=fieldsDataArray.get(i);
                        temp.setProjOnFR(projOnFR);
                        temp.setDistTotal (Calc.getDistance(flightRoute.getStartPoint(), projOnFR) + distFieldFR);
                        potentFields.add(temp);
                    }
                } else if (flightRoute.getQuadrant() == 3) {
                    if (projOnFR.getX() <= startX && projOnFR.getX() >= finishX && 
                            projOnFR.getY() <= startY && projOnFR.getY() >= finishY) {
                        temp=fieldsDataArray.get(i);
                        temp.setProjOnFR(projOnFR);
                        temp.setDistTotal (Calc.getDistance(flightRoute.getStartPoint(), projOnFR) + distFieldFR);
                        potentFields.add(temp);
                    }
                } else {
                    if (projOnFR.getX() >= startX && projOnFR.getX() <= finishX && 
                            projOnFR.getY() >= startY && projOnFR.getY() <= finishY) {
                        temp=fieldsDataArray.get(i);
                        temp.setProjOnFR(projOnFR);
                        temp.setDistTotal (Calc.getDistance(flightRoute.getStartPoint(), projOnFR) + distFieldFR);
                        potentFields.add(temp);
                    }
                }
            }
        }
        //Sorting the elements of the ArrayList by a summary distance from the starting point  
        //to a potential emergency airfield.
        Calc.sortByDistTotal(potentFields);
        return potentFields;
    }
       
    /**
    * Function creates a list of emergency airfields
    * @param flightRoute an object containing info about current flight route
    * @return ArrayList<Field> an ArrayList of emergency airfields
    */
    public ArrayList <Field> getEmergencyFields (FlightRoute flightRoute) throws java.text.ParseException{
        ArrayList <Field> emergencyFields = new ArrayList <>();
        Field tempField = null;
        //Distance from Start Point to CheckPoint 
        double distStartCP = 0;
        //Distance from Finish Point to CheckPoint
        double distFinishCP = Calc.getDistance(flightRoute.getStartPoint(), flightRoute.getFinishPoint());
        String passTime;
        //Distance from CheckPoint to a potential Emergency Field
        double distFieldCP;
        ArrayList <Field> possibleFields = getPotentFields (flightRoute);
        int size = possibleFields.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            distFieldCP = possibleFields.get(i).getDistTotal() - distStartCP;
            if (distFieldCP <= flightRoute.getMaxDistance()) {
                tempField = possibleFields.get(i);
                count++;
            //If the distance between previouse check point and current airfield's projection is more than max distance
            //(command line argument) we take previouse field (distance from which to check point was lesser than max distance)
            //and add it to emergency fields list
            } else {
                if (count != 0 ) {
                    //Updting check point value
                    distStartCP = possibleFields.get(i-1).getDistStartProj() + 
                                (flightRoute.getMaxDistance() - possibleFields.get(i-1).getDistFromFR());
                    count = 0;
                    passTime = Calc.getArrivalTime(flightRoute.getStartTime(), 
                            Calc.getFlightDuration(flightRoute.getSpeed(), possibleFields.get(i-1).getDistStartProj()));
                    tempField.setPassTime(passTime);
                    emergencyFields.add(tempField);
                    distFinishCP -= distStartCP;
                } else {
                    //If given max distance value is too small and the are no emergency airfields available
                    if (distFinishCP > flightRoute.getMaxDistance()) {
                        System.out.printf("Max distance to emergency fields is too short. \n");
                        System.out.printf("No emergency fields available between \n");
                        return null;
                    } 
                }
            }
        }
        return emergencyFields;
    }
   
    /**
    * @param i element's index in the array
    * @param dataArray ArrayList of Fields from which we want to get a value
    * @return Point airfield's coordinates
    */
    public Point getPointByNum (int i, ArrayList<Field> dataArray) {
        Point point = null;
        try {
            point = dataArray.get(i).getCoords();
        } catch (IndexOutOfBoundsException ee) {
            System.out.println("No such point");
            System.exit(0);
        }
        return point;
    }
    
    /**
    * @param iata IATA code of an airfield
    * @param dataArray ArrayList of Fields from which we want to get a value
    * @return Point airfield's coordinates
    */
    public Point getPointByIATA (String iata, ArrayList<Field> dataArray) {
        int fieldCount = 0;
        Point point = null;
        int size = dataArray.size();
        for (int i = 0; i < size; i++) {
            if (dataArray.get(i).getIata().equals(iata) && fieldCount == 0) {
                point = dataArray.get(i).getCoords();
                fieldCount++;
            } else if (dataArray.get(i).getIata().equals(iata) && fieldCount > 0){
                System.out.println("There is more then one field with given IATA");
                System.exit(0);
            } 
        }
        if (fieldCount == 0) {
            System.out.println("There is no field with given IATA");
            System.exit(0);
        }
        return point;
    }
    
    /**
    * @param iata IATA code of an airfield
    * @param dataArray ArrayList of Fields from which we want to get a value
    * @return Field; a Field object
    */
    public Field getFieldByIATA (String iata, ArrayList<Field>dataArray){
        int fieldCount = 0;
        Point point = null;
        String city = null;
        String country = null;
        int size = dataArray.size();
        for (int i = 0; i < size; i++) {
            if (dataArray.get(i).getIata().equals(iata) && fieldCount == 0) {
                point = dataArray.get(i).getCoords();
                city = dataArray.get(i).getCity();
                country = dataArray.get(i).getCountry();
                fieldCount++;
            } else if (dataArray.get(i).getIata().equals(iata) && fieldCount > 0){
                System.out.println("There is more then one field with given IATA");
                System.exit(0);
            } 
        }
        if (fieldCount == 0) {
            System.out.println("There is no field with given IATA");
            System.exit(0);
        }
        Field field = new Field (city, country, iata, point);
        return field;
    }
    
    public ArrayList <Field> getFieldsData () {
        return this.fieldsDataArray;
    }
    
    public String getDataPath () {
        return this.DATA_PATH;
    }
    
    
}
