package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AdminCreateTransit implements Initializable {
    @FXML ComboBox<TransportTypeNotAll> transportTypeComboBox;
    @FXML TextField route;
    @FXML TextField price;
    @FXML ListView<String> connectedSites;
    private ObservableList<String> tableData;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableData = FXCollections.observableArrayList();

        TransportTypeNotAll[] transportType = TransportTypeNotAll.class
                .getEnumConstants();
        transportTypeComboBox.getItems().addAll(transportType);
        transportTypeComboBox.getSelectionModel().selectFirst();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select site_name from site;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(rs.getString( "site_name"));
                }

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectedSites.setItems(tableData);
        connectedSites.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void btnActionAdminVisitorCreateTransitBack(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Administrator_Manage_Transit.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }
    @FXML
    public void btnActionAdminVisitorCreateTransitCreate(ActionEvent event) {
        if (!isUnderTwoSite()) {
            return;
        }
        if (route.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Site Input Warning");
            alert.setContentText("You should fill out route field!");
            alert.showAndWait();
            return;
        }
        if (!checkerFunction.isStringAsInteger(price.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Site Input Warning");
            alert.setContentText("Price Field cannot be String value!");
            alert.showAndWait();
            return;
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query
                // sql statements
                //if no row return, go to catch
                String sql = ("insert into transit values (?,?,?);");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, transportTypeComboBox.getValue().toString());
                pst.setString(2, route.getText());
                pst.setDouble(3, Double.parseDouble(price.getText()));
                int resultSet = pst.executeUpdate();
                System.out.println(resultSet + " Inserted!");

                sql = ("insert into connect values (?, ?, ?)");
                pst = conn.prepareStatement(sql);
                for (String str : connectedSites
                        .getSelectionModel()
                        .getSelectedItems()) {
                    pst.setString(1, str);
                    pst.setString(2, transportTypeComboBox.getValue()
                            .toString());
                    pst.setString(3, route.getText());
                    resultSet = pst.executeUpdate();
                    System.out.println("Inserted!");
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Input information");
                alert.setContentText("Successfully inserted!");
                alert.showAndWait();
            } catch (SQLIntegrityConstraintViolationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR Dialog");
                alert.setHeaderText("Duplicate ERROR");
                alert.setContentText("There is a duplicated data in Database!" +
                        "\nTry to input different route or transport type");
                alert.showAndWait();
                return;
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private boolean isUnderTwoSite() {
        if (connectedSites.getSelectionModel().getSelectedItems().size() < 2) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Site Input Warning");
            alert.setContentText("You should choose at least two sites!" +
                    "\n Tip: Use control or command key to select multiple value");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
