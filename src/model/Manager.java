package model;

import java.util.ArrayList;

public class Manager extends Employee {
    public Manager(String firstName, String lastName, String password,
                   ArrayList<String> email, String status, int emailCount,
                   String address, String city, String state, int zipcode,
                   int employee, String phone) {
        super(firstName, lastName, password, email, status, emailCount, address, city, state, zipcode, employee, phone);
    }
}
