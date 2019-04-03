package model;

import java.util.ArrayList;

public class Session {
    public static User user;

    public static void makeUserDummyData() {
        ArrayList email = new ArrayList();
        email.add("kodw0402@gmail.com");
        user = new User("Ko", "Daewoong", "1234",
                email, "Approved", 1);
    }
}
