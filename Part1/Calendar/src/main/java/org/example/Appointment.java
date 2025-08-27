package org.example;

/* imports */
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.model.Dur;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.UUID;

/* class that handles the creation of appointments */
public class Appointment {

    public static void newAppointmentToCalendar (Calendar calendar) { //creates a new appointment

        VEvent appointment = new VEvent(); //creates an event-type appointment
        Scanner input = new Scanner(System.in); //creates a new scanner-object
        String name; //variable that contains the appointment's name

        System.out.println("Enter appointment name: "); //prints appropriate message
        name = input.nextLine().trim(); //requests for input and removes leading and trailing whitespace
        appointment.getProperties().add(new Summary(name)); //adds the summary in the list of properties
        String description; //variable that contains the appointment's description
        System.out.println("Enter appointment description: "); //prints appropriate message
        description = input.nextLine().trim(); //requests for input and removes leading and trailing whitespace
        appointment.getProperties().add(new Description(description)); //adds the description in the list of properties
        System.out.println("Enter appointment starting date and time (yyyy-MM-dd HH:mm:ss)"); //prints appropriate message
        appointment.getProperties().add(new DtStart(myCalendar.dateTimeFormatChecker())); //adds the starting date in the list of properties
        System.out.println("Enter appointment duration (DD:HH:mm:ss): "); //prints appropriate message

        boolean flag; //flag variable
        Dur dur = null; //initialized at null

        do {
            String durationInput = input.nextLine().trim(); //requests for input and removes leading and trailing whitespace
            // Check the format using DateTimeFormatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:HH:mm:ss"); //checks whether the pattern is valid

            try { //try-catch statement to pinpoint specific exceptions
                formatter.parse(durationInput); //parses the user's input
                dur = new Dur(durationInput); //creates the Dur-object in the case that the parsing was a success
                System.out.println("Parsed Duration: " + dur); //prints appropriate message
                flag = false; //initialized at false
            } catch (Exception e) { //catches the specific exception
                System.out.println("Error: Invalid format. Please enter the duration in DD:HH:mm:ss format."); //informs the user their input is invalid
                flag = true; //flag's value becomes true in order to exit the while-loop
            }
        } while (flag); //infinite loop until the flag's value changes from true to false

        appointment.getProperties().add(new Duration(dur)); //adds the duration in the list of properties
        String uid = UUID.randomUUID().toString(); //variable that contains the appointment's UID (unique identifier)
        appointment.getProperties().add(new Uid(uid)); //adds the UID in the list of properties
        calendar.getComponents().add(appointment); //adds the appointment in the list of components of the calendar
    }
}