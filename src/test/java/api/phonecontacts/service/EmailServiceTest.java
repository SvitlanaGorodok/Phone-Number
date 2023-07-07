package api.phonecontacts.service;

import api.phonecontacts.exception.NoSuchEntityFoundException;
import api.phonecontacts.model.dao.Email;
import api.phonecontacts.repository.EmailRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


class EmailServiceTest {
    @Mock
    EmailRepository repository;
    EmailService service;
    Email email;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        service = new EmailService(repository);
        email = new Email();
        email.setId(UUID.randomUUID());
        email.setEmail("email@gmail.com");
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void save() {
        when(repository.save(any(Email.class))).thenAnswer(i -> i.getArguments()[0]);
        email.setId(null);
        Email saved = service.save(email);

        assertNotNull(email.getId());
        assertEquals(email.getEmail(), saved.getEmail());
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(email));
        Email emailFomDB = (Email) service.findAll().toArray()[0];

        assertEquals(email.getId(), emailFomDB.getId());
        assertEquals(email.getEmail(), emailFomDB.getEmail());
    }

    @Test
    void findById() {
        UUID id = email.getId();
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(email));
        Email findById = service.findById(id);
        assertEquals(findById.getId(), id);
    }

    @Test
    void findByIdNoSuchEntityFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());
        NoSuchEntityFoundException exception = assertThrows(NoSuchEntityFoundException.class,
                () -> service.findById(id));
        assertEquals("Email with id " + id + "not found!", exception.getMessage());
    }

    @Test
    void deleteById() {
        UUID id = UUID.randomUUID();
        doNothing().when(repository).deleteById(any(UUID.class));
        service.deleteById(id);
        verify(repository,times(1)).deleteById(any(UUID.class));
    }

    @Test
    void findByEmail() {
        String emailValue = email.getEmail();
        when(repository.findByEmail(emailValue)).thenReturn(Optional.of(email));
        Email findByEmail = service.findByEmail(emailValue).get();
        assertEquals(emailValue, findByEmail.getEmail());
    }

    @Test
    void setEmailsId() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(email));
        Email result = (Email) service.setEmailsId(Set.of(email.getEmail())).toArray()[0];

        assertEquals(email.getId(), result.getId());
        assertEquals(email.getEmail(), result.getEmail());
    }
}