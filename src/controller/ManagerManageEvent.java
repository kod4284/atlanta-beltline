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

import javax.xml.transform.Result;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
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
        loadTableData();
    }
    private void loadTableData() {
        String nameFilter = "";
        String descripFilter = "";
        String startDateFilter = "";
        String endDateFilter = "";
        String durationFilter = "";
        String totalFilter = "";
        String revFilter = "";

        if ((!startDate.getText().isEmpty() && !checkerFunction.verifyDateFormat(startDate.getText()))
                ||  (!endDate.getText().isEmpty() && !checkerFunction.verifyDateFormat(endDate.getText()))) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The date should follow the format" +
                    "####-##-##");
            alert.showAndWait();
            return;
        }
        if (startDate.getText().trim().compareTo(endDate.getText().trim()) > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Make sure the start date is not later than the end date.");
            alert.showAndWait();
            return;
        }

        manageEventData = FXCollections.observableArrayList();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                int [] dataChecker = new int[10];
                if (!name.getText().equals("")) {
                    nameFilter = "and event.event_name like concat('%','"+ name.getText().trim() +"','%') #Name filter\n";
                }
                if (!descriptionKeyword.getText().trim().equals("")) {
                    descripFilter = "and description like concat('%', '"+ descriptionKeyword.getText().trim() +"','%') #Description Keyword filter\n";
                }
                if (!startDate.getText().trim().equals("")) {
                    startDateFilter = "and '"+ startDate.getText().trim() +"'<event.start_date #Start Date filter\n";
                }
                if (!endDate.getText().trim().equals("")) {
                    endDateFilter = "and end_date< '"+ endDate.getText().trim() +"' #End Date filter\n";
                }
                //duration both filled
                if (!durationRangeMin.getText().equals("") && !durationRangeMax.getText().trim().equals("")) {
                    durationFilter = "and end_date-event.start_date + 1 between "+ durationRangeMin.getText().trim()+
                            " and "+ durationRangeMax.getText().trim() +" #Duration Range filter\n";
                // duration min filled
                } else if (!durationRangeMin.getText().equals("") && durationRangeMax.getText().trim().equals("")) {
                    durationFilter = "and end_date-event.start_date + 1 between "+ durationRangeMin.getText().trim()+
                            " and 9999 #Duration Range filter\n";
                // duration max filled
                } else if (durationRangeMin.getText().equals("") && !durationRangeMax.getText().trim().equals("")) {
                    durationFilter = "and end_date-event.start_date + 1 between 0 and "+ durationRangeMax.getText().trim() +
                            " #Duration Range filter\n";
                }
                //total visit range both filled
                if (!totalVisitsRangeMin.getText().trim().equals("") && !totalVisitsRangeMax.getText().trim().equals("")) {
                    totalFilter = "where total_visits between "+ totalVisitsRangeMin.getText().trim() +" and "+
                            totalVisitsRangeMax.getText().trim() +" #Total Visits Range filter\n";
                // total visit range min filled
                } else if (!totalVisitsRangeMin.getText().trim().equals("") && totalVisitsRangeMax.getText().trim().equals("")) {
                    totalFilter = "where total_visits between "+ totalVisitsRangeMin.getText().trim() +
                            " and 999999 #Total Visits Range filter\n";
                // total visit range max filled
                } else if (totalVisitsRangeMin.getText().trim().equals("") && !totalVisitsRangeMax.getText().trim().equals("")) {
                    totalFilter = "where total_visits between 0 and "+
                            totalVisitsRangeMax.getText().trim() +" #Total Visits Range filter\n";
                }
                // total revenue range both filled
                if (!totalRevenueRangeMin.getText().trim().equals("") && !totalRevenueRangeMax.getText().trim().equals("")) {
                    revFilter = "and total_revenue between "+ totalRevenueRangeMin.getText().trim() +" and " +
                            totalRevenueRangeMax.getText().trim() +" #Total Revenue Range filter\n";
                // total revenue range min filled
                } else if (!totalRevenueRangeMin.getText().trim().equals("") && totalRevenueRangeMax.getText().trim().equals("")) {
                    revFilter = "and total_revenue between " + totalRevenueRangeMin.getText().trim() +
                            " and 9999999 #Total Revenue Range filter\n";
                // total revenue range max filled
                } else if (totalRevenueRangeMin.getText().trim().equals("") && !totalRevenueRangeMax.getText().trim().equals("")) {
                    revFilter = "and total_revenue between 0 and " +
                            totalRevenueRangeMax.getText().trim() + " #Total Revenue Range filter\n";
                }


                String sql = ("select name, t1.start_date, t1.site_name, staff_count, duration, total_visits, total_revenue from\n" +
                        "(select event.event_name as name, count(staff_username) as staff_count, " +
                        "end_date-event.start_date + 1 as duration, event.start_date, event.site_name\n" +
                        "from event natural join assign_to\n" +
                        "where event.site_name in (select site_name from site" +
                        " where manager_username= '"+ Session.user.getUsername() +"')\n" +
//                        "and event.event_name like '%Tour%' #Name filter\n" +
                        nameFilter +
//                        "and description like '%description%' #Description Keyword filter\n" +
                        descripFilter +
//                        "and '2018-01-01'<event.start_date #Start Date filter\n" +
                        startDateFilter +
//                        "and end_date<'2020-02-01' #End Date filter\n" +
                        endDateFilter +
//                        "and end_date-event.start_date between 0 and 20 #Duration Range filter\n" +
                        durationFilter +
                        "group by event.event_name, event.site_name, event.start_date\n" +
                        "order by event_name) as t1\n" +
                        "join\n" +
                        "(select event.event_name, event.site_name, event.start_date, visit_event_date,\n" +
                        "if(count(visitor_username)=0, 0, count(*)) as total_visits, " +
                        "if(count(visitor_username)=0, 0, count(*))*event_price as total_revenue\n" +
                        "from event left outer join visit_event\n" +
                        "on event.event_name=visit_event.event_name " +
                        "and event.site_name=visit_event.site_name " +
                        "and event.start_date=visit_event.start_date\n" +
                        "group by event.event_name, event.site_name, event.start_date\n" +
                        "order by event_name) as t2\n" +
                        "on t1.name=t2.event_name " +
                        "and t1.start_date=t2.start_date " +
                        "and t1.site_name=t2.site_name\n" +
//                        "where total_visits between 0 and 20 #Total Visits Range filter\n" +
                        totalFilter +
//                        "and total_revenue between 0 and 9999 #Total Revenue Range filter\n" +
                        revFilter +
                        "order by name;\n" +
                        "#sort by each column\n" +
                        "-- order by name desc\n" +
                        "-- order by staff_count\n" +
                        "-- order by staff_count desc\n" +
                        "-- order by duration\n" +
                        "-- order by duration desc\n" +
                        "-- order by total_visits\n" +
                        "-- order by total_visits desc\n" +
                        "-- order by total_revenue\n" +
                        "-- order by total_revenue desc\n");

                PreparedStatement pst = conn.prepareStatement(sql);
