package org.example;

/* imports */
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

//handles the GUI-part of the creation of new events
public class NewEventFrame extends CalendarFrames implements ActionListener {

    /* object declaration */
    JFrame frame; //JFrame-type frame
    JRadioButton appointment; //JRadioButton-type button
    JRadioButton work; //JRadioButton-type button
    ButtonGroup group; //ButtonGroup-type group
    JLabel label; //JLabel-type label

    NewEventFrame() { //constructor

        calendarFrame.setVisible(false); //sets the frame as invisible
        frame = new JFrame(); //creates a new frame
        frame.setTitle("Calendar"); //sets the frame's title
        //creates an object that contains a calendar logo
        ImageIcon logo = new ImageIcon("calendar_logo.png");
        frame.setIconImage(logo.getImage()); //sets the calendar logo as the frame icon
        frame.setResizable(false); //sets the frame to a fixed size
        frame.setSize(600, 110); //sets the frame's size
        frame.setLayout(new FlowLayout()); //sets the frame's layout
        frame.setLocationRelativeTo(null); //centers the frame
        frame.setVisible(true); //sets the frame as visible
        //creates a new label which prints an informative message
        label = new JLabel("Select either an appointment or work to add to calendar");
        label.setHorizontalAlignment(JLabel.CENTER); //centers the label
        label.setVerticalAlignment(JLabel.TOP); //places the label at the top of the frame
        label.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label
        frame.add(label); //adds the label to the frame
        appointment = new JRadioButton("Appointment"); //creates a button for 'Appointment'
        work = new JRadioButton("Work"); //creates a button for 'Work'
        group = new ButtonGroup(); //creates a group intended for the buttons 'Appointment' and 'Work'

        //https://www.javatpoint.com/java-actionlistener
        appointment.addActionListener(this); //is notified the moment the 'Appointment' button is pressed
        work.addActionListener(this); //is notified the moment the 'Work' button is pressed

        group.add(appointment); //adds the appointment to the ButtonGroup-type group
        group.add(work); //adds the work to the ButtonGroup-type group
        frame.add(appointment); //adds the appointment to the frame
        frame.add(work); //adds the work to the frame
    }

