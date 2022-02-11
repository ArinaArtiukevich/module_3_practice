package com.esm.epam.service.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.User;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import com.esm.epam.repository.CertificateDao;
import com.esm.epam.repository.UserDao;
import com.esm.epam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private CertificateDao certificateDao;

    @Override
    public List<User> getAll(int page, int size) throws ResourceNotFoundException {
        Optional<List<User>> users = userDao.getAll(page, size);
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

   @Transactional(rollbackFor=Exception.class)
    @Override
    public Optional<User> update(User user, Long idUser) throws DaoException, ResourceNotFoundException, ServiceException {
        Optional<User> userBeforeUpdate = userDao.getById(idUser);
        Optional<User> updatedUser;
        Optional<Certificate> certificate;
        if (!userBeforeUpdate.isPresent()) { // todo validator
            throw new ResourceNotFoundException("No such user");
        }
        if (user.getCertificates() != null) {
            if (user.getCertificates().size() > 1) {
                throw new ServiceException("User can add only one certificate");
            }
            certificate = getCertificate(user);
            if (!certificate.isPresent()) {
                throw new ResourceNotFoundException("No such certificate");
            }
            if (userBeforeUpdate.get().getBudget() >= certificate.get().getPrice()) {
                // todo if exception
                user.setCertificates(Arrays.asList(certificate.get()));
                userDao.updateBudget(idUser, userBeforeUpdate.get().getBudget() - certificate.get().getPrice());
                updatedUser = userDao.update(user, idUser);
            } else {
                throw new ServiceException("User does not have enough money");
            }
        } else {
            throw new ServiceException("Enter certificate");
        }
        return updatedUser;
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
