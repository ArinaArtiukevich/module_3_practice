package com.esm.epam.validator;

import com.esm.epam.entity.User;
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

    public static void validateUserToBeUpdated(User user) throws ControllerException {
        if (user.getId() != null || user.getBudget() != null || user.getLogin() != null) {
            throw new ControllerException("User field can not be updated");
        }
        if (user.getCertificates() == null) {
            throw new ControllerException("Enter certificate");
        }
        if (user.getCertificates().size() != 1) {
            throw new ControllerException("User can add one certificate");
        }
    }
}
