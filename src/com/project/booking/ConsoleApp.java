package com.project.booking;

import com.project.booking.Console.Auth;
import com.project.booking.Console.Command;
import com.project.booking.Console.Commands;
import com.project.booking.Constants.DataUtil;
import com.project.booking.Constants.FileUtil;
import com.project.booking.Controllers.Storage;
import com.project.booking.Logger.AppLogger;

import java.util.*;

import java.util.concurrent.atomic.AtomicInteger;

class ConsoleApp implements FileUtil, DataUtil {
    Auth a = new Auth();

    private final Storage storage = new Storage();
    List<Command> commands = Commands.all(AppLogger.logger, storage, a);

    public ConsoleApp() {
        storage.getCustomers().readData(CUSTOMERS_FILE_PATH);
        storage.getFlights().readData(FLIGHTS_FILE_PATH);
        storage.getBookings().readData(BOOKINGS_FILE_PATH);
    }

    private void printMainMenu() {
        Optional<Command> cmd;
        AtomicInteger index = new AtomicInteger();
        commands
                .stream()
                .filter(command -> command.isAllowToUnAuth())
                .forEach(command -> System.out.printf("%d. %s (%s)\n", index.addAndGet(1), command.description(), command.text()));
    }

    void startApp() {

        AppLogger.logger.info("Start Console Application...");

        storage.getCustomers().getCustomerGuest();
        System.out.println("Hello Guest!\nPlease select follow command:");

        Scanner in = new Scanner(System.in);
        Optional<Command> cmd;
        Boolean isExit = false;
        do {
            printMainMenu();
            System.out.print("Please enter your choice (");
            commands
                    .stream()
                    .filter(Command::isAllowToUnAuth).forEach(command -> System.out.printf("%s/", command.text()));
            System.out.print("):");
//            System.out.printf("Please enter your choice [%d-%d]: ", 1, commands
//                    .stream()
//                    .filter(Command::isAllowToUnAuth)
//                    .count());

            String line = in.nextLine().trim();
            cmd = commands
                    .stream()
                    .filter(Command::isAllowToUnAuth)
                    .filter(command -> command.text().equalsIgnoreCase(line))
                    .findFirst();

            cmd.ifPresent(Command::doCommand);

            if (cmd.isPresent()) {
                System.out.println("> " + cmd.get().text());
            } else {
                System.out.println("No such command in offer list!");
                System.out.println("> " + line.toUpperCase());
            }
            try {
                isExit = cmd.get().isExit();
            } catch (Exception e) {
                e.getMessage();
            }
        } while (!isExit);
        System.out.println("Bye.");
    }
}

