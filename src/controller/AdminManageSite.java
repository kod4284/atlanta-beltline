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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminManageSite implements Initializable {
    @FXML ComboBox<String> site;
    @FXML ComboBox<ManagerForNum19> manager;
    @FXML ComboBox<OpenEveryday> openEveryday;
    @FXML TableView tableView;
    @FXML TableColumn<AdminManageSiteData,AdminManageSiteData> nameCol;
    @FXML TableColumn<AdminManageSiteData,AdminManageSiteData> managerCol;
    @FXML TableColumn<AdminManageSiteData,AdminManageSiteData> openEverydayCol;
    ToggleGroup group;
    private int colIndex;
    private ObservableList<AdminManageSiteData> tableData;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        group = new ToggleGroup();
        fillComboBox();
    }

    private void fillComboBox() {
        OpenEveryday[] open = OpenEveryday.class.getEnumConstants();
        openEveryday.getItems().addAll(open);
        openEveryday.getSelectionModel().selectFirst();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements
                ArrayList<String> sites = new ArrayList<>();
                sites.add("-- ALL --");
                //if no row return, go to catch
                String sql = ("select site_name from site;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    sites.add(rs.getString("site_name"));
                }
                site.getItems().addAll(sites);
                site.getSelectionModel().selectFirst();

                ArrayList<ManagerForNum19> managers = new ArrayList<>();
                managers.add(new ManagerForNum19("-- ALL --", "-- ALL --"));
                sql = ("select concat(firstname, ' ', lastname) as Name, manager.username from manager join user on manager.username=user.username;");
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
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
    // ALL for 4
    private void ALL_ALL_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select site_name, concat(user.firstname,' ', user.lastname) as Manager, username, open_everyday \n" +
                        "from site join user on user.username=site.manager_username\n" +
                        "#where site_name='Atlanta Beltline Center' #Site " +
                        "filter\n" +
                        "#and username='manager3' #Manager filter\n" +
                        "#and open_everyday='No' #Open Everyday filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageSiteData(new
                            SimpleStringProperty(rs.getString("site_name")),
                            new SimpleStringProperty(rs.getString("Manager")),
                            new SimpleStringProperty(rs.getString
                                    ("open_everyday")), new
                            SimpleStringProperty(rs.getString("username"))));
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
    private void ALL_ALL_DATA() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select site_name, concat(user.firstname,' ', user.lastname) as Manager, username, open_everyday \n" +
                        "from site join user on user.username=site.manager_username\n" +
                        "#where site_name='Atlanta Beltline Center' #Site " +
                        "filter\n" +
                        "where username=? #Manager filter\n" +
                        "#and open_everyday='No' #Open Everyday filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,manager.getValue().getUsername());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageSiteData(new
                            SimpleStringProperty(rs.getString("site_name")),
                            new SimpleStringProperty(rs.getString("Manager")),
                            new SimpleStringProperty(rs.getString
                                    ("open_everyday")), new
                            SimpleStringProperty(rs.getString("username"))));
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
    private void ALL_DATA_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select site_name, concat(user.firstname,' ', user.lastname) as Manager, username, open_everyday \n" +
                        "from site join user on user.username=site.manager_username\n" +
                        "where site_name=? #Site " +
                        "filter\n" +
                        "#where username=? #Manager filter\n" +
                        "#and open_everyday='No' #Open Everyday filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,site.getValue());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageSiteData(new
                            SimpleStringProperty(rs.getString("site_name")),
                            new SimpleStringProperty(rs.getString("Manager")),
                            new SimpleStringProperty(rs.getString
                                    ("open_everyday")), new
                            SimpleStringProperty(rs.getString("username"))));
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
    private void ALL_DATA_DATA() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select site_name, concat(user.firstname,' ', user.lastname) as Manager, username, open_everyday \n" +
                        "from site join user on user.username=site.manager_username\n" +
                        "where site_name=? #Site " +
                        "filter\n" +
                        "and username=? #Manager filter\n" +
                        "#and open_everyday='No' #Open Everyday filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,site.getValue());
                pst.setString(2,manager.getValue().getUsername());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageSiteData(new
                            SimpleStringProperty(rs.getString("site_name")),
                            new SimpleStringProperty(rs.getString("Manager")),
                            new SimpleStringProperty(rs.getString
                                    ("open_everyday")), new
                            SimpleStringProperty(rs.getString("username"))));
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

    // YES for 4
    private void YES_ALL_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select site_name, concat(user.firstname,' ', user.lastname) as Manager, username, open_everyday \n" +
                        "from site join user on user.username=site.manager_username\n" +
                        "#where site_name=? #Site " +
                        "filter\n" +
                        "#and username=? #Manager filter\n" +
                        "where open_everyday='Yes' #Open Everyday filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageSiteData(new
                            SimpleStringProperty(rs.getString("site_name")),
                            new SimpleStringProperty(rs.getString("Manager")),
                            new SimpleStringProperty(rs.getString
                                    ("open_everyday")), new
                            SimpleStringProperty(rs.getString("username"))));
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
    private void YES_ALL_DATA() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select site_name, concat(user.firstname,' ', user.lastname) as Manager, username, open_everyday \n" +
                        "from site join user on user.username=site.manager_username\n" +
                        "#where site_name='Atlanta Beltline Center' #Site " +
                        "filter\n" +
                        "where username=? #Manager filter\n" +
                        "and open_everyday='Yes' #Open Everyday filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,manager.getValue().getUsername());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageSiteData(new
                            SimpleStringProperty(rs.getString("site_name")),
                            new SimpleStringProperty(rs.getString("Manager")),
                            new SimpleStringProperty(rs.getString
                                    ("open_everyday")), new
                            SimpleStringProperty(rs.getString("username"))));
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
    private void YES_DATA_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select site_name, concat(user.firstname,' ', user.lastname) as Manager, username, open_everyday \n" +
                        "from site join user on user.username=site.manager_username\n" +
                        "where site_name=? #Site " +
                        "filter\n" +
                        "#where username=? #Manager filter\n" +
                        "and open_everyday='Yes' #Open Everyday filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,site.getValue());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageSiteData(new
                            SimpleStringProperty(rs.getString("site_name")),
                            new SimpleStringProperty(rs.getString("Manager")),
                            new SimpleStringProperty(rs.getString
                                    ("open_everyday")), new
                            SimpleStringProperty(rs.getString("username"))));
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
    private void YES_DATA_DATA() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select site_name, concat(user.firstname,' ', user.lastname) as Manager, username, open_everyday \n" +
                        "from site join user on user.username=site.manager_username\n" +
                        "where site_name=? #Site " +
                        "filter\n" +
                        "and username=? #Manager filter\n" +
                        "and open_everyday='Yes' #Open Everyday filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,site.getValue());
                pst.setString(2,manager.getValue().getUsername());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageSiteData(new
                            SimpleStringProperty(rs.getString("site_name")),
                            new SimpleStringProperty(rs.getString("Manager")),
                            new SimpleStringProperty(rs.getString
                                    ("open_everyday")), new
                            SimpleStringProperty(rs.getString("username"))));
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

    // NO for 4

    private void NO_ALL_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select site_name, concat(user.firstname,' ', user.lastname) as Manager, username, open_everyday \n" +
                        "from site join user on user.username=site.manager_username\n" +
                        "#where site_name=? #Site " +
                        "filter\n" +
                        "#and username=? #Manager filter\n" +
                        "where open_everyday='No' #Open Everyday filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageSiteData(new
                            SimpleStringProperty(rs.getString("site_name")),
                            new SimpleStringProperty(rs.getString("Manager")),
                            new SimpleStringProperty(rs.getString
                                    ("open_everyday")), new
                            SimpleStringProperty(rs.getString("username"))));
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
    private void NO_ALL_DATA() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select site_name, concat(user.firstname,' ', user.lastname) as Manager, username, open_everyday \n" +
                        "from site join user on user.username=site.manager_username\n" +
                        "#where site_name='Atlanta Beltline Center' #Site " +
                        "filter\n" +
                        "where username=? #Manager filter\n" +
                        "and open_everyday='No' #Open Everyday filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,manager.getValue().getUsername());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageSiteData(new
                            SimpleStringProperty(rs.getString("site_name")),
                            new SimpleStringProperty(rs.getString("Manager")),
                            new SimpleStringProperty(rs.getString
                                    ("open_everyday")), new
                            SimpleStringProperty(rs.getString("username"))));
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
    private void NO_DATA_ALL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select site_name, concat(user.firstname,' ', user.lastname) as Manager, username, open_everyday \n" +
                        "from site join user on user.username=site.manager_username\n" +
                        "where site_name=? #Site " +
                        "filter\n" +
                        "#where username=? #Manager filter\n" +
                        "and open_everyday='No' #Open Everyday filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,site.getValue());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageSiteData(new
                            SimpleStringProperty(rs.getString("site_name")),
                            new SimpleStringProperty(rs.getString("Manager")),
                            new SimpleStringProperty(rs.getString
                                    ("open_everyday")), new
                            SimpleStringProperty(rs.getString("username"))));
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
    private void NO_DATA_DATA() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select site_name, concat(user.firstname,' ', user.lastname) as Manager, username, open_everyday \n" +
                        "from site join user on user.username=site.manager_username\n" +
                        "where site_name=? #Site " +
                        "filter\n" +
                        "and username=? #Manager filter\n" +
                        "and open_everyday='No' #Open Everyday filter");
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,site.getValue());
                pst.setString(2,manager.getValue().getUsername());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    tableData.add(new AdminManageSiteData(new
                            SimpleStringProperty(rs.getString("site_name")),
                            new SimpleStringProperty(rs.getString("Manager")),
                            new SimpleStringProperty(rs.getString
                                    ("open_everyday")), new
                            SimpleStringProperty(rs.getString("username"))));
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

    private void filter() {
        tableData = FXCollections.observableArrayList();
        if (openEveryday.getValue().toString().equals("-- ALL --")) {
            System.out.println("ALL");
            if (site.getValue().equals("-- ALL --") && manager.getValue().toString()
                    .equals
                    ("-- ALL --")) {
                ALL_ALL_ALL();
            } else if (!site.getValue().equals("-- ALL --") && manager
                    .getValue().toString()
                    .equals
                            ("-- ALL --")) {
                ALL_DATA_ALL();
            } else if (site.getValue().equals("-- ALL --") && !manager
                    .getValue().toString()
                    .equals
                            ("-- ALL --")) {
                ALL_ALL_DATA();
            } else {
                ALL_DATA_DATA();
            }
        } else if (openEveryday.getValue().toString().equals(OpenEveryday.YES
                .toString())) {
            System.out.println("YES");
            if (site.getValue().equals("-- ALL --") && manager.getValue().toString()
                    .equals
                            ("-- ALL --")) {
                YES_ALL_ALL();
            } else if (!site.getValue().equals("-- ALL --") && manager
                    .getValue().toString()
                    .equals
                            ("-- ALL --")) {
                YES_DATA_ALL();
            } else if (site.getValue().equals("-- ALL --") && !manager
                    .getValue().toString()
                    .equals("-- ALL --")) {
                YES_ALL_DATA();
            } else {
                YES_DATA_DATA();
            }
        } else {
            System.out.println("NO");
            if (site.getValue().equals("-- ALL --") && manager.getValue().toString()
                    .equals
                            ("-- ALL --")) {
                NO_ALL_ALL();
            } else if (!site.getValue().equals("-- ALL --") && manager.getValue().toString()
                    .equals
                            ("-- ALL --")) {
                NO_DATA_ALL();
            } else if (site.getValue().equals("-- ALL --") && !manager.getValue().toString()
                    .equals
                            ("-- ALL --")) {
                NO_ALL_DATA();
            } else {
                NO_DATA_DATA();
            }
        }
        nameCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>
                (param.getValue()));
        nameCol.setCellFactory(param -> new TableCell<AdminManageSiteData,
                AdminManageSiteData>() {
            @Override
            public void updateItem(AdminManageSiteData obj, boolean
                    empty) {
                super.updateItem(obj, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    RadioButton radioButton = new RadioButton(obj.getName());
                    radioButton.setToggleGroup(group);
                    // Add Listeners if any
                    setGraphic(radioButton);

                    radioButton.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent arg0) {
                            if (radioButton.isSelected()) {
                                colIndex = getIndex();

                            }

                        }
                    });
                }
            }
            //private RadioButton radioButton = new RadioButton();


        });
        managerCol.setCellValueFactory(new PropertyValueFactory<>
                ("manager"));
        openEverydayCol.setCellValueFactory(new PropertyValueFactory<>
                ("openEveryday"));

        tableView.setItems(tableData);
    }
    @FXML
    public void btnActionAdminManageSiteFilter(ActionEvent event) {
        filter();
    }

    @FXML
    public void btnActionAdminManageSiteDelete(ActionEvent event) {

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
    @FXML
    public void btnActionAdminManageUserCreate(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Administrator_Create_Site.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }

    @FXML
    public void btnActionAdminManageUserEdit(ActionEvent event) {
        AdminManageSiteData item = (AdminManageSiteData) tableView.getItems()
                .get(colIndex);
        AdminEditSite.siteName = item.getName();
        AdminEditSite.username = item.getUsername();
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/Administrator_Edit_Site.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load User_Login.fxml");
        }
    }

}
