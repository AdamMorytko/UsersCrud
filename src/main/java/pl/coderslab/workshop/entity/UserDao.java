package pl.coderslab.workshop.entity;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.workshop.DBUtil;
import pl.coderslab.workshop.User;


import java.sql.*;
import java.util.Arrays;

public class UserDao {
    private static final String CREATE_USER_QUERY = "INSERT INTO users(username,email,password) VALUES (?,?,?)";
    private static final String READ_USER_QUERY = "SELECT * FROM users where id = ?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?, email = ?, password = ? where id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String CHECK_IF_EMAIL_EXISTS = "SELECT EXISTS(SELECT * FROM users WHERE email='?')";

    //    public User create(User user) throws MySQLIntegrityConstraintViolationException {
//        Connection conn = DBUtil.connect();
//        short emailExists = 0;
//        try (PreparedStatement statement = conn.prepareStatement(CHECK_IF_EMAIL_EXISTS);
//             ResultSet resultSet = statement.executeQuery();) {
//            if (resultSet.next()) {
//                if (resultSet.getString(1).equalsIgnoreCase(user.getEmail())) {
//                    emailExists = 1;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        if (emailExists == 0) {
//            user.setId(DBUtil.insert(conn, CREATE_USER_QUERY, user.getUserName(), user.getEmail(), user.getPassword()));
//        } else {
//            System.out.println("There is already a user with a given email in database!");
//        }
//        return user;
//    }
    public User create(User user) {
        try (Connection conn = DBUtil.connect()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, this.hashPassword(user.getPassword()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(int userId) {
        try (Connection conn = DBUtil.connect()) {
            PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(User user) {
        try (Connection conn = DBUtil.connect()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, this.hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String hashPassword(String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashed;
    }

    public void delete(int userId) {
        try (Connection conn = DBUtil.connect()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User[] findAll() {
        try (Connection conn = DBUtil.connect()) {
            User[] users = new User[0];
            PreparedStatement statement = conn.prepareStatement(FIND_ALL_USERS_QUERY);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                users = addToArray(user, users);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private User[] addToArray(User user, User[] users) {
        users = Arrays.copyOf(users,users.length+1);
        users[users.length-1] = user;
        return users;
    }
}
