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

public class AdminManageUser {

    @FXML
    public void btnActionAdminManageUserBack(ActionEvent event) {
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

}
