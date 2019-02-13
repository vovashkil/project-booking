package com.project.booking.Controllers;

import com.project.booking.Booking.Flight;
import com.project.booking.Constants.DataUtil;
import com.project.booking.Services.FlightService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.booking.Constants.ComUtil.dateLongToString;
import static com.project.booking.Constants.ComUtil.parseDate;
import static com.project.booking.Constants.ComUtil.timeOfDayLongToString;

public class FlightController implements DataUtil {

    private FlightService flightService = new FlightService();

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
                                    destination.equalsIgnoreCase(connectingFlight.getDestination()) &&
                                            flight.getDestination().equalsIgnoreCase(connectingFlight.getOrigin()) &&
                                            connectingFlight.getDepartureDateTime() > flight.getDepartureDateTime()
//                                            connectingFlight.getDepartureDateTime() > (flight.getDepartureDateTime() + flight.getEstFlightDuration()) &&
//                                            connectingFlight.getDepartureDateTime() < (flight.getDepartureDateTime() + flight.getEstFlightDuration() + 12 * 60 * 60 * 1000) &&
//                                            ((connectingFlight.getMaxNumSeats() - connectingFlight.getPassengersOnBoard()) >= passengersNumber)

                            ).forEach(connectingFlight -> {

                        List<Flight> connectingFlights = new ArrayList<>();
                        connectingFlights.add(flight);
                        connectingFlights.add(connectingFlight);
                        result.add(connectingFlights);

                    });
                });

        return result;

    }

}
