package com.esm.epam.util;

import java.util.Arrays;
import java.util.List;

public class ParameterAttribute {
    private ParameterAttribute() {
    }
    public static final String TAG = "tag";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String SORT = "sort";
    public static final String DIRECTION = "direction";
    public static final String PAGE = "page";
    public static final String SIZE = "size";

    public static final List<String> SORT_KEYS = Arrays.asList(TAG, NAME, DESCRIPTION, SORT, DIRECTION, PAGE, SIZE);
}
