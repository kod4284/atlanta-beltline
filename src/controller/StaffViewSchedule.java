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
import java.util.ResourceBundle;

public class StaffViewSchedule implements Initializable {

    @FXML TextField eventName;
    @FXML TextField descriptionKeyword;
    @FXML TextField startDate;
    @FXML TextField endDate;

    @FXML TableView viewScheduleTable;
    @FXML TableColumn<StaffViewScheduleData, StaffViewScheduleData> eventNameCol;
    @FXML TableColumn<StaffViewScheduleData, String> siteNameCol;
    @FXML TableColumn<StaffViewScheduleData, String> startDateCol;
    @FXML TableColumn<StaffViewScheduleData, String> endDateCol;
    @FXML TableColumn<StaffViewScheduleData, String> staffCountCol;

    private TableRow tableRow;
    private int colIndex;

    ToggleGroup group;
    private ObservableList<StaffViewScheduleData> staffViewScheduleData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        group = new ToggleGroup();
    }

    @FXML
    public void btnActionManagerViewScheduleFilter(ActionEvent event) {
        if (!checkerFunction.verifyDateFormat(startDate.getText())
                || !checkerFunction.verifyDateFormat(endDate.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Should follow the date format! " +
                    "\n ex) 1997-01-30");
            alert.showAndWait();
            return;
        }
        if (!checkerFunction.verifyStartEndDate(startDate.getText(), endDate.getText())) {
            return;
        }

        loadDate();
    }

    private void loadDate() {

        try {
            if ((!startDate.getText().equals("")
                    && !checkerFunction.verifyDateFormat(startDate.getText()))
                    || (!endDate.getText().equals("")
                    && !checkerFunction.verifyDateFormat(endDate.getText()))) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Field input Warning");
                alert.setContentText("The date should follow the format" +
                        "####-##-##");
                alert.showAndWait();
                return;
            }
            if (!checkerFunction.verifyStartEndDate(startDate.getText(),endDate.getText())) {
                return;
            }
            staffViewScheduleData = FXCollections.observableArrayList();

            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                // both filled
                if (!startDate.getText().equals("") && !endDate.getText().equals("")) {
                    String sql = ("select t1.event_name, t1.site_name, t1.start_date, end_date, staff_count, description from\n" +
                            "(select event_name, site_name, start_date, end_date, description from assign_to natural join event " +
                            "where staff_username='"+ Session.user.getUsername() +"') t1\n" +
                            "join\n" +
                            "(select event.event_name, event.site_name, event.start_date, count(staff_username) as staff_count \n" +
                            "from assign_to natural join event\n" +
                            "group by event.event_name, event.site_name, event.start_date) t2\n" +
                            "on t1.event_name=t2.event_name and t1.site_name=t2.site_name and t1.start_date=t2.start_date\n" +
                            "where t1.event_name like concat('%',?,'%') #Event Name Filter\n" +
                            "and description like concat('%',?,'%') #Event Description Filter\n" +
                            "and ?<t1.start_date #Event Start Date Filter\n" +
                            "and end_date<? #Event End Date Filter\n" +
                            "order by event_name; \n" +
                            "#sort by each column\n" +
                            "-- order by event_name desc\n" +
                            "-- order by site_name\n" +
                            "-- order by site_name desc\n" +
                            "-- order by start_date\n" +
                            "-- order by start_date desc\n" +
                            "-- order by end_date\n" +
                            "-- order by end_date desc\n" +
                            "-- order by staff_count\n" +
                            "-- order by staff_count desc\n");
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, eventName.getText());
                    pst.setString(2, descriptionKeyword.getText());
                    pst.setString(3, startDate.getText());
                    pst.setString(4, endDate.getText());
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        staffViewScheduleData.add(new StaffViewScheduleData(
                                new SimpleStringProperty(rs.getString("event_name")),
                                new SimpleStringProperty(rs.getString("site_name")),
                                new SimpleStringProperty(rs.getString("start_date")),
                                new SimpleStringProperty(rs.getString("end_date")),
                                rs.getInt("staff_count"),
                                new SimpleStringProperty(rs.getString(
                                        "description"))
                        ));
                    }
                //start date filled
                } else if (!startDate.getText().equals("") && endDate.getText().equals("")) {
                    String sql = ("select t1.event_name, t1.site_name, t1.start_date, end_date, staff_count, description from\n" +
                            "(select event_name, site_name, start_date, end_date, description from assign_to natural join event " +
                            "where staff_username='"+ Session.user.getUsername() +"') t1\n" +
                            "join\n" +
                            "(select event.event_name, event.site_name, event.start_date, count(staff_username) as staff_count \n" +
                            "from assign_to natural join event\n" +
                            "group by event.event_name, event.site_name, event.start_date) t2\n" +
                            "on t1.event_name=t2.event_name and t1.site_name=t2.site_name and t1.start_date=t2.start_date\n" +
                            "where t1.event_name like concat('%',?,'%') #Event Name Filter\n" +
                            "and description like concat('%',?,'%') #Event Description Filter\n" +
                            "and ?<t1.start_date #Event Start Date Filter\n" +
                            "and end_date<'2080-01-01' #Event End Date Filter\n" +
                            "order by event_name; \n" +
                            "#sort by each column\n" +
                            "-- order by event_name desc\n" +
                            "-- order by site_name\n" +
                            "-- order by site_name desc\n" +
                            "-- order by start_date\n" +
                            "-- order by start_date desc\n" +
                            "-- order by end_date\n" +
                            "-- order by end_date desc\n" +
                            "-- order by staff_count\n" +
                            "-- order by staff_count desc\n");
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, eventName.getText());
                    pst.setString(2, descriptionKeyword.getText());
                    pst.setString(3, startDate.getText());
