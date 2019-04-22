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
import model.DB;
import model.ManagerManageEventData;
import model.ManagerViewEditEventData;
import model.Session;

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

    private ManagerViewEditEventData viewEditEventData;
    private List<String> availStaffName;
    private List<String> assignedStaffName;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staffAssigned.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadData();
    }
    private void loadData() {
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
                //dataChecker:
                //Initialized to seven 0's. If certain TextFields are filled out,
                // then 0 on which the index of TextField is located at is triggered to become 1.
                // This is used to fill up sql with appropriate values.
                // Order: Name, Description, StartDate, EndDate, DurationRange, TotalVisitsRange, TotalRevenueRange.
                // E.g. if the user had filled out Name, Start Date, and Total Visits Range,
                // then dataChecker ends up = [1, 0, 1, 0, 0, 1, 0].

                String eN = Session.viewEditEvent.getEventName().trim();
                String sD = Session.viewEditEvent.getStartDate().trim();
                String sN = Session.viewEditEvent.getSiteName().trim();

                System.out.println(Session.viewEditEvent);

                String eventSql = "select event_name, event_price, start_date, end_date, min_staff_required, capacity, description from event \n" +
                        "where event_name=? and start_date=? and site_name=?;\n";

                PreparedStatement pst = conn.prepareStatement(eventSql);
                pst.setString(1, eN);
                pst.setString(2, sD);
                pst.setString(3, sN);
                ResultSet rs = pst.executeQuery();

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
                List<String> availStaffUsername = new ArrayList<>();
                while (rs2.next()) {
                    availStaffName.add(rs2.getString("all_available_staff"));
                    availStaffUsername.add(rs2.getString("t1.username"));
                }

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
                List<String> assignedStaffUsername = new ArrayList<>();
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
                            assignedStaffUsername,
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
        System.out.println(description.getText());
    }

    @FXML
    public void btnActionManagerViewEditEventFilter(ActionEvent event) {
    }

    @FXML
    public void btnActionManagerMangeEventUpdate(ActionEvent event) {
        try {

            System.out.println("DELETED");
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //availStaffName
                //assignedStaffName
                assignedStaffName = staffAssigned.getSelectionModel().getSelectedItems();

                String eN = Session.viewEditEvent.getEventName().trim();
                String sD = Session.viewEditEvent.getStartDate().trim();
                String sN = Session.viewEditEvent.getSiteName().trim();

                String deleteAll = "delete from assign_to where event_name=? " +
                        "and start_date= ? and site_name=?;";
                PreparedStatement pst1 = conn.prepareStatement(deleteAll);
                pst1.setString(1, eN);
                pst1.setString(2, sD);
                pst1.setString(3, sN);
                pst1.executeUpdate();

                for (int x = 0; x < assignedStaffName.size(); x++) {
                    String putAllBackIn = "insert into assign_to values " +
                            "(?, ?, ?, ?), " +
                            "('michael.smith', 'Bus Tour', '2019-02-01', 'Inman Park');";
                    PreparedStatement pst2 = conn.prepareStatement(putAllBackIn);
                    pst2.setString(1, assignedStaffName.get(x));
                    pst2.setString(2, eN);
                    pst2.setString(3, sD);
                    pst2.setString(4, sN);
                }

                String staffSql = "select concat(firstname, ' ', lastname) as 'all_available_staff', t1.username from\n" +
                        "(select staff_username as username from assign_to where event_name=? and start_date=? and site_name=?\n" +
                        "union\n" +
                        "select username from staff where username not in (select staff_username from assign_to)) t1\n" +
                        "join\n" +
                        "(select username, firstname, lastname from user) t2\n" +
                        "on t1.username=t2.username;\n";
                PreparedStatement pst3 = conn.prepareStatement(staffSql);
                pst3.setString(1, eN);
                pst3.setString(2, sD);
                pst3.setString(3, sN);
                ResultSet rs2 = pst3.executeQuery();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        staffAssigned.setItems(FXCollections.observableList(availStaffName));
        for (int x = 0; x < staffAssigned.getItems().size(); x++) {
            if (assignedStaffName.contains(staffAssigned.getItems().get(x))){
                staffAssigned.getSelectionModel().select(x);
            }
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
