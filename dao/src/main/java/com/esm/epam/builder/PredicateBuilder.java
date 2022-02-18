package com.esm.epam.builder;

import com.esm.epam.entity.Certificate;
import org.springframework.util.MultiValueMap;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @param <T> describes type parameter
 */
public interface PredicateBuilder<T> {
    /**
     * gets query to find filtered elements
     *
     * @param params collection that contains {@link String} as key and {@link Object} as value
     * @return required query
     */
    List<Predicate> getPredicates(MultiValueMap<String, Object> params, CriteriaBuilder criteriaBuilder, CriteriaQuery<Certificate> criteriaQuery, Root<Certificate> root);
}
