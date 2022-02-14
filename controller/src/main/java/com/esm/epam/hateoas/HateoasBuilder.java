package com.esm.epam.hateoas;

import com.esm.epam.exception.ControllerException;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import org.springframework.hateoas.RepresentationModel;

public interface HateoasBuilder<T extends RepresentationModel<? extends T>> {
    void buildDefaultHateoas(RepresentationModel model) throws ControllerException, ServiceException, ResourceNotFoundException, DaoException;

    void buildFullHateoas(T t) throws ResourceNotFoundException, DaoException, ControllerException, ServiceException;
}
