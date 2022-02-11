package com.esm.epam.repository.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.User;
import com.esm.epam.exception.DaoException;
import com.esm.epam.extractor.CertificateExtractor;
import com.esm.epam.repository.AbstractDao;
import com.esm.epam.repository.UserDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.esm.epam.util.ParameterAttribute.ADD_USER_CERTIFICATE_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_ALL_USERS_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_CERTIFICATE_BY_NAME_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_USER_BY_ID_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_USER_CERTIFICATES;
import static com.esm.epam.util.ParameterAttribute.UPDATE_USER_BUDGET_QUERY;

@Repository
public class UserDaoImpl extends AbstractDao<User> implements UserDao {
    private final ResultSetExtractor<List<User>> userExtractor;
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate, ResultSetExtractor<List<User>> userExtractor) {
        super(jdbcTemplate);
        this.userExtractor = userExtractor;
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
        List<Certificate> certificates = user.getCertificates();
        updateUserCertificates(idUser, certificates);
        return getById(idUser);
    }

    @Override
    public void updateBudget(Long idUser, int budget) {
        jdbcTemplate.update(UPDATE_USER_BUDGET_QUERY, budget, idUser);
    }

    private void updateUserCertificates(long userId, List<Certificate> certificates) throws DaoException {
        if (certificates != null) {
            if (certificates.size() == 1) {
                Long idAddedCertificate = getCertificatesId(certificates.get(0), userId);
                addUserCertificates(userId, idAddedCertificate);
            }
        }
    }

    private void addUserCertificates(long userId, Long idAddedCertificates) {
        jdbcTemplate.update(ADD_USER_CERTIFICATE_QUERY, userId, idAddedCertificates);
    }

    private Long getCertificatesId(Certificate certificate, long userId) throws DaoException {
        List<Long> certificatesId = jdbcTemplate.queryForList(GET_USER_CERTIFICATES, Long.class, userId);
        return getRequiredTags(certificate, certificatesId);
    }

    private Long getRequiredTags(Certificate certificate, List<Long> certificatesId) throws DaoException {
        Optional<List<Certificate>> requiredCertificates = Optional.ofNullable(jdbcTemplate.query(GET_CERTIFICATE_BY_NAME_QUERY, new CertificateExtractor(), certificate.getName()));
        if (!requiredCertificates.isPresent()) {
            throw new DaoException("No required certificates to update user");
        }
        if (requiredCertificates.get().isEmpty()) {
            throw new DaoException("No required certificates to update user");
        }
        if (certificatesId.contains(requiredCertificates.get().get(0).getId())) {
            throw new DaoException("User has certificate with id = " + requiredCertificates.get().get(0).getId());
        }
        return requiredCertificates.get().get(0).getId();
    }
}
