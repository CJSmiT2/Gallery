package ua.org.smit.gallery.config;

import org.hibernate.SessionFactory;

public class ConfigTest {
    
    public static SessionFactory sessionFactory = new org.hibernate.cfg.Configuration()
            .configure().buildSessionFactory();
}