//                    pst.setString(4, endDate.getText());
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        staffViewScheduleData.add(new StaffViewScheduleData(
                                new SimpleStringProperty(rs.getString("event_name")),
                                new SimpleStringProperty(rs.getString("site_name")),
                                new SimpleStringProperty(rs.getString("start_date")),
                                new SimpleStringProperty(rs.getString("end_date")),
                                rs.getInt("staff_count"),
                                new SimpleStringProperty(rs.getString(
                                        "description"))
                        ));
                    }
                //end Date Filled
                } else if (startDate.getText().equals("") && !endDate.getText().equals("")) {
                    String sql = ("select t1.event_name, t1.site_name, t1.start_date, end_date, staff_count, description from\n" +
                            "(select event_name, site_name, start_date, end_date, description from assign_to natural join event " +
                            "where staff_username='"+ Session.user.getUsername() +"') t1\n" +
                            "join\n" +
                            "(select event.event_name, event.site_name, event.start_date, count(staff_username) as staff_count \n" +
                            "from assign_to natural join event\n" +
                            "group by event.event_name, event.site_name, event.start_date) t2\n" +
                            "on t1.event_name=t2.event_name and t1.site_name=t2.site_name and t1.start_date=t2.start_date\n" +
                            "where t1.event_name like concat('%',?,'%') #Event Name Filter\n" +
                            "and description like concat('%',?,'%') #Event Description Filter\n" +
//                            "and ?<t1.start_date #Event Start Date Filter\n" +
                            "and end_date<? #Event End Date Filter\n" +
                            "order by event_name; \n" +
                            "#sort by each column\n" +
                            "-- order by event_name desc\n" +
                            "-- order by site_name\n" +
                            "-- order by site_name desc\n" +
                            "-- order by start_date\n" +
                            "-- order by start_date desc\n" +
                            "-- order by end_date\n" +
                            "-- order by end_date desc\n" +
                            "-- order by staff_count\n" +
                            "-- order by staff_count desc\n");
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, eventName.getText());
                    pst.setString(2, descriptionKeyword.getText());
//                    pst.setString(3, startDate.getText());
                    pst.setString(3, endDate.getText());
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        staffViewScheduleData.add(new StaffViewScheduleData(
                                new SimpleStringProperty(rs.getString("event_name")),
                                new SimpleStringProperty(rs.getString("site_name")),
                                new SimpleStringProperty(rs.getString("start_date")),
                                new SimpleStringProperty(rs.getString("end_date")),
                                rs.getInt("staff_count"),
                                new SimpleStringProperty(rs.getString(
                                        "description"))
                        ));
                    }
                // both empty
                } else {
                    String sql = ("select t1.event_name, t1.site_name, t1.start_date, end_date, staff_count, description from\n" +
                            "(select event_name, site_name, start_date, end_date, description from assign_to natural join event " +
                            "where staff_username='"+ Session.user.getUsername() +"') t1\n" +
                            "join\n" +
                            "(select event.event_name, event.site_name, event.start_date, count(staff_username) as staff_count \n" +
                            "from assign_to natural join event\n" +
                            "group by event.event_name, event.site_name, event.start_date) t2\n" +
                            "on t1.event_name=t2.event_name and t1.site_name=t2.site_name and t1.start_date=t2.start_date\n" +
                            "where t1.event_name like concat('%',?,'%') #Event Name Filter\n" +
                            "and description like concat('%',?,'%') #Event Description Filter\n" +
//                            "and ?<t1.start_date #Event Start Date Filter\n" +
//                            "and end_date<? #Event End Date Filter\n" +
                            "order by event_name; \n" +
                            "#sort by each column\n" +
                            "-- order by event_name desc\n" +
                            "-- order by site_name\n" +
                            "-- order by site_name desc\n" +
                            "-- order by start_date\n" +
                            "-- order by start_date desc\n" +
                            "-- order by end_date\n" +
                            "-- order by end_date desc\n" +
                            "-- order by staff_count\n" +
                            "-- order by staff_count desc\n");
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, eventName.getText());
                    pst.setString(2, descriptionKeyword.getText());
