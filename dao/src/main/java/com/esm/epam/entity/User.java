package com.esm.epam.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends RepresentationModel<User> {
    @JsonView(View.UI.class)
    private Long id;

    @JsonView(View.UI.class)
    @NotBlank(message = "Login should not be empty.")
    private String login;

    @JsonView(View.UI.class)
    @NotNull(message = "Budget should not be null.")
    @Min(value = 0, message = "Budget should be positive.")
    private Integer budget;


    @JsonView(View.UI.class)
    private List<Certificate> certificates;
}