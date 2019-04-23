package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.ResourceBundle;

public class VisitorTransitDetail implements Initializable {

    @FXML Label site;
    @FXML ComboBox<TransportType> transportTypeComboBox;
    @FXML TextField transitDate;
    @FXML TableView transitTable;
    @FXML TableColumn<UserTakeTransitData, UserTakeTransitData> routeCol;
    @FXML TableColumn<UserTakeTransitData, String> transportTypeCol;
    @FXML TableColumn<UserTakeTransitData, String> priceCol;
    @FXML TableColumn<UserTakeTransitData, String> connectedSitesCol;

    String detailTransitRoute;
    String detailTransitType;
    double detailTransitPrice;
    int detailSiteNumber;

    ObservableList<UserTakeTransitData> transitDataList;
    private int colIndex;

    ToggleGroup group;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        group = new ToggleGroup();
        transitDataList = FXCollections.observableArrayList();
        site.setText(Session.siteDetail.getSiteName());
//        TransportType[] transportType = TransportType.class.getEnumConstants();
        this.transportTypeComboBox.getItems().add(TransportType.MARTA);
        this.transportTypeComboBox.getItems().add(TransportType.BUS);
        this.transportTypeComboBox.getItems().add(TransportType.BIKE);
        this.transportTypeComboBox.getItems().add(TransportType.OTHER);
        this.transportTypeComboBox.getSelectionModel().selectFirst();


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type," +
                        "transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name=?\n" +
                        "and transit_type=?) t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and " +
                        "t1.transit_type=t2.transit_type\n" +
                        "order by transit_route;\n" +
                        "#sort by each column\n" +
                        "-- order by transit_route desc\n" +
                        "-- order by transit_type\n" +
                        "-- order by transit_type desc\n" +
                        "-- order by transit_price\n" +
                        "-- order by transit_price desc\n" +
                        "-- order by site_number\n" +
                        "-- order by site_number desc\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.siteDetail.getSiteName());
                pst.setString(2, transportTypeComboBox.getValue().toString());
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    transitDataList.add(new UserTakeTransitData(
                            new SimpleStringProperty(rs.getString("transit_route")),
                            new SimpleStringProperty(rs.getString("transit_type")),
                            rs.getDouble("transit_price"),
                            rs.getInt("site_number")
                    ));
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
                    radioButton.setSelected(true);
                    radioButton.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent arg0) {
                            if (radioButton.isSelected()) {
                                colIndex = getIndex();

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

        transitTable.setItems(transitDataList);
    }


    @FXML
    public void btnActionVisitorTransitDetailLogTransit(ActionEvent event) {
        if (!checkTransitDate()) {
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
                String sql = ("insert into take_transit " +
                        "values (?, ?, ?, ?);");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                pst.setString(2, item.getTransportType());
                pst.setString(3, item.getRoute());
                pst.setString(4, transitDate.getText());
                int rs = pst.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Field input Confirmation");
                alert.setContentText("The date successfully inserted!");
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

    private boolean checkTransitDate() {
        if (!checkerFunction.verifyDateFormat(transitDate.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The date should follow the format" +
                    "\nex) ####-##-##");
            alert.showAndWait();
            return false;
        } else if(!checkerFunction.laterThanCurrentTime(transitDate.getText())) {
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

    @FXML
    public void btnActionTransTypeComboBox(ActionEvent event) {
        try {
            transitTable.getItems().clear();

            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type," +
                        "transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name=?\n" +
                        "and transit_type=?) t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and " +
                        "t1.transit_type=t2.transit_type\n" +
                        "order by transit_route;\n" +
                        "#sort by each column\n" +
                        "-- order by transit_route desc\n" +
                        "-- order by transit_type\n" +
                        "-- order by transit_type desc\n" +
                        "-- order by transit_price\n" +
                        "-- order by transit_price desc\n" +
                        "-- order by site_number\n" +
                        "-- order by site_number desc\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.siteDetail.getSiteName());
                pst.setString(2, transportTypeComboBox.getValue().toString());
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    transitDataList.add(new UserTakeTransitData(
                            new SimpleStringProperty(rs.getString("transit_route")),
                            new SimpleStringProperty(rs.getString("transit_type")),
                            rs.getDouble("transit_price"),
                            rs.getInt("site_number")
                    ));
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
                    radioButton.setSelected(true);
                    radioButton.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent arg0) {
                            if (radioButton.isSelected()) {
                                colIndex = getIndex();
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

        transitTable.setItems(transitDataList);
    }
    @FXML
    public void btnActionVisitorTransitDetailBack(ActionEvent event) {
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
