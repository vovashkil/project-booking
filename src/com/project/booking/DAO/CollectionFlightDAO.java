package com.project.booking.DAO;


import com.project.booking.Booking.Flight;
import com.project.booking.Logger.FlightLogger;

import java.io.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static com.project.booking.Constants.ComUtil.dateTimeToLong;
import static com.project.booking.Constants.ComUtil.parseTime;
import static com.project.booking.Constants.DataUtil.TIME_ZONE;

public class CollectionFlightDAO implements DAO<Flight> {

    private List<Flight> flightsList;
    private FlightLogger logger;

    {

        logger = new FlightLogger();

    }

    public CollectionFlightDAO() {

        flightsList = new ArrayList();

    }

    @Override
    public List<Flight> getAll() {

        logger.info("Getting all flights list...");

        return flightsList;

    }

    @Override
    public Flight get(int index) {

        logger.info("Getting flight by imdex = " + index + "...");

        Flight result = null;

        if (index >= 0 && index < flightsList.size())
            result = flightsList.get(index);

        return result;
    }

    @Override
    public void save(Flight flight) {

        logger.info("Saving information to db, flight = " + flight + "...");

        if (flight != null) {

            if (flightsList.contains(flight)) {

                flightsList.set(flightsList.indexOf(flight), flight);

            } else {

                flightsList.add(flight);

            }
        }

    }

    @Override
    public boolean remove(int index) {

        logger.info("Deleting flight by imdex = " + index + "...");

        boolean result = false;

        if (index >= 0 && index < flightsList.size()) {
            flightsList.remove(index);
            result = true;
        }

        return result;

    }

    @Override
    public boolean remove(Flight flight) {

        logger.info("Deleting flight by object = " + flight + "...");

        return flightsList.remove(flight);

    }

    @Override
    public void saveData(String filePath) {

        logger.info("Saving flights db to file...");

        try {

            if (new File(filePath).getParentFile().mkdirs())
                logger.info("Filepath created successfully.");
            FileOutputStream fileOutput = new FileOutputStream(filePath);
            ObjectOutputStream streamOutput = new ObjectOutputStream(fileOutput);
            streamOutput.writeObject(flightsList);
            streamOutput.close();
            fileOutput.close();

        } catch (IOException e) {

            logger.error(e.getMessage());

        }

    }

    @Override
    public void readData(String filePath) {

        logger.info("Reading flights db from file...");

        List<Flight> listLoaded = null;

        try {

            FileInputStream fileInput = new FileInputStream(filePath);
            ObjectInputStream inputStream = new ObjectInputStream(fileInput);
            listLoaded = (List<Flight>) inputStream.readObject();
            inputStream.close();
            fileInput.close();
            loadData(listLoaded);

        } catch (ClassNotFoundException | IOException e) {

            logger.error(e.getMessage());

        }

    }

    @Override
    public void loadData(List<Flight> flightsLoaded) {
        logger.info("Loading flights info db...");

        if (flightsLoaded != null) {

            LocalTime currentTime = LocalTime.now(ZoneId.of(TIME_ZONE));
            LocalDate currentDate = LocalDate.now(ZoneId.of(TIME_ZONE));
            ZoneOffset zoneOffset = currentDate.atStartOfDay(ZoneId.of(TIME_ZONE)).getOffset();

            flightsLoaded.forEach(flight -> {

                LocalDate flightDepartureDate = currentDate;
                LocalTime flightDepartureTime = Instant
                        .ofEpochMilli(flight.getDepartureDateTime())
                        .atOffset(zoneOffset)
                        .toLocalTime();

                if (flightDepartureTime.isBefore(currentTime)) {

                    flightDepartureDate = currentDate.plusDays(1);

                }

                long departureDateTimeLong = LocalDateTime.of(
                        flightDepartureDate,
                        flightDepartureTime
                ).toInstant(zoneOffset).toEpochMilli();

                flight.setDepartureDateTime(departureDateTimeLong);
                this.save(flight);

            });
        }

    }

}

