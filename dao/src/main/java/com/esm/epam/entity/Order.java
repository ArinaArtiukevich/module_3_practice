package com.esm.epam.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends RepresentationModel<Order> {
    @JsonView(View.UI.class)
    private Long id;

    @JsonView(View.REST.class)
    private Long idUser;

    @JsonView(View.UI.class)
    private Long idCertificate;

    @JsonView(View.UI.class)
    private Integer price;

    @JsonView(View.UI.class)
    private String paymentDate;

}
