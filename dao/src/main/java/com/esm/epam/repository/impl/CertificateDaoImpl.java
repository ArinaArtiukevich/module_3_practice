package com.esm.epam.repository.impl;

import com.esm.epam.builder.FilterQueryBuilder;
import com.esm.epam.builder.UpdateQueryBuilder;
import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.exception.DaoException;
import com.esm.epam.repository.AbstractDao;
import com.esm.epam.repository.CertificateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.esm.epam.util.ParameterAttribute.ADD_CERTIFICATE_QUERY;
import static com.esm.epam.util.ParameterAttribute.ADD_CERTIFICATE_TAG_QUERY;
import static com.esm.epam.util.ParameterAttribute.ADD_TAG_QUERY;
import static com.esm.epam.util.ParameterAttribute.CERTIFICATE_ID;
import static com.esm.epam.util.ParameterAttribute.DELETE_CERTIFICATE_BY_ID_CERTIFICATES_TAGS_QUERY;
import static com.esm.epam.util.ParameterAttribute.DELETE_CERTIFICATE_BY_ID_QUERY;
import static com.esm.epam.util.ParameterAttribute.DELETE_TAG_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_ALL_CERTIFICATES_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_ALL_TAGS_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_CERTIFICATE_BY_ID_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_CERTIFICATE_BY_NAME_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_CERTIFICATE_TAGS;
import static com.esm.epam.util.ParameterAttribute.GET_TAG_BY_NAME_QUERY;

