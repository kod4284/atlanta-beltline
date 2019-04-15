package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserTransitHistory {
        @FXML
        public void btnActionUserHistoryBack(ActionEvent event) {
            try {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                        .getWindow();
                Parent root = FXMLLoader.load(getClass()
                        .getResource("../view/User_Functionality.fxml"));
                primaryStage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Cannot load User_Login.fxml");
            }
        }

}
