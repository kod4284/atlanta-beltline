package controller;

import com.mysql.cj.log.Log;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

import static java.lang.Integer.valueOf;

public class ManagerSiteReport implements Initializable {

    @FXML TextField startDate;
    @FXML TextField endDate;
    @FXML TextField eventCountRangeStart;
    @FXML TextField eventCountRangeEnd;
    @FXML TextField staffCountRangeStart;
    @FXML TextField staffCountRangeEnd;
    @FXML TextField totalVisitsRangeStart;
    @FXML TextField totalVisitsRangeEnd;
    @FXML TextField totalRevenueRangeStart;
    @FXML TextField totalRevenueRangeEnd;
    @FXML TableView siteReportTable;

    @FXML TableColumn<ManagerSiteReportData, ManagerSiteReportData> dateCol;
    @FXML TableColumn<ManagerSiteReportData, String> eventCountCol;
    @FXML TableColumn<ManagerSiteReportData, String> staffCountCol;
    @FXML TableColumn<ManagerSiteReportData, String> totalVisitsCol;
    @FXML TableColumn<ManagerSiteReportData, String> totalRevenueCol;
    private TableRow tableRow;
    private int colIndex;
    boolean flag;

    ToggleGroup group;
    private ObservableList<ManagerSiteReportData> siteReportData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        group = new ToggleGroup();
        flag = false;
    }

    @FXML
    public void btnActionFilter (ActionEvent event) {
        if (!checkerFunction.verifyStartEndDate(startDate.getText(), endDate.getText())) {
            return;
        }
        if (!checkerFunction.verifyDateFormat(startDate.getText())
                || !checkerFunction.verifyDateFormat(endDate.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The date follows the format:\n" +
                    "ex) 1997-01-30!");
            alert.showAndWait();
            return;
        }
        loadTableData(true);
        flag = false;
    }
    private void loadTableData(boolean init) {
        if (!allDataValid()) {
            return;
        }
        if (!checkerFunction.verifyStartEndDate(startDate.getText(), endDate.getText())) {
            return;
        }
        siteReportData = FXCollections.observableArrayList();
        if(init) {
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
                    String sql = ("select t1.visit_event_date, event_count, staff_count, total_visits, total_revenue from\n" +
                            "(select visit_event_date, count(*) as event_count from\n" +
                            "(select distinct visit_event_date, event_name, start_date \n" +
                            "from visit_event\n" +
                            "where site_name=(select site_name from site where manager_username=?)) as tt1\n" +
                            "group by visit_event_date\n" +
                            "order by visit_event_date) t1\n" +
                            "join\n" +
                            "(select visit_event_date, count(*) as staff_count from\n" +
                            "(select distinct visit_event_date, visit_event.event_name, visit_event.start_date, staff_username\n" +
                            "from visit_event join assign_to on visit_event.event_name=assign_to.event_name and visit_event.start_date=assign_to.start_date\n" +
                            "where visit_event.site_name=(select site_name from site where manager_username=?)) as tt2\n" +
                            "group by visit_event_date\n" +
                            "order by visit_event_date) t2\n" +
                            "on t1.visit_event_date=t2.visit_event_date\n" +
                            "join\n" +
                            "(select visit_date, count(*) as total_visits from\n" +
                            "(select visitor_username, visit_event_date as visit_date, event_name, site_name, start_date from visit_event\n" +
                            "union\n" +
                            "select visitor_username, visit_site_date as visit_date, NULL as event_name, site_name, NULL as start_date from visit_site) as tt3\n" +
                            "where site_name=(select site_name from site where manager_username=?)\n" +
                            "group by visit_date\n" +
                            "order by visit_date) t3\n" +
                            "on t2.visit_event_date=t3.visit_date\n" +
                            "join\n" +
                            "(select visit_event_date, sum(event_price) as total_revenue\n" +
                            "from event join visit_event \n" +
                            "on event.event_name=visit_event.event_name and event.start_date=visit_event.start_date and event.site_name=visit_event.site_name\n" +
                            "where event.site_name=(select site_name from site where manager_username=?)\n" +
                            "group by visit_event_date\n" +
                            "order by visit_event_date) t4\n" +
                            "on t3.visit_date=t4.visit_event_date;\n");
                    PreparedStatement pst = conn.prepareStatement(sql);
                    for (int x = 1; x <= 4 ; x++) {
                        pst.setString(x, Session.user.getUsername());
                    }
                    ResultSet rs = pst.executeQuery();

                    while (rs.next()) {
                        siteReportData.add(new ManagerSiteReportData(
                                new SimpleStringProperty(rs.getString("visit_event_date")),
                                Integer.valueOf(rs.getString("event_count")),
                                Integer.valueOf(rs.getString("staff_count")),
                                Integer.valueOf(rs.getString("total_visits")),
                                Double.valueOf(rs.getString("total_revenue"))
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

            dateCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                    (param.getValue()));
            dateCol.setCellFactory(param -> new TableCell<ManagerSiteReportData,
                    ManagerSiteReportData>() {
                @Override
                public void updateItem(ManagerSiteReportData obj, boolean
                        empty) {
                    super.updateItem(obj, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        RadioButton radioButton = new RadioButton(obj.getDate());
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
            eventCountCol.setCellValueFactory(new PropertyValueFactory<>
                    ("eventCount"));
            staffCountCol.setCellValueFactory(new PropertyValueFactory<>("staffCount"));
            totalVisitsCol.setCellValueFactory(new PropertyValueFactory<>
                    ("totalVisits"));
            totalRevenueCol.setCellValueFactory(new PropertyValueFactory<>
                    ("totalRevenue"));

            siteReportTable.setItems(siteReportData);
        }
    }

    @FXML
    public void btnActionManagerSiteReportDailyDetail (ActionEvent event) {
        if (flag == false) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Radio button selection Warning");
            alert.setContentText("You should select a item on the list!");
            alert.showAndWait();
            return;
        }
        ManagerSiteReportData item = (ManagerSiteReportData) siteReportTable.getItems()
            .get(colIndex);
        Session.dailyDetail = new DailyDetail(item.getDate(), Session.user.getUsername());
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource
                            ("../view/Manager_Daily_Detail" +
                                    ".fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Manager_Daily_Detail.fxml");
        }
    }

    @FXML
    public void btnActionBack(ActionEvent event) {
        if (Session.user.isEmployee() && Session.user.isManager()) {
            try {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                        .getWindow();
                Parent root = FXMLLoader.load(getClass()
                        .getResource
                                ("../view/Manager_Functionality_Only" +
                                        ".fxml"));
                primaryStage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Manager_Functionality_Only.fxml");
            }
        } else if (Session.user.isEmployeeVisitor() && Session.user.isManager()) {
            try {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                        .getWindow();
                Parent root = FXMLLoader.load(getClass()
                        .getResource
                                ("../view/Manager_Visitor_Functionality" +
                                ".fxml"));
                primaryStage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Cannot load Manager_Visitor_Functionality.fxml");
            }
        }
    }

    private boolean allDataValid() {
        if (startDate.getText() == "" || endDate.getText() == "") {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please provide both" +
                    "starting date and ending date.");
            alert.showAndWait();
            return false;
        }
        if (!checkerFunction.verifyDateFormat(startDate.getText()) ||
                !checkerFunction.verifyDateFormat(endDate.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The date should follow the format" +
                    "####-##-##");
            alert.showAndWait();
            return false;
        }
        if (startDate.getText().trim().compareTo(endDate.getText().trim()) > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The starting date should be before or equal to the end date.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

}
