package com.esm.epam.validator;

import com.esm.epam.entity.User;

import java.util.Optional;

public interface UserValidator extends ServiceValidator<User> {
    /**
     * validates certificates list in user entity
     *
     * @param user  is parameter to be validated
     */
    void validateUserToBeUpdated(User user);

}
