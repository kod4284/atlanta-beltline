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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.DB;
import model.Email;
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

public class RegisterUser implements Initializable {
    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML TextField phone;
    @FXML TextField username;
    @FXML Label employee_id;
    @FXML GridPane emailsPane;
    @FXML Button addBtn;
    @FXML TextField emailField;
    @FXML TableView  emailTable;
    @FXML TableColumn<Email, Email> buttonCol;
    @FXML TableColumn<Email, String> emailCol;
    @FXML PasswordField password;
    @FXML PasswordField confirmPassword;
    private ObservableList<Email> emailData;
    private List<String> emailsFromDB;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

    /**
     * This is a method to get an action from Register button
     * @param event Action from Register button
     */
    @FXML
    public void btnActionRegisterUser(ActionEvent event) {
        isFieldsEmpty();

        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/User_Login.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }

    }
    private void fillEmails(List<String> emails) {

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
        for (String email: emails) {
            emailData.add(new Email(email));
        }
        System.out.println(emailData.size());
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailTable.setItems(emailData);
    }
    public void addAnEmail(ActionEvent event) {
        if (emailData.size() > 5) {
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
        } else if (confirmPassword.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Should fill out confirm password field!");
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
        }
        return true;
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

}
