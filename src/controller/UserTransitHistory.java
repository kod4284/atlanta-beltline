package controller;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserTransitHistory implements Initializable {
    @FXML ComboBox<String> containSite;
    @FXML ComboBox<TransportType> transportType;
    @FXML TextField route;
    @FXML TextField startDate;
    @FXML TextField endDate;
    @FXML TableView historyTable;
    @FXML TableColumn<UserTakeTransitData, String> routeCol;
    @FXML TableColumn<UserTakeTransitData, String> transportTypeCol;
    @FXML TableColumn<UserTakeTransitData, String> priceCol;
    @FXML TableColumn<UserTakeTransitData, String> dateCol;
    private ObservableList<UserTransitHistoryData> historyData;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSitesAndFillComboBox();
    }
    private void loadSitesAndFillComboBox() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements
                ArrayList<String> sites = new ArrayList<>();
                //if no row return, go to catch
                String sql = ("select site_name from site;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    sites.add(rs.getString("site_name"));
                }
                containSite.getItems().addAll(sites);
                containSite.getSelectionModel().selectFirst();
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
        TransportType[] transportType = TransportType.class.getEnumConstants();
        this.transportType.getItems().addAll(transportType);
        this.transportType.getSelectionModel().selectFirst();

    }
    private boolean checkFields() {
        if (startDate.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Fill out Start date field!");
            alert.showAndWait();
            return false;
        } else if (endDate.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Fill out End date field!");
            alert.showAndWait();
            return false;
        }
        return true;
    }
    public void btnActionFilter(ActionEvent event) {
        if (!checkFields()) {
            return;
        }
        historyData = FXCollections.observableArrayList();
        System.out.println(transportType.getValue().toString());
        System.out.println(TransportType.ALL);
        System.out.println(transportType.getValue().toString().equals
                (TransportType.ALL.toString()));
        System.out.println(route.getText().trim().equals(""));
        if (transportType.getValue().toString().equals(TransportType
                .ALL.toString()) && route.getText().trim().equals("")) {
            System.out.println("yes!");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                // create a connection to the database
                Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                        .password);

                try {
                    //query

                    // sql statements

                    //if no row return, go to catch
                    String sql = ("select transit_date, transit_route, transit_type, transit_price \n" +
                            "from transit natural join take_transit natural join connect\n" +
                            "where username=? and site_name=?\n" +
                            "and transit_date between ? and ?;");
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, Session.user.getUsername());
                    pst.setString(2, containSite.getValue());
                    pst.setString(3, startDate.getText());
                    pst.setString(4, endDate.getText());
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        historyData.add(new UserTransitHistoryData(new
                                SimpleStringProperty(rs.getString
                                ("transit_date")), new
                                SimpleStringProperty(rs.getString
                                ("transit_route")), new
                                SimpleStringProperty(rs.getString
                                ("transit_type")),
                                rs.getDouble("transit_price"))
                        );
                    }
                    transportTypeCol.setCellValueFactory(new PropertyValueFactory<>
                            ("transportType"));
                    priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
                    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
                    routeCol.setCellValueFactory(new PropertyValueFactory<>
                            ("route"));
                    historyTable.setItems(historyData);

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
        } else if (!transportType.getValue().toString().equals(TransportType
                .ALL.toString()) && route.getText().trim().equals("")) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                // create a connection to the database
                Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                        .password);

                try {
                    //query

                    // sql statements

                    //if no row return, go to catch
                    String sql = ("select transit_date, transit_route, transit_type, transit_price \n" +
                            "from transit natural join take_transit natural join connect\n" +
                            "where username=? and site_name=?\n" +
                            "and transit_type=? " +
                            "and transit_date between ? and ?;");
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, Session.user.getUsername());
                    pst.setString(2, containSite.getValue());
                    pst.setString(3, transportType.getValue().toString());
                    pst.setString(4, startDate.getText());
                    pst.setString(5, endDate.getText());
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        historyData.add(new UserTransitHistoryData(new
                                SimpleStringProperty(rs.getString
                                ("transit_date")), new
                                SimpleStringProperty(rs.getString
                                ("transit_route")), new
                                SimpleStringProperty(rs.getString
                                ("transit_type")),
                                rs.getDouble("transit_price"))
                        );
                    }
                    transportTypeCol.setCellValueFactory(new PropertyValueFactory<>
                            ("transportType"));
                    priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
                    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
                    routeCol.setCellValueFactory(new PropertyValueFactory<>
                            ("route"));
                    historyTable.setItems(historyData);
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
        } else if (transportType.getValue().toString().equals(TransportType
                .ALL.toString()) && !route.getText().trim().equals("")) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                // create a connection to the database
                Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                        .password);

                try {
                    //query

                    // sql statements

                    //if no row return, go to catch
                    String sql = ("select transit_date, transit_route, transit_type, transit_price \n" +
                            "from transit natural join take_transit natural join connect\n" +
                            "where username=? and site_name=?\n" +
                            "and transit_route=?" +
                            "and transit_date between ? and ?;");
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, Session.user.getUsername());
                    pst.setString(2, containSite.getValue());
                    pst.setString(3, route.getText());
                    pst.setString(4, startDate.getText());
                    pst.setString(5, endDate.getText());
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        historyData.add(new UserTransitHistoryData(new
                                SimpleStringProperty(rs.getString
                                ("transit_date")), new
                                SimpleStringProperty(rs.getString
                                ("transit_route")), new
                                SimpleStringProperty(rs.getString
                                ("transit_type")),
                                rs.getDouble("transit_price"))
                        );
                    }
                    transportTypeCol.setCellValueFactory(new PropertyValueFactory<>
                            ("transportType"));
                    priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
                    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
                    routeCol.setCellValueFactory(new PropertyValueFactory<>
                            ("route"));
                    historyTable.setItems(historyData);
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
        } else if (!transportType.getValue().toString().equals(TransportType
                .ALL.toString()) && route.getText().trim().equals("")) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                // create a connection to the database
                Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                        .password);

                try {
                    //query

                    // sql statements

                    //if no row return, go to catch
                    String sql = ("select transit_date, transit_route, transit_type, transit_price \n" +
                            "from transit natural join take_transit natural join connect\n" +
                            "where username=? and site_name=?\n" +
                            "and transit_type=? " +
                            "and transit_route=?" +
                            "and transit_date between ? and ?;");
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, Session.user.getUsername());
                    pst.setString(2, containSite.getValue());
                    pst.setString(3, transportType.getValue().toString());
                    pst.setString(4, route.getText());
                    pst.setString(5, startDate.getText());
                    pst.setString(6, endDate.getText());
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        historyData.add(new UserTransitHistoryData(new
                                SimpleStringProperty(rs.getString
                                ("transit_date")), new
                                SimpleStringProperty(rs.getString
                                ("transit_route")), new
                                SimpleStringProperty(rs.getString
                                ("transit_type")),
                                rs.getDouble("transit_price"))
                        );
                    }
                    transportTypeCol.setCellValueFactory(new PropertyValueFactory<>
                            ("transportType"));
                    priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
                    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
                    routeCol.setCellValueFactory(new PropertyValueFactory<>
                            ("route"));
                    historyTable.setItems(historyData);
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


    }


        ////if should be totally 7 implementation
        @FXML
        public void btnActionUserHistoryBack(ActionEvent event) {
            try {
                //User Only
                if (Session.user.isUser()) {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view/User_Functionality.fxml"));
                    primaryStage.setScene(new Scene(root));

                    //Employee Only
                } else if (Session.user.isEmployee()) {
                    //Manager Only
                    if (Session.user.isManager()) {
                        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                                .getWindow();
                        Parent root = FXMLLoader.load(getClass()
                                .getResource("../view/Manager_Functionality_Only.fxml"));
                        primaryStage.setScene(new Scene(root));
                        //Staff Only
                    } else if (Session.user.isStaff()) {
                        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                                .getWindow();
                        Parent root = FXMLLoader.load(getClass()
                                .getResource("../view/Staff_Functionality_Only" +
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
                    // Employee Visitor
                } else if (Session.user.isEmployeeVisitor()) {
                    //Manager-Visitor
                    if (Session.user.isManager()) {
                        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                                .getWindow();
                        Parent root = FXMLLoader.load(getClass()
                                .getResource("../view/Manager_Visitor_Functionality.fxml"));
                        primaryStage.setScene(new Scene(root));
                        //Staff-Visitor
                    } else if (Session.user.isStaff()) {
                        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                                .getWindow();
                        Parent root = FXMLLoader.load(getClass()
                                .getResource("../view/Staff_Visitor_Functionality" +
                                        ".fxml"));
                        primaryStage.setScene(new Scene(root));
                        //Admin-Visitor
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
                System.out.println("Cannot go back to the functionality.fxml");
            }
        }



}
