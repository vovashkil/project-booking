package com.project.booking;

import com.project.booking.Booking.*;
import com.project.booking.Console.Auth;
import com.project.booking.Console.Command;
import com.project.booking.Console.Commands;
import com.project.booking.Constants.DataUtil;
import com.project.booking.Constants.FileUtil;
import com.project.booking.Constants.PersonType;
import com.project.booking.Constants.Sex;
import com.project.booking.Controllers.Storage;
import com.project.booking.Logger.AppLogger;

import java.util.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.project.booking.Constants.ComUtil.*;

class ConsoleApp implements FileUtil, DataUtil {
    Auth a = new Auth();

    private final Storage storage = new Storage();
    //private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static Customer customerApp;
    List<Command> commands = Commands.all(AppLogger.logger, storage, a);


    public ConsoleApp() {
        storage.getCustomers().readData(CUSTOMERS_FILE_PATH);
    }

    private void printMainMenu2() {
        Optional<Command> cmd;
        AtomicInteger index = new AtomicInteger();
        commands
                .stream()
                .filter(command -> command.isAllowToUnAuth())
                .forEach(command -> {
                    System.out.printf("%d. %s (%s)| ", index.addAndGet(1), command.description(), command.text());
                });
        System.out.println();

//        System.out.println("1. Online table.");
//        System.out.println("2. Flight information.");
//        System.out.println("3. Flights search and booking.");
//        System.out.println("4. Booking cancelling.");
//        System.out.println("5. My flights.");
//        System.out.println("6. Close session.");
//        System.out.println("7. Resetting/Re-creating flights db from schedule file.");
//        System.out.println("8. Exit.");
//        System.out.println("12. test. Display all flights.");
//        System.out.println("13. test. Load flights from file.");
//        System.out.println("14. test. Save flights to file.");

    }

    void startApp2() {

        System.out.println("Hello Guest!\nPlease select follow command:");


        Scanner in = new Scanner(System.in);
        Optional<Command> cmd;
        Boolean isExit = false;
        do {
            printMainMenu2();
            System.out.printf("Please enter your choice [%d-%d]: ", 1, commands.stream().filter(command -> command.isAllowToUnAuth()).count());

            String line = in.nextLine().trim();
            cmd = commands
                    .stream()
                    //.filter(command -> command.isAllowToUnAuth())
                    .filter(command -> command.text().equalsIgnoreCase(line))
                    .findFirst();
            //cmd.ifPresent(Command::doCommand);
            cmd.ifPresentOrElse(Command::doCommand, () -> System.out.println("No such command in offer list!"));
            cmd.ifPresentOrElse(command -> System.out.println("> " + command.text()), () -> System.out.println("> " + line));
            try {
                isExit = cmd.get().isExit();
            } catch (Exception e) {
                e.getMessage();
            }
            // System.out.println("> " + cmd.get().text());
            // } while (!cmd.get().isExit());
        } while (!isExit);
        System.out.println("Bye.");
    }

