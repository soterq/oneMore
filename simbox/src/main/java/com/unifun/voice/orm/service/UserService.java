package com.unifun.voice.orm.service;

import com.unifun.voice.orm.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;;import java.util.List;

public class UserService {
    private SessionFactory sessionFactory;
    private Session session;

    UserService() {
        sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        session = sessionFactory.openSession();
    }

    private void initSession() {
        session.beginTransaction();
    }

    private void closeSession() {
        session.getTransaction().commit();
        session.close();
    }

    public void addUser(User user) {
        initSession();
        session.save(user);
        closeSession();
    }

    public List<User> getAllUsers() {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT u from u", User.class)
                .getResultList();
    }


    public void remove(User user) {

    }

    public void checkForExistingUser(User user) {

    }

//    public boolean validateUser(User user) {
//        for (user :
//             getAllUsers()) {
//    };


}

