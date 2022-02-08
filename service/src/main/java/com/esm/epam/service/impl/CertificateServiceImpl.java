package com.esm.epam.service.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.repository.CRUDDao;
import com.esm.epam.service.CRUDService;
import com.esm.epam.util.CurrentDate;
import com.esm.epam.validator.ServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CRUDService<Certificate> {

    @Autowired
    private CRUDDao<Certificate> certificateDao;
    @Autowired
    private ServiceValidator<Certificate> validator;
    @Autowired
    private CurrentDate date;

    @Override
    public Optional<Certificate> update(Certificate certificate, Long idCertificate) throws DaoException, ResourceNotFoundException {
        Optional<List<Certificate>> certificates = certificateDao.getAll();
        Optional<Certificate> updatedCertificate = Optional.empty();
        validator.validateListIsNull(certificates);
        validator.validateListIsEmpty(certificates.get());
        List<Long> certificatesId = certificates.get().stream()
                .map(Certificate::getId)
                .collect(Collectors.toList());
        if (certificatesId.contains(idCertificate)) {
            certificate.setLastUpdateDate(date.getCurrentDate());
            updatedCertificate = certificateDao.update(certificate, idCertificate);
        }
        return updatedCertificate;
    }

    @Override
    public List<Certificate> getAll() throws ResourceNotFoundException {
        Optional<List<Certificate>> certificates = certificateDao.getAll();
        validator.validateListIsNull(certificates);
        return certificates.get();
    }

    @Override
    public Optional<Certificate> add(Certificate certificate) throws DaoException {
        certificate.setCreateDate(date.getCurrentDate());
        return certificateDao.add(certificate);
    }

    @Override
    public Certificate getById(Long id) throws ResourceNotFoundException, DaoException {
        Optional<Certificate> certificate = certificateDao.getById(id);
        validator.validateEntity(certificate, id);
        return certificate.get();
    }

    @Override
    public boolean deleteById(Long id) {
        return certificateDao.deleteById(id);
    }

    @Override
    public List<Certificate> getFilteredList(MultiValueMap<String, Object> params) throws ResourceNotFoundException {
        Optional<List<Certificate>> certificates = certificateDao.getFilteredList(params);
        validator.validateListIsNull(certificates);
        return certificates.get();
    }

    @Override
    public Optional<Certificate> deleteTag(Long id, Long idTag) throws DaoException {
        return certificateDao.deleteTag(id, idTag);
    }
}
