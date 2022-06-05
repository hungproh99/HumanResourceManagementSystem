package com.csproject.hrm.common.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
  private static SessionFactory sessionFactory = null;

  public static SessionFactory getSesstionFactory() {
    if (sessionFactory == null) {
      sessionFactory = new Configuration().configure().buildSessionFactory();
    }
    return sessionFactory;
  }
}
