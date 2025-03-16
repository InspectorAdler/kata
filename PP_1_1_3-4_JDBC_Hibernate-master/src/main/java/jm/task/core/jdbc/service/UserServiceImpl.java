package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    public void createUsersTable() throws SQLException {
        UserDao userDao = new UserDaoJDBCImpl();
        userDao.createUsersTable();
    }

    public void dropUsersTable() throws SQLException {
        UserDao userDao = new UserDaoJDBCImpl();
        userDao.dropUsersTable();
    }

    public void saveUser(String name, String lastName, int age) {
        UserDao userDao = new UserDaoJDBCImpl();
        userDao.saveUser(name, lastName, age);
    }

    public void removeUserById(long id) {
        UserDao userDao = new UserDaoJDBCImpl();
        userDao.removeUserById(id);
    }

    public List<User> getAllUsers() throws SQLException {
        UserDao userDao = new UserDaoJDBCImpl();
        return userDao.getAllUsers();
    }

    public void cleanUsersTable() {
        UserDao userDao = new UserDaoJDBCImpl();
        userDao.cleanUsersTable();
    }
}
