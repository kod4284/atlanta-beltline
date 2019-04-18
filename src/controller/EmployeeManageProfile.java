package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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
    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML TextField phone;
    @FXML Label username;
    @FXML Label employee_id;
    @FXML Label siteName;
    @FXML Label address;
    @FXML CheckBox isVisitor;
    @FXML GridPane emailsPane;
    @FXML Button addBtn;
    @FXML TextField emailField;

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
                sql = ("select employee_id, phone, employee_address, employee_city," +
                        " employee_state, employee_zipcode from " +
                        "employee where username=?;");
                pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                rs = pst.executeQuery();
                rs.next();
                Session.user.setEmployeeId(rs.getString("employee_id"));
                Session.user.setPhone(rs.getString("phone"));
                Session.user.setEmployeeAddress(rs.getString
                        ("employee_address"));
                Session.user.setCity(rs.getString("employee_city"));
                Session.user.setState(rs.getString("employee_state"));
                Session.user.setZipcode(rs.getString("employee_zipcode"));

                //query4

                sql = ("select email from user_email where username=?;");
                pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                rs = pst.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getString("email"));
                    Session.user.getEmail().add(rs.getString("email"));
                }

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

        showInfo();


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
    public void btnActionRegister(ActionEvent event) {

    }
    private void showInfo() {
        firstName.setText(Session.user.getFirstName());
        lastName.setText(Session.user.getLastName());
        phone.setText(Session.user.getPhone());
        username.setText(Session.user.getUsername());
        employee_id.setText(Session.user.getEmployeeId());
        address.setText(Session.user.getEmployeeAddress() +", "+ Session.user
                .getCity() + ", " + Session.user.getState() + " " + Session
                .user
                .getZipcode());
        siteName.setText(Session.user.getSiteName());
        isVisitor.setSelected(Session.user.isVisitor());

    }
    private void fillEmails() {

    }
}
