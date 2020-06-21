package pl.coderslab.workshop;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import pl.coderslab.workshop.entity.UserDao;

import java.sql.Connection;

public class Main01 {
    public static void main(String[] args) throws MySQLIntegrityConstraintViolationException {
        Connection connection = DBUtil.connect();

        UserDao userDao = new UserDao();

        User newUser = new User();
        newUser.setUserName("Alusia");
        newUser.setEmail("husalicja@gmail.com");
        newUser.setPassword("twojastara");
        userDao.create(newUser);
        userDao.findAll();


    }
}
