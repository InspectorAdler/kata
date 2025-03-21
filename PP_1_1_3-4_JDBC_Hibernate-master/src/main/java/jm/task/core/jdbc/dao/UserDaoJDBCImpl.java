package jm.task.core.jdbc.dao;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.SCHEMA_NAME;
import static jm.task.core.jdbc.util.Util.TABLE_NAME;

public class UserDaoJDBCImpl implements UserDao {

    public final static String CREATE_USERS_TABLE = String.format("""
             CREATE TABLE %s.%s (
             id INT AUTO_INCREMENT,
             name VARCHAR(100) NOT NULL,
             last_name VARCHAR(100) NOT NULL,
             age INT NOT NULL,
             PRIMARY KEY (id));""", SCHEMA_NAME, TABLE_NAME);

    private final static String CHECK_TABLE = """
            SELECT COUNT(*)
            FROM information_schema.tables
            WHERE table_schema = ? AND table_name = ?""";

    public final static String SAVE_SQL = """
            INSERT INTO new_schema.table_user
                        (name, last_name, age)
            VALUES (?, ?, ?);""";

    private final static String DELETE_SQL = """
            DELETE FROM new_schema.table_user\s
            WHERE id = ?;""";

    public final static String DROP_TABLE = String.format("DROP TABLE %s.%s;", SCHEMA_NAME, TABLE_NAME);

    public static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %s.%s;", SCHEMA_NAME, TABLE_NAME);

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() throws SQLException {
        try (Connection conn = Util.getConnection();
             PreparedStatement checkTable = conn.prepareStatement(CHECK_TABLE);
             Statement stmt = conn.createStatement()) {

            checkTable.setString(1, SCHEMA_NAME);
            checkTable.setString(2, TABLE_NAME);
            ResultSet rs = checkTable.executeQuery();
            if (rs.next() && rs.getInt(1) == 1) {
                System.out.printf("Таблица %s уже существует.%n", TABLE_NAME);
            } else {
                stmt.executeUpdate(CREATE_USERS_TABLE);
                System.out.printf("Таблица %s создана.%n", TABLE_NAME);
            }
        } catch (SQLException e) {
            System.out.println("Создание таблицы юзеров отклонено.");
            throw e;
        }
    }

    public void dropUsersTable() throws SQLException {
        try (Connection conn = Util.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(DROP_TABLE);
            System.out.printf("Таблица %s удалена.%n", TABLE_NAME);
        } catch (SQLException e) {
            System.out.println("Удалить таблицу не удалось.");
            throw new SQLException(e);
        }
    }

    public void saveUser(String name, String lastName, int age) {
        try (var connection = Util.getConnection();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    System.out.println("Порядковый номер юзера: " + keys.getInt(1));
                } else {
                    throw new SQLException("Создание пользователя не удалось, идентификатор не получен.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            if (statement.executeUpdate() > 0) {
                System.out.printf("Учетная запись юзера c id = %d была удалена.%n", id);
            } else {
                System.out.printf("Учетной запись юзера c id = %d не существует.%n", id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (var connection = Util.getConnection();
             ResultSet results = connection.createStatement().executeQuery("SELECT * FROM new_schema.table_user")) {
            while (results.next()) {
                 User user = new User();
                 user.setId(results.getLong("id"));
                 user.setName(results.getString("name"));
                 user.setLastName(results.getString("last_name"));
                 user.setAge(results.getInt("age"));
                 users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Connection conn = Util.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(TRUNCATE_TABLE);
            System.out.printf("Таблица %s.%s успешно очищена.%n", SCHEMA_NAME, TABLE_NAME);
        } catch (SQLException e) {
            System.out.println("Очистить таблицу не удалось: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
