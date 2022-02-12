package com.esm.epam.repository.impl;

import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;
import com.esm.epam.exception.DaoException;
import com.esm.epam.repository.AbstractDao;
import com.esm.epam.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.esm.epam.util.ParameterAttribute.GET_ALL_USERS_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_MOST_WIDELY_USED_TAG;
import static com.esm.epam.util.ParameterAttribute.GET_USER_BY_ID_QUERY;
import static com.esm.epam.util.ParameterAttribute.UPDATE_USER_BUDGET_QUERY;

@Repository
public class UserDaoImpl extends AbstractDao<User> implements UserDao {
    private final ResultSetExtractor<List<User>> userExtractor;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> rowMapper;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate, ResultSetExtractor<List<User>> userExtractor, RowMapper<Tag> rowMapper) {
        super(jdbcTemplate);
        this.userExtractor = userExtractor;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
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

    @Override
    public Optional<Tag> getMostWidelyUsedTag(){
        Optional<Tag> tag = Optional.empty();
        List<Tag> tags = jdbcTemplate.query(GET_MOST_WIDELY_USED_TAG, rowMapper);
        if (!tags.isEmpty()) {
            tag = Optional.of(tags.get(0));
        }
        return tag;
    }
}
