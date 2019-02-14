package com.project.booking.Booking;

import com.project.booking.Constants.DataUtil;
import com.project.booking.Constants.Sex;

import java.io.Serializable;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Booking implements DataUtil, Serializable {

    private long number;
    private final LocalDateTime dateTime = LocalDateTime.now();
    private Customer customer;
    private List<Person> passengers = new ArrayList();
    private List<Flight> flights = new ArrayList<>();

    public Booking(List<Flight> flights) {

        this.number = setBookingNumber();
        this.flights = flights;
    }

    public Booking(List<Flight> flights, final Customer customer, List<Person> passengers) {
        this.number = setBookingNumber();
        this.flights = flights;
        this.setCustomer(customer);
        this.setPassengers(passengers);
    }

    public long getBookingNumber() {
        return this.number;
    }

    private long setBookingNumber() {

        return dateTime.getYear() * 10000000000L +
                dateTime.getMonth().getValue() * 100000000 +
                dateTime.getDayOfMonth() * 1000000 +
                dateTime.getHour() * 10000 +
                dateTime.getMinute() * 100 +
                dateTime.getSecond();

    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    public List<Person> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Person> passengers) {
        this.passengers = passengers;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlight(List<Flight> flights) {
        this.flights = flights;
    }

    public boolean addPassenger(Person passenger) {
        if (!passengers.contains(passenger) &&
                passenger != null) {
            passengers.add(passenger);
            return true;
        } else
            return false;
    }

    public boolean deletePassenger(Person passenger) {
        if (!passengers.contains(passenger)) return false;

        passengers.remove(passenger);
        return true;
    }

    public boolean deletePassenger(int index) {
        if (index >= 0 && index < passengers.size()) {
            if (!passengers.contains(passengers.get(index))) return false;

            passengers.remove(passengers.get(index));
            return true;
        }
        return false;
    }

    public Person createPerson(String name, String surname, long birthdate, Sex sex, String... args) {
        Person result = null;

        if (args.length == 1) {
            result = new Passenger(name, surname, birthdate, sex, args[0]);
            addPassenger(result);
        }
        if (args.length == 2) {
            result = new Customer(name, surname, birthdate, sex, args[0], args[1]);
            setCustomer((Customer) result);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return number == booking.number;
    }

    @Override
    public int hashCode() {
        int result = 11;
        int coef = 31;

        result = coef * result + (int) number;

        return (int) number;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "number=" + number +
                ", dateTime='" + dateTime.format(DateTimeFormatter
                .ofPattern(DATE_TIME_FORMAT)) + '\'' +
                ", customer=" + customer +
                ",\n\t flight=" + flights +
                ",\n\t\t passengers=" + passengers +
                '}';
    }
}
