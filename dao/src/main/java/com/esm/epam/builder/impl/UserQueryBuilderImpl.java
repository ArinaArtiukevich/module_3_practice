package com.esm.epam.builder.impl;

import com.esm.epam.builder.UpdateQueryBuilder;
import com.esm.epam.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.esm.epam.util.ParameterAttribute.BEGIN_USER_UPDATE_QUERY;
import static com.esm.epam.util.ParameterAttribute.USER_PART_NAME_FIELDS;
import static com.esm.epam.util.ParameterAttribute.WHERE_USER_UPDATE_QUERY;

@Component
public class UserQueryBuilderImpl implements UpdateQueryBuilder<User> {
    @Override
    public Optional<String> getUpdateQuery(User user, Long idUser) {
        Optional<String> query = Optional.empty();
        Map<String, Object> fieldsToBeUpdated = getFieldsToBeUpdated(user);
        String values = fieldsToBeUpdated.entrySet()
                .stream()
                .map(entry -> entry.getKey() + " = " + entry.getValue())
                .collect(Collectors.joining(", "));
        if(!values.isEmpty()){
            query = Optional.of(BEGIN_USER_UPDATE_QUERY + values + WHERE_USER_UPDATE_QUERY + idUser);
        }
        return query;
    }

    private Map<String, Object> getFieldsToBeUpdated(User user) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> mapFields = mapper.convertValue(user, Map.class);

        mapFields.values().removeIf(Objects::isNull);
        mapFields.values().removeIf(values -> values instanceof List);

        return mapFields.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> getUserFields(e.getKey()),
                        e -> getStringWithQuotes(e.getValue())));
    }

    private String getUserFields(String key) {
        return USER_PART_NAME_FIELDS + key;
    }

    private Object getStringWithQuotes(Object object) {
        if (object.getClass() == String.class) {
            String tmpValue = (String) object;
            object = "\'" + tmpValue + "\'";
        }
        return object;
    }
}
