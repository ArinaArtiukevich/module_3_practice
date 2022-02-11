package com.esm.epam.util;

public class ParameterAttribute {
    public final static String DATABASE_DRIVER = "org.postgresql.Driver";
    public final static String DATABASE_URL = "jdbc:postgresql://localhost:5432/lab_module_2_projectDB";
    public final static String DATABASE_USERNAME = "postgres";
    public final static String DATABASE_PASSWORD = "root";

    public final static String USER_TABLE = "users";
    public final static String USER_ID = "user_id";
    public final static String USER_LOGIN = "user_login";
    public final static String USER_BUDGET = "user_budget";
    public final static String USER_PART_NAME_FIELDS = "user_";

    public final static String TAG_TABLE = "tags";
    public final static String TAG = "tag";
    public final static String TAG_ID = "tag_id";
    public final static String TAG_NAME = "tag_name";

    public final static String CERTIFICATE_TABLE = "gift_certificates";
    public final static String CERTIFICATE_ID = "id";
    public final static String CERTIFICATE_NAME = "name";
    public final static String CERTIFICATE_DESCRIPTION = "description";
    public final static String CERTIFICATE_PRICE = "price";
    public final static String CERTIFICATE_DURATION = "duration";
    public final static String CERTIFICATE_CREATE_DATE = "creation_date";
    public final static String CERTIFICATE_LAST_UPDATE_DATE = "last_update_date";
    public final static String CERTIFICATE_LAST_UPDATE_DATE_FIELD = "lastUpdateDate";

    public final static String CERTIFICATES_TAGS_TABLE = "certificates_tags";
    public final static String CERTIFICATE_TAGS_CERTIFICATE_ID = "certificate_id";
    public final static String CERTIFICATE_TAGS_TAG_ID = "tag_id";

    public final static String BEGIN_CERTIFICATE_UPDATE_QUERY = "UPDATE gift_certificates SET ";
    public final static String WHERE_CERTIFICATE_UPDATE_QUERY = " WHERE gift_certificates.id = ";

    public final static String BEGIN_USER_UPDATE_QUERY = "UPDATE users SET ";
    public final static String WHERE_USER_UPDATE_QUERY = " WHERE users.user_id = ";

    public final static String GET_CERTIFICATE_BY_NAME_QUERY = "SELECT * FROM gift_certificates \n" +
            "LEFT JOIN certificates_tags ON (gift_certificates.id=certificates_tags.certificate_id) \n" +
            "LEFT JOIN tags ON(tags.tag_id = certificates_tags.tag_id)\n" +
            "WHERE gift_certificates.name = ?\n";
    public final static String GET_ALL_CERTIFICATES_QUERY = "SELECT * FROM gift_certificates \n" +
            "LEFT JOIN certificates_tags ON (gift_certificates.id=certificates_tags.certificate_id) \n" +
            "LEFT JOIN tags ON (tags.tag_id = certificates_tags.tag_id)\n" +
            "ORDER BY gift_certificates.id";
    public final static String GET_CERTIFICATE_BY_ID_QUERY = "SELECT * FROM gift_certificates \n" +
            "LEFT JOIN certificates_tags ON (gift_certificates.id=certificates_tags.certificate_id) \n" +
            "LEFT JOIN tags ON(tags.tag_id = certificates_tags.tag_id)\n" +
            "WHERE gift_certificates.id = ?\n";

    public final static String GET_CERTIFICATE_TAGS = "SELECT certificates_tags.tag_id FROM certificates_tags WHERE certificates_tags.certificate_id = ?\n";

