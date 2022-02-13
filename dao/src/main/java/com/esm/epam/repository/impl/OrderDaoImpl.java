package com.esm.epam.repository.impl;

import com.esm.epam.entity.Order;
import com.esm.epam.repository.OrderDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import static com.esm.epam.util.ParameterAttribute.ADD_ORDER_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_ALL_ORDERS_QUERY;
import static com.esm.epam.util.ParameterAttribute.GET_USER_CERTIFICATES;

@Repository
public class OrderDaoImpl implements OrderDao {
    private final RowMapper<Order> orderRowMapper;
    private final JdbcTemplate jdbcTemplate;

    public OrderDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Order> orderRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderRowMapper = orderRowMapper;
    }

    @Override
    public void addOrder(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(ADD_ORDER_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, order.getIdUser());
            ps.setLong(2, order.getIdCertificate());
            ps.setInt(3, order.getPrice());
            ps.setString(4, order.getPaymentDate());
            return ps;
        }, keyHolder);
    }

    @Override
    public List<Long> getUserCertificateIds(Long idUser) {
        return jdbcTemplate.queryForList(GET_USER_CERTIFICATES, Long.class, idUser);
    }

    @Override
    public List<Order> getOrders(Long id, int page, int size) {
        return jdbcTemplate.query(GET_ALL_ORDERS_QUERY, orderRowMapper, id, size, page);
    }
}
