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
