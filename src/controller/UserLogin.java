package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserLogin implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * This is a method to get an action from Login button
     * @param event Action from Login button
     */
    public void btnActionLogin1(ActionEvent event) {

    }

    /**
     * This is a method to get an action from Register button
     * @param event Action from Register button
     */
    @FXML
    public void btnActionRegister1(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Register_Navigation.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }

    }
}
