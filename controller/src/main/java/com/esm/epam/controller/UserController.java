package com.esm.epam.controller;

import com.esm.epam.entity.Order;
import com.esm.epam.entity.User;
import com.esm.epam.entity.View;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import com.esm.epam.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    public UserService userService;

    @GetMapping(params = {"page", "size"})
    @JsonView(View.UI.class)
    public ResponseEntity<List<User>> getUserList(@RequestParam("page") @Min(0) int page, @RequestParam("size") @Min(1) int size) throws ResourceNotFoundException {
        List<User> users = userService.getAll(page, size);
        return new ResponseEntity<>(users, OK);
    }

    @GetMapping("/{id}")
    @JsonView(View.UI.class)
    public ResponseEntity<User> getUser(@PathVariable("id") @Min(1L) Long id) throws ResourceNotFoundException, DaoException {
        User user = userService.getById(id);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping(value = "/{id}/orders", params = { "page", "size" })
    @JsonView(View.UI.class)
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable("id") @Min(1L) Long id, @RequestParam("page") @Min(0) int page, @RequestParam("size") @Min(1) int size) throws ResourceNotFoundException, DaoException {
        return new ResponseEntity<>(userService.getOrders(id, page, size), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") @Min(1L) Long id, @RequestBody User user) throws DaoException, ResourceNotFoundException, ServiceException {
        // todo validation
        return userService.update(user, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}