package com.esm.epam.hateoas.impl;

import com.esm.epam.controller.UserController;
import com.esm.epam.entity.User;
import com.esm.epam.exception.ControllerException;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import com.esm.epam.hateoas.HateoasBuilder;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoasBuilderImpl implements HateoasBuilder<User> {
    @Override
    public void buildDefaultHateoas(RepresentationModel model) throws ControllerException, ServiceException, ResourceNotFoundException, DaoException {
        model.add(linkTo(methodOn(UserController.class).getUserList(0, 5)).withSelfRel().withType("GET"));
        model.add(linkTo(methodOn(UserController.class).getMostWidelyUsedTag()).slash("mostWidelyUsedTag").withRel("Tag").withType("GET"));
    }

    @Override
    public void buildFullHateoas(User user) throws ResourceNotFoundException, DaoException, ControllerException, ServiceException {
        buildDefaultHateoas(user);
        user.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel().withType("GET"));
        user.add(linkTo(methodOn(UserController.class).getUserOrders(user.getId(), 1, 5)).withRel("Order").withType("GET"));
        user.add(linkTo(methodOn(UserController.class).updateUser(user.getId(), new User())).withSelfRel().withType("PATCH"));
    }
}
