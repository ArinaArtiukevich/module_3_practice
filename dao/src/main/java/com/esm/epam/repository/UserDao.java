package com.esm.epam.repository;

import com.esm.epam.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<List<User>> getAll(int page, int size);
}
