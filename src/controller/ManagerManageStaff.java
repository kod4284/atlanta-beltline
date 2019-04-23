package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.DB;
import model.ManagerManageStaffData;
import model.Session;
import model.TransportType;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManagerManageStaff implements Initializable {

    @FXML ComboBox<String> sites;
    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML TextField startDate;
    @FXML TextField endDate;

    @FXML TableView staffDataTable;
    @FXML TableColumn<ManagerManageStaffData, String> staffNameCol;
    @FXML TableColumn<ManagerManageStaffData, String> eventShiftsCol;

    ObservableList<ManagerManageStaffData> staffData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSiteComboBox();
    }

    private void loadSiteComboBox() {
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
                String sql = ("select site_name from site where manager_username = ?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    sites.add(rs.getString("site_name"));
                }
                this.sites.getItems().addAll(sites);
                this.sites.getSelectionModel().selectFirst();
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
    public void btnActionManagerManageStaffFilter(ActionEvent event) {
        staffData = FXCollections.observableArrayList();
        staffDataTable.setItems(staffData);

        String fName = firstName.getText().trim();
        String lName = lastName.getText().trim();
        String sDate = startDate.getText().trim();
        String eDate = endDate.getText().trim();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            ResultSet rs;
            //Case 1. If first name & last name are given, but dates aren't
            String filterSql = "select concat(firstname,' ',lastname) as staff_name, username, count(*) as event_num\n" +
                    "from event natural join assign_to join user on username=staff_username \n" +
                    "where site_name in (select site_name from site where manager_username= ?)\n";
            if (!fName.isEmpty()) {
                filterSql += "and firstname like ?  #First Name filter\n";
            }
            if (!lName.isEmpty()) {
                filterSql += "and lastname like ? #Last Name filter\n";
            }
            if (!sDate.isEmpty()) {
                filterSql += "and ? < start_date #Start Date filter\n";
            }
            if (!eDate.isEmpty()) {
                filterSql += "and end_date < ? #End Date filter\n";
            }
            filterSql += "group by staff_username\n order by staff_name;\n";
            PreparedStatement pst1 = conn.prepareStatement(filterSql);
            int ct = 1;
            pst1.setString(ct++, Session.user.getUsername());
            if (!fName.isEmpty()) {
                pst1.setString(ct++, fName);
            }
            if (!lName.isEmpty()) {
                pst1.setString(ct++, lName);
            }
            if (!sDate.isEmpty()) {
                pst1.setString(ct++, sDate);
            }
            if (!eDate.isEmpty()) {
                pst1.setString(ct++, eDate);
            }
            System.out.println(filterSql);
            rs = pst1.executeQuery();

            while (rs.next()) {
                staffData.add(new ManagerManageStaffData(
                        new SimpleStringProperty(rs.getString("staff_name")),
                        Integer.parseInt(rs.getString("event_num"))));
            }

            staffNameCol.setCellValueFactory(new PropertyValueFactory<>("staffName"));
            eventShiftsCol.setCellValueFactory(new PropertyValueFactory<>("numEventShifts"));
            staffDataTable.setItems(staffData);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void btnActionBack(Event event) {
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
                System.out.println("Cannot load Manager_Visitor_Functionality_Only.fxml");
            }
        }
    }

}
