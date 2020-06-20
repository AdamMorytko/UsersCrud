package pl.coderslab.workshop;

import java.sql.*;

public class DBUtil {

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/products_ex?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8", "root", "");
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return conn;
    }

    public static Connection connect(String dbName) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName + "?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8", "root", "");
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return conn;
    }

    public static int insert(Connection conn, String sql, String... params) {
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            return statement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return -1;
        }
    }

    public static void printData(Connection conn, String query, String... columnNames) {
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery();) {
            while (resultSet.next()) {
                for (String param : columnNames) {
                    System.out.println(resultSet.getString(param));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printData(Connection conn, String query, String[] params, String[] columns) {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            ResultSet resultSet = statement.executeQuery();
            for (int i = 0; i < columns.length; i++){
                if (i != 0) {
                    System.out.print(";");
                }
                System.out.print(columns[i]);
            }
            System.out.println();
            while (resultSet.next()) {
                for (int i = 0; i < columns.length; i++) {
                    String value = resultSet.getString(columns[i]);
                    if (i != 0) {
                        System.out.print(";");
                    }
                    System.out.print(value);
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}