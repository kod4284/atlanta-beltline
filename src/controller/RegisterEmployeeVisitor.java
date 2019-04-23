package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegisterEmployeeVisitor implements Initializable {
    @FXML ComboBox<EmployeeTypeForRegister> empTypes;
    @FXML ComboBox<StateType> stateTypes;
    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML TextField username;
    @FXML TextField phone;
    @FXML TextField address;
    @FXML TextField zipcode;
    @FXML TextField city;
    @FXML Button addBtn;
    @FXML TextField emailField;
    @FXML TableView emailTable;
    @FXML TableColumn<Email, Email> buttonCol;
    @FXML TableColumn<Email, String> emailCol;
    @FXML PasswordField password;
    @FXML PasswordField confirmPassword;
    private ObservableList<Email> emailData;
    private List<String> emailsFromDB;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillComboBox();
        loadEmails();
        fillEmails();
    }

    public void btnActionBack(Event event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Register_Navigation.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load Register_Navigation.fxml");
        }
    }
    public void btnActionRegister(Event event) {
        if (!isFieldsEmpty()) {
            return;
        }
        if (!checkerFunction.validatePhone(phone.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Phone input Warning");
            alert.setContentText("The format of phone number is invalid!" +
                    "\nShould be followed format: 123-123-1234");
            alert.showAndWait();
            return;
        }
        if (!checkUsernameExists()) {
            return;
        }
        if (!checkPhoneExists()) {
            return;
        }
        try {
            //query
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);
            // sql statements


            //if no row return, go to catch
            //query 1
            String sql = ("select max(employee_id) as max from employee;");
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet resultSet = pst.executeQuery();
            resultSet.next();
            long idTobeRegistered = resultSet.getLong("max") + 1;
            //query 2
            sql = ("insert into user values (?, ?, 'Pending', " +
                    "?, ?, 'Employee, Visitor');");
            pst = conn.prepareStatement(sql);
            pst.setString(1, username.getText());
            pst.setString(2, SHA256.encrypt(password.getText()));
            pst.setString(3, firstName.getText());
            pst.setString(4, lastName.getText());
            int insertedCnt = pst.executeUpdate();
            System.out.println("User: " + insertedCnt + " rows inserted!");

            //query 3
            sql = ("insert into employee values (?, ?, ?, " +
                    "?, ?, ?, ?, ?);");
            pst = conn.prepareStatement(sql);
            pst.setString(1, username.getText());
            pst.setLong(2, idTobeRegistered);
            pst.setLong(3, checkerFunction.deFormatAndInt(phone.getText()));
            pst.setString(4, address.getText());
            pst.setString(5, city.getText());
            pst.setString(6, stateTypes.getValue().toString());
            pst.setInt(7, Integer.parseInt(zipcode.getText()));
            pst.setString(8, empTypes.getValue().toString());
            insertedCnt = pst.executeUpdate();
            System.out.println("Employee: " + insertedCnt + " rows inserted!");

            //query 4
            if (empTypes.getValue().equals(EmployeeTypeForRegister.MANAGER)) {
                sql = ("insert into manager values (?);");
                pst = conn.prepareStatement(sql);
                pst.setString(1, username.getText());
                int rs = pst.executeUpdate();
                System.out.println(rs + " manager registered");
            }
            if (empTypes.getValue().equals(EmployeeTypeForRegister.STAFF)) {
                //query 5
                sql = ("insert into staff values (?);");
                pst = conn.prepareStatement(sql);
                pst.setString(1, username.getText());
                int rs = pst.executeUpdate();
                System.out.println(rs + " staff registered");
            }
            //query 6
            for (Email email:emailData) {
                sql = ("insert into user_email values (?, ?);");
                pst = conn.prepareStatement(sql);
                pst.setString(1, username.getText());
                pst.setString(2, email.getEmail());
                int rs = pst.executeUpdate();
                System.out.println(rs + "email added ");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Registration input Information");
            alert.setContentText("Employee Visitor registration success!");
            alert.showAndWait();

            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/User_Login.fxml"));
            primaryStage.setScene(new Scene(root));

        }catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }


    }
    public void addAnEmail(ActionEvent event) {
        if (emailData.size() >= 5) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Email input Warning");
            alert.setContentText("Cannot have more than 5 emails!");
            alert.showAndWait();
        } else if (!checkerFunction.verifyInputEmail(emailField.getText())) {
            return;
        } else if (emailsFromDB.contains(emailField.getText())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Email input Warning");
            alert.setContentText("The email is being used by someone\n" +
                    "Try another one!");
            alert.showAndWait();
        } else {
            emailData.add(new Email(emailField.getText()));
            emailsFromDB.add(emailField.getText());
        }
    }
    private void loadEmails() {
        emailsFromDB = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select email from user_email;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    emailsFromDB.add(rs.getString("email"));
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
    }
    private void fillEmails() {

        buttonCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        buttonCol.setCellFactory(param -> new TableCell<Email, Email>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Email email, boolean empty) {
                super.updateItem(email, empty);

                if (email == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    if (emailData.size() == 1) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Cannot delete the email!");
                        alert.setContentText("At least one email account " +
                                "should exist!");
                        alert.showAndWait();
                        return;
                    } else {
                        emailsFromDB.remove(email.getEmail());
                        emailData.remove(email);
                        System.out.println(emailData.size());
                        return;
                    }
                });
            }
        });
        emailData = FXCollections.observableArrayList();
        System.out.println(emailData.size());
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailTable.setItems(emailData);
    }
    private boolean isFieldsEmpty() {
        if (firstName.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Should fill out First Name field!");
            alert.showAndWait();
            return false;
        } else if (lastName.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Should fill out Last Name field!");
            alert.showAndWait();
            return false;
        } else if (username.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Should fill out username field!");
            alert.showAndWait();
            return false;
        } else if (password.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Should fill out password field!");
            alert.showAndWait();
            return false;
        } else if (confirmPassword.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Should fill out confirm password field!");
            alert.showAndWait();
            return false;
        } else if (address.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Should fill out address field!");
            alert.showAndWait();
            return false;
        } else if (!checkerFunction.verifyZip(zipcode.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Zipcode should be 5 digit numbers!");
            alert.showAndWait();
            return false;
        } else if (city.getText().trim().equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Should fill out city field!");
            alert.showAndWait();
            return false;
        } else if (emailData.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Should add at least one email!");
            alert.showAndWait();
            return false;
        } else if (!confirmPassword.getText().equals(password.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("confirm password and password don't match!");
            alert.showAndWait();
            return false;
        } else if (password.getText().length() < 8) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("password should be at least 8 characters!");
            alert.showAndWait();
            return false;
        } else if (!checkerFunction.validatePhone(phone.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Phone Number Should follow the format: " +
                    "###-###-####");
            alert.showAndWait();
            return false;
        } else if (firstName.getText().length() > 20) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("First Name should be less than 20 " +
                    "characters");
            alert.showAndWait();
            return false;
        } else if (lastName.getText().length() > 20) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Last Name should be less than 20 " +
                    "characters");
            alert.showAndWait();
            return false;
        } else if (username.getText().length() > 20) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Username should be less than 20 " +
                    "characters");
            alert.showAndWait();
            return false;
        } else if (password.getText().length() > 20) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Password should be less than 20 " +
                    "characters");
            alert.showAndWait();
            return false;
        } else if (confirmPassword.getText().length() > 20) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Confirm password should be less than 20 " +
                    "characters");
            alert.showAndWait();
            return false;
        } else if (address.getText().length() > 20) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Address should be less than 20 " +
                    "characters");
            alert.showAndWait();
            return false;
        } else if (city.getText().length() > 20) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("City should be less than 20 " +
                    "characters");
            alert.showAndWait();
            return false;
        }
        return true;
    }
    private boolean checkUsernameExists() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query
                // sql statements
                String sql = ("select count(username) as cnt from user where " +
                        "username=?");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, username.getText());
                ResultSet rs = pst.executeQuery();
                rs.next();
                int cnt = rs.getInt("cnt");
                if (cnt >= 1) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Field input Warning");
                    alert.setContentText("The username is being used by " +
                            "someone.\n Try another username!");
                    alert.showAndWait();
                    return false;
                }
                return true;



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
        return false;
    }
    private void fillComboBox() {
        EmployeeTypeForRegister[] eTypes = EmployeeTypeForRegister.class
                .getEnumConstants();
        empTypes.getItems().addAll(eTypes);
        empTypes.getSelectionModel().selectFirst();
        StateType[] sTypes = StateType.class.getEnumConstants();
        stateTypes.getItems().addAll(sTypes);
        stateTypes.getSelectionModel().selectFirst();
    }
    private boolean checkPhoneExists() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query
                // sql statements
                String sql = ("select count(phone) as cnt from employee where" +
                        " phone=?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setLong(1, checkerFunction.deFormatAndInt(phone.getText()));
                ResultSet resultSet = pst.executeQuery();
                resultSet.next();
                int cnt = resultSet.getInt("cnt");
                if (cnt >= 1) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Field input Warning");
                    alert.setContentText("The phone number is being used by " +
                            "someone.\n Try another one!");
                    alert.showAndWait();
                    return false;
                }
                return true;


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
        return false;
    }
}
