package controller;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagerVisitorFunc {
    public void btnActionManageProfile (Event event) {
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
    public void btnActionViewStaff (Event event) {
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
    public void btnActionExploreSite (Event event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Visitor_Explore_Site.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load Visitor_Explore_Site.fxml");
        }
    }
    public void btnActionTakeTransit (Event event) {
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

    public void btnActionViewVisitHistory (Event event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Visitor_Visit_History.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load Visitor_Visit_History.fxml");
        }
    }
    public void btnActionManageEvent (Event event) {
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
    public void btnActionViewSiteReport (Event event) {
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
    public void btnActionVisitorExploreEvent (Event event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Visitor_Explore_Event.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load Visitor_Explore_Event.fxml");
        }
    }
    public void btnActionViewTransitHistory (Event event) {
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

     public void btnActionBack(Event event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/User_Login.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }
}
