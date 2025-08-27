package org.example;

/* imports */
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Property;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

//handles the GUI-part editing of events
public class EditEventFrame extends CalendarFrames  implements ActionListener {

    /* object declaration */
    JFrame frame; //JFrame-type frame
    JLabel l0; //JLabel-type label
    JPanel panel; //JPanel-type panel
    JButton done; //JButton-type button
    JButton editButton; //JButton-type button
    JFrame frameApp; //JFrame-type frame
    JTextField titleText; //JTextField-type text
    JLabel titleLabel; //JLabel-type label
    JTextField descriptionText; //JTextField-type text
    JLabel descriptionLabel; //JLabel-type label
    JTextField startingDateText; //JTextField-type text
    JLabel startingDateLabel; //JLabel-type label
    JTextField durationText; //JTextField-type text
    JLabel durationLabel; //JLabel-type label
    JButton saveChanges; //JButton-type button
    JFrame errFrame; //JFrame-type frame
    JLabel errLabel0; //JLabel-type label
    JLabel errLabel1; //JLabel-type label
    JLabel errLabel2; //JLabel-type label
    JTextField deadlineText; //JTextField-type text
    JLabel deadlineLabel; //JLabel-type label
    JTextField statusText; //JTextField-type text
    JLabel statusLabel; //JLabel-type label

    EditEventFrame() { //constructor

        calendarFrame.setVisible(false); //sets the frame as invisible
        frame = new JFrame(); //creates a new frame
        frame.setTitle("Calendar"); //sets the frame's title to 'Calendar'
        //sets "DISPOSE_ON_CLOSE" as the default operation for when the user decides to close the window
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout()); //sets the frame's layout
        ImageIcon logo = new ImageIcon("calendar_logo.png"); //creates an object that contains a calendar logo
        frame.setIconImage(logo.getImage()); //sets the calendar logo as the frame icon

        l0 = new JLabel("EDIT EVENTS"); //creates a new label
        //places the label to the top-center of the frame
        l0.setHorizontalAlignment(JLabel.CENTER);
        l0.setVerticalAlignment(JLabel.TOP);
        l0.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label

