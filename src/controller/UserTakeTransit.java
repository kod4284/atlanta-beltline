package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
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

public class UserTakeTransit implements Initializable {
    @FXML ComboBox<String> containSite;
    @FXML ComboBox<TransportType> transportType;
    @FXML TextField priceRangeStart;
    @FXML TextField priceRangeEnd;
    @FXML TextField transiteDate;
    @FXML TableView transitTable;
    @FXML TableColumn<UserTakeTransitData, UserTakeTransitData> routeCol;
    @FXML TableColumn<UserTakeTransitData, String> transportTypeCol;
    @FXML TableColumn<UserTakeTransitData, String> priceCol;
    @FXML TableColumn<UserTakeTransitData, String> connectedSitesCol;
    private TableRow tableRow;
    private int colIndex;
    ToggleGroup group;
    boolean flag;
    private ObservableList<UserTakeTransitData> transitData;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        group = new ToggleGroup();
        loadSitesAndFillComboBox();
        flag = false;

    }
    private void Empty_Empty_ALL_ALL() {
        System.out.println("Empty_Empty_ALL_ALL");
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
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit) t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_route asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    transitData.add(new UserTakeTransitData(new SimpleStringProperty(rs.getString
                            ("transit_route")), new SimpleStringProperty(rs.getString
                            ("transit_type")), rs.getDouble("transit_price")
                            , rs.getInt("site_number")));
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

        routeCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        routeCol.setCellFactory(param -> new TableCell<UserTakeTransitData,
                UserTakeTransitData>() {
            @Override
            public void updateItem(UserTakeTransitData obj, boolean
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
                            if(radioButton.isSelected()){
                                tableRow = getTableRow();
                                colIndex = getIndex();
                                flag = true;
                            }

                        }
                    });
                }
            }

        });


        transportTypeCol.setCellValueFactory(new PropertyValueFactory<>
                ("transportType"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        connectedSitesCol.setCellValueFactory(new PropertyValueFactory<>
                ("connectedSites"));

        transitTable.setItems(transitData);
    }
    private void Empty_Empty_NOT_ALL() {
        System.out.println("Empty_Empty_NOT_NULL");
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
                    String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                            "(select transit_route, transit_type, transit_price\n" +
                            "from connect natural join transit\n" +
                            "where site_name=?\n" +
                            "and transit_price between 0 and 9999) t1\n" +
                            "join\n" +
                            "(select transit_route, transit_type, count(*) as site_number\n" +
                            "from connect natural join transit\n" +
                            "group by transit_route, transit_type) t2\n" +
                            "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                            "order by transit_route asc;");
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, containSite.getValue());
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        transitData.add(new UserTakeTransitData(new SimpleStringProperty(rs.getString
                                ("transit_route")), new SimpleStringProperty(rs.getString
                                ("transit_type")), rs.getDouble("transit_price")
                                , rs.getInt("site_number")));
                    }
                    System.out.println(transitData);

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
            routeCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                    (param.getValue()));
            routeCol.setCellFactory(param -> new TableCell<UserTakeTransitData,
                    UserTakeTransitData>() {
                @Override
                public void updateItem(UserTakeTransitData obj, boolean
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
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            connectedSitesCol.setCellValueFactory(new PropertyValueFactory<>
                    ("connectedSites"));
            transitTable.setItems(transitData);
    }
    private void Empty_Empty_ALL_NOT() {
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
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where transit_type=?\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_route asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, transportType.getValue().toString());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    transitData.add(new UserTakeTransitData(new SimpleStringProperty(rs.getString
                            ("transit_route")), new SimpleStringProperty(rs.getString
                            ("transit_type")), rs.getDouble("transit_price")
                            , rs.getInt("site_number")));
                }
                System.out.println(transitData);

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
        routeCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        routeCol.setCellFactory(param -> new TableCell<UserTakeTransitData,
                UserTakeTransitData>() {
            @Override
            public void updateItem(UserTakeTransitData obj, boolean
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
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        connectedSitesCol.setCellValueFactory(new PropertyValueFactory<>
                ("connectedSites"));

        transitTable.setItems(transitData);
    }
    private void Empty_Empty_NOT_NOT() {
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
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name=?\n" +
                        "and transit_type=?\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_route asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, containSite.getValue().toString());
                pst.setString(2, transportType.getValue().toString());

                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    transitData.add(new UserTakeTransitData(new SimpleStringProperty(rs.getString
                            ("transit_route")), new SimpleStringProperty(rs.getString
                            ("transit_type")), rs.getDouble("transit_price")
                            , rs.getInt("site_number")));
                }
                System.out.println(transitData);

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
        routeCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        routeCol.setCellFactory(param -> new TableCell<UserTakeTransitData,
                UserTakeTransitData>() {
            @Override
            public void updateItem(UserTakeTransitData obj, boolean
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
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        connectedSitesCol.setCellValueFactory(new PropertyValueFactory<>
                ("connectedSites"));

        transitTable.setItems(transitData);
    }
    private void Data_Data_ALL_ALL() {
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
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where transit_price between ? and ?\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_route asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setDouble(1, Double.parseDouble(priceRangeStart.getText()));
                pst.setDouble(2, Double.parseDouble(priceRangeEnd.getText()));

                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    transitData.add(new UserTakeTransitData(new SimpleStringProperty(rs.getString
                            ("transit_route")), new SimpleStringProperty(rs.getString
                            ("transit_type")), rs.getDouble("transit_price")
                            , rs.getInt("site_number")));
                }
                System.out.println(transitData);

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
        routeCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        routeCol.setCellFactory(param -> new TableCell<UserTakeTransitData,
                UserTakeTransitData>() {
            @Override
            public void updateItem(UserTakeTransitData obj, boolean
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
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        connectedSitesCol.setCellValueFactory(new PropertyValueFactory<>
                ("connectedSites"));

        transitTable.setItems(transitData);
    }
    private void Data_Data_ALL_NOT() {
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
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where transit_type=?\n" +
                        "and transit_price between ? and ?\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_route asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, transportType.getValue().toString());
                pst.setDouble(2, Double.parseDouble(priceRangeStart.getText()));
                pst.setDouble(3, Double.parseDouble(priceRangeEnd.getText()));


                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    transitData.add(new UserTakeTransitData(new SimpleStringProperty(rs.getString
                            ("transit_route")), new SimpleStringProperty(rs.getString
                            ("transit_type")), rs.getDouble("transit_price")
                            , rs.getInt("site_number")));
                }
                System.out.println(transitData);

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
        routeCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        routeCol.setCellFactory(param -> new TableCell<UserTakeTransitData,
                UserTakeTransitData>() {
            @Override
            public void updateItem(UserTakeTransitData obj, boolean
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
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        connectedSitesCol.setCellValueFactory(new PropertyValueFactory<>
                ("connectedSites"));

        transitTable.setItems(transitData);
    }
    private void Data_Data_NOT_ALL() {
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
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name=?\n" +
                        "and transit_price between ? and ?\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_route asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, containSite.getValue().toString());
                pst.setDouble(2, Double.parseDouble(priceRangeStart.getText()));
                pst.setDouble(3, Double.parseDouble(priceRangeEnd.getText()));


                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    transitData.add(new UserTakeTransitData(new SimpleStringProperty(rs.getString
                            ("transit_route")), new SimpleStringProperty(rs.getString
                            ("transit_type")), rs.getDouble("transit_price")
                            , rs.getInt("site_number")));
                }
                System.out.println(transitData);

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
        routeCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        routeCol.setCellFactory(param -> new TableCell<UserTakeTransitData,
                UserTakeTransitData>() {
            @Override
            public void updateItem(UserTakeTransitData obj, boolean
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
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        connectedSitesCol.setCellValueFactory(new PropertyValueFactory<>
                ("connectedSites"));

        transitTable.setItems(transitData);
    }
    private void Data_Data_NOT_NOT() {
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
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name=?\n" +
                        "and transit_type=?\n" +
                        "and transit_price between ? and ?\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_route asc;\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, containSite.getValue().toString());
                pst.setString(2, transportType.getValue().toString());
                pst.setDouble(3, Double.parseDouble(priceRangeStart.getText()));
                pst.setDouble(4, Double.parseDouble(priceRangeEnd.getText()));


                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    transitData.add(new UserTakeTransitData(new SimpleStringProperty(rs.getString
                            ("transit_route")), new SimpleStringProperty(rs.getString
                            ("transit_type")), rs.getDouble("transit_price")
                            , rs.getInt("site_number")));
                }
                System.out.println(transitData);

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
        routeCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        routeCol.setCellFactory(param -> new TableCell<UserTakeTransitData,
                UserTakeTransitData>() {
            @Override
            public void updateItem(UserTakeTransitData obj, boolean
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
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        connectedSitesCol.setCellValueFactory(new PropertyValueFactory<>
                ("connectedSites"));

        transitTable.setItems(transitData);
    }
    public void btnActionFilter(ActionEvent event) {
        if (!checkerFunction.verifyPriceRange(priceRangeStart.getText(),priceRangeEnd.getText())) {
            return;
        }
        transitData = FXCollections.observableArrayList();
        if (priceRangeStart.getText().trim().equals("") && priceRangeEnd.getText()
                .trim().equals("")) {
            if (containSite.getValue().toString().equals("-- ALL --") &&
                    transportType.getValue().toString().equals("-- ALL --")) {
                Empty_Empty_ALL_ALL();
            } else if (!containSite.getValue().toString().equals("-- ALL --") &&
                    transportType.getValue().toString().equals("-- ALL --")) {
                Empty_Empty_NOT_ALL();
            } else if (containSite.getValue().toString().equals("-- ALL --") &&
                    !transportType.getValue().toString().equals("-- ALL --")) {
                Empty_Empty_ALL_NOT();
            } else {
                Empty_Empty_NOT_NOT();
            }

        } else {
            if (!checkPriceRange()) {
                return;
            }
            if (containSite.getValue().toString().equals("-- ALL --") &&
                    transportType.getValue().toString().equals("-- ALL --")) {
                Data_Data_ALL_ALL();
            } else if (!containSite.getValue().toString().equals("-- ALL --") &&
                    transportType.getValue().toString().equals("-- ALL --")) {
                Data_Data_NOT_ALL();
            } else if (containSite.getValue().toString().equals("-- ALL --") &&
                    !transportType.getValue().toString().equals("-- ALL --")) {
                Data_Data_ALL_NOT();
            } else {
                Data_Data_NOT_NOT();
            }

        }
        flag = false;
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

    public void btnActionLogTransit(ActionEvent event) {
        if (!checkTransiteDate()) {
            return;
        }
        if (flag == false) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Radio button selection Warning");
            alert.setContentText("You should select a item on the list!");
            alert.showAndWait();
            return;
        }
        UserTakeTransitData item = (UserTakeTransitData) transitTable.getItems()
                    .get(colIndex);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("insert into take_transit values (?, ?, " +
                        "?, ?);");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                pst.setString(2, item.getTransportType());
                pst.setString(3, item.getRoute());
                pst.setString(4, transiteDate.getText());
                int rs = pst.executeUpdate();
                System.out.println(rs + " rows inserted");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Field input Information");
                alert.setContentText("The data successfully inserted!");
                alert.showAndWait();

            } catch(SQLIntegrityConstraintViolationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Duplicate Transit Input");
                alert.setContentText("You cannot take a same transit on a same" +
                        "day.");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    private boolean checkTransiteDate() {
        if (!checkerFunction.verifyDateFormat(transiteDate.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The date should follow the format" +
                    "\nex) ####-##-##");
            alert.showAndWait();
            return false;
        } else if(!checkerFunction.laterThanCurrentTime(transiteDate.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("You should input the time later than " +
                    "current date");
            alert.showAndWait();
            return false;
        }
        return true;
    }
    private boolean checkPriceRange() {
        if (!checkerFunction.verifyNumeric(priceRangeStart.getText()) ||
                !checkerFunction.verifyNumeric(priceRangeEnd.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Should type numeric value into the range " +
                    "field!");
            alert.showAndWait();
            return false;
        } else if (Integer.parseInt(priceRangeStart.getText()) > Integer.parseInt(priceRangeEnd.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Should type the starting range smaller than" +
                    " ending range!");
            alert.showAndWait();
            return false;
        } else if (Integer.parseInt(priceRangeStart.getText()) < 0 ||
                Integer.parseInt(priceRangeEnd.getText()) < 0 ||
                Integer.parseInt(priceRangeStart.getText()) > 9999 ||
                Integer.parseInt(priceRangeEnd.getText()) > 9999) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Should type the range between 0 ~ 9999");
            alert.showAndWait();
            return false;
        }
        return true;
    }

        @FXML
        public void btnActionTakeTransitBack(ActionEvent event) {
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
                System.out.println("Cannot load Manager_Functionality_Only.fxml");
            }
        }

}
