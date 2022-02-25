package com.esm.epam.controller;

import com.esm.epam.entity.Tag;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.service.CRDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/tags")
@Validated
public class TagController {

    @Autowired
    public CRDService<Tag> tagService;

    @GetMapping
    public ResponseEntity<List<Tag>> getTagList() throws ResourceNotFoundException {
        List<Tag> tags = tagService.getAll();
        return new ResponseEntity<>(tags, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTag(@PathVariable("id") @Min(1L) Long id) throws ResourceNotFoundException, DaoException {
        Tag tag = tagService.getById(id);
        return new ResponseEntity<>(tag, OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable("id") @Min(1L) Long id) throws ResourceNotFoundException {
        ResponseEntity<Void> responseEntity;
        if (tagService.deleteById(id)) {
            responseEntity = new ResponseEntity<>(OK);
        } else {
            responseEntity = new ResponseEntity<>(NO_CONTENT);
        }
        return responseEntity;

    }

    @PostMapping
    public ResponseEntity<Tag> addTag(@Valid @RequestBody Tag tag, BindingResult bindingResult) throws DaoException {
        return tagService.add(tag)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());

    }

}