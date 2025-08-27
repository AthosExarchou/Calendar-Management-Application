package org.example;

/* imports */
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;

//handles the GUI-part status editing of works
public class EditStatusFrame extends CalendarFrames {

    /* object declaration */
    JFrame frame; //JFrame-type frame
    JLabel label; //JLabel-type label
    JPanel panel; //JPanel-type panel
    JButton done; //JButton-type button

    EditStatusFrame() { //constructor

        ArrayList<VToDo> sortedWorks = new ArrayList<>(); //creates an arraylist to contain the sorted works
        calendarFrame.setVisible(false); //sets the frame as invisible
        frame = new JFrame(); //creates a new frame
        frame.setTitle("Calendar"); //sets the frame's title to 'Calendar'
        //sets "DISPOSE_ON_CLOSE" as the default operation for when the user decides to close the window
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout()); //sets the frame's layout

        ImageIcon logo = new ImageIcon("calendar_logo.png"); //creates an object that contains a calendar logo
        frame.setIconImage(logo.getImage()); //sets the calendar logo as the frame icon

        label = new JLabel("EDIT STATUS"); //creates a new label

        //places the label to the top-center of the frame
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label

        panel = new JPanel(); //creates a new panel
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); //sets the panel's layout
        panel.setBackground(Color.lightGray); //sets the background colour to light gray
        JScrollPane scrollPane = new JScrollPane(panel); //creates a scrollbar
        scrollPane.setPreferredSize(new Dimension(798, 420)); //sets the scrollbar's dimensions
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

        panel.setLayout(new GridLayout(0, 6)); //sets the panel's layout

        /* for-loop that accesses every single component of the given calendar */
        for (Object component : calendar.getComponents()) { //traverses through the calendar's components
            if (component instanceof VToDo) { //case where the component is of type VToDo

                VToDo vtodo = (VToDo) component; //casts the component to type-VToDo to prevent errors
                sortedWorks.add(vtodo); //adds a work to the sortedEvents ArrayList
            }
        }

        /* try-catch statement to pinpoint a specific exception */
        try {
            //https://www.geeksforgeeks.org/double-colon-operator-in-java/
            //sorting the array using method reference:
            //https://stackoverflow.com/questions/18895915/how-to-sort-an-array-of-objects-in-java
            sortedWorks.sort(Comparator.comparing(myCalendar :: getDateTime));
        } catch (NullPointerException e) {  //catches the specific exception
            System.out.println("Failed to sort the events."); //prints appropriate message
        }

        for (VToDo vtodo : sortedWorks) { //traverses through the work's properties

            /* adds the work's properties to the panel */
            panel.add(new JLabel(vtodo.getSummary().getValue()));
            panel.add(new JLabel(vtodo.getDescription().getValue()));
            panel.add(new JLabel(vtodo.getDue() != null ? vtodo.getDue().getValue() : ""));
            panel.add(new JLabel(vtodo.getStatus() != null ? vtodo.getStatus().getValue() : ""));

            ButtonGroup statusGroup = new ButtonGroup(); //creates a button group

            JRadioButton completeRadioButton = new JRadioButton("Complete"); //creates a new radio button
            /* is notified the moment the user presses the 'Complete' button */
            completeRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    /* removes and then adds the status to the work's properties */
                    vtodo.getProperties().remove(vtodo.getProperty(Property.STATUS));
                    vtodo.getProperties().add(new Status("COMPLETED"));
                    completeRadioButton.setSelected(false); //sets the button to "not selected"
                }
            });
            statusGroup.add(completeRadioButton); //adds the radio button to the group
            panel.add(completeRadioButton); //adds the button to the panel

            JRadioButton incompleteRadioButton = new JRadioButton("Incomplete"); //creates a new radio button
            /* is notified the moment the user presses the 'Incomplete' button */
            incompleteRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    /* removes and then adds the status to the work's properties */
                    vtodo.getProperties().remove(vtodo.getProperty(Property.STATUS));
                    vtodo.getProperties().add(new Status("IN-PROCESS"));
                    incompleteRadioButton.setSelected(false); //sets the button to "not selected"
                }
            });
            statusGroup.add(incompleteRadioButton); //adds the button to the group
            panel.add(incompleteRadioButton); //adds the button to the panel
        }
        frame.add(label, BorderLayout.NORTH); //adds the label to the frame
        frame.add(scrollPane, BorderLayout.CENTER); //adds the scrollbar to the frame
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); //creates a new scrollbar
        southPanel.add(done); //adds the panel
        frame.add(southPanel, BorderLayout.SOUTH); //adds the scrollbar to the frame
        frame.setSize(695, 390); //sets the frame's size
        frame.setLocationRelativeTo(null); //centers the frame relative to the monitor's size
        frame.setResizable(false); //sets the frame to a fixed size
        frame.setVisible(true); //sets the frame as visible
    }
}
