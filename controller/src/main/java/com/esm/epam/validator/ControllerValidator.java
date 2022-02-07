package com.esm.epam.validator;

import com.esm.epam.exception.ControllerException;
import org.springframework.util.MultiValueMap;

import static com.esm.epam.util.ParameterAttribute.SORT_KEYS;

public class ControllerValidator {
    public static void validateSortValues(MultiValueMap<String, Object> params) throws ControllerException {
        if (params.keySet().stream().anyMatch(key -> !SORT_KEYS.contains(key))) {
            throw new ControllerException("Invalid filter key.");
        }
    }

    public static void validateIntToBeUpdated(Integer value) throws ControllerException {
        if (value != null) {
            if (value <= 0) {
                throw new ControllerException("Invalid integer value = " + value);
            }
        }
    }
}
