package com.esm.epam.builder;

import com.esm.epam.exception.DaoException;

public interface Builder<T> {
    T buildObject(T currentObject, T objectToBeUpdated) throws DaoException;
}
