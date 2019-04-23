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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VisitorVisitHistory implements Initializable {

    @FXML TextField event;
    @FXML ComboBox<String> site;
    @FXML TextField startDate;
    @FXML TextField endDate;

    @FXML TableView visitHistoryTable;
    @FXML TableColumn<VisitorVisitHistoryData, String> dateCol;
    @FXML TableColumn<VisitorVisitHistoryData, String> eventCol;
    @FXML TableColumn<VisitorVisitHistoryData, String> siteCol;
    @FXML TableColumn<VisitorVisitHistoryData, String> priceCol;

    private ObservableList<VisitorVisitHistoryData> visitorVisitHistoryData;
    ToggleGroup group;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        group = new ToggleGroup();
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
                site.getItems().addAll(sites);
                site.getSelectionModel().selectFirst();
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
    public void btnActionVisitorVisitHistoryFilter(ActionEvent event) {
        loadTableData();
    }

    private void loadTableData() {
        String eventFilter = "";
        String siteFilter = "";
        String dateFilter = "where Date between '2018-01-01' and '2020-01-01' #filter the date\n";

        visitorVisitHistoryData = FXCollections.observableArrayList();

        if (!startDate.getText().equals("") && !endDate.getText().equals("")) {
            dateFilter = "where Date between '"+startDate.getText().trim()
                    +"' and '"+endDate.getText().trim()+"' #filter the date\n";
        } else if (startDate.getText().equals("") && !endDate.getText().equals("")) {
            dateFilter = "where Date between '2000-01-01' and '"
                    +endDate.getText().trim()+"' #filter the date\n";
        } else if (!startDate.getText().equals("") && endDate.getText().equals("")) {
            dateFilter = "where Date between '"+startDate.getText().trim()
                    +"' and '2020-01-01' #filter the date\n";
        }
        if (!site.getValue().trim().equals("-- ALL --")) {
            siteFilter = "and Site='"+site.getValue().trim()
                    +"' #filter visitor1’s visit site\n";
        }
        if (!event.getText().trim().equals("")) {
            eventFilter = "and Event like concat('%','"+
                    event.getText().trim() +"','%') #filter visitor1’s visit event\n";
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                String sql = ("select * from\n" +
                        "(select visit_event_date as Date, " +
                        "event.event_name as Event, event.site_name " +
                        "as Site, event.start_date, event.event_price as 'Price'\n" +
                        "from visit_event join event \n" +
                        "on event.event_name=visit_event.event_name " +
                        "and event.site_name=visit_event.site_name " +
                        "and event.start_date=visit_event.start_date\n" +
                        "where visitor_username='"+Session.user.getUsername()+"'\n" +
                        "union\n" +
                        "select visit_site_date as Date, NULL as Event, site_name " +
                        "as Site, NULL as start_date, 0 as 'Price' from visit_site " +
                        "where visitor_username='"+Session.user.getUsername()+"') t\n" +
                        dateFilter +
                        siteFilter +
                        eventFilter +
                        "order by Date;\n" +
                        "#sort by each column\n" +
                        "-- order by Date desc\n" +
                        "-- order by Event\n" +
                        "-- order by Event desc\n" +
                        "-- order by Site\n" +
                        "-- order by Site desc\n" +
                        "-- order by Price\n" +
                        "-- order by Price desc\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    visitorVisitHistoryData.add(new VisitorVisitHistoryData(
                            new SimpleStringProperty(rs.getString("Date")),
                            new SimpleStringProperty(rs.getString("Event")),
                            new SimpleStringProperty(rs.getString("Site")),
                            Integer.valueOf(rs.getInt("Price"))));
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


        dateCol.setCellValueFactory(new PropertyValueFactory<>(
                "date"));
        eventCol.setCellValueFactory(new PropertyValueFactory<>(
                "event"));
        siteCol.setCellValueFactory(new PropertyValueFactory<>(
                "site"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>(
                "price"));

        visitHistoryTable.setItems(visitorVisitHistoryData);

    }

    @FXML
    public void btnActionVisitorVisitHistoryBack(ActionEvent event) {
        try {
            //Employee
            if (Session.user.isEmployeeVisitor()) {
                //Manager Visitor
                if (Session.user.isManager()) {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view" +
                                    "/Manager_Visitor_functionality" +
                                    ".fxml"));
                    primaryStage.setScene(new Scene(root));
                    //Staff Visitor
                } else if (Session.user.isStaff()) {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view/Staff_Visitor_Functionality" +
                                    ".fxml"));
                    primaryStage.setScene(new Scene(root));
                    //Administrator Visitor
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
            System.out.println("Cannot load User_Login.fxml");
        }
    }

}
