package com.esm.epam.repository.impl;

import com.esm.epam.builder.UpdateQueryBuilder;
import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.User;
import com.esm.epam.exception.DaoException;
import com.esm.epam.extractor.CertificateExtractor;
import com.esm.epam.repository.AbstractService;
import com.esm.epam.repository.UserDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.esm.epam.util.ParameterAttribute.ADD_USER_CERTIFICATE_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_ALL_CERTIFICATES_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_ALL_USERS_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_CERTIFICATE_BY_NAME_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_USER_BY_ID_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_USER_CERTIFICATES;

@Repository
public class UserDaoImpl extends AbstractService<User> implements UserDao {
    private final ResultSetExtractor<List<User>> userExtractor;
    private final UpdateQueryBuilder<User> updateQueryBuilder;
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate, ResultSetExtractor<List<User>> userExtractor, UpdateQueryBuilder<User> updateQueryBuilder) {
        super(jdbcTemplate);
        this.userExtractor = userExtractor;
        this.updateQueryBuilder = updateQueryBuilder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<List<User>> getAll(int page, int size) {
        return getCertificates(page, size, GET_ALL_USERS_QUERY, userExtractor);
    }

    @Override
    public Optional<User> getById(Long id) throws DaoException {
        Optional<User> user = Optional.empty();
        Optional<List<User>> users = Optional.ofNullable(jdbcTemplate.query(GET_USER_BY_ID_QUERY, userExtractor, id));
        if (!users.isPresent()) {
            throw new DaoException("User was not found");
        }
        if (!users.get().isEmpty()) {
            user = Optional.of(users.get().get(0));
        }
        return user;
    }

    @Override
    public Optional<User> update(User user, Long idUser) throws DaoException {
        Optional<String> updateCertificateQuery = updateQueryBuilder.getUpdateQuery(user, idUser);
        updateCertificateQuery.ifPresent(jdbcTemplate::update);

        List<Certificate> certificates = user.getCertificates();
        updateUserCertificates(idUser, certificates);
        return getById(idUser);
    }

    private void updateUserCertificates(long userId, List<Certificate> certificates) throws DaoException {
        if (certificates != null) {
            if (certificates.size() != 0) {
                List<Long> idAddedCertificates = getCertificatesId(certificates, userId);
                addUserCertificates(userId, idAddedCertificates);
            }
        }
    }

    private void addUserCertificates(long userId, List<Long> idAddedCertificates) {
        for (Long idCertificate : idAddedCertificates) {
            jdbcTemplate.update(ADD_USER_CERTIFICATE_QUERY, userId, idCertificate);
        }
    }

    private List<Long> getCertificatesId(List<Certificate> certificates, long userId) throws DaoException {
        List<Long> idsCertificate = new ArrayList<>();

        List<Certificate> certificatesDB = jdbcTemplate.query(GET_ALL_CERTIFICATES_QUERY, new CertificateExtractor());
        List<String> certificatesNameDB = certificatesDB.stream()
                .map(Certificate::getName)
                .collect(Collectors.toList());

        List<Certificate> validCertificates = certificates.stream()
                .filter(Objects::nonNull)
                .filter(certificate -> certificate.getName() != null)
                .collect(Collectors.toList());

        List<Long> certificatesId = jdbcTemplate.queryForList(GET_USER_CERTIFICATES, Long.class, userId);

        return getRequiredTags(idsCertificate, certificatesNameDB, validCertificates, certificatesId);
    }

    private List<Long> getRequiredTags(List<Long> idsCertificate, List<String> certificatesNameDB, List<Certificate> validCertificates, List<Long> certificatesId) throws DaoException {
        for (Certificate certificate : validCertificates) {
            Optional<List<Certificate>> requiredCertificate = Optional.ofNullable(jdbcTemplate.query(GET_CERTIFICATE_BY_NAME_QUERY, new CertificateExtractor(), certificate.getName()));
            if (!requiredCertificate.isPresent()) {
                throw new DaoException("No required certificates to update user");
            }
            if (requiredCertificate.get().isEmpty()) {
                throw new DaoException("No required certificates to update user");
            }
            if (!certificatesId.contains(requiredCertificate.get().get(0).getId())) {
                idsCertificate.add(requiredCertificate.get().get(0).getId());
            }
        }

        return idsCertificate.stream()
                .distinct()
                .collect(Collectors.toList());
    }
}
