package com.esm.epam.hateoas.impl;

import com.esm.epam.controller.TagController;
import com.esm.epam.entity.Tag;
import com.esm.epam.hateoas.HateoasBuilder;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagHateoasBuilderImpl implements HateoasBuilder<Tag> {
    @Override
    public void buildDefaultHateoas(RepresentationModel representationModel) {
        representationModel.add(linkTo(methodOn(TagController.class).getTagList(0, 5)).withSelfRel().withType("GET"));
        representationModel.add(linkTo(methodOn(TagController.class).addTag(new Tag())).withSelfRel().withType("POST"));
    }

    @Override
    public void buildFullHateoas(Tag tag) {
        buildDefaultHateoas(tag);
        tag.add(linkTo(methodOn(TagController.class).getTag(tag.getIdTag())).withSelfRel().withType("GET"));
        tag.add(linkTo(methodOn(TagController.class).deleteTag(tag.getIdTag())).withSelfRel().withType("DELETE"));
    }
}
