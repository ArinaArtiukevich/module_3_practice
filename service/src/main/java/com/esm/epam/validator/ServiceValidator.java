package com.esm.epam.validator;

import com.esm.epam.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * @param <T> describes type parameter
 */
public interface ServiceValidator<T> {
    /**
     * validates element with id
     *
     * @param t  is parameter to be validated
     * @param id is id of parameter
     */
    void validateEntity(Optional<T> t, Long id) throws ResourceNotFoundException;

    /**
     * validates list is empty
     *
     * @param t is list to be validated
     */
    void validateListIsEmpty(List<T> t) throws ResourceNotFoundException;

    /**
     * validates list is null
     *
     * @param t is list to be validated
     */
    void validateListIsNull(Optional<List<T>> t) throws ResourceNotFoundException;

}
