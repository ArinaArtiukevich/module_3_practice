package com.esm.epam.repository;

import com.esm.epam.exception.DaoException;

public interface CRUDDao<T> extends CRDDao<T> {
    /**
     * updates element by id
     *
     * @param t   is element with fields to be updated
     * @return updated element
     */
    T update(T t) throws DaoException;
}
