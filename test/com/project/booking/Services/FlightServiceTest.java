package com.project.booking.Services;

import com.project.booking.Booking.Flight;
import com.project.booking.Booking.Passenger;
import com.project.booking.Booking.Person;
import com.project.booking.Constants.Sex;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FlightServiceTest {

    public void getAllFlightsReturnsEmptyListWhenNoFlights() {

        FlightService flightsService = new FlightService();

        int result = flightsService.getAllFlights().size();

        Assert.assertEquals(result, 0);
    }

    @Test
    public void getAllFlightsReturnsNonEmptyListWhenFlightsAdded() {

        FlightService flightsService = new FlightService();


        flightsService.saveFlight(new Flight("flight1", 0, 0, "orig1", "dest1", 200));
        flightsService.saveFlight(new Flight("flight2", 0, 0, "orig1", "dest2", 200));
        flightsService.saveFlight(new Flight("flight3", 0, 0, "orig1", "dest3", 200));

        int result = flightsService.getAllFlights().size();

        Assert.assertEquals(result, 3);

    }

    @Test
    public void saveDataAndThenReadDataReturnsTheSameList() {

        String FILE_PATH="./files/test/flights.db";
        FlightService flightsService = new FlightService();

        Flight flight1 = new Flight("flight1", 0, 0, "orig1", "dest1", 200);
        Flight flight2 = new Flight("flight2", 0, 0, "orig1", "dest2", 250);
        flight1.addPassenger(new Passenger("Vasia", "Pupkin", 0L, Sex.MALE, "AA1111"));
        flight1.addPassenger(new Passenger("Peria", "Petrov", 0L, Sex.MALE, "AA2222"));
        flightsService.saveFlight(flight1);
        flightsService.saveFlight(flight2);
        flightsService.saveData(FILE_PATH);

        FlightService flightsService2 = new FlightService();
        flightsService2.readData(FILE_PATH);

        assertThat(flightsService.getAllFlights().equals(
                flightsService2.getAllFlights())).isTrue();

    }


    @Test
    public void returnsTrueIfSaveDataAndThenReadDataReturnsTheSamePassengersList() {

        String FILE_PATH="./files/test/flights.db";
        FlightService flightsService = new FlightService();

        Flight flight1 = new Flight("flight1", 0, 0, "orig1", "dest1", 200);
        flight1.addPassenger(new Passenger("Vasia", "Pupkin", 0L, Sex.MALE, "AA1111"));
        flight1.addPassenger(new Passenger("Peria", "Petrov", 0L, Sex.MALE, "AA2222"));
        flight1.addPassenger(new Passenger("Peria", "Petrov", 1000000L, Sex.MALE, "AA3333"));
        flight1.addPassenger(new Passenger("Peria", "Petrov", 1000000L, Sex.MALE, "BB3333"));
        flight1.addPassenger(new Passenger("Vania", "Ivanov", 1000000L, Sex.MALE, "AA4444"));
        flightsService.saveFlight(flight1);
        List<Person> passListBeforeSave = flight1.getPassengers();
        flightsService.saveData(FILE_PATH);

        FlightService flightsService2 = new FlightService();
        flightsService2.readData(FILE_PATH);

        List<Person> passListAfterRead = flightsService2
                .getFlightById(flightsService2.getAllFlights().indexOf(flight1)).getPassengers();

        assertThat(passListBeforeSave.equals(
                passListAfterRead)).isTrue();

    }

    @Test
    public void returnsFalseIfPassengersListsAreNotTheSame() {

        String FILE_PATH="./files/test/flights.db";
        FlightService flightsService = new FlightService();

        Flight flight1 = new Flight("flight1", 0, 0, "orig1", "dest1", 200);
        flight1.addPassenger(new Passenger("Vasia", "Pupkin", 0L, Sex.MALE, "AA1111"));
        flight1.addPassenger(new Passenger("Peria", "Petrov", 0L, Sex.MALE, "AA2222"));
        flight1.addPassenger(new Passenger("Peria", "Petrov", 1000000L, Sex.MALE, "AA3333"));
        flight1.addPassenger(new Passenger("Peria", "Petrov", 1000000L, Sex.MALE, "BB3333"));
        flight1.addPassenger(new Passenger("Vania", "Ivanov", 1000000L, Sex.MALE, "AA4444"));
        flightsService.saveFlight(flight1);
        List<Person> passListBeforeSave = flight1.getPassengers();
        flightsService.saveData(FILE_PATH);

        FlightService flightsService2 = new FlightService();
        flightsService2.readData(FILE_PATH);

        Flight flightReadFromFile = flightsService2
                .getFlightById(flightsService2.getAllFlights().indexOf(flight1));

        List<Person> passListAfterRead = flightsService2
                .getFlightById(flightsService2.getAllFlights().indexOf(flight1)).getPassengers();

        assertThat(passListBeforeSave.equals(
                passListAfterRead)).isTrue();

        flightReadFromFile.addPassenger(new Passenger("Masha", "Petrova", 14500000L, Sex.FEMALE, "ZZ7777"));

        passListAfterRead = flightsService2
                .getFlightById(flightsService2.getAllFlights().indexOf(flight1)).getPassengers();

        assertThat(passListBeforeSave.equals(
                passListAfterRead)).isFalse();

    }

}