    void startApp() {

        Methods m = new Methods(storage.getFlights(), storage.getBookings());

        storage.getFlights().readData(FLIGHTS_FILE_PATH);
        storage.getBookings().readData(BOOKINGS_FILE_PATH);

        boolean control = true;

        while (control) {
            printMainMenu();

            Scanner input = new Scanner(System.in);
            System.out.print("Please enter your choice [1-8]: ");

            int choice;

            try {
                choice = input.nextInt();
            } catch (InputMismatchException e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    System.out.println("Displaying online table...");
                    m.method10_displayingOnlineTable();
                    break;
                case 2:
                    System.out.println("Displaying flight information...");
                    m.method20_displayFlightInformation();
                    break;
                case 3:
                    System.out.println("Flight search and booking...");

                    List<Flight> searchResult = m.method30_searchFlights();

                    if (searchResult.size() == 0) {
                        System.out.println("Sorry, no flight matching the criteria found. Repeat yor search.");
                        break;
                    }

                    boolean controlSearchAndBooking = true;

                    while (controlSearchAndBooking) {
                        printSearchFlightsdResultMenu(searchResult, m);

                        System.out.print("Please enter flight order number [1-" +
                                searchResult.size() + "] to book or 0 to return: ");

                        int choiceSearchAndBooking;

                        try {

                            choiceSearchAndBooking = input.nextInt();

                        } catch (InputMismatchException e) {

                            choiceSearchAndBooking = -1;

                        }

                        if (choiceSearchAndBooking >= 1 && choiceSearchAndBooking <= searchResult.size()) {

                            m.method32_makingBooking(searchResult.get(choiceSearchAndBooking - 1), customerApp);
                            m.setPassengersNumberForBooking(0);
                            controlSearchAndBooking = false;

                        } else if (choiceSearchAndBooking == 0) {

                            controlSearchAndBooking = false;

                        } else
                            System.out.println(
                                    "Your choice is wrong. Please enter the flight order number [1-" +
                                            searchResult.size() + "] to book or 0 to return.");
                    }

                    break;
                case 4:
                    System.out.println("Booking cancelling...");

                    boolean controlBookingCancel = true;

                    if (m.method40_bookingsIsEmpty()) {
                        System.out.println("There is no booking made in the DB.");
                        controlBookingCancel = false;
                    }

                    while (controlBookingCancel) {

                        printCancelBookingMenu(m.method42_getAllBookings(), m);

                        System.out.print("Please enter booking ID to cancel or 0 to return: ");

                        long choiceCancelBooking;

                        try {
                            choiceCancelBooking = input.nextLong();
                        } catch (InputMismatchException e) {
                            choiceCancelBooking = -1;
                        }

                        if (choiceCancelBooking == 0) {
                            controlBookingCancel = false;
                        } else if (choiceCancelBooking == -1) {
                            System.out.println(
                                    "Your choice is wrong. Please enter booking ID to cancel or 0 to return: ");
                        } else if (m.method46_bookingNumberIsPresent(choiceCancelBooking)) {

                            System.out.println("Deleteing booking...");
                            m.method48_cancelBooking(choiceCancelBooking);

                            controlBookingCancel = false;

                        } else {
                            System.out.println(
                                    "There is no booking with ID=\'" + choiceCancelBooking + "\' in db. Please enter booking ID to cancel or 0 to return: ");
                        }
                    }

                    break;
                case 5:
                    System.out.println("Display my flights...");
                    m.method50_displayMyFlights();
                    break;
                case 6:
                    for (; ; ) {
                        if (loginCustomer()) {
                            break;
                        }
                    }
                    break;
                case 7:
                    System.out.println("Creating new list of flights from schedule...");
                    m.method70_flightDbFromScheduleFile();
                    break;
                case 8:
                    control = false;
                    storage.getCustomers().saveData(CUSTOMERS_FILE_PATH);
                    m.method80_saveData();
                    break;
                case 12:
                    System.out.println("Displaying entire list of flights...");
                    storage.getFlights().displayAllFlights();
                    break;
                case 13:
                    System.out.println("Loading a list of flights from file...");
                    storage.getFlights().readData(FLIGHTS_FILE_PATH);
                    break;
                case 14:
                    System.out.println("Saving the list of flights to file...");
                    storage.getFlights().saveData(FLIGHTS_FILE_PATH);
                    break;
                default:
                    System.out.println("Your choice is wrong. Please repeat your choice.");
            }
        }

    }

    private void printMainMenu() {

        System.out.println("1. Online table.");
        System.out.println("2. Flight information.");
        System.out.println("3. Flights search and booking.");
        System.out.println("4. Booking cancelling.");
        System.out.println("5. My flights.");
        System.out.println("6. Close session.");
        System.out.println("7. Resetting/Re-creating flights db from schedule file.");
        System.out.println("8. Exit.");
        System.out.println("12. test. Display all flights.");
        System.out.println("13. test. Load flights from file.");
        System.out.println("14. test. Save flights to file.");

    }

    private void printSearchFlightsdResultMenu(List<Flight> flights, Methods m) {

        System.out.println("Found flights matched criteria...");

        final String PRINT_FORMAT = "| %-7s | %-10s | %-5s | %-30s | %8s | %15s |\n";
        final String DASHES = new String(new char[94]).replace("\0", "-");

        System.out.printf("   %s\n   ", DASHES);
        System.out.printf(
                PRINT_FORMAT,
                "Flight", "Date", "Time", "Destination", "Duration", "Available Seats");
        System.out.printf("   %s\n", DASHES);

        m.method003_printMultipleFlightsWithOrderNumbers(flights, PRINT_FORMAT);

        System.out.println("0.   Return to the main menu.");

    }

