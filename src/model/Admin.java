package model;

import java.util.ArrayList;

public class Admin extends Employee {

    public Admin(String username, String firstName, String lastName, String
            password, String userType, ArrayList<String> email, String
            status, int emailCount, String address, String city, String
            state, String zipcode, int employee, String phone) {
        super(username, firstName, lastName, password, userType, email, status, emailCount, address, city, state, zipcode, employee, phone);
    }
}
