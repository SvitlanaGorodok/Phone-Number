package api.phonecontacts.controller;

import api.phonecontacts.model.dto.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @PostMapping("/auth")
    public ResponseEntity<String> getLogin(@Valid @RequestBody UserDto userDto, HttpServletRequest request) {
        String username = userDto.getLogin();
        String password = userDto.getPassword();
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("You are login!", HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<String>  registration(@Valid @RequestBody UserDto userDto, HttpServletRequest request) {
        String username = userDto.getLogin();
        String password = userDto.getPassword();

        return new ResponseEntity<>("You are registered! Please login with your credentials!", HttpStatus.OK);
    }
}
