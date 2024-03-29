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
    }
    private void loadFreeStaffs() {
        staffAssigned.getItems().clear();
        staffAvailableName.clear();
        staffAvailableUsername.clear();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);
            String temp = "select username, concat(firstname, ' ', lastname) as Name from staff natural join user where username not in\n" +
                    "(select distinct staff_username from event right join assign_to on event.event_name=assign_to.event_name and event.site_name=assign_to.site_name and event.start_date=assign_to.start_date\n" +
                    "where staff_username not in (select staff_username where ?<event.start_date or end_date<?));";

            PreparedStatement pst = conn.prepareStatement(temp);
            pst.setString(1, endDate.getText().trim());
            pst.setString(2, startDate.getText().trim());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                staffAvailableUsername.add(rs.getString("username"));
                staffAvailableName.add(rs.getString("Name"));
            }

            staffAssigned.getItems().setAll(staffAvailableName);

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

    @FXML
    public void btnActionManagerFilterStaff(ActionEvent event) {
        if (!checkerFunction.verifyStartEndDate(startDate.getText(),endDate.getText())) {
            return;
        }
        if (startDate.getText().trim().isEmpty() || endDate.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please provide dates before requesting for available staffs.");
            alert.showAndWait();
            return;
        }
        if (startDate.getText().trim().compareTo(endDate.getText().trim()) > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The starting date must be before or equal to the end date.");
            alert.showAndWait();
            return;
        }

        if (!checkerFunction.verifyDateFormat(startDate.getText().trim()) ||
                !checkerFunction.verifyDateFormat(endDate.getText().trim())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("The date should follow the format" +
                    "####-##-##");
            alert.showAndWait();
            return;
        }
        try {
            loadFreeStaffs();
        } catch (Exception e) {
            e.printStackTrace();
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
                descriptionIn.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please provide all " +
                    "necessary data.");
            alert.showAndWait();
            return false;
        }
        if (!isStaffChosen()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Please choose " + minimumStaffRequired.getText().trim() + " or more staffs.");
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
