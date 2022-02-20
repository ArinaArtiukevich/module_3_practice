package com.esm.epam.entity;

import com.esm.epam.entity.audit.ModificationInformation;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends RepresentationModel<Order> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    @JsonView(View.UI.class)
    private Long id;

    @Column(name = "user_id")
    @Min(value = 0, message = "Id user should be positive.")
    @JsonView(View.REST.class)
    private Long idUser;

    @Column(name = "certificate_id")
    @Min(value = 0, message = "Id certificate should be positive.")
    @JsonView(View.UI.class)
    private Long idCertificate;

    @Column(name = "price")
    @Min(value = 0, message = "Price should be positive.")
    @JsonView(View.UI.class)
    private Integer price;

    @Column(name = "payment_date")
    @NotBlank(message = "Payment date should not be empty.")
    @JsonView(View.UI.class)
    private String paymentDate;

    @Embedded
    private ModificationInformation modificationInformation = new ModificationInformation();

}
