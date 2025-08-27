package org.example;

/* imports */
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import gr.hua.dit.oop2.calendar.TimeService;
import gr.hua.dit.oop2.calendar.TimeTeller;
import gr.hua.dit.oop2.calendar.TimeListener;
import gr.hua.dit.oop2.calendar.TimeEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import static org.example.myCalendar.notificationCheck;

//handles all the GUI
public class CalendarFrames implements ActionListener {

    /* object declaration */
    JFrame calendarFrame; //JFrame-type frame
    JMenuBar menuBar; //JMenu-type menu bar
    JMenu file; //JMenu-type popup window
    JMenuItem create; //JMenuItem-type item(option for the user)
    JMenuItem load; //JMenuItem-type item(option for the user)
    JMenuItem save; //JMenuItem-type item(option for the user)
    JMenuItem exit; //JMenuItem-type item(option for the user)
    JLabel l2; //JLabel-type label
    JLabel l1; //JLabel-type label
    JLabel l3; //JLabel-type label
    JLabel l4; //JLabel-type label

    CalendarFrames() { //constructor

        calendarFrame = new JFrame(); //creates a new frame
        calendarFrame.setTitle("Calendar"); //sets the frame's title to "Calendar"
        //sets "EXIT_ON_CLOSE" as the default operation for when the user decides to close the window so the whole programs exits
        calendarFrame.setDefaultCloseOperation(JFrame. EXIT_ON_CLOSE);
        //https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
        calendarFrame.setLayout(new FlowLayout()); //sets the frame's layout
        calendarFrame.setSize(540,220); //sets the frame's size
        calendarFrame.setLocationRelativeTo(null); //centers the frame relative to the monitor's size
        calendarFrame.setResizable(false); //sets the frame to a fixed size

        ImageIcon logo = new ImageIcon("calendar_logo.png"); //creates an object that contains a calendar logo
        calendarFrame.setIconImage(logo.getImage()); //sets the calendar logo as the frame icon

        l1 = new JLabel("Welcome to your personal calendar!"); //creates a new label to welcome the user

        //places the label at the top center of the frame
        l1.setHorizontalAlignment(JLabel.CENTER);
        l1.setVerticalAlignment(JLabel.TOP);
        l1.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label

        l2 = new JLabel("Click on the top left to create/load a calendar!"); //creates a new label

        //places the label at the top center of the frame
        l2.setHorizontalAlignment(JLabel.CENTER);
        l2.setVerticalAlignment(JLabel.TOP);
        l2.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label

        //creates a new label
        l3 = new JLabel("Click on the top left to save the calendar.");

        //places the label at the top center of the frame
        l3.setHorizontalAlignment(JLabel.CENTER);
        l3.setVerticalAlignment(JLabel.TOP);
        l3.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label

        l4 = new JLabel(); //creates a new label
        l4.setFont(new Font("Calibri", Font.PLAIN, 18)); //customizes the label

        /* gets the time every set amount of seconds */
        TimeTeller teller = TimeService.getTeller(); //creates TimeTeller object to get the time
        teller.addTimeListener(new TimeListener() {
            @Override
            public void timeChanged(TimeEvent e) { //method that gets the user's current time
                //sets the user's current time to the label as text
                l4.setText("Current Time: " + e.getDateTime());
            }
        });
        TimeService.waitUntilTimeEnds();

        menuBar = new JMenuBar(); //creates a JMenuBar-type menu bar
        //creates a JMenu-type file to contain the following options for the user to choose from
        file = new JMenu("File");
        //creates JMenuItem-type objects
        create = new JMenuItem("Create");
        load = new JMenuItem("Load");
        save = new JMenuItem("Save");
        exit = new JMenuItem("Exit");

        //adds the action listeners to be notified when one of the following buttons is pressed
        create.addActionListener(this);
        load.addActionListener(this);
        save.addActionListener(this);
        exit.addActionListener(this);

        //adds all the options made available to the user
        file.add(create);
        file.add(load);
        file.add(save);
        file.add(exit);

        menuBar.add(file); //adds the 'file' to the menu bar

        calendarFrame.setJMenuBar(menuBar); //sets the menu bar to JMenuBar-type

        //adds the following labels to the frame
        calendarFrame.add(l1);
        calendarFrame.add(l2);
        calendarFrame.add(l4); //adds the current time label to the frame

        calendarFrame.setVisible(true); //sets the frame as visible
    }

