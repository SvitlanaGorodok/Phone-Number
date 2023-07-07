package api.phonecontacts.controller;

import api.phonecontacts.exception.UserAlreadyExistException;
import api.phonecontacts.model.dto.UserDto;
import api.phonecontacts.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;
    @PostMapping("/auth")
    public ResponseEntity<String> getLogin(@Valid @RequestBody UserDto userDto, HttpServletRequest request) {
        String username = userDto.getLogin();
        String password = userDto.getPassword();
        try {
            request.login(username, password);
        } catch (ServletException e) {
            return new ResponseEntity<>("Bad credentials!", HttpStatus.NOT_FOUND);
        }
        log.info("User " + username + " login");
        return new ResponseEntity<>("You are signed in!", HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<String>  registration(@Valid @RequestBody UserDto userDto) {
        String message = "You are registered! Please login with your credentials!";
        try{
            service.registration(userDto);
        } catch (UserAlreadyExistException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        log.info("User " + userDto.getLogin() + " registered");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
