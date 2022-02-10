package com.esm.epam.service.impl;

import com.esm.epam.entity.User;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.repository.UserDao;
import com.esm.epam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getAll(int page, int size) throws ResourceNotFoundException {
        Optional<List<User>> users = userDao.getAll(page, size);
        return users.get();
    }

    @Override
    public User getById(Long id) throws ResourceNotFoundException, DaoException {
        Optional<User> user = userDao.getById(id);
        // todo validator
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("Requested user resource not found id = " + id);
        }
        return user.get();
    }

    @Override
    public Optional<User> update(User user, Long idUser) throws DaoException {
        // todo validate if user exists
        return userDao.update(user, idUser);
    }
}
