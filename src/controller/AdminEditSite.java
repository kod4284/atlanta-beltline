package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.*;
import model.DB;
import model.ManagerUsernameForCombo;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminEditSite implements Initializable {

    @FXML TextField name;
    @FXML TextField zipcode;
    @FXML TextField address;
    @FXML ComboBox<ManagerUsernameForCombo> manager;
    @FXML CheckBox openEveryday;
    public static String siteName;
    public static String username;
    public static String managerName;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillData();
    }
    private void fillData() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements
                ArrayList<ManagerUsernameForCombo> managers = new ArrayList<>();
                //if no row return, go to catch
                String sql = ("select concat(firstname, ' ', lastname) as 'Manager', t1.username from\n" +
                        "(select manager_username as username from site where manager_username=?\n" +
                        "union\n" +
                        "select username from employee where username not in (select manager_username from site) and employee_type='Manager' ) t1\n" +
                        "join\n" +
                        "(select username, firstname, lastname from user where status='Approved') t2\n" +
                        "on t1.username=t2.username;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, username);
                ResultSet rs = pst.executeQuery();

                ManagerUsernameForCombo dataToBeShown = null;
                while (rs.next()) {
                    ManagerUsernameForCombo temp = new ManagerUsernameForCombo(rs.getString
                            ("Manager"), rs.getString
                            ("username"));
                    if (temp.getUsername().equals(username)) {
                        dataToBeShown = temp;
                    }
                    managers.add(temp);
                }
                //dataToBeShown = new ManagerUsernameForCombo(managerName, username);

                manager.getItems().addAll(managers);
                manager.getSelectionModel().select(dataToBeShown);

                sql = ("select site_name, site_zipcode, site_address, " +
                        "manager_username, open_everyday from site where site_name=?;");
                pst = conn.prepareStatement(sql);
                pst.setString(1, siteName);
                rs = pst.executeQuery();
                rs.next();
                name.setText(rs.getString("site_name"));
                zipcode.setText(rs.getString("site_zipcode"));
                address.setText(rs.getString("site_address"));
                if (rs.getString("open_everyday").equals("Yes")) {
                    openEveryday.setSelected(true);
                } else {
                    openEveryday.setSelected(false);
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
    }
    @FXML
    public void btnActionAdminEditSiteBack(ActionEvent event) {
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
    public void btnActionAdminEditSiteUpdate(ActionEvent event) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements
                ArrayList<String> managers = new ArrayList<>();
                //if no row return, go to catch
                String sql = ("update site set site_name=?, \n" +
                        "site_zipcode=?, site_address=?, manager_username=?,\n" +
                        "open_everyday=?\n" +
                        "where site_name = ?\n");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name.getText());
                pst.setInt(2,Integer.parseInt(zipcode.getText()));
                pst.setString(3, address.getText());
                pst.setString(4, username);
                if (openEveryday.isSelected()) {
                    pst.setString(5, "Yes");
                } else {
                    pst.setString(5, "No");
                }
                pst.setString(6, siteName);


                int rs = pst.executeUpdate();
                System.out.println(rs + "rows updated");

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Update success!");
                alert.setContentText("The information is Updated " +
                        "successfully!");
                alert.showAndWait();

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
    }
}