    private void printCancelBookingMenu(List<Booking> bookings, Methods m) {

        System.out.println("Displaying bookings...");

        final String PRINT_FORMAT = "| %-15s | %-18s | %-20s | %-19s | %6s |\n";
        final String DASHES = new String(new char[94]).replace("\0", "-");

        System.out.printf("   %s\n   ", DASHES);
        System.out.printf(PRINT_FORMAT, "BookingNumber", "Date and Time", "Customer Info", "E-mail", "Count");
        System.out.printf("   %s\n", DASHES);

        m.method007_printMultipleBookingsWithOrderNumbers(bookings, PRINT_FORMAT);

        System.out.println("0.   Return to the main menu.");

    }

    boolean loginCustomer() {
        AppLogger.logger.setLevel(Level.INFO);
        AppLogger.logger.info("Try login for booking ticket");

        boolean result = false;
        customerApp = null;
        String loginName;
        String password;


        Scanner scanner = new Scanner(System.in);
        for (; ; ) {
            System.out.println("Enter \"login\", \"register\", or \"exit\"");
            String input = scanner.nextLine().toUpperCase();

            switch (input) {
                case "LOGIN":
                    System.out.print("LoginName: ");
                    loginName = scanner.nextLine();
                    System.out.print("Password: ");
                    password = scanner.nextLine();
                    customerApp = storage.getCustomers().getCustomerByLogin(loginName, password);
                    if (customerApp != null) {
                        System.out.printf("%s %s, Welcome to booking!!!\n", customerApp.getSurname(), customerApp.getName());
                        result = true;
                    } else {
                        System.out.println("Invalid Username & Password!");
                    }
                    return result;
                case "REGISTER":
                    customerApp = (Customer) createPerson(PersonType.CUSTOMER);
                    storage.getCustomers().saveCustomer(customerApp);
                    if (customerApp != null) {
                        System.out.printf("%s %s, Welcome to booking!!!\n", customerApp.getSurname(), customerApp.getName());
                        result = true;
                    } else {
                        System.out.println("Customer is not registered!");
                    }
                    return result;
                case "EXIT":
                    System.exit(0);
                    break;
                default:
                    System.out.printf("Invalid option (%s), choose login or register!%n", input);
            }
        }
    }

    public void closeSession() {
        customerApp = null;
        loginCustomer();
    }

    private static Person createPerson(PersonType personType) {
        Person result;

        System.out.println("Enter personal data, please... ");

        String name = parseAndValidateInputString(
                "Name: ",
                "^[A-Z][A-Za-z ]+",
                "Name",
                "Vasia"
        );
        String surname = parseAndValidateInputString(
                "Surname: ",
                "^[A-Z][A-Za-z ]+",
                "Surname",
                "Sidorov"
        );
        long birthdate =
                parseDate(
                        parseAndValidateInputString(
                                "BirthDate (dd/MM/yyyy): ",
                                "^[0-9][0-9]/[0-9][0-9]/[12][09][0-9][0-9]",
                                "Date",
                                "21/07/1990"
                        ));
        Sex sex = Sex.valueOf(parseAndValidateInputString(
                "Sex (Male or Female):  ",
                "(?i)Male|(?i)Female",
                "Sex",
                "Male").toUpperCase());

        if (personType == PersonType.CUSTOMER) {
            String loginName = parseAndValidateInputString(
                    "LoginName (your e-mail): ",
                    "^(.+)@(.+)$",
                    "LoginName",
                    "Ivanov@gmail.com"
            );
            String password = parseAndValidateInputString(
                    "Password: ",
                    "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}",
                    "Password",
                    "aaZZa44@"
            );
            /*
            (?=.*[0-9]) a digit must occur at least once
            (?=.*[a-z]) a lower case letter must occur at least once
            (?=.*[A-Z]) an upper case letter must occur at least once
            (?=.*[@#$%^&+=]) a special character must occur at least once
            (?=\\S+$) no whitespace allowed in the entire string
            .{8,} at least 8 characters
            */
            result = new Customer(name, surname, birthdate, sex, loginName, password);
        } else {
            String passNumber = parseAndValidateInputString(
                    "Passport Number: ",
                    "^[A-Z][A-Z][0-9]+",
                    "Passport Number",
                    "AK876543"
            );
            result = new Passenger(name, surname, birthdate, sex, passNumber);
        }
        return result;
    }


}

