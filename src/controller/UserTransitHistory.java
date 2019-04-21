package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Session;
import model.TransportType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserTransitHistory implements Initializable {
    @FXML ComboBox<TransportType> transportType;
    @FXML ComboBox<String> containSite;
    @FXML TextField route;
    @FXML TextField startDate;
    @FXML TextField endDate;
    @FXML TableView transitTable;




    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void btnActionTransitHistoryFilter(ActionEvent event) {

    }

    @FXML
    public void btnActionUserHistoryBack(ActionEvent event) {
        try {
            //User Only
            if (Session.user.isUser()) {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                        .getWindow();
                Parent root = FXMLLoader.load(getClass()
                        .getResource("../view/User_Functionality.fxml"));
                primaryStage.setScene(new Scene(root));

                //Employee Only
            } else if (Session.user.isEmployee()) {
                //Manager Only
                if (Session.user.isManager()) {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view/Manager_Functionality_Only.fxml"));
                    primaryStage.setScene(new Scene(root));
                    //Staff Only
                } else if (Session.user.isStaff()) {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view/Staff_Functionality_Only" +
                                    ".fxml"));
                    primaryStage.setScene(new Scene(root));
                    //Administrator Only
                } else {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view" +
                                    "/Administrator_Functionality_Only" +
                                    ".fxml"));
                    primaryStage.setScene(new Scene(root));
                }
                // Employee Visitor
            } else if (Session.user.isEmployeeVisitor()) {
                //Manager-Visitor
                if (Session.user.isManager()) {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view/Manager_Visitor_Functionality.fxml"));
                    primaryStage.setScene(new Scene(root));
                    //Staff-Visitor
                } else if (Session.user.isStaff()) {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view/Staff_Visitor_Functionality" +
                                    ".fxml"));
                    primaryStage.setScene(new Scene(root));
                    //Admin-Visitor
                } else {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view" +
                                    "/Administrator_Visitor_Functionality" +
                                    ".fxml"));
                    primaryStage.setScene(new Scene(root));
                }
                //Visitor Only
            } else {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                        .getWindow();
                Parent root = FXMLLoader.load(getClass()
                        .getResource("../view/Visitor_Functionality.fxml"));
                primaryStage.setScene(new Scene(root));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot go back to the functionality.fxml");
        }

    }


}
