package controller;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManagerManageEvent implements Initializable {

    @FXML TextField name;
    @FXML TextField descriptionKeyword;
    @FXML TextField startDate;
    @FXML TextField endDate;
    @FXML TextField durationRangeMin;
    @FXML TextField durationRangeMax;
    @FXML TextField totalVisitsRangeMin;
    @FXML TextField totalVisitsRangeMax;
    @FXML TextField totalRevenueRangeMin;
    @FXML TextField totalRevenueRangeMax;
    @FXML TableView manageEventTable;

    @FXML TableColumn<ManagerManageEventData, ManagerManageEventData> nameCol;
    @FXML TableColumn<ManagerManageEventData, String> staffCountCol;
    @FXML TableColumn<ManagerManageEventData, String> durationCol;
    @FXML TableColumn<ManagerManageEventData, String> totalVisitsCol;
    @FXML TableColumn<ManagerManageEventData, String> totalRevenueCol;
    private TableRow tableRow;
    private int colIndex;

    ToggleGroup group;
    private ObservableList<ManagerManageEventData> manageEventData;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        group = new ToggleGroup();
    }

    @FXML
    public void btnActionManagerMangeEventFilter(ActionEvent event) {
        loadTableData(true);
    }
    private void loadTableData(boolean init) {
        if (!allDataValid()) {
            return;
        }
        manageEventData = FXCollections.observableArrayList();
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
                    //dataChecker:
                    //Initialized to seven 0's. If certain TextFields are filled out,
                    // then 0 on which the index of TextField is located at is triggered to become 1.
                    // This is used to fill up sql with appropriate values.
                    // Order: Name, Description, StartDate, EndDate, DurationRange, TotalVisitsRange, TotalRevenueRange.
                    // E.g. if the user had filled out Name, Start Date, and Total Visits Range,
                    // then dataChecker ends up = [1, 0, 1, 0, 0, 1, 0].

                    int [] dataChecker = {0,0,0,0,0,0,0};
                    String sql = "select name, t1.site_name, t1.start_date, staff_count, duration, total_visits, total_revenue from\n" +
                            "(select event.event_name as name, count(staff_username) as staff_count, end_date-event.start_date as duration, event.start_date, event.site_name\n" +
                            "from event natural join assign_to\n where";

                    if (!name.getText().trim().isEmpty()) {
                        sql += " event.event_name like \"%";
                        sql += name.getText().trim();
                        sql +=  "%\" \n";
                        dataChecker[0] = 1;
                    }
                    if (!descriptionKeyword.getText().trim().isEmpty()) {
                        if (dataChecker[0] == 1) sql += "and";
                        sql += " description like \"%";
                        sql += descriptionKeyword.getText().trim();
                        sql += "%\" \n";
                        dataChecker[1] = 1;
                    }
                    if (!startDate.getText().trim().isEmpty()) {
                        for (int j = 0; j <= 1; j++)
                            if (dataChecker[j] == 1) {
                                sql += "and";
                                break;
                            }
                        sql += " ? < event.start_date \n";
                        dataChecker[2] = 1;
                    }
                    if (!endDate.getText().trim().isEmpty()) {
                        for (int j = 0; j <= 2; j++)
                            if (dataChecker[j] == 1) {
                                sql += "and";
                                break;
                            }
                        sql += " end_date < ? \n";
                        dataChecker[3] = 1;
                    }
                    if (!durationRangeMin.getText().trim().isEmpty()
                            && !durationRangeMax.getText().trim().isEmpty()) {
                        for (int j = 0; j <= 2; j++)
                            if (dataChecker[j] == 1) {
                                sql += "and";
                                break;
                            }
                        sql += " end_date-event.start_date between ? and ? \n";
                        dataChecker[4] = 1;
                    }
                    sql += "group by event.event_name, event.site_name, event.start_date\n" +
                            "order by event_name) as t1\n" +
                            "join\n" +
                            "(select event.event_name, event.site_name, event.start_date, visit_event_date,\n" +
                            "if(count(visitor_username)=0, 0, count(*)) as total_visits, if(count(visitor_username)=0, 0, count(*))*event_price as total_revenue\n" +
                            "from event left outer join visit_event\n" +
                            "on event.event_name=visit_event.event_name and event.site_name=visit_event.site_name and event.start_date=visit_event.start_date\n" +
                            "group by event.event_name, event.site_name, event.start_date\n" +
                            "order by event_name) as t2\n" +
                            "on t1.name=t2.event_name and t1.start_date=t2.start_date and t1.site_name=t2.site_name\n";

                    if (!totalVisitsRangeMin.getText().trim().isEmpty()
                            && !totalVisitsRangeMax.getText().trim().isEmpty()) {
                        sql += "where total_visits between ? and ? \n";
                        dataChecker[5] = 1;
                    }
                    if (!totalRevenueRangeMin.getText().trim().isEmpty()
                            && !totalRevenueRangeMax.getText().trim().isEmpty()) {
                        sql += "and total_revenue between ? and ?\n";
                        dataChecker[6] = 1;
                    }
                    sql +=  "order by name;\n";

                    PreparedStatement pst = conn.prepareStatement(sql);
                    int ct = 1;
                    if (dataChecker[0] == 1) //pst.setString(ct++, name.getText().trim());
                    if (dataChecker[1] == 1) //pst.setString(ct++, descriptionKeyword.getText().trim());
                    if (dataChecker[2] == 1) pst.setString(ct++, startDate.getText().trim());
                    if (dataChecker[3] == 1) pst.setString(ct++, endDate.getText().trim());
                    if (dataChecker[4] == 1) {
                        pst.setString(ct++, durationRangeMin.getText().trim());
                        pst.setString(ct++, durationRangeMax.getText().trim());
                    }
                    if (dataChecker[5] == 1) {
                        pst.setString(ct++, totalVisitsRangeMin.getText().trim());
                        pst.setString(ct++, totalRevenueRangeMax.getText().trim());
                    }
                    if (dataChecker[6] == 1) {
                        pst.setString(ct++, totalRevenueRangeMin.getText().trim());
                        pst.setString(ct++, totalRevenueRangeMax.getText().trim());
                    }
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        manageEventData.add(new ManagerManageEventData(
                                new SimpleStringProperty(rs.getString("name")),
                                Integer.valueOf(rs.getString("staff_count")),
                                Integer.valueOf(rs.getString("duration")),
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

            nameCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                    (param.getValue()));
            nameCol.setCellFactory(param -> new TableCell<ManagerManageEventData,
                    ManagerManageEventData>() {
                @Override
                public void updateItem(ManagerManageEventData obj, boolean
                        empty) {
                    super.updateItem(obj, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        RadioButton radioButton = new RadioButton(obj.getName());
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
            staffCountCol.setCellValueFactory(new PropertyValueFactory<>("staffCount"));
            durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
            totalVisitsCol.setCellValueFactory(new PropertyValueFactory<>
                    ("totalVisits"));
            totalRevenueCol.setCellValueFactory(new PropertyValueFactory<>
                    ("totalRevenue"));

            manageEventTable.setItems(manageEventData);
        }
    }

    @FXML
    public void btnActionManagerMangeEventDelete(ActionEvent event) {

    }

    @FXML
    public void btnActionManagerManageEventViewEdit(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Manager_View_Edit_Event.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }
    @FXML
    public void btnActionManagerManageEventCreate(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Manager_Create_Event.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }
    @FXML
    public void btnActionManagerManageEventBack(ActionEvent event) {
        try {
            if (Session.user.isEmployeeVisitor()) {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                        .getWindow();
                Parent root = FXMLLoader.load(getClass()
                        .getResource("../view/Manager_Visitor_Functionality" +
                                ".fxml"));
                primaryStage.setScene(new Scene(root));
            } else {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                        .getWindow();
                Parent root = FXMLLoader.load(getClass()
                        .getResource("../view/Manager_Functionality_Only" +
                                ".fxml"));
                primaryStage.setScene(new Scene(root));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }

    private boolean allDataValid() {
        if (name.getText().isEmpty() && descriptionKeyword.getText().isEmpty()
                && startDate.getText().isEmpty() && endDate.getText().isEmpty()
                ) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please provide all " +
                    "necessary data.");
            alert.showAndWait();
            return false;
        }
        if ((!startDate.getText().isEmpty() && !checkerFunction.verifyDateFormat(startDate.getText()))
                ||  (!endDate.getText().isEmpty() && !checkerFunction.verifyDateFormat(endDate.getText()))) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The date should follow the format" +
                    "####-##-##");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
