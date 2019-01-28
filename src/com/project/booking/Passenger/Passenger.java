package com.project.booking.Passenger;

import com.project.booking.Constants.Sex;
import com.project.booking.Persons.Person;

import java.io.Serializable;
import java.util.Objects;

public class Passenger extends Person implements Serializable {
    private int pass_ID;
    private String passportNumber;

    private static int count = 0;

    {
        this.pass_ID = ++count;
    }

    public int getPass_ID() {
        return pass_ID;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public Passenger(String name, String surname, Long birthDate, Sex sex, String passportNumber) {
        super(name, surname, birthDate, sex);
        this.passportNumber = passportNumber;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "pass_ID=" + pass_ID +
                ", passportNumber='" + passportNumber + '\'' +
                "} " + super.toString();
    }
}
