package com.esm.epam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DaoException extends Exception {
    private String message;
}
