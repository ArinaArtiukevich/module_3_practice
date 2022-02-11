package com.esm.epam.mapper;

import com.esm.epam.entity.Order;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.esm.epam.util.ParameterAttribute.ORDERS_CERTIFICATE_ID;
import static com.esm.epam.util.ParameterAttribute.ORDERS_PAYMENT_DATE;
import static com.esm.epam.util.ParameterAttribute.ORDERS_PRICE;
import static com.esm.epam.util.ParameterAttribute.ORDERS_USER_ID;
import static com.esm.epam.util.ParameterAttribute.ORDER_ID;

@Component
public class OrderMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong(ORDER_ID));
        order.setIdUser(rs.getLong(ORDERS_USER_ID));
        order.setIdCertificate(rs.getLong(ORDERS_CERTIFICATE_ID));
        order.setPaymentDate(rs.getString(ORDERS_PAYMENT_DATE));
        order.setPrice(rs.getInt(ORDERS_PRICE));
        return order;
    }
}
