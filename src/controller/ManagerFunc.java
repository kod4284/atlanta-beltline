package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.DB;
import model.Session;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerFunc implements Initializable {
    private boolean hasSites;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.hasSites = checkIfSiteResponsible();
    }
    private boolean checkIfSiteResponsible() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            String checkManagerSql = "select manager_username from site;";
            PreparedStatement pst = conn.prepareStatement(checkManagerSql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                if (rs.getString("manager_username").trim().equals(Session.user.getUsername())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void btnActionViewTransitHistory(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/User_Transit_History.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Transit_History.fxml");

        }
    }
    public void btnTakeTransit(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/User_Take_Transit.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Take_Transit.fxml");

        }
    }
    public void btnActionViewSiteReport(ActionEvent event) {
        if (!this.hasSites) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Permission Required");
            alert.setContentText("You must be responsible for at least one site to access this page.");
            alert.showAndWait();
            return;
        }
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Manager_Site_Report.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load Manager_Site_Report.fxml");

        }
    }
    public void btnActionViewStaff(ActionEvent event) {
        if (!this.hasSites) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Permission Required");
            alert.setContentText("You must be responsible for at least one site to access this page.");
            alert.showAndWait();
            return;
        }
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Manager_Manage_Staff.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load Manager_Manage_Staff.fxml");

        }
    }
    public void btnActionManageEvent(ActionEvent event) {
        if (!this.hasSites) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Permission Required");
            alert.setContentText("You must be responsible for at least one site to access this page.");
            alert.showAndWait();
            return;
        }
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Manager_Manage_Event.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load Manager_Manage_Event.fxml");

        }
    }
    public void btnActionManageProfile(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Employee_Manage_Profile.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load Employee_Manage_Profile.fxml");

        }
    }
    public void btnActionBack(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/User_Login.fxml"));
            primaryStage.setScene(new Scene(root));
            Session.user = null;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }
}