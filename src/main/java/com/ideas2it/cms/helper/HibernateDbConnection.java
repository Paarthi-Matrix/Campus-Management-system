package com.ideas2it.cms.helper;

import com.ideas2it.cms.customexception.HibernateDbConnectionException;

import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * <p>
 * The `HibernateDbConnection` class is a singleton that manages the Hibernate-
 * SessionFactory. It ensures that only one instance of the SessionFactory is created
 * and provides global access to it. The class also provides a method to shut down the
 * SessionFactory.
 * </p>
 */
public class HibernateDbConnection {

    private static HibernateDbConnection instance;
    private SessionFactory sessionFactory;

    /**
     * <p>
     * Private constructor for singleton implementation. Initializes the SessionFactory.
     * </p>
     *
     * @throws HibernateDbConnectionException if an error occurs while configuring SessionFactory.
     */
    private HibernateDbConnection() {
        try {
            Dotenv dotenv = Dotenv.configure().directory("./src/main/resources").load();
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.driver_class", dotenv.get("DB_DRIVER_CONNECTOR"));
            configuration.setProperty("hibernate.connection.url", dotenv.get("DB_URL"));
            configuration.setProperty("hibernate.connection.username", dotenv.get("DB_USERNAME"));
            configuration.setProperty("hibernate.connection.password", dotenv.get("DB_PASSWORD"));
            configuration.configure("hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();

        } catch (Throwable e) {
            String errorMessage = "Error occurred while configuring SessionFactory. Please try again!";
            throw new HibernateDbConnectionException(errorMessage, e);
        }
    }

    /**
     * <p>
     * Provides the singleton instance of `HibernateDbConnection`. Ensures thread safety.
     * </p>
     *
     * @return HibernateDbConnection 
     *         The singleton instance.
     */
    public static synchronized HibernateDbConnection getInstance() {
        if (null == instance) {
            instance = new HibernateDbConnection();
        }
        return instance;
    }

    /**
     * <p>
     * Provides the SessionFactory instance.
     * </p>
     *
     * @return SessionFactory 
     *         The Hibernate SessionFactory.
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * <p>
     * Shuts down the SessionFactory.
     * </p>
     */
    public static void shutdown() {
        getInstance().getSessionFactory().close();
    }
}