package com.esm.epam.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
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
