package api.phonecontacts.service;

import api.phonecontacts.exception.InvalidFormatException;
import api.phonecontacts.exception.NoSuchEntityFoundException;
import api.phonecontacts.exception.NonUniqueDataException;
import api.phonecontacts.model.dao.ContactDao;
import api.phonecontacts.model.dto.ContactDto;
import api.phonecontacts.model.dto.UserDto;
import api.phonecontacts.model.mapper.EntityMapper;
import api.phonecontacts.repository.ContactRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ContactService implements CrudService<ContactDto> {
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_NUMBER_REGEX = "^\\+\\d{10,12}$";
    private final ContactRepository repository;
    private final EmailService emailService;
    private final PhoneNumberService phoneNumberService;
    private final EntityMapper mapper;

    private Gson gson;

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

    public Set<ContactDto> findAllByUserId(UUID userId) {
        return findAll().stream()
                .filter(contact -> contact.getUser().getId().equals(userId))
                .collect(Collectors.toSet());
    }
    public ContactDto addContact(ContactDto contactDto, UUID userId) {
        isContactDataValid(contactDto);

        if (findByNameAndUserId(contactDto.getName(), userId).isEmpty()) {
            contactDto.setId(UUID.randomUUID());
            contactDto.setUser(new UserDto(userId));

            Set<ContactDto> userContacts = findAllByUserId(userId);
            validateIfContactDataUnique(contactDto, userContacts);

            return save(contactDto);
        } else {
            throw new NonUniqueDataException("Contact already exist!");
        }
    }

    public ContactDto updateContact(ContactDto contactDto, UUID userId) {
        isContactDataValid(contactDto);

        Optional<ContactDto> findByName = findByNameAndUserId(contactDto.getName(), userId);

        if (findByName.isPresent()) {
            ContactDto contactToUpdate = findByName.get();
            contactToUpdate.setUser(new UserDto(userId));
            contactToUpdate.setEmails(contactDto.getEmails());
            contactToUpdate.setPhoneNumbers(contactDto.getPhoneNumbers());

            Set<ContactDto> existingContacts = findAllByUserId(userId).stream()
                    .filter(c -> !c.getName().equals(contactDto.getName()))
                    .collect(Collectors.toSet());
            validateIfContactDataUnique(contactToUpdate, existingContacts);
            return save(contactToUpdate);
        } else {
            throw new NoSuchEntityFoundException("Contact with name " + contactDto.getName() + " not found!");
        }
    }

    public void deleteByName(String name, UUID userId) {
        Optional<ContactDto> findByName = findByNameAndUserId(name, userId);

        if (findByName.isPresent()) {
            repository.deleteById(findByName.get().getId());
        } else {
            throw new NoSuchEntityFoundException("Contact with name " + name + " not found!");
        }
    }

    public void exportContacts(UUID userId){
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try (PrintWriter out = new PrintWriter(new FileWriter("user_" + userId + "_contacts.json")))
        {
            out.write(gson.toJson(findAllByUserId(userId)));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private Optional<ContactDto> findByNameAndUserId(String name, UUID userId) {
        List<ContactDao> contacts = repository.findByNameAndUserId(name, userId);

        if (contacts.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(mapper.contactToDto(contacts.get(0)));
        }
    }

    private void isContactDataValid(ContactDto contactDto){
        if (!isEmailsValid(contactDto.getEmails())) {
            throw new InvalidFormatException("Please provide valid emails!");
        }

        if (!isPhoneNumbersValid(contactDto.getPhoneNumbers())) {
            throw new InvalidFormatException("Please provide valid phone numbers!");
        }
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

    private void validateIfContactDataUnique(ContactDto contactDto, Set<ContactDto> userContacts) {
        boolean isUnique = userContacts.stream()
                .allMatch(contact ->
                        Collections.disjoint(contact.getEmails(), contactDto.getEmails()) &&
                                Collections.disjoint(contact.getPhoneNumbers(), contactDto.getPhoneNumbers()));
        if(!isUnique){
            throw new NonUniqueDataException("Email and phone number should be unique!");
        }
    }


}
