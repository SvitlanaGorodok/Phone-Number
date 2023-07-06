package api.phonecontacts.controller;

import api.phonecontacts.model.dto.ContactDto;
import api.phonecontacts.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/contacts")
public class ContactController {
    private final ContactService service;

    @GetMapping
    public ResponseEntity<Set<ContactDto>> findAll() {
        UUID userId = UUID.fromString("4e3c27be-76de-496a-bed2-fb2dcb71ab7a");
        return new ResponseEntity<>(service.findAllByUserId(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addContact(@Valid @RequestBody ContactDto contactDto)  {
        UUID userId = UUID.fromString("4e3c27be-76de-496a-bed2-fb2dcb71ab7a");
        return new ResponseEntity<>(service.addContact(contactDto, userId), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String > updateContact(@Valid @RequestBody ContactDto contactDto)  {
        UUID userId = UUID.fromString("4e3c27be-76de-496a-bed2-fb2dcb71ab7a");
        return new ResponseEntity<>(service.update(contactDto, userId), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteContact(@RequestParam String name) {
        UUID userId = UUID.fromString("4e3c27be-76de-496a-bed2-fb2dcb71ab7a");
        return new ResponseEntity<>(service.deleteByName(name, userId), HttpStatus.OK);
    }
}
