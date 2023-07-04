package api.phonecontacts.controller;

import api.phonecontacts.model.dto.ContactDto;
import api.phonecontacts.model.mapper.EntityMapper;
import api.phonecontacts.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
        Set<ContactDto> contacts = service.findAll();
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ContactDto> addContact(@Validated @RequestBody ContactDto contactDto)  {
        ContactDto saved = service.save(contactDto);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ContactDto> updateContact(@Validated @RequestBody ContactDto contactDto)  {
        ContactDto saved = service.save(contactDto);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteContact(@RequestParam UUID id) {
        service.deleteById(id);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }
}
