package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;


import javafx.scene.control.*;
import javafx.scene.control.TextField;
import model.DB;
import model.ManagerForNum19;
import model.checkerFunction;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminCreateSite implements Initializable {

    @FXML TextField name;
    @FXML TextField address;
    @FXML TextField zipcode;
    @FXML ComboBox<ManagerForNum19> manager;
    @FXML CheckBox openEveryday;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillComboBox();
    }
    private void fillComboBox() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                ArrayList<ManagerForNum19> managers = new ArrayList<>();
                String sql = ("select concat(firstname, ' ', lastname) as Name, manager.username from manager join user on manager.username=user.username \n" +
                        "where manager.username not in (select manager_username from site) and user.status='Approved';");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    managers.add(new ManagerForNum19(rs.getString("Name"),rs
                            .getString("username")));
                }
                manager.getItems().addAll(managers);
                manager.getSelectionModel().selectFirst();
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
    public void btnActionAdminCreateSiteBack(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Administrator_Manage_Site.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }

    @FXML
    public void btnActionAdminCreateSiteCreate(ActionEvent event) {
        if (name.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Empty Field Error");
            alert.setContentText("You should input name of site!");
            alert.showAndWait();
            return;
        }
        if (!checkerFunction.verifyZip(zipcode.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Zipcode digits Error");
            alert.setContentText("The zipcode should be 5 digits!");
            alert.showAndWait();
            return;
        }
        if (checkSiteExists()) {
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
                String sql = ("insert into site values (?, ?, ?, ?, ?);");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name.getText());
                pst.setString(2, address.getText());
                pst.setInt(3, Integer.parseInt(zipcode.getText()));
                String temp;
                if (openEveryday.isSelected()) {
                    temp = "Yes";
                } else {
                    temp = "No";
                }
                pst.setString(4, temp);
                pst.setString(5, manager.getValue().getUsername());
                int rs = pst.executeUpdate();
                System.out.println(rs + "rows inserted!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Insert Confirmation");
                alert.setContentText("Create successfully!");
                alert.showAndWait();

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
/*
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Administrator_Manage_Site.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }*/
    }
    private boolean checkSiteExists() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements
                //if no row return, go to catch
                String sql = ("select count(site_name) as cnt from site where" +
                        " site_name=?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,name.getText());
                ResultSet rs = pst.executeQuery();
                rs.next();
                if(rs.getInt("cnt") >= 1) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Duplication Error");
                    alert.setContentText("The site name already exists!");
                    alert.showAndWait();
                    return true;
                }
            } catch (SQLException e) {
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
