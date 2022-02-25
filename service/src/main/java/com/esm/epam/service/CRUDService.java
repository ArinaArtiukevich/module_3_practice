package com.esm.epam.service;

import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;

public interface CRUDService<T> extends CRDService<T> {
    /**
     * updates element by id
     *
     * @param t   is element with fields to be updated
     * @param idT of element to be updated
     * @return updated element
     */
    T update(T t, Long idT) throws ResourceNotFoundException, DaoException;

}
