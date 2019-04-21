package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.Session;
import model.UserStatus;
import model.UserType;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminManageUser implements Initializable {
    @FXML TextField username;
    @FXML ComboBox<UserStatus> status;
    @FXML ComboBox<UserType> type;



    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void btnActionAdminManageUserFilter(ActionEvent event) {

    }

    @FXML
    public void btnActionAdminManageUserApprove(ActionEvent event) {

    }

    @FXML
    public void btnActionAdminManageUserDecline(ActionEvent event) {

    }

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
