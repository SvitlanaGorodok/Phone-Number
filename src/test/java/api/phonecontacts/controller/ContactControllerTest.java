package api.phonecontacts.controller;

import api.phonecontacts.exception.InvalidFormatException;
import api.phonecontacts.exception.NoSuchEntityFoundException;
import api.phonecontacts.exception.NonUniqueDataException;
import api.phonecontacts.model.dto.ContactDto;
import api.phonecontacts.model.dto.UserDto;
import api.phonecontacts.service.ContactService;
import api.phonecontacts.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class ContactControllerTest {
    private final static String CONTACTS_URL = "/contacts";

    @Mock
    private ContactService contactService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ContactController contactController;

    private MockMvc mvc;

    private ContactDto contact;

    private Gson gson;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(contactController).build();

        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        contact = new ContactDto(UUID.randomUUID(), "Name",
                Set.of("email1@gmail.com", "email2@gmail.com"), Set.of("+380981111111", "+380982222222"),
                new UserDto(UUID.randomUUID()));
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void findAll() throws Exception {
        when(userService.getCurrentUserId()).thenReturn(UUID.randomUUID());
        when(contactService.findAllByUserId(any(UUID.class))).thenReturn(Set.of(contact));

        mvc.perform(get(CONTACTS_URL))
                .andExpect(status().isOk())
                .andExpect(content().string(gson.toJson(Set.of(contact))));
    }

    @Test
    void addContactSuccessful() throws Exception {
        String message = "Contact successfully added!";

        when(userService.getCurrentUserId()).thenReturn(UUID.randomUUID());
        when(contactService.addContact(any(ContactDto.class), any(UUID.class))).thenReturn(contact);

        mvc.perform(post(CONTACTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(contact)))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }

    @Test
    void addNonUniqueContact() throws Exception {
        String exceptionMessage = "Non unique contact!";
        when(userService.getCurrentUserId()).thenReturn(UUID.randomUUID());
        when(contactService.addContact(any(ContactDto.class), any(UUID.class)))
                .thenThrow(new NonUniqueDataException(exceptionMessage));

        mvc.perform(post(CONTACTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(contact)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionMessage));
    }

    @Test
    void addNonValidData() throws Exception {
        String exceptionMessage = "Invalid data!";
        when(userService.getCurrentUserId()).thenReturn(UUID.randomUUID());
        when(contactService.addContact(any(ContactDto.class), any(UUID.class)))
                .thenThrow(new InvalidFormatException(exceptionMessage));

        mvc.perform(post(CONTACTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(contact)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionMessage));
    }

    @Test
    void updateContactSuccessful() throws Exception {
        String message = "Contact successfully updated!";

        when(userService.getCurrentUserId()).thenReturn(UUID.randomUUID());
        when(contactService.updateContact(any(ContactDto.class), any(UUID.class))).thenReturn(contact);

        mvc.perform(put(CONTACTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(contact)))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }

    @Test
    void updateNotExistingContact() throws Exception {
        String exceptionMessage = "Contact not found!";
        when(userService.getCurrentUserId()).thenReturn(UUID.randomUUID());
        when(contactService.updateContact(any(ContactDto.class), any(UUID.class)))
                .thenThrow(new NoSuchEntityFoundException(exceptionMessage));

        mvc.perform(put(CONTACTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(contact)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionMessage));
    }

    @Test
    void updateWithNonValidData() throws Exception {
        String exceptionMessage = "Invalid data!";
        when(userService.getCurrentUserId()).thenReturn(UUID.randomUUID());
        when(contactService.updateContact(any(ContactDto.class), any(UUID.class)))
                .thenThrow(new InvalidFormatException(exceptionMessage));

        mvc.perform(put(CONTACTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(contact)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionMessage));
    }

    @Test
    void deleteContactSuccessful() throws Exception {
        String message = "Contact successfully deleted!";
        when(userService.getCurrentUserId()).thenReturn(UUID.randomUUID());
        doNothing().when(contactService).deleteByName(anyString(), any(UUID.class));

        mvc.perform(delete(CONTACTS_URL)
                        .param("name", contact.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(message));

        verify(contactService, times(1)).deleteByName(anyString(), any(UUID.class));
    }

    @Test
    void deleteNotExistingContact() throws Exception {
        String exceptionMessage = "Contact not found!";
        when(userService.getCurrentUserId()).thenReturn(UUID.randomUUID());
        doThrow(new NoSuchEntityFoundException(exceptionMessage))
                .when(contactService).deleteByName(anyString(), any(UUID.class));

        mvc.perform(delete(CONTACTS_URL)
                        .param("name", contact.getName()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionMessage));
    }
}