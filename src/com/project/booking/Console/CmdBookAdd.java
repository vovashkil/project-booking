package com.project.booking.Console;

import com.project.booking.Booking.Flight;
import com.project.booking.Constants.Airports;
import com.project.booking.Controllers.Storage;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class CmdBookAdd extends CommandBase implements Command {
    private Auth a;

    public CmdBookAdd(Logger log, Storage storage, Auth a) {
        super(log, storage);
        this.a = a;
    }

    @Override
    public String text() {
        return "BOOK";
    }

    @Override
    public String description() {
        return "Flights search/booking";
    }

    @Override
    public void doCommand() {
        log.info(String.format("%s executing", this.text()));

        System.out.println("Flight search and booking...");

        List<List<Flight>> searchResult = storage.getFlights().searchFlightsForBooking(Airports.KBP.getName());

        if (searchResult.size() != 0) {
            boolean controlSearchAndBooking = true;

            while (controlSearchAndBooking) {
                storage.getFlights().printListFlightsdResultMenu(searchResult);

                System.out.print("Please enter flight order number [1-" + searchResult.size() + "] to book or 0 to return: ");

                int choiceSearchAndBooking;
                try {
                    Scanner input = new Scanner(System.in);
                    choiceSearchAndBooking = input.nextInt();

                } catch (InputMismatchException e) {
                    choiceSearchAndBooking = -1;
                }

                if (choiceSearchAndBooking >= 1 && choiceSearchAndBooking <= searchResult.size()) {
                    storage.getBookings().makingBooking(searchResult.get(choiceSearchAndBooking - 1), storage.getUser(), storage.getFlights().getPassengersCount());
                    storage.getFlights().setPassengersCount(0);
                    controlSearchAndBooking = false;
                } else if (choiceSearchAndBooking == 0) {
                    controlSearchAndBooking = false;
                } else
                    System.out.println(
                            "Your choice is wrong. Please enter the flight order number [1-" +
                                    searchResult.size() + "] to book or 0 to return.");
            }
        }
        else{
            System.out.println("Sorry, no flight matching the criteria found. Repeat yor search.");
        }
    }

    @Override
    public boolean isAllowToUnAuth() {
        return (a.isAuth());
    }
}
