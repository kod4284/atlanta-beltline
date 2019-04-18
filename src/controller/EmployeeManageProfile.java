package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DB;
import model.Session;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class EmployeeManageProfile implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                // sql statements
                //query1
                String sql = ("select firstname, lastname, username from user" +
                        " where username=?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                ResultSet rs = pst.executeQuery();
                rs.next();

                Session.user.setFirstName(rs.getString("firstname"));
                Session.user.setLastName(rs.getString("lastname"));
                //query2
                if (Session.user.isManager()) {
                    sql = ("select site_name from site where " +
                            "manager_username=?; ");
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, Session.user.getUsername());
                    rs = pst.executeQuery();
                    rs.next();
                    Session.user.setSiteName(rs.getString("site_name"));
                }
                //query3
                sql = ("select employee_id, phone, employee_address from " +
                        "employee where username=?;");
                pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                rs = pst.executeQuery();
                rs.next();
                //Session.user.(rs.getString("site_name"));


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
    }
    public void btnActionBack(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Manager_Functionality_Only.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load Manager_Functionality_Only.fxml");

        }
    }
}
