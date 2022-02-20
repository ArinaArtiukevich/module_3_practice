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
import java.util.List;

import static javax.persistence.CascadeType.MERGE;

@Entity
@Table(name = "gift_certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate extends RepresentationModel<Certificate> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView(View.UI.class)
    private Long id;

    @Column(name = "name", unique = true)
    @JsonView(View.UI.class)
    private String name;

    @Column(name = "description")
    @JsonView(View.UI.class)
    private String description;

    @Column(name = "price")
    @JsonView(View.UI.class)
    @Min(value = 0, message = "Price should be positive.")
    private Integer price;

    @Column(name = "duration")
    @JsonView(View.UI.class)
    @Min(value = 0, message = "Duration should be positive.")
    private Integer duration;

    @Column(name = "creation_date")
    @JsonView(View.REST.class)
    private String createDate;

    @Column(name = "last_update_date")
    @JsonView(View.REST.class)
    private String lastUpdateDate;

    @ManyToMany(cascade = {
            MERGE
    })
    @JoinTable(name = "certificates_tags",
            joinColumns = {@JoinColumn(name = "certificate_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    @JsonView(View.UI.class)
    private List<Tag> tags;

    @ManyToMany(mappedBy = "certificates")
    @JsonView(View.REST.class)
    private List<User> userList;

    @Embedded
    private ModificationInformation modificationInformation = new ModificationInformation();
}