package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeManageProfile implements Initializable {
    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML TextField phone;
    @FXML Label username;
    @FXML Label employee_id;
    @FXML Label siteName;
    @FXML Label address;
    @FXML CheckBox isVisitor;
    @FXML GridPane emailsPane;
    @FXML Button addBtn;
    @FXML TextField emailField;
    @FXML TableView  emailTable;
    @FXML TableColumn<Email, Email> buttonCol;
    @FXML TableColumn<Email, String> emailCol;
    private ObservableList<Email> emailData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                // sql statements
                //query1
                String sql = ("select firstname, lastname, username from user" +
                        " where username=?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                ResultSet rs = pst.executeQuery();
                rs.next();

                Session.user.setFirstName(rs.getString("firstname"));
                Session.user.setLastName(rs.getString("lastname"));
                //query2
                if (Session.user.isManager()) {
                    sql = ("select site_name from site where " +
                            "manager_username=?; ");
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, Session.user.getUsername());
                    rs = pst.executeQuery();
                    rs.next();
                    Session.user.setSiteName(rs.getString("site_name"));
                }
                //query3
                sql = ("select employee_id, phone, employee_address, employee_city," +
                        " employee_state, employee_zipcode from " +
                        "employee where username=?;");
                pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                rs = pst.executeQuery();
                rs.next();
                Session.user.setEmployeeId(rs.getString("employee_id"));
                Session.user.setPhone(rs.getString("phone"));
                Session.user.setEmployeeAddress(rs.getString
                        ("employee_address"));
                Session.user.setCity(rs.getString("employee_city"));
                Session.user.setState(rs.getString("employee_state"));
                Session.user.setZipcode(rs.getString("employee_zipcode"));

                //query4

                sql = ("select email from user_email where username=?;");
                pst = conn.prepareStatement(sql);
                pst.setString(1, Session.user.getUsername());
                rs = pst.executeQuery();
                while (rs.next()) {
                    Session.user.getEmail().clear(); // 초기화 꼭 해줘야함!
                    Session.user.getEmail().add(rs.getString("email"));
                }

                fillEmails(Session.user.getEmail());
                System.out.println(Session.user.getEmail().size());
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

        showInfo();


    }
    public void btnActionBack(ActionEvent event) {
        try {
            //Visitor
            if (Session.user.isVisitor()) {
                //Manager-Visitor
                if (Session.user.isManager()) {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view/Manager_Visitor_Functionality.fxml"));
                    primaryStage.setScene(new Scene(root));
                //Staff-Visitor
                } else if (Session.user.isStaff()) {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view/Staff_Visitor_Functionality" +
                                    ".fxml"));
                    primaryStage.setScene(new Scene(root));
                //Admin-Visitor
                } else {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view" +
                                    "/Administrator_Visitor_Functionality" +
                                    ".fxml"));
                    primaryStage.setScene(new Scene(root));
                }
            //Employee Only
            } else {
                //Manager Only
                if (Session.user.isManager()) {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view/Manager_Functionality_Only.fxml"));
                    primaryStage.setScene(new Scene(root));
                //Staff Only
                } else if (Session.user.isStaff()) {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view/Staff_Functionality_Only" +
                                    ".fxml"));
                    primaryStage.setScene(new Scene(root));
                //Administrator Only
                } else {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                            .getWindow();
                    Parent root = FXMLLoader.load(getClass()
                            .getResource("../view" +
                                    "/Administrator_Functionality_Only" +
                                    ".fxml"));
                    primaryStage.setScene(new Scene(root));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load Manager_Functionality_Only.fxml");

        }
    }
    public void btnActionUpdate(ActionEvent event) {

    }
    private void showInfo() {
        firstName.setText(Session.user.getFirstName());
        lastName.setText(Session.user.getLastName());
        phone.setText(Session.user.getPhone());
        username.setText(Session.user.getUsername());
        employee_id.setText(Session.user.getEmployeeId());
        address.setText(Session.user.getEmployeeAddress() +", "+ Session.user
                .getCity() + ", " + Session.user.getState() + " " + Session
                .user
                .getZipcode());
        siteName.setText(Session.user.getSiteName());
        isVisitor.setSelected(Session.user.isVisitor());

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
    private boolean addAnEmail() {
        checkerFunction.verifyInputEmail(emailField.getText());
        return true;

    }
}
