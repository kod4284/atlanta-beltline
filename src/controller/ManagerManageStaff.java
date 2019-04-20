package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Session;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagerManageStaff implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void btnActionBack(Event event) {
        if (Session.user.isEmployee() && Session.user.isManager()) {
            try {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                        .getWindow();
                Parent root = FXMLLoader.load(getClass()
                        .getResource
                                ("../view/Manager_Functionality_Only" +
                                        ".fxml"));
                primaryStage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Manager_Functionality_Only.fxml");
            }
        } else if (Session.user.isEmployeeVisitor() && Session.user.isManager()) {
            try {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                        .getWindow();
                Parent root = FXMLLoader.load(getClass()
                        .getResource
                                ("../view/Manager_Visitor_Functionality" +
                                        ".fxml"));
                primaryStage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Cannot load Manager_Visitor_Functionality_Only.fxml");
            }
        }
    }

}
