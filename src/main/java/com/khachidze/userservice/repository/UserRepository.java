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
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@ApplicationScoped
public class UserRepository {

    private final Map<String, Optional<UserEntity>> userCache = new ConcurrentHashMap<>();

    public Optional<UserEntity> findByPhoneNumber(String phoneNumber) {

        Optional<UserEntity> resultCache = userCache.get(phoneNumber);

        if (resultCache != null) {
            return resultCache;
        }

        try {
            return executeInTransaction(entityManager -> {
                Query query = entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.phoneNumber = :phone");
                query.setParameter("phone", phoneNumber);
                UserEntity userEntity = (UserEntity) query.getSingleResult();
                userCache.put(phoneNumber, Optional.of(userEntity));
                return Optional.of(userEntity);
            });
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<UserEntity> findAll() {
        return executeInTransaction(entityManager -> {
            Query query = entityManager.createQuery("SELECT u FROM UserEntity u", UserEntity.class);
            return query.getResultList();
        });
    }

    public void save(UserEntity userEntity) {
        executeInTransaction(entityManager -> {
            entityManager.persist(userEntity);
            userCache.put(userEntity.getPhoneNumber(), Optional.of(userEntity));
            return null;
        });
    }

    public void updateBalance(UserEntity userEntity, UserEntity userEntity2) {
        executeInTransaction(entityManager -> {
            entityManager.merge(userEntity);
            entityManager.merge(userEntity2);
            userCache.put(userEntity.getPhoneNumber(), Optional.of(userEntity));
            userCache.put(userEntity2.getPhoneNumber(), Optional.of(userEntity2));
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
