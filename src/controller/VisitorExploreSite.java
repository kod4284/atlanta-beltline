package controller;

import com.google.protobuf.Internal;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VisitorExploreSite implements Initializable {

    @FXML ComboBox<String> name;
    @FXML ComboBox<OpenEveryday> openEveryday;
    @FXML TextField startDate;
    @FXML TextField endDate;
    @FXML CheckBox includeVisited;
    @FXML TextField totalVisitsRangeMin;
    @FXML TextField totalVisitsRangeMax;
    @FXML TextField eventCountRangeMin;
    @FXML TextField eventCountRangeMax;

    @FXML TableView exploreSiteTable;
    @FXML TableColumn<VisitorExploreSiteData,VisitorExploreSiteData> siteNameCol;
    @FXML TableColumn<VisitorExploreSiteData,String> eventCountCol;
    @FXML TableColumn<VisitorExploreSiteData,String> totalVisitsCol;
    @FXML TableColumn<VisitorExploreSiteData,String> myVisitsCol;
    private TableRow tableRow;
    private int colIndex;

    String detailSiteName;
    String detailOpenEveryday;
    String detailSiteAddress;

    ToggleGroup group;
    private ObservableList<VisitorExploreSiteData> visitorExploreSiteData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        group = new ToggleGroup();
        loadNameAndOpenEveryday();
    }

    private void loadNameAndOpenEveryday() {
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
                name.getItems().addAll(sites);
                name.getSelectionModel().selectFirst();

                OpenEveryday[] open = OpenEveryday.class.getEnumConstants();
                openEveryday.getItems().addAll(open);
                openEveryday.getSelectionModel().selectFirst();
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
    public void btnActionVisitorExploreSiteFilter(ActionEvent event) {
        LoadTableData();
    }

    private void LoadTableData() {
        if (!allDataValid()) {
            return;
        }
        visitorExploreSiteData = FXCollections.observableArrayList();
        if (totalVisitsRangeMax.getText().trim().equals("")) {
            totalVisitsRangeMax.setText("9999");
        }
        if (totalVisitsRangeMin.getText().trim().equals("")) {
            totalVisitsRangeMin.setText("0");
        }
        if (eventCountRangeMax.getText().trim().equals("")) {
            eventCountRangeMax.setText("9999");
        }
        if (eventCountRangeMin.getText().trim().equals("")) {
            eventCountRangeMin.setText("0");
        }

        if (includeVisited.isSelected()) {
            if (name.getValue().toString().equals("-- ALL --") &&
                    openEveryday.getValue().toString().equals("-- ALL --")) {
                checkedAllBoth();
            } else if (!name.getValue().toString().equals("-- ALL --") &&
                    openEveryday.getValue().toString().equals("-- ALL --")) {
                chekcedAllEveryday();
            } else if (name.getValue().toString().equals("-- ALL --") &&
                    !openEveryday.getValue().toString().equals("-- ALL --")) {
                checkedAllName();
            } else if (!name.getValue().toString().equals("-- ALL --") &&
                    !openEveryday.getValue().toString().equals("-- ALL --")) {
                chekcedAllNeither();
            }
        } else {
            if (name.getValue().toString().equals("-- ALL --") &&
                    openEveryday.getValue().toString().equals("-- ALL --")) {
                unCheckedAllBoth();
            } else if (!name.getValue().toString().equals("-- ALL --") &&
                    openEveryday.getValue().toString().equals("-- ALL --")) {
                unChekcedAllEveryday();
            } else if (name.getValue().toString().equals("-- ALL --") &&
                    !openEveryday.getValue().toString().equals("-- ALL --")) {
                unCheckedAllName();
            } else if (!name.getValue().toString().equals("-- ALL --") &&
                    !openEveryday.getValue().toString().equals("-- ALL --")) {
                unChekcedAllNeither();
            }
        }
    }

    private void unChekcedAllNeither() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select eventcount.site_name, event_count, " +
                        "total_visits, my_visits from\n" +
                        "(select site.site_name, count(*) as event_count\n" +
                        "from site join event on site.site_name=event.site_name\n" +
                        "where site.site_name=? #Site Name Filter\n" +
                        "and open_everyday=? #Open Everyday Filter\n" +
                        "and ? < start_date #Start Date Filter\n" +
                        "and end_date<? #End Date Filter\n" +
                        "group by site.site_name\n" +
                        "order by site_name) as eventcount\n" +
                        "right outer join\n" +
                        "(select site_name, count(*) as total_visits\n" +
                        "from\n" +
                        "(select visitor_username, event_name, start_date, " +
                        "visit_event.site_name, visit_event_date as 'Date' from visit_event\n" +
                        "union\n" +
                        "select visitor_username, NULL as event_name, " +
                        "NULL as start_date, visit_site.site_name, visit_site_date " +
                        "as 'Date' from visit_site) t\n" +
                        "group by site_name\n" +
                        "order by site_name) as totalvisits\n" +
                        "on totalvisits.site_name=eventcount.site_name\n" +
                        "left outer join\n" +
                        "(select site_name, count(*) as my_visits\n" +
                        "from\n" +
                        "(select event_name, start_date, visit_event.site_name, " +
                        "visit_event_date as 'Date' from visit_event " +
                        "where visitor_username='manager2'\n" +
                        "union\n" +
                        "select NULL as event_name, NULL as start_date, " +
                        "visit_site.site_name, visit_site_date as 'Date' " +
                        "from visit_site where visitor_username='manager2') t\n" +
                        "group by site_name\n" +
                        "order by site_name) as myvisits\n" +
                        "on totalvisits.site_name=myvisits.site_name\n" +
                        "where total_visits between ? and ? #Total Visits Range filter\n" +
                        "and event_count between ? and ? #Event Count Range filter\n" +
                        "and my_visits is NULL #if include visited not checked " +
                        "#My Visit Null Filter\n" +
                        "order by site_name;\n" +
                        "#sort by each column\n" +
                        "-- order by site_name desc\n" +
                        "-- order by event_count\n" +
                        "-- order by event_count desc\n" +
                        "-- order by total_visits\n" +
                        "-- order by total_visits desc\n" +
                        "-- order by my_visits\n" +
                        "-- order by my_visits desc\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, openEveryday.getValue().toString().trim());
                pst.setString(2, name.getValue().toString().trim());
                pst.setString(3, startDate.getText());
                pst.setString(4, endDate.getText());
                pst.setInt(5, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(6, Integer.parseInt(totalVisitsRangeMax.getText()));
                pst.setInt(7, Integer.parseInt(eventCountRangeMin.getText()));
                pst.setInt(8, Integer.parseInt(eventCountRangeMax.getText()));
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    visitorExploreSiteData.add(new VisitorExploreSiteData(
                            new SimpleStringProperty(rs.getString("site_name")),
                            Integer.valueOf(rs.getInt("event_count")),
                            Integer.valueOf(rs.getInt("total_visits")),
                            Integer.valueOf(rs.getInt("my_visits"))));
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

        siteNameCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        siteNameCol.setCellFactory(param -> new TableCell<VisitorExploreSiteData,
                VisitorExploreSiteData>() {
            @Override
            public void updateItem(VisitorExploreSiteData obj, boolean
                    empty) {
                super.updateItem(obj, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    RadioButton radioButton =
                            new RadioButton(obj.getSiteName());
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
        eventCountCol.setCellValueFactory(new PropertyValueFactory<>(
                "eventCount"));
        totalVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "totalVisits"));
        myVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "myVisits"));

        exploreSiteTable.setItems(visitorExploreSiteData);

    }

    private void unCheckedAllName() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select eventcount.site_name, event_count, " +
                        "total_visits, my_visits from\n" +
                        "(select site.site_name, count(*) as event_count\n" +
                        "from site join event on site.site_name=event.site_name\n" +
//                        "where site.site_name=? #Site Name Filter\n" +
                        "where open_everyday=? #Open Everyday Filter\n" +
                        "and ? < start_date #Start Date Filter\n" +
                        "and end_date<? #End Date Filter\n" +
                        "group by site.site_name\n" +
                        "order by site_name) as eventcount\n" +
                        "right outer join\n" +
                        "(select site_name, count(*) as total_visits\n" +
                        "from\n" +
                        "(select visitor_username, event_name, start_date, " +
                        "visit_event.site_name, visit_event_date as 'Date' from visit_event\n" +
                        "union\n" +
                        "select visitor_username, NULL as event_name, " +
                        "NULL as start_date, visit_site.site_name, visit_site_date " +
                        "as 'Date' from visit_site) t\n" +
                        "group by site_name\n" +
                        "order by site_name) as totalvisits\n" +
                        "on totalvisits.site_name=eventcount.site_name\n" +
                        "left outer join\n" +
                        "(select site_name, count(*) as my_visits\n" +
                        "from\n" +
                        "(select event_name, start_date, visit_event.site_name, " +
                        "visit_event_date as 'Date' from visit_event " +
                        "where visitor_username='manager2'\n" +
                        "union\n" +
                        "select NULL as event_name, NULL as start_date, " +
                        "visit_site.site_name, visit_site_date as 'Date' " +
                        "from visit_site where visitor_username='manager2') t\n" +
                        "group by site_name\n" +
                        "order by site_name) as myvisits\n" +
                        "on totalvisits.site_name=myvisits.site_name\n" +
                        "where total_visits between ? and ? #Total Visits Range filter\n" +
                        "and event_count between ? and ? #Event Count Range filter\n" +
                        "and my_visits is NULL #if include visited not checked " +
                        "#My Visit Null Filter\n" +
                        "order by site_name;\n" +
                        "#sort by each column\n" +
                        "-- order by site_name desc\n" +
                        "-- order by event_count\n" +
                        "-- order by event_count desc\n" +
                        "-- order by total_visits\n" +
                        "-- order by total_visits desc\n" +
                        "-- order by my_visits\n" +
                        "-- order by my_visits desc\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, openEveryday.getValue().toString().trim());
                pst.setString(2, startDate.getText());
                pst.setString(3, endDate.getText());
                pst.setInt(4, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(5, Integer.parseInt(totalVisitsRangeMax.getText()));
                pst.setInt(6, Integer.parseInt(eventCountRangeMin.getText()));
                pst.setInt(7, Integer.parseInt(eventCountRangeMax.getText()));
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    visitorExploreSiteData.add(new VisitorExploreSiteData(
                            new SimpleStringProperty(rs.getString("site_name")),
                            Integer.valueOf(rs.getInt("event_count")),
                            Integer.valueOf(rs.getInt("total_visits")),
                            Integer.valueOf(rs.getInt("my_visits"))));
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

        siteNameCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        siteNameCol.setCellFactory(param -> new TableCell<VisitorExploreSiteData,
                VisitorExploreSiteData>() {
            @Override
            public void updateItem(VisitorExploreSiteData obj, boolean
                    empty) {
                super.updateItem(obj, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    RadioButton radioButton =
                            new RadioButton(obj.getSiteName());
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
        eventCountCol.setCellValueFactory(new PropertyValueFactory<>(
                "eventCount"));
        totalVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "totalVisits"));
        myVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "myVisits"));

        exploreSiteTable.setItems(visitorExploreSiteData);

    }

    private void unChekcedAllEveryday() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select eventcount.site_name, event_count, " +
                        "total_visits, my_visits from\n" +
                        "(select site.site_name, count(*) as event_count\n" +
                        "from site join event on site.site_name=event.site_name\n" +
                        "where site.site_name=? #Site Name Filter\n" +
//                        "and open_everyday='No' #Open Everyday Filter\n" +
                        "and ? < start_date #Start Date Filter\n" +
                        "and end_date<? #End Date Filter\n" +
                        "group by site.site_name\n" +
                        "order by site_name) as eventcount\n" +
                        "right outer join\n" +
                        "(select site_name, count(*) as total_visits\n" +
                        "from\n" +
                        "(select visitor_username, event_name, start_date, " +
                        "visit_event.site_name, visit_event_date as 'Date' from visit_event\n" +
                        "union\n" +
                        "select visitor_username, NULL as event_name, " +
                        "NULL as start_date, visit_site.site_name, visit_site_date " +
                        "as 'Date' from visit_site) t\n" +
                        "group by site_name\n" +
                        "order by site_name) as totalvisits\n" +
                        "on totalvisits.site_name=eventcount.site_name\n" +
                        "left outer join\n" +
                        "(select site_name, count(*) as my_visits\n" +
                        "from\n" +
                        "(select event_name, start_date, visit_event.site_name, " +
                        "visit_event_date as 'Date' from visit_event " +
                        "where visitor_username='manager2'\n" +
                        "union\n" +
                        "select NULL as event_name, NULL as start_date, " +
                        "visit_site.site_name, visit_site_date as 'Date' " +
                        "from visit_site where visitor_username='manager2') t\n" +
                        "group by site_name\n" +
                        "order by site_name) as myvisits\n" +
                        "on totalvisits.site_name=myvisits.site_name\n" +
                        "where total_visits between ? and ? #Total Visits Range filter\n" +
                        "and event_count between ? and ? #Event Count Range filter\n" +
                        "and my_visits is NULL #if include visited not checked " +
                        "#My Visit Null Filter\n" +
                        "order by site_name;\n" +
                        "#sort by each column\n" +
                        "-- order by site_name desc\n" +
                        "-- order by event_count\n" +
                        "-- order by event_count desc\n" +
                        "-- order by total_visits\n" +
                        "-- order by total_visits desc\n" +
                        "-- order by my_visits\n" +
                        "-- order by my_visits desc\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name.getValue().toString().trim());
                pst.setString(2, startDate.getText());
                pst.setString(3, endDate.getText());
                pst.setInt(4, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(5, Integer.parseInt(totalVisitsRangeMax.getText()));
                pst.setInt(6, Integer.parseInt(eventCountRangeMin.getText()));
                pst.setInt(7, Integer.parseInt(eventCountRangeMax.getText()));
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    visitorExploreSiteData.add(new VisitorExploreSiteData(
                            new SimpleStringProperty(rs.getString("site_name")),
                            Integer.valueOf(rs.getInt("event_count")),
                            Integer.valueOf(rs.getInt("total_visits")),
                            Integer.valueOf(rs.getInt("my_visits"))));
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

        siteNameCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        siteNameCol.setCellFactory(param -> new TableCell<VisitorExploreSiteData,
                VisitorExploreSiteData>() {
            @Override
            public void updateItem(VisitorExploreSiteData obj, boolean
                    empty) {
                super.updateItem(obj, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    RadioButton radioButton =
                            new RadioButton(obj.getSiteName());
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
        eventCountCol.setCellValueFactory(new PropertyValueFactory<>(
                "eventCount"));
        totalVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "totalVisits"));
        myVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "myVisits"));

        exploreSiteTable.setItems(visitorExploreSiteData);

    }

    private void unCheckedAllBoth() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select eventcount.site_name, event_count, " +
                        "total_visits, my_visits from\n" +
                        "(select site.site_name, count(*) as event_count\n" +
                        "from site join event on site.site_name=event.site_name\n" +
//                        "where site.site_name=? #Site Name Filter\n" +
//                        "and open_everyday='No' #Open Everyday Filter\n" +
                        "where ?<start_date #Start Date Filter\n" +
                        "and end_date<? #End Date Filter\n" +
                        "group by site.site_name\n" +
                        "order by site_name) as eventcount\n" +
                        "right outer join\n" +
                        "(select site_name, count(*) as total_visits\n" +
                        "from\n" +
                        "(select visitor_username, event_name, start_date, " +
                        "visit_event.site_name, visit_event_date as 'Date' from visit_event\n" +
                        "union\n" +
                        "select visitor_username, NULL as event_name, " +
                        "NULL as start_date, visit_site.site_name, visit_site_date " +
                        "as 'Date' from visit_site) t\n" +
                        "group by site_name\n" +
                        "order by site_name) as totalvisits\n" +
                        "on totalvisits.site_name=eventcount.site_name\n" +
                        "left outer join\n" +
                        "(select site_name, count(*) as my_visits\n" +
                        "from\n" +
                        "(select event_name, start_date, visit_event.site_name, " +
                        "visit_event_date as 'Date' from visit_event " +
                        "where visitor_username='manager2'\n" +
                        "union\n" +
                        "select NULL as event_name, NULL as start_date, " +
                        "visit_site.site_name, visit_site_date as 'Date' " +
                        "from visit_site where visitor_username='manager2') t\n" +
                        "group by site_name\n" +
                        "order by site_name) as myvisits\n" +
                        "on totalvisits.site_name=myvisits.site_name\n" +
                        "where total_visits between ? and ? #Total Visits Range filter\n" +
                        "and event_count between ? and ? #Event Count Range filter\n" +
                        "and my_visits is NULL #if include visited not checked " +
                        "#My Visit Null Filter\n" +
                        "order by site_name;\n" +
                        "#sort by each column\n" +
                        "-- order by site_name desc\n" +
                        "-- order by event_count\n" +
                        "-- order by event_count desc\n" +
                        "-- order by total_visits\n" +
                        "-- order by total_visits desc\n" +
                        "-- order by my_visits\n" +
                        "-- order by my_visits desc\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, startDate.getText());
                pst.setString(2, endDate.getText());
                pst.setInt(3, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(4, Integer.parseInt(totalVisitsRangeMax.getText()));
                pst.setInt(5, Integer.parseInt(eventCountRangeMin.getText()));
                pst.setInt(6, Integer.parseInt(eventCountRangeMax.getText()));
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    visitorExploreSiteData.add(new VisitorExploreSiteData(
                            new SimpleStringProperty(rs.getString("site_name")),
                            Integer.valueOf(rs.getInt("event_count")),
                            Integer.valueOf(rs.getInt("total_visits")),
                            Integer.valueOf(rs.getInt("my_visits"))));
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

        siteNameCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        siteNameCol.setCellFactory(param -> new TableCell<VisitorExploreSiteData,
                VisitorExploreSiteData>() {
            @Override
            public void updateItem(VisitorExploreSiteData obj, boolean
                    empty) {
                super.updateItem(obj, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    RadioButton radioButton =
                            new RadioButton(obj.getSiteName());
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
        eventCountCol.setCellValueFactory(new PropertyValueFactory<>(
                "eventCount"));
        totalVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "totalVisits"));
        myVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "myVisits"));

        exploreSiteTable.setItems(visitorExploreSiteData);

    }

    private void chekcedAllNeither() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select eventcount.site_name, event_count, " +
                        "total_visits, my_visits from\n" +
                        "(select site.site_name, count(*) as event_count\n" +
                        "from site join event on site.site_name=event.site_name\n" +
                        "where site.site_name=? #Site Name Filter\n" +
                        "and open_everyday=? #Open Everyday Filter\n" +
                        "and ? < start_date #Start Date Filter\n" +
                        "and end_date<? #End Date Filter\n" +
                        "group by site.site_name\n" +
                        "order by site_name) as eventcount\n" +
                        "right outer join\n" +
                        "(select site_name, count(*) as total_visits\n" +
                        "from\n" +
                        "(select visitor_username, event_name, start_date, " +
                        "visit_event.site_name, visit_event_date as 'Date' from visit_event\n" +
                        "union\n" +
                        "select visitor_username, NULL as event_name, " +
                        "NULL as start_date, visit_site.site_name, visit_site_date " +
                        "as 'Date' from visit_site) t\n" +
                        "group by site_name\n" +
                        "order by site_name) as totalvisits\n" +
                        "on totalvisits.site_name=eventcount.site_name\n" +
                        "left outer join\n" +
                        "(select site_name, count(*) as my_visits\n" +
                        "from\n" +
                        "(select event_name, start_date, visit_event.site_name, " +
                        "visit_event_date as 'Date' from visit_event " +
                        "where visitor_username='manager2'\n" +
                        "union\n" +
                        "select NULL as event_name, NULL as start_date, " +
                        "visit_site.site_name, visit_site_date as 'Date' " +
                        "from visit_site where visitor_username='manager2') t\n" +
                        "group by site_name\n" +
                        "order by site_name) as myvisits\n" +
                        "on totalvisits.site_name=myvisits.site_name\n" +
                        "where total_visits between ? and ? #Total Visits " +
                        "Range filter\n" +
                        "and event_count between ? and ? #Event Count Range" +
                        " filter\n" +
                        "#and my_visits is NULL; #if include visited not checked " +
                        "#My Visit Null Filter\n" +
                        "order by site_name;\n" +
                        "#sort by each column\n" +
                        "-- order by site_name desc\n" +
                        "-- order by event_count\n" +
                        "-- order by event_count desc\n" +
                        "-- order by total_visits\n" +
                        "-- order by total_visits desc\n" +
                        "-- order by my_visits\n" +
                        "-- order by my_visits desc\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, openEveryday.getValue().toString().trim());
                pst.setString(2, name.getValue().toString().trim());
                pst.setString(3, startDate.getText());
                pst.setString(4, endDate.getText());
                pst.setInt(5, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(6, Integer.parseInt(totalVisitsRangeMax.getText()));
                pst.setInt(7, Integer.parseInt(eventCountRangeMin.getText()));
                pst.setInt(8, Integer.parseInt(eventCountRangeMax.getText()));
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    visitorExploreSiteData.add(new VisitorExploreSiteData(
                            new SimpleStringProperty(rs.getString("site_name")),
                            Integer.valueOf(rs.getInt("event_count")),
                            Integer.valueOf(rs.getInt("total_visits")),
                            Integer.valueOf(rs.getInt("my_visits"))));
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

        siteNameCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        siteNameCol.setCellFactory(param -> new TableCell<VisitorExploreSiteData,
                VisitorExploreSiteData>() {
            @Override
            public void updateItem(VisitorExploreSiteData obj, boolean
                    empty) {
                super.updateItem(obj, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    RadioButton radioButton =
                            new RadioButton(obj.getSiteName());
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
        eventCountCol.setCellValueFactory(new PropertyValueFactory<>(
                "eventCount"));
        totalVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "totalVisits"));
        myVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "myVisits"));

        exploreSiteTable.setItems(visitorExploreSiteData);

    }

    private void checkedAllName() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select eventcount.site_name, event_count, " +
                        "total_visits, my_visits from\n" +
                        "(select site.site_name, count(*) as event_count\n" +
                        "from site join event on site.site_name=event.site_name\n" +
//                        "where site.site_name=? #Site Name Filter\n" +
                        "where open_everyday=? #Open Everyday Filter\n" +
                        "and ? < start_date #Start Date Filter\n" +
                        "and end_date<? #End Date Filter\n" +
                        "group by site.site_name\n" +
                        "order by site_name) as eventcount\n" +
                        "right outer join\n" +
                        "(select site_name, count(*) as total_visits\n" +
                        "from\n" +
                        "(select visitor_username, event_name, start_date, " +
                        "visit_event.site_name, visit_event_date as 'Date' from visit_event\n" +
                        "union\n" +
                        "select visitor_username, NULL as event_name, " +
                        "NULL as start_date, visit_site.site_name, visit_site_date " +
                        "as 'Date' from visit_site) t\n" +
                        "group by site_name\n" +
                        "order by site_name) as totalvisits\n" +
                        "on totalvisits.site_name=eventcount.site_name\n" +
                        "left outer join\n" +
                        "(select site_name, count(*) as my_visits\n" +
                        "from\n" +
                        "(select event_name, start_date, visit_event.site_name, " +
                        "visit_event_date as 'Date' from visit_event " +
                        "where visitor_username='manager2'\n" +
                        "union\n" +
                        "select NULL as event_name, NULL as start_date, " +
                        "visit_site.site_name, visit_site_date as 'Date' " +
                        "from visit_site where visitor_username='manager2') t\n" +
                        "group by site_name\n" +
                        "order by site_name) as myvisits\n" +
                        "on totalvisits.site_name=myvisits.site_name\n" +
                        "where total_visits between ? and ? #Total Visits " +
                        "Range filter\n" +
                        "and event_count between ? and ? #Event Count Range" +
                        " filter\n" +
                        "#and my_visits is NULL; #if include visited not checked " +
                        "#My Visit Null Filter\n" +
                        "order by site_name;\n" +
                        "#sort by each column\n" +
                        "-- order by site_name desc\n" +
                        "-- order by event_count\n" +
                        "-- order by event_count desc\n" +
                        "-- order by total_visits\n" +
                        "-- order by total_visits desc\n" +
                        "-- order by my_visits\n" +
                        "-- order by my_visits desc\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, openEveryday.getValue().toString().trim());
                pst.setString(2, startDate.getText());
                pst.setString(3, endDate.getText());
                pst.setInt(4, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(5, Integer.parseInt(totalVisitsRangeMax.getText()));
                pst.setInt(6, Integer.parseInt(eventCountRangeMin.getText()));
                pst.setInt(7, Integer.parseInt(eventCountRangeMax.getText()));
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    visitorExploreSiteData.add(new VisitorExploreSiteData(
                            new SimpleStringProperty(rs.getString("site_name")),
                            Integer.valueOf(rs.getInt("event_count")),
                            Integer.valueOf(rs.getInt("total_visits")),
                            Integer.valueOf(rs.getInt("my_visits"))));
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

        siteNameCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        siteNameCol.setCellFactory(param -> new TableCell<VisitorExploreSiteData,
                VisitorExploreSiteData>() {
            @Override
            public void updateItem(VisitorExploreSiteData obj, boolean
                    empty) {
                super.updateItem(obj, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    RadioButton radioButton =
                            new RadioButton(obj.getSiteName());
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
        eventCountCol.setCellValueFactory(new PropertyValueFactory<>(
                "eventCount"));
        totalVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "totalVisits"));
        myVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "myVisits"));

        exploreSiteTable.setItems(visitorExploreSiteData);

    }

    private void chekcedAllEveryday() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select eventcount.site_name, event_count, " +
                        "total_visits, my_visits from\n" +
                        "(select site.site_name, count(*) as event_count\n" +
                        "from site join event on site.site_name=event.site_name\n" +
                        "where site.site_name=? #Site Name Filter\n" +
//                        "and open_everyday='No' #Open Everyday Filter\n" +
                        "and ? < start_date #Start Date Filter\n" +
                        "and end_date<? #End Date Filter\n" +
                        "group by site.site_name\n" +
                        "order by site_name) as eventcount\n" +
                        "right outer join\n" +
                        "(select site_name, count(*) as total_visits\n" +
                        "from\n" +
                        "(select visitor_username, event_name, start_date, " +
                        "visit_event.site_name, visit_event_date as 'Date' from visit_event\n" +
                        "union\n" +
                        "select visitor_username, NULL as event_name, " +
                        "NULL as start_date, visit_site.site_name, visit_site_date " +
                        "as 'Date' from visit_site) t\n" +
                        "group by site_name\n" +
                        "order by site_name) as totalvisits\n" +
                        "on totalvisits.site_name=eventcount.site_name\n" +
                        "left outer join\n" +
                        "(select site_name, count(*) as my_visits\n" +
                        "from\n" +
                        "(select event_name, start_date, visit_event.site_name, " +
                        "visit_event_date as 'Date' from visit_event " +
                        "where visitor_username='manager2'\n" +
                        "union\n" +
                        "select NULL as event_name, NULL as start_date, " +
                        "visit_site.site_name, visit_site_date as 'Date' " +
                        "from visit_site where visitor_username='manager2') t\n" +
                        "group by site_name\n" +
                        "order by site_name) as myvisits\n" +
                        "on totalvisits.site_name=myvisits.site_name\n" +
                        "where total_visits between ? and ? #Total Visits " +
                        "Range filter\n" +
                        "and event_count between ? and ? #Event Count Range" +
                        " filter\n" +
                        "#and my_visits is NULL; #if include visited not checked " +
                        "#My Visit Null Filter\n" +
                        "order by site_name;\n" +
                        "#sort by each column\n" +
                        "-- order by site_name desc\n" +
                        "-- order by event_count\n" +
                        "-- order by event_count desc\n" +
                        "-- order by total_visits\n" +
                        "-- order by total_visits desc\n" +
                        "-- order by my_visits\n" +
                        "-- order by my_visits desc\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name.getValue().toString().trim());
                pst.setString(2, startDate.getText());
                pst.setString(3, endDate.getText());
                pst.setInt(4, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(5, Integer.parseInt(totalVisitsRangeMax.getText()));
                pst.setInt(6, Integer.parseInt(eventCountRangeMin.getText()));
                pst.setInt(7, Integer.parseInt(eventCountRangeMax.getText()));
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    visitorExploreSiteData.add(new VisitorExploreSiteData(
                            new SimpleStringProperty(rs.getString("site_name")),
                            Integer.valueOf(rs.getInt("event_count")),
                            Integer.valueOf(rs.getInt("total_visits")),
                            Integer.valueOf(rs.getInt("my_visits"))));
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

        siteNameCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        siteNameCol.setCellFactory(param -> new TableCell<VisitorExploreSiteData,
                VisitorExploreSiteData>() {
            @Override
            public void updateItem(VisitorExploreSiteData obj, boolean
                    empty) {
                super.updateItem(obj, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    RadioButton radioButton =
                            new RadioButton(obj.getSiteName());
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
        eventCountCol.setCellValueFactory(new PropertyValueFactory<>(
                "eventCount"));
        totalVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "totalVisits"));
        myVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "myVisits"));

        exploreSiteTable.setItems(visitorExploreSiteData);

    }

    private void checkedAllBoth() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select eventcount.site_name, event_count, " +
                        "total_visits, my_visits from\n" +
                        "(select site.site_name, count(*) as event_count\n" +
                        "from site join event on site.site_name=event.site_name\n" +
//                        "where site.site_name=? #Site Name Filter\n" +
//                        "and open_everyday='No' #Open Everyday Filter\n" +
                        "where ?<start_date #Start Date Filter\n" +
                        "and end_date<? #End Date Filter\n" +
                        "group by site.site_name\n" +
                        "order by site_name) as eventcount\n" +
                        "right outer join\n" +
                        "(select site_name, count(*) as total_visits\n" +
                        "from\n" +
                        "(select visitor_username, event_name, start_date, " +
                        "visit_event.site_name, visit_event_date as 'Date' from visit_event\n" +
                        "union\n" +
                        "select visitor_username, NULL as event_name, " +
                        "NULL as start_date, visit_site.site_name, visit_site_date " +
                        "as 'Date' from visit_site) t\n" +
                        "group by site_name\n" +
                        "order by site_name) as totalvisits\n" +
                        "on totalvisits.site_name=eventcount.site_name\n" +
                        "left outer join\n" +
                        "(select site_name, count(*) as my_visits\n" +
                        "from\n" +
                        "(select event_name, start_date, visit_event.site_name, " +
                        "visit_event_date as 'Date' from visit_event " +
                        "where visitor_username='manager2'\n" +
                        "union\n" +
                        "select NULL as event_name, NULL as start_date, " +
                        "visit_site.site_name, visit_site_date as 'Date' " +
                        "from visit_site where visitor_username='manager2') t\n" +
                        "group by site_name\n" +
                        "order by site_name) as myvisits\n" +
                        "on totalvisits.site_name=myvisits.site_name\n" +
                        "where total_visits between ? and ? #Total Visits " +
                        "Range filter\n" +
                        "and event_count between ? and ? #Event Count Range" +
                        " filter\n" +
                        "#and my_visits is NULL; #if include visited not checked " +
                        "#My Visit Null Filter\n" +
                        "order by site_name;\n" +
                        "#sort by each column\n" +
                        "-- order by site_name desc\n" +
                        "-- order by event_count\n" +
                        "-- order by event_count desc\n" +
                        "-- order by total_visits\n" +
                        "-- order by total_visits desc\n" +
                        "-- order by my_visits\n" +
                        "-- order by my_visits desc\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, startDate.getText());
                pst.setString(2, endDate.getText());
                pst.setInt(3, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(4, Integer.parseInt(totalVisitsRangeMax.getText()));
                pst.setInt(5, Integer.parseInt(eventCountRangeMin.getText()));
                pst.setInt(6, Integer.parseInt(eventCountRangeMax.getText()));
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    visitorExploreSiteData.add(new VisitorExploreSiteData(
                            new SimpleStringProperty(rs.getString("site_name")),
                            Integer.valueOf(rs.getInt("event_count")),
                            Integer.valueOf(rs.getInt("total_visits")),
                            Integer.valueOf(rs.getInt("my_visits"))));
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

        siteNameCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        siteNameCol.setCellFactory(param -> new TableCell<VisitorExploreSiteData,
                VisitorExploreSiteData>() {
            @Override
            public void updateItem(VisitorExploreSiteData obj, boolean
                    empty) {
                super.updateItem(obj, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    RadioButton radioButton =
                            new RadioButton(obj.getSiteName());
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
        eventCountCol.setCellValueFactory(new PropertyValueFactory<>(
                "eventCount"));
        totalVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "totalVisits"));
        myVisitsCol.setCellValueFactory(new PropertyValueFactory<>(
                "myVisits"));

        exploreSiteTable.setItems(visitorExploreSiteData);

    }

    private boolean allDataValid() {
        if (startDate.getText().trim().equals("") || endDate.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please fill the blank");
            alert.showAndWait();
            return false;
        } else if (!checkerFunction.verifyDateFormat(startDate.getText()) || !checkerFunction.verifyDateFormat(endDate.getText())) {
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

    @FXML
    public void btnActionVisitorExploreSiteDetail(ActionEvent event) {
        try {
            VisitorExploreSiteData item =
                    (VisitorExploreSiteData) exploreSiteTable.getItems().get(colIndex);
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                // create a connection to the database
                Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                        .password);

                try {
                    //query

                    // sql statements

                    //if no row return, go to catch
                    String sql = ("select site_name, open_everyday, site_address " +
                            "from site " +
                            "where site_name=?;");
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, item.getSiteName());
                    ResultSet rs = pst.executeQuery();

                    while (rs.next()) {
                        detailSiteName = rs.getString("site_name");
                        detailOpenEveryday = rs.getString("open_everyday");
                        detailSiteAddress = rs.getString("site_address");
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

            Session.siteDetail = new SiteDetail(detailSiteName,
                    detailOpenEveryday, detailSiteAddress);

            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Visitor_Site_Detail.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please select an item");
            alert.showAndWait();
        }
    }

    @FXML
    public void btnActionVisitorExploreSiteTransitDetail(ActionEvent event) {
        try {
            VisitorExploreSiteData item =
                    (VisitorExploreSiteData) exploreSiteTable.getItems().get(colIndex);

            Session.siteDetail = new SiteDetail(item.getSiteName());
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Visitor_Transit_Detail.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please select an item");
            alert.showAndWait();
        }
    }


    @FXML
    public void btnActionVisitorExploreSiteBack(ActionEvent event) {
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
