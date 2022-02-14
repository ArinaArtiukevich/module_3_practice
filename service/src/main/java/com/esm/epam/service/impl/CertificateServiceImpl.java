package com.esm.epam.service.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import com.esm.epam.repository.CRDDao;
import com.esm.epam.repository.CertificateDao;
import com.esm.epam.service.CertificateService;
import com.esm.epam.util.CurrentDate;
import com.esm.epam.validator.ServiceValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.esm.epam.util.ParameterAttribute.TAG;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateDao certificateDao;
    private final CRDDao<Tag> tagDao;
    private final ServiceValidator<Certificate> validator; // todo
    private final CurrentDate date;

    public CertificateServiceImpl(CertificateDao certificateDao, CRDDao<Tag> tagDao, ServiceValidator<Certificate> validator, CurrentDate date) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.validator = validator;
        this.date = date;
    }

    @Override
    public Optional<Certificate> update(Certificate certificate, Long idCertificate) throws DaoException, ResourceNotFoundException {
        Optional<Certificate> certificateBeforeUpdate = certificateDao.getById(idCertificate);
        Optional<Certificate> updatedCertificate = Optional.empty();
        if (certificateBeforeUpdate.isPresent()) {
            certificate.setLastUpdateDate(date.getCurrentDate());
            updatedCertificate = certificateDao.update(certificate, idCertificate);
        } else {
            throw new ResourceNotFoundException("No such certificate");
        }
        return updatedCertificate;
    }

    @Override
    public List<Certificate> getAll(int page, int size) throws ResourceNotFoundException {
        Optional<List<Certificate>> certificates = certificateDao.getAll(page, size);
        validator.validateListIsPresent(certificates);
        return certificates.get();
    }

    @Override
    @Transactional
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
    public List<Certificate> getFilteredList(MultiValueMap<String, Object> params, int page, int size) throws ResourceNotFoundException, ServiceException, DaoException {
        prepareTagParam(params);
        Optional<List<Certificate>> certificates = certificateDao.getFilteredList(params, page, size);
        validator.validateListIsPresent(certificates);
        return certificates.get();
    }

    @Override
    public Optional<Certificate> deleteTag(Long id, Long idTag) throws DaoException {
        return certificateDao.deleteTag(id, idTag);
    }

    private void prepareTagParam(MultiValueMap<String, Object> params) throws ServiceException, DaoException {
        if (params.containsKey(TAG)) {
            List<Object> nameTags = params.get(TAG);
            List<Long> idTags = new ArrayList<>();
            for (Object name : nameTags) {
                if (!name.getClass().equals(String.class)) {
                    throw new ServiceException("Enter tag name");
                }
                Optional<Tag> tag = tagDao.getByName((String) name);
                if (!tag.isPresent()) {
                    throw new ServiceException("Tag with name = " + name + " does not exist");
                }
                idTags.add(tag.get().getId());
            }
            List<Object> idTagsObjectList = new ArrayList<>(idTags);
            params.replace(TAG, idTagsObjectList);
        }
    }
}
