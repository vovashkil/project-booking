package com.project.booking.Controllers;

import com.project.booking.Booking.Flight;
import com.project.booking.Constants.DataUtil;
import com.project.booking.Services.FlightService;

import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.project.booking.Constants.ComUtil.*;

public class FlightController implements DataUtil {

    private FlightService flightService = new FlightService();
    private int passengersCount = 0;

    public int getPassengersCount() {
        return passengersCount;
    }

    public void setPassengersCount(int passengersCount) {
        this.passengersCount = passengersCount;
    }

    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    public void displayAllFlights() {

        flightService.displayAllFlights();

    }

    public void saveFlight(Flight flight) {

        flightService.saveFlight(flight);

    }

    public void saveData(String filePath) {

        flightService.saveData(filePath);

    }

    public void readData(String filePath) {

        flightService.readData(filePath);

    }

    public void deleteFlightByIndex(int index) {

        flightService.deleteFlightByIndex(index);

    }

    public void deleteFlightByObject(Flight flight) {

        flightService.deleteFlightByObject(flight);

    }

    public int count() {

        return flightService.count();

    }

    public Flight getFlightById(int index) {
        return flightService.getFlightById(index);
    }

    public void printAllSortedCurrent24Hours(String origin, String format) {
        flightService.getAllFlights()
                .stream()
                .filter(flight -> origin.equals(flight.getOrigin()))
                .sorted(Comparator.comparingLong(Flight::getDepartureDateTime))
                .forEach(flight -> printFlight(flight, format)
                );
    }

    public void printFlight(Flight flight, String format) {
        System.out.printf(format,
                flight.getFlightNumber(),
                dateLongToString(flight.getDepartureDateTime(), DATE_FORMAT),
                dateLongToString(flight.getDepartureDateTime(), TIME_FORMAT),
                flight.getDestination(),
                timeOfDayLongToString(flight.getEstFlightDuration())
        );
    }

    public Flight getByFlightNumber(String origin, String flightNumber) {

        return flightService.getAllFlights()
                .stream()
                .filter(item -> item.getFlightNumber()
                        .equalsIgnoreCase(flightNumber)
                        && origin.equals(item.getOrigin()))
                .findFirst().orElse(null);

    }

    public int getMaxSeatNumber() {

        return
                flightService.getAllFlights()
                        .stream()
                        .mapToInt(Flight::getMaxNumSeats)
                        .max().orElse(-1);

    }

    public List<List<Flight>> getFlightsMatchedCriteria(String origin, String destination, String date, int passengersNumber) {

        List<List<Flight>> result = new ArrayList<>();
        int i = 0;

        List<Flight> singleFlights = flightService.getAllFlights()
                .stream()
                .filter(
                        item -> origin.equalsIgnoreCase(item.getOrigin()) &&
                                item.getDestination().equalsIgnoreCase(destination) &&
                                item.getDepartureDateTime() > parseDate(date) &&
                                ((item.getMaxNumSeats() - item.getPassengersOnBoard()) >= passengersNumber)
                )
                .sorted(Comparator.comparingLong(Flight::getDepartureDateTime))
                .collect(Collectors.toList());

        singleFlights.forEach(flight -> {

            List<Flight> connectingFlights = new ArrayList<>();
            connectingFlights.add(flight);
            result.add(connectingFlights);

        });

        flightService.getAllFlights().stream()
                .filter(flight -> origin.equalsIgnoreCase(
                        flight.getOrigin()) &&
                        !destination.equalsIgnoreCase(flight.getDestination()) &&
                        flight.getDepartureDateTime() > parseDate(date) &&
                        ((flight.getMaxNumSeats() - flight.getPassengersOnBoard()) >= passengersNumber)
                )
                .forEach(flight -> {
                    flightService.getAllFlights().stream()
                            .filter(connectingFlight ->

                                    destination.equalsIgnoreCase(connectingFlight.getDestination())
                                            &&
                                            flight.getDestination().equalsIgnoreCase(connectingFlight.getOrigin())
                                            &&
                                            Duration.between(
                                                    Instant.ofEpochMilli(flight.getDepartureDateTime())
                                                            .atZone(ZoneId.of(TIME_ZONE))
                                                            .toLocalDateTime().plusNanos(flight.getEstFlightDuration()),
                                                    Instant.ofEpochMilli(connectingFlight.getDepartureDateTime())
                                                            .atZone(ZoneId.of(TIME_ZONE))
                                                            .toLocalDateTime()
                                            ).compareTo(Duration.ofMinutes(LAYOVER_TIME_MIN_MINS)) >= 0
                                            &&
                                            Duration.between(
                                                    Instant.ofEpochMilli(flight.getDepartureDateTime())
                                                            .atZone(ZoneId.of(TIME_ZONE))
                                                            .toLocalDateTime().plusNanos(flight.getEstFlightDuration()),
                                                    Instant.ofEpochMilli(connectingFlight.getDepartureDateTime())
                                                            .atZone(ZoneId.of(TIME_ZONE))
                                                            .toLocalDateTime()
                                            ).compareTo(Duration.ofHours(LAYOVER_TIME_MAX_HRS)) <= 0
                                            &&
                                            (connectingFlight.getMaxNumSeats() - connectingFlight.getPassengersOnBoard()) >= passengersNumber

                            ).forEach(connectingFlight -> {

                        List<Flight> connectingFlights = new ArrayList<>();
                        connectingFlights.add(flight);
                        connectingFlights.add(connectingFlight);
                        result.add(connectingFlights);

                    });
                });

        return result;

    }

