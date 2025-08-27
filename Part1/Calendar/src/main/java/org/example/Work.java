package org.example;

/* imports */
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.*;
import java.util.Scanner;
import java.util.UUID;

/* class that handles the creation of works */
public class Work {

    public static void newWorkToCalendar (Calendar calendar) { //creates a new work

        VToDo work = new VToDo(); //creates a new work
        Scanner input = new Scanner(System.in); //creates a new scanner-object
        String name; //variable that contains the work's name

        System.out.println("Enter work name: "); //prints appropriate message
        name = input.nextLine().trim(); //requests for input and removes leading and trailing whitespace
        work.getProperties().add(new Summary(name));
        String description; //variable that contains the work's description
        System.out.println("Enter work description: "); //prints appropriate message
        description = input.nextLine().trim(); //requests for input and removes leading and trailing whitespace
        work.getProperties().add(new Description(description));
        System.out.println("Enter work deadline (yyyy-MM-dd HH:mm:ss)"); //prints appropriate message
        DateTime endTime = myCalendar.dateTimeFormatChecker();
        work.getProperties().add(new Due(endTime));
        String status; //variable that contains the work's status
        System.out.println("Is the work completed? (yes/no)"); //prints appropriate message
        status = input.nextLine().trim(); //requests for input and removes leading and trailing whitespace

        while (!status.equalsIgnoreCase("yes") && !status.equalsIgnoreCase("no")) {
            System.out.println("Please enter yes or no: "); //prints appropriate message
            status = input.nextLine().trim(); //requests for input and removes leading and trailing whitespace
        }
        if (status.equalsIgnoreCase("yes")) { //case where the input was "yes"
            work.getProperties().add(new Status("COMPLETED")); //adds the COMPLETED status if the specific work has been completed
        } else { //case where the input was "no"
            work.getProperties().add(new Status("IN-PROCESS")); //adds the IN-PROCESS status if the specific work has yet to be completed
        }
        String uid = UUID.randomUUID().toString(); //variable that contains the work's UID (unique identifier)
        work.getProperties().add(new Uid(uid)); //adds the UID in the list of properties
        calendar.getComponents().add(work); //adds the work in the list of components of the calendar
    }
}