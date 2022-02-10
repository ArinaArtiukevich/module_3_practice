package com.esm.epam.service;

import com.esm.epam.entity.User;
import com.esm.epam.exception.ResourceNotFoundException;

import java.util.List;

public interface UserService {
    List<User> getAll(int page, int size) throws ResourceNotFoundException;

}
