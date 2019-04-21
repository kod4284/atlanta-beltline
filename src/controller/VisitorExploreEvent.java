package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.DB;
import model.Session;
import model.checkerFunction;

import javafx.scene.control.*;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    ToggleGroup group;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        group = new ToggleGroup();
    }

    @FXML
    public void btnActionVisitorExploreEventFilter(ActionEvent event) {
        LoadTableData();
    }
    private void LoadTableData() {
        if (!allDataValid()) {
            return;
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
                String sql = ("select site_name from site;\n" +
                        "\n" +
                        "select t1.event_name, t1.site_name, t1.start_date, event_price, ticket_remaining, total_visits, my_visits from\n" +
                        "(select event.event_name, event.site_name, event.start_date, event_price, capacity-if(count(visitor_username)=0, 0, count(*)) as ticket_remaining, if(count(visitor_username)=0, 0, count(*)) as total_visits\n" +
                        "from event left outer join visit_event \n" +
                        "on event.event_name=visit_event.event_name and event.start_date=visit_event.start_date and event.site_name=visit_event.site_name\n" +
                        "#where event.event_name like '%%'\n" +
                        "#and description like '%%'\n" +
                        "#and event.site_name='Inman Park'\n" +
                        "#and '2019-02-01'<=event.start_date\n" +
                        "#and event.end_date<='2020-01-01'\n" +
                        "#and event_price between 0 and 9999\n" +
                        "group by event.event_name, event.site_name, event.start_date\n" +
                        "order by event.event_name) as t1\n" +
                        "left outer join\n" +
                        "(select event_name, site_name, start_date, if(count(*)=0, 0, count(*)) as my_visits\n" +
                        "from visit_event where visitor_username='mary.smith'\n" +
                        "group by event_name, site_name, start_date\n" +
                        "order by event_name) as t2\n" +
                        "on t1.event_name=t2.event_name and t1.site_name=t2.site_name and t1.start_date=t2.start_date\n" +
                        "#where total_visits between 0 and 20\n" +
                        "#and my_visits is NULL #if include visited not checked\n" +
                        "#and ticket_remaining>0 #if include sold out event not checked\n" +
                        "order by event_name;\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, email_field.getText());
                ResultSet rs = pst.executeQuery();

            } catch (IOException e) {
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

    private boolean allDataValid() {
        if (name.getText() == "" || descriptionKeyword.getText() == "" || startDate.getText() == "" || endDate.getText() == "" ||
        totalVisitsRangeMin.getText() == "" || totalVisitsRangeMax.getText() == "" || totalRevenueRangeMin.getText() == "" ||
        totalRevenueRangeMax.getText() == "") {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please fill the blank");
            alert.showAndWait();
            return false;
        } else if (checkerFunction.verifyDateFormat(startDate.getText()) || checkerFunction.verifyDateFormat(endDate.getText())) {
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
