package api.phonecontacts.service;

import api.phonecontacts.exception.NoSuchEntityFoundException;
import api.phonecontacts.model.dao.PhoneNumber;
import api.phonecontacts.repository.PhoneNumberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class PhoneNumberServiceTest {
    @Mock
    PhoneNumberRepository repository;
    PhoneNumberService service;
    PhoneNumber phoneNumber;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        service = new PhoneNumberService(repository);
        phoneNumber = new PhoneNumber();
        phoneNumber.setId(UUID.randomUUID());
        phoneNumber.setNumber("+380981111111");
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void save() {
        when(repository.save(any(PhoneNumber.class))).thenAnswer(i -> i.getArguments()[0]);
        phoneNumber.setId(null);
        PhoneNumber saved = service.save(phoneNumber);

        assertNotNull(phoneNumber.getId());
        assertEquals(phoneNumber.getNumber(), saved.getNumber());
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(phoneNumber));
        PhoneNumber phoneNumberFomDB = (PhoneNumber) service.findAll().toArray()[0];

        assertEquals(phoneNumber.getId(), phoneNumberFomDB.getId());
        assertEquals(phoneNumber.getNumber(), phoneNumberFomDB.getNumber());
    }

    @Test
    void findById() {
        UUID id = phoneNumber.getId();
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(phoneNumber));
        PhoneNumber findById = service.findById(id);
        assertEquals(findById.getId(), id);
    }

    @Test
    void findByIdNoSuchEntityFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());
        NoSuchEntityFoundException exception = assertThrows(NoSuchEntityFoundException.class,
                () -> service.findById(id));
        assertEquals("Phone number with id " + id + "not found!", exception.getMessage());
    }

    @Test
    void deleteById() {
        UUID id = UUID.randomUUID();
        doNothing().when(repository).deleteById(any(UUID.class));
        service.deleteById(id);
        verify(repository,times(1)).deleteById(any(UUID.class));
    }

    @Test
    void findByNumber() {
        String numberValue = phoneNumber.getNumber();
        when(repository.findByNumber(numberValue)).thenReturn(Optional.of(phoneNumber));
        PhoneNumber findByNumber = service.findByNumber(numberValue).get();
        assertEquals(numberValue, findByNumber.getNumber());
    }

    @Test
    void setNumbersId() {
        when(repository.findByNumber(anyString())).thenReturn(Optional.of(phoneNumber));
        PhoneNumber result = (PhoneNumber) service.setNumbersId(Set.of(phoneNumber.getNumber())).toArray()[0];

        assertEquals(phoneNumber.getId(), result.getId());
        assertEquals(phoneNumber.getNumber(), result.getNumber());
    }
}