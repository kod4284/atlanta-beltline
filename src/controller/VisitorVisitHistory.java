package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Session;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VisitorVisitHistory implements Initializable {

    @FXML TextField event;
    @FXML TextField site;
    @FXML TextField startDate;
    @FXML TextField endDate;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void btnActionVisitorVisitHistoryFilter(ActionEvent event) {

    }

    @FXML
    public void btnActionVisitorVisitHistoryBack(ActionEvent event) {
        try {
            //Employee
            if (Session.user.isEmployeeVisitor()) {
                //Manager Visitor
                if (Session.user.isManager()) {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view" +
                                    "/Manager_Visitor_functionality" +
                                    ".fxml"));
                    primaryStage.setScene(new Scene(root));
                    //Staff Visitor
                } else if (Session.user.isStaff()) {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view/Staff_Visitor_Functionality" +
                                    ".fxml"));
                    primaryStage.setScene(new Scene(root));
                    //Administrator Visitor
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
            System.out.println("Cannot load User_Login.fxml");
        }
    }

}
