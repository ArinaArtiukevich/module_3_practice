package com.esm.epam.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class CurrentDate {
    public final static String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
