package com.esm.epam.builder.impl;

import com.esm.epam.builder.Builder;
import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.exception.DaoException;
import com.esm.epam.repository.CRDDao;
import com.esm.epam.util.CurrentDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CertificateBuilderImpl implements Builder<Certificate> {
    private final CurrentDate date;
    private final CRDDao<Tag> tagDao;

    @Autowired
    public CertificateBuilderImpl(CurrentDate date, CRDDao<Tag> tagDao) {
        this.date = date;
        this.tagDao = tagDao;
    }

    @Override
    public Certificate buildObject(Certificate currentObject, Certificate objectToBeUpdated) throws DaoException {
        Certificate newCertificate = new Certificate();

        newCertificate.setId(currentObject.getId());
        newCertificate.setLastUpdateDate(date.getCurrentDate());
        newCertificate.setCreateDate(currentObject.getCreateDate());

        if (objectToBeUpdated.getName() != null) {
            newCertificate.setName(objectToBeUpdated.getName());
        } else {
            newCertificate.setName(currentObject.getName());
        }

        if (objectToBeUpdated.getDescription() != null) {
            newCertificate.setDescription(objectToBeUpdated.getDescription());
        } else {
            newCertificate.setDescription(currentObject.getDescription());
        }
        if (objectToBeUpdated.getPrice() != null) {
            newCertificate.setPrice(objectToBeUpdated.getPrice());
        } else {
            newCertificate.setPrice(currentObject.getPrice());
        }
        if (objectToBeUpdated.getDuration() != null) {
            newCertificate.setDuration(objectToBeUpdated.getDuration());
        } else {
            newCertificate.setDuration(currentObject.getDuration());
        }
        List<Tag> tags = new ArrayList<>();
        if (objectToBeUpdated.getTags() != null) {
            addNewTags(objectToBeUpdated, tags);
        }
        if (currentObject.getTags() != null) {
            addExistedTags(tags, currentObject.getTags());
        }
        newCertificate.setTags(tags);
        return newCertificate;
    }

    private void addExistedTags(List<Tag> tags, List<Tag> currentTags) {
        for (Tag tag : currentTags) {
            if (!tags.contains(tag)) {
                tags.add(tag);
            }
        }
    }

    private void addNewTags(Certificate objectToBeUpdated, List<Tag> tags) throws DaoException {
        List<Tag> tagsToBeAdded = objectToBeUpdated.getTags();
        Optional<Tag> tagToBeAdded;
        for (Tag tag : tagsToBeAdded) {
            if (tag.getId() == null && tag.getName() != null) {
                tagToBeAdded = tagDao.getByName(tag.getName());
                addTagToList(tagToBeAdded, tags, tag);
            } else if (tag.getId() != null && tag.getName() == null) {
                tagToBeAdded = tagDao.getById(tag.getId());
                addTagToList(tagToBeAdded, tags, tag);
            } else if (tag.getId() != null && tag.getName() != null) {
                tags.add(tag);
            }
        }
    }

    private void addTagToList(Optional<Tag> tagToBeAdded, List<Tag> tags, Tag tag) {
        if (tagToBeAdded.isPresent() && !tags.contains(tagToBeAdded.get())) {
            tags.add(tagToBeAdded.get());
        }
    }
}
