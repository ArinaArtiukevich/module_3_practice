package com.esm.epam.repository;

import com.esm.epam.entity.Order;
import com.esm.epam.exception.DaoException;

import java.util.List;

public interface OrderDao {
    void addOrder(Order order) throws DaoException;

    List<Order> getUserOrders(Long idUser);

    List<Order> getLimitedOrders(Long id, int page, int size);
}
