package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;

import static jm.task.core.jdbc.dao.UserDaoJDBCImpl.*;
import static jm.task.core.jdbc.util.Util.SCHEMA_NAME;
import static jm.task.core.jdbc.util.Util.TABLE_NAME;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = Util.openSession()) {
            Transaction transaction = session.beginTransaction();
            NativeQuery<?> query = session.createNativeQuery(CREATE_USERS_TABLE);
            query.executeUpdate();
            transaction.commit();
            System.out.printf("Таблица %s создана.%n", TABLE_NAME);
        } catch (HibernateException e) {
            System.out.println("ERROR");
        }
    }

    @Override
    public void dropUsersTable() {
        try(Session session = Util.openSession()) {
            Transaction transaction = session.beginTransaction();
            NativeQuery<?> query = session.createNativeQuery(DROP_TABLE);
            query.executeUpdate();
            transaction.commit();
            System.out.printf("Таблица %s удалена.%n", TABLE_NAME);
        } catch (HibernateException e) {
            System.out.println("ERROR");
        }
    }

    @Override
    public void saveUser(String name, String lastName, int age) {
        System.out.printf("Создание юзера %s %s %d./n", name, lastName, age);
        try (Session session = Util.openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.persist(user);
            transaction.commit();
            System.out.printf("Юзер -%s- был создан.%n", user.getName());
        } catch (HibernateException e){
            System.out.println("ERROR");
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.remove(user);
            transaction.commit();
            System.out.printf("Юзер с порядковым номером - %d - был удален из базы данных.%n", id);
        } catch (HibernateException e) {
            System.out.println("ERROR");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = null;
        try (Session session = Util.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<User> query = session.createQuery("from User", User.class);
            list = query.list();
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println("ERROR");
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.openSession()) {
            Transaction transaction = session.beginTransaction();
            NativeQuery<?> query = session.createNativeQuery(TRUNCATE_TABLE);
            query.executeUpdate();
            transaction.commit();
            System.out.printf("Таблица %s.%s успешно очищена.%n", SCHEMA_NAME, TABLE_NAME);
        } catch (HibernateException e) {
            System.out.println("Очистить таблицу не удалось: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

/*
Прежде чем выполнить запрос к базе, нужно создать отдельную сессию.
Если запросы связанные, то они могут выполняться в одной сессии.
Если запросы не связанные (и между ними может пройти несколько минут),
то для них нужно делать свои собственные сессии.

Если ты хочешь прочитать данные из базы или выполнить сложный запрос,
то нужно сначала создать объект Query и с помощью него выполнить свой запрос.

Также каждый запрос к базе выполняется в своей собственной транзакции.
Ее нужно открыть, выполнить нужные операции, и затем закрыть (закоммитить).

В версиях Hibernate, начиная с 6.0, введено явное разделение на два типа запросов:

-----
session.createQuery() используется для запросов на выборку данных (SELECT queries).
Ты можешь использовать его для получения сущностей, списков сущностей или
для выполнения запросов, которые возвращают проекции, например, части сущностей
или агрегированные значения.

session.createMutationQuery() используется для запросов, которые изменяют
данные (так называемые мутационные операции, включающие INSERT, UPDATE, DELETE).
Этот метод предназначен для создания запросов, которые непосредственно изменяют
состояние данных в базе, не предназначены для выборки данных и могут привести
к сайд-эффектам в базе данных.
------

 */