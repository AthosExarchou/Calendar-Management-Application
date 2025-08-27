package org.example;

/* imports */
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VToDo;
import java.util.ArrayList;
import java.util.Comparator;

/* main class that executes the program */
public class Main {
    public static void main (String[] args) {

        if (args.length == 2) { //case where 2 arguments where given

            String calendarName = args[1]; //adds the second argument provided, to calendarName
            Calendar calendar = myCalendar.loadCalendar(calendarName); //loads the calendar

            /* case where the calendar was loaded successfully */

            //creates an ArrayList that will contain the sorted events (appointments & works)
            ArrayList<Object> sortedEvents = new ArrayList<>();

            //for-loop that accesses every single component of the given calendar
            for (Object component : calendar.getComponents()) {
                if (component instanceof VEvent) { //case where the component is of type VEvent

                    VEvent vevent = (VEvent) component; //casts the component to type-VEvent to prevent errors
                    sortedEvents.add(vevent); //adds an appointment to the sortedEvents ArrayList

                } else if (component instanceof VToDo) { //case where the component is of type VToDo

                    VToDo vtodo = (VToDo) component; //casts the component to type-VToDo to prevent errors
                    sortedEvents.add(vtodo); //adds a work to the sortedEvents ArrayList
                }
            }

            //try-catch statement to pinpoint a specific exception
            try {
                //https://www.geeksforgeeks.org/double-colon-operator-in-java/
                //sorting the array using method reference:
                //https://stackoverflow.com/questions/18895915/how-to-sort-an-array-of-objects-in-java
                sortedEvents.sort(Comparator.comparing(myCalendar :: getDateTime));
                String userChoice = args[0]; //variable containing the user's first argument
                myCalendar.calendarLists(userChoice, sortedEvents); //prints a sorted list of the events

            } catch (NullPointerException e) {  //catches the specific exception
                //informs the user via appropriate message and exits
                System.out.println("Calendar is empty. Exiting..." + e.getMessage());
            }
        } else if (args.length == 1) { //case where 1 argument was given

            String calendarName = args[0]; //adds the sole argument provided, to calendarName
            Calendar calendar = myCalendar.loadCalendar(calendarName); //loads the calendar

            if (calendar != null) { //case where the calendar was loaded successfully
                myCalendar.calendarHandler(calendar, calendarName); //handles the newly created calendar (adds new events etc.)
            } else { //case where the calendar failed to load

                System.out.println("This calendar doesn't exist. Creating a new one..."); //prints appropriate message
                calendar = myCalendar.newCalendar(); //creates the new calendar
                myCalendar.calendarHandler(calendar, calendarName); //handles the newly created calendar (adds new events etc.)
            }
        } else {
            //informs the user their provided arguments' number is invalid and exits
            System.out.println("Please enter either 1 or 2 arguments! Exiting...");
            throw new IllegalArgumentException(); //throws a fitting exception
        }
    }
}
