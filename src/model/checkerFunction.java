package model;


import javafx.scene.control.Alert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public static boolean laterThanCurrentTime(String input) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        if (input.compareTo(formatter.format(date)) < 0) {
            return false;
        }
        return true;
    }
    public static boolean verifyStartEndDate(String startDate, String endDate) {
        if (startDate.compareTo(endDate) > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The date end date should be later than start date!");
            alert.showAndWait();
            return false;
        }
        return true;
    }
    public static boolean verifyPriceRange(String min, String max) {
        if (min.compareTo(max) > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The price range min is bigger than max!");
            alert.showAndWait();
            return false;
        }
        return true;
    }
    public static boolean verifyRange(String min, String max) {
        if (min.compareTo(max) > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The second input must be bigger than the first input!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public static boolean verifyBetweenDate(String startDate, String endDate,
                                            String input) {
        if (input.compareTo(startDate) >= 0 && input.compareTo(endDate) <= 0 ) {
            return true;
        }
        return false;
    }
    public static boolean verifyDateFormat(String strDate)
    {
        /* Check if date is 'null' */
        if (strDate.trim().equals(""))
        {
            return true;
        }
        /* Date is not 'null' */
        else
        {
            /*
             * Set preferred date format,
             * For example MM-dd-yyyy, MM.dd.yyyy,dd.MM.yyyy etc.*/
            SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
            sdfrmt.setLenient(false);
            /* Create Date object
             * parse the string into date
             */
            try
            {
                Date javaDate = sdfrmt.parse(strDate);
                System.out.println(strDate+" is valid date format");
            }
            /* Date format is invalid */
            catch (ParseException e)
            {
                System.out.println(strDate+" is Invalid Date format");
                return false;
            }
            /* Return true if date format is valid */
            return true;
        }
    }

    public static boolean verifyNumeric(String price) {
        try {
            double d = Double.parseDouble(price);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }
    public static boolean verifyZip(String zip) {
        String regex = "^[0-9]{5}";
        Pattern pattern = Pattern.compile(regex);
        return (zip != null) && pattern.matcher(zip).matches();
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
    public static boolean isStringAsInteger(String str) {
        try {
            Double num = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            System.out.println(str + " is not a number");
            return false;
        }
        System.out.println(str + " is a number");
        return true;
    }
    public static String formatPhone(String phone) {
        return phone.replaceFirst("(\\d{3})(\\d{3})(\\d+)","$1-$2-$3");
    }
    public static long deFormatAndInt(String str) {
        return Long.parseUnsignedLong(str.replaceAll("[^\\d]", "" ));
    }
    public static boolean validatePhone(String phoneNo) {
         if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}")) {
             return true;
         } else {
             return false;
         }

    }

}
