package com.social.app.repository;

import com.social.app.model.PasswordReset;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PasswordResetDAOimpl implements PasswordResetDAO {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    public PasswordResetDAOimpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public PasswordReset  findByEmail(String email) {

        TypedQuery<PasswordReset> query = entityManager.createQuery("from PasswordReset where email=:data", PasswordReset.class);
        query.setParameter("data",email);
        PasswordReset passwordReset = null;

        try {
            passwordReset = query.getSingleResult();

        } catch(NoResultException e) {
            return passwordReset = null;
        }

        return passwordReset;
    }
    @Transactional
    @Override
    public void save(PasswordReset passwordReset) {
        entityManager.persist(passwordReset);
    }
    @Transactional
    @Override
    public void delete(PasswordReset passwordReset) {
        entityManager.remove(passwordReset);
    }

    @Override
    public Boolean isExist(String email) {
        TypedQuery<PasswordReset> query = entityManager.createQuery("from PasswordReset where email=:data", PasswordReset.class);
        query.setParameter("data",email);
        PasswordReset passwordReset = null;
//ném ra ngoại lệ nếu không tìm thấy email
        try {
            passwordReset = query.getSingleResult();

        } catch (NoResultException e) {
            passwordReset = null;
        }
if (passwordReset != null) {
            return  true;
        } else {
    return  false;
        }
    }

}