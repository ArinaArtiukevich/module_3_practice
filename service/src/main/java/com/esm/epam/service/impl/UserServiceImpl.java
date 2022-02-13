package com.esm.epam.service.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Order;
import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import com.esm.epam.repository.CertificateDao;
import com.esm.epam.repository.OrderDao;
import com.esm.epam.repository.UserDao;
import com.esm.epam.service.UserService;
import com.esm.epam.util.CurrentDate;
import com.esm.epam.validator.UserValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserValidator userValidator;
    private final UserDao userDao;
    private final OrderDao orderDao;
    private final CertificateDao certificateDao;
    private final CurrentDate date;

    public UserServiceImpl(UserValidator userValidator, UserDao userDao, OrderDao orderDao, CertificateDao certificateDao, CurrentDate date) {
        this.userValidator = userValidator;
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.certificateDao = certificateDao;
        this.date = date;
    }

    @Override
    public List<User> getAll(int page, int size) throws ResourceNotFoundException {
        Optional<List<User>> users = userDao.getAll(page, size);
        if (!users.isPresent()) {
            throw new ResourceNotFoundException("Users were not found.");
        } // todo validator or return empty list
        return users.get();
    }

    @Override
    public User getById(Long id) throws ResourceNotFoundException, DaoException {
        Optional<User> user = userDao.getById(id);
        // todo validator
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("Requested user resource not found id = " + id);
        }
        return user.get();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Optional<User> update(User user, Long idUser) throws DaoException, ResourceNotFoundException, ServiceException {
        Optional<User> userBeforeUpdate = userDao.getById(idUser);
        Optional<User> updatedUser;
        Optional<Certificate> certificate;
        userValidator.validateUser(userBeforeUpdate);
        certificate = getCertificate(user);
        if (!certificate.isPresent()) {
            throw new ResourceNotFoundException("No such certificate");
        }
        if (userBeforeUpdate.get().getBudget() >= certificate.get().getPrice()) {
            validateUserHasCertificate(idUser, certificate);
            Order order = getOrder(idUser, certificate);
            orderDao.addOrder(order);
            user.setCertificates(Arrays.asList(certificate.get()));
            updatedUser = userDao.updateBudget(idUser, userBeforeUpdate.get().getBudget() - certificate.get().getPrice());
        } else {
            throw new ServiceException("User does not have enough money");
        }
        return updatedUser;
    }

    @Override
    public List<Order> getOrders(Long idUser, int page, int size) {
        return orderDao.getOrders(idUser, page, size);
    }

    @Override
    public Optional<Tag> getMostWidelyUsedTag() {
        return userDao.getMostWidelyUsedTag();
    }

    private void validateUserHasCertificate(Long idUser, Optional<Certificate> certificate) throws DaoException {
        List<Long> certificatesId = orderDao.getUserCertificateIds(idUser);
        if (certificatesId.contains(certificate.get().getId())) {
            throw new DaoException("User has certificate with id = " + certificate.get().getId());
        }
    }

    private Order getOrder(Long idUser, Optional<Certificate> certificate) {
        Order order = new Order();
        order.setIdUser(idUser);
        order.setIdCertificate(certificate.get().getId());
        order.setPaymentDate(date.getCurrentDate());
        order.setPrice(certificate.get().getPrice());
        return order;
    }

    private Optional<Certificate> getCertificate(User user) throws DaoException {
        Optional<Certificate> certificate;
        if (user.getCertificates().get(0).getId() != null) {
            certificate = certificateDao.getById(user.getCertificates().get(0).getId());
        } else if (user.getCertificates().get(0).getName() != null) {
            certificate = certificateDao.getByName(user.getCertificates().get(0).getName());
        } else {
            throw new DaoException("Enter name or id of required certificate");
        }
        return certificate;
    }
}
