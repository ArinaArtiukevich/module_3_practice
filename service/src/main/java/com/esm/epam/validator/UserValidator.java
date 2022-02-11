package com.esm.epam.validator;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.User;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface UserValidator {
    void validateUser(Optional<User> user) throws ResourceNotFoundException;

    void validateUserCertificateListToBeAdded(List<Certificate> certificateList) throws ResourceNotFoundException, ServiceException;
}
