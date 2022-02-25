package com.esm.epam.entity;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
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
