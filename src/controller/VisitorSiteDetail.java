package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VisitorSiteDetail implements Initializable {

    @FXML Label site;
    @FXML Label openEveryday;
    @FXML Label address;
    @FXML TextField visitDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void btnActionVisitorSiteDetailLogVisit(ActionEvent event) {

    }

    @FXML
    public void btnActionVisitorSiteDetailBack(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Visitor_Explore_Site.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }

}
