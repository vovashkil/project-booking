package com.project.booking.Console;

import com.project.booking.Controllers.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Commands {
    public static List<Command> all(Logger log, Storage storage, Auth a) {
        return new ArrayList<Command>() {{
            add(new CmdShow(log, storage));
            add(new CmdFlightInfo(log, storage));
            add(new CmdBookAdd(log, storage, a));
            add(new CmdBookCancel(log, storage, a));
            add(new CmdFlightsMy(log, storage, a));
            add(new CmdLogin(log, storage, a));
            add(new CmdRegister(log, storage, a));
            add(new CmdCloseSession(log, storage, a));
            add(new CmdFlightsCreate(log, storage));
            add(new CmdExit(log, storage));
            //test command
//            add(new CmdFlightsAll(log, storage));
//            add(new CmdFlightsLoad(log, storage));
//            add(new CmdFlightsSave(log, storage));
        }};
    }

//            System.out.println("1. Online table.");               //CmdShow
//            System.out.println("2. Flight information.");         //CmdFlightInfo
//            System.out.println("3. Flights search and booking."); //CmdBookAdd
//            System.out.println("4. Booking cancelling.");         //CmdBookCancel
//            System.out.println("5. My flights.");                 //CmdFlightsMy
//            System.out.println("6. Close session.");              //CmdCloseSession
//            System.out.println("7. Resetting/Re-creating flights db from schedule file.");//CmdFlightsCreate
//            System.out.println("8. Exit.");                       //CmdExit
//            System.out.println("12. test. Display all flights.");//CmdFlightsAll
//            System.out.println("13. test. Load flights from file.");//CmdFlightsLoad
//            System.out.println("14. test. Save flights to file.");//CmdFlightsSave

}
