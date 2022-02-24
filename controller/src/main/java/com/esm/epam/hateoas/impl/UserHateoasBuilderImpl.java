package com.esm.epam.hateoas.impl;

import com.esm.epam.controller.UserController;
import com.esm.epam.entity.User;
import com.esm.epam.hateoas.HateoasBuilder;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import static com.esm.epam.util.ParameterAttribute.MOST_WIDELY_USED_TAG;
import static com.esm.epam.util.ParameterAttribute.ORDER;
import static com.esm.epam.util.ParameterAttribute.TAG;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoasBuilderImpl implements HateoasBuilder<User> {
    @Override
    public void buildDefaultHateoas(RepresentationModel model) {
        model.add(linkTo(methodOn(UserController.class).getUserList(0, 5)).withSelfRel().withType(HttpMethod.GET.toString()));
        model.add(linkTo(methodOn(UserController.class).getMostWidelyUsedTag()).slash(MOST_WIDELY_USED_TAG).withRel(TAG).withType(HttpMethod.GET.toString()));
    }

    @Override
    public void buildFullHateoas(User user) {
        buildDefaultHateoas(user);
        user.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel().withType(HttpMethod.GET.toString()));
        user.add(linkTo(methodOn(UserController.class).getUserOrders(user.getId(), 1, 5)).withRel(ORDER).withType(HttpMethod.GET.toString()));
        user.add(linkTo(methodOn(UserController.class).updateUser(user.getId(), new User())).withSelfRel().withType(HttpMethod.PATCH.toString()));
    }
}
