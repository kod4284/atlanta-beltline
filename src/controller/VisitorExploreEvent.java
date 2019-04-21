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
    @FXML TextField totalRevenueRangeMin;
    @FXML TextField totalRevenueRangeMax;
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
        if (!allDataValid()) {
            return;
        }
        exploreEventData = FXCollections.observableArrayList();
        if (totalVisitsRangeMin.getText().trim().equals("")) {
            totalVisitsRangeMin.setText("0");
        }
        if (totalVisitsRangeMax.getText().trim().equals("")) {
            totalVisitsRangeMax.setText("9999");
        }
        if (totalRevenueRangeMin.getText().trim().equals("")) {
            totalRevenueRangeMin.setText("0");
        }
        if (totalRevenueRangeMax.getText().trim().equals("")) {
            totalRevenueRangeMax.setText("9999");
        }

        if (siteName.getValue().toString().equals("-- ALL --")) {
            if (includeVisited.isSelected() && includeSoldOutEvent.isSelected()) {
                sqlAllCheckedboth();
            } else if (includeVisited.isSelected() && !includeSoldOutEvent.isSelected()) {
                sqlAllVisitedChecked();
            } else if (!includeVisited.isSelected() && includeSoldOutEvent.isSelected()) {
                sqlAllSoldOutChecked();
            } else {
                sqlAllNotCheckdBoth();
            }
        } else {
            if (includeVisited.isSelected() && includeSoldOutEvent.isSelected()) {
                sqlNotAllCheckedboth();
            } else if (includeVisited.isSelected() && !includeSoldOutEvent.isSelected()) {
                sqlNotAllVisitedChecked();
            } else if (!includeVisited.isSelected() && includeSoldOutEvent.isSelected()) {
                sqlNotAllSoldOutChecked();
            } else {
                sqlNotAllNotCheckdBoth();
            }
        }

    }

    private void sqlAllCheckedboth() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.event_name, t1.site_name, t1.start_date, event_price, ticket_remaining, total_visits, my_visits from\n" +
                        "(select event.event_name, event.site_name, event.start_date, event_price, capacity-if(count(visitor_username)=0, 0, count(*)) as ticket_remaining, if(count(visitor_username)=0, 0, count(*)) as total_visits\n" +
                        "from event left outer join visit_event \n" +
                        "on event.event_name=visit_event.event_name and event.start_date=visit_event.start_date and " +
                        "event.site_name=visit_event.site_name\n" +
                        "where event.event_name like concat('%',?,'%') \n" +
                        "and description like concat('%',?,'%') \n" +
                        "#and event.site_name=?\n" +
                        "and ?<=event.start_date\n" +
                        "and event.end_date<=?\n" +
                        "and event_price between ? and ?\n" +
                        "group by event.event_name, event.site_name, event.start_date\n" +
                        "order by event.event_name) as t1\n" +
                        "left outer join\n" +
                        "(select event_name, site_name, start_date, if(count(*)=0, 0, count(*)) as my_visits\n" +
                        "from visit_event where visitor_username='mary.smith'\n" +
                        "group by event_name, site_name, start_date\n" +
                        "order by event_name) as t2\n" +
                        "on t1.event_name=t2.event_name and t1.site_name=t2.site_name and t1.start_date=t2.start_date\n" +
                        "where total_visits between ? and ?\n" +
                        "#and my_visits is NULL #if include visited not " +
                        "checked\n" +
                        "#and ticket_remaining>0 #if include sold out event " +
                        "not checked\n" +
                        "order by event_name;\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name.getText());
                pst.setString(2, descriptionKeyword.getText());
