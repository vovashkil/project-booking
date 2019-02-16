package com.project.booking.Console;

import com.project.booking.Booking.Booking;
import com.project.booking.Controllers.Storage;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static com.project.booking.Constants.ComUtil.parseAndValidateInputString;

public class CmdFlightsMy extends CommandBase implements Command {
    private Auth a;

    public CmdFlightsMy(Logger log, Storage storage, Auth a) {
        super(log, storage);
        this.a = a;
    }

    @Override
    public String text() {
        return "MYFLIGHTS";
    }

    @Override
    public String description() {
        return "My flights";
    }

    @Override
    public void doCommand() {
        log.info(String.format("%s executing", this.text()));
        System.out.println("Display my flights...");

        System.out.println("Enter personal data, please... ");
        String name = parseAndValidateInputString(
                "Name: ",
                "^[A-Z][A-Za-z ]+",
                "Name",
                "Vasia");
        String surname = parseAndValidateInputString(
                "Surname: ",
                "^[A-Z][A-Za-z ]+",
                "Surname",
                "Sidorov");

//        String name = storage.getUser().getName();
//        String surname = storage.getUser().getSurname();

        List<Booking> bookings = storage.getBookings().getAllBookingsByNameAndSurname(name, surname);
        if (bookings.size() > 0 ) {

            AtomicInteger index = new AtomicInteger(0);
            bookings.forEach(booking -> {
                System.out.printf("%d. ", index.addAndGet(1));
                storage.getBookings().displayBookingInfo(booking);
            });
        } else {
            System.out.printf("There is no booking on name=%s and surname=%s in data .\n", name, surname);
        }
    }

    @Override
    public boolean isAllowToUnAuth() {
        return (a.isAuth());
    }
}
