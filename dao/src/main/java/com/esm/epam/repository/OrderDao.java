package com.esm.epam.repository;

import com.esm.epam.entity.Order;
import com.esm.epam.exception.DaoException;

import java.util.List;

public interface OrderDao {
    void addOrder(Order order) throws DaoException;

    List<Long> getUserCertificateIds(Long idUser);

    List<Order> getOrders(Long id, int page, int size);
}
