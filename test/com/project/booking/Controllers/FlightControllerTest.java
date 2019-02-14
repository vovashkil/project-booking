package com.project.booking.Controllers;

import com.project.booking.Booking.Flight;
import com.project.booking.Booking.Passenger;
import com.project.booking.Booking.Person;
import com.project.booking.Constants.Sex;
import com.project.booking.Services.FlightService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static com.project.booking.Constants.DataUtil.TIME_ZONE;
import static org.junit.Assert.*;

public class FlightControllerTest {

    @Test
    public void returnsListWithSizeZeroWhenNoMatch() {

        FlightController flights = new FlightController();

        Flight flight1 = new Flight("FL0001", 1549981129543L, 41043979000000L, "Origin1", "Destination1", 200);

        flights.saveFlight(flight1);

        List<List<Flight>> result = flights.getFlightsMatchedCriteria("Origin2", "Destination1", "12/12/2018", 1);

        Assert.assertEquals(result.size(),0);
//        Assertions.assertThat(result.containsAll(Collections.));

            System.out.println("date time = " +
                LocalDateTime.now()
                        .toInstant(LocalDateTime.now(ZoneId.of(TIME_ZONE)).atZone(ZoneId.of(TIME_ZONE)).getOffset()).toEpochMilli());

        System.out.println("est time = " +
                LocalTime.now(ZoneId.of(TIME_ZONE)).minusHours(12).toNanoOfDay());

    }

    @Test
    public void returnsListWithSizeOneWhenMatches() {

        FlightController flights = new FlightController();

        Flight flight1 = new Flight("FL0001", 1549981129543L, 41043979000000L, "Origin1", "Destination1", 200);

        flights.saveFlight(flight1);

        List<List<Flight>> result = flights.getFlightsMatchedCriteria("Origin1", "Destination1", "12/12/2018", 1);

        Assert.assertEquals(result.size(),1);

    }

    @Test
    public void returnsListWithSizeOneWhenConnectingFlightsMatch() {

        FlightController flights = new FlightController();

        Flight flight1 = new Flight("FL0001", 1549981129543L, 41043979000000L, "Origin1", "Destination1", 200);
        Flight flight2 = new Flight("FL0002", 1559981129543L, 41043979000000L, "Destination1", "Destination2", 200);
        Flight flight3 = new Flight("FL0003", 1549981129543L, 41043979000000L, "Origin3", "Destination3", 200);

        flights.saveFlight(flight1);
        flights.saveFlight(flight2);
        flights.saveFlight(flight3);

        List<List<Flight>> result = flights.getFlightsMatchedCriteria("Origin1", "Destination2", "12/12/2018", 1);

        Assert.assertEquals(result.size(),1);

    }


}