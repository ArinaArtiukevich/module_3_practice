package com.esm.epam.repository.impl;

import com.esm.epam.entity.User;
import com.esm.epam.extractor.UserExtractor;
import com.esm.epam.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.esm.epam.util.ParameterAttribute.GET_ALL_USERS_QUERY;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<List<User>> getAll(int page, int size) {
        return Optional.ofNullable(jdbcTemplate.query(GET_ALL_USERS_QUERY, new UserExtractor(), size, page));
    }
}
