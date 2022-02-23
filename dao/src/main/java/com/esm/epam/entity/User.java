package com.esm.epam.entity;

import com.esm.epam.entity.audit.ModificationInformation;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends RepresentationModel<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonView(View.UI.class)
    private long id;

    @Column(name = "user_login", unique = true)
    @JsonView(View.UI.class)
    @NotBlank(message = "Login should not be empty.")
    private String login;

    @Column(name = "user_budget")
    @JsonView(View.UI.class)
    @NotNull(message = "Budget should not be null.")
    @Min(value = 0, message = "Budget should be positive.")
    private int budget;

    @ManyToMany(cascade = {
            MERGE
    })
    @JoinTable(name = "orders",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "certificate_id")})
    @NotEmpty
    @JsonView(View.UI.class)
    private List<Certificate> certificates;

    @Embedded
    private ModificationInformation modificationInformation = new ModificationInformation();

}