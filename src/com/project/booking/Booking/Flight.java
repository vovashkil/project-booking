package com.project.booking.Booking;

import com.project.booking.Constants.DataUtil;

import java.io.Serializable;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Flight implements DataUtil, Serializable {

    private String flightNumber;
    private long departureDateTime;
    private long estFlightDuration;
    private String origin;
    private String destination;
    private int maxNumSeats;
    private List<Person> passengers;

    public Flight(String flightNumber, long departureDateTime, long estFlightDuration, String origin, String destination, int maxNumSeats) {

        this.flightNumber = flightNumber;
        this.departureDateTime = departureDateTime;
        this.estFlightDuration = estFlightDuration;
        this.origin = origin;
        this.destination = destination;
        this.maxNumSeats = maxNumSeats;
        this.passengers = new ArrayList();

    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public long getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(long departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public long getEstFlightDuration() {
        return estFlightDuration;
    }

    public void setEstFlightDuration(long estFlightDuration) {
        this.estFlightDuration = estFlightDuration;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getPassengersOnBoard() {
        return passengers.size();
    }

    public int getMaxNumSeats() {
        return maxNumSeats;
    }

    public List<Person> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Person> passengers) {
        this.passengers = passengers;
    }

    public boolean addPassenger(Person passenger) {

        if (!passengers.contains(passenger) &&
                passengers.size() < maxNumSeats &&
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return flightNumber.equals(flight.getFlightNumber());
    }

    @Override
    public int hashCode() {

        int result = 11;
        int coef = 31;

        return coef * result + flightNumber.hashCode();

    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber='" + flightNumber + '\'' +
                ", departureDate ='" +
                Instant.ofEpochMilli(departureDateTime)
                        .atZone(ZoneId.of(TIME_ZONE))
                        .toLocalDateTime()
                        .format(DateTimeFormatter
                                .ofPattern(DATE_FORMAT)) + '\'' +
                ", departureTime ='" +
                Instant.ofEpochMilli(departureDateTime)
                        .atZone(ZoneId.of(TIME_ZONE))
                        .toLocalDateTime()
                        .format(DateTimeFormatter
                                .ofPattern(TIME_FORMAT)) + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", passengersOnBoard=" + getPassengersOnBoard() +
                ", maxNumSeats=" + maxNumSeats +
                ", duration=" +
                LocalTime.ofNanoOfDay(estFlightDuration)
                        .format(DateTimeFormatter.ofPattern(TIME_FORMAT)) +
                '}';
    }

    public String prettyFormat() {
        return flightNumber + " | " +
                Instant.ofEpochMilli(departureDateTime)
                        .atZone(ZoneId.of(TIME_ZONE))
                        .toLocalDateTime()
                        .format(DateTimeFormatter
                                .ofPattern(TIME_FORMAT)) + " | " +
                destination;
    }
}
