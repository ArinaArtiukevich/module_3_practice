package com.esm.epam.service;

import com.esm.epam.entity.Certificate;
import com.esm.epam.exception.DaoException;

import java.util.Optional;

public interface CertificateService extends CRUDService<Certificate>{
    /**
     * deletes tag by tag's id
     *
     * @param id  is id of element with tags
     * @param idTag is id of tag to be deleted
     * @return element with updated tags
     */
    Optional<Certificate> deleteTag(Long id, Long idTag) throws DaoException;
}
