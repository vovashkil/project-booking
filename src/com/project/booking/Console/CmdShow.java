package com.project.booking.Console;

import com.project.booking.Constants.Airports;
import com.project.booking.Controllers.Storage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import static com.project.booking.Constants.DataUtil.DATE_TIME_FORMAT;
import static com.project.booking.Constants.DataUtil.TIME_ZONE;

public class CmdShow extends CommandBase implements Command {
    public CmdShow(Logger log, Storage storage) {
        super(log, storage);
    }

    @Override
    public String text() {
        return "SHOW";
    }

    @Override
    public String description() {
        return "Online table";
    }

    @Override
    public void doCommand() {
        log.info(String.format("%s executing", this.text()));

        final String PRINT_FORMAT = "| %-7s | %-10s | %-5s | %-30s | %8s |\n";
        final String DASHES = new String(new char[76]).replace("\0", "-");

        System.out.printf("%-64s\n", "Online Table Airport: Kiev Boryspil, "
                + LocalDateTime.now(ZoneId.of(TIME_ZONE))
                .format(DateTimeFormatter
                        .ofPattern(DATE_TIME_FORMAT)));

        System.out.printf("%s\n", DASHES);
        System.out.printf(PRINT_FORMAT, "Flight", "Date", "Time", "Destination", "Duration");
        System.out.printf("%s\n", DASHES);

        storage.getFlights().printAllSortedCurrent24Hours(Airports.KBP.getName(), PRINT_FORMAT);

        System.out.printf("%s\n", DASHES);
    }

    @Override
    public boolean isAllowToUnAuth() {
        return true;
    }
}
