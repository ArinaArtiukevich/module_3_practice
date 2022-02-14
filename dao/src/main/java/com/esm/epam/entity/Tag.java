package com.esm.epam.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends RepresentationModel<Tag> {
    @JsonView(View.UI.class)
    private Long id;

    @JsonView(View.UI.class)
    @NotBlank(message = "Tag name should not be empty.")
    private String name;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Tag(this.getId(), this.getName());
    }
}
