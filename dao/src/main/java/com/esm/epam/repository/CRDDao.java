package com.esm.epam.repository;

import com.esm.epam.exception.DaoException;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

/**
 * @param <T> describes type parameter
 */
public interface CRDDao<T> {
    /**
     * gets all values
     *
     * @return List with values
     */
    Optional<List<T>> getAll(int page, int size);

    /**
     * gets filtered values
     *
     * @param params collection that contains {@link String} as
     *               key and {@link Object} as value
     * @return List with sorted values
     */
    default Optional<List<T>> getFilteredList(MultiValueMap<String, Object> params, int page, int size) {
        return getAll(page, size);
    }

    /**
     * adds new element
     *
     * @param t the type of element to be added
     * @return element
     */
    Optional<T> add(T t) throws DaoException;

    /**
     * finds element by id
     *
     * @param id is required element id
     * @return required element
     */
    Optional<T> getById(Long id) throws DaoException;

    /**
     * deletes element by id
     *
     * @param id is required element id
     * @return true when element was deleted
     */
    boolean deleteById(Long id);
}

