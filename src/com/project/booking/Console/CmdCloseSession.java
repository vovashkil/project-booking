package com.project.booking.Console;

import com.project.booking.Controllers.Storage;

import java.util.logging.Logger;

public class CmdCloseSession extends CommandBase implements Command {
    private Auth a;

    public CmdCloseSession(Logger log, Storage storage, Auth a) {
        super(log, storage);
        this.a = a;
    }

    @Override
    public String text() {
        return "CLOSE";
    }

    @Override
    public String description() {
        return "Close session";
    }

    @Override
    public void doCommand() {
        log.info(String.format("%s executing", this.text()));
        storage.setUser(storage.getCustomers().getCustomerGuest());
        a.setAuth(false);
    }

    @Override
    public boolean isAllowToUnAuth() {
        return (a.isAuth());
    }
}
