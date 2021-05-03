package com.bradyrussell.data;

import com.bradyrussell.data.objects.Kingdom;
import com.bradyrussell.data.objects.Player;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
 
public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();
 
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration()
                    .addAnnotatedClass(Player.class)
                    .addAnnotatedClass(Kingdom.class)
                    .setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC")
                    .setProperty("hibernate.connection.url", "jdbc:sqlite:test.sqlite")
                    .setProperty("hibernate.dialect", "org.sqlite.hibernate.dialect.SQLiteDialect")
                    .setProperty("hibernate.show_sql", "true")
                    .setProperty("hibernate.hdm2ddl.auto", "validate");
            configuration.configure();

            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
 
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}