    /* object declaration */
    JButton AppointmentButton; //JButton-type button
    JButton WorkButton; //JButton-type button
    JTextField titleText; //JTextField-type text
    JLabel titleLabel; //JLabel-type label
    JTextField descriptionText; //JTextField-type text
    JLabel descriptionLabel; //JLabel-type label
    JTextField startingDateText; //JTextField-type text
    JLabel startingDateLabel; //JLabel-type label
    JTextField durationText; //JTextField-type text
    JLabel durationLabel; //JLabel-type label
    JTextField deadlineText; //JTextField-type text
    JLabel deadlineLabel; //JLabel-type label
    JTextField statusText; //JTextField-type text
    JLabel statusLabel; //JLabel-type label
    JFrame errFrame; //JFrame-type frame
    JLabel errLabel0; //JLabel-type label
    JLabel errLabel1; //JLabel-type label
    JLabel errLabel2; //JLabel-type label

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == appointment) { //appointment case

            frame.setLayout(new FlowLayout()); //sets 'FlowLayout' as the layout to be used for this frame
            frame.setResizable(false); //sets the frame to a fixed size
            frame.setLocationRelativeTo(null); //centers the frame
            frame.setVisible(true); //sets the frame as visible

            /* removes all that existed previously to redesign the frame */
            frame.remove(label);
            frame.remove(appointment);
            frame.remove(work);

            //smoothens the frame's appearance
            JLabel spacetext = new JLabel("                                              " +
                    "                                          ");
            titleText = new JTextField(); //creates new title text field
            titleText.setPreferredSize(new Dimension(250,35)); //sets the dimension
            titleLabel = new JLabel("Title: "); //creates new title label
            titleLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

            descriptionText = new JTextField(); //creates new description text field
            descriptionText.setPreferredSize(new Dimension(250,35)); //sets the dimension
            descriptionLabel = new JLabel("Description: "); //creates new description label
            descriptionLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

            startingDateText = new JTextField(); //creates new start date text field
            startingDateText.setPreferredSize(new Dimension(250,35)); //sets the dimension
            startingDateLabel = new JLabel("Start Date: (yyyy-MM-dd HH:mm:ss)"); //creates new start date label
            startingDateLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

            durationText = new JTextField(); //creates new duration text field
            durationText.setPreferredSize(new Dimension(250,35)); //sets the dimension
            durationLabel = new JLabel("Duration: (dd:HH:mm:ss)"); //creates new duration label
            durationLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

            AppointmentButton = new JButton("Add Event"); //creates new button
            AppointmentButton.setLayout(null); //declares that a Layout Manager won't be used in this case
            AppointmentButton.setSize(50, 50); //set's the buttons size
            AppointmentButton.setVisible(true); //sets the frame as visible
            AppointmentButton.addActionListener(this); //is notified when the button is pressed

            frame.add(spacetext); //adds spacetext to the frame
            frame.add(titleLabel); //adds titleLabel to the frame
            frame.add(titleText); //adds titleText to the frame
            frame.add(descriptionLabel); //adds descriptionLabel to the frame
            frame.add(descriptionText); //adds descriptionText to the frame
            frame.add(startingDateLabel); //adds startingDateLabel to the frame
            frame.add(startingDateText); //adds startingDateText to the frame
            frame.add(durationLabel); //adds durationLabel to the frame
            frame.add(durationText); //adds durationText to the frame
            frame.add(AppointmentButton); //adds addAppointmentButton to the frame

            //https://www.tutorialspoint.com/when-can-we-use-the-pack-method-in-java
            frame.pack(); //sizes the frame appropriately
            frame.setSize(300, 390); //sets the frame's size

        } else if (e.getSource() == work) { //work case

            frame.setLayout(new FlowLayout()); //sets 'FlowLayout' as the layout to be used for this frame
            frame.setResizable(false); //sets the frame to a fixed size
            frame.setLocationRelativeTo(null); //centers the frame
            frame.setVisible(true); //sets the frame as visible

            /* removes all that existed previously to redesign the frame */
            frame.remove(label);
            frame.remove(appointment);
            frame.remove(work);

            //smoothens the frame's appearance
            JLabel spacetext = new JLabel("                                              " +
                    "                                          ");
            titleText = new JTextField(); //creates new title text field
            titleText.setPreferredSize(new Dimension(250,35)); //sets the dimension
            titleLabel = new JLabel("Title: "); //creates new title label
            titleLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

            descriptionText = new JTextField(); //creates new description text field
            descriptionText.setPreferredSize(new Dimension(250,35)); //sets the dimension
            descriptionLabel = new JLabel("Description: "); //creates new description label
            descriptionLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

            deadlineText = new JTextField(); //creates new description text field
            deadlineText.setPreferredSize(new Dimension(250,35)); //sets the dimension
            deadlineLabel = new JLabel("Deadline: (yyyy-MM-dd HH:mm:ss)"); //creates new description label
            deadlineLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

            statusText = new JTextField(); //creates new status text field
            statusText.setPreferredSize(new Dimension(250,35)); //sets the dimension
            statusLabel = new JLabel("Status: (complete/incomplete)"); //creates new status label
            statusLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

            WorkButton = new JButton("Add Event"); //creates new button
            WorkButton.setLayout(null); //declares that a Layout Manager won't be used in this case
            WorkButton.setSize(50, 50); //set's the buttons size
            WorkButton.setVisible(true); //sets the frame as visible
            WorkButton.addActionListener(this); //is notified when the button is pressed

            frame.add(spacetext); //adds spacetext to the frame
            frame.add(titleLabel); //adds titleLabel to the frame
            frame.add(titleText); //adds titleText to the frame
            frame.add(descriptionLabel); //adds descriptionLabel to the frame
            frame.add(descriptionText); //adds descriptionText to the frame
            frame.add(deadlineLabel); //adds deadlineLabel to the frame
            frame.add(deadlineText); //adds deadlineText to the frame
            frame.add(statusLabel); //adds statusLabel to the frame
            frame.add(statusText); //adds statusText to the frame
            frame.add(WorkButton); //adds WorkButton to the frame
            frame.pack(); //sizes the frame appropriately
            frame.setSize(300, 390); //sets the frame's size

        } else if (e.getSource() == AppointmentButton) { //case where the appointment button was pressed

            boolean flag1 = true; //flag variable to be used for 'DateTimeParseException' initialized at true
            DateTime userDateTime = null; //initialized at null
            String startingDate = startingDateText.getText(); //gets the start date as text

            /* try-catch statement */
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //checks whether the pattern provided is valid
                LocalDateTime localDateTime = LocalDateTime.parse(startingDate, formatter); //creates the localDateTime-object
                /*
                  convert LocalDateTime to DateTime:
                  https://stackoverflow.com/questions/19431234/converting-between-java-time-localdatetime-and-java-util-date
                */
                userDateTime = new DateTime(java.util.Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant()));

            } catch (DateTimeParseException f) { //catches the exception where the pattern was invalid
                flag1 = false; //flag's value becomes false
            }

            boolean flag2 = true; //flag variable to be used for 'Exception' initialized at true
            Dur dur = null; //duration initialized at null
            String duration = durationText.getText(); //requests for input and removes leading and trailing whitespace

            /* try-catch statement */
            try { //try-catch statement to pinpoint specific exceptions
                // Split the input into hours and minutes
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:HH:mm:ss"); //checks whether the pattern provided is valid
                formatter.parse(duration); //parses the duration

                /* creates the array "parts" which contains the days, hours,
                 minutes and seconds, all of which have been split with a ':'
                 and places them all in "dur"
                */
                String[] parts = duration.split(":");
                int days = Integer.parseInt(parts[0]); //checks whether the days provided are a parsable int
                int hours = Integer.parseInt(parts[1]); //checks whether the hours provided are a parsable int
                int minutes = Integer.parseInt(parts[2]); //checks whether the minutes provided are a parsable int
                int seconds = Integer.parseInt(parts[3]); //checks whether the seconds provided are a parsable int
                dur = new Dur(days, hours, minutes, seconds);
            } catch (Exception d) { //catches the specific exception
                flag2 = false; //flag's value becomes false
            }
            if (flag1 && flag2) { //case where no exception was caught

                VEvent appointment = new VEvent(); //creates an event-type appointment
                //adds the summary in the appointment's list of properties
                appointment.getProperties().add(new Summary(titleText.getText()));
                //adds the description in the list of properties
                appointment.getProperties().add(new Description(descriptionText.getText()));
                appointment.getProperties().add(new DtStart(userDateTime)); //adds the starting date in the appointment's list of properties
                appointment.getProperties().add(new Duration(dur)); //adds the duration in the appointment's list of properties
                String uid = UUID.randomUUID().toString(); //creates a unique & random ID
                appointment.getProperties().add(new Uid(uid)); //adds the UID in the appointment's list of properties
                //adds the appointment with all of its newly added properties to the calendar
                calendar.getComponents().add(appointment);
                frame.dispose(); //cleans the frame of all its labels, buttons etc.

            /* case where at least one of two exceptions was caught, hence the user made a mistake in the format */
            } else {

                errFrame = new JFrame(); //creates a new frame
                errFrame.setTitle("Calendar"); //sets the frame's title to "Calendar"
                //sets "DISPOSE_ON_CLOSE" as the default operation for when the user decides to close the window
                errFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                errFrame.setLayout(new FlowLayout()); //https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
                errFrame.setLocationRelativeTo(null); //centers the frame relative to the monitor's size
                errFrame.setSize(385,150); //sets the frame's size
                errFrame.setResizable(false); //sets the frame to a fixed size

                //creates an object that contains a calendar logo
                ImageIcon logo = new ImageIcon("calendar_logo.png");
                errFrame.setIconImage(logo.getImage()); //sets the calendar logo as the frame's icon
                errLabel0 = new JLabel("Correct Format:"); //presents to the user the correct format

                //places the label at the top center of the frame
                errLabel0.setHorizontalAlignment(JLabel.CENTER);
                errLabel0.setVerticalAlignment(JLabel.TOP);

                errLabel0.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label
                errFrame.add(errLabel0); //adds the label to the frame

                //creates a new label that also plays the role of presenting the user with the correct format
                errLabel1 = new JLabel("Starting Time: (yyyy-MM-dd HH:mm:ss)");
                //places the label at the top center of the frame
                errLabel1.setHorizontalAlignment(JLabel.CENTER);
                errLabel1.setVerticalAlignment(JLabel.TOP);
                errLabel1.setFont(new Font("Calibri", Font.PLAIN, 20)); //customizes the label
                errFrame.add(errLabel1); //adds the label to the frame

                //creates a new label that also plays the role of presenting the user with the correct format
                errLabel2 = new JLabel("Duration: (dd:HH:mm:ss)");

                //places the label at the top center of the frame
                errLabel2.setHorizontalAlignment(JLabel.CENTER);
                errLabel2.setVerticalAlignment(JLabel.TOP);
                errLabel2.setFont(new Font("Calibri", Font.PLAIN, 20)); //customizes the label
                errFrame.add(errLabel2); //adds the label to the frame

                errFrame.setVisible(true); //sets the frame as visible
            }
        } else if (e.getSource() == WorkButton) { //case where the work button was pressed

            boolean flag = true; //flag variable to be used for 'DateTimeParseException' initialized at true
            DateTime userDateTime = null; //initialized at null
            String deadline = deadlineText.getText(); //gets the deadline as text

            /* try-catch statement */
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //checks whether the pattern provided is valid
                LocalDateTime localDateTime = LocalDateTime.parse(deadline, formatter); //creates the localDateTime-object
                /* convert LocalDateTime to DateTime:
                https://stackoverflow.com/questions/19431234/converting-between-java-time-localdatetime-and-java-util-date */
                userDateTime = new DateTime(java.util.Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant()));

            } catch (DateTimeParseException f) { //catches the exception where the pattern was invalid
                flag = false; //flag's value becomes false
            }

            /* case where the exception wasn't caught and the status is either 'complete' or 'incomplete' */
            if (flag && ((statusText.getText().equalsIgnoreCase("complete")) ||
                          (statusText.getText().equalsIgnoreCase("incomplete")))) {

                VToDo work = new VToDo(); //creates a new work
                work.getProperties().add(new Summary(titleText.getText())); //adds the work's title in the list of properties
                work.getProperties().add(new Description(descriptionText.getText())); //adds the work's description in the list of properties
                if (statusText.getText().equalsIgnoreCase("complete")) { //case where the user typed in "complete"
                    //adds the work's status(COMPLETED in this case) in the list of properties
                    work.getProperties().add(new Status("COMPLETED"));
                } else { //case where the user typed in "incomplete"
                    //adds the work's status(IN-PROCESS in this case) in the list of properties
                    work.getProperties().add(new Status("IN-PROCESS"));
                }
                work.getProperties().add(new Due(userDateTime)); //adds the work's due in the list of properties
                String uid = UUID.randomUUID().toString(); //creates a unique & random ID
                work.getProperties().add(new Uid(uid)); //adds the UID in the appointment's list of properties
                //adds the work with all of its newly added properties to the calendar
                calendar.getComponents().add(work);
                frame.dispose(); //cleans the frame of all its labels, buttons etc.
            } else {

                errFrame = new JFrame(); //creates a new frame
                errFrame.setTitle("Calendar"); //sets the frame's title as "Calendar"
                //sets "DISPOSE_ON_CLOSE" as the default operation for when the user decides to close the window
                errFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                errFrame.setLayout(new FlowLayout()); //sets the frame's layout
                errFrame.setLocationRelativeTo(null); //center's the frame relative to the monitor's size
                errFrame.setSize(385,150); //sets the frame's size
                errFrame.setResizable(false); //sets the frame to a fixed size

                //creates an object that contains a calendar logo
                ImageIcon logo = new ImageIcon("calendar_logo.png");
                errFrame.setIconImage(logo.getImage()); //sets the calendar logo as the frame's icon
                errLabel0 = new JLabel("Correct Format:"); //presents to the user the correct format

                //places the label at the top center of the frame
                errLabel0.setHorizontalAlignment(JLabel.CENTER);
                errLabel0.setVerticalAlignment(JLabel.TOP);
                errLabel0.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label
                errFrame.add(errLabel0); //adds the label to the frame

                //creates a new label that also plays the role of presenting the user with the correct format
                errLabel1 = new JLabel("Deadline: (yyyy-MM-dd HH:mm:ss)");

                //places the label at the top center of the frame
                errLabel1.setHorizontalAlignment(JLabel.CENTER);
                errLabel1.setVerticalAlignment(JLabel.TOP);
                errLabel1.setFont(new Font("Calibri", Font.PLAIN, 20)); //customizes the label
                errFrame.add(errLabel1); //adds the label to the frame

                //creates a new label that also plays the role of presenting the user with the correct format
                errLabel2 = new JLabel("Status: (complete/incomplete)");

                //places the label at the top center of the frame
                errLabel2.setHorizontalAlignment(JLabel.CENTER);
                errLabel2.setVerticalAlignment(JLabel.TOP);
                errLabel2.setFont(new Font("Calibri", Font.PLAIN, 20)); //customizes the label
                errFrame.add(errLabel2); //adds the label to the frame

                errFrame.setVisible(true); //sets the frame as visible
            }
        }
    }
}
