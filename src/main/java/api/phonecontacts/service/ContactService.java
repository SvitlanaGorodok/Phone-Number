package api.phonecontacts.service;

import api.phonecontacts.exception.NoSuchEntityFoundException;
import api.phonecontacts.model.dao.ContactDao;
import api.phonecontacts.model.dto.ContactDto;
import api.phonecontacts.model.mapper.EntityMapper;
import api.phonecontacts.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ContactService implements CrudService<ContactDto>{
    private final ContactRepository repository;
    private final EntityMapper mapper;

    @Override
    public ContactDto save(ContactDto contactDto) {
        if (contactDto.getId() == null) {
            contactDto.setId(UUID.randomUUID());
        }
        ContactDao contactDao = repository.save(mapper.contactToDao(contactDto));
        return mapper.contactToDto(contactDao);
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
}
