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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegisterUser implements Initializable {
    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML TextField username;

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
    private List<String> usernames;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEmails();
        loadUsernames();
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

    /**
     * This is a method to get an action from Register button
     * @param event Action from Register button
     */
    @FXML
    public void btnActionRegisterUser(ActionEvent event) {
        if (!isFieldsEmpty()) {
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
            String sql = ("insert into user values (?, ?, 'Pending'," +
                    " ?, ?, 'User');");
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username.getText());
            pst.setString(2, SHA256.encrypt(password.getText()));
            pst.setString(3, firstName.getText());
            pst.setString(4, lastName.getText());
            int rs = pst.executeUpdate();
            System.out.println(rs + "User registered");

            //query2
            for (Email email:emailData) {
                sql = ("insert into user_email values (?, ?);");
                pst = conn.prepareStatement(sql);
                pst.setString(1, username.getText());
                pst.setString(2, email.getEmail());
                rs = pst.executeUpdate();
                System.out.println(rs + "email added ");
            }


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
        /*} else if (!checkerFunction.validatePhone(phone.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Field input Warning");
            alert.setContentText("Phone Number Should follow the format: " +
                    "###-###-####");
            alert.showAndWait();
            return false; */
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
    private void loadUsernames() {
        usernames = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query
                // sql statements
                String sql = ("select username from user;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    usernames.add(rs.getString("username"));
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
