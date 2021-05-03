package com.bradyrussell.data;

import com.bradyrussell.data.objects.Kingdom;
import com.bradyrussell.data.objects.Player;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
 
public class DatabaseUtil {
    public static final String DatabaseConnectionString = "jdbc:sqlite:kingdom";
    public static final String TestDatabaseConnectionString = "jdbc:sqlite:kingdom_test";

    private static final SessionFactory productionSessionFactory = buildSessionFactory(DatabaseConnectionString);
    private static final SessionFactory testSessionFactory = buildSessionFactory(TestDatabaseConnectionString);
 
    private static SessionFactory buildSessionFactory(String ConnectionString) {
        try {
            Configuration configuration = new Configuration()
                    .addAnnotatedClass(Player.class)
                    .addAnnotatedClass(Kingdom.class)
                    .setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC")
                    .setProperty("hibernate.connection.url", ConnectionString)
                    .setProperty("hibernate.dialect", "org.sqlite.hibernate.dialect.SQLiteDialect")
                    .setProperty("hibernate.show_sql", "true")
                    .setProperty("hibernate.hdm2ddl.auto", "validate");

            return configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 
    public static SessionFactory getProductionSessionFactory() {
        return productionSessionFactory;
    }
    public static SessionFactory getTestSessionFactory() {
        return testSessionFactory;
    }
}