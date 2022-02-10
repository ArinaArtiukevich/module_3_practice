package com.esm.epam.builder;

import java.util.Optional;

public interface UpdateQueryBuilder<T> {
    /**
     * gets query to update element with idT
     *
     * @param t   is element with fields to be updated
     * @param idT of element to be updated
     * @return required query
     */
    Optional<String> getUpdateQuery(T t, Long idT);

}
