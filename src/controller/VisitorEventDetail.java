package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DB;
import model.Session;
import model.VisitorExploreEventData;
import model.checkerFunction;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class VisitorEventDetail implements Initializable {

    @FXML Label event;
    @FXML Label site;
    @FXML Label startDate;
    @FXML Label endDate;
    @FXML Label ticketPrice;
    @FXML Label ticketRemaining;
    @FXML TextArea description;
    @FXML TextField visitDate;

    String startDateString;
    String endDateString;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }

    private void loadData() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select event.event_name, event.site_name, event.start_date, end_date, event_price, " +
                        "capacity-if(count(visitor_username)=0, 0, count(*)) as 'Tickets Remaining', description\n" +
                        "from event left outer join visit_event \n" +
                        "on event.event_name=visit_event.event_name and event.start_date=visit_event.start_date and " +
                        "event.site_name=visit_event.site_name\n" +
                        "where event.event_name=? and event.site_name=? and " +
                        "event.start_date=?\n" +
                        "group by event.event_name, event.site_name, event.start_date;\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.eventDetail.getEventName());
                pst.setString(2, Session.eventDetail.getSiteName());
                pst.setString(3, Session.eventDetail.getStartDate());
                ResultSet rs = pst.executeQuery();

                while(rs.next()) {
                    event.setText(rs.getString("event_name"));
                    site.setText(rs.getString("site_name"));
                    endDateString = rs.getString("end_date");
                    startDateString = rs.getString("start_date");
                    ticketPrice.setText(rs.getString("event_price"));
                    ticketRemaining.setText(rs.getString("Tickets Remaining"));
                    description.setText(rs.getString("description"));
                }
                description.setWrapText(true);
                startDate.setText(startDateString);
                endDate.setText(endDateString);


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
    public void btnActionVisitorEventDetailLogVisit(ActionEvent event) {
        if (!checkVisitDate()) {
            return;
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            //query

            // sql statements

            //if no row return, go to catch
            if (Session.eventDetail.getTicketRemaining() > 0) {
            String sql = ("insert into visit_event values (?, ?," +
                    " ?, ?, ?); \n" +
                    "#only insert if tickets remaining>0\n");
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, Session.user.getUsername());
            pst.setString(2, Session.eventDetail.getEventName());
            pst.setString(3, Session.eventDetail.getStartDate());
            pst.setString(4, Session.eventDetail.getSiteName());
            pst.setString(5, visitDate.getText());
            int rs = pst.executeUpdate();
                System.out.println(rs + "  inserted");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Field input Information");
                alert.setContentText("The data is successfully inserted");
                alert.showAndWait();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Ticket is sold out");
                alert.setContentText("You cannot buy a ticket. The " +
                        "ticket is sold out");
                alert.showAndWait();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Duplicate Visit Input");
            alert.setContentText("You cannot insert the same Visit");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkVisitDate() {
        if (!checkerFunction.verifyDateFormat(visitDate.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The date should follow the format" +
                    "####-##-##");
            alert.showAndWait();
            return false;
        }
        if (!checkerFunction.verifyBetweenDate(startDateString, endDateString
                , visitDate.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The date should be between start date and " +
                    "end date");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    @FXML
    public void btnActionVisitorEventDetailBack(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Visitor_Explore_Event.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }

}
