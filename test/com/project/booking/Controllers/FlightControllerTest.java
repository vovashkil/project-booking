package com.project.booking.Controllers;

import com.project.booking.Booking.Flight;
import org.junit.Assert;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.project.booking.Constants.DataUtil.*;
import static org.junit.Assert.*;

public class FlightControllerTest {

    @Test
    public void returnsListWithSizeZeroWhenOriginsNotMatched() {

        LocalDate date = LocalDate.now(ZoneId.of(TIME_ZONE));
        ZoneOffset zoneOffset = date.atStartOfDay(ZoneId.of(TIME_ZONE)).getOffset();

        FlightController flights = new FlightController();

        String flightNumber = "FL0001";
        long flightDepartureTime = LocalDateTime.parse(
                "11/01/2019 00:01",
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
        ).toInstant(zoneOffset).toEpochMilli();
        long flightEstDuration = LocalTime.parse(
                "01:45",
                DateTimeFormatter.ofPattern(TIME_FORMAT)
        ).toNanoOfDay();
        String fligthOrigin = "Origin1";
        String flightDestination = "Destination1";
        int flightNumSeats = 200;

        String searchOrigin = "Origin2";
        String searchDestination = "Destination1";
        String searchDate = LocalDate.parse(
                "10/01/2019",
                DateTimeFormatter.ofPattern(DATE_FORMAT)
        ).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        int searchPassengersNumber = 1;

        Flight flight1 = new Flight(
                flightNumber,
                flightDepartureTime,
                flightEstDuration,
                fligthOrigin,
                flightDestination,
                flightNumSeats);

        flights.saveFlight(flight1);

        List<List<Flight>> result = flights.getFlightsMatchedCriteria(
                searchOrigin,
                searchDestination,
                searchDate,
                searchPassengersNumber
        );

        Assert.assertEquals(result.size(), 0);

    }

    @Test
    public void returnsListWithSizeZeroWhenDestinationsNotMatched() {

        LocalDate date = LocalDate.now(ZoneId.of(TIME_ZONE));
        ZoneOffset zoneOffset = date.atStartOfDay(ZoneId.of(TIME_ZONE)).getOffset();

        FlightController flights = new FlightController();

        String flightNumber = "FL0001";
        long flightDepartureTime = LocalDateTime.parse(
                "11/01/2019 00:01",
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
        ).toInstant(zoneOffset).toEpochMilli();
        long flightEstDuration = LocalTime.parse(
                "01:45",
                DateTimeFormatter.ofPattern(TIME_FORMAT)
        ).toNanoOfDay();
        String fligthOrigin = "Origin1";
        String flightDestination = "Destination1";
        int flightNumSeats = 200;

        String searchOrigin = "Origin1";
        String searchDestination = "Destination2";
        String searchDate = LocalDate.parse(
                "10/01/2019",
                DateTimeFormatter.ofPattern(DATE_FORMAT)
        ).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        int searchPassengersNumber = 1;

        Flight flight1 = new Flight(
                flightNumber,
                flightDepartureTime,
                flightEstDuration,
                fligthOrigin,
                flightDestination,
                flightNumSeats);

        flights.saveFlight(flight1);

        List<List<Flight>> result = flights.getFlightsMatchedCriteria(
                searchOrigin,
                searchDestination,
                searchDate,
                searchPassengersNumber
        );

        Assert.assertEquals(result.size(), 0);

    }

    @Test
    public void returnsListWithSizeZeroWhenSearchedTimeIsAfterDepartureTime() {

        LocalDate date = LocalDate.now(ZoneId.of(TIME_ZONE));
        ZoneOffset zoneOffset = date.atStartOfDay(ZoneId.of(TIME_ZONE)).getOffset();

        FlightController flights = new FlightController();

        String flightNumber = "FL0001";
        long flightDepartureTime = LocalDateTime.parse(
                "10/01/2019 23:59",
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
        ).toInstant(zoneOffset).toEpochMilli();
        long flightEstDuration = LocalTime.parse(
                "01:45",
                DateTimeFormatter.ofPattern(TIME_FORMAT)
        ).toNanoOfDay();
        String fligthOrigin = "Origin1";
        String flightDestination = "Destination1";
        int flightNumSeats = 200;

        String searchOrigin = "Origin1";
        String searchDestination = "Destination1";
        String searchDate = LocalDate.parse(
                "11/01/2019",
                DateTimeFormatter.ofPattern(DATE_FORMAT)
        ).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        int searchPassengersNumber = 1;

        Flight flight1 = new Flight(
                flightNumber,
                flightDepartureTime,
                flightEstDuration,
                fligthOrigin,
                flightDestination,
                flightNumSeats);

        flights.saveFlight(flight1);

        List<List<Flight>> result = flights.getFlightsMatchedCriteria(
                searchOrigin,
                searchDestination,
                searchDate,
                searchPassengersNumber
        );

        Assert.assertEquals(result.size(), 0);

    }