//                pst.setString(3, siteName.getValue().toString());
                pst.setString(3, startDate.getText());
                pst.setString(4, endDate.getText());
                pst.setInt(5, Integer.parseInt(totalRevenueRangeMin.getText()));
                pst.setInt(6, Integer.parseInt(totalRevenueRangeMax.getText()));
                pst.setInt(7, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(8,Integer.parseInt(totalVisitsRangeMax.getText()));
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

    private void sqlAllVisitedChecked() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.event_name, t1.site_name, t1.start_date, event_price, ticket_remaining, total_visits, my_visits from\n" +
                        "(select event.event_name, event.site_name, event.start_date, event_price, capacity-if(count(visitor_username)=0, 0, count(*)) as ticket_remaining, if(count(visitor_username)=0, 0, count(*)) as total_visits\n" +
                        "from event left outer join visit_event \n" +
                        "on event.event_name=visit_event.event_name and event.start_date=visit_event.start_date and " +
                        "event.site_name=visit_event.site_name\n" +
                        "where event.event_name like concat('%',?,'%') \n" +
                        "and description like concat('%',?,'%') \n" +
                        "#and event.site_name=?\n" +
                        "and ?<=event.start_date\n" +
                        "and event.end_date<=?\n" +
                        "and event_price between ? and ?\n" +
                        "group by event.event_name, event.site_name, event.start_date\n" +
                        "order by event.event_name) as t1\n" +
                        "left outer join\n" +
                        "(select event_name, site_name, start_date, if(count(*)=0, 0, count(*)) as my_visits\n" +
                        "from visit_event where visitor_username='mary.smith'\n" +
                        "group by event_name, site_name, start_date\n" +
                        "order by event_name) as t2\n" +
                        "on t1.event_name=t2.event_name and t1.site_name=t2.site_name and t1.start_date=t2.start_date\n" +
                        "where total_visits between ? and ?\n" +
                        "#and my_visits is NULL #if include visited not " +
                        "checked\n" +
                        "and ticket_remaining>0 #if include sold out event " +
                        "not checked\n" +
                        "order by event_name;\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name.getText());
                pst.setString(2, descriptionKeyword.getText());
//                pst.setString(3, siteName.getValue().toString());
                pst.setString(3, startDate.getText());
                pst.setString(4, endDate.getText());
                pst.setInt(5, Integer.parseInt(totalRevenueRangeMin.getText()));
                pst.setInt(6, Integer.parseInt(totalRevenueRangeMax.getText()));
                pst.setInt(7, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(8,Integer.parseInt(totalVisitsRangeMax.getText()));
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

            }  catch (Exception e) {
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

    private void sqlAllSoldOutChecked() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.event_name, t1.site_name, t1.start_date, event_price, ticket_remaining, total_visits, my_visits from\n" +
                        "(select event.event_name, event.site_name, event.start_date, event_price, capacity-if(count(visitor_username)=0, 0, count(*)) as ticket_remaining, if(count(visitor_username)=0, 0, count(*)) as total_visits\n" +
                        "from event left outer join visit_event \n" +
                        "on event.event_name=visit_event.event_name and event.start_date=visit_event.start_date and " +
                        "event.site_name=visit_event.site_name\n" +
                        "where event.event_name like concat('%',?,'%') \n" +
                        "and description like concat('%',?,'%') \n" +
                        "#and event.site_name=?\n" +
                        "and ?<=event.start_date\n" +
                        "and event.end_date<=?\n" +
                        "and event_price between ? and ?\n" +
                        "group by event.event_name, event.site_name, event.start_date\n" +
                        "order by event.event_name) as t1\n" +
                        "left outer join\n" +
                        "(select event_name, site_name, start_date, if(count(*)=0, 0, count(*)) as my_visits\n" +
                        "from visit_event where visitor_username='mary.smith'\n" +
                        "group by event_name, site_name, start_date\n" +
                        "order by event_name) as t2\n" +
                        "on t1.event_name=t2.event_name and t1.site_name=t2.site_name and t1.start_date=t2.start_date\n" +
                        "where total_visits between ? and ?\n" +
                        "and my_visits is NULL #if include visited not checked\n" +
                        "#and ticket_remaining>0 #if include sold out event " +
                        "not checked\n" +
                        "order by event_name;\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name.getText());
                pst.setString(2, descriptionKeyword.getText());
//                pst.setString(3, siteName.getValue().toString());
                pst.setString(3, startDate.getText());
                pst.setString(4, endDate.getText());
                pst.setInt(5, Integer.parseInt(totalRevenueRangeMin.getText()));
                pst.setInt(6, Integer.parseInt(totalRevenueRangeMax.getText()));
                pst.setInt(7, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(8,Integer.parseInt(totalVisitsRangeMax.getText()));
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

            }  catch (Exception e) {
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

    private void sqlAllNotCheckdBoth() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.event_name, t1.site_name, t1.start_date, event_price, ticket_remaining, total_visits, my_visits from\n" +
                        "(select event.event_name, event.site_name, event.start_date, event_price, capacity-if(count(visitor_username)=0, 0, count(*)) as ticket_remaining, if(count(visitor_username)=0, 0, count(*)) as total_visits\n" +
                        "from event left outer join visit_event \n" +
                        "on event.event_name=visit_event.event_name and event.start_date=visit_event.start_date and " +
                        "event.site_name=visit_event.site_name\n" +
                        "where event.event_name like concat('%',?,'%') \n" +
                        "and description like concat('%',?,'%') \n" +
                        "#and event.site_name=?\n" +
                        "and ?<=event.start_date\n" +
                        "and event.end_date<=?\n" +
                        "and event_price between ? and ?\n" +
                        "group by event.event_name, event.site_name, event.start_date\n" +
                        "order by event.event_name) as t1\n" +
                        "left outer join\n" +
                        "(select event_name, site_name, start_date, if(count(*)=0, 0, count(*)) as my_visits\n" +
                        "from visit_event where visitor_username='mary.smith'\n" +
                        "group by event_name, site_name, start_date\n" +
                        "order by event_name) as t2\n" +
                        "on t1.event_name=t2.event_name and t1.site_name=t2.site_name and t1.start_date=t2.start_date\n" +
                        "where total_visits between ? and ?\n" +
                        "and my_visits is NULL #if include visited not checked\n" +
                        "and ticket_remaining>0 #if include sold out event " +
                        "not checked\n" +
                        "order by event_name;\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name.getText());
                pst.setString(2, descriptionKeyword.getText());
//                pst.setString(3, siteName.getValue().toString());
                pst.setString(3, startDate.getText());
                pst.setString(4, endDate.getText());
                pst.setInt(5, Integer.parseInt(totalRevenueRangeMin.getText()));
                pst.setInt(6, Integer.parseInt(totalRevenueRangeMax.getText()));
                pst.setInt(7, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(8,Integer.parseInt(totalVisitsRangeMax.getText()));
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

            }  catch (Exception e) {
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

    private void sqlNotAllCheckedboth() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.event_name, t1.site_name, t1.start_date, event_price, ticket_remaining, total_visits, my_visits from\n" +
                        "(select event.event_name, event.site_name, event.start_date, event_price, capacity-if(count(visitor_username)=0, 0, count(*)) as ticket_remaining, if(count(visitor_username)=0, 0, count(*)) as total_visits\n" +
                        "from event left outer join visit_event \n" +
                        "on event.event_name=visit_event.event_name and event.start_date=visit_event.start_date and " +
                        "event.site_name=visit_event.site_name\n" +
                        "where event.event_name like concat('%',?,'%') \n" +
                        "and description like concat('%',?,'%') \n" +
                        "and event.site_name=?\n" +
                        "and ?<=event.start_date\n" +
                        "and event.end_date<=?\n" +
                        "and event_price between ? and ?\n" +
                        "group by event.event_name, event.site_name, event.start_date\n" +
                        "order by event.event_name) as t1\n" +
                        "left outer join\n" +
                        "(select event_name, site_name, start_date, if(count(*)=0, 0, count(*)) as my_visits\n" +
                        "from visit_event where visitor_username='mary.smith'\n" +
                        "group by event_name, site_name, start_date\n" +
                        "order by event_name) as t2\n" +
                        "on t1.event_name=t2.event_name and t1.site_name=t2.site_name and t1.start_date=t2.start_date\n" +
                        "where total_visits between ? and ?\n" +
                        "#and my_visits is NULL #if include visited not " +
                        "checked\n" +
                        "#and ticket_remaining>0 #if include sold out event " +
                        "not checked\n" +
                        "order by event_name;\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name.getText());
                pst.setString(2, descriptionKeyword.getText());
                pst.setString(3, siteName.getValue().toString());
                pst.setString(4, startDate.getText());
                pst.setString(5, endDate.getText());
                pst.setInt(6, Integer.parseInt(totalRevenueRangeMin.getText()));
                pst.setInt(7, Integer.parseInt(totalRevenueRangeMax.getText()));
                pst.setInt(8, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(9,Integer.parseInt(totalVisitsRangeMax.getText()));
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
    private void sqlNotAllVisitedChecked() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.event_name, t1.site_name, t1.start_date, event_price, ticket_remaining, total_visits, my_visits from\n" +
                        "(select event.event_name, event.site_name, event.start_date, event_price, capacity-if(count(visitor_username)=0, 0, count(*)) as ticket_remaining, if(count(visitor_username)=0, 0, count(*)) as total_visits\n" +
                        "from event left outer join visit_event \n" +
                        "on event.event_name=visit_event.event_name and event.start_date=visit_event.start_date and " +
                        "event.site_name=visit_event.site_name\n" +
                        "where event.event_name like concat('%',?,'%') \n" +
                        "and description like concat('%',?,'%') \n" +
                        "and event.site_name=?\n" +
                        "and ?<=event.start_date\n" +
                        "and event.end_date<=?\n" +
                        "and event_price between ? and ?\n" +
                        "group by event.event_name, event.site_name, event.start_date\n" +
                        "order by event.event_name) as t1\n" +
                        "left outer join\n" +
                        "(select event_name, site_name, start_date, if(count(*)=0, 0, count(*)) as my_visits\n" +
                        "from visit_event where visitor_username='mary.smith'\n" +
                        "group by event_name, site_name, start_date\n" +
                        "order by event_name) as t2\n" +
                        "on t1.event_name=t2.event_name and t1.site_name=t2.site_name and t1.start_date=t2.start_date\n" +
                        "where total_visits between ? and ?\n" +
                        "#and my_visits is NULL #if include visited not " +
                        "checked\n" +
                        "and ticket_remaining>0 #if include sold out event " +
                        "not checked\n" +
                        "order by event_name;\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name.getText());
                pst.setString(2, descriptionKeyword.getText());
                pst.setString(3, siteName.getValue().toString());
                pst.setString(4, startDate.getText());
                pst.setString(5, endDate.getText());
                pst.setInt(6, Integer.parseInt(totalRevenueRangeMin.getText()));
                pst.setInt(7, Integer.parseInt(totalRevenueRangeMax.getText()));
                pst.setInt(8, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(9,Integer.parseInt(totalVisitsRangeMax.getText()));
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

            }  catch (Exception e) {
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
    private void sqlNotAllSoldOutChecked() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.event_name, t1.site_name, t1.start_date, event_price, ticket_remaining, total_visits, my_visits from\n" +
                        "(select event.event_name, event.site_name, event.start_date, event_price, capacity-if(count(visitor_username)=0, 0, count(*)) as ticket_remaining, if(count(visitor_username)=0, 0, count(*)) as total_visits\n" +
                        "from event left outer join visit_event \n" +
                        "on event.event_name=visit_event.event_name and event.start_date=visit_event.start_date and " +
                        "event.site_name=visit_event.site_name\n" +
                        "where event.event_name like concat('%',?,'%') \n" +
                        "and description like concat('%',?,'%') \n" +
                        "and event.site_name=?\n" +
                        "and ?<=event.start_date\n" +
                        "and event.end_date<=?\n" +
                        "and event_price between ? and ?\n" +
                        "group by event.event_name, event.site_name, event.start_date\n" +
                        "order by event.event_name) as t1\n" +
                        "left outer join\n" +
                        "(select event_name, site_name, start_date, if(count(*)=0, 0, count(*)) as my_visits\n" +
                        "from visit_event where visitor_username='mary.smith'\n" +
                        "group by event_name, site_name, start_date\n" +
                        "order by event_name) as t2\n" +
                        "on t1.event_name=t2.event_name and t1.site_name=t2.site_name and t1.start_date=t2.start_date\n" +
                        "where total_visits between ? and ?\n" +
                        "and my_visits is NULL #if include visited not checked\n" +
                        "#and ticket_remaining>0 #if include sold out event " +
                        "not checked\n" +
                        "order by event_name;\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name.getText());
                pst.setString(2, descriptionKeyword.getText());
                pst.setString(3, siteName.getValue().toString());
                pst.setString(4, startDate.getText());
                pst.setString(5, endDate.getText());
                pst.setInt(6, Integer.parseInt(totalRevenueRangeMin.getText()));
                pst.setInt(7, Integer.parseInt(totalRevenueRangeMax.getText()));
                pst.setInt(8, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(9,Integer.parseInt(totalVisitsRangeMax.getText()));
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

            }  catch (Exception e) {
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
    private void sqlNotAllNotCheckdBoth() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.event_name, t1.site_name, t1.start_date, event_price, ticket_remaining, total_visits, my_visits from\n" +
                        "(select event.event_name, event.site_name, event.start_date, event_price, capacity-if(count(visitor_username)=0, 0, count(*)) as ticket_remaining, if(count(visitor_username)=0, 0, count(*)) as total_visits\n" +
                        "from event left outer join visit_event \n" +
                        "on event.event_name=visit_event.event_name and event.start_date=visit_event.start_date and " +
                        "event.site_name=visit_event.site_name\n" +
                        "where event.event_name like concat('%',?,'%') \n" +
                        "and description like concat('%',?,'%') \n" +
                        "and event.site_name=?\n" +
                        "and ?<=event.start_date\n" +
                        "and event.end_date<=?\n" +
                        "and event_price between ? and ?\n" +
                        "group by event.event_name, event.site_name, event.start_date\n" +
                        "order by event.event_name) as t1\n" +
                        "left outer join\n" +
                        "(select event_name, site_name, start_date, if(count(*)=0, 0, count(*)) as my_visits\n" +
                        "from visit_event where visitor_username='mary.smith'\n" +
                        "group by event_name, site_name, start_date\n" +
                        "order by event_name) as t2\n" +
                        "on t1.event_name=t2.event_name and t1.site_name=t2.site_name and t1.start_date=t2.start_date\n" +
                        "where total_visits between ? and ?\n" +
                        "and my_visits is NULL #if include visited not checked\n" +
                        "and ticket_remaining>0 #if include sold out event " +
                        "not checked\n" +
                        "order by event_name;\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name.getText());
                pst.setString(2, descriptionKeyword.getText());
                pst.setString(3, siteName.getValue().toString());
                pst.setString(4, startDate.getText());
                pst.setString(5, endDate.getText());
                pst.setInt(6, Integer.parseInt(totalRevenueRangeMin.getText()));
                pst.setInt(7, Integer.parseInt(totalRevenueRangeMax.getText()));
                pst.setInt(8, Integer.parseInt(totalVisitsRangeMin.getText()));
                pst.setInt(9,Integer.parseInt(totalVisitsRangeMax.getText()));
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

            }  catch (Exception e) {
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



    private boolean allDataValid() {
        if (name.getText().trim().equals("") || descriptionKeyword.getText().trim().equals("")
                || startDate.getText().trim().equals("") || endDate.getText().trim().equals("")) {
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
    public void btnActionVisitorExploreEventDetail(ActionEvent event) {
        VisitorExploreEventData item =
                (VisitorExploreEventData) visitorExploreEventTable.getItems().get(colIndex);
        Session.eventDetail = new EventDetail(item.getEventName(),
                item.getSiteName(), item.getStartDate(), item.getTicketRemaining());

        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Visitor_Event_Detail.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
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
