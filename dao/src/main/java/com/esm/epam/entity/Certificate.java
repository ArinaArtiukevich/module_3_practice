package com.esm.epam.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Certificate extends RepresentationModel<Certificate> {
    @JsonView(View.UI.class)
    private Long id;

    @JsonView(View.UI.class)
    @NotBlank(message = "Certificate name should not be empty.")
    private String name;

    @JsonView(View.UI.class)
    @NotBlank(message = "Certificate description should not be empty.")
    private String description;

    @JsonView(View.UI.class)
    @NotNull(message = "Price should not be null.")
    @Min(value = 0, message = "Price should be positive.")
    private Integer price;

    @JsonView(View.UI.class)
    @NotNull(message = "Duration should not be null.")
    @Min(value = 0, message = "Duration should be positive.")
    private Integer duration;

    @JsonView(View.REST.class)
    private String createDate;

    @JsonView(View.REST.class)
    private String lastUpdateDate;

    @JsonView(View.UI.class)
    private List<Tag> tags;

}