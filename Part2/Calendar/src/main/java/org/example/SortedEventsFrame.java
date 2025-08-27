package org.example;

/* imports */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//handles the GUI-part presentation of sorted events
public class SortedEventsFrame extends CalendarFrames implements ActionListener {

    /* object declaration */
    JFrame frame; //JFrame-type frame
    JComboBox comboBox; //JComboBox-type combo box(combines buttons)
    JLabel label; //JLabel-type label
    JPanel panel; //JPanel-type panel

    SortedEventsFrame() { //constructor

        calendarFrame.setVisible(false); //sets the frame as invisible
        frame = new JFrame(); //creates a new frame
        frame.setTitle("Calendar"); //sets the frame's title to "Calendar"
        //sets "DISPOSE_ON_CLOSE" as the default operation for when the user decides to close the window
        frame.setDefaultCloseOperation(JFrame. DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout()); //https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
        frame.setSize(815,497); //sets the frame's size
        frame.setLocationRelativeTo(null); //centers the frame relative to the monitor's size
        frame.setResizable(false); //sets the frame to a fixed size
        ImageIcon logo = new ImageIcon("calendar_logo.png"); //creates an object that contains a calendar logo
        frame.setIconImage(logo.getImage()); //sets the calendar logo as the frame icon
        label = new JLabel("Select a list of events:"); //creates a new label

        //places the label at the top center of the frame
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(new Font("Calibri", Font.BOLD, 22)); //customizes the label
        frame.add(label); //adds the label to the frame
        /*
           creates a String variable of which the contents will later be
           printed out to the user as the available options they have access to
        */
        String[] choices = {"", "Events until end of day", "Events until end of week",
                "Events until end of month", "Events for past day", "Events for past week",
                "Events for past month", "Incomplete works before deadline", "Incomplete works after deadline"};

        comboBox = new JComboBox(choices); //creates a new combo box to contain the user's available options
        comboBox.addActionListener(this); //is notified the moment the appropriate button is pressed
        frame.add(comboBox); //adds the combo box to the frame
        panel = new JPanel(); //creates a new panel
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); //sets the layout to BoxLayout
        panel.setBackground(Color.lightGray); //sets the background colour to light gray
        JScrollPane scrollPane = new JScrollPane(panel); //creates a scrollbar
        panel.setAutoscrolls(true); //sets the scrollbar to be able to automatically scroll up/down when dragged
        scrollPane.setPreferredSize(new Dimension(798, 420)); //sets the scrollbar's dimensions
        //sets the scrollbar to vertical
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setVisible(true); //sets the scrollbar as visible
        frame.setVisible(true); //sets the frame as visible
        frame.add(scrollPane); //adds the scrollbar to the frame
    }
    @Override
    public void actionPerformed(ActionEvent e) { //is invoked when an action occurs
        if (e.getSource() == comboBox) { //case where the user chose an option

            if (comboBox.getSelectedIndex() == 0) { //case where the 'empty' option was pressed
                panel.removeAll(); //removes all previous components of the panel
            }
            if (comboBox.getSelectedIndex() == 1) { //case where "Events until end of day" was pressed

                panel.removeAll(); //removes all previous components of the panel
                panel.setVisible(false); //sets the panel as invisible

                //calls the calendarLists method of the myCalendar class to display the events until the end of the day
                myCalendar.calendarLists("day", calendar, panel);
                panel.setVisible(true); //sets the panel as visible

            } else if (comboBox.getSelectedIndex() == 2) { //case where "Events until end of week" was pressed

                panel.removeAll(); //removes all previous components of the panel
                panel.setVisible(false); //sets the panel as invisible

                //calls the calendarLists method of the myCalendar class to display the events until the end of the week
                myCalendar.calendarLists("week", calendar, panel);
                panel.setVisible(true); //sets the panel as visible

            } else if (comboBox.getSelectedIndex() == 3) { //case where "Events until end of month" was pressed

                panel.removeAll(); //removes all previous components of the panel
                panel.setVisible(false); //sets the panel as invisible

                //calls the calendarLists method of the myCalendar class to display the events until the end of the month
                myCalendar.calendarLists("month", calendar, panel);
                panel.setVisible(true); //sets the panel as visible

            } else if (comboBox.getSelectedIndex() == 4) { //case where "Events for past day" was pressed

                panel.removeAll(); //removes all previous components of the panel
                panel.setVisible(false); //sets the panel as invisible

                // calls the calendarLists method of the myCalendar class to display the events
                // from the start of the day till the user's current time
                myCalendar.calendarLists("pastday", calendar, panel);
                panel.setVisible(true); //sets the panel as visible

            } else if (comboBox.getSelectedIndex() == 5) { //case where "Events for past week" was pressed

                panel.removeAll(); //removes all previous components of the panel
                panel.setVisible(false); //sets the panel as invisible

                // calls the calendarLists method of the myCalendar class to display the events
                // from the start of the week till the user's current time
                myCalendar.calendarLists("pastweek", calendar, panel);
                panel.setVisible(true); //sets the panel as visible

            } else if (comboBox.getSelectedIndex() == 6) { //case where "Events for past month" was pressed

                panel.removeAll(); //removes all previous components of the panel
                panel.setVisible(false); //sets the panel as invisible

                //calls the calendarLists method of the myCalendar class to display the events
                //from the start of the month till the user's current time
                myCalendar.calendarLists("pastmonth", calendar, panel);
                panel.setVisible(true); //sets the panel as visible

            } else if (comboBox.getSelectedIndex() == 7) { //case where "Incomplete works before deadline" was pressed

                panel.removeAll(); //removes all previous components of the panel
                panel.setVisible(false); //sets the panel as invisible

                //calls the calendarLists method of the myCalendar class to display the works
                //that have yet to be completed and of which the deadline hasn't passed
                myCalendar.calendarLists("todo", calendar, panel);
                panel.setVisible(true); //sets the panel as visible

            } else if (comboBox.getSelectedIndex() == 8) { //case where "Incomplete works after deadline" was pressed

                panel.removeAll(); //removes all previous components of the panel
                panel.setVisible(false); //sets the panel as invisible

                //calls the calendarLists method of the myCalendar class to display the works
                //that have yet to be completed and of which the deadline has already passed
                myCalendar.calendarLists("due", calendar, panel);
                panel.setVisible(true); //sets the panel as visible
            }
        }
    }
}
