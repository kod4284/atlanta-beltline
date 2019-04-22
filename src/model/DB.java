package model;

import java.sql.*;

public class DB {

    public static String url =
            "jdbc:mysql://localhost:3306/atlanta_beltline?useSSL=false" +
                    "&useUnicode=true" +
                    "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static String user  = "root";
    public static String password  = "rootroot";
    public static Connection readDB() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = null;
        try {
            // db parameters


            // create a connection to the database
            conn = DriverManager.getConnection(DB.url, DB.user, DB.password);

            // more processing here
            // ...
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try{
                if(conn != null) {
                    System.out.println("success!");
                    conn.close();

                }

            }catch(SQLException ex){
                System.out.println(ex.getMessage());

            }
        }
        return null;

    }
}
/* boilerplate
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select count(username) as cnt, username from user_email " +
                    "where email = ?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, email_field.getText());
                ResultSet rs = pst.executeQuery();

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
 */

/*
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Field input Confirmation");
                alert.setContentText("The date successfully inserted!");
                alert.showAndWait();

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Email input Warning");
                alert.setContentText("The Email doesn't match, try it again!");
                alert.showAndWait();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR Dialog");
                alert.setHeaderText("User input ERROR");
                alert.setContentText("The email or password input doesn't " +
                        "exist, try it " +
                        "again with anther input!");
                alert.showAndWait();
 */