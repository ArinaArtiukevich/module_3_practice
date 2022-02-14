package com.esm.epam.validator;

import com.esm.epam.entity.User;
import com.esm.epam.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserValidator {
    void validateUser(Optional<User> user) throws ResourceNotFoundException;
    void validateListIsPresent(Optional<List<User>> users) throws ResourceNotFoundException;
}
