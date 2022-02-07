package com.esm.epam.extractor;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

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

public class CertificateExtractor implements ResultSetExtractor<List<Certificate>> {
    @Override
    public List<Certificate> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Long idCertificate;
        List<Certificate> certificates = new ArrayList<>();
        Tag tag;
        if (rs.isBeforeFirst()) {
            rs.next();
            while (!rs.isAfterLast()) {
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
        }
        return certificates;
    }
}