//                int ct = 1;
//                if (dataChecker[0] == 1) //pst.setString(ct++, name.getText().trim());
//                if (dataChecker[1] == 1) //pst.setString(ct++, descriptionKeyword.getText().trim());
//                if (dataChecker[2] == 1) pst.setString(ct++, startDate.getText().trim());
//                if (dataChecker[3] == 1) pst.setString(ct++, endDate.getText().trim());
//                if (dataChecker[4] == 1) {
//                    pst.setString(ct++, durationRangeMin.getText().trim());
//                    pst.setString(ct++, durationRangeMax.getText().trim());
//                }
//                if (dataChecker[5] == 1) {
//                    pst.setString(ct++, totalVisitsRangeMin.getText().trim());
//                    pst.setString(ct++, totalRevenueRangeMax.getText().trim());
//                }
//                if (dataChecker[6] == 1) {
//                    pst.setString(ct++, totalRevenueRangeMin.getText().trim());
//                    pst.setString(ct++, totalRevenueRangeMax.getText().trim());
//                }
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    manageEventData.add(new ManagerManageEventData(
                            new SimpleStringProperty(rs.getString("name")),
                            Integer.valueOf(rs.getString("staff_count")),
                            Integer.valueOf(rs.getString("duration")),
                            Integer.valueOf(rs.getString("total_visits")),
                            Double.valueOf(rs.getString("total_revenue")),
                            new SimpleStringProperty(rs.getString("site_name")),
                            new SimpleStringProperty(rs.getString("start_date"))
                    ));
                }
            } catch (SQLSyntaxErrorException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("No Output");
                alert.setContentText("Empty Result");
                alert.showAndWait();
            }
            catch (Exception e) {
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

    @FXML
    public void btnActionManagerMangeEventDelete(ActionEvent event) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                ManagerManageEventData item = (ManagerManageEventData) manageEventTable.getItems()
                        .get(colIndex);

                String sql = "delete from event where event_name=? and site_name=? and start_date=?;";

                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, item.getName());
                pst.setString(2, item.getSiteName());
                pst.setString(3, item.getStartDate());

                System.out.println(pst);

                pst.executeUpdate();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Field input Warning");
                alert.setContentText("Please select the item");
                alert.showAndWait();
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
    public void btnActionManagerManageEventViewEdit(ActionEvent event) {
        try {
            ManagerManageEventData item = (ManagerManageEventData) manageEventTable.getItems().get(colIndex);
            Session.viewEditEvent = new ViewEditEvent(item.getName(), item.getSiteName(), item.getStartDate());

            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Manager_View_Edit_Event.fxml"));
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
