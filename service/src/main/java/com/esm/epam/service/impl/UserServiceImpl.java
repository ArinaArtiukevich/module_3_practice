package com.esm.epam.service.impl;

import com.esm.epam.entity.User;
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
}
