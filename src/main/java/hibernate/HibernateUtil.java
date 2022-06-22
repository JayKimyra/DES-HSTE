package hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tutorial.spring.models.*;

import java.util.Properties;

@Component
public class HibernateUtil {
    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.postgresql.Driver");
               /* settings.put(Environment.URL, "jdbc:postgresql://localhost:5432/db_test");
                settings.put(Environment.USER, "postgres");
                settings.put(Environment.PASS, "JKimyra619");*/
                settings.put(Environment.URL, "jdbc:postgresql://ec2-63-32-248-14.eu-west-1.compute.amazonaws.com:5432/d3jq6h5v43gc31");
                settings.put(Environment.USER, "dhxjeiwrpxqbns");
                settings.put(Environment.PASS, "9accd8f0588e7620dd49f96fa0a6356e6beba24d00f2894ec57df4637b779188");


                settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");

                settings.put(Environment.SHOW_SQL, "true");

                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                settings.put(Environment.HBM2DDL_AUTO, "create");
                settings.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, "true");
                configuration.setProperties(settings);

                configuration.addAnnotatedClass(Problem.class);
                configuration.addAnnotatedClass(Variant.class);
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(TeacherStudent.class);
                configuration.addAnnotatedClass(Solve.class);
                configuration.addAnnotatedClass(Homework.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
