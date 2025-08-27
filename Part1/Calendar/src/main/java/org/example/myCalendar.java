package org.example;

/* imports */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.*;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.*;
import java.io.IOException;
import java.util.Date;
import net.fortuna.ical4j.model.DateTime;
import gr.hua.dit.oop2.calendar.TimeService;
import gr.hua.dit.oop2.calendar.TimeTeller;
import net.fortuna.ical4j.util.CompatibilityHints;

/* class that handles everything that has to do with the calendar */
public class myCalendar {

    //loads the calendar that was requested by the user
    public static Calendar loadCalendar (String calendarName) {

        //try-catch statement to pinpoint specific exceptions
        try (FileInputStream fileInput = new FileInputStream(calendarName)) { //creates a new object for fileInput
            //https://www.ical4j.org/examples/parsing/
            //https://stackoverflow.com/questions/14106844/how-to-make-ical4j-not-fail-on-parsing-errors-like-00001231t000000z
            //used "CompatibilityHints" to be able to accept complex calendars (e.g. greece.ics)
            CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, true);
            CalendarBuilder calendarBuilder = new CalendarBuilder(); //creates a new object for calendarBuilder
            Calendar calendar = calendarBuilder.build(fileInput);
            System.out.println("Calendar loaded successfully in path: " + calendarName); //prints appropriate message

            return calendar; //returns the calendar

        } catch (IOException | ParserException e) { //catches these specific exceptions
            System.out.println("Parsing error when loading the calendar in path: " + calendarName); //prints appropriate message
            throw new NullPointerException(); //Exits the program by throwing the appropriate exception
        }
    }

    //adds the standard properties of the new calendar that was requested by the user
    public static Calendar newCalendar() { //https://www.ical4j.org/examples/model/ cc : Creating a new calendar

        Calendar calendar = new Calendar(); //creates the new calendar-object
        calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0); //adds the version
        calendar.getProperties().add(CalScale.GREGORIAN); //adds the calculator scale
        return calendar; //returns the calendar with the added properties
    }

    //handles the calendar (adds an appointment or work)
    public static void calendarHandler (Calendar calendar, String calendarName) {

        Scanner input = new Scanner(System.in); //creates a new scanner-object
        String userChoice; //variable for the user's input

        System.out.println("Do you want to add a new event? (yes/no)"); //prints appropriate message
        //https://www.w3schools.com/jsref/jsref_trim_string.asp
        userChoice = input.nextLine().trim(); //requests for input and removes leading and trailing whitespace
        do {
            while (!userChoice.equalsIgnoreCase("yes") && !userChoice.equalsIgnoreCase("no")) {
                System.out.println("Please enter yes or no: "); //prints appropriate message
                userChoice = input.nextLine().trim(); //requests for input and removes leading and trailing whitespace
            }
            if (userChoice.equalsIgnoreCase("yes")) {
                System.out.println("There are 2 options available:\n1.Appointment\n2.Work"); //prints appropriate message
                userChoice = input.nextLine().trim(); //requests for input and removes leading and trailing whitespace

                while  (!userChoice.equalsIgnoreCase("appointment") &&
                        !userChoice.equalsIgnoreCase("work")) //the only events available are work & appointment
                {
                    System.out.println("This choice is not available. Please try again:"); //prints appropriate message
                    userChoice = input.nextLine().trim(); //requests for input and removes leading and trailing whitespace
                }
                if (userChoice.equalsIgnoreCase("appointment")) { //case where the user chose to add an appointment
                    //calls the newAppointmentToCalendar method to add a new appointment to the calendar
                    Appointment.newAppointmentToCalendar(calendar);
                } else { //case where the user chose to add a work
                    //calls the newWorkToCalendar method to add a new work to the calendar
                    Work.newWorkToCalendar(calendar);
                }
                myCalendar.saveCalendar(calendar, calendarName); //saves the calendar
                System.out.println("Event added successfully"); //prints appropriate message
                System.out.println("Do you want to add a new event? (yes/no)"); //prints appropriate message
                userChoice = input.nextLine().trim(); //requests for input and removes leading and trailing whitespace

                while (!userChoice.equalsIgnoreCase("yes") && !userChoice.equalsIgnoreCase("no")) {
                    System.out.println("Please enter yes or no: "); //prints appropriate message
                    userChoice = input.nextLine().trim(); //requests for input and removes leading and trailing whitespace
                }
            }
        } while (userChoice.equalsIgnoreCase("yes")); //continues so long as the answer is "yes"
    }

    private static void saveCalendar (Calendar calendar, String calendarName) { //saves the calendar

        //try-catch statement to pinpoint specific exceptions
        //https://www.ical4j.org/examples/output/
        try (FileOutputStream fileOutput = new FileOutputStream(calendarName)) { //creates a new object for fileOutput
            Calendar newCalendar = newCalendar(); //creates a new calendar to pass only the "useful" attributes from the old one
            //for-loop which filters out the "unwanted" properties of classes "Appointment" and "Work" respectively

            for (Object component : calendar.getComponents()) {
                if (component instanceof VEvent) { //if-statement that handles the details of class "Appointment"

                    //https://stackoverflow.com/questions/5306835/casting-objects-in-java
                    VEvent event = (VEvent) component; //casts the component as VEvent-type
                    VEvent newEvent = new VEvent(); //creates a new object for newEvent

                    for (Object property : event.getProperties()) { //parses each property of the appointment
                        if (property instanceof Summary || property instanceof Description ||
                                property instanceof DtStart || property instanceof Uid
                                || property instanceof Duration || property instanceof Status
                                || property instanceof Due) {

                            newEvent.getProperties().add(property); //adds the "wanted" property after every iteration
                        }
                    }
                    newCalendar.getComponents().add(newEvent); //adds the "useful" components of the new appointment to the new calendar
                } else if (component instanceof VToDo) { //if-statement that handles the details of class "Work"

                    VToDo event = (VToDo) component; //casts the component as VToDo-type
                    VToDo newEvent = new VToDo(); //creates a new object for VToDo

                    for (Object property : event.getProperties()) { //parses each property of the work
                        if (property instanceof Summary || property instanceof Description ||
                                property instanceof DtStart || property instanceof Uid
                                || property instanceof Duration || property instanceof Status
                                || property instanceof Due) {

                            newEvent.getProperties().add(property); //adds the "wanted" property after every iteration
                        }
                    }
                    newCalendar.getComponents().add(newEvent); //adds the "useful" components of the new work to the new calendar
                }
            }
            CalendarOutputter out = new CalendarOutputter(); //creates a new object for the CalendarOutputter
            out.output(newCalendar, fileOutput);

        } catch (IOException | ValidationException e) { //catches these specific exceptions
            System.out.println("Error saving calendar when parsing: " + e.getMessage()); //informs the user on the error which occurred
        }
    }

    //method that gets the date times based on the corresponding event type to be used for sorting
    public static DateTime getDateTime (Object event) {

        if (event instanceof VEvent) { //appointment case
            //casts the object-event to type-VEvent and adds the starting date to startDate
            Date startDate = ((VEvent) event).getStartDate().getDate();
            return new DateTime(startDate.getTime()); //returns the starting date as a DateTime-type
        } else if (event instanceof VToDo) { //work case
            //casts the object-event to type-VToDo and adds the starting date to dueDate
            Date dueDate = ((VToDo) event).getDue().getDate();
            return new DateTime(dueDate.getTime()); //returns the due date as a DateTime-type
        } else { //case where the event was neither type-VTodo nor V-Event
            System.out.println("The event is neither an appointment nor a work! Exiting..."); //prints an appropriate message
            throw new IllegalArgumentException(); //exits the program by throwing the appropriate exception
        }
    }

    //handles all cases that refer to time
    public static void calendarLists (String userChoice, ArrayList<Object> sortedEvents) {

        /* object declaration */
        TimeTeller teller = TimeService.getTeller(); //creates a TimeTeller instance with TimeService
        LocalDateTime timeNow = teller.now(); //object that gets the current time of the system date
        DayOfWeek currentDay = timeNow.getDayOfWeek(); //object that gets the current day of the system date
        YearMonth currentMonth = YearMonth.from(timeNow); //object that gets the current month of the system date

        /* switch statement that accepts the user's first argument and handles it accordingly */
        switch (userChoice) {
            /* case where the user requested for the events till the end of the day to be printed */
            case "day":

                LocalDateTime endOfDay = timeNow.toLocalDate().atTime(23, 59, 59); //puts in endOfDay the end of the system date
                for (Object event : sortedEvents) { //accesses every sorted event whether that is an appointment or a work
                    if (event instanceof VEvent) { //case where the event is an appointment
                        //https://stackoverflow.com/questions/19431234/converting-between-java-time-localdatetime-and-java-util-date
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the appointment
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VEvent) event).getStartDate().getDate().toInstant(), ZoneId.systemDefault());

                        if (dtStart.isAfter(timeNow) && (dtStart.isBefore(endOfDay))) { //case where the appointment is included in the current day
                            System.out.println("Appointment:"); //prints appropriate message
                            System.out.println(event); //prints the coded contents of the appointment
                        }
                    } else { //case where the event is a work
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the work
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VToDo) event).getDue().getDate().toInstant(), ZoneId.systemDefault());

                        if (dtStart.isAfter(timeNow) && (dtStart.isBefore(endOfDay))) { //case where the work is included in the current day
                            System.out.println("Work:"); //prints appropriate message
                            System.out.println(event); //prints the coded contents of the work
                        }
                    }
                }
                break; //exits the switch statement
            /* case where the user requested for the events till the end of the week to be printed */
            case "week":

                /* variable declaration */
                int daysUntilEndOfWeek = DayOfWeek.SUNDAY.getValue() - currentDay.getValue(); //contains the days until the end of the week

                //puts in endOfWeek the end of the system week
                LocalDateTime endOfWeek = timeNow.plusDays(daysUntilEndOfWeek).withHour(23).withMinute(59).withSecond(59);
                for (Object event : sortedEvents) { //accesses every sorted event whether that is an appointment or a work
                    if (event instanceof VEvent) { //case where the event is an appointment
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the appointment
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VEvent) event).getStartDate().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the appointment is included in the current week
                        if (dtStart.isAfter(timeNow) && (dtStart.isBefore(endOfWeek))) {
                            System.out.println("Appointment:"); //prints appropriate message
                            System.out.println(event); //prints the coded contents of the appointment
                        }
                    } else { //case where the event is a work
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the work
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VToDo) event).getDue().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the work is included in the current week
                        if (dtStart.isAfter(timeNow) && (dtStart.isBefore(endOfWeek))) {
                            System.out.println("Work:"); //prints appropriate message
                            System.out.println(event); //prints the coded contents of the work
                        }
                    }
                }
                break; //exits the switch statement
            /* case where the user requested for the events till the end of the month to be printed */
            case "month":

                LocalDate endOfMonth = currentMonth.atEndOfMonth(); //puts in endOfMonth the end of the system month
                for (Object event : sortedEvents) { //accesses every sorted event whether that is an appointment or a work
                    if (event instanceof VEvent) { //case where the event is an appointment
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the appointment
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VEvent) event).getStartDate().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the appointment is included in the current month
                        if (dtStart.isAfter(timeNow) && (dtStart.isBefore(endOfMonth.atTime(23, 59, 59)))) {
                            System.out.println("Appointment:"); //prints appropriate message
                            System.out.println(event); //prints the coded contents of the appointment
                        }
                    } else { //case where the event is a work
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the work
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VToDo) event).getDue().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the work is included in the current month
                        if (dtStart.isAfter(timeNow) && (dtStart.isBefore(endOfMonth.atTime(23, 59, 59)))) {
                            System.out.println("Work:"); //prints appropriate message
                            System.out.println(event); //prints the coded contents of the work
                        }
                    }
                }
                break; //exits the switch statement
            /* case where the user requested for the events from the start of the day till the user's current time to be printed */
            case "pastday":

                LocalDateTime startOfDay = timeNow.toLocalDate().atTime(00, 00, 00); //puts in startOfDay the start of the system date
                for (Object event : sortedEvents) { //accesses every sorted event whether that is an appointment or a work
                    if (event instanceof VEvent) { //case where the event is an appointment
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the appointment
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VEvent) event).getStartDate().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the appointment will take place between the start of the day and the user's current time
                        if (dtStart.isBefore(timeNow) && (dtStart.isAfter(startOfDay))) {
                            System.out.println("Appointment:"); //prints appropriate message
                            System.out.println(event); //prints the coded contents of the appointment
                        }
                    } else { //case where the event is a work
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the work
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VToDo) event).getDue().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the work will take place between the start of the day and the user's current time
                        if (dtStart.isBefore(timeNow) && (dtStart.isAfter(startOfDay))) {
                            System.out.println("Work:"); //prints appropriate message
                            System.out.println(event); //prints the coded contents of the work
                        }
                    }
                }
                break; //exits the switch statement
            /* case where the user requested for the events from the start of the week till the user's current time to be printed */
            case "pastweek":

                /* variable declaration */
                int daysUntilStartOfWeek = currentDay.getValue() - DayOfWeek.MONDAY.getValue(); //contains the days until the start of the week

                //puts in startOfWeek the start of the system week
                LocalDateTime startOfWeek = timeNow.minusDays(daysUntilStartOfWeek).withHour(00).withMinute(00).withSecond(00);
                for (Object event : sortedEvents) { //accesses every sorted event whether that is an appointment or a work
                    if (event instanceof VEvent) { //case where the event is an appointment
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the appointment
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VEvent) event).getStartDate().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the appointment will take place between the start of the week and the user's current time
                        if (dtStart.isBefore(timeNow) && (dtStart.isAfter(startOfWeek))) {
                            System.out.println("Appointment:"); //prints appropriate message
                            System.out.println(event); //prints the coded contents of the appointment
                        }
                    } else { //case where the event is a work
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the work
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VToDo) event).getDue().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the work will take place between the start of the day and the user's current time
                        if (dtStart.isBefore(timeNow) && (dtStart.isAfter(startOfWeek))) {
                            System.out.println("Work:"); //prints appropriate message
                            System.out.println(event); //prints the coded contents of the work
                        }
                    }
                }
                break; //exits the switch statement
            /* case where the user requested for the events from the start of the month till the user's current time to be printed */
            case "pastmonth":

                LocalDate startOfMonth = currentMonth.atDay(01); //puts in startOfMonth the start of the system month
                for (Object event : sortedEvents) { //accesses every sorted event whether that is an appointment or a work
                    if (event instanceof VEvent) { //case where the event is an appointment
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the appointment
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VEvent) event).getStartDate().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the appointment will take place between the start of the month and the user's current time
                        if (dtStart.isBefore(timeNow) && (dtStart.isAfter(startOfMonth.atTime(00, 00, 00)))) {
                            System.out.println("Appointment:"); //prints appropriate message
                            System.out.println(event); //prints the coded contents of the appointment
                        }
                    } else { //case where the event is a work
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the work
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VToDo) event).getDue().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the work will take place between the start of the month and the user's current time
                        if (dtStart.isBefore(timeNow) && (dtStart.isAfter(startOfMonth.atTime(00, 00, 00)))) {
                            System.out.println("Work:"); //prints appropriate message
                            System.out.println(event); //prints the coded contents of the work
                        }
                    }
                }
                break; //exits the switch statement
            /* case where the user requested for the works that have yet to be completed and of which the deadline hasn't passed to be printed */
            case "todo":

                for (Object event : sortedEvents) { //accesses every sorted event whether that is an appointment or a work
                    if (event instanceof VToDo) { //case where the event is a work
                        if (((VToDo) event).getStatus().getValue().equals("IN-PROCESS")) { //case where the work has yet to be completed

                            Date endDate = ((VToDo) event).getDue().getDate(); //contains the due date of the work
                            //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the work
                            LocalDateTime eventEndDate = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());

                            if (timeNow.isBefore(eventEndDate)) { //case where the work's deadline hasn't passed
                                System.out.println("Work:"); //prints appropriate message
                                System.out.println(event); //prints the coded contents of the work
                            }
                        }
                    }
                }
                break; //exits the switch statement
            /* case where the user requested for the works that have yet to be completed and of which the deadline has already passed to be printed */
            case "due":

                for (Object event : sortedEvents) { //accesses every sorted event whether that is an appointment or a work
                    if (event instanceof VToDo) { //case where the event is a work
                        if (((VToDo) event).getStatus().getValue().equals("IN-PROCESS")) { //case where the work has yet to be completed

                            Date endDate = ((VToDo) event).getDue().getDate(); //contains the due date of the work
                            //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the work
                            LocalDateTime eventEndDate = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());

                            if (timeNow.isAfter(eventEndDate)) { //case where the work's deadline has passed
                                System.out.println("Work:"); //prints appropriate message
                                System.out.println(event); //prints the coded contents of the work
                            }
                        }
                    }
                }
                break; //exits the switch statement
            default: //case where none of the previous conditions were met

                System.out.println("Invalid option! Exiting..."); //informs the user their input is invalid
                throw new IllegalArgumentException(); //Exits the program by throwing the appropriate exception
        }
        TimeService.stop(); //force stop
    }

    protected static DateTime dateTimeFormatChecker() {

        Scanner input = new Scanner(System.in); //creates a new scanner-object

        //variable declaration
        boolean flag;
        String date;
        DateTime userDateTime = null; //initialized at null

        do {
            date = input.nextLine().trim(); //requests for input and removes leading and trailing whitespace
            //try-catch statement to pinpoint a specific exception
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //checks whether the pattern provided is valid
                LocalDateTime localDateTime = LocalDateTime.parse(date, formatter); //creates the localDateTime-object
                //convert LocalDateTime to DateTime: (https://stackoverflow.com/questions/19431234/converting-between-java-time-localdatetime-and-java-util-date)
                userDateTime = new DateTime(java.util.Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant()));
                flag = false; //initialized at false

            } catch (DateTimeParseException e) { //catches the exception where the pattern was invalid
                System.out.println("Error! Please enter valid date and time formats."); //informs the user their input is invalid
                flag = true; //flag's value becomes true in order to exit the while-loop
            }
        } while (flag); //infinite loop until the flag's value changes from true to false

        return userDateTime; //returns
    }
}