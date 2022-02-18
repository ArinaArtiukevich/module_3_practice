package com.esm.epam.repository.impl;

import com.esm.epam.entity.Order;
import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;
import com.esm.epam.exception.DaoException;
import com.esm.epam.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_FIELD_USERS;
import static com.esm.epam.util.ParameterAttribute.ORDER_FIELD_PRICE;
import static com.esm.epam.util.ParameterAttribute.ORDER_FIELD_USER_ID;
import static com.esm.epam.util.ParameterAttribute.TAG_FIELD_CERTIFICATES;
import static com.esm.epam.util.ParameterAttribute.TAG_FIELD_ID;

@Repository


public class UserDaoImpl implements UserDao {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate, EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Optional<List<User>> getAll(int page, int size) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        return Optional.ofNullable(query.setFirstResult(page).setMaxResults(size).getResultList());

    }

    @Override
    public Optional<User> getById(Long id) throws DaoException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public User updateBudget(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        User updatedUser = entityManager.merge(user);
        entityManager.getTransaction().commit();
        return updatedUser;
    }

    @Override
    public Optional<Tag> getMostWidelyUsedTag() throws DaoException {
        Optional<Tag> tag = Optional.empty();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Long idUser = getIdUser(entityManager, criteriaBuilder);

        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);

        criteriaQuery.select(root);

        criteriaQuery.where(criteriaBuilder.equal(root.join(TAG_FIELD_CERTIFICATES).join(CERTIFICATE_FIELD_USERS), idUser));
        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.count(root.get(TAG_FIELD_ID))));
        criteriaQuery.groupBy(root.get(TAG_FIELD_ID));

        List<Tag> tags = entityManager.createQuery(criteriaQuery).getResultList();

        if (tags.size() > 0) {
            tag = Optional.of(tags.get(0));
        }
        return tag;

    }

    private Long getIdUser(EntityManager entityManager, CriteriaBuilder criteriaBuilder) throws DaoException {
        Long idUser;
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root.get(ORDER_FIELD_USER_ID));
        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.sum(root.get(ORDER_FIELD_PRICE))));
        criteriaQuery.groupBy(root.get(ORDER_FIELD_USER_ID));
        List<Long> idUsers = entityManager.createQuery(criteriaQuery).getResultList();
        if (idUsers.size() > 0) {
            idUser = idUsers.get(0);
        } else {
            throw new DaoException("Could not find tag");
        }
        return idUser;
    }
}
