package com.esm.epam.controller;

import com.esm.epam.entity.User;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<List<User>> getTagList(@RequestParam("page") @Min(0) int page, @RequestParam("size") @Min(1) int size) throws ResourceNotFoundException {
        List<User> users = userService.getAll(page, size);
        return new ResponseEntity<>(users, OK);
    }


}