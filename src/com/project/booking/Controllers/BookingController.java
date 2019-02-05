package com.project.booking.Controllers;

import com.project.booking.Booking.Booking;
import com.project.booking.Services.BookingService;

import java.util.List;

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
}