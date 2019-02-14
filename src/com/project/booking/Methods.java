package com.project.booking;

import com.opencsv.CSVReader;
import com.project.booking.Booking.*;
import com.project.booking.Constants.DataUtil;
import com.project.booking.Constants.FileUtil;
import com.project.booking.Constants.PersonType;
import com.project.booking.Constants.Sex;
import com.project.booking.Controllers.BookingController;
import com.project.booking.Controllers.FlightController;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.project.booking.Constants.ComUtil.*;
import static com.project.booking.Constants.ComUtil.dateLongToString;

public class Methods implements DataUtil, FileUtil {

//    private FlightController flightsController;
//    private BookingController bookingsController;
//    private int passengersNumberForBooking = 0;
//
//    Methods(FlightController flights, BookingController bookings) {
//        this.flightsController = flights;
//        this.bookingsController = bookings;
//    }
//
//    public void setPassengersNumberForBooking(int passengersNumberForBooking) {
//        this.passengersNumberForBooking = passengersNumberForBooking;
//    }
//
//    void method10_displayingOnlineTable() {
//
//        final String PRINT_FORMAT = "| %-7s | %-10s | %-5s | %-30s | %8s |\n";
//        final String DASHES = new String(new char[76]).replace("\0", "-");
//
//        System.out.printf("%-64s\n", "Online Table Airport: Kiev Boryspil, "
//                + LocalDateTime.now(ZoneId.of(TIME_ZONE))
//                .format(DateTimeFormatter
//                        .ofPattern(DATE_TIME_FORMAT)));
//
//        System.out.printf("%s\n", DASHES);
//        System.out.printf(PRINT_FORMAT, "Flight", "Date", "Time", "Destination", "Duration");
//        System.out.printf("%s\n", DASHES);
//
//        flightsController.printAllSortedCurrent24Hours(PRINT_FORMAT);
//
//        System.out.printf("%s\n", DASHES);
//
//    }
//
//    void method20_displayFlightInformation() {
//
//        String flightNumber = parseAndValidateInputString(
//                "Enter Flight number: ",
//                "^[A-Za-z][A-Za-z][0-9]+",
//                "Flight number",
//                "PS0779");
//
//        Flight flight = flightsController.getByFlightNumber(flightNumber);
//
//        if (flight != null) {
//
//            method001_displayingFlightInformation(flight);
//
//        } else
//
//            System.out.println("Sorry, there is no flight " + flightNumber + " in the db.");
//
//    }
//
//    List<Flight> method30_searchFlights() {
//        System.out.println("Search flights...");
//
//        String destination = parseAndValidateInputString(
//                "Enter Destination: ",
//                "^[A-Z][A-Za-z ]+",
//                "Destination",
//                "Frankfurt");
//
//        String date = parseAndValidateInputString(
//                "Enter Date: ",
//                "^[0-9][0-9]/[0-9][0-9]/[2][0][1-2][0-9]",
//                "Date",
//                "25/11/2019");
//
//        int number = parseAndValidateInputInteger(
//                "Enter number of passengers: ",
//                1,
//                flightsController.getMaxSeatNumber());
//
//        passengersNumberForBooking = number;
//        return flightsController.getFlightsMatchedCriteria(destination, date, number);
//
//    }
//
//    void method32_makingBooking(Flight flight, Customer customer) {
//
//        System.out.println("Booking...");
//        method001_displayingFlightInformation(flight);
//
//        Booking booking = method34_createBooking(flight, customer, passengersNumberForBooking);
//
//        if (booking != null) method005_displayBookingInfo(booking);
//
//    }
//
//    private Booking method34_createBooking(Flight flight, Customer customer, int passengersNumber) {
//        System.out.println("Creating booking...");
//        Booking result = null;
//
//        if (flight != null && passengersNumber > 0) {
//
//            result = bookingsController
//                    .createBooking(flight,
//                            customer,
//                            method36_createPassengersList(passengersNumber)
//                    );
//
//        }
//
//        return result;
//    }
//
//    private List<Person> method36_createPassengersList(int number) {
//        List<Person> result = new ArrayList();
//
//        for (int i = 0; i < number; i++) {
//
//            System.out.println("Enter passenger #" + (+i + +1) + "'s (of " + number + ") personal data, please... ");
//            result.add(method004_createPerson(PersonType.PASSENGER));
//
//        }
//
//        return result;
//    }
//
//    boolean method40_bookingsIsEmpty() {
//
//        return bookingsController.count() == 0;
//
//    }
//
//    List<Booking> method42_getAllBookings() {
//
//        return bookingsController.getAllBookings();
//
//    }
//
//    private Booking method44_getBookingByBookingNumber(long bookingNumber) {
//
//        return bookingsController.getBookingByBookingNumber(bookingNumber);
//
//    }
//
//    boolean method46_bookingNumberIsPresent(long bookingNumber) {
//        return
//                method44_getBookingByBookingNumber(bookingNumber) != null;
//    }
//
//    void method48_cancelBooking(long bookingNumber) {
//
//        bookingsController.cancelBooking(bookingNumber);
//
//    }
//
//    void method50_displayMyFlights() {
//
//        System.out.println("Enter personal data, please... ");
//        String name = parseAndValidateInputString(
//                "Name: ",
//                "^[A-Z][A-Za-z ]+",
//                "Name",
//                "Vasia");
//        String surname = parseAndValidateInputString(
//                "Surname: ",
//                "^[A-Z][A-Za-z ]+",
//                "Surname",
//                "Sidorov");
//
//        List<Booking> bookings = bookingsController.getAllBookingsByNameAndSurname(name, surname);
//
//        if (bookings.size() > 0 ) {
//
//            bookings.forEach(this::method005_displayBookingInfo);
//
//        } else {
//
//            System.out.printf("%s%s%s\n", "There is no booking on name=%s and surname=%s in the db.", name, surname);
//
//        }
//
//    }
//
//    void method70_flightDbFromScheduleFile() {
//        System.out.println("Resetting/Generating new database of flights...");
//
//        LocalTime currentTime = LocalTime.now(ZoneId.of(TIME_ZONE));
//        LocalDate currentDate = LocalDate.now(ZoneId.of(TIME_ZONE));
//
//        try (
//                Reader reader = Files.newBufferedReader(Paths.get(KBP_SCHEDULE_FILE_PATH));
//                CSVReader csvReader = new CSVReader(reader, ',', '\'', 1);
//        ) {
//
//            String[] nextRecord;
//
//            while ((nextRecord = csvReader.readNext()) != null) {
//
//                long flightDepartureTimeLong = parseTime(nextRecord[1]);
//                long flightDurationTimeLong = parseTime(nextRecord[7]);
//                LocalDate flightDepartureDate = currentDate;
//
//                if (flightDepartureTimeLong <= currentTime.toNanoOfDay())
//                    flightDepartureDate = currentDate.plusDays(1);
//
//                long departureDateTimeLong = dateTimeToLong(
//                        LocalDateTime.of(
//                                flightDepartureDate,
//                                LocalTime.ofNanoOfDay(flightDepartureTimeLong)
//                        )
//                );
//
//                flightsController.saveFlight(
//                        new Flight(nextRecord[0],
//                                departureDateTimeLong,
//                                flightDurationTimeLong,
//                                "Kiev Boryspil",
//                                nextRecord[2],
//                                150
//                        ));
//            }
//        } catch (IOException e) {
//            System.out.println(e.getStackTrace());
//            System.out.println(e.getMessage());
//        }
//    }
//
//    void method80_saveData() {
//
//        flightsController.saveData(FLIGHTS_FILE_PATH);
//        bookingsController.saveData(BOOKINGS_FILE_PATH);
//
//    }
//
//    private void method001_displayingFlightInformation(Flight flight) {
//
//        final String PRINT_FORMAT = "| %-7s | %-10s | %-5s | %-30s | %8s | %15s |\n";
//        final String DASHES = new String(new char[94]).replace("\0", "-");
//
//        System.out.printf("%s\n", "Flight infomation:");
//        System.out.printf("%s\n", DASHES);
//
//        System.out.printf(PRINT_FORMAT,
//                "Flight", "Date", "Time", "Destination", "Duration", "Available Seats"
//        );
//
//        System.out.printf("%s\n", DASHES);
//
//        method002_printFlight(flight, PRINT_FORMAT);
//
//        System.out.printf("%s\n", DASHES);
//
//    }
//
//    private void method002_printFlight(Flight flight, String format) {
//
//        if (flight != null && format.length() > 0)
//            System.out.printf(format,
//                    flight.getFlightNumber(),
//                    dateLongToString(flight.getDepartureDateTime(), DATE_FORMAT),
//                    dateLongToString(flight.getDepartureDateTime(), TIME_FORMAT),
//                    flight.getDestination(),
//                    timeOfDayLongToString(flight.getEstFlightDuration()),
//                    flight.getMaxNumSeats() - flight.getPassengersOnBoard()
//            );
//
//    }
//
//    void method003_printMultipleFlightsWithOrderNumbers(List<Flight> flights, String format) {
//
//        if (flights.size() > 0)
//            flights.forEach(flight -> {
//                System.out.print(flights.indexOf(flight) + +1 + ". ");
//                method002_printFlight(flight, format);
//            });
//
//    }
//
//    private Person method004_createPerson(PersonType personType) {
//        Person result;
//
//        System.out.println("Enter personal data, please... ");
//
//        String name = parseAndValidateInputString(
//                "Name: ",
//                "^[A-Z][A-Za-z ]+",
//                "Name",
//                "Vasia"
//        );
//        String surname = parseAndValidateInputString(
//                "Surname: ",
//                "^[A-Z][A-Za-z ]+",
//                "Surname",
//                "Sidorov"
//        );
//        long birthdate =
//                parseDate(
//                        parseAndValidateInputString(
//                                "BirthDate (dd/MM/yyyy): ",
//                                "^[0-9][0-9]/[0-9][0-9]/[12][09][0-9][0-9]",
//                                "Date",
//                                "21/07/1990"
//                        ));
//        Sex sex = Sex.valueOf(parseAndValidateInputString(
//                "Sex (Male or Female):  ",
//                "(?i)Male|(?i)Female",
//                "Sex",
//                "Male").toUpperCase());
//
//        if (personType == PersonType.CUSTOMER) {
//            String loginName = parseAndValidateInputString(
//                    "LoginName (your e-mail): ",
//                    "^(.+)@(.+)$",
//                    "LoginName",
//                    "Ivanov@gmail.com"
//            );
//            String password = parseAndValidateInputString(
//                    "Password: ",
//                    "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}",
//                    "Password",
//                    "aaZZa44@"
//            );
//            /*
//            (?=.*[0-9]) a digit must occur at least once
//            (?=.*[a-z]) a lower case letter must occur at least once
//            (?=.*[A-Z]) an upper case letter must occur at least once
//            (?=.*[@#$%^&+=]) a special character must occur at least once
//            (?=\\S+$) no whitespace allowed in the entire string
//            .{8,} at least 8 characters
//            */
//            result = new Customer(name, surname, birthdate, sex, loginName, password);
//        } else {
//            String passNumber = parseAndValidateInputString(
//                    "Passport Number: ",
//                    "^[A-Z][A-Z][0-9]+",
//                    "Passport Number",
//                    "AK876543"
//            );
//            result = new Passenger(name, surname, birthdate, sex, passNumber);
//        }
//        return result;
//    }
//
//    private void method005_displayBookingInfo(Booking booking) {
//
//        final String PRINT_FORMAT = "| %-15s | %-18s | %-20s | %-19s | %6s |\n";
//        final String PRINT_PASSENGER_FORMAT = "| %-7s | %-30s | %-6s | %-15s | %-20s |\n";
//        final String DASHES = new String(new char[94]).replace("\0", "-");
//
//        System.out.printf("%-94s\n", "Current Booking Information on time: "
//                + LocalDateTime.now(ZoneId.of(TIME_ZONE))
//                .format(DateTimeFormatter
//                        .ofPattern(DATE_TIME_FORMAT)));
//        System.out.printf("%s\n", DASHES);
//        System.out.printf(PRINT_FORMAT, "BookingNumber", "Date and Time", "Customer Info", "E-mail", "Count");
//        System.out.printf("%s\n", DASHES);
//        method006_printBooking(booking, PRINT_FORMAT);
//        System.out.printf("%s\n", DASHES);
//        method001_displayingFlightInformation(booking.getFlight());
//        System.out.printf(PRINT_PASSENGER_FORMAT, "Number", "Passenger Info", "Sex", "Date Of Birth", "Passport Number");
//        System.out.printf("%s\n", DASHES);
//
//        booking.getPassengers()
//                .stream()
//                .forEach(person -> {
//                    System.out.printf(PRINT_PASSENGER_FORMAT, booking.getPassengers().indexOf(person) + 1 + " of " + booking.getPassengers().size(),
//                            person.getName().concat(" ").concat(person.getSurname()),
//                            person.getSex(),
//                            dateLongToString(person.getBirthDate(), DataUtil.DATE_FORMAT),
//                            ((Passenger) person).getPassportNumber());
//                    System.out.printf("%s\n", DASHES);
//                });
//    }
//
//    private void method006_printBooking(Booking booking, String format) {
//
//        if (booking != null && format.length() > 0)
//
//            System.out.printf(format,
//                    booking.getBookingNumber(),
//                    booking.getDateTime().format(DateTimeFormatter
//                            .ofPattern(DATE_TIME_FORMAT)),
//                    booking.getCustomer().getName().concat(" ").concat(booking.getCustomer().getSurname()),
//                    booking.getCustomer().getLoginName(),
//                    booking.getPassengers().size());
//
//    }
//
//    void method007_printMultipleBookingsWithOrderNumbers(List<Booking> bookings, String format) {
//
//        if (bookings.size() > 0)
//            bookings.forEach(booking -> {
//                System.out.print(bookings.indexOf(booking) + +1 + ". ");
//                method006_printBooking(booking, format);
//            });
//
//    }
}

