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
                sites.add("-- ALL --");
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
    private void Empty_ALL_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct transit_date, transit_route, transit_type, transit_price \n" +
                        "from transit natural join take_transit natural join connect\n" +
                        "where username=? and " +
                        "transit_date between ? and ?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                pst.setString(2, startDate.getText());
                pst.setString(3, endDate.getText());
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
    private void Empty_NOT_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct transit_date, transit_route, transit_type, transit_price from transit natural join take_transit natural join connect\n" +
                        "where username=? and site_name=?\n" +
                        "and transit_date between ? and ?\n");
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
    }
    private void Empty_ALL_NOT() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct transit_date, transit_route, transit_type, transit_price \n" +
                        "from transit natural join take_transit natural join connect\n" +
                        "where username=? and transit_type=? " +
                        "and transit_date between ? and ?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                pst.setString(2, transportType.getValue().toString());
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
    }
    private void Empty_NOT_NOT() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct transit_date, transit_route, transit_type, transit_price \n" +
                        "from transit natural join take_transit natural join connect\n" +
                        "where username=? and transit_type=? and site_name=?" +
                        "and transit_date between ? and ?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                pst.setString(2, transportType.getValue().toString());
                pst.setString(3, containSite.getValue().toString());
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
    }
    private void Data_ALL_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements
                //if no row return, go to catch
                String sql = ("select distinct transit_date, transit_route, transit_type, transit_price \n" +
                        "from transit natural join take_transit natural join connect\n" +
                        "where username=? " +
                        "and transit_route=?\n" +
                        "and transit_date between ? and ?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                pst.setString(2, route.getText());
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
    }
    private void Data_NOT_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct transit_date, transit_route, transit_type, transit_price \n" +
                        "from transit natural join take_transit natural join connect\n" +
                        "where username=? and site_name=? and " +
                        "transit_route=?\n" +
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
    }
    private void Data_ALL_NOT() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct transit_date, transit_route, transit_type, transit_price \n" +
                        "from transit natural join take_transit natural join connect\n" +
                        "where username=? and transit_type=? " +
                        "and transit_route=?\n" +
                        "and transit_date between ? and ?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                pst.setString(2, transportType.getValue().toString());
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
    }
    private void Data_NOT_NOT() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct transit_date, transit_route, transit_type, transit_price \n" +
                        "from transit natural join take_transit natural join connect\n" +
                        "where username=? and transit_type=? and site_name=?" +
                        "and transit_route=?\n" +
                        "and transit_date between ? and ?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                pst.setString(2, transportType.getValue().toString());
                pst.setString(3, containSite.getValue().toString());
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
    public void btnActionFilter(ActionEvent event) {
        if (!checkFields()) {
            return;
        }
        if (!checkerFunction.verifyDateFormat(startDate.getText())
                || !checkerFunction.verifyDateFormat(endDate.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The Date format is wrong!" +
                    "\n ex) 1997-01-20");
            alert.showAndWait();
            return;
        }
        if (!checkerFunction.verifyStartEndDate(startDate.getText(), endDate.getText())) {
            return;
        }


        historyData = FXCollections.observableArrayList();
        System.out.println(transportType.getValue().toString());
        System.out.println(TransportType.ALL);
        System.out.println(transportType.getValue().toString().equals
                (TransportType.ALL.toString()));
        System.out.println(route.getText().trim().equals(""));

        if (route.getText().trim().equals("")) {
            System.out.println("empty");
            if (containSite.getValue().toString().equals("-- ALL --") &&
                    transportType.getValue().toString().equals("-- ALL --")) {
                System.out.println("empty-ALL-ALL");
                Empty_ALL_ALL();
            } else if (!containSite.getValue().toString().equals("-- ALL --") &&
                    transportType.getValue().toString().equals("-- ALL --")) {
                Empty_NOT_ALL();
            } else if (containSite.getValue().toString().equals("-- ALL --") &&
                    !transportType.getValue().toString().equals("-- ALL --")) {
                Empty_ALL_NOT();
            } else {
                System.out.println("empty-not-not");
                Empty_NOT_NOT();
            }

        } else {
            System.out.println("Data");
            if (containSite.getValue().toString().equals("-- ALL --") &&
                    transportType.getValue().toString().equals("-- ALL --")) {
                Data_ALL_ALL();
            } else if (!containSite.getValue().toString().equals("-- ALL --") &&
                    transportType.getValue().toString().equals("-- ALL --")) {
                Data_NOT_ALL();
            } else if (containSite.getValue().toString().equals("-- ALL --") &&
                    !transportType.getValue().toString().equals("-- ALL --")) {
                Data_ALL_NOT();
            } else {
                Data_NOT_NOT();
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
