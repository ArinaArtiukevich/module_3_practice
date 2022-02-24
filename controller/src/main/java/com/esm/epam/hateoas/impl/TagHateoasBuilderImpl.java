package com.esm.epam.hateoas.impl;

import com.esm.epam.controller.TagController;
import com.esm.epam.entity.Tag;
import com.esm.epam.hateoas.HateoasBuilder;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagHateoasBuilderImpl implements HateoasBuilder<Tag> {
    @Override
    public void buildDefaultHateoas(RepresentationModel representationModel) {
        representationModel.add(linkTo(methodOn(TagController.class).getTagList(0, 5)).withSelfRel().withType(HttpMethod.GET.toString()));
        representationModel.add(linkTo(methodOn(TagController.class).addTag(new Tag())).withSelfRel().withType(HttpMethod.POST.toString()));
    }

    @Override
    public void buildFullHateoas(Tag tag) {
        buildDefaultHateoas(tag);
        tag.add(linkTo(methodOn(TagController.class).getTag(tag.getId())).withSelfRel().withType(HttpMethod.GET.toString()));
        tag.add(linkTo(methodOn(TagController.class).deleteTag(tag.getId())).withSelfRel().withType(HttpMethod.DELETE.toString()));
    }
}
