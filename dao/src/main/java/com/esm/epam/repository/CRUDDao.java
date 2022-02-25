package com.esm.epam.repository;

import com.esm.epam.exception.DaoException;

import java.util.Optional;

public interface CRUDDao<T> extends CRDDao<T> {
    /**
     * updates element by id
     *
     * @param t   is element with fields to be updated
     * @param idT of element to be updated
     * @return updated element
     */
    Optional<T> update(T t, Long idT) throws DaoException;

    /**
     * deletes tag by tag's id
     *
     * @param id  is id of element with tags
     * @param idTag is id of tag to be deleted
     * @return element with updated tags
     */
    Optional<T> deleteTag(Long id, Long idTag) throws DaoException;

}
