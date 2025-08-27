# Calendar Management Application (part 1 of 2)

Developed as a group project for the **Object-Oriented Programming-II** course at [Harokopio University of Athens – Dept. of Informatics and Telematics](https://www.dit.hua.gr), **Calendar Management Application (part 1 of 2)** is a command-line Java application for managing events.
It allows users to manage appointments and tasks, storing them in the standard iCal (`.ics`) file format.

## Description

The program operates in two main modes determined by the number of command-line arguments provided:

1.  **Viewing Mode (2 arguments)**: Loads an existing calendar file and displays lists of events sorted by time.
2.  **Editing Mode (1 argument)**: Loads an existing calendar file or creates a new one, then interactively prompts the user to add new events (Appointments or Tasks).

The application handles two distinct types of events:
* **Appointment**: An event with a specific start date/time and duration.
* **Task (Work)**: An event with a completion deadline and a status (completed or not).

## Features

* **iCal Format Support**: Loads and saves calendar data in the standard `.ics` format.
* **Event Creation**: Interactively add new "Appointments" or "Tasks" to a calendar file.
* **Sorted Event Lists**: Displays various lists of events, sorted from the nearest to the furthest in time.
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

The application is run from the command line using the generated JAR file. The behavior depends on the arguments provided.

**Note:** A sample calendar file, `greece.ics`, is provided in the repository to help you test the application's functionality.

### Mode 1: Viewing Event Lists (2 Arguments)

Use this mode to display filtered and sorted lists of events from an `.ics` file.

**Syntax:**
```bash
java -jar target/Calendar-1.0-SNAPSHOT.jar [option] [calendarFile.ics]
```

#### Example:
```bash
# Display events for the rest of the day from sample calendar
java -jar target/Calendar-1.0-SNAPSHOT.jar day greece.ics
```

Available `option` arguments:

| Option    | Description                                                         |
|-----------|---------------------------------------------------------------------|
| day       | Displays future events until the end of the current day.            |
| week      | Displays future events until the end of the current week.           |
| month     | Displays future events until the end of the current month.          |
| pastday   | Displays past events from the start of the current day until now.   |
| pastweek  | Displays past events from the start of the current week until now.  |
| pastmonth | Displays past events from the start of the current month until now. |
| todo      | Displays incomplete tasks whose deadlines have not yet passed.      |
| due       | Displays incomplete tasks whose deadlines have passed.              |

### Mode 2: Adding New Events (1 Argument)

Use this mode to add new events to a `.ics` file. If the file does not exist, it will be created.

**Syntax:**
```bash
java -jar target/Calendar-1.0-SNAPSHOT.jar [calendarFile.ics]
```

#### Example:
```bash
# Start an interactive session to add events to sample calendar
java -jar target/Calendar-1.0-SNAPSHOT.jar greece.ics
```

After running the command, the application will prompt you to enter the details for new appointments or tasks.

## Authors

| Student ID | Name                         |
|------------|------------------------------|
| it22149    | Alexandros-Georgios Zarkalis |
| it2022134  | Exarchou Athos               |
