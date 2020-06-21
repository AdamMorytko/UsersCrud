package pl.coderslab.workshop;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import java.sql.*;

public class DBUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/workshop2?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "coderslab";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return conn;
    }

    public static int insert(Connection conn, String sql, String... params) throws MySQLIntegrityConstraintViolationException {
        try (PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Inserted ID: " + id);
                return id;
            }else return 0;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return 0;
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
            for (int i = 0; i < columns.length; i++) {
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