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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class ManagerDailyDetail implements Initializable {

    @FXML TableColumn<ManagerDailyDetail, String> eventNameCol;
    @FXML TableColumn<ManagerDailyDetail, String> staffNameCol;
    @FXML TableColumn<ManagerDailyDetail, String> visitCol;
    @FXML TableColumn<ManagerDailyDetail, String> revenueCol;
    @FXML TableView dailyDetailTable;

    private ObservableList<ManagerDailyDetailData> dailyDetailData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dailyDetailData = FXCollections.observableArrayList();
        loadTableData(true);
    }

    private void loadTableData(boolean init) {
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
                String sql = ("select t1.event_name, t1.site_name, t1.start_date, staff_names, total_visits, total_revenue from\n" +
                        "(select event.event_name, event.site_name, event.start_date,\n" +
                        "if(count(visitor_username)=0, 0, count(*)) as total_visits, if(count(visitor_username)=0, 0, count(*))*event_price as total_revenue\n" +
                        "from event left outer join visit_event\n" +
                        "on event.event_name=visit_event.event_name and event.site_name=visit_event.site_name and event.start_date=visit_event.start_date\n" +
                        "where visit_event_date= ? and event.site_name in (select site_name from site where manager_username= ?)\n" +
                        "group by event.event_name, event.site_name, event.start_date\n" +
                        "order by event_name) t1\n" +
                        "join\n" +
                        "(select event_name, site_name, start_date, group_concat(firstname, ' ', lastname order by firstname) as staff_names\n" +
                        "from user join assign_to on user.username=assign_to.staff_username\n" +
                        " group by event_name, site_name, start_date) t2\n" +
                        "on t1.event_name=t2.event_name and t1.site_name=t2.site_name and t1.start_date=t2.start_date\n" +
                        "order by event_name;\n" +
                        "-- order by event_name desc\n" +
                        "-- order by staff_names\n" +
                        "-- order by staff_names desc\n" +
                        "-- order by total_visits\n" +
                        "-- order by total_visits desc\n" +
                        "-- order by total_revenue\n" +
                        "-- order by total_revenue desc\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.dailyDetail.getDate());
                pst.setString(2, Session.dailyDetail.getManagerName());
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    dailyDetailData.add(new ManagerDailyDetailData(
                            new SimpleStringProperty(rs.getString("event_name")),
                            rs.getString("staff_names"),
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

        eventNameCol.setCellValueFactory(new PropertyValueFactory<>
                ("eventName"));
        staffNameCol.setCellValueFactory(new PropertyValueFactory<>
                ("staffName"));
        visitCol.setCellValueFactory(new PropertyValueFactory<>
                ("visits"));
        revenueCol.setCellValueFactory(new PropertyValueFactory<>
                ("revenue"));

        dailyDetailTable.setItems(dailyDetailData);
    }

    @FXML
    public void btnActionManagerDailyDetailBack(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Manager_Site_Report.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }


}
