package model;

import javafx.scene.control.Alert;

import java.util.regex.Pattern;

public class checkerFunction {
    /**
     * This method checks whether an email is valid.
     * @param email email address of a user
     * @return returns true if email address is valid or false if email address if invalid
     */
    public static boolean verifyEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        return (email != null) && pat.matcher(email).matches();
    }

    /**
     * This method checks whether password is valid.
     * @param password password
     * @return returns true when pass is valid
     */
    public static boolean verifyPassword(String password) {
        return (password != null) && (password.length() >= 8);
    }
    public static boolean verifyInputs(String email, String password) {
        if (!checkerFunction.verifyEmail(email)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Email input Warning");
            alert.setContentText("Should follow email format!\n" +
                    "ex) hello@gatech.edu");
            alert.showAndWait();
            return false;
        } else if (!checkerFunction.verifyPassword(password)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Password input Warning");
            alert.setContentText("The password should be at least " +
                    "8 characters!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public static boolean verifyInputEmail(String email) {
        if(!checkerFunction.verifyEmail(email)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Email input Warning");
            alert.setContentText("Should follow email format!\n" +
                    "ex) hello@gatech.edu");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }
    public static String formatPhone(String phone) {
        return phone.replaceFirst("(\\d{3})(\\d{3})(\\d+)","$1-$2-$3");
    }
    public static long deFormatAndInt(String str) {
        return Long.parseUnsignedLong(str.replaceAll("[^\\d]", "" ));
    }
    public static boolean validatePhone(String phoneNo) {
         if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {
             return true;
         } else {
             return false;
         }

    }

}
