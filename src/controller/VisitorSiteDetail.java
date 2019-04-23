package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DB;
import model.Session;
import model.checkerFunction;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ResourceBundle;

public class VisitorSiteDetail implements Initializable {

    @FXML Label site;
    @FXML Label openEveryday;
    @FXML Label address;
    @FXML TextField visitDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDate();
    }

    private void loadDate() {
        site.setText(Session.siteDetail.getSiteName());
        openEveryday.setText(Session.siteDetail.getOpenEveryday());
        address.setText(Session.siteDetail.getAddress());
    }

    @FXML
    public void btnActionVisitorSiteDetailLogVisit(ActionEvent event) {
        if (visitDate.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please, Enter the date");
            alert.showAndWait();
            return;
        }
        if (!checkerFunction.verifyDateFormat(visitDate.getText().trim())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The date should follow the format" +
                    "####-##-##");
            alert.showAndWait();
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            //query

            // sql statements

            //if no row return, go to catch
            String sql = ("insert into visit_site " +
                    "values (?, ?, ?);\n");
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, Session.user.getUsername());
            pst.setString(2, Session.siteDetail.getSiteName());
            pst.setString(3, visitDate.getText());
            int rs = pst.executeUpdate();


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Field input Confirmation");
            alert.setContentText("The data is successfully inserted");
            alert.showAndWait();

        } catch (SQLIntegrityConstraintViolationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Duplicate Visit Input");
            alert.setContentText("You cannot insert the same Visit");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean checkVisitDate() {
        if (!checkerFunction.verifyDateFormat(visitDate.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The date should follow the format" +
                    "####-##-##");
            alert.showAndWait();
            return false;
        }
        return true;
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
