package org.example;

/* imports */
import java.awt.*;
import java.io.FileInputStream;
import java.time.*;
import java.util.*;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VToDo;
import java.io.IOException;
import java.util.Date;
import net.fortuna.ical4j.model.DateTime;
import gr.hua.dit.oop2.calendar.TimeService;
import gr.hua.dit.oop2.calendar.TimeTeller;
import net.fortuna.ical4j.util.CompatibilityHints;
import javax.swing.*;
import static javax.swing.JOptionPane.showMessageDialog;

/* class that handles everything that has to do with the calendar itself */
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
            return null;
            //throw new NullPointerException(); //Exits the program by throwing the appropriate exception
        }
    }

    //method that gets the date times based on the corresponding event type to be used for sorting
    public static DateTime getDateTime (Object event) {

        if (event instanceof VEvent) { //appointment case
            //casts the object-event to type-VEvent and adds the starting date to startDate
            if (((VEvent) event).getStartDate() != null) {
                Date startDate = ((VEvent) event).getStartDate().getDate();
                return new DateTime(startDate.getTime()); //returns the starting date as a DateTime-type
            }
        } else if (event instanceof VToDo) { //work case
            //casts the object-event to type-VToDo and adds the starting date to dueDate
            if (((VToDo) event).getDue() != null) {
                Date dueDate = ((VToDo) event).getDue().getDate();
                return new DateTime(dueDate.getTime()); //returns the due date as a DateTime-type
            }
        } else { //case where the event was neither type-VTodo nor V-Event
            System.out.println("The event is neither an appointment nor a work! Exiting..."); //prints an appropriate message
            throw new IllegalArgumentException(); //throws the appropriate exception
        }
        return null; //exits the program
    }

    private static final Set<String> procEvents = new HashSet<>(); //stores the UIDs of processed events

    //notifies the user on upcoming events
    public static void notificationCheck(Calendar calendar, LocalDateTime timeNow) {

        for (Object component : calendar.getComponents()) {  //accesses every component that belongs to the calendar

            if (component instanceof VEvent) { //case where the event is an appointment
                VEvent vevent = (VEvent) component; //casts the component to VEvent

                if (vevent.getStartDate() != null) { //case where a 'Start Date' exists
                    String eventId = vevent.getUid().getValue(); //puts the appointment's UID to the eventID String

                    //https://www.w3schools.com/java/ref_string_contains.asp
                    if (procEvents.contains(eventId)) { //case where the processed appointment has a UID
                        continue; //"breaks" the current iteration and continues on with the next event
                    }
                    //https://www.geeksforgeeks.org/localtime-ofinstant-method-in-java-with-examples/
                    //https://stackoverflow.com/questions/76329767/how-do-i-convert-a-java-date-into-an-instant-for-a-given-timezone
                    //gets the 'Start Date' and puts it in dtStart
                    LocalDateTime dtStart = LocalDateTime.ofInstant(vevent.getStartDate().getDate().toInstant(), ZoneId.systemDefault());
                    //https://www.geeksforgeeks.org/period-between-method-in-java-with-examples/
                    Duration duration = Duration.between(timeNow, dtStart); //checks whether the duration is between the current time and 'Date Start'

                    //case where the appointment is going to take place 10 minutes or less
                    if (!duration.isNegative() && duration.toMinutes() <= 10) {
                        //https://stackoverflow.com/questions/9119481/how-to-present-a-simple-alert-message-in-java
                        //prints appropriate message with the appointment's name
                        showMessageDialog(null, "Appointment forthcoming in less than 10 minutes\n" + "Title: " + vevent.getSummary().getValue());
                        procEvents.add(eventId); //since the appointment has now been processed, it is put in the String ProcEvents
                    }
                }
            } else if (component instanceof VToDo) { //case where the event is a work
                VToDo vtodo = (VToDo) component; //casts the component to VToDo

                if (vtodo.getDue() != null) { //case where a 'Due' exists
                    String eventId = vtodo.getUid().getValue(); //puts the work's UID to the eventID String

                    if (procEvents.contains(eventId)) { //case where the processed work has a UID
                        continue; //"breaks" the current iteration and continues on with the next event
                    }
                    //gets the 'Due' and puts it in due
                    LocalDateTime due = LocalDateTime.ofInstant(vtodo.getDue().getDate().toInstant(), ZoneId.systemDefault());
                    Duration duration = Duration.between(timeNow, due); //checks whether the due is between the current time and 'Due'

                    //case where the work is going to take place 10 minutes or less
                    if (!duration.isNegative() && duration.toMinutes() <= 10) {
                        //prints appropriate message with the work's name
                        showMessageDialog(null, "Work forthcoming in less than 10 minutes\n" + "Title: " + vtodo.getSummary().getValue());
                        procEvents.add(eventId); //since the work has now been processed, it is put in the String ProcEvents
                    }
                }
            }
        }
    }

    //handles all cases that refer to time
    public static void calendarLists (String userChoice, Calendar calendar, JPanel panel) {

        /* object declaration */
        TimeTeller teller = TimeService.getTeller(); //creates a TimeTeller instance with TimeService
        LocalDateTime timeNow = teller.now(); //object that gets the current time of the system date
        DayOfWeek currentDay = timeNow.getDayOfWeek(); //object that gets the current day of the system date
        YearMonth currentMonth = YearMonth.from(timeNow); //object that gets the current month of the system date

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
        } catch (NullPointerException e) {  //catches the specific exception
            System.out.println("Failed to sort the events."); //prints appropriate message
        }

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
                            JLabel label0 = new JLabel("Appointment: "); //creates a label for the appointment
                            label0.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label
                            //creates a label containing the appointment's title
                            JLabel label1 = new JLabel("Title: " + ((VEvent) event).getSummary().getValue());
                            label1.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label1.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the appointment's description
                            JLabel label2 = new JLabel("Description: " + ((VEvent) event).getDescription().getValue());
                            label2.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label2.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the appointment's start date
                            JLabel label3 = new JLabel("Start Date: " + ((VEvent) event).getStartDate().getValue());
                            label3.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label3.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label

                            /* adds all labels to the panel */
                            panel.add(label0);
                            panel.add(label1);
                            panel.add(label2);
                            panel.add(label3);

                            if (((VEvent) event).getDuration() != null) { //case where a 'duration' is included in the appointment
                                //creates a label containing the appointment's end date
                                JLabel label4 = new JLabel("Date End (Start Date + Duration): " + ((VEvent) event).getEndDate().getValue());
                                label4.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label4.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label4); //adds the label to the panel
                            }
                        }
                    } else { //case where the event is a work
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the work
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VToDo) event).getDue().getDate().toInstant(), ZoneId.systemDefault());

                        if (dtStart.isAfter(timeNow) && (dtStart.isBefore(endOfDay))) { //case where the work is included in the current day
                            JLabel label0 = new JLabel("Work: "); //creates a label for the work
                            label0.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label
                            //creates a label containing the work's title
                            JLabel label1 = new JLabel("Title: " + ((VToDo) event).getSummary().getValue());
                            label1.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label1.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the work's description
                            JLabel label2 = new JLabel("Description: " + ((VToDo) event).getDescription().getValue());
                            label2.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label2.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label

                            /* adds all labels to the panel */
                            panel.add(label0);
                            panel.add(label1);
                            panel.add(label2);

                            if (((VToDo) event).getDue() != null) { //case where a 'due' is included in the work
                                //creates a label containing the work's due
                                JLabel label3 = new JLabel("Due: " + ((VToDo) event).getDue().getValue());
                                label3.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label3.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label3); //adds the label to the panel
                            }
                            if (((VToDo) event).getStatus() != null) { //case where a 'status' is included in the work
                                //creates a label containing the work's status
                                JLabel label4 = new JLabel("Status: " + ((VToDo) event).getStatus().getValue());
                                label4.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label4.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label4); //adds the label to the panel
                            }
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
                            JLabel label0 = new JLabel("Appointment: "); //creates a label for the appointment
                            label0.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label
                            //creates a label containing the appointment's title
                            JLabel label1 = new JLabel("Title: " + ((VEvent) event).getSummary().getValue());
                            label1.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label1.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the appointment's description
                            JLabel label2 = new JLabel("Description: " + ((VEvent) event).getDescription().getValue());
                            label2.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label2.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the appointment's start date
                            JLabel label3 = new JLabel("Start Date: " + ((VEvent) event).getStartDate().getValue());
                            label3.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label3.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label

                            /* adds all labels to the panel */
                            panel.add(label0);
                            panel.add(label1);
                            panel.add(label2);
                            panel.add(label3);

                            if (((VEvent) event).getDuration() != null) { //case where a 'duration' is included in the appointment
                                //creates a label containing the appointment's end date
                                JLabel label4 = new JLabel("Date End (Start Date + Duration): " + ((VEvent) event).getEndDate().getValue());
                                label4.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label4.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label4); //adds the label to the panel
                            }
                        }
                    } else { //case where the event is a work
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the work
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VToDo) event).getDue().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the work is included in the current week
                        if (dtStart.isAfter(timeNow) && (dtStart.isBefore(endOfWeek))) {
                            JLabel label0 = new JLabel("Work: "); //creates a label for the work
                            label0.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label
                            //creates a label containing the work's title
                            JLabel label1 = new JLabel("Title: " + ((VToDo) event).getSummary().getValue());
                            label1.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label1.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the work's description
                            JLabel label2 = new JLabel("Description: " + ((VToDo) event).getDescription().getValue());
                            label2.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label2.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label

                            /* adds all labels to the panel */
                            panel.add(label0);
                            panel.add(label1);
                            panel.add(label2);

                            if (((VToDo) event).getDue() != null) { //case where a 'due' is included in the work
                                //creates a label containing the work's due
                                JLabel label3 = new JLabel("Due: " + ((VToDo) event).getDue().getValue());
                                label3.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label3.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label3); //adds the label to the panel
                            }
                            if (((VToDo) event).getStatus() != null) { //case where a 'status' is included in the work
                                //creates a label containing the work's status
                                JLabel label4 = new JLabel("Status: " + ((VToDo) event).getStatus().getValue());
                                label4.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label4.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label4); //adds the label to the panel
                            }
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
                            JLabel label0 = new JLabel("Appointment: "); //creates a label for the appointment
                            label0.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label
                            //creates a label containing the appointment's title
                            JLabel label1 = new JLabel("Title: " + ((VEvent) event).getSummary().getValue());
                            label1.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label1.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the appointment's description
                            JLabel label2 = new JLabel("Description: " + ((VEvent) event).getDescription().getValue());
                            label2.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label2.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the appointment's start date
                            JLabel label3 = new JLabel("Start Date: " + ((VEvent) event).getStartDate().getValue());
                            label3.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label3.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label

                            /* adds all labels to the panel */
                            panel.add(label0);
                            panel.add(label1);
                            panel.add(label2);
                            panel.add(label3);

                            if (((VEvent) event).getDuration() != null) { //case where a 'duration' is included in the appointment
                                //creates a label containing the appointment's end date
                                JLabel label4 = new JLabel("Date End (Start Date + Duration): " + ((VEvent) event).getEndDate().getValue());
                                label4.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label4.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label4); //adds the label to the panel
                            }
                        }
                    } else { //case where the event is a work
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the work
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VToDo) event).getDue().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the work is included in the current month
                        if (dtStart.isAfter(timeNow) && (dtStart.isBefore(endOfMonth.atTime(23, 59, 59)))) {
                            JLabel label0 = new JLabel("Work: "); //creates a label for the work
                            label0.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label
                            //creates a label containing the work's title
                            JLabel label1 = new JLabel("Title: " + ((VToDo) event).getSummary().getValue());
                            label1.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label1.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the work's description
                            JLabel label2 = new JLabel("Description: " + ((VToDo) event).getDescription().getValue());
                            label2.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label2.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label

                            /* adds all labels to the panel */
                            panel.add(label0);
                            panel.add(label1);
                            panel.add(label2);

                            if (((VToDo) event).getDue() != null) { //case where a 'due' is included in the work
                                //creates a label containing the work's due
                                JLabel label3 = new JLabel("Due: " + ((VToDo) event).getDue().getValue());
                                label3.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label3.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label3); //adds the label to the panel
                            }
                            if (((VToDo) event).getStatus() != null) { //case where a 'status' is included in the work
                                //creates a label containing the work's status
                                JLabel label4 = new JLabel("Status: " + ((VToDo) event).getStatus().getValue());
                                label4.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label4.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label4); //adds the label to the panel
                            }
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
                            JLabel label0 = new JLabel("Appointment: "); //creates a label for the appointment
                            label0.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label
                            //creates a label containing the appointment's title
                            JLabel label1 = new JLabel("Title: " + ((VEvent) event).getSummary().getValue());
                            label1.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label1.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the appointment's description
                            JLabel label2 = new JLabel("Description: " + ((VEvent) event).getDescription().getValue());
                            label2.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label2.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the appointment's start date
                            JLabel label3 = new JLabel("Start Date: " + ((VEvent) event).getStartDate().getValue());
                            label3.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label3.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label

                            /* adds all labels to the panel */
                            panel.add(label0);
                            panel.add(label1);
                            panel.add(label2);
                            panel.add(label3);

                            if (((VEvent) event).getDuration() != null) { //case where a 'duration' is included in the appointment
                                //creates a label containing the appointment's end date
                                JLabel label4 = new JLabel("Date End (Start Date + Duration): " + ((VEvent) event).getEndDate().getValue());
                                label4.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label4.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label4); //adds the label to the panel
                            }
                        }
                    } else { //case where the event is a work
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the work
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VToDo) event).getDue().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the work will take place between the start of the day and the user's current time
                        if (dtStart.isBefore(timeNow) && (dtStart.isAfter(startOfDay))) {
                            JLabel label0 = new JLabel("Work: "); //creates a label for the work
                            label0.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label
                            //creates a label containing the work's title
                            JLabel label1 = new JLabel("Title: " + ((VToDo) event).getSummary().getValue());
                            label1.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label1.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the work's description
                            JLabel label2 = new JLabel("Description: " + ((VToDo) event).getDescription().getValue());
                            label2.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label2.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label

                            /* adds all labels to the panel */
                            panel.add(label0);
                            panel.add(label1);
                            panel.add(label2);

                            if (((VToDo) event).getDue() != null) { //case where a 'due' is included in the work
                                //creates a label containing the work's due
                                JLabel label3 = new JLabel("Due: " + ((VToDo) event).getDue().getValue());
                                label3.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label3.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label3); //adds the label to the panel
                            }
                            if (((VToDo) event).getStatus() != null) { //case where a 'status' is included in the work
                                //creates a label containing the work's status
                                JLabel label4 = new JLabel("Status: " + ((VToDo) event).getStatus().getValue());
                                label4.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label4.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label4); //adds the label to the panel
                            }
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
                            JLabel label0 = new JLabel("Appointment: "); //creates a label for the appointment
                            label0.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label
                            //creates a label containing the appointment's title
                            JLabel label1 = new JLabel("Title: " + ((VEvent) event).getSummary().getValue());
                            label1.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label1.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the appointment's description
                            JLabel label2 = new JLabel("Description: " + ((VEvent) event).getDescription().getValue());
                            label2.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label2.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the appointment's start date
                            JLabel label3 = new JLabel("Start Date: " + ((VEvent) event).getStartDate().getValue());
                            label3.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label3.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label

                            /* adds all labels to the panel */
                            panel.add(label0);
                            panel.add(label1);
                            panel.add(label2);
                            panel.add(label3);

                            if (((VEvent) event).getDuration() != null) { //case where a 'duration' is included in the appointment
                                //creates a label containing the appointment's end date
                                JLabel label4 = new JLabel("Date End (Start Date + Duration): " + ((VEvent) event).getEndDate().getValue());
                                label4.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label4.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label4); //adds the label to the panel
                            }
                        }
                    } else { //case where the event is a work
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the work
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VToDo) event).getDue().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the work will take place between the start of the day and the user's current time
                        if (dtStart.isBefore(timeNow) && (dtStart.isAfter(startOfWeek))) {
                            JLabel label0 = new JLabel("Work: "); //creates a label for the work
                            label0.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label
                            //creates a label containing the work's title
                            JLabel label1 = new JLabel("Title: " + ((VToDo) event).getSummary().getValue());
                            label1.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label1.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the work's description
                            JLabel label2 = new JLabel("Description: " + ((VToDo) event).getDescription().getValue());
                            label2.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label2.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label

                            /* adds all labels to the panel */
                            panel.add(label0);
                            panel.add(label1);
                            panel.add(label2);

                            if (((VToDo) event).getDue() != null) { //case where a 'due' is included in the work
                                //creates a label containing the work's due
                                JLabel label3 = new JLabel("Due: " + ((VToDo) event).getDue().getValue());
                                label3.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label3.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label3); //adds the label to the panel
                            }
                            if (((VToDo) event).getStatus() != null) { //case where a 'status' is included in the work
                                //creates a label containing the work's status
                                JLabel label4 = new JLabel("Status: " + ((VToDo) event).getStatus().getValue());
                                label4.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label4.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label4); //adds the label to the panel
                            }
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
                            JLabel label0 = new JLabel("Appointment: "); //creates a label for the appointment
                            label0.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label
                            //creates a label containing the appointment's title
                            JLabel label1 = new JLabel("Title: " + ((VEvent) event).getSummary().getValue());
                            label1.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label1.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the appointment's description
                            JLabel label2 = new JLabel("Description: " + ((VEvent) event).getDescription().getValue());
                            label2.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label2.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the appointment's start date
                            JLabel label3 = new JLabel("Start Date: " + ((VEvent) event).getStartDate().getValue());
                            label3.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label3.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label

                            /* adds all labels to the panel */
                            panel.add(label0);
                            panel.add(label1);
                            panel.add(label2);
                            panel.add(label3);

                            if (((VEvent) event).getDuration() != null) { //case where a 'duration' is included in the appointment
                                //creates a label containing the appointment's end date
                                JLabel label4 = new JLabel("Date End (Start Date + Duration): " + ((VEvent) event).getEndDate().getValue());
                                label4.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label4.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label4); //adds the label to the panel
                            }
                        }
                    } else { //case where the event is a work
                        //accepts different times regardless of time-zone and does a conversion form DateTime to LocalDateTime for the work
                        LocalDateTime dtStart = LocalDateTime.ofInstant(((VToDo) event).getDue().getDate().toInstant(), ZoneId.systemDefault());

                        //case where the work will take place between the start of the month and the user's current time
                        if (dtStart.isBefore(timeNow) && (dtStart.isAfter(startOfMonth.atTime(00, 00, 00)))) {
                            JLabel label0 = new JLabel("Work: "); //creates a label for the work
                            label0.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label
                            //creates a label containing the work's title
                            JLabel label1 = new JLabel("Title: " + ((VToDo) event).getSummary().getValue());
                            label1.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label1.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                            //creates a label containing the work's description
                            JLabel label2 = new JLabel("Description: " + ((VToDo) event).getDescription().getValue());
                            label2.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                            label2.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label

                            /* adds all labels to the panel */
                            panel.add(label0);
                            panel.add(label1);
                            panel.add(label2);

                            if (((VToDo) event).getDue() != null) { //case where a 'due' is included in the work
                                //creates a label containing the work's due
                                JLabel label3 = new JLabel("Due: " + ((VToDo) event).getDue().getValue());
                                label3.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label3.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label3); //adds the label to the panel
                            }
                            if (((VToDo) event).getStatus() != null) { //case where a 'status' is included in the work
                                //creates a label containing the work's status
                                JLabel label4 = new JLabel("Status: " + ((VToDo) event).getStatus().getValue());
                                label4.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label4.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                panel.add(label4); //adds the label to the panel
                            }
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
                                JLabel label0 = new JLabel("Work: "); //creates a label for the work
                                label0.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label
                                //creates a label containing the work's title
                                JLabel label1 = new JLabel("Title: " + ((VToDo) event).getSummary().getValue());
                                label1.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label1.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                //creates a label containing the work's description
                                JLabel label2 = new JLabel("Description: " + ((VToDo) event).getDescription().getValue());
                                label2.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label2.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label

                                /* adds all labels to the panel */
                                panel.add(label0);
                                panel.add(label1);
                                panel.add(label2);

                                if (((VToDo) event).getDue() != null) { //case where a 'due' is included in the work
                                    //creates a label containing the work's due
                                    JLabel label3 = new JLabel("Due: " + ((VToDo) event).getDue().getValue());
                                    label3.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                    label3.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                    panel.add(label3); //adds the label to the panel
                                }
                                if (((VToDo) event).getStatus() != null) { //case where a 'status' is included in the work
                                    //creates a label containing the work's status
                                    JLabel label4 = new JLabel("Status: " + ((VToDo) event).getStatus().getValue());
                                    label4.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                    label4.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                    panel.add(label4); //adds the label to the panel
                                }
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
                                JLabel label0 = new JLabel("Work: "); //creates a label for the work
                                label0.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label
                                //creates a label containing the work's title
                                JLabel label1 = new JLabel("Title: " + ((VToDo) event).getSummary().getValue());
                                label1.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label1.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                //creates a label containing the work's description
                                JLabel label2 = new JLabel("Description: " + ((VToDo) event).getDescription().getValue());
                                label2.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                label2.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label

                                /* adds all labels to the panel */
                                panel.add(label0);
                                panel.add(label1);
                                panel.add(label2);

                                if (((VToDo) event).getDue() != null) { //case where a 'due' is included in the work
                                    //creates a label containing the work's due
                                    JLabel label3 = new JLabel("Due: " + ((VToDo) event).getDue().getValue());
                                    label3.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                    label3.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                    panel.add(label3); //adds the label to the panel
                                }
                                if (((VToDo) event).getStatus() != null) { //case where a 'status' is included in the work
                                    //creates a label containing the work's status
                                    JLabel label4 = new JLabel("Status: " + ((VToDo) event).getStatus().getValue());
                                    label4.setVerticalAlignment(JLabel.TOP); //positions the label at the top
                                    label4.setFont(new Font("Calibri", Font.PLAIN, 16)); //customizes the label
                                    panel.add(label4); //adds the label to the panel
                                }
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
}
