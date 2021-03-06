package com.esm.epam.validator.impl;

import com.esm.epam.entity.Tag;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.validator.ServiceValidator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ServiceTagValidatorImpl implements ServiceValidator<Tag> {

    @Override
    public void validateEntity(Optional<Tag> tag, Long id) throws ResourceNotFoundException {
        if (!tag.isPresent()) {
            throw new ResourceNotFoundException("Requested tag resource not found id = " + id);
        }
    }
}
