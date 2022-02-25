package epam.validator.impl;

import com.esm.epam.entity.Tag;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.validator.impl.ServiceTagValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

class ServiceTagValidatorImplTest {

    private ServiceTagValidatorImpl tagValidator = new ServiceTagValidatorImpl();
    private Tag tag = new Tag(1L, "tag_winter");

    @Test
    void validateEntity_resourceNotFoundException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            tagValidator.validateEntity(Optional.empty(), 1L);
        });
    }

    @Test
    void validateEntity_positive() throws ResourceNotFoundException {
        tagValidator.validateEntity(Optional.ofNullable(tag), 1L);
    }

    @Test
    void validateListIsNull_resourceNotFoundException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            tagValidator.validateListIsNull(Optional.empty());
        });
    }

    @Test
    void validateListIsEmpty_resourceNotFoundException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            tagValidator.validateListIsNull(Optional.empty());
        });
    }

    @Test
    void validateListIsNull_positive() throws ResourceNotFoundException {
        tagValidator.validateListIsNull(Optional.of(Arrays.asList(tag)));
    }

    @Test
    void validateListIsEmpty_positive() throws ResourceNotFoundException {
        tagValidator.validateListIsEmpty(Arrays.asList(tag));
    }

}