package com.project.booking.Controllers;

import com.project.booking.Booking.*;
import com.project.booking.Services.BookingService;

import java.util.List;
import java.util.stream.Collectors;

public class BookingController {

    private BookingService bookingService = new BookingService();

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

}
