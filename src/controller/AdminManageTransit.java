package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Session;
import model.TransportType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminManageTransit implements Initializable {

    @FXML ComboBox<TransportType> transportTypeComboBox;
    @FXML ComboBox<String> manager;
    @FXML ComboBox<String> containSite;
    @FXML TextField priceRangeMin;
    @FXML TextField priceRangeMax;

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void btnActionAdminManageTransitFilter(ActionEvent event) {

    }

    @FXML
    public void btnActionAdminManageTransitDelete(ActionEvent event) {

    }

    @FXML
    public void btnActionAdminVisitorManageTransitBack(ActionEvent event) {
        try {
            //Administrator Visitor
            if (Session.user.isEmployeeVisitor()) {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                        .getWindow();
                Parent root = FXMLLoader.load(getClass()
                        .getResource("../view/Administrator_Visitor_Functionality" +
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
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }
    @FXML
    public void btnActionAdminVisitorManageTransitCreateTransit(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Administrator_Create_Transit.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }

    @FXML
    public void btnActionAdminVisitorManageTransitEditTransit(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Administrator_Edit_Transit.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }



}
