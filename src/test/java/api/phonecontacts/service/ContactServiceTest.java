package api.phonecontacts.service;

import api.phonecontacts.exception.InvalidFormatException;
import api.phonecontacts.exception.NoSuchEntityFoundException;
import api.phonecontacts.exception.NonUniqueDataException;
import api.phonecontacts.model.dao.ContactDao;
import api.phonecontacts.model.dto.ContactDto;
import api.phonecontacts.model.dto.UserDto;
import api.phonecontacts.model.mapper.EntityMapper;
import api.phonecontacts.model.mapper.EntityMapperImpl;
import api.phonecontacts.repository.ContactRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

import java.util.*;

class ContactServiceTest {
    @Mock
    ContactRepository repository;
    @Mock
    EmailService emailService;
    @Mock
    PhoneNumberService phoneNumberService;

    ContactService service;
    EntityMapper mapper;

    private ContactDto contactDto;
    private ContactDao contactDao;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mapper = new EntityMapperImpl();
        service = new ContactService(repository, emailService, phoneNumberService, mapper);
        contactDto = new ContactDto(UUID.randomUUID(), "Name",
                Set.of("email1@gmail.com", "email2@gmail.com"), Set.of("+380981111111", "+380982222222"),
                new UserDto(UUID.randomUUID()));
        contactDao = mapper.contactToDao(contactDto);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void save() {
        when(emailService.setEmailsId(anySet())).thenReturn(contactDao.getEmails());
        when(phoneNumberService.setNumbersId(anySet())).thenReturn(contactDao.getPhoneNumbers());
        when(repository.save(any(ContactDao.class))).thenAnswer(i -> i.getArguments()[0]);

        ContactDto saved = service.save(contactDto);

        assertEquals(saved.getId(), contactDto.getId());
        assertEquals(saved.getName(), contactDto.getName());
        assertEquals(saved.getUser().getId(), contactDto.getUser().getId());
        assertEquals(saved.getEmails(), contactDto.getEmails());
        assertEquals(saved.getPhoneNumbers(), contactDto.getPhoneNumbers());
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(contactDao));
        ContactDto contactFromDB = (ContactDto) service.findAll().toArray()[0];
        assertEquals(contactFromDB.getId(), contactDto.getId());
    }

    @Test
    void findAllByUserId() {
        when(repository.findAll()).thenReturn(List.of(contactDao));
        ContactDto contactFromDB = (ContactDto) service.findAllByUserId(contactDto.getUser().getId()).toArray()[0];
        assertEquals(contactFromDB.getId(), contactDto.getId());
    }

    @Test
    void findById() {
        UUID contactId = contactDto.getId();
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(contactDao));
        ContactDto findById = service.findById(contactId);
        assertEquals(findById.getId(), contactId);
    }

    @Test
    void findByIdNoSuchEntityFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());
        NoSuchEntityFoundException exception = assertThrows(NoSuchEntityFoundException.class,
                () -> service.findById(id));
        assertEquals("Contact with id " + id + "not found!", exception.getMessage());
    }

    @Test
    void deleteById() {
        doNothing().when(repository).deleteById(any(UUID.class));
        service.deleteById(UUID.randomUUID());
        verify(repository,times(1)).deleteById(any(UUID.class));
    }

    @Test
    void addContactSuccessful() {
        when(repository.findByNameAndUserId(anyString(), any(UUID.class))).thenReturn(Collections.emptyList());
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(emailService.setEmailsId(anySet())).thenReturn(contactDao.getEmails());
        when(phoneNumberService.setNumbersId(anySet())).thenReturn(contactDao.getPhoneNumbers());
        when(repository.save(any(ContactDao.class))).thenAnswer(i -> i.getArguments()[0]);
        contactDto.setId(null);
        service.addContact(contactDto, UUID.randomUUID());
        assertNotNull(contactDto.getId());
    }

    @Test
    void addExistingContact() {
        when(repository.findByNameAndUserId(anyString(), any(UUID.class))).thenReturn(List.of(contactDao));
        NonUniqueDataException exception = assertThrows(NonUniqueDataException.class,
                () -> service.addContact(contactDto, contactDto.getUser().getId()));
        assertEquals("Contact already exist!", exception.getMessage());
    }

    @Test
    void addExistingData() {
        when(repository.findByNameAndUserId(anyString(), any(UUID.class))).thenReturn(Collections.emptyList());
        when(repository.findAll()).thenReturn(List.of(contactDao));
        NonUniqueDataException exception = assertThrows(NonUniqueDataException.class,
                () -> service.addContact(contactDto, contactDto.getUser().getId()));
        assertEquals("Email and phone number should be unique!", exception.getMessage());
    }

    @Test
    void addInvalidEmail() {
        contactDto.setEmails(Set.of("email"));
        InvalidFormatException exception = assertThrows(InvalidFormatException.class,
                () -> service.addContact(contactDto, contactDto.getUser().getId()));
        assertEquals("Please provide valid emails!", exception.getMessage());
    }

    @Test
    void addInvalidPhoneNumber() {
        contactDto.setPhoneNumbers(Set.of("+3800a"));
        InvalidFormatException exception = assertThrows(InvalidFormatException.class,
                () -> service.addContact(contactDto, contactDto.getUser().getId()));
        assertEquals("Please provide valid phone numbers!", exception.getMessage());
    }

    @Test
    void updateContactSuccessful() {
        Set<String> newEmails = Set.of("email@gmail.com");
        Set<String> newPhoneNumbers = Set.of("+380980000000");
        contactDto.setEmails(newEmails);
        contactDto.setPhoneNumbers(newPhoneNumbers);
        contactDao = mapper.contactToDao(contactDto);


        when(repository.findByNameAndUserId(anyString(), any(UUID.class))).thenReturn(List.of(contactDao));
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(emailService.setEmailsId(anySet())).thenReturn(contactDao.getEmails());
        when(phoneNumberService.setNumbersId(anySet())).thenReturn(contactDao.getPhoneNumbers());
        when(repository.save(any(ContactDao.class))).thenAnswer(i -> i.getArguments()[0]);

        ContactDto updated = service.updateContact(contactDto, UUID.randomUUID());

        assertEquals(contactDto.getEmails(), updated.getEmails());
        assertEquals(contactDto.getPhoneNumbers(), updated.getPhoneNumbers());
    }

    @Test
    void updateContactNotFound() {
        when(repository.findByNameAndUserId(anyString(), any(UUID.class))).thenReturn(Collections.emptyList());
        NoSuchEntityFoundException exception = assertThrows(NoSuchEntityFoundException.class,
                () -> service.updateContact(contactDto, contactDto.getUser().getId()));
        assertEquals("Contact with name " + contactDto.getName() + " not found!", exception.getMessage());
    }

    @Test
    void updateWithExistingData() {
        ContactDao contactWithSameData = new ContactDao();
        contactWithSameData.setName("another");
        contactWithSameData.setEmails(contactDao.getEmails());
        contactWithSameData.setPhoneNumbers(contactDao.getPhoneNumbers());
        contactWithSameData.setUser(contactDao.getUser());
        when(repository.findByNameAndUserId(anyString(), any(UUID.class)))
                .thenReturn(List.of(contactDao));
        when(repository.findAll()).thenReturn(List.of(contactWithSameData));

        NonUniqueDataException exception = assertThrows(NonUniqueDataException.class,
                () -> service.updateContact(contactDto, contactDto.getUser().getId()));
        assertEquals("Email and phone number should be unique!", exception.getMessage());
    }

    @Test
    void updateInvalidEmail() {
        contactDto.setEmails(Set.of("email"));
        InvalidFormatException exception = assertThrows(InvalidFormatException.class,
                () -> service.updateContact(contactDto, contactDto.getUser().getId()));
        assertEquals("Please provide valid emails!", exception.getMessage());
    }

    @Test
    void updateInvalidPhoneNumber() {
        contactDto.setPhoneNumbers(Set.of("+3800a"));
        InvalidFormatException exception = assertThrows(InvalidFormatException.class,
                () -> service.updateContact(contactDto, contactDto.getUser().getId()));
        assertEquals("Please provide valid phone numbers!", exception.getMessage());
    }

    @Test
    void deleteByNameSuccessful() {
        when(repository.findByNameAndUserId(anyString(), any(UUID.class))).thenReturn(List.of(contactDao));
        doNothing().when(repository).deleteById(any(UUID.class));
        service.deleteByName(contactDto.getName(), contactDto.getUser().getId());
        verify(repository,times(1)).deleteById(any(UUID.class));
    }

    @Test
    void deleteByNameNotFound() {
        when(repository.findByNameAndUserId(anyString(), any(UUID.class))).thenReturn(Collections.emptyList());
        NoSuchEntityFoundException exception = assertThrows(NoSuchEntityFoundException.class,
                () -> service.deleteByName(contactDto.getName(), contactDto.getUser().getId()));
        assertEquals("Contact with name " + contactDto.getName() + " not found!", exception.getMessage());
    }

}