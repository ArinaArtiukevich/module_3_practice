package com.esm.epam.validator.impl;

import com.esm.epam.entity.User;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.validator.ServiceValidator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ServiceUserValidatorImpl implements ServiceValidator<User> {
    @Override
    public void validateEntity(Optional<User> user, Long id) {
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("No such user with id = " + id);
        }
    }
}
