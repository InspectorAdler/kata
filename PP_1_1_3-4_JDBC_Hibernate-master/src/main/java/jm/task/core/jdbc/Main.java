package jm.task.core.jdbc;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {

        UserService userService = new UserServiceImpl();
        System.out.println("Создание таблицы юзеров:");
        userService.createUsersTable();
        System.out.println("Повторное создание таблицы юзеров: ");
        userService.createUsersTable();

        System.out.println("Создание юзеров:");
        userService.saveUser("vova", "putin", 62);
        userService.saveUser("donald", "tramp", 70);
        userService.saveUser("vova2", "zelya", 42);

        System.out.println("Состав базы данных:");
        for (User user : userService.getAllUsers()) {
            System.out.println(user);
        }

        System.out.println("Удаление юзера по id:");
        userService.removeUserById(3);

        userService.cleanUsersTable();

        userService.dropUsersTable();

    }
}
