package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.DB;
import model.Session;
import model.checkerFunction;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerCreateEvent implements Initializable {

    @FXML TextField name;
    @FXML TextField price;
    @FXML TextField capacity;
    @FXML TextField minimumStaffRequired;
    @FXML TextField startDate;
    @FXML TextField endDate;
    @FXML TextArea description;
    @FXML ListView<String> staffAssigned;

    private List<String> staffAvailableName;
    private List<String> staffAvailableUsername;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staffAvailableName = new ArrayList<>();
        staffAvailableUsername = new ArrayList<>();
        staffAssigned.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadFreeStaffs();
    }
    private void loadFreeStaffs() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);
            String availableStaff = "select concat(firstname, ' ', lastname) as available_staff, t1.username as username from\n" +
                    "(select staff_username as username from event natural join assign_to where '2019-02-02'<start_date or end_date<'2019-02-01'\n" +
                    "union\n" +
                    "select username from staff where username not in (select staff_username from assign_to)) t1\n" +
                    "join\n" +
                    "(select username, firstname, lastname from user) t2\n" +
                    "on t1.username=t2.username;\n";
            PreparedStatement pst = conn.prepareStatement(availableStaff);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                staffAvailableName.add(rs.getString("available_staff"));
                staffAvailableUsername.add(rs.getString("username"));
            }

            staffAssigned.getItems().addAll(staffAvailableName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnActionManagerCreateEventBack(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Manager_Manage_Event.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }

    public void btnActionManagerCreateEventCreate(ActionEvent event) {
        if (!isInfoAllFilled()) {
            return;
        }
        String nameIn = name.getText().trim();
        String priceIn = price.getText().trim();
        String capacityIn = capacity.getText().trim();
        String minStaffRequiredIn = minimumStaffRequired.getText().trim();
        String sD = startDate.getText().trim();
        String eD = endDate.getText().trim();
        String descriptionIn = description.getText().trim();
        List<Integer> chosenStaffs = staffAssigned.getSelectionModel().getSelectedIndices();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            final String mUsername = Session.user.getUsername();

            String createEventSql = "insert into event values (?, " +
                    "?, (select site_name from site where manager_username= ?), " +
                    "?, " + priceIn + ", " + capacityIn + ", " + minStaffRequiredIn + ", ?);";

            PreparedStatement pst1 = conn.prepareStatement(createEventSql);
            pst1.setString(1, nameIn);
            pst1.setString(2, sD);
            pst1.setString(3, mUsername);
            pst1.setString(4, eD);
            pst1.setString(5, descriptionIn);
            pst1.executeUpdate();

            for (Integer i : chosenStaffs) {
                String assignStaffSql = "insert into assign_to values (?, ?, ?, " +
                        "(select site_name from site where manager_username = ?));";
                PreparedStatement pst2 = conn.prepareStatement(assignStaffSql);
                pst2.setString(1, staffAvailableUsername.get(i));
                pst2.setString(2, nameIn);
                pst2.setString(3, sD);
                pst2.setString(4, mUsername);
                pst2.executeUpdate();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Update");
            alert.setContentText("Event successfully created!");
            alert.showAndWait();

            try {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                        .getWindow();
                Parent root = FXMLLoader.load(getClass()
                        .getResource("../view/Manager_Manage_Event.fxml"));
                primaryStage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Cannot load User_Login.fxml");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isInfoAllFilled() {
        String nameIn = name.getText().trim();
        String priceIn = price.getText().trim();
        String capacityIn = capacity.getText().trim();
        String minStaffRequiredIn = minimumStaffRequired.getText().trim();
        String sD = startDate.getText().trim();
        String eD = endDate.getText().trim();
        String descriptionIn = description.getText().trim();
        List<Integer> chosenStaffs = staffAssigned.getSelectionModel().getSelectedIndices();
        if (nameIn.isEmpty() || priceIn.isEmpty() ||
                capacityIn.isEmpty() || minStaffRequiredIn.isEmpty() ||
                sD.isEmpty() || eD.isEmpty() ||
                descriptionIn.isEmpty() || !isStaffChosen()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please provide all " +
                    "necessary data.");
            alert.showAndWait();
            return false;
        }
        if (!checkerFunction.verifyDateFormat(sD) ||
                !checkerFunction.verifyDateFormat(eD)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The date should follow the format" +
                    "####-##-##");
            alert.showAndWait();
            return false;
        }
        if (sD.compareTo(eD) > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The start date is later than end date.");
            alert.showAndWait();
            return false;
        }
        try {
            Double.parseDouble(price.getText().trim());
            Integer.parseInt(capacity.getText().trim());
            Integer.parseInt(minimumStaffRequired.getText().trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please only input number for Price, Capapcity, and Min. Staff Required.\n" +
                    "Check that Capacity and Min. Staff Required should be whole numbers.");
            alert.showAndWait();
            return false;
        }
        if (Integer.parseInt(minStaffRequiredIn) < 1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("We need at least one staff.");
            alert.showAndWait();
            return false;
        }
        if (Integer.parseInt(priceIn) < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Price needs to be greater than 0.");
            alert.showAndWait();
            return false;
        }
        if (Integer.parseInt(capacityIn) < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Capacity needs to be greater than 0.");
            alert.showAndWait();
            return false;
        }
        return true;
    }
    private boolean isStaffChosen () {
        List<Integer> indices = staffAssigned.getSelectionModel().getSelectedIndices();
        if (indices.isEmpty()) {
            return false;
        }
        if (indices.size() < Integer.parseInt(minimumStaffRequired.getText().trim())) {
            return false;
        }
        return true;
    }
}
