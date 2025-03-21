package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    void createUsersTable() throws SQLException;

    void dropUsersTable() throws SQLException;

    void saveUser(String name, String lastName, int age);

    boolean removeUserById(long id);

    List<User> getAllUsers() throws SQLException;

    void cleanUsersTable();
}
