package com.esm.epam.repository.impl;

import com.esm.epam.entity.User;
import com.esm.epam.exception.DaoException;
import com.esm.epam.repository.AbstractDao;
import com.esm.epam.repository.UserDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.esm.epam.util.ParameterAttribute.GET_ALL_USERS_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_USER_BY_ID_QUERY;
import static com.esm.epam.util.ParameterAttribute.UPDATE_USER_BUDGET_QUERY;

@Repository
public class UserDaoImpl extends AbstractDao<User> implements UserDao {
    private final ResultSetExtractor<List<User>> userExtractor;
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate, ResultSetExtractor<List<User>> userExtractor) {
        super(jdbcTemplate);
        this.userExtractor = userExtractor;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<List<User>> getAll(int page, int size) {
        return getPaginationList(page, size, GET_ALL_USERS_QUERY, userExtractor);
    }

    @Override
    public Optional<User> getById(Long id) throws DaoException {
        Optional<User> user = Optional.empty();
        Optional<List<User>> users = Optional.ofNullable(jdbcTemplate.query(GET_USER_BY_ID_QUERY, userExtractor, id));
        if (!users.isPresent()) {
            throw new DaoException("User was not found");
        }
        if (!users.get().isEmpty()) {
            user = Optional.of(users.get().get(0));
        }
        return user;
    }

    @Override
    public Optional<User> updateBudget(Long idUser, int budget) throws DaoException {
        jdbcTemplate.update(UPDATE_USER_BUDGET_QUERY, budget, idUser);
        return getById(idUser);
    }

}
