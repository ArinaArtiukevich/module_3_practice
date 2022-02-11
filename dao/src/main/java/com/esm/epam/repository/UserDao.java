package com.esm.epam.repository;

import com.esm.epam.entity.User;
import com.esm.epam.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<List<User>> getAll(int page, int size);

    Optional<User> getById(Long id) throws DaoException;

    Optional<User> update(User user, Long idUser) throws DaoException;

    void updateBudget(Long idUser, int budget) throws DaoException;
}
