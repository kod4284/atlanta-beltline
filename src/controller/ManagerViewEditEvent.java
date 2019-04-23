package controller;

import javafx.beans.Observable;
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
import jdk.jfr.Event;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerViewEditEvent implements Initializable {

    @FXML Label eventName;
    @FXML Label eventPrice;
    @FXML Label startDate;
    @FXML Label endDate;
    @FXML Label minimumStaffRequired;
    @FXML Label eventCapacity;
    @FXML ListView<String> staffAssigned;
    @FXML TextArea description;

    @FXML TextField dailyVisitsRangeMin;
    @FXML TextField dailyVisitsRangeMax;
    @FXML TextField dailyRevenueRangeMin;
    @FXML TextField dailyRevenueRangeMax;

    @FXML TableView eventDetailsTable;
    @FXML TableColumn<ManagerViewEditEventTableData, String> dateCol;
    @FXML TableColumn<ManagerViewEditEventTableData, String> dailyVisitsCol;
    @FXML TableColumn<ManagerViewEditEventTableData, String> dailyRevenueCol;

    private ObservableList<ManagerViewEditEventTableData> viewEditData;

    private ManagerViewEditEventData viewEditEventData;
    private List<String> availStaffName;
    private List<String> availStaffUsername;
    private List<String> assignedStaffName;
    private List<String> assignedStaffUsername;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staffAssigned.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        viewEditData = FXCollections.observableArrayList();
        loadData();
    }
    private void loadData() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                String eN = Session.viewEditEvent.getEventName().trim();
                String sD = Session.viewEditEvent.getStartDate().trim();
                String sN = Session.viewEditEvent.getSiteName().trim();

                //Get event status
                String eventSql = "select event_name, event_price, start_date, end_date, min_staff_required, capacity, description from event \n" +
                        "where event_name=? and start_date=? and site_name=?;\n";

                PreparedStatement pst = conn.prepareStatement(eventSql);
                pst.setString(1, eN);
                pst.setString(2, sD);
                pst.setString(3, sN);
                ResultSet rs = pst.executeQuery();

                //Get all staff both assigned to this event and those that are not assigned to any events
                String staffSql = "select concat(firstname, ' ', lastname) as 'all_available_staff', t1.username from\n" +
                        "(select staff_username as username from assign_to where event_name=? and start_date=? and site_name=?\n" +
                        "union\n" +
                        "select username from staff where username not in (select staff_username from assign_to)) t1\n" +
                        "join\n" +
                        "(select username, firstname, lastname from user) t2\n" +
                        "on t1.username=t2.username;\n";
                PreparedStatement pst2 = conn.prepareStatement(staffSql);
                pst2.setString(1, eN);
                pst2.setString(2, sD);
                pst2.setString(3, sN);
                ResultSet rs2 = pst2.executeQuery();

                availStaffName = new ArrayList<>();
                availStaffUsername = new ArrayList<>();
                while (rs2.next()) {
                    availStaffName.add(rs2.getString("all_available_staff"));
                    availStaffUsername.add(rs2.getString("t1.username"));
                }

                //Get only the staff assigned to this event
                String assignedAlreadyStaff = "select concat(firstname, ' ', lastname) as " +
                        "'already_assigned_staff', username from assign_to join user " +
                        "on assign_to.staff_username=user.username \n" +
                        "where event_name=? and start_date=? and site_name=?;\n";

                PreparedStatement pst3 = conn.prepareStatement(assignedAlreadyStaff);
                pst3.setString(1, eN);
                pst3.setString(2, sD);
                pst3.setString(3, sN);
                ResultSet rs3 = pst3.executeQuery();

                assignedStaffName = new ArrayList<>();
                assignedStaffUsername = new ArrayList<>();
                while (rs3.next()) {
                    assignedStaffName.add(rs3.getString("already_assigned_staff"));
                    assignedStaffUsername.add(rs3.getString("username"));
                }

                if (rs.next()) {
                    viewEditEventData = new ManagerViewEditEventData(
                            eN,
                            rs.getDouble("event_price"),
                            rs.getString("start_date"),
                            rs.getString("end_date"),
                            rs.getInt("min_staff_required"),
                            rs.getInt("capacity"),
                            availStaffUsername,
                            rs.getString("description")
                    );
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

        System.out.println(viewEditEventData);

        eventName.setText(viewEditEventData.getEventName());
        eventPrice.setText("" + viewEditEventData.getEventPrice());
        startDate.setText(viewEditEventData.getEventStartDate());
        endDate.setText(viewEditEventData.getEventEndDate());
        minimumStaffRequired.setText("" + viewEditEventData.getMinimumStaffRequired());
        eventCapacity.setText("" + viewEditEventData.getEventCapacity());

        staffAssigned.setItems(FXCollections.observableList(availStaffName));
        for (int x = 0; x < staffAssigned.getItems().size(); x++) {
            if (assignedStaffName.contains(staffAssigned.getItems().get(x))){
                staffAssigned.getSelectionModel().select(x);
            }
        }
        System.out.println(viewEditEventData.getDescription());
        description.setText(viewEditEventData.getDescription());
        description.setWrapText(true);
        description.setEditable(false);
        System.out.println(description.getText());

        //Fill up Event Details Table
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            String eN = Session.viewEditEvent.getEventName().trim();
            String sD = Session.viewEditEvent.getStartDate().trim();
            String sN = Session.viewEditEvent.getSiteName().trim();

            String eventTableSql ="select visit_event_date as date, count(*) as daily_visits, count(*)*event_price as daily_revenue\n" +
                    "from visit_event natural join event \n" +
                    "where event_name= ? and start_date=? and site_name=?\n" +
                    "group by visit_event_date\n" +
                    "order by date;\n";

            PreparedStatement pst = conn.prepareStatement(eventTableSql);
            pst.setString(1, eN);
            pst.setString(2, sD);
            pst.setString(3, sN);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                viewEditData.add(new ManagerViewEditEventTableData(
                        new SimpleStringProperty(rs.getString("date")),
                        new SimpleStringProperty(rs.getString("daily_visits")),
                        new SimpleStringProperty(rs.getString("daily_revenue"))));
            }

            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            dailyVisitsCol.setCellValueFactory(new PropertyValueFactory<>("dailyVisits"));
            dailyRevenueCol.setCellValueFactory(new PropertyValueFactory<>
                    ("dailyRevenue"));

            eventDetailsTable.setItems(viewEditData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnActionManagerViewEditEventFilter(ActionEvent event) {
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
        String dailyVisitsMin = dailyVisitsRangeMin.getText().trim();
        String dailyVisitsMax = dailyVisitsRangeMax.getText().trim();
        String dailyRevenueMin = dailyRevenueRangeMin.getText().trim();
        String dailyRevenueMax = dailyRevenueRangeMax.getText().trim();

        String eN = Session.viewEditEvent.getEventName().trim();
        String sD = Session.viewEditEvent.getStartDate().trim();
        String sN = Session.viewEditEvent.getSiteName().trim();

        if ((dailyVisitsMin.isEmpty() && !dailyVisitsMax.isEmpty()) ||
            (!dailyVisitsMin.isEmpty() && dailyVisitsMax.isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Information Needed");
            alert.setContentText("Please fill in both dates.");
            alert.showAndWait();
            return;
        }
        if ((dailyRevenueMin.isEmpty() && !dailyRevenueMax.isEmpty()) ||
                (!dailyRevenueMin.isEmpty() && dailyRevenueMax.isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Information Needed");
            alert.setContentText("Please fill in both revenues.");
            alert.showAndWait();
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            String filterSql = "select visit_event_date as date, count(*) as daily_visits, " +
                    "count(*)*event_price as daily_revenue\n" +
                    "from visit_event natural join event \n" +
                    "where event_name= ? and start_date= ? and site_name= ?\n" +
                    "group by visit_event_date\n" +
                        "order by date;\n";

            System.out.println(filterSql);

            PreparedStatement pst = conn.prepareStatement(filterSql);
            pst.setString(1, eN);
            pst.setString(2, sD);
            pst.setString(3, sN);
            ResultSet rs = pst.executeQuery();

            viewEditData = FXCollections.observableArrayList();
            while (rs.next()) {
                if (!dailyVisitsMin.isEmpty() && !dailyVisitsMax.isEmpty()) {
                    System.out.println("Found: " +Integer.parseInt(rs.getString("daily_visits")));
                    System.out.println("Cmp to: " + Integer.parseInt(dailyVisitsMin));
                    if (Integer.parseInt(rs.getString("daily_visits")) <
                            Integer.parseInt(dailyVisitsMin) ||
                        Integer.parseInt(rs.getString("daily_visits")) >
                            Integer.parseInt(dailyVisitsMax)) {
                        continue;
                    }
                }
                if (!dailyRevenueMin.isEmpty() && !dailyRevenueMax.isEmpty()) {
                    if (Double.parseDouble(rs.getString("daily_revenue")) <
                            Double.parseDouble(dailyRevenueMin) ||
                            Double.parseDouble(rs.getString("daily_revenue")) >
                                    Integer.parseInt(dailyRevenueMax)) {
                        continue;
                    }
                }
                viewEditData.add(new ManagerViewEditEventTableData(
                        new SimpleStringProperty(rs.getString("date")),
                        new SimpleStringProperty(rs.getString("daily_visits")),
                        new SimpleStringProperty(rs.getString("daily_revenue"))));
            }

            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            dailyVisitsCol.setCellValueFactory(new PropertyValueFactory<>("dailyVisits"));
            dailyRevenueCol.setCellValueFactory(new PropertyValueFactory<>
                    ("dailyRevenue"));

            eventDetailsTable.setItems(viewEditData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnActionManagerMangeEventUpdate(ActionEvent event) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //Get chosen staffs
                assert(assignedStaffName.size() == assignedStaffUsername.size());
                List<String> chosenStaffName = staffAssigned.getSelectionModel().getSelectedItems();

                if (chosenStaffName.size() == 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Field input Warning");
                    alert.setContentText("One staff must remain responsible for the event.");
                    alert.showAndWait();
                    return;
                }

                ObservableList<Integer> chosenStaffIndices = staffAssigned.getSelectionModel().getSelectedIndices();
                if (chosenStaffIndices.size() < Integer.parseInt(minimumStaffRequired.getText().trim())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Field input Warning");
                    alert.setContentText("We need at least " + minimumStaffRequired.getText().trim() + " or more staffs.");
                    alert.showAndWait();
                    return;
                }
                List<String> chosenStaffUsername = new ArrayList<>();
                for (Integer x : chosenStaffIndices) {
                    chosenStaffUsername.add(availStaffUsername.get(x));
                }

                String eN = Session.viewEditEvent.getEventName().trim();
                String sD = Session.viewEditEvent.getStartDate().trim();
                String sN = Session.viewEditEvent.getSiteName().trim();

                //Delete all staffs from this event
                String deleteAll = "delete from assign_to where event_name=? and " +
                        "start_date = ? and site_name = ?;";
                PreparedStatement pst1 = conn.prepareStatement(deleteAll);
                pst1.setString(1, eN);
                pst1.setString(2, sD);
                pst1.setString(3, sN);
                pst1.executeUpdate();

                //Insert chosen staff usernames back to assigned_to
                for (String username : chosenStaffUsername) {
                    String putAllBackIn = "insert into assign_to values " +
                            "(?, ?, ?, ?);";
                    PreparedStatement pst2 = conn.prepareStatement(putAllBackIn);
                    pst2.setString(1, username);
                    pst2.setString(2, eN);
                    pst2.setString(3, sD);
                    pst2.setString(4, sN);
                    pst2.executeUpdate();
                }

                //Get all staffs assigned to this event and staffs that are free
                String staffSql = "select concat(firstname, ' ', lastname) as 'all_available_staff', t1.username from\n" +
                        "(select staff_username as username from assign_to where event_name = ? and start_date = ? and site_name = ?\n" +
                        "union\n" +
                        "select username from staff where username not in (select staff_username from assign_to)) t1\n" +
                        "join\n" +
                        "(select username, firstname, lastname from user) t2\n" +
                        "on t1.username=t2.username;\n";
                PreparedStatement pst3 = conn.prepareStatement(staffSql);
                pst3.setString(1, eN);
                pst3.setString(2, sD);
                pst3.setString(3, sN);
                ResultSet rs3 = pst3.executeQuery();

                availStaffName = new ArrayList<>();
                availStaffUsername = new ArrayList<>();

                while (rs3.next()) {
                    availStaffName.add(rs3.getString("all_available_staff"));
                    availStaffUsername.add(rs3.getString("t1.username"));
                }

                staffAssigned.setItems(FXCollections.observableList(availStaffName));
                for (int x = 0; x < staffAssigned.getItems().size(); x++) {
                    if (chosenStaffName.contains(staffAssigned.getItems().get(x))){
                        staffAssigned.getSelectionModel().select(x);
                    }
                }

                assignedStaffUsername = chosenStaffUsername;
                assignedStaffName = chosenStaffName;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void btnActionBack(ActionEvent event){
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Manager_Manage_Event.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load Manager_Functionality_Only.fxml");
        }
    }
}
