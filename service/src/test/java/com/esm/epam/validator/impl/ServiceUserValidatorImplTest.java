package com.esm.epam.validator.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;
import com.esm.epam.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceUserValidatorImpl.class)
class ServiceUserValidatorImplTest {
    private final User user = User.builder().id(1L).login("arina").budget(100).certificates(Arrays.asList(Certificate.builder()
                                    .id(1L)
                                    .name("sneakers")
                                    .description("clothing and presents")
                                    .price(200)
                                    .duration(1)
                                    .tags(Arrays.asList(new Tag(1L, "tag_paper"), new Tag(2L, "tag_name")))
            .build(),
                            Certificate.builder()
                                    .id(2L)
                                    .name("hockey")
                                    .description("sport")
                                    .price(120)
                                    .duration(62)
                                    .tags(Collections.singletonList(new Tag(2L, "tag_name")))
            .build()))
            .build();


    @Autowired
    private ServiceUserValidatorImpl userValidator;

    @Test
    void validateUser_resourceNotFoundException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userValidator.validateEntity(Optional.empty(), 1L);
        });
    }

    @Test
    void validateUser_positive() throws ResourceNotFoundException {
        userValidator.validateEntity(Optional.ofNullable(user), 1L);
    }

}
