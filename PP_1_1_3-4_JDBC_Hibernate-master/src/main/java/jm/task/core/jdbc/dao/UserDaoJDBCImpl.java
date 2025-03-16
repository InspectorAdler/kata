package jm.task.core.jdbc.dao;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final static String SAVE_SQL = """
                    INSERT INTO new_schema.table_user (name, last_name, age)
                    VALUES (?, ?, ?);
                    """;

    private final static String DELETE_SQL = """
                    DELETE FROM new_schema.table_user WHERE id = ?;
                    """;

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() throws SQLException {
        String schemaName = "new_schema";
        String tableName = "table_user";
        String checkTableSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ? AND table_name = ?";
        String sql =
                "CREATE TABLE " + schemaName + "." + tableName + " (" +
                        " id INT AUTO_INCREMENT," +
                        " name VARCHAR(100) NOT NULL," +
                        " last_name VARCHAR(100) NOT NULL," +
                        " age INT NOT NULL," +
                        " PRIMARY KEY (id));";

        try (Connection conn = Util.getConnection();
             PreparedStatement checkTable = conn.prepareStatement(checkTableSql);
             Statement stmt = conn.createStatement()) {

            checkTable.setString(1, schemaName);
            checkTable.setString(2, tableName);
            ResultSet rs = checkTable.executeQuery();
            if (rs.next() && rs.getInt(1) == 1) {
                System.out.println("Таблица " + tableName + " уже существует.");
            } else {
                stmt.executeUpdate(sql);
                System.out.println("Таблица " + tableName + " создана.");
            }
        } catch (SQLException e) {
            System.out.println("Создание таблицы юзеров отклонено.");
            throw new SQLException(e);
        }
    }

    public void dropUsersTable() throws SQLException {
        String schemaName = "new_schema";
        String tableName = "table_user";
        String sql = "DROP TABLE " + schemaName + "." + tableName;

        try (Connection conn = Util.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Таблица " + tableName + " удалена.");
        } catch (SQLException e) {
            System.out.println("Удалить таблицу не удалось.");
            throw new SQLException(e);
        }
    }

    public void saveUser(String name, String lastName, int age) {
        try(var connection = Util.getConnection();
            var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setInt(3, age);

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    System.out.println("Порядковый номер юзера: " + keys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeUserById(long id) {
        try (var connection = Util.getConnection();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            System.out.println("Учетная запись юзера удалена.");
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (var connection = Util.getConnection();
             Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM new_schema.table_user")) {
            while (results.next()) {
                Long id = results.getLong("id");
                String name = results.getString("name");
                String lastName = results.getString("last_name");
                byte age = results.getByte("age");

                User user = new User(name, lastName, age);
                user.setId(id);
                users.add(user);
            }
        }
        return users;
    }

    public void cleanUsersTable() {
        String schemaName = "new_schema";
        String tableName = "table_user";
        String sql = "TRUNCATE TABLE " + schemaName + "." + tableName;

        try (Connection conn = Util.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Таблица " + schemaName + "." + tableName + " успешно очищена.");
        } catch (SQLException e) {
            System.out.println("Очистить таблицу не удалось: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