    @Test
    public void returnsListWithSizeOneWhenSearchedTimeIsBeforeDepartureTime() {

        LocalDate date = LocalDate.now(ZoneId.of(TIME_ZONE));
        ZoneOffset zoneOffset = date.atStartOfDay(ZoneId.of(TIME_ZONE)).getOffset();

        FlightController flights = new FlightController();

        String flightNumber = "FL0001";
        long flightDepartureTime = LocalDateTime.parse(
                "11/01/2019 00:01",
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
        ).toInstant(zoneOffset).toEpochMilli();
        long flightEstDuration = LocalTime.parse(
                "01:45",
                DateTimeFormatter.ofPattern(TIME_FORMAT)
        ).toNanoOfDay();
        String fligthOrigin = "Origin1";
        String flightDestination = "Destination1";
        int flightNumSeats = 200;

        String searchOrigin = "Origin1";
        String searchDestination = "Destination1";
        String searchDate = LocalDate.parse(
                "11/01/2019",
                DateTimeFormatter.ofPattern(DATE_FORMAT)
        ).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        int searchPassengersNumber = 1;

        Flight flight1 = new Flight(
                flightNumber,
                flightDepartureTime,
                flightEstDuration,
                fligthOrigin,
                flightDestination,
                flightNumSeats);

        flights.saveFlight(flight1);

        List<List<Flight>> result = flights.getFlightsMatchedCriteria(
                searchOrigin,
                searchDestination,
                searchDate,
                searchPassengersNumber
        );

        Assert.assertEquals(result.size(), 1);

    }

    @Test
    public void returnsListWithSizeZeroWhenLayoverTimeLessThanLayoverTimeMin() {

        LocalDate date = LocalDate.now(ZoneId.of(TIME_ZONE));
        ZoneOffset zoneOffset = date.atStartOfDay(ZoneId.of(TIME_ZONE)).getOffset();

        FlightController flights = new FlightController();

        String flightNumber = "FL0001";
        long flightDepartureTime = LocalDateTime.parse(
                "11/01/2019 00:01",
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
        ).toInstant(zoneOffset).toEpochMilli();
        long flightEstDuration = LocalTime.parse(
                "02:00",
                DateTimeFormatter.ofPattern(TIME_FORMAT)
        ).toNanoOfDay();
        String fligthOrigin = "Origin1";
        String flightDestination = "Destination1";
        int flightNumSeats = 200;

        Flight flight1 = new Flight(
                flightNumber,
                flightDepartureTime,
                flightEstDuration,
                fligthOrigin,
                flightDestination,
                flightNumSeats);

        flights.saveFlight(flight1);

        String flightNumber2 = "FL0002";
        long flightDepartureTime2 = Instant.ofEpochMilli(flightDepartureTime)
                .plusNanos(flightEstDuration)
                .plus(LAYOVER_TIME_MIN_MINS, ChronoUnit.MINUTES)
                .minusSeconds(1)
                .toEpochMilli();
        long flightEstDuration2 = LocalTime.parse(
                "01:45",
                DateTimeFormatter.ofPattern(TIME_FORMAT)
        ).toNanoOfDay();
        String flightDestination2 = "Destination2";
        int flightNumSeats2 = 200;

        Flight flight2 = new Flight(
                flightNumber2,
                flightDepartureTime2,
                flightEstDuration2,
                flightDestination,
                flightDestination2,
                flightNumSeats2);

        flights.saveFlight(flight2);

        String searchOrigin = "Origin1";
        String searchDestination = "Destination2";
        String searchDate = LocalDate.parse(
                "11/01/2019",
                DateTimeFormatter.ofPattern(DATE_FORMAT)
        ).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        int searchPassengersNumber = 1;

        List<List<Flight>> result = flights.getFlightsMatchedCriteria(
                searchOrigin,
                searchDestination,
                searchDate,
                searchPassengersNumber
        );

        Assert.assertEquals(result.size(), 0);

    }