    public void displayFlightInformationWithSeats(Flight flight, int flightIndex, int flightCount, String printFormatPrefix) {
        final String PRINT_FORMAT = printFormatPrefix.concat("| %-7s | %-10s | %-5s | %-18s | %-18s | %8s | %6s |\n");
        final String DASHES = new String(new char[94]).replace("\0", "-");
        final String PRINT_FORMAT_DASHES = printFormatPrefix.concat("%s\n");

        if (flightIndex == 1) {
            System.out.printf(PRINT_FORMAT_DASHES, "Flight information:");
            System.out.printf(PRINT_FORMAT_DASHES, DASHES);

            System.out.printf(PRINT_FORMAT,
                    "Flight", "Date", "Time", "Origin", "Destination", "Duration", "Av.Sts");
            System.out.printf(PRINT_FORMAT_DASHES, DASHES);
        }
        printFlightWithSeats(flight, PRINT_FORMAT, 1);
        if (flightIndex == flightCount) {
            System.out.printf(PRINT_FORMAT_DASHES, DASHES);
        }
    }

    public void printFlightWithSeats(Flight flight, String format, int index) {
        if (flight != null && format.length() > 0)
            if (index > 1) {
                format = "   ->" + format;
            }
        System.out.printf(format,
                flight.getFlightNumber(),
                dateLongToString(flight.getDepartureDateTime(), DATE_FORMAT),
                dateLongToString(flight.getDepartureDateTime(), TIME_FORMAT),
                flight.getOrigin(),
                flight.getDestination(),
                timeOfDayLongToString(flight.getEstFlightDuration()),
                flight.getMaxNumSeats() - flight.getPassengersOnBoard()
        );
    }

    public List<List<Flight>> searchFlightsForBooking(String origin) {
        String destination = parseAndValidateInputString(
                "Enter Destination: ",
                "^[A-Z][A-Za-z ]+",
                "Destination",
                "Frankfurt");

        String date = parseAndValidateInputString(
                "Enter Date: ",
                "^[0-9][0-9]/[0-9][0-9]/[2][0][1-2][0-9]",
                "Date",
                "25/11/2019");

        this.passengersCount = parseAndValidateInputInteger(
                "Enter number of passengers: ",
                1,
                getMaxSeatNumber());
        return getFlightsMatchedCriteria(origin, destination, date, this.passengersCount);
    }

    public void printMultipleFlightsWithOrderNumbers(List<List<Flight>> flights, String format) {
        if (flights.size() > 0)
            flights.forEach(flight -> {
                System.out.printf("%3d. ", flights.indexOf(flight) + +1);
                AtomicInteger index = new AtomicInteger();
                flight.forEach(item -> printFlightWithSeats(item, format, index.addAndGet(1)));
            });
    }

    public void printListFlightsdResultMenu(List<List<Flight>> flights) {
        System.out.println("Found flights matched criteria...");

        final String PRINT_FORMAT_PREFIX = "     ";
        final String PRINT_FORMAT = "| %-7s | %-10s | %-5s | %-18s | %-18s | %8s | %6s |\n";
        final String DASHES = new String(new char[94]).replace("\0", "-");
        final String PRINT_FORMAT_DASHES = "%s\n";

        System.out.printf(PRINT_FORMAT_PREFIX.concat(PRINT_FORMAT_DASHES), DASHES);

        System.out.printf(PRINT_FORMAT_PREFIX.concat(PRINT_FORMAT),
                "Flight", "Date", "Time", "Origin", "Destination", "Duration", "Av.Sts");
        System.out.printf(PRINT_FORMAT_PREFIX.concat(PRINT_FORMAT_DASHES), DASHES);

        printMultipleFlightsWithOrderNumbers(flights, PRINT_FORMAT);

        System.out.println("  0.   Return to the main menu.");

    }
}
