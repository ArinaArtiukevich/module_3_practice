//package com.esm.epam.repository.impl;
//
//
//import com.esm.epam.config.HsqlConfiguration;
//import com.esm.epam.entity.Certificate;
//import com.esm.epam.entity.Tag;
//import com.esm.epam.exception.DaoException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@ContextConfiguration(classes = {CertificateQueryBuilderImpl.class, CertificateDaoImpl.class, HsqlConfiguration.class})
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//class CertificateDaoImplTest {
//    @Autowired
//    private CertificateDaoImpl certificateDao;
//
//    private Tag tag_winter = new Tag(1L, "tag_winter");
//    private Tag tag_relax = new Tag(2L, "tag_relax");
//    private Tag tag_snow = new Tag(3L, "tag_snow");
//    private List<Certificate> certificates = new ArrayList<>(Arrays.asList(
//            Certificate.builder()
//                    .id(1L)
//                    .name("skiing")
//                    .description("skiing school")
//                    .duration(12)
//                    .price(1000)
//                    .createDate("2022-01-24T15:35:04.072")
//                    .tags(Arrays.asList(tag_winter, tag_snow))
//                    .build(),
//            Certificate.builder()
//                    .id(2L)
//                    .name("massage")
//                    .description("massage center")
//                    .duration(2)
//                    .price(100)
//                    .createDate("2022-01-24T15:36:18.987")
//                    .tags(Arrays.asList(tag_winter, tag_relax))
//                    .build()
//    ));
//
//    @Test
//    void testGetAll() {
//        Optional<List<Certificate>> actualCertificates = certificateDao.getAll();
//        assertEquals(certificates, actualCertificates.get());
//    }
//
//    @Test
//    void testGetFilteredList_findByPartDescription() {
//        String partName = "ss";
//        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
//        params.add("name", partName);
//        List<Certificate> expectedCertificates = Arrays.asList(certificates.get(1));
//        Optional<List<Certificate>> actualCertificates = certificateDao.getFilteredList(params);
//        assertEquals(expectedCertificates, actualCertificates.get());
//
//    }
//
//    @Test
//    void testGetFilteredList_getByTag() {
//        String tagParameter = tag_relax.getName();
//        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
//        params.add("tag", tagParameter);
//
//        List<Certificate> expectedCertificates = Arrays.asList(certificates.get(1));
//        Optional<List<Certificate>> actualCertificates = certificateDao.getFilteredList(params);
//        assertEquals(expectedCertificates, actualCertificates.get());
//    }
//
//    @Test
//    void testGetFilteredList_sortByNameAsc() {
//        String sortParameter = "name";
//        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
//        params.add("sort", sortParameter);
//
//        List<Certificate> expectedCertificates = Arrays.asList(certificates.get(1), certificates.get(0));
//        Optional<List<Certificate>> actualCertificates = certificateDao.getFilteredList(params);
//        assertEquals(expectedCertificates, actualCertificates.get());
//    }
//
//    @Test
//    void testUpdate() throws DaoException {
//        Certificate certificateWithFieldsToBeUpdated = Certificate.builder()
//                .name("snowboarding")
//                .tags(Arrays.asList(tag_relax))
//                .build();
//
//        Certificate expectedCertificate =
//                Certificate.builder()
//                        .id(1L)
//                        .name("snowboarding")
//                        .description("skiing school")
//                        .duration(12)
//                        .price(1000)
//                        .createDate("2022-01-24T15:35:04.072")
//                        .tags(Arrays.asList(tag_winter, tag_snow, tag_relax))
//                        .build();
//
//        certificateDao.update(certificateWithFieldsToBeUpdated, 1L);
//        Optional<Certificate> actualCertificate = certificateDao.getById(1L);
//        assertEquals(expectedCertificate, actualCertificate.get());
//    }
//
//    @Test
//    void testGetById() throws DaoException {
//        Certificate expectedCertificate = certificates.get(0);
//        Optional<Certificate> actualCertificate = certificateDao.getById(1L);
//        assertEquals(expectedCertificate, actualCertificate.get());
//    }
//
//    @Test
//    void testDeleteById() {
//        boolean isDeleted = certificateDao.deleteById(1L);
//        certificates.remove(0);
//        Optional<List<Certificate>> actualCertificates = certificateDao.getAll();
//        assertTrue(isDeleted);
//        assertEquals(certificates, actualCertificates.get());
//    }
//}