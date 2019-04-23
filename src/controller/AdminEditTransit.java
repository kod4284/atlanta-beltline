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
import model.AdminManageTransitData;
import model.DB;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AdminEditTransit implements Initializable {

    @FXML Label transportType;
    @FXML TextField route;
    @FXML TextField price;
    @FXML ListView<String> connectedSites;
    private ObservableList<String> tableData;
    private ObservableList<String> selectedTableData;
    public static AdminManageTransitData data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableData = FXCollections.observableArrayList();
        selectedTableData = FXCollections.observableArrayList();
        route.setText(data.getRoute());
        price.setText(String.valueOf(data.getPrice()));
        transportType.setText(data.getTransportType());
        connectedSites.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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

                sql = ("select site_name from connect where transit_type=? " +
                        "and transit_route=?;");
                pst = conn.prepareStatement(sql);
                pst.setString(1, data.getTransportType());
                pst.setString(2, data.getRoute());
                rs = pst.executeQuery();
                while (rs.next()) {
                    selectedTableData.add(rs.getString( "site_name"));
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
        for (String str:
                selectedTableData) {
            connectedSites.getSelectionModel().select(str);

        }

    }

    @FXML
    public void btnActionAdminVisitorEditTransitUpdate(ActionEvent event) {
        if (!isUnderTwoSite()) {
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


                String sql = ("update transit set transit_route=?, " +
                        "transit_price=? where transit_type=? and " +
                        "transit_route=?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, route.getText());
                pst.setString(2, price.getText());
                pst.setString(3, data.getTransportType());
                pst.setString(4, data.getRoute());
                int resultSet = pst.executeUpdate();
                System.out.println(resultSet + " Updated!");


                sql = ("delete from connect where transit_type=? and " +
                        "transit_route=?;");
                pst = conn.prepareStatement(sql);
                pst.setString(1, data.getTransportType());
                pst.setString(2, data.getRoute());
                resultSet = pst.executeUpdate();
                System.out.println(resultSet + " Deleted!");
                sql = ("insert into connect values (?, ?, ?);");
                pst = conn.prepareStatement(sql);
                for (String str : connectedSites
                        .getSelectionModel()
                        .getSelectedItems()) {
                    pst.setString(1, str);
                    pst.setString(2, data.getTransportType());
                    pst.setString(3, data.getRoute());
                    resultSet = pst.executeUpdate();
                    System.out.println("Inserted!");
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
    public void btnActionAdminVisitorEditTransitBack(ActionEvent event) {
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
