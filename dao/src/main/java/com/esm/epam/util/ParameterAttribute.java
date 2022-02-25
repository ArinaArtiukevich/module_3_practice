package com.esm.epam.util;

public class ParameterAttribute {
    public final static String USER_NAME = "arina";

    public final static String TAG_FIELD_ID = "idTag";
    public final static String TAG_FIELD_NAME = "name";
    public final static String TAG_FIELD_CERTIFICATES = "certificateList";

    public final static String CERTIFICATE_FIELD_ID = "id";
    public final static String CERTIFICATE_FIELD_NAME = "name";
    public final static String CERTIFICATE_FIELD_DESCRIPTION = "description";
    public final static String CERTIFICATE_FIELD_TAGS = "tags";
    public final static String CERTIFICATE_FIELD_DATE = "createDate";
    public final static String CERTIFICATE_FIELD_USERS = "userList";


    public final static String ORDER_FIELD_PRICE = "price";
    public final static String ORDER_FIELD_USER_ID = "idUser";

    public final static String PERCENT_SYMBOL = "%";

    public final static String TAG = "tag";

    public final static String CERTIFICATE_NAME = "name";
    public final static String CERTIFICATE_DESCRIPTION = "description";

    public final static String NAME_PARAMETER = "name";
    public final static String DATE_PARAMETER = "date";
    public final static String DIRECTION_PARAMETER = "direction";

    public final static String SORT_STATEMENT = "sort";
    public final static String ASC_STATEMENT = "ASC";
    public final static String DESC_STATEMENT = "DESC";

    public final static String GET_MOST_WIDELY_USED_TAG = "SELECT * FROM tags WHERE tags.tag_id IN (\n" +
            "SELECT tags.tag_id\n" +
            "FROM tags\n" +
            "RIGHT JOIN certificates_tags ON (tags.tag_id = certificates_tags.tag_id) \n" +
            "RIGHT JOIN gift_certificates ON (gift_certificates.id = certificates_tags.certificate_id)\n" +
            "RIGHT JOIN orders ON (gift_certificates.id = orders.certificate_id) \n" +
            "RIGHT JOIN users ON (orders.user_id = users.user_id) \n" +
            "WHERE users.user_id = \n" +
                "(SELECT orders.user_id \n" +
                "FROM orders \n" +
                "GROUP BY orders.user_id\n" +
                "HAVING sum(orders.price) =\n" +
                    "(SELECT max(user_price) FROM (\n" +
                        "SELECT orders.user_id, sum(orders.price) as user_price \n" +
                        "FROM orders \n" +
                        "GROUP BY orders.user_id) AS all_users_price) \n" +
                " LIMIT 1 OFFSET 0)\n" +
            "GROUP BY tags.tag_id\n" +
            "HAVING count(tags.tag_id) = \n" +
                "( SELECT max(tags_count) FROM (\n" +
                    "SELECT tags.tag_id, count(tags.tag_id) AS tags_count\n" +
                    "FROM tags\n" +
                    "RIGHT JOIN certificates_tags ON (tags.tag_id = certificates_tags.tag_id) \n" +
                    "RIGHT JOIN gift_certificates ON (gift_certificates.id = certificates_tags.certificate_id)\n" +
                    "RIGHT JOIN orders ON (gift_certificates.id = orders.certificate_id) \n" +
                    "RIGHT JOIN users ON (orders.user_id = users.user_id) \n" +
                    "WHERE users.user_id = \n" +
                        "(SELECT orders.user_id \n" +
                        "FROM orders \n" +
                        "GROUP BY orders.user_id\n" +
                        "HAVING sum(orders.price) =\n" +
                            "(SELECT max(user_price) FROM (\n" +
                                "SELECT orders.user_id, sum(orders.price) as user_price \n" +
                                "FROM orders \n" +
                                "GROUP BY orders.user_id) AS all_users_price)\n" +
                        " LIMIT 1 OFFSET 0)\n" +
                "GROUP BY tags.tag_id ) AS counted_tags )\n" +
            "LIMIT 1 OFFSET 0\n" +
            ")";

}
