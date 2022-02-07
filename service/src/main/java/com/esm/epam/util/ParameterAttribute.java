package com.esm.epam.util;

import java.util.Arrays;
import java.util.List;

public class ParameterAttribute {
    public final static String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public final static String TAG = "tag";
    public final static String NAME = "name";
    public final static String DESCRIPTION = "description";
    public final static String SORT = "sort";
    public final static String DIRECTION = "direction";

    public final static List<String> SORT_KEYS = Arrays.asList(TAG, NAME, DESCRIPTION, SORT, DIRECTION);
}