    @Test
    public void returnsListWithSizeOneWhenLayoverTimeMoreThanLayoverTimeMin() {

        LocalDate date = LocalDate.now(ZoneId.of(TIME_ZONE));
        ZoneOffset zoneOffset = date.atStartOfDay(ZoneId.of(TIME_ZONE)).getOffset();

        FlightController flights = new FlightController();

        String flightNumber = "FL0001";
        long flightDepartureTime = LocalDateTime.parse(
                "11/01/2019 00:01",
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
        ).toInstant(zoneOffset).toEpochMilli();
        long flightEstDuration = LocalTime.parse(
                "02:00",
                DateTimeFormatter.ofPattern(TIME_FORMAT)
        ).toNanoOfDay();
        String fligthOrigin = "Origin1";
        String flightDestination = "Destination1";
        int flightNumSeats = 200;

        Flight flight1 = new Flight(
                flightNumber,
                flightDepartureTime,
                flightEstDuration,
                fligthOrigin,
                flightDestination,
                flightNumSeats);

        flights.saveFlight(flight1);

        String flightNumber2 = "FL0002";
        long flightDepartureTime2 = Instant.ofEpochMilli(flightDepartureTime)
                .plusNanos(flightEstDuration)
                .plus(LAYOVER_TIME_MIN_MINS, ChronoUnit.MINUTES)
                .toEpochMilli();
        long flightEstDuration2 = LocalTime.parse(
                "01:45",
                DateTimeFormatter.ofPattern(TIME_FORMAT)
        ).toNanoOfDay();
        String flightDestination2 = "Destination2";
        int flightNumSeats2 = 200;

        Flight flight2 = new Flight(
                flightNumber2,
                flightDepartureTime2,
                flightEstDuration2,
                flightDestination,
                flightDestination2,
                flightNumSeats2);

        flights.saveFlight(flight2);

        String searchOrigin = "Origin1";
        String searchDestination = "Destination2";
        String searchDate = LocalDate.parse(
                "11/01/2019",
                DateTimeFormatter.ofPattern(DATE_FORMAT)
        ).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        int searchPassengersNumber = 1;

        List<List<Flight>> result = flights.getFlightsMatchedCriteria(
                searchOrigin,
                searchDestination,
                searchDate,
                searchPassengersNumber
        );

        Assert.assertEquals(result.size(), 1);

    }

    @Test
    public void returnsListWithSizeZeroWhenLayoverTimeMoreThanLayoverTimeMax() {

        LocalDate date = LocalDate.now(ZoneId.of(TIME_ZONE));
        ZoneOffset zoneOffset = date.atStartOfDay(ZoneId.of(TIME_ZONE)).getOffset();

        FlightController flights = new FlightController();

        String flightNumber = "FL0001";
        long flightDepartureTime = LocalDateTime.parse(
                "11/01/2019 00:01",
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
        ).toInstant(zoneOffset).toEpochMilli();
        long flightEstDuration = LocalTime.parse(
                "02:00",
                DateTimeFormatter.ofPattern(TIME_FORMAT)
        ).toNanoOfDay();
        String fligthOrigin = "Origin1";
        String flightDestination = "Destination1";
        int flightNumSeats = 200;

        Flight flight1 = new Flight(
                flightNumber,
                flightDepartureTime,
                flightEstDuration,
                fligthOrigin,
                flightDestination,
                flightNumSeats);

        flights.saveFlight(flight1);

        String flightNumber2 = "FL0002";
        long flightDepartureTime2 = Instant.ofEpochMilli(flightDepartureTime)
                .plusNanos(flightEstDuration)
                .plus(LAYOVER_TIME_MAX_HRS, ChronoUnit.HOURS)
                .plusSeconds(1)
                .toEpochMilli();
        long flightEstDuration2 = LocalTime.parse(
                "01:45",
                DateTimeFormatter.ofPattern(TIME_FORMAT)
        ).toNanoOfDay();
        String flightDestination2 = "Destination2";
        int flightNumSeats2 = 200;

        Flight flight2 = new Flight(
                flightNumber2,
                flightDepartureTime2,
                flightEstDuration2,
                flightDestination,
                flightDestination2,
                flightNumSeats2);

        flights.saveFlight(flight2);

        String searchOrigin = "Origin1";
        String searchDestination = "Destination2";
        String searchDate = LocalDate.parse(
                "11/01/2019",
                DateTimeFormatter.ofPattern(DATE_FORMAT)
        ).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        int searchPassengersNumber = 1;

        List<List<Flight>> result = flights.getFlightsMatchedCriteria(
                searchOrigin,
                searchDestination,
                searchDate,
                searchPassengersNumber
        );

        Assert.assertEquals(result.size(), 0);

    }

