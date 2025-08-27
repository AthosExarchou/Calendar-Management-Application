# Calendar Management Application (part 2 of 2)

Developed as a group project for the **Object-Oriented Programming-II** course at [Harokopio University of Athens – Dept. of Informatics and Telematics](https://www.dit.hua.gr), **Calendar Management Application (part 2 of 2)** is a graphical user interface (GUI) application for managing events.
It expands on the functionality of part 1 by adding a full-featured GUI, event reminders, and more.

## Description

This application provides a comprehensive, windowed interface for managing events and calendars. It incorporates all the specifications from Part 1, but with a GUI instead of a command-line interface.

## Features

* **GUI Interface**: Provides a user-friendly, windowed application for all interactions.
* **Calendar Loading**: Allows the user to select and load an iCal (`.ics`) file with events.
* **Sorted Event Lists**: Displays loaded events in a list or table, sorted by their proximity to the current time.
* **Event Management**: Offers options to create new events, edit existing ones, and mark tasks as complete.
* **Reminders**: Monitors the current time and loaded events to display reminders for upcoming appointments or tasks.
* **Maven-Based**: The project is built and managed using Apache Maven.

## Prerequisites

* Java
* Apache Maven

## Dependencies

This project relies on the following libraries:

* **`net.fortuna.ical4j`**: A library used to parse, manipulate, and save iCalendar data. The project instructions allow for the use of a third-party library for this purpose.
* **`gr.hua.dit.oop2.calendar`**: A custom library provided for the project to interact with the system time.

## Setup and Installation

1.  Clone the repository to your local machine.
2.  Ensure your `pom.xml` file is configured to download the required `calendar` library. You *may* need to add the specified repository and dependency to your `pom.xml` file.

    **Add this repository block:**
    ```xml
    <repositories>
        <repository>
            <id>gitlab-maven</id>
            <url>[https://gitlab.com/api/v4/projects/41008035/packages/maven](https://gitlab.com/api/v4/projects/41008035/packages/maven)</url>
        </repository>
    </repositories>
    ```

    **Add this dependency to your `<dependencies>` block:**
    ```xml
    <dependency>
        <groupId>gr.hua.dit.oop2</groupId>
        <artifactId>calendar</artifactId>
        <version>1.0.0</version>
    </dependency>
    ```

3.  Build the project from the root directory using Maven. This will compile the code and package it into an executable JAR file in the `target/` directory.
    ```bash
    mvn clean package
    ```

## How to Run

After building, you can run the application directly from the command line.

**Syntax:**
```bash
java -jar target/Calendar-1.0-SNAPSHOT.jar
```

Running this command will launch the graphical user interface. Within the application, you can load a calendar file and begin managing your events.

## Areas for Improvement

- Multi-Calendar Support: The application could be enhanced to support multiple calendars simultaneously.
This would allow the user to load several calendars from different files at once.
A filtering mechanism could also be implemented, enabling users to view events based on the calendar they belong to.
- User Interface Refinements: While functional, the GUI could be made more intuitive and visually appealing.
This could involve using more modern UI components, improving layout and spacing, and adding icons for a better user experience.
For example, a calendar view could be added for a better visual representation of events, similar to what is described in the File Fetcher output.
- Error Handling: The application could provide more detailed and user-friendly error messages, especially when dealing with unparsable files or other unexpected issues.
- Persistence: Currently, the application saves the calendar file when the user explicitly chooses the "Save" option.
An autosave feature could be implemented to prevent data loss in case of unexpected closures.

## Authors

| Student ID | Name                         |
|------------|------------------------------|
| it22149    | Alexandros-Georgios Zarkalis |
| it2022134  | Exarchou Athos               |