@Repository
public class CertificateDaoImpl extends AbstractDao<Certificate> implements CertificateDao {
    private final FilterQueryBuilder<Certificate> filterQueryBuilder;
    private final UpdateQueryBuilder<Certificate> updateQueryBuilder;
    private final ResultSetExtractor<List<Certificate>> certificateExtractor;
    private final RowMapper<Tag> rowMapper;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateDaoImpl(JdbcTemplate jdbcTemplate, FilterQueryBuilder<Certificate> filterQueryBuilder, UpdateQueryBuilder<Certificate> updateQueryBuilder, ResultSetExtractor<List<Certificate>> certificateExtractor, RowMapper<Tag> rowMapper) {
        super(jdbcTemplate);
        this.filterQueryBuilder = filterQueryBuilder;
        this.updateQueryBuilder = updateQueryBuilder;
        this.certificateExtractor = certificateExtractor;
        this.rowMapper = rowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Optional<List<Certificate>> getAll(int page, int size) {
        return getPaginationList(page, size, GET_ALL_CERTIFICATES_QUERY, certificateExtractor);
    }

    @Override
    public Optional<List<Certificate>> getFilteredList(MultiValueMap<String, Object> params, int page, int size) {
        return getPaginationList(page, size, filterQueryBuilder.getFilteredList(params), certificateExtractor);
    }

    @Override
    public Optional<Certificate> update(Certificate certificate, Long idCertificate) throws DaoException {
        Optional<String> updateCertificateQuery = updateQueryBuilder.getUpdateQuery(certificate, idCertificate);
        updateCertificateQuery.ifPresent(jdbcTemplate::update);

        List<Tag> tags = certificate.getTags();
        updateCertificateTags(idCertificate, tags);
        return getById(idCertificate);
    }

    @Override
    public Optional<Certificate> add(Certificate certificate) throws DaoException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Optional<Certificate> addedCertificate = Optional.empty();
        long idCertificate;
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(ADD_CERTIFICATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, certificate.getName());
            ps.setString(2, certificate.getDescription());
            ps.setLong(3, certificate.getDuration());
            ps.setString(4, certificate.getCreateDate());
            ps.setInt(5, certificate.getPrice());
            return ps;
        }, keyHolder);

        Optional<Map<String, Object>> keys = Optional.ofNullable(keyHolder.getKeys());
        if (keys.isPresent()) {
            idCertificate = (long) keys.get().get(CERTIFICATE_ID);
            List<Tag> tags = certificate.getTags();
            updateCertificateTags(idCertificate, tags);
            addedCertificate = getById(idCertificate);
        }
        return addedCertificate;
    }

    @Override
    public Optional<Certificate> getById(Long id) throws DaoException {
        return getCertificate(GET_CERTIFICATE_BY_ID_QUERY, id);
    }

    @Override
    public boolean deleteById(Long id) {
        boolean isDeleted = false;
        jdbcTemplate.update(DELETE_CERTIFICATE_BY_ID_CERTIFICATES_TAGS_QUERY, id);
        int affectedRows = jdbcTemplate.update(DELETE_CERTIFICATE_BY_ID_QUERY, id);
        if (affectedRows > 0) {
            isDeleted = true;
        }
        return isDeleted;
    }

    @Override
    public Optional<Certificate> deleteTag(Long id, Long idTag) throws DaoException {
        Optional<Certificate> certificate = Optional.empty();
        int affectedRows = jdbcTemplate.update(DELETE_TAG_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY, id, idTag);
        if (affectedRows > 0) {
            certificate = getById(id);
        }
        return certificate;
    }

    @Override
    public Optional<Certificate> getByName(String name) throws DaoException {
        return getCertificate(GET_CERTIFICATE_BY_NAME_QUERY, name);
    }

    private Optional<Certificate> getCertificate(String query, Object parameter) throws DaoException {
        Optional<Certificate> certificate = Optional.empty();
        Optional<List<Certificate>> certificates = Optional.ofNullable(jdbcTemplate.query(query, certificateExtractor, parameter));
        if (!certificates.isPresent()) {
            throw new DaoException("Certificate was not found");
        }
        if (!certificates.get().isEmpty()) {
            certificate = Optional.of(certificates.get().get(0));
        }
        return certificate;
    }


    private void updateCertificateTags(long certificateId, List<Tag> tags) throws DaoException {
        if (tags != null) {
            if (tags.size() != 0) {
                List<Long> idsAddedTag = getTagsId(tags, certificateId);
                addCertificateTags(certificateId, idsAddedTag);
            }
        }
    }

    private void addCertificateTags(long certificate_id, List<Long> idsAddedTag) {
        for (Long idTag : idsAddedTag) {
            jdbcTemplate.update(ADD_CERTIFICATE_TAG_QUERY, certificate_id, idTag);
        }
    }

    private List<Long> getTagsId(List<Tag> tags, long certificateId) throws DaoException {
        List<Long> idsTag = new ArrayList<>();

        List<Tag> tagsDB = jdbcTemplate.query(GET_ALL_TAGS_QUERY, rowMapper);
        List<String> tagsNameDB = tagsDB.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        List<Tag> validTags = tags.stream()
                .filter(Objects::nonNull)
                .filter(tag -> tag.getName() != null)
                .collect(Collectors.toList());

        List<Long> certificateTagsId = jdbcTemplate.queryForList(GET_CERTIFICATE_TAGS, Long.class, certificateId);

        return getRequiredTags(idsTag, tagsNameDB, validTags, certificateTagsId);
    }

    private List<Long> getRequiredTags(List<Long> idsTag, List<String> tagsNameDB, List<Tag> validTags, List<Long> certificateTagsId) throws DaoException {
        for (Tag tag : validTags) {
            if (!tagsNameDB.contains(tag.getName())) {
                jdbcTemplate.update(ADD_TAG_QUERY, tag.getName());
            }
            Optional<Tag> requiredTag = Optional.ofNullable(jdbcTemplate.queryForObject(GET_TAG_BY_NAME_QUERY, rowMapper, tag.getName()));
            if (!requiredTag.isPresent()) {
                throw new DaoException("No required tags to update certificates");
            }

            if (!certificateTagsId.contains(requiredTag.get().getId())) {
                idsTag.add(requiredTag.get().getId());
            }
        }

        return idsTag.stream()
                .distinct()
                .collect(Collectors.toList());
    }

}
