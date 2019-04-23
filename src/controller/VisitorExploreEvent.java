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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import javafx.scene.control.*;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VisitorExploreEvent implements Initializable {

    @FXML TextField name;
    @FXML TextField descriptionKeyword;
    @FXML ComboBox<String> siteName;
    @FXML TextField startDate;
    @FXML TextField endDate;
    @FXML TextField totalVisitsRangeMin;
    @FXML TextField totalVisitsRangeMax;
    @FXML TextField ticketPriceMin;
    @FXML TextField ticketPriceMax;
    @FXML CheckBox includeVisited;
    @FXML CheckBox includeSoldOutEvent;
    @FXML TableView visitorExploreEventTable;

    @FXML TableColumn<VisitorExploreEventData, VisitorExploreEventData> eventNameCol;
    @FXML TableColumn<VisitorExploreEventData, String> siteNameCol;
    @FXML TableColumn<VisitorExploreEventData, String> ticketPriceCol;
    @FXML TableColumn<VisitorExploreEventData, String> ticketRemainingCol;
    @FXML TableColumn<VisitorExploreEventData, String> totalVisitsCol;
    @FXML TableColumn<VisitorExploreEventData, String> myVisitsCol;
    private TableRow tableRow;
    private int colIndex;

    ToggleGroup group;
    private ObservableList<VisitorExploreEventData> exploreEventData;

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
                siteName.getItems().addAll(sites);
                siteName.getSelectionModel().selectFirst();
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
    public void btnActionVisitorExploreEventFilter(ActionEvent event) {
        LoadTableData();
    }
    private void LoadTableData() {

        exploreEventData = FXCollections.observableArrayList();

        if (!checkerFunction.verifyStartEndDate(startDate.getText(),endDate.getText())) {
            return;
        }
        if (!checkerFunction.verifyRange(totalVisitsRangeMin.getText(), totalVisitsRangeMax.getText())) {
            return;
        }
        if (!checkerFunction.verifyRange(ticketPriceMin.getText(),ticketPriceMax.getText())) {
            return;
        }

        String nameFilter = "where event.event_name like concat('%%') #Event Name Filter\n";;
        String descripFilter = "";
        String siteNameFilter = "";
        String startDateFilter = "";
        String endDateFilter = "";
        String totalVisitFilter = "where total_visits between 0 and 99999 #Total Visit Filter\n";;
        String ticketPriceFilter = "";
        String visitCheckBoxFilter = "";
        String soldoutCheckBoxFilter = "";

        //both selected
        if (!includeVisited.isSelected()) {
            visitCheckBoxFilter = "and my_visits is NULL #if include visited not checked #My Visit Null Filter\n";
        }
        if (!includeSoldOutEvent.isSelected()) {
            soldoutCheckBoxFilter = "and ticket_remaining>0 #if include sold out event not checked #Ticket Remaining Positivity Filter\n";
        }
        if (!name.getText().trim().equals("")) {
            nameFilter = "where event.event_name like concat('%','"+
                    name.getText().trim() + "','%') #Event Name Filter\n";
        }
        if (!descriptionKeyword.getText().trim().equals("")) {
            descripFilter = "and description like concat('%','"+
                    descriptionKeyword.getText().trim()+"','%') #Description Filter\n";
        }
        if (!siteName.getValue().equals("-- ALL --")) {
            System.out.println(siteName.getValue().trim());
            siteNameFilter = "and event.site_name='" + siteName.getValue().trim()
                    + "' #Event Site Name Filter\n";
        }
        if (!startDate.getText().trim().equals("")) {
            if (!checkerFunction.verifyDateFormat(startDate.getText().trim())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Field input Warning");
                alert.setContentText("The date should follow the format" +
                        "####-##-##");
                alert.showAndWait();
                return;
            }
            startDateFilter = "and '"+ startDate.getText() +"'<=event.start_date #Event Start Date Filter\n";
        }
        if (!endDate.getText().trim().equals("")) {
            if (!checkerFunction.verifyDateFormat(endDate.getText().trim())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Field input Warning");
                alert.setContentText("The date should follow the format" +
                        "####-##-##");
                alert.showAndWait();
                return;
            }
            endDateFilter = "and event.end_date<='"+endDate.getText().trim()+"' #Event End Date Filter\n";
        }
        if (!totalVisitsRangeMin.getText().trim().equals("") &&
                !totalVisitsRangeMax.getText().trim().equals("")) {
            totalVisitFilter = "where total_visits between "+totalVisitsRangeMin.getText().trim()
                    +" and "+ totalVisitsRangeMax.getText().trim()+" #Total Visit Filter\n";
        } else if (totalVisitsRangeMin.getText().trim().equals("") &&
                !totalVisitsRangeMax.getText().trim().equals("")) {
            totalVisitFilter = "where total_visits between 0 and "+ totalVisitsRangeMax.getText().trim()+
                    " #Total Visit Filter\n";
        } else if (!totalVisitsRangeMin.getText().trim().equals("") &&
                totalVisitsRangeMax.getText().trim().equals("")) {
            totalVisitFilter = "where total_visits between "+totalVisitsRangeMin.getText().trim()
                    +" and 99999 #Total Visit Filter\n";
        }
        if (!ticketPriceMin.getText().trim().equals("") &&
                !ticketPriceMax.getText().trim().equals("")) {
            ticketPriceFilter = "and event_price between "+ticketPriceMin.getText().trim()
                    +" and "+ticketPriceMax.getText().trim()+" #Event Price Filter\n";
        } else if (ticketPriceMin.getText().trim().equals("") &&
                !ticketPriceMax.getText().trim().equals("")) {
            ticketPriceFilter = "and event_price between 0 and "
                    +ticketPriceMax.getText().trim()+" #Event Price Filter\n";
        } else if (!ticketPriceMin.getText().trim().equals("") &&
                ticketPriceMax.getText().trim().equals("")) {
            ticketPriceFilter = "and event_price between "+ticketPriceMin.getText().trim()
                    +" and 99999 #Event Price Filter\n";
        }



        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.event_name, t1.site_name, t1.start_date, event_price, " +
                        "ticket_remaining, total_visits, my_visits from\n" +
                        "(select event.event_name, event.site_name, event.start_date, " +
                        "event_price, capacity-if(count(visitor_username)=0, 0, count(*)) " +
                        "as ticket_remaining, if(count(visitor_username)=0, 0, count(*)) " +
                        "as total_visits\n" +
                        "from event left outer join visit_event \n" +
                        "on event.event_name=visit_event.event_name " +
                        "and event.start_date=visit_event.start_date " +
                        "and event.site_name=visit_event.site_name\n" +
                        nameFilter +
                        descripFilter+
                        siteNameFilter +
                        startDateFilter +
                        endDateFilter +
                        ticketPriceFilter +
                        "group by event.event_name, event.site_name, event.start_date\n" +
                        "order by event.event_name) as t1\n" +
                        "left outer join\n" +
                        "(select event_name, site_name, start_date, " +
                        "if(count(*)=0, 0, count(*)) as my_visits\n" +
                        "from visit_event where visitor_username='"+ Session.user.getUsername() +"'\n" +
                        "group by event_name, site_name, start_date\n" +
                        "order by event_name) as t2\n" +
                        "on t1.event_name=t2.event_name and t1.site_name=t2.site_name " +
                        "and t1.start_date=t2.start_date\n" +
                         totalVisitFilter +
                         visitCheckBoxFilter +
                         soldoutCheckBoxFilter +
                        "order by event_name;\n" +
                        "#sort by each column\n" +
                        "-- order by event_name desc\n" +
                        "-- order by site_name\n" +
                        "-- order by site_name desc\n" +
                        "-- order by event_price\n" +
                        "-- order by event_price desc\n" +
                        "-- order by ticket_remaining\n" +
                        "-- order by ticket_remaining desc\n" +
                        "-- order by total_visits\n" +
                        "-- order by total_visits desc\n" +
                        "-- order by my_visits\n" +
                        "-- order by my_visits desc\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    exploreEventData.add(new VisitorExploreEventData(new SimpleStringProperty(rs.getString("event_name")),
                            new SimpleStringProperty(rs.getString("site_name")),
                            Double.valueOf(rs.getDouble("event_price")),
                            Integer.valueOf(rs.getInt("ticket_remaining")),
                            Integer.valueOf(rs.getInt("total_visits")),
                            Integer.valueOf(rs.getInt("my_visits")),
                            new SimpleStringProperty(rs.getString("start_date"))));
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

        eventNameCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        eventNameCol.setCellFactory(param -> new TableCell<VisitorExploreEventData,
                VisitorExploreEventData>() {
            @Override
            public void updateItem(VisitorExploreEventData obj, boolean
                    empty) {
                super.updateItem(obj, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    RadioButton radioButton = new RadioButton(obj.getEventName());
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
        siteNameCol.setCellValueFactory(new PropertyValueFactory<>("siteName"));
        ticketPriceCol.setCellValueFactory(new PropertyValueFactory<>(
                "ticketPrice"));
        ticketRemainingCol.setCellValueFactory(new PropertyValueFactory<>(
                "ticketRemaining"));
        totalVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "totalVisits"));
        myVisitsCol.setCellValueFactory(new PropertyValueFactory<>("myVisits"));

        visitorExploreEventTable.setItems(exploreEventData);


    }

    @FXML
    public void btnActionVisitorExploreEventDetail(ActionEvent event) {
        try {
            VisitorExploreEventData item =
                    (VisitorExploreEventData) visitorExploreEventTable.getItems().get(colIndex);


            Session.eventDetail = new EventDetail(item.getEventName(),
                    item.getSiteName(), item.getStartDate(), item.getTicketRemaining());

            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Visitor_Event_Detail.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (Exception e) {
//            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please select the item");
            alert.showAndWait();
        }
    }

    @FXML
    public void btnActionVisitorExploreEventBack(ActionEvent event) {
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
