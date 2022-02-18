package com.esm.epam.service.impl;

import com.esm.epam.entity.Certificate;
import com.esm.epam.entity.Tag;
import com.esm.epam.entity.User;
import com.esm.epam.exception.DaoException;
import com.esm.epam.exception.ResourceNotFoundException;
import com.esm.epam.exception.ServiceException;
import com.esm.epam.repository.impl.CertificateDaoImpl;
import com.esm.epam.repository.impl.OrderDaoImpl;
import com.esm.epam.repository.impl.UserDaoImpl;
import com.esm.epam.util.CurrentDate;
import com.esm.epam.validator.impl.ServiceUserValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    private final List<User> users = Arrays.asList(
            User.builder().id(1L).login("arina").budget(100).certificates(Arrays.asList(Certificate.builder()
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
                    .build(),
            User.builder().id(2L).login("viktor").budget(1020).certificates(Arrays.asList(
                            Certificate.builder()
                                    .id(2L)
                                    .name("hockey")
                                    .description("sport")
                                    .price(120)
                                    .duration(62)
                                    .tags(Collections.singletonList(new Tag(2L, "tag_name")))
                                    .build(),
                            Certificate.builder()
                                    .id(3L)
                                    .name("snowboarding")
                                    .description("snowboarding school")
                                    .price(10)
                                    .duration(12)
                                    .build()))
                    .build()
    );

    private final Certificate certificate = Certificate.builder()
            .id(4L)
            .name("skiing")
            .description("skiing in alps")
            .price(200)
            .duration(100)
            .build();
    private final User userToBeUpdated = User.builder().certificates(Arrays.asList(certificate)).build();

    @Mock
    private UserDaoImpl userDao = Mockito.mock(UserDaoImpl.class);

    @Mock
    private OrderDaoImpl orderDao = Mockito.mock(OrderDaoImpl.class);

    @Mock
    private CertificateDaoImpl certificateDao = Mockito.mock(CertificateDaoImpl.class);

    @Mock
    private ServiceUserValidatorImpl validator = Mockito.mock(ServiceUserValidatorImpl.class);

    @Mock
    private CurrentDate date = new CurrentDate();

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAll_positive() throws ResourceNotFoundException {
        when(userDao.getAll(0, 1000)).thenReturn(Optional.of(users));
        List<User> actualUsers = userService.getAll(0, 1000);
        assertEquals(users, actualUsers);
    }

    @Test
    void getById() throws DaoException, ResourceNotFoundException {
        User expectedUser = users.get(1);
        when(userDao.getById(1L)).thenReturn(Optional.ofNullable(users.get(1)));
        User actualUser = userService.getById(1L);
        assertEquals(expectedUser, actualUser);

    }

    @Test
    void update() throws DaoException, ServiceException, ResourceNotFoundException {
        User expectedUser = User.builder().id(2L).login("viktor").budget(820).certificates(Arrays.asList(
                        Certificate.builder()
                                .id(2L)
                                .name("hockey")
                                .description("sport")
                                .price(120)
                                .duration(62)
                                .tags(Collections.singletonList(new Tag(2L, "tag_name")))
                                .build(),
                        Certificate.builder()
                                .id(3L)
                                .name("snowboarding")
                                .description("snowboarding school")
                                .price(10)
                                .duration(12)
                                .build(),
                        Certificate.builder()
                                .id(4L)
                                .name("skiing")
                                .description("skiing in alps")
                                .price(200)
                                .duration(100)
                                .build()))
                .build();

        when(userDao.getById(2L)).thenReturn(Optional.ofNullable(users.get(1)));
        when(userDao.updateBudget(2L, 820)).thenReturn(Optional.ofNullable(expectedUser));
        when(certificateDao.getById(4L)).thenReturn(Optional.ofNullable(certificate));
        Optional<User> actualUser = userService.update(userToBeUpdated, 2L);
        assertEquals(expectedUser, actualUser.get());
    }

    @Test
    void getMostWidelyUsedTag() {
        Tag expectedTag = new Tag(2L, "tag_name");
        doReturn(Optional.of(expectedTag)).when(userDao).getMostWidelyUsedTag();
        Optional<Tag> actualTag = userService.getMostWidelyUsedTag();
        assertEquals(expectedTag, actualTag.get());
    }
}