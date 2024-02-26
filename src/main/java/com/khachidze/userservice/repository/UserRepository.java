package com.khachidze.userservice.repository;


import com.khachidze.userservice.util.EntityManagerProvider;
import com.khachidze.userservice.util.TransactionalOperation;
import com.khachidze.userservice.entity.UserEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class UserRepository {

    public UserEntity findById(Long id) {
        return executeInTransaction(entityManager -> entityManager.find(UserEntity.class, id));
    }

    public List<UserEntity> findAll() {
        return executeInTransaction(entityManager -> {
            Query query = entityManager.createQuery("SELECT u FROM UserEntity u", UserEntity.class);
            return query.getResultList();
        });
    }

    public Optional<UserEntity> findByPhoneNumber(String phoneNumber) {
        try {
            return executeInTransaction(entityManager -> {
                Query query = entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.phoneNumber = :phone");
                query.setParameter("phone", phoneNumber);
                UserEntity userEntity = (UserEntity) query.getSingleResult();
                return  Optional.of(userEntity);
            });
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void update(UserEntity userEntity) {
        executeInTransaction(entityManager -> {
            entityManager.merge(userEntity);
            return null;
        });
    }

    public void save(UserEntity userEntity) {
        executeInTransaction(entityManager -> {
            entityManager.persist(userEntity);
            return null;
        });
    }

    public void updateBalance(UserEntity userEntity, UserEntity userEntity2) {
        executeInTransaction(entityManager -> {
            entityManager.merge(userEntity);
            entityManager.merge(userEntity2);
            return null;
        });
    }

    public void delete(UserEntity userEntity) {
        executeInTransaction(entityManager -> {
            entityManager.remove(userEntity);
            return null;
        });
    }

    private <T> T executeInTransaction(TransactionalOperation<T> operation) {
        EntityManager entityManager = EntityManagerProvider.createEntityManager();
        try {
            return operation.executeTransactionally(entityManager);
        } finally {
            entityManager.close();
        }
    }

}
