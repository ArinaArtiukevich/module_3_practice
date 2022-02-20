package com.esm.epam.entity;

import com.esm.epam.entity.audit.ModificationInformation;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Tag extends RepresentationModel<Tag> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    @JsonView(View.UI.class)
    @NonNull
    private Long idTag;

    @Column(name = "tag_name", unique = true)
    @JsonView(View.UI.class)
    @NotBlank(message = "Tag name should not be empty.")
    @NonNull
    private String name;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "tags")
    @JsonView(View.REST.class)
    private List<Certificate> certificateList;

    @Embedded
    private ModificationInformation modificationInformation = new ModificationInformation();

}
