package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class SortingFunctions {
    public static List userTakeTransitAscColOne(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_route asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List userTakeTransitDescColOne(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_route asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List userTakeTransitAscColtwo(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_type asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List userTakeTransitDescColtwo(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_type asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List userTakeTransitAsColThree(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_price asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List userTakeTransitDecColThree(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_price asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List userTakeTransitAscColFour(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_price asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List AdminManageSiteAscOne(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_price asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List AdminManageSiteDescOne(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_price asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List AdminManageTransitOneDesc(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_price asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List AdminManageTransitColThree(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_price asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List ManagerManageStaffDescOne(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_price asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List ManagerManageStaffAscColThree(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_price asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List ManagerSiteReportAscColOne(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_price asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List ManagerSiteReportDescColOne(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_price asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List StaffViewScheduleAsc(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_price asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }
    public static List StaffViewScheduleDesc(List list) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create a connection to the database
            Connection conn = DriverManager.getConnection(DB.url, DB.user, DB
                    .password);

            try {
                //query

                // sql statements

                //if no row return, go to catch
                String sql = ("select distinct t1.transit_route, t1.transit_type, transit_price, site_number from\n" +
                        "(select transit_route, transit_type, transit_price\n" +
                        "from connect natural join transit\n" +
                        "where site_name='Piedmont Park' #Contain Site filter\n" +
                        "and transit_type='MARTA' #Transport Type filter\n" +
                        "and transit_price between 1 and 10 #Price Range filter\n" +
                        ") t1\n" +
                        "join\n" +
                        "(select transit_route, transit_type, count(*) as site_number\n" +
                        "from connect natural join transit\n" +
                        "group by transit_route, transit_type) t2\n" +
                        "on t1.transit_route=t2.transit_route and t1.transit_type=t2.transit_type\n" +
                        "order by transit_price asc;");
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("transit_route"));
                    list.add(rs.getString("transit_type"));
                    list.add(rs.getString("transit_price"));
                    list.add(rs.getString("site_number"));
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
        return list;
    }

}
