package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminManageTransit implements Initializable {

    @FXML ComboBox<TransportType> transportTypeComboBox;
    @FXML ComboBox<String> containSite;
    @FXML TextField route;
    @FXML TextField priceRangeMin;
    @FXML TextField priceRangeMax;
    @FXML TableColumn<AdminManageTransitData,AdminManageTransitData> routeCol;
    @FXML TableColumn<AdminManageTransitData,AdminManageTransitData> transportTypeCol;
    @FXML TableColumn<AdminManageTransitData,AdminManageTransitData> priceCol;
    @FXML TableColumn<AdminManageTransitData,AdminManageTransitData> connectedSitesCol;
    @FXML TableColumn<AdminManageTransitData,AdminManageTransitData> transitLoggedCol;
    @FXML TableView tableView;
    ToggleGroup group;
    private int colIndex;
    private ObservableList<AdminManageTransitData> tableData;
    boolean flag;
    public void initialize(URL url, ResourceBundle resourceBundle) {
        group = new ToggleGroup();
        fillComboBox();
        flag = false;
    }

    private void fillComboBox() {
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

                TransportType[] transportType = TransportType.class
                        .getEnumConstants();
                transportTypeComboBox.getItems().addAll(transportType);
                transportTypeComboBox.getSelectionModel().selectFirst();
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
    //Empty for 4
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
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_count, transit_count from \n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "#where site_name='Historic Fourth Ward Park'  " +
                        "Contain Site filter\n" +
                        "#and transit_type='MARTA'  #Transport Type filter\n" +
                        "#and transit_price between 0 and 9999 #Price Range " +
                        "filter\n" +
                        "#and transit_route='Blue' #Route filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_type, transit_route, count(site_name) as site_count from connect group by transit_type, transit_route) t2\n" +
                        "on t1.transit_type=t2.transit_type and t1.transit_route=t2.transit_route\n" +
                        "left outer join\n" +
                        "(select transit_type, transit_route, count(username) as transit_count from take_transit group by transit_type, transit_route) t3\n" +
                        "on t2.transit_type=t3.transit_type and t2.transit_route=t3.transit_route");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Object obj = rs.getObject("transit_count");
                    String str;
                    if (obj == null) {
                        str = "0";
                    } else {
                        str = obj.toString();
                    }
                    System.out.println(str);
                    tableData.add(new AdminManageTransitData(new
                            SimpleStringProperty(rs.getString("transit_route")),
                            new SimpleStringProperty(rs.getString
                                    ("transit_type")),
                            Double.parseDouble(rs.getString("transit_price")),
                            Integer.parseInt(rs.getString("site_count")),
                            Integer.parseInt(str)));
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
    }
    private void Empty_ALL_DATA() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_count, transit_count from \n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name=?  #Contain Site filter\n" +
                        "#and transit_type='MARTA'  #Transport Type filter\n" +
                        "#and transit_price between 0 and 9999 #Price Range filter\n" +
                        "#and transit_route='Blue' #Route filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_type, transit_route, count(site_name) as site_count from connect group by transit_type, transit_route) t2\n" +
                        "on t1.transit_type=t2.transit_type and t1.transit_route=t2.transit_route\n" +
                        "left outer join\n" +
                        "(select transit_type, transit_route, count(username) as transit_count from take_transit group by transit_type, transit_route) t3\n" +
                        "on t2.transit_type=t3.transit_type and t2.transit_route=t3.transit_route");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, containSite.getValue().toString());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageTransitData(new
                            SimpleStringProperty(rs.getString("transit_route")),
                            new SimpleStringProperty(rs.getString
                                    ("transit_type")),
                            Double.parseDouble(rs.getString("transit_price")),
                            Integer.parseInt(rs.getString("site_count")),
                            Integer.parseInt(rs.getString("transit_count"))));
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
    }
    private void Empty_DATA_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_count, transit_count from \n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "#where site_name=?  " +
                        "Contain Site filter\n" +
                        "where transit_type=?  #Transport Type filter\n" +
                        "#and transit_price between 0 and 9999 #Price Range " +
                        "filter\n" +
                        "#and transit_route='Blue' #Route filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_type, transit_route, count(site_name) as site_count from connect group by transit_type, transit_route) t2\n" +
                        "on t1.transit_type=t2.transit_type and t1.transit_route=t2.transit_route\n" +
                        "left outer join\n" +
                        "(select transit_type, transit_route, count(username) as transit_count from take_transit group by transit_type, transit_route) t3\n" +
                        "on t2.transit_type=t3.transit_type and t2.transit_route=t3.transit_route");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, transportTypeComboBox.getValue().toString());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageTransitData(new
                            SimpleStringProperty(rs.getString("transit_route")),
                            new SimpleStringProperty(rs.getString
                                    ("transit_type")),
                            Double.parseDouble(rs.getString("transit_price")),
                            Integer.parseInt(rs.getString("site_count")),
                            Integer.parseInt(rs.getString("transit_count"))));
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
    }
    private void Empty_DATA_DATA() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_count, transit_count from \n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name=?  " +
                        "Contain Site filter\n" +
                        "and transit_type=?  #Transport Type filter\n" +
                        "#and transit_price between 0 and 9999 #Price Range " +
                        "filter\n" +
                        "#and transit_route='Blue' #Route filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_type, transit_route, count(site_name) as site_count from connect group by transit_type, transit_route) t2\n" +
                        "on t1.transit_type=t2.transit_type and t1.transit_route=t2.transit_route\n" +
                        "left outer join\n" +
                        "(select transit_type, transit_route, count(username) as transit_count from take_transit group by transit_type, transit_route) t3\n" +
                        "on t2.transit_type=t3.transit_type and t2.transit_route=t3.transit_route");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, containSite.getValue().toString());
                pst.setString(2, transportTypeComboBox.getValue().toString());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageTransitData(new
                            SimpleStringProperty(rs.getString("transit_route")),
                            new SimpleStringProperty(rs.getString
                                    ("transit_type")),
                            Double.parseDouble(rs.getString("transit_price")),
                            Integer.parseInt(rs.getString("site_count")),
                            Integer.parseInt(rs.getString("transit_count"))));
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
    }

    //DATA for 4
    private void DATA_ALL_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_count, transit_count from \n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "#where site_name='Historic Fourth Ward Park'  " +
                        "Contain Site filter\n" +
                        "#and transit_type='MARTA'  #Transport Type filter\n" +
                        "where transit_price between ? and ? #Price Range " +
                        "filter\n" +
                        "and transit_route=? #Route filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_type, transit_route, count(site_name) as site_count from connect group by transit_type, transit_route) t2\n" +
                        "on t1.transit_type=t2.transit_type and t1.transit_route=t2.transit_route\n" +
                        "left outer join\n" +
                        "(select transit_type, transit_route, count(username) as transit_count from take_transit group by transit_type, transit_route) t3\n" +
                        "on t2.transit_type=t3.transit_type and t2.transit_route=t3.transit_route");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setDouble(1, Double.parseDouble(priceRangeMin.getText()));
                pst.setDouble(2, Double.parseDouble(priceRangeMax.getText()));
                pst.setString(3, route.getText());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Object obj = rs.getObject("transit_count");
                    int cnt = 0;
                    if (obj == null) {
                        cnt = 0;
                    } else {
                        cnt = Integer.parseInt(String.valueOf(obj));
                    }
                    tableData.add(new AdminManageTransitData(new
                            SimpleStringProperty(rs.getString("transit_route")),
                            new SimpleStringProperty(rs.getString
                                    ("transit_type")),
                            Double.parseDouble(rs.getString("transit_price")),
                            Integer.parseInt(rs.getString("site_count")),
                            cnt));
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
    }
    private void DATA_ALL_DATA() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_count, transit_count from \n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name=?  #Contain Site filter\n" +
                        "#and transit_type='MARTA'  #Transport Type filter\n" +
                        "and transit_price between ? and ? #Price Range " +
                        "filter\n" +
                        "and transit_route=? #Route filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_type, transit_route, count(site_name) as site_count from connect group by transit_type, transit_route) t2\n" +
                        "on t1.transit_type=t2.transit_type and t1.transit_route=t2.transit_route\n" +
                        "left outer join\n" +
                        "(select transit_type, transit_route, count(username) as transit_count from take_transit group by transit_type, transit_route) t3\n" +
                        "on t2.transit_type=t3.transit_type and t2.transit_route=t3.transit_route");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, containSite.getValue().toString());
                pst.setDouble(2, Double.parseDouble(priceRangeMin.getText()));
                pst.setDouble(3, Double.parseDouble(priceRangeMax.getText()));
                pst.setString(4, route.getText());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageTransitData(new
                            SimpleStringProperty(rs.getString("transit_route")),
                            new SimpleStringProperty(rs.getString
                                    ("transit_type")),
                            Double.parseDouble(rs.getString("transit_price")),
                            Integer.parseInt(rs.getString("site_count")),
                            Integer.parseInt(rs.getString("transit_count"))));
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
    }
    private void DATA_DATA_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_count, transit_count from \n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "#where site_name=?  " +
                        "Contain Site filter\n" +
                        "where transit_type=?  #Transport Type filter\n" +
                        "and transit_price between ? and ? #Price Range " +
                        "filter\n" +
                        "and transit_route=? #Route filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_type, transit_route, count(site_name) as site_count from connect group by transit_type, transit_route) t2\n" +
                        "on t1.transit_type=t2.transit_type and t1.transit_route=t2.transit_route\n" +
                        "left outer join\n" +
                        "(select transit_type, transit_route, count(username) as transit_count from take_transit group by transit_type, transit_route) t3\n" +
                        "on t2.transit_type=t3.transit_type and t2.transit_route=t3.transit_route");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, transportTypeComboBox.getValue().toString());
                pst.setDouble(2, Double.parseDouble(priceRangeMin.getText()));
                pst.setDouble(3, Double.parseDouble(priceRangeMax.getText()));
                pst.setString(4, route.getText());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageTransitData(new
                            SimpleStringProperty(rs.getString("transit_route")),
                            new SimpleStringProperty(rs.getString
                                    ("transit_type")),
                            Double.parseDouble(rs.getString("transit_price")),
                            Integer.parseInt(rs.getString("site_count")),
                            Integer.parseInt(rs.getString("transit_count"))));
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
    }
    private void DATA_DATA_DATA() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_count, transit_count from \n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name=?  #Contain Site filter\n" +
                        "and transit_type=?  #Transport Type filter\n" +
                        "and transit_price between ? and ? #Price Range filter\n" +
                        "and transit_route=? #Route filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_type, transit_route, count(site_name) as site_count from connect group by transit_type, transit_route) t2\n" +
                        "on t1.transit_type=t2.transit_type and t1.transit_route=t2.transit_route\n" +
                        "left outer join\n" +
                        "(select transit_type, transit_route, count(username) as transit_count from take_transit group by transit_type, transit_route) t3\n" +
                        "on t2.transit_type=t3.transit_type and t2.transit_route=t3.transit_route");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, containSite.getValue().toString());
                pst.setString(2, transportTypeComboBox.getValue().toString());
                pst.setDouble(3, Double.parseDouble(priceRangeMin.getText()));
                pst.setDouble(4, Double.parseDouble(priceRangeMax.getText()));
                pst.setString(5, route.getText());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageTransitData(new
                            SimpleStringProperty(rs.getString("transit_route")),
                            new SimpleStringProperty(rs.getString
                                    ("transit_type")),
                            Double.parseDouble(rs.getString("transit_price")),
                            Integer.parseInt(rs.getString("site_count")),
                            Integer.parseInt(rs.getString("transit_count"))));
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
    }

    private void filter() {
        tableData = FXCollections.observableArrayList();
        if (!checkCondition()) {
            return;
        }
        if (!checkerFunction.verifyStartEndDate(priceRangeMin.getText(), priceRangeMax.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Input Warning");
            alert.setContentText("Starting price should be smaller than end price!");
            alert.showAndWait();
            return;
        }
        if (route.getText().trim().equals("") && priceRangeMin.getText().trim
                ().equals("") && priceRangeMax.getText().trim().equals("")) {
            if (transportTypeComboBox.getValue().toString().equals("-- ALL " +
                    "--") && containSite.getValue().equals("-- ALL --")) {
                Empty_ALL_ALL();
            } else if (transportTypeComboBox.getValue().toString().equals("-- ALL " +
                    "--") && !containSite.getValue().equals("-- ALL --")) {
                Empty_ALL_DATA();
            } else if (!transportTypeComboBox.getValue().toString().equals
                    ("-- ALL " +
                    "--") && containSite.getValue().equals("-- ALL --")) {
                Empty_DATA_ALL();
            } else {
                Empty_DATA_DATA();
            }
        } else {
            if (transportTypeComboBox.getValue().toString().equals("-- ALL " +
                    "--") && containSite.getValue().equals("-- ALL --")) {
                DATA_ALL_ALL();
            } else if (transportTypeComboBox.getValue().toString().equals("-- ALL " +
                    "--") && !containSite.getValue().equals("-- ALL --")) {
                DATA_ALL_DATA();
            } else if (!transportTypeComboBox.getValue().toString().equals
                    ("-- ALL " +
                            "--") && containSite.getValue().equals("-- ALL --")) {
                DATA_DATA_ALL();
            } else {
                DATA_DATA_DATA();
            }
            flag = false;
        }

        routeCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        routeCol.setCellFactory(param -> new TableCell<AdminManageTransitData,AdminManageTransitData>() {
            @Override
            public void updateItem(AdminManageTransitData obj, boolean
                    empty) {
                super.updateItem(obj, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    RadioButton radioButton = new RadioButton(obj.getRoute());
                    radioButton.setToggleGroup(group);
                    // Add Listeners if any
                    setGraphic(radioButton);

                    radioButton.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent arg0) {
                            if (radioButton.isSelected()) {
                                colIndex = getIndex();
                                flag = true;
                            }

                        }
                    });
                }
            }
            //private RadioButton radioButton = new RadioButton();


        });
        transportTypeCol.setCellValueFactory(new PropertyValueFactory<>
                ("transportType"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>
                ("price"));
        connectedSitesCol.setCellValueFactory(new PropertyValueFactory<>
                ("connectedSites"));
        transitLoggedCol.setCellValueFactory(new PropertyValueFactory<>
                ("price"));

        transitLoggedCol.setCellValueFactory(new PropertyValueFactory<>
                        ("transitLogged"));
        tableView.setItems(tableData);
    }
    private boolean checkCondition() {
        if (!route.getText().trim().equals("") || !priceRangeMin.getText()
                .trim().equals("") || !priceRangeMax.getText().trim().equals
                ("")) {
            if (route.getText().trim().equals("") || priceRangeMin.getText()
                    .trim().equals("") || priceRangeMax.getText().trim().equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Input Warning");
                alert.setContentText("All field must be filled out!");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }
    @FXML
    public void btnActionAdminManageTransitFilter(ActionEvent event) {
        filter();
    }

    @FXML
    public void btnActionAdminManageTransitDelete(ActionEvent event) {
        if (flag == false) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Radio button selection Warning");
            alert.setContentText("You should select a item on the list!");
            alert.showAndWait();
            return;
        }
        try {
        AdminManageTransitData item = (AdminManageTransitData) tableView.getItems()
                .get(colIndex);

            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("delete from transit where transit_route=? and " +
                        "transit_type=?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, item.getRoute());
                pst.setString(2, item.getTransportType());
                int rs = pst.executeUpdate();
                System.out.println(rs + "rows deleted");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Deletion Confirmation");
                alert.setContentText("The date successfully Deleted!");
                alert.showAndWait();
                filter();
            } catch (RuntimeException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR Dialog");
                alert.setHeaderText("Radio button ERROR");
                alert.setContentText("You should check a Radio button!");
                alert.showAndWait();
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

    @FXML
    public void btnActionAdminVisitorManageTransitBack(ActionEvent event) {
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
    @FXML
    public void btnActionAdminVisitorManageTransitCreateTransit(ActionEvent event) {

        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Administrator_Create_Transit.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }

    @FXML
    public void btnActionAdminVisitorManageTransitEditTransit(ActionEvent event) {
        if (flag == false) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Radio button selection Warning");
            alert.setContentText("You should select a item on the list!");
            alert.showAndWait();
            return;
        }
        try {
        AdminManageTransitData item = (AdminManageTransitData) tableView.getItems()
                .get(colIndex);
        AdminEditTransit.data = item;


            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Administrator_Edit_Transit.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setHeaderText("Radio button ERROR");
            alert.setContentText("You should check a Radio button!");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }



}
