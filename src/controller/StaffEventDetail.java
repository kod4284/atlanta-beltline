package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.DB;
import model.Session;

import java.io.IOException;
import java.net.URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class StaffEventDetail implements Initializable {

    @FXML Label event;
    @FXML Label site;
    @FXML Label startDate;
    @FXML Label endDate;
    @FXML Label durationDays;
    @FXML ListView<String> staffsAssigned;
    @FXML Label capacity;
    @FXML Label price;
    @FXML TextArea description;

    private ObservableList<String> assignedStaffs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDate();
    }

    public void loadDate() {
        event.setText(Session.staffEventDetailModel.getEventName());
        site.setText(Session.staffEventDetailModel.getSiteName());
        startDate.setText(Session.staffEventDetailModel.getStartDate());
        endDate.setText(Session.staffEventDetailModel.getEndDate());
        durationDays.setText(Integer.valueOf(Session.staffEventDetailModel.getDurationDays()).toString());
        capacity.setText(Integer.valueOf(Session.staffEventDetailModel.getCapacity()).toString());
        price.setText(Double.valueOf(Session.staffEventDetailModel.getEventPrice()).toString());
        description.setText(Session.staffEventDetailModel.getDescription());
        description.setWrapText(true);

        assignedStaffs = FXCollections.observableArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select concat(firstname, ' ', lastname) as staffs_assigned, username " +
                        "from user join assign_to on user.username=assign_to.staff_username\n" +
                        "where event_name=? and site_name=? " +
                        "and start_date=? group by username;\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.staffEventDetailModel.getEventName());
                pst.setString(2, Session.staffEventDetailModel.getSiteName());
                pst.setString(3, Session.staffEventDetailModel.getStartDate());
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    assignedStaffs.add(rs.getString("staffs_assigned"));
                }
                staffsAssigned.setItems(assignedStaffs);

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
    public void btnActionStaffEventDetailBack(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Staff_View_Schedule.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }
}
