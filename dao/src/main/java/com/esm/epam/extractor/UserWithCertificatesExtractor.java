package com.esm.epam.extractor;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_CREATE_DATE;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_DESCRIPTION;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_DURATION;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_ID;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_LAST_UPDATE_DATE;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_NAME;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_PRICE;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_TAGS_TAG_ID;
import static com.esm.epam.util.ParameterAttribute.TAG_NAME;
import static com.esm.epam.util.ParameterAttribute.USER_ID;
import static com.esm.epam.util.ParameterAttribute.USER_LOGIN;

@Component
public class UserWithCertificatesExtractor implements ResultSetExtractor<List<User>> {

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Long idUser;
        Long idCertificate;
        Tag tag;
        List<User> users = new ArrayList<>();
        if (rs.isBeforeFirst()) {
            rs.next();
            while (!rs.isAfterLast()) {
                User user = new User();
                idUser = rs.getLong(USER_ID);
                user.setId(idUser);
                user.setLogin(rs.getString(USER_LOGIN));
                List<Certificate> certificates = new ArrayList<>();
                while (!rs.isAfterLast() && rs.getLong(USER_ID) == idUser) {
                    Certificate certificate = new Certificate();
                    idCertificate = rs.getLong(CERTIFICATE_ID);
                    certificate.setId(idCertificate);
                    certificate.setName(rs.getString(CERTIFICATE_NAME));
                    certificate.setDescription(rs.getString(CERTIFICATE_DESCRIPTION));
                    certificate.setPrice(rs.getInt(CERTIFICATE_PRICE));
                    certificate.setDuration(rs.getInt(CERTIFICATE_DURATION));
                    certificate.setCreateDate(rs.getString(CERTIFICATE_CREATE_DATE));
                    certificate.setLastUpdateDate(rs.getString(CERTIFICATE_LAST_UPDATE_DATE));
                    List<Tag> tags = new ArrayList<>();
                    while (!rs.isAfterLast() && rs.getLong(CERTIFICATE_ID) == idCertificate) {
                        tag = new Tag(rs.getLong(CERTIFICATE_TAGS_TAG_ID), rs.getString(TAG_NAME));
                        tags.add(tag);
                        rs.next();
                    }
                    certificate.setTags(tags);
                    certificates.add(certificate);
                }
                user.setCertificates(certificates);
                users.add(user);
            }
        }
        return users;
    }
}