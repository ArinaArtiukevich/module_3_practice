package com.esm.epam.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.List;
import java.util.Optional;

public class AbstractDao<T> {
    private final JdbcTemplate jdbcTemplate;
    public AbstractDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected Optional<List<T>> getPaginationList(int page, int size, String query, ResultSetExtractor<List<T>> extractor) {
        Optional<List<T>> elementsList = Optional.empty();
        Optional<List<T>> allCertificates = Optional.ofNullable(jdbcTemplate.query(query, extractor));
        if (allCertificates.isPresent() && allCertificates.get().size() > page) {
            if (allCertificates.get().size() >= page + size) {
                elementsList = Optional.of(allCertificates.get().subList(page, page + size));
            } else {
                elementsList = Optional.of(allCertificates.get().subList(page, allCertificates.get().size()));
            }
        }
        return elementsList;
    }

}
