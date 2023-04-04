import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Load configuration from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure();

            // Create a SessionFactory based on the configuration
            return configuration.buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Error creating SessionFactory: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }
}