    /* object declaration */
    /* creates JButton-type buttons */
    JButton newEvent;
    JButton editEvent;
    JButton changeStatus;
    JButton events;

    static Calendar calendar; //creates a static object for the calendar

    /* variable declaration */
    boolean flag1 = false; //flag variable initialized at false
    boolean flag2 = true; //flag variable initialized at true

    @Override
    public void actionPerformed(ActionEvent e) { //is notified when the user chooses an option

        if (e.getSource() == load) { //case where the user chose to load a calendar

            //creates a JFileChooser-type file chooser for when the user chooses a calendar
            JFileChooser fileChooser = new JFileChooser();
            //displays a dialog with the option of opening a calendar
            int response = fileChooser.showOpenDialog(null);

            if (response == JFileChooser.APPROVE_OPTION) { //case where chose a calendar with no arising problems

                //gets the absolute path of the chosen calendar
                String calendarName = fileChooser.getSelectedFile().getAbsolutePath();
                //calls the loadCalendar method to load the chosen calendar
                calendar = myCalendar.loadCalendar(calendarName);

                if (calendar != null) { //case where the user chose a parsable calendar

                    /* checks indefinitely for any time-related changes,
                       in order to notify the user for any upcoming events */
                    TimeTeller teller = TimeService.getTeller(); //creates TimeTeller object to get the time
                    teller.addTimeListener(new TimeListener() {
                        @Override
                        public void timeChanged(TimeEvent e) {
                            notificationCheck(calendar, e.getDateTime());
                        }
                    });
                    TimeService.waitUntilTimeEnds();

                    if (!flag1 && flag2) { //case where everything proceeded smoothly

                        calendarFrame.remove(l4); //removes the 'l4' label from the frame
                        calendarFrame.remove(l2); //removes the 'l2' label from the frame
                        calendarFrame.add(l3); //adds the 'l3' label to the frame

                        newEvent = new JButton("New Event"); //creates a 'New Event' JButton-type button
                        newEvent.setLayout(null); //declares that a Layout Manager won't be used in this case
                        newEvent.setSize(50, 50); //sets the button's size
                        newEvent.setVisible(true); //sets the button as visible
                        newEvent.addActionListener(this); //is notified when the 'New Event' button is pressed
                        calendarFrame.add(newEvent); //adds the button to the frame

                        editEvent = new JButton("Edit Event"); //creates a 'Edit Event' JButton-type button
                        editEvent.setLayout(null); //declares that a Layout Manager won't be used in this case
                        editEvent.setSize(50, 50); //sets the button's size
                        editEvent.setVisible(true); //sets the button as visible
                        editEvent.addActionListener(this); //is notified when the 'Edit Event' button is pressed
                        calendarFrame.add(editEvent); //adds the button to the frame

                        changeStatus = new JButton("Edit Status"); //creates a 'Edit Status' JButton-type button
                        changeStatus.setLayout(null); //declares that a Layout Manager won't be used in this case
                        changeStatus.setSize(50, 50); //sets the button's size
                        changeStatus.setVisible(true); //sets the button as visible
                        changeStatus.addActionListener(this); //is notified when the 'Edit Status' button is pressed
                        calendarFrame.add(changeStatus); //adds the button to the frame

                        events = new JButton("Events"); //creates a 'Events' JButton-type button
                        events.setLayout(null); //declares that a Layout Manager won't be used in this case
                        events.setSize(50, 50); //sets the button's size
                        events.setVisible(true); //sets the button as visible
                        events.addActionListener(this); //is notified when the 'Events' button is pressed
                        calendarFrame.add(events); //adds the button to the frame

                        calendarFrame.add(l4); //adds the current time label to the frame
                        calendarFrame.pack(); //sizes the frame appropriately
                        calendarFrame.setSize(450, 220); //sets the frame's size

                        //signifies that all previous actions worked properly
                        flag1 = true;
                        flag2 = false;
                    }
                } else { //case where the user didn't choose a parsable calendar

                    flag1 = false; //flag's value becomes false
                    flag2 = true; //flag's value becomes true

                    JFrame frame = new JFrame(); //creates a new JFrame-type frame
                    frame.setTitle("Calendar"); //sets the frame's title to Calendar
                    ImageIcon logo = new ImageIcon("calendar_logo.png"); //creates an object that contains a calendar logo
                    frame.setIconImage(logo.getImage()); //sets the calendar logo as the frame icon
                    frame.setResizable(false); //sets the frame to a fixed size
                    frame.setSize(385, 115); //sets the frame's size
                    frame.setLayout(new FlowLayout()); //sets the frame's layout
                    frame.setLocationRelativeTo(null); //centers the frame relative to the monitor's size

                    JLabel message1 = new JLabel("There was an error parsing the file."); //displays an error message
                    //places the label at the top center of the frame
                    message1.setHorizontalAlignment(JLabel.CENTER);
                    message1.setVerticalAlignment(JLabel.TOP);
                    message1.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label
                    frame.add(message1); //adds the message to the frame
                    JLabel message2 = new JLabel("Please try again."); //urges the user to try again

                    //places the label at the top center of the frame
                    message2.setHorizontalAlignment(JLabel.CENTER);
                    message2.setVerticalAlignment(JLabel.BOTTOM);
                    message2.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label
                    frame.add(message2); //adds the message to the frame
                    frame.setVisible(true); //sets the frame as visible

                    if (!flag1) {

                        calendarFrame.setLayout(new FlowLayout()); //sets the frame's layout
                        calendarFrame.setResizable(false); //sets the frame to a fixed size
                        calendarFrame.setLocationRelativeTo(null); //centers the frame relative to the monitor's size

                        /* removes all the buttons from the label */
                        calendarFrame.remove(newEvent);
                        calendarFrame.remove(editEvent);
                        calendarFrame.remove(changeStatus);
                        calendarFrame.remove(events);

                        calendarFrame.add(l2); //adds the label to the frame
                        calendarFrame.pack(); //sizes the frame appropriately
                        flag2 = true; //flag's value becomes true
                    }
                }
            }
        } else if (e.getSource() == create) { //case where the user chose to create a calendar

            calendar = new Calendar(); //creates the new calendar-object
            calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN")); //adds the ProdId
            calendar.getProperties().add(Version.VERSION_2_0); //adds the version
            calendar.getProperties().add(CalScale.GREGORIAN); //adds the calculator scale

            /* checks indefinitely for any time-related changes,
                       in order to notify the user for any upcoming events */
            TimeTeller teller = TimeService.getTeller();
            teller.addTimeListener (new TimeListener() {
                @Override
                public void timeChanged(TimeEvent e) {
                    notificationCheck(calendar, e.getDateTime());
                }
            });
            TimeService.waitUntilTimeEnds();

            if (!flag1 && flag2) {

                calendarFrame.remove(l2); //removes the 'l2' label from the frame
                calendarFrame.remove(l4); //removes the 'l4' label from the frame
                calendarFrame.add(l3); //adds the 'l3' label to the frame

                newEvent = new JButton("New Event"); //creates a 'New Event' JButton-type button
                newEvent.setLayout(null); //declares that a Layout Manager won't be used in this case
                newEvent.setSize(50, 50); //sets the button's size
                newEvent.setVisible(true); //sets the button as visible
                newEvent.addActionListener(this); //is notified when the 'New Event' button is pressed
                calendarFrame.add(newEvent); //adds the button to the frame

                editEvent = new JButton("Edit Event"); //creates a 'Edit Event' JButton-type button
                editEvent.setLayout(null); //declares that a Layout Manager won't be used in this case
                editEvent.setSize(50, 50); //sets the button's size
                editEvent.setVisible(true); //sets the button as visible
                editEvent.addActionListener(this); //is notified when the 'Edit Event' button is pressed
                calendarFrame.add(editEvent); //adds the button to the frame

                changeStatus = new JButton("Edit Status"); //creates a 'Edit Status' JButton-type button
                changeStatus.setLayout(null); //declares that a Layout Manager won't be used in this case
                changeStatus.setSize(50, 50); //sets the button's size
                changeStatus.setVisible(true); //sets the button as visible
                changeStatus.addActionListener(this); //is notified when the 'Edit Status' button is pressed
                calendarFrame.add(changeStatus); //adds the button to the frame

                events = new JButton("Events"); //creates a 'Events' JButton-type button
                events.setLayout(null); //declares that a Layout Manager won't be used in this case
                events.setSize(50, 50); //sets the button's size
                events.setVisible(true); //sets the button as visible
                events.addActionListener(this); //is notified when the 'Events' button is pressed
                calendarFrame.add(events); //adds the button to the frame

                calendarFrame.add(l4); //adds the current time label to the frame
                calendarFrame.pack(); //sizes the frame appropriately
                calendarFrame.setSize(450, 220); //sets the frame's size

                flag1 = true; //flag's value becomes true
                flag2 = false; //flag's value becomes false
            }
        } else if (e.getSource() == newEvent) { //case where the user chose to add a new event
            new NewEventFrame(); //calls the NewEventFrame method to add a new event

        } else if (e.getSource() == events) { //case where the user chose for a particular set of events to be displayed
            new SortedEventsFrame(); //calls the SortedEventsFrame method to display a particular set of events

        } else if (e.getSource() == editEvent) { //case where the user chose to edit an event's details
            new EditEventFrame(); //calls the EditEventFrame method to edit an event's characteristics

        } else if (e.getSource() == changeStatus) { //case where the user chose to change a work's status
            new EditStatusFrame(); //calls the EditStatusFrame method to edit a work's status

        } else if (e.getSource() == save) { //case where the user chose to save any changes made
            if (!flag1 && flag2) {

                JFrame frame = new JFrame(); //creates a JFrame-type frame
                frame.setTitle("Calendar"); //sets the frame's title to 'Calendar'
                ImageIcon logo = new ImageIcon("calendar_logo.png"); //creates an object that contains a calendar logo
                frame.setIconImage(logo.getImage()); //sets the calendar logo as the frame icon
                frame.setResizable(false); //sets the frame to a fixed size
                frame.setSize(385, 115); //sets the frame's size
                frame.setLayout(new FlowLayout()); //sets the frame's layout
                frame.setLocationRelativeTo(null); //centers the frame relative to the monitor's size

                //creates a new message
                JLabel message1 = new JLabel("Create or load a calendar first.");
                message1.setHorizontalAlignment(JLabel.CENTER); //centers the label
                message1.setVerticalAlignment(JLabel.TOP); //places the label at the top
                message1.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label
                frame.add(message1); //adds the message to the frame

                //creates a new message
                JLabel message2 = new JLabel("Then save it.");
                message2.setHorizontalAlignment(JLabel.CENTER); //centers the label
                message2.setVerticalAlignment(JLabel.BOTTOM); //places the label at the bottom
                message2.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label
                frame.add(message2); //adds the message to the frame
                frame.setVisible(true); //sets the frame as visible

            } else {

                //creates a JFileChooser-type file chooser for when the user chooses a calendar
                JFileChooser fileChooser = new JFileChooser();
                //displays a dialog with the option of saving a calendar
                int response = fileChooser.showSaveDialog(null);

                if (response == JFileChooser.APPROVE_OPTION) { //case where the user decided to save the calendar

                    //gets the absolute path of the selected calendar it puts it in the 'calendarName' String
                    String calendarName = fileChooser.getSelectedFile().getAbsolutePath();

                    /* try-catch statement */
                    try (FileOutputStream fileOutput = new FileOutputStream(calendarName)) {
                        String calendarString = calendar.toString(); //converts the calendar to a string
                        fileOutput.write(calendarString.getBytes()); //writes the string to the file
                    } catch (IOException f) { //catches the Input/Output Exception
                        System.out.println("Error saving calendar: " + f.getMessage()); //prints appropriate message
                    }
                }
            }
        } else if (e.getSource() == exit) { //case where the user chose to exit the program
            System.exit(0); //exits the program
        }
    }
}
