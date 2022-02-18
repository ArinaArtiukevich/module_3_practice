package com.esm.epam.service.impl;

import com.esm.epam.entity.Tag;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.repository.CRDDao;
import com.esm.epam.service.CRDService;
import com.esm.epam.validator.ServiceValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements CRDService<Tag> {

    private final CRDDao<Tag> tagDao;
    private final ServiceValidator<Tag> validator;

    public TagServiceImpl(CRDDao<Tag> tagDao, ServiceValidator<Tag> validator) {
        this.tagDao = tagDao;
        this.validator = validator;
    }

    @Override
    public List<Tag> getAll(int page, int size) throws ResourceNotFoundException {
        Optional<List<Tag>> tags = tagDao.getAll(page, size);
        validator.validateListIsPresent(tags);
        return tags.get();
    }

    @Override
    public Tag add(Tag tag) throws DaoException {
        return tagDao.add(tag);

    }

    @Override
    public Tag getById(Long id) throws ResourceNotFoundException, DaoException {
        Optional<Tag> tag = tagDao.getById(id);
        validator.validateEntity(tag, id);
        return tag.get();
    }

    @Override
    public boolean deleteById(Long id) {
        return tagDao.deleteById(id);
    }
}
