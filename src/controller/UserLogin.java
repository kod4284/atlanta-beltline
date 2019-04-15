package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.StageStyle;
import model.Session;
import model.DB;
import model.User;
import model.UserStatus;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static model.DB.*;

public class UserLogin implements Initializable {

    @FXML private TextField email_field;
    @FXML private PasswordField password_field;
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
            // db parameters
            Statement st = conn.createStatement();
            String sql = ("SELECT username, password, status, user_type FROM user;");
            ResultSet rs = st.executeQuery(sql);
            while (!rs.isLast()) {
                rs.next();
                String username = rs.getString("username");
                String password = rs.getString("password");
                String status = rs.getString("status");
                String userType = rs.getString("user_type");
                userInfos.add(new User(username, password, status, userType));
            }
            User tempUser = getUser(userInfos, email_field.getText());
            if (tempUser != null) {
                if (!tempUser.getPassword().equals(password_field.getText())) {
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
                        // should add the function that works depending on
                        // usertype.
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

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Email input Warning");
                alert.setContentText("The email doesn't exist, try it again!");
                alert.showAndWait();
                System.out.println(email_field.getText());

            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if(conn != null) {
                System.out.println(userInfos);
                System.out.println("Loading success!");
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
