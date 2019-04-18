package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Session;

import java.io.IOException;

public class UserTransitHistory {
        ////if should be totally 7 implementation
        @FXML
        public void btnActionUserHistoryBack(ActionEvent event) {
            //Employee manager
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
            } else if (Session.user.isEmployee() && Session.user.isStaff()) {
                try {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view/Staff_Functionality_Only" +
                                    ".fxml"));
                    primaryStage.setScene(new Scene(root));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Cannot load Staff_Functionality_Only.fxml");
                }
            }

        }


}
