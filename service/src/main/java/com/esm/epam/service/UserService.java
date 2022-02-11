package com.esm.epam.service;

import com.esm.epam.entity.User;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll(int page, int size) throws ResourceNotFoundException;

    User getById(Long id) throws ResourceNotFoundException, DaoException;

    Optional<User> update(User user, Long idUser) throws DaoException, ResourceNotFoundException, ServiceException;


}
