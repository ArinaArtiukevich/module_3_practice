package com.esm.epam.repository;

import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;
import com.esm.epam.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<List<User>> getAll(int page, int size);

    Optional<User> getById(Long id) throws DaoException;

    User updateBudget(User user);

    Optional<Tag> getMostWidelyUsedTag() throws DaoException;
}
