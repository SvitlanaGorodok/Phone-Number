package api.phonecontacts.controller;

import api.phonecontacts.model.dto.ContactDto;
import api.phonecontacts.service.ContactService;
import api.phonecontacts.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/contacts")
@Slf4j
public class ContactController {
    private final ContactService service;

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Set<ContactDto>> findAll() {
        UUID userId = userService.getCurrentUserId();
        Set<ContactDto> contacts = service.findAllByUserId(userId);
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addContact(@Valid @RequestBody ContactDto contactDto)  {
        UUID userId = userService.getCurrentUserId();
        String message = service.addContact(contactDto, userId);
        log.info("Handling save contact: " + contactDto.getId());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String > updateContact(@Valid @RequestBody ContactDto contactDto)  {
        UUID userId = userService.getCurrentUserId();
        String message = service.update(contactDto, userId);
        log.info("Handling update contact: " + contactDto.getId());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteContact(@RequestParam String name) {
        UUID userId = userService.getCurrentUserId();
        String message = service.deleteByName(name, userId);
        log.info("Handling delete contact: " + name);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
