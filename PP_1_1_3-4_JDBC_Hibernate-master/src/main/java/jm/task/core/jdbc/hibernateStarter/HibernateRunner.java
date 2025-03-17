package jm.task.core.jdbc.hibernateStarter;

import org.hibernate.cfg.Configuration;

public class HibernateRunner {
    public static void main(String[] args) {
        Configuration cfg = new Configuration();
        cfg.configure();
        try (var sessionFactory = cfg.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            System.out.println("OK");
        }
    }
}
