package com.esm.epam.validator.impl;

import com.esm.epam.entity.User;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.validator.UserValidator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ServiceUserValidatorImpl implements UserValidator {
    @Override
    public void validateUser(Optional<User> user) throws ResourceNotFoundException {
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("No such user");
        }
    }

    @Override
    public void validateListIsPresent(Optional<List<User>> users) throws ResourceNotFoundException {
        if (!users.isPresent()) {
            throw new ResourceNotFoundException("Users were not found.");
        }
    } // todo validator
}
