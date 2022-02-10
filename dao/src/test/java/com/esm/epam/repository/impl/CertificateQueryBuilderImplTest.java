package com.esm.epam.repository.impl;

import com.esm.epam.builder.QueryBuilder;
import com.esm.epam.builder.impl.CertificateQueryBuilderImpl;
import com.esm.epam.entity.Certificate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CertificateQueryBuilderImpl.class)
class CertificateQueryBuilderImplTest {

    @Autowired
    private QueryBuilder<Certificate> queryBuilder;

    @Test
    void getUpdateQuery() {
        Certificate certificateWithFieldsToBeUpdated =
                Certificate.builder()
                        .name("snowboarding")
                        .description("snowboarding school")
                        .build();
        String expectedQuery = "UPDATE gift_certificates SET name = 'snowboarding', description = 'snowboarding school' WHERE gift_certificates.id = 1";
        String actualQuery = queryBuilder.getUpdateQuery(certificateWithFieldsToBeUpdated, 1L);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void getFilteredList() {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("name", "sn");
        params.add("desc", "sc");
        params.add("tag", "tag_winter");
        params.add("sort", "name");
        params.add("direction", "asc");
        String expectedQuery = "SELECT * FROM gift_certificates \n" +
                "LEFT JOIN certificates_tags ON (gift_certificates.id=certificates_tags.certificate_id) \n" +
                "LEFT JOIN tags ON (tags.tag_id = certificates_tags.tag_id) WHERE  ( gift_certificates.name LIKE '%sn%' )  AND  ( gift_certificates.id IN ( SELECT certificates_tags.certificate_id FROM certificates_tags WHERE certificates_tags.tag_id = tag_winter ))  ORDER BY gift_certificates.name ASC";
        String actualQuery = queryBuilder.getFilteredList(params);
        assertEquals(expectedQuery, actualQuery);
    }
}