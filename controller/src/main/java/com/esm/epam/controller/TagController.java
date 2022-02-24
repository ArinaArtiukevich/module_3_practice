package com.esm.epam.controller;

import com.esm.epam.entity.Tag;
import com.esm.epam.entity.View;
import com.esm.epam.hateoas.HateoasBuilder;
import com.esm.epam.service.CRDService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/tags")
@Validated
@AllArgsConstructor
public class TagController {
    private final CRDService<Tag> tagService;
    private final HateoasBuilder<Tag> hateoasBuilder;


    @GetMapping
    @ResponseStatus(OK)
    @JsonView(View.UI.class)
    public List<Tag> getTagList(@RequestParam("page") @Min(0) int page, @RequestParam("size") @Min(1) int size) {
        List<Tag> tags = tagService.getAll(page, size);
        tags.forEach(hateoasBuilder::buildFullHateoas);
        return tags;
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @JsonView(View.UI.class)
    public Tag getTag(@PathVariable("id") @Min(1L) long id) {
        Tag tag = tagService.getById(id);
        hateoasBuilder.buildFullHateoas(tag);
        return tag;
    }

    @DeleteMapping("/{id}")
    @JsonView(View.UI.class)
    public ResponseEntity<RepresentationModel<Tag>> deleteTag(@PathVariable("id") @Min(1L) long id) {
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
    @ResponseStatus(OK)
    @JsonView(View.UI.class)
    public RepresentationModel<Tag> addTag(@RequestBody Tag tag) {
        Tag addedTag = tagService.add(tag);
        hateoasBuilder.buildFullHateoas(addedTag);
        return addedTag;
    }

}