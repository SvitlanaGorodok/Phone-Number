package api.phonecontacts.service;

import api.phonecontacts.exception.NoSuchEntityFoundException;
import api.phonecontacts.exception.UserAlreadyExistException;
import api.phonecontacts.model.dao.UserDao;
import api.phonecontacts.model.dto.UserDto;
import api.phonecontacts.model.mapper.EntityMapper;
import api.phonecontacts.model.mapper.EntityMapperImpl;
import api.phonecontacts.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class UserServiceTest {

    @Mock
    UserRepository repository;

    UserService service;

    BCryptPasswordEncoder encoder;
    EntityMapper mapper;
    UserDto userDto;
    UserDao userDao;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        encoder = new BCryptPasswordEncoder(12);
        mapper = new EntityMapperImpl();
        service = new UserService(repository, mapper, encoder);
        userDto = new UserDto(UUID.randomUUID(), "login", "password", new HashSet<>());
        userDao = mapper.userToDao(userDto);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void save() {
        when(repository.save(any(UserDao.class))).thenAnswer(i -> i.getArguments()[0]);
        userDto.setId(null);
        UserDto saved = service.save(userDto);

        assertNotNull(userDto.getId());
        assertEquals(userDto.getLogin(), saved.getLogin());
        assertEquals(userDto.getPassword(), saved.getPassword());
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(userDao));
        UserDto userFomDB = (UserDto) service.findAll().toArray()[0];

        assertEquals(userDto.getId(), userFomDB.getId());
        assertEquals(userDto.getLogin(), userFomDB.getLogin());
        assertEquals(userDto.getPassword(), userFomDB.getPassword());

    }

    @Test
    void findById() {
        UUID id = userDto.getId();
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(userDao));
        UserDto findById = service.findById(id);
        assertEquals(findById.getId(), id);
    }

    @Test
    void findByIdNoSuchEntityFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());
        NoSuchEntityFoundException exception = assertThrows(NoSuchEntityFoundException.class,
                () -> service.findById(id));
        assertEquals("User with id " + id + "not found!", exception.getMessage());
    }

    @Test
    void deleteById() {
        UUID id = UUID.randomUUID();
        doNothing().when(repository).deleteById(any(UUID.class));
        service.deleteById(id);
        verify(repository,times(1)).deleteById(any(UUID.class));
    }

    @Test
    void findByLogin() {
        String login = userDto.getLogin();
        when(repository.findByLogin(login)).thenReturn(Optional.of(userDao));
        UserDto userDto = service.findByLogin(login).get();
        assertEquals(login, userDto.getLogin());
    }

    @Test
    void registration() {
        when(repository.save(any(UserDao.class))).thenReturn(userDao);
        when(repository.findByLogin(anyString())).thenReturn(Optional.empty());
        String rawPassword = userDto.getPassword();
        service.registration(userDto);
        assertThat(encoder.matches(rawPassword, userDto.getPassword())).isTrue();
    }

    @Test
    void registrationUserAlreadyExistException() {
        when(repository.findByLogin(anyString())).thenReturn(Optional.of(userDao));
        UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class,
                () -> service.registration(userDto));
        assertEquals("User with login " + userDto.getLogin() + "is already exist!", exception.getMessage());
    }
}