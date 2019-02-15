package com.project.booking.Console;

import com.project.booking.Booking.Flight;
import com.project.booking.Constants.Airports;
import com.project.booking.Controllers.Storage;

import java.util.logging.Logger;

import static com.project.booking.Constants.ComUtil.parseAndValidateInputString;

public class CmdFlightInfo extends CommandBase implements Command {
    public CmdFlightInfo(Logger log, Storage storage) {
        super(log, storage);
    }

    @Override
    public String text() {
        return "FLIGHT";
    }

    @Override
    public String description() {
        return "Flight Info";
    }

    @Override
    public void doCommand() {
        log.info(String.format("%s executing", this.text()));
        System.out.println("Displaying flight information...");

        String flightNumber = parseAndValidateInputString(
                "Enter Flight number: ",
                "^[A-Za-z][A-Za-z][0-9]+",
                "Flight number",
                "PS0779");

        Flight flight = storage.getFlights().getByFlightNumber(Airports.KBP.getName(), flightNumber);

        if (flight != null) {
            storage.getFlights().displayFlightInformationWithSeats(flight, 1, 1, "");
        } else {
            System.out.println("Sorry, there is no flight " + flightNumber + " in the data.");
            log.warning(String.format("No Information found about this flight: %s", flightNumber));
        }
    }

    @Override
    public boolean isAllowToUnAuth() {
        return true;
    }
}
