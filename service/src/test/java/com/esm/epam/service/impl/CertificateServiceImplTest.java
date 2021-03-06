package com.esm.epam.service.impl;

import com.esm.epam.builder.Builder;
import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import com.esm.epam.repository.impl.CertificateDaoImpl;
import com.esm.epam.util.CurrentDate;
import com.esm.epam.validator.impl.ServiceCertificateValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
class CertificateServiceImplTest {

    @Mock
    private CertificateDaoImpl certificateDao = Mockito.mock(CertificateDaoImpl.class);

    @Mock
    private ServiceCertificateValidatorImpl validator = Mockito.mock(ServiceCertificateValidatorImpl.class);

    @Mock
    private CurrentDate date = new CurrentDate();

    @Mock
    private Builder<Certificate> certificateBuilder;

    @InjectMocks
    private CertificateServiceImpl certificateService;

    private final Certificate certificateWithFieldsToBeUpdated = Certificate.builder()
            .name("football")
            .description("playing football")
            .build();
    private final Certificate newCertificate = Certificate.builder()
            .id(null)
            .name("tennis")
            .description("playing tennis")
            .price(204)
            .duration(30)
            .build();
    private final Certificate certificate = Certificate.builder()
            .id(1L)
            .name("skiing")
            .description("skiing in alps")
            .price(200)
            .duration(100)
            .build();

    private final List<Certificate> certificates = Arrays.asList(Certificate.builder()
                    .id(2L)
                    .name("snowboarding")
                    .description("snowboarding school")
                    .price(1440)
                    .duration(12)
                    .build(),
            Certificate.builder()
                    .id(3L)
                    .name("sneakers")
                    .description("clothing and presents")
                    .price(200)
                    .duration(1)
                    .tags(Collections.singletonList(new Tag(1L, "tag_paper")))
                    .build(),
            Certificate.builder()
                    .id(4L)
                    .name("hockey")
                    .description("sport")
                    .price(120)
                    .duration(62)
                    .tags(Collections.singletonList(new Tag(2L, "tag_name")))
                    .build()
    );

    @Test
    void testUpdate_positive() throws DaoException, ResourceNotFoundException {
        Certificate expectedCertificate = Certificate.builder().id(1L)
                .name("football")
                .description("playing football")
                .price(200)
                .duration(100)
                .build();
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                certificate.setName("football");
                certificate.setDescription("playing football");
                return null;
            }
        }).when(certificateDao).update(certificateWithFieldsToBeUpdated);
        when(certificateDao.getById(1L)).thenReturn(Optional.ofNullable(certificate));
        when(certificateBuilder.buildObject(certificate, certificateWithFieldsToBeUpdated)).thenReturn(expectedCertificate);
        when(certificateDao.getAll(0, 1000)).thenReturn(Arrays.asList(certificate));

        certificateService.update(certificateWithFieldsToBeUpdated, 1L);
        Optional<Certificate> actualCertificate = certificateDao.getById(1L);

        assertEquals(expectedCertificate, actualCertificate.get());
    }

    @Test
    void testGetAll_positive() throws ResourceNotFoundException {
        when(certificateDao.getAll(0, 1000)).thenReturn(certificates);
        List<Certificate> actualCertificates = certificateService.getAll(0, 1000);
        assertEquals(certificates, actualCertificates);
    }


    @Test
    public void testAdd_positive() throws DaoException {
        Long newId = 5L;
        Certificate addedCertificate = Certificate.builder()
                .id(newId)
                .name("tennis")
                .description("playing tennis")
                .price(204)
                .duration(30)
                .build();
        when(certificateDao.add(newCertificate)).thenReturn(addedCertificate);
        Optional<Certificate> actualCertificate = Optional.ofNullable(certificateService.add(newCertificate));
        assertEquals(addedCertificate, actualCertificate.get());
    }


    @Test
    public void testGetById_positive() throws ResourceNotFoundException, DaoException {
        when(certificateDao.getById(1L)).thenReturn(Optional.ofNullable(certificate));
        Certificate actualCertificate = certificateService.getById(1L);
        assertEquals(certificate, actualCertificate);
    }

    @Test
    public void testDeleteById_positive() {
        when(certificateDao.deleteById(2L)).thenReturn(true);
        certificateService.deleteById(certificates.get(0).getId());
        Mockito.verify(certificateDao).deleteById(certificates.get(0).getId());
    }

    @Test
    public void testDeleteById() {
        boolean expectedResult = false;
        Long invalidId = -1L;
        when(certificateDao.deleteById(invalidId)).thenReturn(false);
        Boolean actualResult = certificateService.deleteById(invalidId);

        assertEquals(expectedResult, actualResult);

    }

    @Test
    void testGetFilteredList_filterByPartName() throws ResourceNotFoundException, ServiceException, DaoException {
        String partName = "ing";
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("name", partName);
        List<Certificate> list = new LinkedList<>(certificates);

        doAnswer(new Answer<List<Certificate>>() {
            public List<Certificate> answer(InvocationOnMock invocation) {
                list.removeIf(e -> !e.getName().contains(partName));
                return list;
            }
        }).when(certificateDao).getFilteredList(params, 0, 1000);

        List<Certificate> actualCertificates = certificateService.getFilteredList(params, 0, 1000);
        assertEquals(list, actualCertificates);
    }

    @Test
    void testDeleteTag_positive() throws DaoException, ResourceNotFoundException {
        Certificate certificateBefore = Certificate.builder()
                .id(1L)
                .name("skiing")
                .description("skiing in alps")
                .price(200)
                .duration(100)
                .build();
        certificateBefore.setTags(new LinkedList<>(Collections.singletonList(new Tag(1L, "tag_concert"))));
        when(certificateDao.getById(1L)).thenReturn(Optional.of(certificateBefore));
        doAnswer(new Answer<Optional<Certificate>>() {
            public Optional<Certificate> answer(InvocationOnMock invocation) {
                certificateBefore.getTags().remove(0);
                return Optional.of(certificateBefore);
            }
        }).when(certificateDao).deleteTag(1L, 1L);

        Optional<Certificate> actualCertificate = certificateService.deleteTag(1L, 1L);
        assertEquals(certificate, actualCertificate.get());
    }
}