    @Test
    public void returnsListWithSizeOneWhenLayoverTimeLessThanLayoverTimeMax() {

        LocalDate date = LocalDate.now(ZoneId.of(TIME_ZONE));
        ZoneOffset zoneOffset = date.atStartOfDay(ZoneId.of(TIME_ZONE)).getOffset();

        FlightController flights = new FlightController();

        String flightNumber = "FL0001";
        long flightDepartureTime = LocalDateTime.parse(
                "11/01/2019 00:01",
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
        ).toInstant(zoneOffset).toEpochMilli();
        long flightEstDuration = LocalTime.parse(
                "02:00",
                DateTimeFormatter.ofPattern(TIME_FORMAT)
        ).toNanoOfDay();
        String fligthOrigin = "Origin1";
        String flightDestination = "Destination1";
        int flightNumSeats = 200;

        Flight flight1 = new Flight(
                flightNumber,
                flightDepartureTime,
                flightEstDuration,
                fligthOrigin,
                flightDestination,
                flightNumSeats);

        flights.saveFlight(flight1);

        String flightNumber2 = "FL0002";
        long flightDepartureTime2 = Instant.ofEpochMilli(flightDepartureTime)
                .plusNanos(flightEstDuration)
                .plus(LAYOVER_TIME_MAX_HRS, ChronoUnit.HOURS)
                .toEpochMilli();
        long flightEstDuration2 = LocalTime.parse(
                "01:45",
                DateTimeFormatter.ofPattern(TIME_FORMAT)
        ).toNanoOfDay();
        String flightDestination2 = "Destination2";
        int flightNumSeats2 = 200;

        Flight flight2 = new Flight(
                flightNumber2,
                flightDepartureTime2,
                flightEstDuration2,
                flightDestination,
                flightDestination2,
                flightNumSeats2);

        flights.saveFlight(flight2);

        String searchOrigin = "Origin1";
        String searchDestination = "Destination2";
        String searchDate = LocalDate.parse(
                "11/01/2019",
                DateTimeFormatter.ofPattern(DATE_FORMAT)
        ).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        int searchPassengersNumber = 1;

        List<List<Flight>> result = flights.getFlightsMatchedCriteria(
                searchOrigin,
                searchDestination,
                searchDate,
                searchPassengersNumber
        );

        Assert.assertEquals(result.size(), 1);

    }

    @Test
    public void testingPrintFlightInformationOutput() {

        LocalDate date = LocalDate.now(ZoneId.of(TIME_ZONE));
        ZoneOffset zoneOffset = date.atStartOfDay(ZoneId.of(TIME_ZONE)).getOffset();

        FlightController flights = new FlightController();

        String flightNumber = "FL0001";
        long flightDepartureTime = LocalDateTime.parse(
                "11/01/2019 00:01",
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
        ).toInstant(zoneOffset).toEpochMilli();
        long flightEstDuration = LocalTime.parse(
                "02:00",
                DateTimeFormatter.ofPattern(TIME_FORMAT)
        ).toNanoOfDay();
        String fligthOrigin = "Origin1";
        String flightDestination = "Destination1";
        int flightNumSeats = 200;

        Flight flight1 = new Flight(
                flightNumber,
                flightDepartureTime,
                flightEstDuration,
                fligthOrigin,
                flightDestination,
                flightNumSeats);

        String PRINT_FORMAT = "| %-7s | %-10s | %-5s | %-30s | %8s | %15s |\n";

//        flights.printFlightWithSeats(flight1, PRINT_FORMAT);

    }

}