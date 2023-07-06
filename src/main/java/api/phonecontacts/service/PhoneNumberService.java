package api.phonecontacts.service;

import api.phonecontacts.exception.NoSuchEntityFoundException;
import api.phonecontacts.model.dao.Email;
import api.phonecontacts.model.dao.PhoneNumber;
import api.phonecontacts.repository.PhoneNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PhoneNumberService implements CrudService<PhoneNumber> {
    private final PhoneNumberRepository repository;
    @Override
    public PhoneNumber save(PhoneNumber phoneNumber) {
        if (phoneNumber.getId() == null) {
            phoneNumber.setId(UUID.randomUUID());
        }
        return repository.save(phoneNumber);
    }

    @Override
    public Set<PhoneNumber> findAll() {
        return new HashSet<>(repository.findAll());
    }

    @Override
    public PhoneNumber findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchEntityFoundException("Phone number with id " + id + "not found!"));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public Optional<PhoneNumber> findByNumber(String number) {
        return repository.findByNumber(number);
    }

    public Set<PhoneNumber> setNumbersId(Set<String> numbers){
        Set<PhoneNumber> result = new HashSet<>();

        for (String number : numbers) {
            Optional<PhoneNumber> findNumber = findByNumber(number);
            if (findNumber.isEmpty()){
                PhoneNumber toSave = new PhoneNumber();
                toSave.setNumber(number);
                PhoneNumber saved = save(toSave);
                result.add(saved);
            } else {
                result.add(findNumber.get());
            }
        }

        return result;
    }
}
