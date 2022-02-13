package com.esm.epam.repository.impl;

import com.esm.epam.entity.Tag;
import com.esm.epam.exception.DaoException;
import com.esm.epam.repository.CRDDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.esm.epam.util.ParameterAttribute.ADD_TAG_QUERY;
import static com.esm.epam.util.ParameterAttribute.DELETE_TAG_BY_ID_CERTIFICATES_TAG_QUERY;
import static com.esm.epam.util.ParameterAttribute.DELETE_TAG_BY_ID_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_TAGS_PAGINATION_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_TAG_BY_ID_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_TAG_BY_NAME_QUERY;
import static com.esm.epam.util.ParameterAttribute.TAG_ID;


@Repository
public class TagDaoImpl implements CRDDao<Tag> {
    private final RowMapper<Tag> rowMapper;
    private final JdbcTemplate jdbcTemplate;

    public TagDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public Optional<List<Tag>> getAll(int page, int size) {
        return Optional.of(jdbcTemplate.query(GET_TAGS_PAGINATION_QUERY, rowMapper, size, page));
    }

    @Override
    public Optional<Tag> add(Tag tag) throws DaoException {
        Optional<Tag> addedTag = Optional.empty();
        Long idAddedObject;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(ADD_TAG_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);

        Optional<Map<String, Object>> keys = Optional.ofNullable(keyHolder.getKeys());
        if (keys.isPresent()) {
            idAddedObject = (long) keys.get().get(TAG_ID);
            addedTag = getById(idAddedObject);
        }
        return addedTag;
    }

    @Override
    public Optional<Tag> getById(Long id) throws DaoException {
        List<Tag> tags = jdbcTemplate.query(GET_TAG_BY_ID_QUERY, rowMapper, id);
        if (tags.isEmpty()) {
            throw new DaoException("No tag by id = " + id);
        }
        return Optional.of(tags.get(0));
    }

    @Override
    public Optional<Tag> getByName(String name) {
        return getTag(GET_TAG_BY_NAME_QUERY, name); // todo +certificate to abstract
    }

    @Override
    public boolean deleteById(Long id) {
        boolean isDeleted = false;
        jdbcTemplate.update(DELETE_TAG_BY_ID_CERTIFICATES_TAG_QUERY, id);
        int affectedRows = jdbcTemplate.update(DELETE_TAG_BY_ID_QUERY, id);
        if (affectedRows > 0) {
            isDeleted = true;
        }
        return isDeleted;
    }

    private Optional<Tag> getTag(String query, Object parameter) {
        Optional<Tag> tag = Optional.empty();
        List<Tag> tags = jdbcTemplate.query(query, rowMapper, parameter);
        if (tags.size() == 1) {
            tag = Optional.of(tags.get(0));
        }
        return tag;
    }

}
