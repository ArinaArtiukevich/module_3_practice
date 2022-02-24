package com.esm.epam.controller;

import com.esm.epam.entity.Order;
import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;
import com.esm.epam.entity.View;
import com.esm.epam.hateoas.HateoasBuilder;
import com.esm.epam.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/users")
@Validated
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final HateoasBuilder<User> hateoasBuilder;

    @GetMapping
    @JsonView(View.UI.class)
    @ResponseStatus(OK)
    public List<User> getUserList(@RequestParam("page") @Min(1) int page, @RequestParam("size") @Min(1) int size) {
        List<User> users = userService.getAll(page, size);
        users.forEach(hateoasBuilder::buildFullHateoas);
        return users;
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @JsonView(View.UI.class)
    public User getUser(@PathVariable("id") @Min(1L) long id) {
        User user = userService.getById(id);
        hateoasBuilder.buildFullHateoas(user);
        return user;
    }

    @GetMapping(value = "/{id}/orders")
    @ResponseStatus(OK)
    @JsonView(View.UI.class)
    public List<Order> getUserOrders(@PathVariable("id") @Min(1L) long id, @RequestParam("page") @Min(1) int page, @RequestParam("size") @Min(1) int size) {
        List<Order> orders = userService.getOrders(id, page, size);
        orders.forEach(hateoasBuilder::buildDefaultHateoas);
        return orders;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    @JsonView(View.UI.class)
    public RepresentationModel<User> updateUser(@PathVariable("id") @Min(1L) long id, @RequestBody User user) {
        User updatedUser = userService.update(user, id);
        hateoasBuilder.buildFullHateoas(updatedUser);
        return updatedUser;
    }

    @GetMapping("/mostWidelyUsedTag")
    @JsonView(View.UI.class)
    public ResponseEntity<RepresentationModel<Tag>> getMostWidelyUsedTag() {
        ResponseEntity<RepresentationModel<Tag>> responseEntity;
        Optional<Tag> requiredTag = userService.getMostWidelyUsedTag();
        if (requiredTag.isPresent()) {
            hateoasBuilder.buildDefaultHateoas(requiredTag.get());
            responseEntity = new ResponseEntity<>(requiredTag.get(), OK);
        } else {
            responseEntity = ResponseEntity.noContent().build();
        }
        return responseEntity;
    }
}