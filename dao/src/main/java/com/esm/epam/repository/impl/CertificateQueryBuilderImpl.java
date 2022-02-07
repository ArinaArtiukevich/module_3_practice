package com.esm.epam.repository.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.repository.QueryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.esm.epam.util.ParameterAttribute.AND_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.ASC_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.BEGIN_GET_FILTERED_CERTIFICATE_LIST_QUERY;
import static com.esm.epam.util.ParameterAttribute.BEGIN_UPDATE_QUERY;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATES_TAGS_TABLE;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_CREATE_DATE;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_DESCRIPTION;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_ID;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_LAST_UPDATE_DATE;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_LAST_UPDATE_DATE_FIELD;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_NAME;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_TABLE;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_TAGS_CERTIFICATE_ID;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_TAGS_TAG_ID;
import static com.esm.epam.util.ParameterAttribute.DATE_PARAMETER;
import static com.esm.epam.util.ParameterAttribute.DESC_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.DIRECTION_PARAMETER;
import static com.esm.epam.util.ParameterAttribute.FROM_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.IN_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.LIKE_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.NAME_PARAMETER;
import static com.esm.epam.util.ParameterAttribute.ORDER_BY_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.OR_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.SELECT_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.SORT_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.TAG;
import static com.esm.epam.util.ParameterAttribute.WHERE_STATEMENT;
import static com.esm.epam.util.ParameterAttribute.WHERE_UPDATE_QUERY;


@Component
@ComponentScan(basePackages = {"com.esm.epam.repository.impl"})
@Qualifier(value = "certificateQueryBuilder")
public class CertificateQueryBuilderImpl implements QueryBuilder<Certificate> {

    @Override
    public String getUpdateQuery(Certificate certificate, Long idCertificate) {
        String query = BEGIN_UPDATE_QUERY;
        Map<String, Object> fieldsToBeUpdated = getFieldsToBeUpdated(certificate);
        String values = fieldsToBeUpdated.entrySet()
                .stream()
                .map(entry -> entry.getKey() + " = " + entry.getValue())
                .collect(Collectors.joining(", "));

        query = query + values + WHERE_UPDATE_QUERY + idCertificate;
        return query;
    }

    @Override
    public String getFilteredList(MultiValueMap<String, Object> params) {
        String query = BEGIN_GET_FILTERED_CERTIFICATE_LIST_QUERY;
        String whereQuery = "";
        String orderByQuery = "";
        String direction = getDirection(params);
        for (Map.Entry<String, List<Object>> entry : params.entrySet()) {
            switch (entry.getKey()) {
                case TAG:
                    whereQuery = getStatementByTagFilter(entry, whereQuery);
                    break;
                case CERTIFICATE_NAME:
                    whereQuery = getWhereStatementByValue(whereQuery, entry, CERTIFICATE_NAME);
                    break;
                case CERTIFICATE_DESCRIPTION:
                    whereQuery = getWhereStatementByValue(whereQuery, entry, CERTIFICATE_DESCRIPTION);
                    break;
                case SORT_STATEMENT:
                    orderByQuery = getOrderByStatement(orderByQuery, entry, direction);
                    break;
            }
        }
        if (orderByQuery.length() == 0) {
            orderByQuery = orderByQuery + ORDER_BY_STATEMENT + CERTIFICATE_TABLE + "." + CERTIFICATE_ID;
        }

        query = query + whereQuery + orderByQuery;
        return query;
    }

    private String getDirection(MultiValueMap<String, Object> params) {
        Optional<List<Object>> directionParameter = params.entrySet().stream()
                .filter(e -> e.getKey().equals(DIRECTION_PARAMETER))
                .map(Map.Entry::getValue)
                .findFirst();
        return directionParameter.isPresent() ? directionParameter.get().toString() : ASC_STATEMENT;
    }

