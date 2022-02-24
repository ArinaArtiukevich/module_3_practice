package com.esm.epam.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.esm.epam.util.ParameterAttribute.DATE_PATTERN;


@Component
public class CurrentDate {
    public String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