//                    pst.setString(3, startDate.getText());
//                    pst.setString(4, endDate.getText());
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        staffViewScheduleData.add(new StaffViewScheduleData(
                                new SimpleStringProperty(rs.getString("event_name")),
                                new SimpleStringProperty(rs.getString("site_name")),
                                new SimpleStringProperty(rs.getString("start_date")),
                                new SimpleStringProperty(rs.getString("end_date")),
                                rs.getInt("staff_count"),
                                new SimpleStringProperty(rs.getString(
                                        "description"))
                        ));
                    }
                }

            } catch (Exception e) {
//                e.printStackTrace();

            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please select the item");
            alert.showAndWait();
        }

        eventNameCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        eventNameCol.setCellFactory(param -> new TableCell<StaffViewScheduleData,
                StaffViewScheduleData>() {
            @Override
            public void updateItem(StaffViewScheduleData obj, boolean
                    empty) {
                super.updateItem(obj, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    RadioButton radioButton =
                            new RadioButton(obj.getEventName());
                    radioButton.setToggleGroup(group);
                    // Add Listeners if any
                    setGraphic(radioButton);
                    radioButton.setSelected(true);
                    radioButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent arg0) {
                            if(radioButton.isSelected()){
                                tableRow = getTableRow();
                                colIndex = getIndex();
                            }
                        }
                    });
                }
            }
        });
        siteNameCol.setCellValueFactory(new PropertyValueFactory<>(
                "siteName"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>(
                "startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>(
                "endDate"));
        staffCountCol.setCellValueFactory(new PropertyValueFactory<>(
                "staffCount"));

        viewScheduleTable.setItems(staffViewScheduleData);

    }

    @FXML
    public void btnActionStaffViewScheduleEventDetail(ActionEvent event) {
        try {
            StaffViewScheduleData item =
                    (StaffViewScheduleData) viewScheduleTable.getItems().get(colIndex);
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                // create a connection to the database
                Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                        .password);

                try {
                    //query

                    // sql statements

                    //if no row return, go to catch
                    String sql = ("select event_name, site_name, start_date, end_date, end_date-start_date " +
                            "as 'Duration_Days', capacity, event_price, description from event\n" +
                            "where event_name=? " +
                            "and site_name=? " +
                            "and start_date=?;\n");
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, item.getEventName());
                    pst.setString(2, item.getSiteName());
                    pst.setString(3, item.getStartDate());
                    ResultSet rs = pst.executeQuery();

                    while (rs.next()) {
                        Session.staffEventDetailModel =
                                new StaffEventDetailModel(
                                        rs.getString("event_name"),
                                        rs.getString("site_name"),
                                        rs.getString("start_date"),
                                        rs.getString("end_date"),
                                        rs.getInt("Duration_Days"),
                                        rs.getInt("capacity"),
                                        rs.getDouble("event_price"),
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

            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Staff_Event_Detail.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please select the item");
            alert.showAndWait();

        }
    }

    @FXML
    public void btnActionStaffViewScheduleBack(ActionEvent event) {
        try {
            if (Session.user.isEmployeeVisitor()) {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                        .getWindow();
                Parent root = FXMLLoader.load(getClass()
                        .getResource("../view" +
                                "/Staff_Visitor_Functionality.fxml"));
                primaryStage.setScene(new Scene(root));
            } else {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                        .getWindow();
                Parent root = FXMLLoader.load(getClass()
                        .getResource("../view/Staff_Functionality_Only.fxml"));
                primaryStage.setScene(new Scene(root));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }

}
