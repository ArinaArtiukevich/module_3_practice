package com.esm.epam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ControllerException extends Exception {
    private String message;
}
