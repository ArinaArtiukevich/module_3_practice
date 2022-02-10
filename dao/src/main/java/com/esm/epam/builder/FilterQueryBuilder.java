package com.esm.epam.builder;

import org.springframework.util.MultiValueMap;

/**
 * @param <T> describes type parameter
 */
public interface FilterQueryBuilder<T> {
    /**
     * gets query to find filtered elements
     *
     * @param params collection that contains {@link String} as key and {@link Object} as value
     * @return required query
     */
    String getFilteredList(MultiValueMap<String, Object> params);
}
