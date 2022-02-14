package com.esm.epam.controller;

import com.esm.epam.entity.Tag;
import com.esm.epam.exception.ControllerException;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import com.esm.epam.hateoas.HateoasBuilder;
import com.esm.epam.service.CRDService;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/tags")
@Validated
public class TagController {

    private final CRDService<Tag> tagService;
    private final HateoasBuilder<Tag> hateoasBuilder;

    public TagController(CRDService<Tag> tagService, HateoasBuilder<Tag> hateoasBuilder) {
        this.tagService = tagService;
        this.hateoasBuilder = hateoasBuilder;
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<Tag>> getTagList(@RequestParam("page") @Min(0) int page, @RequestParam("size") @Min(1) int size) throws ResourceNotFoundException, DaoException, ControllerException, ServiceException {
        List<Tag> tags = tagService.getAll(page, size);
        for (Tag tag : tags) {
            hateoasBuilder.buildFullHateoas(tag);
        }
        return new ResponseEntity<>(tags, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTag(@PathVariable("id") @Min(1L) Long id) throws ResourceNotFoundException, DaoException, ControllerException, ServiceException {
        Tag tag = tagService.getById(id);
        hateoasBuilder.buildFullHateoas(tag);
        return new ResponseEntity<>(tag, OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RepresentationModel<Tag>> deleteTag(@PathVariable("id") @Min(1L) Long id) throws ResourceNotFoundException, DaoException, ControllerException, ServiceException {
        ResponseEntity<RepresentationModel<Tag>> responseEntity;
        if (tagService.deleteById(id)) {
            RepresentationModel<Tag> representationModel = new RepresentationModel<>();
            hateoasBuilder.buildDefaultHateoas(representationModel);
            responseEntity = new ResponseEntity<>(representationModel, OK);
        } else {
            responseEntity = ResponseEntity.noContent().build();
        }
        return responseEntity;
    }

    @PostMapping
    public ResponseEntity<RepresentationModel<Tag>> addTag(@Valid @RequestBody Tag tag) throws DaoException, ResourceNotFoundException, ControllerException, ServiceException {
        ResponseEntity<RepresentationModel<Tag>> responseEntity;
        Optional<Tag> addedTag = tagService.add(tag);
        if (addedTag.isPresent()) {
            hateoasBuilder.buildFullHateoas(addedTag.get());
            responseEntity = new ResponseEntity<>(addedTag.get(), OK);
        } else {
            responseEntity = ResponseEntity.noContent().build();
        }
        return responseEntity;
    }

}