    private String getOrderByStatement(String orderByQuery, Map.Entry<String, List<Object>> entry, String directionParameter) {
        String direction;
        if (directionParameter.toUpperCase().contains(DESC_STATEMENT)) {
            direction = DESC_STATEMENT;
        } else {
            direction = ASC_STATEMENT;
        }
        for (int i = 0; i < entry.getValue().size(); i++) {
            switch (entry.getValue().get(i).toString()) {
                case NAME_PARAMETER:
                    orderByQuery = getOrderByStatementByValue(orderByQuery, CERTIFICATE_NAME, direction);
                    return orderByQuery;
                case DATE_PARAMETER:
                    orderByQuery = getOrderByStatementByValue(orderByQuery, CERTIFICATE_CREATE_DATE, direction);
                    break;
            }
            if (i != entry.getValue().size() - 1) {
                orderByQuery = orderByQuery + " , ";
            }
        }
        return orderByQuery;
    }

    private String getOrderByStatementByValue(String orderByQuery, String value, String direction) {
        orderByQuery = prepareOrderStatement(orderByQuery);
        orderByQuery = orderByQuery + CERTIFICATE_TABLE + "." + value + " " + direction;
        return orderByQuery;
    }

    private String getWhereStatementByValue(String whereQuery, Map.Entry<String, List<Object>> entry, String value) {
        whereQuery = prepareWhereStatement(whereQuery);
        whereQuery = whereQuery + " ( ";
        for (int i = 0; i < entry.getValue().size(); i++) {
            whereQuery = whereQuery + CERTIFICATE_TABLE + "." + value + LIKE_STATEMENT + "\'%" + entry.getValue().get(i) + "%\'";
            if (i != entry.getValue().size() - 1) {
                whereQuery = whereQuery + OR_STATEMENT;
            }
        }
        whereQuery = whereQuery + " ) ";

        return whereQuery;
    }

    private String getStatementByTagFilter(Map.Entry<String, List<Object>> entry, String whereStatement) {

        whereStatement = prepareWhereStatement(whereStatement);
        whereStatement = whereStatement + " ( " + CERTIFICATE_TABLE + "." + CERTIFICATE_ID + IN_STATEMENT + "(" + SELECT_STATEMENT +
                CERTIFICATES_TAGS_TABLE + "." + CERTIFICATE_TAGS_CERTIFICATE_ID + FROM_STATEMENT + CERTIFICATES_TAGS_TABLE + WHERE_STATEMENT + CERTIFICATES_TAGS_TABLE + "." + CERTIFICATE_TAGS_TAG_ID + " = " + entry.getValue().get(0) + " )) ";
        return whereStatement;
    }

    private String prepareWhereStatement(String whereQuery) {
        if (whereQuery.length() == 0) {
            whereQuery = WHERE_STATEMENT;
        } else {
            whereQuery = whereQuery + AND_STATEMENT;
        }
        return whereQuery;
    }

    private String prepareOrderStatement(String orderByStatement) {
        if (orderByStatement.length() == 0) {
            orderByStatement = ORDER_BY_STATEMENT;
        } else {
            orderByStatement = orderByStatement + " , ";
        }
        return orderByStatement;
    }

    private Map<String, Object> getFieldsToBeUpdated(Certificate certificate) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> mapFields = mapper.convertValue(certificate, Map.class);

        mapFields.values().removeIf(Objects::isNull);
        mapFields.values().removeIf(values -> values instanceof List);
        if (mapFields.containsKey(CERTIFICATE_LAST_UPDATE_DATE_FIELD)) {
            mapFields.put(CERTIFICATE_LAST_UPDATE_DATE, mapFields.remove(CERTIFICATE_LAST_UPDATE_DATE_FIELD));
        }
        return mapFields.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> getStringWithQuotes(e.getValue())));
    }

    private Object getStringWithQuotes(Object object) {
        if (object.getClass() == String.class) {
            String tmpValue = (String) object;
            object = "\'" + tmpValue + "\'";
        }
        return object;
    }

}
