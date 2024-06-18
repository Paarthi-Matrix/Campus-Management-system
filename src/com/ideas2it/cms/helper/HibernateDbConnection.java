package src.com.ideas2it.cms.helper;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import src.com.ideas2it.cms.customexception.HibernateDbConnectionException;

/**
 * <p>
 *
 * The `HibernateDbConnection` class is a singleton that manages the Hibernate-
 * SessionFactory. It ensures that only one instance of the SessionFactory is created
 * and provides global access to it. The class also provides a method to shut down the
 * SessionFactory.
 *
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

    @SuppressWarnings("deprecation")
    private HibernateDbConnection() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
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
