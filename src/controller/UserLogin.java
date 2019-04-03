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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserLogin implements Initializable {

    @FXML private TextField email_field;
    @FXML private PasswordField password_field;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Session.makeUserDummyData();
        System.out.println(Session.user.getEmail());
        System.out.println(Session.user.getPassword());
    }

    /**
     * This is a method to get an action from Login button
     * @param event Action from Login button
     */
    public void btnActionLogin(ActionEvent event) {
        //query
        //Temp statement assuming User login
        Session.makeUserDummyData();
        if (Session.user.getEmail().contains(email_field.getText()) && Session
                .user.getPassword().equals(password_field.getText())) {
            //if (user is User)


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

            //else if (user is Manager)....

        } else if (Session.user.getEmail().contains(email_field.getText()) &&
                !Session.user.getPassword().equals(password_field.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Password input Warning");
            alert.setContentText("The password doesn't match, try it again!");
            alert.showAndWait();
            System.out.println(email_field.getText());

        } else if (!Session.user.getEmail().contains(email_field.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Email input Warning");
            alert.setContentText("The email doesn't exist, try it again!");
            alert.showAndWait();
            System.out.println(email_field.getText());

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
}
