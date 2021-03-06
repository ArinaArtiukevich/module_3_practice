package com.esm.epam.hateoas;

import com.esm.epam.exception.ControllerException;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import org.springframework.hateoas.RepresentationModel;

/**
 * @param <T> describes type parameter
 */
public interface HateoasBuilder<T extends RepresentationModel<? extends T>> {
    /**
     * builds hateos with default links
     *
     * @param model is entity to collect links
     */
    void buildDefaultHateoas(RepresentationModel model) throws ControllerException, ServiceException, ResourceNotFoundException, DaoException;

    /**
     * builds hateos with extended number of links
     *
     * @param t is entity to collect links
     */
    void buildFullHateoas(T t) throws ResourceNotFoundException, DaoException, ControllerException, ServiceException;
}
