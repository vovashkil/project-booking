package com.project.booking.Controllers;

import com.project.booking.Booking.*;
import com.project.booking.Constants.DataUtil;
import com.project.booking.Constants.PersonType;
import com.project.booking.Services.BookingService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.booking.Constants.ComUtil.dateLongToString;
import static com.project.booking.Constants.DataUtil.DATE_TIME_FORMAT;
import static com.project.booking.Constants.DataUtil.TIME_ZONE;

public class BookingController {

    private BookingService bookingService = new BookingService();
    private FlightController flightController = new FlightController();
    private CustomerController customerController = new CustomerController();

    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    public void displayAllBookings() {

        bookingService.displayAllBookings();

    }

    public void saveBooking(Booking booking) {

        bookingService.saveBooking(booking);

    }

    public void saveData(String filePath) {

        bookingService.saveData(filePath);

    }

    public void readData(String filePath) {

        bookingService.readData(filePath);

    }

    public void deleteBookingByIndex(int index) {

        bookingService.deleteBookingByIndex(index);

    }

    public void deleteBookingByObject(Booking booking) {

        bookingService.deleteBookingByObject(booking);

    }

    public int count() {

        return bookingService.count();

    }

    public Booking getBookingById(int index) {

        return bookingService.getBookingById(index);

    }

    public Booking getBookingByBookingNumber(long bookingNumber) {

        return bookingService.getAllBookings()
                .stream()
                .filter(booking -> booking.getBookingNumber() == bookingNumber)
                .findAny().orElse(null);

    }

    public Booking createBooking(List<Flight> flights, Customer customer, List<Person> passengers) {
        Booking result = null;

        if (flights != null && customer != null && passengers.size() > 0) {

            result = new Booking(flights, customer, passengers);
            passengers.forEach(passenger->flights.forEach(flight->flight.addPassenger(passenger)));
            saveBooking(result);
        }

        return result;
    }

    public void cancelBooking(long bookingNumber) {

        Booking booking = getBookingByBookingNumber(bookingNumber);
        if (booking != null) {

            booking.getPassengers().forEach(passenger->booking.getFlights().forEach(flight->flight.deletePassenger(passenger)));
            deleteBookingByObject(booking);

        }

    }

    public List<Booking> getAllBookingsByNameAndSurname(String name, String surname) {

        return
                bookingService.getAllBookings().stream()
                        .filter(x ->
                                bookingContainsPassengerWithName(x, name)
                                        && bookingContainsPassengerWithSurname(x, surname)
                                        || x.getCustomer().getName().equalsIgnoreCase(name)
                                        && x.getCustomer().getSurname().equalsIgnoreCase(surname))
                        .collect(Collectors.toList());

    }

    private boolean bookingContainsPassengerWithName(Booking booking, String name) {
        return
                !booking.getPassengers().stream()
                        .filter(x -> x.getName().equalsIgnoreCase(name))
                        .collect(Collectors.toList()).isEmpty();
    }

    private boolean bookingContainsPassengerWithSurname(Booking booking, String surname) {
        return
                !booking.getPassengers().stream()
                        .filter(x -> x.getSurname().equalsIgnoreCase(surname))
                        .collect(Collectors.toList()).isEmpty();
    }

    private List<Person> createPassengersListForBooking(int number) {
        List<Person> result = new ArrayList();
        for (int i = 0; i < number; i++) {
            System.out.println("Enter passenger #" + (+i + +1) + "'s (of " + number + ") personal data, please... ");
            result.add(customerController.createPerson(PersonType.PASSENGER));
        }
        return result;
    }

    public void makingBooking(Flight flight, Customer customer, int passengersNumberForBooking) {
        flightController.displayFlightInformationWithSeats(flight); //method001_displayingFlightInformation(flight);
        if (flight != null && passengersNumberForBooking > 0) {
            Booking booking = createBooking(flight, customer, createPassengersListForBooking(passengersNumberForBooking));
            if (booking != null) {
                displayBookingInfo(booking);
            }
        }
    }

    private void printBooking(Booking booking, String format) {
        if (booking != null && format.length() > 0) {
            System.out.printf(format,
                    booking.getBookingNumber(),
                    booking.getDateTime().format(DateTimeFormatter
                            .ofPattern(DATE_TIME_FORMAT)),
                    booking.getCustomer().getName().concat(" ").concat(booking.getCustomer().getSurname()),
                    booking.getCustomer().getLoginName(),
                    booking.getPassengers().size());
        }
    }

    public void displayBookingInfo(Booking booking) {

        final String PRINT_FORMAT = "| %-15s | %-18s | %-20s | %-19s | %6s |\n";
        final String PRINT_PASSENGER_FORMAT = "| %-7s | %-30s | %-6s | %-15s | %-20s |\n";
        final String DASHES = new String(new char[94]).replace("\0", "-");

        System.out.printf("%-94s\n", "Current Booking Information on time: "
                + LocalDateTime.now(ZoneId.of(TIME_ZONE))
                .format(DateTimeFormatter
                        .ofPattern(DATE_TIME_FORMAT)));
        System.out.printf("%s\n", DASHES);
        System.out.printf(PRINT_FORMAT, "BookingNumber", "Date and Time", "Customer Info", "E-mail", "Count");
        System.out.printf("%s\n", DASHES);
        printBooking(booking, PRINT_FORMAT);//method006_printBooking(booking, PRINT_FORMAT);
        System.out.printf("%s\n", DASHES);
        flightController.displayFlightInformationWithSeats(booking.getFlight());////method001_displayingFlightInformation(booking.getFlight());
        System.out.printf(PRINT_PASSENGER_FORMAT, "Number", "Passenger Info", "Sex", "Date Of Birth", "Passport Number");
        System.out.printf("%s\n", DASHES);

        booking.getPassengers()
                .stream()
                .forEach(person -> {
                    System.out.printf(PRINT_PASSENGER_FORMAT, booking.getPassengers().indexOf(person) + 1 + " of " + booking.getPassengers().size(),
                            person.getName().concat(" ").concat(person.getSurname()),
                            person.getSex(),
                            dateLongToString(person.getBirthDate(), DataUtil.DATE_FORMAT),
                            ((Passenger) person).getPassportNumber());
                    System.out.printf("%s\n", DASHES);
                });
    }

    public boolean isEmptyBookings() {
        return this.count() == 0;
    }

    public void printCancelBookingMenu(List<Booking> bookings) {
        System.out.println("Displaying bookings...");

        final String PRINT_FORMAT = "| %-15s | %-18s | %-20s | %-19s | %6s |\n";
        final String DASHES = new String(new char[94]).replace("\0", "-");

        System.out.printf("   %s\n   ", DASHES);
        System.out.printf(PRINT_FORMAT, "BookingNumber", "Date and Time", "Customer Info", "E-mail", "Count");
        System.out.printf("   %s\n", DASHES);

        printMultipleBookingsWithOrderNumbers(bookings, PRINT_FORMAT);

        System.out.println("0.   Return to the main menu.");
    }

    public void printMultipleBookingsWithOrderNumbers(List<Booking> bookings, String format) {
        if (bookings.size() > 0)
            bookings.forEach(booking -> {
                System.out.print(bookings.indexOf(booking) + +1 + ". ");
                printBooking(booking, format);
            });
    }

    public boolean bookingNumberIsPresent(long bookingNumber) {
        return getBookingByBookingNumber(bookingNumber) != null;
    }
}
