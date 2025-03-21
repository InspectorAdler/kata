package jm.task.core.jdbc;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {

        UserService userService = new UserServiceImpl();

        userService.saveUser("vova", "putin", 62);
        userService.saveUser("donald", "tramp", 70);
        userService.saveUser("vova2", "zelya", 42);

        System.out.println("Состав базы данных:");
        for (User user : userService.getAllUsers()) {
            System.out.println(user);
        }

        userService.removeUserById(3);


    }
}
