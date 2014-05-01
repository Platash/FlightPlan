
package flightplan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
/*
 * Algorytm znachodzenia lotnisk zapasowych:
 * - Traktuję linię lotu jako wektor z punktem zaczepienia (0, 0) i zwrotem w punkcie końcowym lotu. 
 * - Znachozę ćwiartek w którym leży wektor
 * - Znachodzę lotniska, projekcje których na linię lotu znachodzą się pomiędzy punktem startowym a końcowym lotu
 * - Wybieram lotniska odległe od linii lotu nie więcej niż maxDistance podana jako jeden z argumentów.
 * - Sortuję wybrane lotniska wg odległości od punktu startowego do danego lotniska
 * - Znachodzę pierwsze lotnisko zapasowe - to będzie lotnisko, sumarna odległość którego od punktu startowego
 * jest największa, ale mniejsza od maxDistance.
 * - Zaznaczam nową CheckPoint (CP) - chyli punkt na linii lotu, z którego odległość do danego lotniska zapasowego
 * jest równa maxDistance
 *                                    lotnisko
 *                                        |  
 *        Finish <---|---CP----|----CP----|----Start
 *                   |         |
 *                   |      lotnisko
 *                lotnisko
 * 
 * 
 * - Szukam kolejne lotnisko, odległośc którego od CheckPoint jest maksymalna, ale mniejsza od maxDistance
 * - Kiedy odległość pomiędzy kolejnym lotniskiem zapasowym a punktem końcowym będzie mniejsza od maxDistance, 
 * poszukiwanie lotnisk jest zakończone.
 * - Wychodzę z załozenia, że smolot w razie zaistnienia takiej potrzeby może lecieć w kierunku przeciwnym
 * do poprzedniego kierunku lotu, jeśli już przeleciał punkt projekcji kolejnego lotniska zapasowego, 
 * ale wciąż znajduję się na odległości mniejszej niż maxDistance od niego.
 * Zmiana całego algorytmu by samolot wchodził do "zony odpowiedzialności" kolejnego lotniska
 * odrazu po przelocie nad punktem projekcji poprzedniego polega na kasowaniu dwoch ostatnich składowych 
 * z równania: 
 * distStartCP = possibleFields.get(i-1).getDistStartProj() + (flightRoute.getMaxDistance() - possibleFields.get(i-1).getDistFromFR());
 */ 
 
/**
 * The main class.
 * @author Anna Platash
 */
public class Main {
    
    /**
     * @param args the command line arguments
     * IATA code of the starting point airfield
     * IATA code of the destination airfield
     * max distance to emergency airfield from the flight route
     * airplane's speed
     */
    public static void main(String[] args) throws ParseException {
        //Checking if the number of given arguments is correct
        if(args.length < 5) {
            System.out.println("Wrong arguments");
            System.exit(0);
        }
        //Creating an ArrayList of Fields with info about airfields from atteched file
        FieldsData fieldsData = new FieldsData();
        Field startField = fieldsData.getFieldByIATA(args[0], fieldsData.getFieldsData());
        Field finishField = fieldsData.getFieldByIATA(args[1], fieldsData.getFieldsData());
        double speed=0;
        double maxDistance=0;
        String time = null;
        //Checking if given arguments are correct
        try {
            speed = Double.parseDouble(args[3]);
        } catch (NumberFormatException e) {
            System.out.println("Wrong speed");
            System.exit(0);
        }
        try {
            maxDistance = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Wrong max distance");
            System.exit(0);
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            df.parse(args[4]); 
            time = args[4];
        }
        catch (ParseException ee) {
            System.out.printf("Unable to parse date.");
            System.exit(0);
        }
        FlightRoute flightRoute = new FlightRoute (startField, finishField, maxDistance, speed, time);
        ArrayList <Field> emergencyFields = fieldsData.getEmergencyFields(flightRoute);
        System.out.println(flightRoute.getStartField().toString());
        int size = emergencyFields.size();
        for (int i = 0; i < size; i++) {
            System.out.println(emergencyFields.get(i).toString());
        }
        System.out.println(flightRoute.getFinishField().toString());
    }
}