        panel = new JPanel(); //creates a new panel
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); //sets the panel's layout
        panel.setBackground(Color.lightGray); //sets the background colour to light gray
        JScrollPane scrollPane = new JScrollPane(panel); //creates a scrollbar
        scrollPane.setPreferredSize(new Dimension(798, 420));
        //sets the scrollbar to be vertical
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //sets the scrollbar's dimensions
        scrollPane.setPreferredSize(new Dimension(panel.getPreferredSize().width + 20, 420));

        done = new JButton("Done"); //creates a new button
        /* is notified the moment the user presses the 'Done' button */
        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); //cleans the frame of all its labels, buttons etc.
            }
        });
        panel.setLayout(new GridLayout(0, 5)); //sets the panel's layout

        /* creates an arraylist to contain sorted events */
        ArrayList<Object> sortedEvents = new ArrayList<>();

        /* for-loop that accesses every single component of the given calendar */
        for (Object component : calendar.getComponents()) { //traverses through the calendar's components

            if (component instanceof VEvent) { //case where the component is of type VEvent

                VEvent vevent = (VEvent) component; //casts the component to type-VEvent in order to prevent errors
                sortedEvents.add(vevent); //adds an appointment to the sortedEvents ArrayList

            } else if (component instanceof VToDo) { //case where the component is of type VToDo

                VToDo vtodo = (VToDo) component; //casts the component to type-VToDo to prevent errors
                sortedEvents.add(vtodo); //adds a work to the sortedEvents ArrayList
            }
        }

        /* try-catch statement to pinpoint a specific exception */
        try {
            //https://www.geeksforgeeks.org/double-colon-operator-in-java/
            //sorting the array using method reference:
            //https://stackoverflow.com/questions/18895915/how-to-sort-an-array-of-objects-in-java
            sortedEvents.sort(Comparator.comparing(myCalendar :: getDateTime));
        } catch (NullPointerException e) {  //catches the specific exception
            System.out.println("Failed to sort the events."); //prints appropriate message
        }
        for (Object event : sortedEvents) { //accesses every sorted event whether that is an appointment or a work
            if (event instanceof VEvent) { //case where the event is an appointment

                /* gets the appointment's properties and adds them as labels to the panel(with appropriate spacing where needed */
                panel.add(new JLabel(((VEvent) event).getSummary() != null ? ((VEvent) event).getSummary().getValue() : ""));
                panel.add(new JLabel(((VEvent) event).getDescription() != null ? (((VEvent) event).getDescription().getValue() + "         ") : ""));
                panel.add(new JLabel(((VEvent) event).getStartDate() != null ? ((VEvent) event).getStartDate().getValue() : ""));
                panel.add(new JLabel(((VEvent) event).getDuration() != null ? ((VEvent) event).getEndDate().getValue() : ""));

                editButton = new JButton("Edit Appointment"); //creates a new button for the purpose of editing appointments
                editButton.setSize(200,20); //sets the button's size

                /* is notified the moment the user presses the 'Edit Appointment' button */
                editButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        frameApp = new JFrame(); //creates a new frame
                        frameApp.setTitle("Calendar"); //sets the frame's title to 'Calendar'
                        ImageIcon logo = new ImageIcon("calendar_logo.png"); //creates an object that contains a calendar logo
                        frameApp.setIconImage(logo.getImage()); //sets the calendar logo as the frame icon
                        frameApp.setResizable(false); //sets the frame to a fixed size
                        frameApp.setLayout(new FlowLayout()); //sets the frame's size
                        frameApp.setLocationRelativeTo(null); //centers the frame relative to the monitor's size
                        JLabel text = new JLabel("Update Details"); //creates a new label
                        text.setFont(new Font("Calibri", Font.BOLD, 20)); //customizes the label

                        /* adds space between the texts to smoothen the appearance */
                        JLabel spacetext1 = new JLabel("                     ");
                        JLabel spacetext2 = new JLabel("                     ");

                        titleText = new JTextField(); //creates a new text field for the title
                        titleText.setPreferredSize(new Dimension(250,35)); //sets the field's dimensions
                        titleLabel = new JLabel("Title: "); //creates a new label for the title
                        titleLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

                        descriptionText = new JTextField(); //creates a new description text
                        descriptionText.setPreferredSize(new Dimension(250,35)); //sets the preferred dimensions
                        descriptionLabel = new JLabel("Description: ");
                        descriptionLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

                        startingDateText = new JTextField(); //creates a new start date text
                        startingDateText.setPreferredSize(new Dimension(250,35)); //sets the preferred dimensions
                        //creates a new start date label that also plays the role of presenting the user with the correct format
                        startingDateLabel = new JLabel("Start Date: (yyyy-MM-dd HH:mm:ss)");
                        startingDateLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

                        durationText = new JTextField(); //creates a new duration text
                        durationText.setPreferredSize(new Dimension(250,35)); //sets the preferred dimensions
                        //creates a new duration label that also plays the role of presenting the user with the correct format
                        durationLabel = new JLabel("Duration: (dd:HH:mm:ss)");
                        durationLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

                        saveChanges = new JButton("Save Changes"); //creates a new button
                        saveChanges.setLayout(null); //declares that a Layout Manager won't be used in this case
                        saveChanges.setSize(50, 50); //sets the buttons size
                        saveChanges.setVisible(true); //sets the button as visible

                        /* is notified the moment the user presses the 'Save Changes' button */
                        saveChanges.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                                boolean flag1 = true; //flag's value becomes true
                                DateTime userDateTime = null; //initialized at null
                                String startingDate = startingDateText.getText(); //start date variable

                                /* try-catch statement to pinpoint specific exceptions */
                                try {
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //checks whether the pattern provided is valid
                                    LocalDateTime localDateTime = LocalDateTime.parse(startingDate, formatter); //creates the localDateTime-object
                                    //convert LocalDateTime to DateTime: (https://stackoverflow.com/questions/19431234/converting-between-java-time-localdatetime-and-java-util-date)
                                    userDateTime = new DateTime(java.util.Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant()));

                                } catch (DateTimeParseException f) { //catches the exception where the pattern was invalid
                                    flag1 = false; //flag variable's value becomes false
                                }
                                boolean flag2 = true; //flag variable's value initialized at true
                                Dur dur = null; //duration object 'dur' initialized at null
                                String duration = durationText.getText(); //requests for input

                                /* try-catch statement to pinpoint specific exceptions */
                                try {
                                    //splits the input into hours and minutes
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
                                    flag2 = false; //flag's value becomes false in order to exit the while-loop
                                }
                                if (flag1 && flag2) { //case where no exception was caught

                                    /* removes and then adds the existing properties of the appointment */
                                    ((VEvent) event).getProperties().remove(((VEvent) event).getProperty(Property.SUMMARY));
                                    ((VEvent) event).getProperties().add(new Summary(titleText.getText()));
                                    ((VEvent) event).getProperties().remove(((VEvent) event).getProperty(Property.DESCRIPTION));
                                    ((VEvent) event).getProperties().add(new Description(descriptionText.getText()));
                                    ((VEvent) event).getProperties().remove(((VEvent) event).getProperty(Property.DTSTART));
                                    ((VEvent) event).getProperties().add(new DtStart(userDateTime));

                                    if (((VEvent) event).getDuration() != null) { //case where the appointment has a duration
                                        ((VEvent) event).getProperties().remove(((VEvent) event).getProperty(Property.DURATION));
                                    }
                                    ((VEvent) event).getProperties().add(new Duration(dur)); //adds the appointment's duration

                                    ((VEvent) event).getProperties().remove(((VEvent) event).getProperty(Property.UID)); //removes the appointment's UID
                                    String uid = UUID.randomUUID().toString(); //variable that contains the appointment's UID (unique identifier)
                                    ((VEvent) event).getProperties().add(new Uid(uid)); //adds the appointment's UID
                                    frameApp.dispose(); //cleans the frame of all its labels, buttons etc.
                                } else {

                                    errFrame = new JFrame(); //creates a new frame
                                    errFrame.setTitle("Calendar"); //sets the frame's title to 'Calendar'
                                    //sets "DISPOSE_ON_CLOSE" as the default operation for when the user decides to close the window
                                    errFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                    errFrame.setLayout(new FlowLayout()); //https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
                                    errFrame.setLocationRelativeTo(null); //centers the frame relative to the monitor's size
                                    errFrame.setSize(385,150); //sets the frame's size
                                    errFrame.setResizable(false); //sets the frame to a fixed size
                                    ImageIcon logo = new ImageIcon("calendar_logo.png"); //creates an object that contains a calendar logo
                                    errFrame.setIconImage(logo.getImage()); //sets the calendar logo as the frame icon

                                    errLabel0 = new JLabel("Correct Format:"); //creates a new label
                                    //places the label at the top center of the frame
                                    errLabel0.setHorizontalAlignment(JLabel.CENTER);
                                    errLabel0.setVerticalAlignment(JLabel.TOP);
                                    errLabel0.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label
                                    errFrame.add(errLabel0); //adds the label to the frame

                                    //creates a new start date label that also plays the role of presenting the user with the correct format
                                    errLabel1 = new JLabel("Starting Date: (yyyy-MM-dd HH:mm:ss)");
                                    //places the label at the top-center of the frame
                                    errLabel1.setHorizontalAlignment(JLabel.CENTER);
                                    errLabel1.setVerticalAlignment(JLabel.TOP);
                                    errLabel1.setFont(new Font("Calibri", Font.PLAIN, 20)); //customizes the label
                                    errFrame.add(errLabel1); //adds the label to the frame

                                    //creates a new duration label that also plays the role of presenting the user with the correct format
                                    errLabel2 = new JLabel("Duration: (dd:HH:mm:ss)");
                                    //places the label at the top-center of the frame
                                    errLabel2.setHorizontalAlignment(JLabel.CENTER);
                                    errLabel2.setVerticalAlignment(JLabel.TOP);
                                    errLabel2.setFont(new Font("Calibri", Font.PLAIN, 20)); //customizes the label
                                    errFrame.add(errLabel2); //adds the label to the frame

                                    errFrame.setVisible(true); //sets the frame as visible
                                }
                            }
                        });
                        /* adds the labels/fields to the frame */
                        frameApp.add(spacetext1);
                        frameApp.add(text);
                        frameApp.add(spacetext2);
                        frameApp.add(titleLabel);
                        frameApp.add(titleText);
                        frameApp.add(descriptionLabel);
                        frameApp.add(descriptionText);
                        frameApp.add(startingDateLabel);
                        frameApp.add(startingDateText);
                        frameApp.add(durationLabel);
                        frameApp.add(durationText);

                        frameApp.add(saveChanges); //adds the button to the frame

                        frameApp.setSize(300, 390); //sets the frame's size
                        frameApp.setVisible(true); //sets the frame as visible
                    }
                });
                panel.add(editButton); //adds the button to the panel

            } else { //case where the event is a work
                /* adds the work's properties to the panel */
                panel.add(new JLabel(((VToDo) event).getSummary() != null ? ((VToDo) event).getSummary().getValue() : ""));
                panel.add(new JLabel(((VToDo) event).getDescription() != null ? (((VToDo) event).getDescription().getValue() + "         ") : ""));
                panel.add(new JLabel(((VToDo) event).getDue() != null ? ((VToDo) event).getDue().getValue() : ""));
                panel.add(new JLabel(((VToDo) event).getStatus() != null ? ((VToDo) event).getStatus().getValue() : ""));

                editButton = new JButton("Edit Work"); //creates a new button
                editButton.setSize(100,20); //sets the button's size

                /* is notified the moment the user presses the 'Edit Work' button */
                editButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        frameApp = new JFrame(); //creates a new frame
                        frameApp.setTitle("Calendar"); //sets the frame's title to 'Calendar'

                        ImageIcon logo = new ImageIcon("calendar_logo.png"); //creates an object that contains a calendar logo
                        frameApp.setIconImage(logo.getImage()); //sets the calendar logo as the frame icon
                        frameApp.setResizable(false); //sets the frame to a fixed size
                        frameApp.setLayout(new FlowLayout()); //sets the frame's layout
                        frameApp.setLocationRelativeTo(null); //centers the frame relative to the monitor's size
                        JLabel text = new JLabel("Update Details"); //creates a new label
                        text.setFont(new Font("Calibri", Font.BOLD, 20)); //customizes the label

                        /* adds space between the texts to smoothen the appearance */
                        JLabel spacetext1 = new JLabel("                     ");
                        JLabel spacetext2 = new JLabel("                     ");

                        titleText = new JTextField(); //creates a new title text field
                        titleText.setPreferredSize(new Dimension(250,35)); //sets the preferred dimensions
                        titleLabel = new JLabel("Title: "); //creates a new label
                        titleLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

                        descriptionText = new JTextField(); //creates a new description text field
                        descriptionText.setPreferredSize(new Dimension(250,35));
                        descriptionLabel = new JLabel("Description: "); //creates a new label
                        descriptionLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

                        deadlineText = new JTextField(); //creates a new deadline text field
                        deadlineText.setPreferredSize(new Dimension(250,35)); //sets the preferred dimensions
                        //creates a new deadline label that also plays the role of presenting the user with the correct format
                        deadlineLabel = new JLabel("Deadline: (yyyy-MM-dd HH:mm:ss)");
                        deadlineLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

                        statusText = new JTextField(); //creates a new status text field
                        statusText.setPreferredSize(new Dimension(250,35)); //sets the preferred dimensions
                        //creates a new status label that also plays the role of presenting the user with the correct format
                        statusLabel = new JLabel("Status: (complete/incomplete)");
                        statusLabel.setFont(new Font("Calibri", Font.BOLD, 18)); //customizes the label

                        saveChanges = new JButton("Save Changes"); //creates a new button
                        saveChanges.setLayout(null); //declares that a Layout Manager won't be used in this case
                        saveChanges.setSize(50, 50); //sets the button's size
                        saveChanges.setVisible(true); //sets the button as visible

                        /* is notified the moment the user presses the 'Save Changes' button */
                        saveChanges.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {

                                boolean flag = true; //flag variable's value becomes true
                                DateTime userDateTime = null; //initialized at null
                                String deadline = deadlineText.getText(); //gets the deadline and puts it in the 'deadline' String variable

                                /* try-catch statement to pinpoint specific exceptions */
                                try {
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //checks whether the pattern provided is valid
                                    LocalDateTime localDateTime = LocalDateTime.parse(deadline, formatter); //creates the localDateTime-object
                                    //convert LocalDateTime to DateTime: (https://stackoverflow.com/questions/19431234/converting-between-java-time-localdatetime-and-java-util-date)
                                    userDateTime = new DateTime(java.util.Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant()));

                                } catch (DateTimeParseException f) { //catches the exception where the pattern was invalid
                                    flag = false; //flag's value becomes false
                                }

                                /* case where no exception was caught and the status provided by the user is either 'complete' or 'incomplete' */
                                if (flag && ((statusText.getText().equalsIgnoreCase("complete")) || (statusText.getText().equalsIgnoreCase("incomplete")))) {

                                    /* removes and then adds the existing properties of the work */
                                    ((VToDo) event).getProperties().remove(((VToDo) event).getProperty(Property.SUMMARY));
                                    ((VToDo) event).getProperties().add(new Summary(titleText.getText()));
                                    ((VToDo) event).getProperties().remove(((VToDo) event).getProperty(Property.DESCRIPTION));
                                    ((VToDo) event).getProperties().add(new Description(descriptionText.getText()));

                                    if (((VToDo) event).getDue() != null) { //case where the work has a due

                                        //removes the work's 'due'
                                        ((VToDo) event).getProperties().remove(((VToDo) event).getProperty(Property.DUE));
                                    }
                                    ((VToDo) event).getProperties().add(new Due(userDateTime)); //adds the work's 'due'

                                    if (((VToDo) event).getStatus() != null) { //case where the work has a status

                                        //removes the work's 'status'
                                        ((VToDo) event).getProperties().remove(((VToDo) event).getProperty(Property.STATUS));
                                    }
                                    //case where the user entered 'complete' as the 'status' value
                                    if (statusText.getText().equalsIgnoreCase("complete")) {

                                        ((VToDo) event).getProperties().add(new Status("COMPLETE")); //adds the work's 'status'
                                    } else { //case where the user entered 'incomplete' as the 'status' value

                                        ((VToDo) event).getProperties().add(new Status("IN-PROCESS")); //adds the work's 'status'
                                    }

                                    ((VToDo) event).getProperties().remove(((VToDo) event).getProperty(Property.UID)); //removes the work's UID
                                    String uid = UUID.randomUUID().toString(); //variable that contains the work's UID (unique identifier)
                                    ((VToDo) event).getProperties().add(new Uid(uid)); //adds the UID to the work's properties

                                    frameApp.dispose(); //cleans the frame of all its labels, buttons etc.

                                } else { //case where an exception was caught (the provided format was wrong)

                                    errFrame = new JFrame(); //creates a new frame
                                    errFrame.setTitle("Calendar"); //sets the frame's title to 'Calendar'
                                    //sets "DISPOSE_ON_CLOSE" as the default operation for when the user decides to close the window
                                    errFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                    errFrame.setLayout(new FlowLayout()); //https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
                                    errFrame.setLocationRelativeTo(null); //centers the frame relative to the monitor's size
                                    errFrame.setSize(385,150); //sets the frame's size
                                    errFrame.setResizable(false); //sets the frame to a fixed size
                                    ImageIcon logo = new ImageIcon("calendar_logo.png"); //creates an object that contains a calendar logo
                                    errFrame.setIconImage(logo.getImage()); //sets the calendar logo as the frame icon

                                    errLabel0 = new JLabel("Correct Format:"); //creates a new label
                                    //places the label to the top-center of the frame
                                    errLabel0.setHorizontalAlignment(JLabel.CENTER);
                                    errLabel0.setVerticalAlignment(JLabel.TOP);
                                    errLabel0.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label
                                    errFrame.add(errLabel0); //adds the label to the frame

                                    //creates a new deadline label that also plays the role of presenting the user with the correct format
                                    errLabel1 = new JLabel("Deadline: (yyyy-MM-dd HH:mm:ss)");
                                    //places the label to the top-center of the frame
                                    errLabel1.setHorizontalAlignment(JLabel.CENTER);
                                    errLabel1.setVerticalAlignment(JLabel.TOP);
                                    errLabel1.setFont(new Font("Calibri", Font.PLAIN, 20)); //customizes the label
                                    errFrame.add(errLabel1); //adds the label to the frame

                                    //creates a new status label that also plays the role of presenting the user with the correct format
                                    errLabel2 = new JLabel("Status: (complete/incomplete)");
                                    //places the label to the top-center of the frame
                                    errLabel2.setHorizontalAlignment(JLabel.CENTER);
                                    errLabel2.setVerticalAlignment(JLabel.TOP);
                                    errLabel2.setFont(new Font("Calibri", Font.PLAIN, 20)); //customizes the label
                                    errFrame.add(errLabel2); //adds the label to the frame

                                    errFrame.setVisible(true); //sets the frame as visible
                                }
                            }
                        });
                        /* adds the labels/text-fields to the frame */
                        frameApp.add(spacetext1);
                        frameApp.add(text);
                        frameApp.add(spacetext2);
                        frameApp.add(titleLabel);
                        frameApp.add(titleText);
                        frameApp.add(descriptionLabel);
                        frameApp.add(descriptionText);
                        frameApp.add(deadlineLabel);
                        frameApp.add(deadlineText);
                        frameApp.add(statusLabel);
                        frameApp.add(statusText);

                        frameApp.add(saveChanges); //adds the button to the frame

                        frameApp.setSize(300, 390); //sets the frame's size
                        frameApp.setVisible(true); //sets the frame as visible
                    }
                });

                panel.add(editButton); //adds the button to the panel
            }
        }
        frame.add(l0, BorderLayout.NORTH); //adds the label to the frame
        frame.add(scrollPane, BorderLayout.CENTER); //adds the scrollbar to the frame

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); //creates a new panel
        southPanel.add(done); //adds the button to the panel
        frame.add(southPanel, BorderLayout.SOUTH); //adds the panel to the frame
        frame.setSize(695, 390); //sets the frame's size
        frame.setLocationRelativeTo(null); //centers the frame relative to the monitor's size
        frame.setResizable(false); //sets the frame to a fixed size
        frame.setVisible(true); //sets the frame as visible
    }
}
