package com.esm.epam.repository;

import org.springframework.util.MultiValueMap;

/**
 * @param <T> describes type parameter
 */
public interface QueryBuilder<T> {
    /**
     * gets query to update element with idT
     *
     * @param t   is element with fields to be updated
     * @param idT of element to be updated
     * @return required query
     */
    String getUpdateQuery(T t, Long idT);

    /**
     * gets query to find filtered elements
     *
     * @param params collection that contains {@link String} as key and {@link Object} as value
     * @return required query
     */
    String getFilteredList(MultiValueMap<String, Object> params);
}
