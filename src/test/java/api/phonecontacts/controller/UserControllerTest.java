package api.phonecontacts.controller;

import api.phonecontacts.exception.NoSuchEntityFoundException;
import api.phonecontacts.exception.UserAlreadyExistException;
import api.phonecontacts.model.dto.UserDto;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private final static String REGISTRATION_URL = "/registration";
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

    private MockMvc mvc;

    private UserDto user;

    private Gson gson;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        user = new UserDto(UUID.randomUUID(), "login", "password", new HashSet<>());
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void registration() throws Exception {
        String message = "You are registered! Please login with your credentials!";

        when(userService.getCurrentUserId()).thenReturn(UUID.randomUUID());

        mvc.perform(post(REGISTRATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }

    @Test
    void registrationWithExistingLogin() throws Exception {
        String login = user.getLogin();
        String exceptionMessage = "User with login " + login + "is already exist!";

        doThrow(new UserAlreadyExistException(exceptionMessage)).when(userService)
                .registration(any(UserDto.class));

        mvc.perform(post(REGISTRATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionMessage));

    }
}