    public final static String DELETE_CERTIFICATE_BY_ID_CERTIFICATES_TAGS_QUERY = "DELETE FROM certificates_tags\n" +
            "    WHERE certificates_tags.certificate_id = ?;\n";
    public final static String DELETE_CERTIFICATE_BY_ID_QUERY =
            "    DELETE FROM gift_certificates\n" +
                    "    WHERE gift_certificates.id = ?;";
    public final static String ADD_CERTIFICATE_QUERY = "INSERT INTO gift_certificates(name, description, duration, creation_date, price) \n" +
            "VALUES (?, ?, ?, ?, ?)";
    public final static String ADD_CERTIFICATE_TAG_QUERY = "INSERT INTO certificates_tags(certificate_id, tag_id) VALUES (?,?)";
    public final static String GET_TAG_BY_NAME_QUERY = "SELECT * FROM tags WHERE tags.tag_name = ?";
    public final static String DELETE_TAG_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY = "DELETE FROM certificates_tags\n" +
            "    WHERE (certificates_tags.certificate_id = ?) AND (certificates_tags.tag_id = ?);\n";

    public final static String GET_ALL_TAGS_QUERY = "SELECT * FROM tags ORDER BY tag_id LIMIT ? OFFSET ?";
    public final static String GET_TAG_BY_ID_QUERY = "SELECT * FROM tags WHERE tags.tag_id = ?";
    public final static String ADD_TAG_QUERY = "INSERT INTO tags(tag_name) VALUES (?)";
    public final static String DELETE_TAG_BY_ID_QUERY = " DELETE FROM tags WHERE tags.tag_id = ?";
    public final static String DELETE_TAG_BY_ID_CERTIFICATES_TAG_QUERY = "DELETE FROM certificates_tags WHERE certificates_tags.tag_id = ?;";

    public final static String GET_ALL_USERS_QUERY = "SELECT * FROM users \n" +
            "LEFT JOIN users_certificates ON (users.user_id=users_certificates.user_id) \n" +
            "LEFT JOIN gift_certificates ON (gift_certificates.id = users_certificates.certificate_id)\n" +
            "LEFT JOIN certificates_tags ON (gift_certificates.id=certificates_tags.certificate_id) \n" +
            "LEFT JOIN tags ON (tags.tag_id = certificates_tags.tag_id) \n" +
            "ORDER BY users.user_id";
    public final static String GET_USER_CERTIFICATES = "SELECT users_certificates.certificate_id FROM users_certificates WHERE users_certificates.user_id = ?\n";
    public final static String ADD_USER_CERTIFICATE_QUERY = "INSERT INTO users_certificates(user_id, certificate_id) VALUES (?,?)";
    public final static String GET_USER_BY_ID_QUERY = "SELECT * FROM users \n" +
            "LEFT JOIN users_certificates ON (users.user_id=users_certificates.user_id) \n" +
            "LEFT JOIN gift_certificates ON (gift_certificates.id = users_certificates.certificate_id)\n" +
            "LEFT JOIN certificates_tags ON (gift_certificates.id=certificates_tags.certificate_id) \n" +
            "LEFT JOIN tags ON (tags.tag_id = certificates_tags.tag_id) \n" +
            "WHERE  users.user_id=?";
    public final static String UPDATE_USER_BUDGET_QUERY = "UPDATE users SET user_budget = ? WHERE user_id = ?";

    public final static String BEGIN_GET_FILTERED_CERTIFICATE_LIST_QUERY = "SELECT * FROM gift_certificates \n" +
            "LEFT JOIN certificates_tags ON (gift_certificates.id=certificates_tags.certificate_id) \n" +
            "LEFT JOIN tags ON (tags.tag_id = certificates_tags.tag_id)";
    public final static String ORDER_BY_STATEMENT = " ORDER BY ";

    public final static String NAME_PARAMETER = "name";
    public final static String DATE_PARAMETER = "date";
    public final static String DIRECTION_PARAMETER = "direction";

    public final static String SORT_STATEMENT = "sort";
    public final static String WHERE_STATEMENT = " WHERE ";
    public final static String FROM_STATEMENT = " FROM ";
    public final static String SELECT_STATEMENT = " SELECT ";
    public final static String AND_STATEMENT = " AND ";
    public final static String LIKE_STATEMENT = " LIKE ";
    public final static String OR_STATEMENT = " OR ";
    public final static String IN_STATEMENT = " IN ";
    public final static String ASC_STATEMENT = "ASC";
    public final static String DESC_STATEMENT = "DESC";

}
