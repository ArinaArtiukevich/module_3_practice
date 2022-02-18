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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        userValidator.validateListIsPresent(users);
         // todo ? return empty list
        return users.get();
    }

    @Override
    public User getById(Long id) throws ResourceNotFoundException, DaoException {
        Optional<User> user = userDao.getById(id);
        userValidator.validateUser(user);
        return user.get();
    }

    @Override
    public User update(User user, Long idUser) throws DaoException, ResourceNotFoundException, ServiceException {
        User updatedUser;
        Optional<Certificate> certificate;
        Optional<User> userBeforeUpdate = userDao.getById(idUser);
        userValidator.validateUser(userBeforeUpdate);
        certificate = getCertificate(user);
        if (!certificate.isPresent()) {
            throw new ResourceNotFoundException("No such certificate");
        }
        if (userBeforeUpdate.get().getBudget() >= certificate.get().getPrice()) {
            validateUserHasCertificate(idUser, certificate);
            Order order = getOrder(idUser, certificate);
            orderDao.addOrder(order);
            prepareUserToBeUpdated(user, certificate, userBeforeUpdate);
            updatedUser = userDao.updateBudget(user);
        } else {
            throw new ServiceException("User does not have enough money");
        }
        return updatedUser;
    }

    private void prepareUserToBeUpdated(User user, Optional<Certificate> certificate, Optional<User> userBeforeUpdate) {
        List<Certificate> userCertificates = userBeforeUpdate.get().getCertificates();
        user.setCertificates(userCertificates);
        user.setBudget(userBeforeUpdate.get().getBudget() - certificate.get().getPrice());
        user.setId(userBeforeUpdate.get().getId());
        user.setLogin(userBeforeUpdate.get().getLogin());
    }

    @Override
    public List<Order> getOrders(Long idUser, int page, int size) {
        return orderDao.getLimitedOrders(idUser, page, size);
    }

    @Override
    public Optional<Tag> getMostWidelyUsedTag() throws DaoException {
        return userDao.getMostWidelyUsedTag();
    }

    private void validateUserHasCertificate(Long idUser, Optional<Certificate> certificate) throws DaoException {
        List<Long> certificatesId = orderDao.getUserOrders(idUser).stream()
                .map(Order::getIdCertificate)
                .collect(Collectors.toList());
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
