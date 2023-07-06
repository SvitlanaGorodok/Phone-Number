package api.phonecontacts.service;

import api.phonecontacts.exception.NoSuchEntityFoundException;
import api.phonecontacts.model.dao.ContactDao;
import api.phonecontacts.model.dto.ContactDto;
import api.phonecontacts.model.dto.UserDto;
import api.phonecontacts.model.mapper.EntityMapper;
import api.phonecontacts.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ContactService implements CrudService<ContactDto> {
    private final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private final String PHONE_NUMBER_REGEX = "^\\+\\d{10,12}$";
    private final ContactRepository repository;
    private final EmailService emailService;
    private final PhoneNumberService phoneNumberService;
    private final EntityMapper mapper;

    @Override
    public ContactDto save(ContactDto contactDto) {
        ContactDao toSave = mapper.contactToDao(contactDto);
        toSave.setEmails(emailService.setEmailsId(contactDto.getEmails()));
        toSave.setPhoneNumbers(phoneNumberService.setNumbersId(contactDto.getPhoneNumbers()));
        toSave.setUser(mapper.userToDao(contactDto.getUser()));
        ContactDao saved = repository.save(toSave);
        return mapper.contactToDto(saved);
    }

    @Override
    public Set<ContactDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::contactToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public ContactDto findById(UUID id) {
        ContactDao contactDao = repository.findById(id)
                .orElseThrow(() -> new NoSuchEntityFoundException("Contact with id " + id + "not found!"));
        return mapper.contactToDto(contactDao);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public String addContact(ContactDto contactDto, UUID userId) {
        if (!isEmailsValid(contactDto.getEmails())) {
            return "Please provide valid emails!";
        }
        if (!isPhoneNumbersValid(contactDto.getPhoneNumbers())) {
            return "Please provide valid phone numbers!";
        }
        if (findByNameAndUserId(contactDto.getName(), userId).isEmpty()) {
            contactDto.setId(UUID.randomUUID());
            UserDto userDto = new UserDto();
            userDto.setId(userId);
            contactDto.setUser(userDto);
            Set<ContactDto> userContacts = findAllByUserId(userId);
            if (validateIfEmailAndNumberIsExist(contactDto, userContacts)) {
                return "Email and phone number should be unique!";
            }
            save(contactDto);
            return "Contact successfully added!";
        }
        return "Contact already exist!";
    }

    public String update(ContactDto contactDto, UUID userId) {
        if (!isEmailsValid(contactDto.getEmails())) {
            return "Please provide valid emails!";
        }
        if (!isPhoneNumbersValid(contactDto.getPhoneNumbers())) {
            return "Please provide valid phone numbers!";
        }
        Optional<ContactDto> toUpdate = findByNameAndUserId(contactDto.getName(), userId);
        if (toUpdate.isPresent()) {
            UserDto userDto = new UserDto();
            userDto.setId(userId);
            toUpdate.get().setUser(userDto);
            toUpdate.get().setEmails(contactDto.getEmails());
            toUpdate.get().setPhoneNumbers(contactDto.getPhoneNumbers());
            Set<ContactDto> userContacts = findAllByUserId(userId).stream()
                    .filter(c -> !c.getName().equals(contactDto.getName()))
                    .collect(Collectors.toSet());
            if (validateIfEmailAndNumberIsExist(toUpdate.get(), userContacts)) {
                return "Email and phone number should be unique!";
            }
            save(toUpdate.get());
            return "Contact successfully updated!";
        }
        return "Contact not found!";
    }

    public String deleteByName(String name, UUID userId) {
        Optional<ContactDto> found = findByNameAndUserId(name, userId);
        if (found.isPresent()) {
            repository.deleteById(found.get().getId());
            return "Contact successfully deleted!";
        } else {
            return "Contact not found!";
        }
    }

    private Optional<ContactDto> findByNameAndUserId(String name, UUID userId) {
        List<ContactDao> contactDaos = repository.findByNameAndUserId(name, userId);
        if (contactDaos.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(mapper.contactToDto(contactDaos.get(0)));
        }
    }

    public Set<ContactDto> findAllByUserId(UUID userId) {
        return findAll().stream()
                .filter(contact -> contact.getUser().getId().equals(userId))
                .collect(Collectors.toSet());
    }

    private boolean validateIfEmailAndNumberIsExist(ContactDto contactDto, Set<ContactDto> userContacts) {
        return userContacts.stream()
                .anyMatch(contact ->
                        !Collections.disjoint(contact.getEmails(), contactDto.getEmails()) ||
                                !Collections.disjoint(contact.getPhoneNumbers(), contactDto.getPhoneNumbers()));
    }

    private boolean isEmailsValid(Set<String> emails) {
        return emails.stream()
                .allMatch(e -> isValid(e, EMAIL_REGEX));
    }

    private boolean isPhoneNumbersValid(Set<String> phoneNumbers) {
        return phoneNumbers.stream()
                .allMatch(e -> isValid(e, PHONE_NUMBER_REGEX));
    }

    private boolean isValid(String value, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(value)
                .matches();
    }

}
