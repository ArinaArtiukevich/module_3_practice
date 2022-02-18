package com.esm.epam.builder.impl;

import com.esm.epam.builder.PredicateBuilder;
import com.esm.epam.entity.Certificate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.esm.epam.util.ParameterAttribute.ASC_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_DESCRIPTION;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_FIELD_DATE;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_FIELD_DESCRIPTION;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_FIELD_NAME;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_FIELD_TAGS;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_NAME;
import static com.esm.epam.util.ParameterAttribute.DATE_PARAMETER;
import static com.esm.epam.util.ParameterAttribute.DESC_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.DIRECTION_PARAMETER;
import static com.esm.epam.util.ParameterAttribute.NAME_PARAMETER;
import static com.esm.epam.util.ParameterAttribute.PERCENT_SYMBOL;
import static com.esm.epam.util.ParameterAttribute.SORT_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.TAG;


@Component
@Qualifier
public class CertificatePredicateBuilderImpl implements PredicateBuilder<Certificate> {

    @Override
    public List<Predicate> getPredicates(MultiValueMap<String, Object> params, CriteriaBuilder criteriaBuilder, CriteriaQuery<Certificate> criteriaQuery, Root<Certificate> root) {
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, List<Object>> entry : params.entrySet()) {
            switch (entry.getKey()) {
                case CERTIFICATE_NAME:
                    addPredicate(params, entry, predicates, criteriaBuilder, root, CERTIFICATE_FIELD_NAME);
                    break;
                case CERTIFICATE_DESCRIPTION:
                    addPredicate(params, entry, predicates, criteriaBuilder, root, CERTIFICATE_FIELD_DESCRIPTION);
                    break;
                case TAG:
                    addTagPredicate(criteriaBuilder, root, predicates, entry);
                    break;
                case SORT_STATEMENT:
                    sortByParameter(params, criteriaBuilder, criteriaQuery, root, entry);
            }
        }
        return predicates;
    }

    private void addTagPredicate(CriteriaBuilder criteriaBuilder, Root<Certificate> root, List<Predicate> predicates, Map.Entry<String, List<Object>> entry) {
        for (Object tag : entry.getValue()) {
            predicates.add(
                    criteriaBuilder.isMember(tag, root.get(CERTIFICATE_FIELD_TAGS)
                    ));
        }
    }

    private void addPredicate(MultiValueMap<String, Object> params, Map.Entry<String, List<Object>> entry, List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<Certificate> root, String certificateField) {
        if (params.get(entry.getKey()).size() > 0) {
            predicates.add(
                    criteriaBuilder.like(root.get(certificateField), PERCENT_SYMBOL + params.get(entry.getKey()).get(0) + PERCENT_SYMBOL
                    ));
        }
    }

    private void sortByParameter(MultiValueMap<String, Object> params, CriteriaBuilder criteriaBuilder, CriteriaQuery<Certificate> criteriaQuery, Root<Certificate> root, Map.Entry<String, List<Object>> entry) {
        String direction = ASC_STATEMENT;
        if (params.containsKey(DIRECTION_PARAMETER)) {
            if (((String) params.get(DIRECTION_PARAMETER).get(0)).equalsIgnoreCase(DESC_STATEMENT)) {
                direction = DESC_STATEMENT;
            }
        }
        if (entry.getValue().size() > 0) {
            switch (entry.getValue().get(0).toString()) {
                case NAME_PARAMETER:
                    sortByDirectionAndParameter(criteriaBuilder, criteriaQuery, root, direction, CERTIFICATE_FIELD_NAME);
                    break;
                case DATE_PARAMETER:
                    sortByDirectionAndParameter(criteriaBuilder, criteriaQuery, root, direction, CERTIFICATE_FIELD_DATE);
                    break;
            }
        }
    }

    private void sortByDirectionAndParameter(CriteriaBuilder criteriaBuilder, CriteriaQuery<Certificate> criteriaQuery, Root<Certificate> root, String direction, String parameter) {
        if (direction.equals(DESC_STATEMENT)) {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(parameter)));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(parameter)));
        }
    }
}
