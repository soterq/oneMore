package com.unifun.voice.orm.repository;

import com.unifun.voice.helpers.Constants;
import com.unifun.voice.jwt.TokenManager;
import com.unifun.voice.orm.model.SessionTable;
import lombok.NoArgsConstructor;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@NoArgsConstructor
public class SessionTableRepository {
    @Inject
    private EntityManager entityManager;

    @Transactional
    public void addSession(SessionTable sessionTable) {
        entityManager.persist(sessionTable);
    }

    @Transactional
    public void removeSession(SessionTable sessionTable) {
        entityManager.remove(sessionTable);
    }

    @Transactional
    public List<SessionTable> getSessions() throws Exception {
        Query query = entityManager.createQuery("SELECT s from SessionTable as s");
        return query.getResultList();
    }

    @Transactional
    public List<String> getTokens() throws Exception {
        Query query = entityManager.createQuery("SELECT token FROM SessionTable");
        return query.getResultList();
    }

    @Transactional
    public String getIPByToken(String token) {
        Query query = entityManager.createQuery("SELECT ipaddr FROM SessionTable WHERE token=" + token);
        return query.getSingleResult().toString();
    }

    public void removeSessionByToken(String token) {
        try {
            for (String tkn : getTokens()) {
                if(tkn == token) {
                    removeSession(getNewSession(getIPByToken(token), token));
                }
            }
        } catch(Exception e) {

        }
    }

    public SessionTable getNewSession(String ipAddr, String token) {
        SessionTable st = new SessionTable();
        st.setIpAddr(ipAddr);
        st.setToken(token);
        //id is auto generated
        return st;
    }

    public boolean checkIfTokenIsAlreadyInDB(String token) {
        try {
            for (String temp : getTokens()) {
                if(token == temp) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public void checkForExpiredTokensAndRemoveThem() {
        TokenManager tokenManager = new TokenManager();
        try {
            for(String token: getTokens()) {
                if(tokenManager.verifyIfTokenIsValid(token, Constants.SECRET_KEY)) {

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
