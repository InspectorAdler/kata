package jm.task.core.jdbc.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.*;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static  final String SCHEMA_NAME = "new_schema";
    public static  final String TABLE_NAME = "table_user";

    private final static Connection INSTANCE;

    static {
        try {
            INSTANCE = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return INSTANCE;
    }



    private static final SessionFactory sessionFactory = buildSessionFactory();

    public static SessionFactory buildSessionFactory() {
        SessionFactory config;
        try {
            config = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Первоначальное создание SessionFactory не удалось." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        return config;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }

    public static void closeSession(Session session) {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

}
