package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.ResourceBundle;

public class AdminManageUser implements Initializable {
    @FXML TextField username;
    @FXML ComboBox<UserStatus> status;
    @FXML ComboBox<UserTypeForFilter> type;
    @FXML TableColumn<AdminManageUserData, AdminManageUserData> usernameCol;
    @FXML TableColumn<AdminManageUserData, AdminManageUserData> emailCountCol;
    @FXML TableColumn<AdminManageUserData, AdminManageUserData> userTypeCol;
    @FXML TableColumn<AdminManageUserData, AdminManageUserData> statusCol;
    @FXML TableView tableView;
    @FXML ToggleGroup group;
    private int colIndex;
    boolean flag;
    private ObservableList<AdminManageUserData> userData;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillComboBox();
        group = new ToggleGroup();
        flag = false;
    }

    private void fillComboBox() {
        UserStatus[] userStatuses = UserStatus.class.getEnumConstants();
        status.getItems().addAll(userStatuses);
        status.getSelectionModel().selectFirst();
        UserTypeForFilter[] userTypes = UserTypeForFilter.class.getEnumConstants();
        type.getItems().addAll(userTypes);
        type.getSelectionModel().selectFirst();

    }
    private void Empty_ALL_ALL() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.username, email_count, user_type, status from\n" +
                        "(select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from user left outer join employee on user.username=employee.username \n" +
                        "where user_type in ('User', 'Visitor')\n" +
                        "union\n" +
                        "select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from employee right outer join user on user.username=employee.username \n" +
                        "where employee_type<>'Admin') as t1\n" +
                        "join\n" +
                        "(select username, count(email) as email_count from user_email where username not in (select username from employee where employee_type='Admin') group by username) as t2\n" +
                        "on t1.username=t2.username\n" +
                        "#where t1.username='user1' #Username filter\n" +
                        "#and user_type='User' #Type filter\n" +
                        "#and status='Pending' #Status filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    userData.add(new AdminManageUserData(new
                            SimpleStringProperty(rs.getString("username")),
                            rs.getInt("email_count"), new
                            SimpleStringProperty(rs.getString
                            ("user_type")),
                            new SimpleStringProperty(rs.getString("status"))));
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
    private void Empty_NOT_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.username, email_count, user_type, status from\n" +
                        "(select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from user left outer join employee on user.username=employee.username \n" +
                        "where user_type in ('User', 'Visitor')\n" +
                        "union\n" +
                        "select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from employee right outer join user on user.username=employee.username \n" +
                        "where employee_type<>'Admin') as t1\n" +
                        "join\n" +
                        "(select username, count(email) as email_count from user_email where username not in (select username from employee where employee_type='Admin') group by username) as t2\n" +
                        "on t1.username=t2.username\n" +
                        "#where t1.username='user1' #Username filter\n" +
                        "where user_type=? #Type filter\n" +
                        "#and status='Pending' #Status filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, type.getValue().toString());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    userData.add(new AdminManageUserData(new
                            SimpleStringProperty(rs.getString("username")),
                            rs.getInt("email_count"), new
                            SimpleStringProperty(rs.getString
                            ("user_type")),
                            new SimpleStringProperty(rs.getString("status"))));
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
    private void Empty_ALL_NOT() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.username, email_count, user_type, status from\n" +
                        "(select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from user left outer join employee on user.username=employee.username \n" +
                        "where user_type in ('User', 'Visitor')\n" +
                        "union\n" +
                        "select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from employee right outer join user on user.username=employee.username \n" +
                        "where employee_type<>'Admin') as t1\n" +
                        "join\n" +
                        "(select username, count(email) as email_count from user_email where username not in (select username from employee where employee_type='Admin') group by username) as t2\n" +
                        "on t1.username=t2.username\n" +
                        "#where t1.username='user1' #Username filter\n" +
                        "#and user_type=? #Type filter\n" +
                        "where status=? #Status filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, status.getValue().toString());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    userData.add(new AdminManageUserData(new
                            SimpleStringProperty(rs.getString("username")),
                            rs.getInt("email_count"), new
                            SimpleStringProperty(rs.getString
                            ("user_type")),
                            new SimpleStringProperty(rs.getString("status"))));
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
    private void Empty_NOT_NOT() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.username, email_count, user_type, status from\n" +
                        "(select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from user left outer join employee on user.username=employee.username \n" +
                        "where user_type in ('User', 'Visitor')\n" +
                        "union\n" +
                        "select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from employee right outer join user on user.username=employee.username \n" +
                        "where employee_type<>'Admin') as t1\n" +
                        "join\n" +
                        "(select username, count(email) as email_count from user_email where username not in (select username from employee where employee_type='Admin') group by username) as t2\n" +
                        "on t1.username=t2.username\n" +
                        "#where t1.username='user1' #Username filter\n" +
                        "where user_type=? #Type filter\n" +
                        "and status=? #Status filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, type.getValue().toString());
                pst.setString(2, status.getValue().toString());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    userData.add(new AdminManageUserData(new
                            SimpleStringProperty(rs.getString("username")),
                            rs.getInt("email_count"), new
                            SimpleStringProperty(rs.getString
                            ("user_type")),
                            new SimpleStringProperty(rs.getString("status"))));
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
    private void Data_ALL_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.username, email_count, user_type, status from\n" +
                        "(select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from user left outer join employee on user.username=employee.username \n" +
                        "where user_type in ('User', 'Visitor')\n" +
                        "union\n" +
                        "select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from employee right outer join user on user.username=employee.username \n" +
                        "where employee_type<>'Admin') as t1\n" +
                        "join\n" +
                        "(select username, count(email) as email_count from user_email where username not in (select username from employee where employee_type='Admin') group by username) as t2\n" +
                        "on t1.username=t2.username\n" +
                        "where t1.username=? #Username filter\n" +
                        "#and user_type='User' #Type filter\n" +
                        "#and status='Pending' #Status filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, username.getText());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    userData.add(new AdminManageUserData(new
                            SimpleStringProperty(rs.getString("username")),
                            rs.getInt("email_count"), new
                            SimpleStringProperty(rs.getString
                            ("user_type")),
                            new SimpleStringProperty(rs.getString("status"))));
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
    private void Data_NOT_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.username, email_count, user_type, status from\n" +
                        "(select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from user left outer join employee on user.username=employee.username \n" +
                        "where user_type in ('User', 'Visitor')\n" +
                        "union\n" +
                        "select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from employee right outer join user on user.username=employee.username \n" +
                        "where employee_type<>'Admin') as t1\n" +
                        "join\n" +
                        "(select username, count(email) as email_count from user_email where username not in (select username from employee where employee_type='Admin') group by username) as t2\n" +
                        "on t1.username=t2.username\n" +
                        "where t1.username=? #Username filter\n" +
                        "and user_type=? #Type filter\n" +
                        "#and status='Pending' #Status filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, username.getText());
                pst.setString(2, type.getValue().toString());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    userData.add(new AdminManageUserData(new
                            SimpleStringProperty(rs.getString("username")),
                            rs.getInt("email_count"), new
                            SimpleStringProperty(rs.getString
                            ("user_type")),
                            new SimpleStringProperty(rs.getString("status"))));
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
    private void Data_ALL_NOT() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.username, email_count, user_type, status from\n" +
                        "(select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from user left outer join employee on user.username=employee.username \n" +
                        "where user_type in ('User', 'Visitor')\n" +
                        "union\n" +
                        "select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from employee right outer join user on user.username=employee.username \n" +
                        "where employee_type<>'Admin') as t1\n" +
                        "join\n" +
                        "(select username, count(email) as email_count from user_email where username not in (select username from employee where employee_type='Admin') group by username) as t2\n" +
                        "on t1.username=t2.username\n" +
                        "where t1.username=? #Username filter\n" +
                        "#and user_type=? #Type filter\n" +
                        "and status=? #Status filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, username.getText());
                pst.setString(2, status.getValue().toString());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    userData.add(new AdminManageUserData(new
                            SimpleStringProperty(rs.getString("username")),
                            rs.getInt("email_count"), new
                            SimpleStringProperty(rs.getString
                            ("user_type")),
                            new SimpleStringProperty(rs.getString("status"))));
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
    private void Data_NOT_NOT() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select t1.username, email_count, user_type, status from\n" +
                        "(select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from user left outer join employee on user.username=employee.username \n" +
                        "where user_type in ('User', 'Visitor')\n" +
                        "union\n" +
                        "select user.username, if(user_type in ('Employee', 'Employee, Visitor'), employee_type, user_type) as user_type, status\n" +
                        "from employee right outer join user on user.username=employee.username \n" +
                        "where employee_type<>'Admin') as t1\n" +
                        "join\n" +
                        "(select username, count(email) as email_count from user_email where username not in (select username from employee where employee_type='Admin') group by username) as t2\n" +
                        "on t1.username=t2.username\n" +
                        "where t1.username=? #Username filter\n" +
                        "and user_type=? #Type filter\n" +
                        "and status=? #Status filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, username.getText());
                pst.setString(2, type.getValue().toString());
                pst.setString(3, status.getValue().toString());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    userData.add(new AdminManageUserData(new
                            SimpleStringProperty(rs.getString("username")),
                            rs.getInt("email_count"), new
                            SimpleStringProperty(rs.getString
                            ("user_type")),
                            new SimpleStringProperty(rs.getString("status"))));
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
    @FXML
    public void btnActionAdminManageUserFilter(ActionEvent event) {
        filter();
    }
    private void filter() {
        userData = FXCollections.observableArrayList();
        if (username.getText().trim().equals("")) {
            System.out.println("empty");
            if (type.getValue().toString().equals("-- ALL --") &&
                    status.getValue().toString().equals("-- ALL --")) {
                System.out.println("empty-ALL-ALL");
                Empty_ALL_ALL();
            } else if (!type.getValue().toString().equals("-- ALL --") &&
                    status.getValue().toString().equals("-- ALL --")) {
                Empty_NOT_ALL();
            } else if (type.getValue().toString().equals("-- ALL --") &&
                    !status.getValue().toString().equals("-- ALL --")) {
                Empty_ALL_NOT();
            } else {
                System.out.println("empty-not-not");
                Empty_NOT_NOT();
            }

        } else {
            System.out.println("Data");
            if (type.getValue().toString().equals("-- ALL --") &&
                    status.getValue().toString().equals("-- ALL --")) {
                Data_ALL_ALL();
            } else if (!type.getValue().toString().equals("-- ALL --") &&
                    status.getValue().toString().equals("-- ALL --")) {
                Data_NOT_ALL();
            } else if (type.getValue().toString().equals("-- ALL --") &&
                    !status.getValue().toString().equals("-- ALL --")) {
                Data_ALL_NOT();
            } else {
                Data_NOT_NOT();
            }

        }

        usernameCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        usernameCol.setCellFactory(param -> new TableCell<AdminManageUserData, AdminManageUserData>() {
            @Override
            public void updateItem(AdminManageUserData obj, boolean
                    empty) {
                super.updateItem(obj, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    RadioButton radioButton = new RadioButton(obj.getUsername());
                    radioButton.setToggleGroup(group);
                    // Add Listeners if any
                    setGraphic(radioButton);

                    radioButton.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent arg0) {
                            if (radioButton.isSelected()) {
                                colIndex = getIndex();
                                flag = true;
                            }

                        }
                    });
                }
            }
            //private RadioButton radioButton = new RadioButton();


        });
        emailCountCol.setCellValueFactory(new PropertyValueFactory<>
                ("emailCount"));
        userTypeCol.setCellValueFactory(new PropertyValueFactory<>
                ("userType"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>
                ("status"));

        tableView.setItems(userData);
        flag = false;
    }
    @FXML
    public void btnActionAdminManageUserApprove(ActionEvent event) {
        if (flag == false) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Radio button selection Warning");
            alert.setContentText("You should select a item on the list!");
            alert.showAndWait();
            return;
        }
        AdminManageUserData item = (AdminManageUserData) tableView.getItems()
                .get(colIndex);
        System.out.println(item.getStatus().equals(UserStatus.DECLINED.toString()));
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("update user set status='Approved' where " +
                        "username=?;");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, item.getUsername());
                int rs = pst.executeUpdate();
                System.out.println(rs + " rows inserted");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Field input Information");
                alert.setContentText("The data successfully inserted!");
                alert.showAndWait();

            } catch(SQLIntegrityConstraintViolationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Duplicate Input");
                alert.setContentText("You cannot input because of duplicate " +
                        "value!");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        filter();
    }

    @FXML
    public void btnActionAdminManageUserDecline(ActionEvent event) {
        if (flag == false) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Radio button selection Warning");
            alert.setContentText("You should select a item on the list!");
            alert.showAndWait();
            return;
        }
        AdminManageUserData item = (AdminManageUserData) tableView.getItems()
                .get(colIndex);
        System.out.println(item.getStatus().equals(UserStatus.APPROVED.toString()));
        if(item.getStatus().equals(UserStatus.APPROVED.toString())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Update Error");
            alert.setContentText("You cannot decline approved account!");
            alert.showAndWait();
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
                String sql = ("update user set status='Declined' where " +
                        "username=? and user_Type<>'Approved'");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, item.getUsername());
                int rs = pst.executeUpdate();
                System.out.println(rs + " rows updated");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Field input Information");
                alert.setContentText("The data successfully inserted!");
                alert.showAndWait();

            } catch(SQLIntegrityConstraintViolationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Duplicate Input");
                alert.setContentText("You cannot input because of duplicate " +
                        "value!");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        filter();
    }

    @FXML
    public void btnActionAdminManageUserBack(ActionEvent event) {
        try {
            //Administrator Visitor
            if (Session.user.isEmployeeVisitor()) {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                        .getWindow();
                Parent root = FXMLLoader.load(getClass()
                        .getResource("../view/Administrator_Visitor_Functionality" +
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

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }

}
