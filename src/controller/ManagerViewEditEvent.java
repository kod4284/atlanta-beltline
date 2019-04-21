package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jdk.jfr.Event;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagerViewEditEvent implements Initializable {

    @FXML Label name;
    @FXML Label price;
    @FXML Label startDate;
    @FXML Label endDate;
    @FXML Label minimumStaffRequired;
    @FXML Label capacity;
    @FXML ListView<String> staffAssigned;
    @FXML ListView<String> description;
    @FXML TextField dailyVisitsRangeMin;
    @FXML TextField dailyVisitsRangeMax;
    @FXML TextField dailyRevenueRangeMin;
    @FXML TextField dailyRevenueRangeMax;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void btnActionManagerViewEditEventFilter(ActionEvent event) {

    }

    @FXML
    public void btnActionManagerMangeEventUpdate(ActionEvent event) {

    }

    public void btnActionBack(ActionEvent event){
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Manager_Manage_Event.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load Manager_Functionality_Only.fxml");
        }
    }
}
