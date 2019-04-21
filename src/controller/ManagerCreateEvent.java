package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagerCreateEvent implements Initializable {

    @FXML TextField name;
    @FXML TextField price;
    @FXML TextField capacity;
    @FXML TextField minimumStaffRequired;
    @FXML TextField startDate;
    @FXML TextField endDate;
    @FXML TextArea description;
    @FXML ListView<String> staffAssigned;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void btnActionManagerCreateEventBack(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Manager_Manage_Event.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }

    public void btnActionManagerCreateEventCreate(ActionEvent event) {

    }
}
