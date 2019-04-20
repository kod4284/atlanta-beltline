package controller;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.EmployeeType;
import model.StateType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterEmployee implements Initializable {
    @FXML
    ComboBox<EmployeeType> empTypes;
    @FXML
    ComboBox<StateType> stateTypes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EmployeeType[] eTypes = EmployeeType.class.getEnumConstants();
        empTypes.getItems().addAll(eTypes);
        empTypes.getSelectionModel().selectFirst();
        StateType[] sTypes = StateType.class.getEnumConstants();
        stateTypes.getItems().addAll(sTypes);
        stateTypes.getSelectionModel().selectFirst();
    }
    public void btnActionBack(Event event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Register_Navigation.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load Register_Navigation.fxml");
        }
    }
}
