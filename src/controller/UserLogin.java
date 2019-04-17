package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class UserLogin implements Initializable {

    @FXML private TextField email_field;
    @FXML private PasswordField password_field;
    private User tempUser;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * This is a method to get an action from Login button
     * @param event Action from Login button
     */
    public void btnActionLogin(ActionEvent event) throws Exception {
        //List of Users
        List<User> userInfos = new ArrayList<User>();
        //query
        Class.forName("com.mysql.cj.jdbc.Driver");
        // create a connection to the database
        Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                .password);
        try {
            // sql statements

            //if no row return, go to catch
            String sql = ("select count(username) as cnt, username from user_email " +
                    "where email = ?;");
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, email_field.getText());
            ResultSet rs = pst.executeQuery();
            int cnt = rs.findColumn("cnt");
            //if row exists
            if (cnt >= 1) {
                String username = "";
                String password = "";
                String status = "";
                String userType = "";
                if (rs.next()) {
                    username = rs.getString("username");
                    sql = ("SELECT password, status, user_type FROM" +
                            " user " +
                            "where " +
                            "username = ?;");

                    pst = conn.prepareStatement(sql);
                    pst.setString(1, username);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        password = rs.getString("password");
                        status = rs.getString("status");
                        userType = rs.getString("user_type");
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("User Account doesn't exist");
                        alert.setContentText("try it again!");
                        alert.showAndWait();
                        return;
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("User Account doesn't exist");
                    alert.setContentText("try it again!");
                    alert.showAndWait();
                    return;
                }


                tempUser = new User(username, password, status, userType);
                String hashedPW = SHA256.encrypt(password_field.getText());
                if (!tempUser.getPassword().equals(hashedPW)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Password input Warning");
                    alert.setContentText("The password doesn't match, try it again!");
                    alert.showAndWait();
                    System.out.println(email_field.getText());
                } else if (tempUser.getPassword().equals(password_field
                        .getText()) && tempUser.getStatus().equals(UserStatus
                        .DECLINED.toString())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Your Account is declined");
                    alert.setContentText("Inquire this problem to admin!");
                    alert.showAndWait();
                    System.out.println(email_field.getText());
                } else if (tempUser.getPassword().equals(password_field
                        .getText()) && tempUser.getStatus().equals(UserStatus
                        .PENDING.toString())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Your Account is Pending");
                    alert.setContentText("Wait for the account approved!");
                    alert.showAndWait();
                    System.out.println(email_field.getText());
                } else {
                    try {
                        //Check Employee type
                        Session.user = tempUser; //login

                        System.out.println(Session.user.getUsername());
                        sql = ("SELECT employee_type FROM employee where " +
                                "username = ?;");
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, Session.user.getUsername());
                        rs = pst.executeQuery();
                        rs.next();
                        String type = rs.getString("employee_type");

                        if (Session.user.isEmployee()) {

                            if (type.equals(EmployeeType.STAFF.toString())) {
                                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                                        .getWindow();
                                Parent root = FXMLLoader.load(getClass()
                                        .getResource
                                                ("../view/Staff_Functionality_Only" +
                                                        ".fxml"));
                                primaryStage.setScene(new Scene(root));
                            } else if (type.equals(EmployeeType.MANAGER.toString())) {
                                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                                        .getWindow();
                                Parent root = FXMLLoader.load(getClass()
                                        .getResource
                                                ("../view/Manager_Functionality_Only" +
                                                        ".fxml"));
                                primaryStage.setScene(new Scene(root));
                            } else {
                                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                                        .getWindow();
                                Parent root = FXMLLoader.load(getClass()
                                        .getResource
                                                ("../view/Administrator_Functionality_Only" +
                                                        ".fxml"));
                                primaryStage.setScene(new Scene(root));
                            }

                        } else if (Session.user.isEmployeeVisitor()) {
                            if (type.equals(EmployeeType.STAFF.toString())) {
                                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                                        .getWindow();
                                Parent root = FXMLLoader.load(getClass()
                                        .getResource
                                                ("../view/Staff_Visitor_Functionality" +
                                                        ".fxml"));
                                primaryStage.setScene(new Scene(root));
                            } else if (type.equals(EmployeeType.MANAGER
                                    .toString())) {
                                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                                        .getWindow();
                                Parent root = FXMLLoader.load(getClass()
                                        .getResource
                                                ("../view/Manager_Visitor_Functionality" +
                                                        ".fxml"));
                                primaryStage.setScene(new Scene(root));
                            } else {
                                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                                        .getWindow();
                                Parent root = FXMLLoader.load(getClass()
                                        .getResource
                                                ("../view/Administrator_Visitor_Functionality" +
                                                        ".fxml"));
                                primaryStage.setScene(new Scene(root));
                            }

                        } else if(Session.user.isUser()) {
                            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                                    .getWindow();
                            Parent root = FXMLLoader.load(getClass()
                                    .getResource
                                            ("../view/User_Functionality" +
                                                    ".fxml"));
                            primaryStage.setScene(new Scene(root));
                        } else {
                            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                                    .getWindow();
                            Parent root = FXMLLoader.load(getClass()
                                    .getResource
                                            ("../view/Visitor_Functionality" +
                                                    ".fxml"));
                            primaryStage.setScene(new Scene(root));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Email input Warning");
                alert.setContentText("The Email doesn't match, try it again!");
                alert.showAndWait();
            }

        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("ErrorCode: " + e.getErrorCode());
            if (e.getErrorCode() == 0) { // if the email input doesn't exist
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR Dialog");
                alert.setHeaderText("User input ERROR");
                alert.setContentText("The email or password input doesn't " +
                        "exist, try it " +
                        "again with anther input!");
                alert.showAndWait();
                System.out.println(email_field.getText());
            }
        } finally {
            if(conn != null) {
                conn.close();
            }
        }

    }

    /**
     * This is a method to get an action from Register button
     * @param event Action from Register button
     */
    @FXML
    public void btnActionRegister(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Register_Navigation.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }

    }

    private User getUser(List<User> users, String username) {
        for (User u: users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
}
