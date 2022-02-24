package com.esm.epam.entity;

import com.esm.epam.entity.audit.ModificationInformation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    @NonNull
    private Long id;

    @Column(name = "tag_name", unique = true)
    @NotBlank(message = "Tag name should not be empty.")
    @NonNull
    private String name;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "tags")
    private List<Certificate> certificateList;

    @Embedded
    private ModificationInformation modificationInformation = new ModificationInformation();

}
