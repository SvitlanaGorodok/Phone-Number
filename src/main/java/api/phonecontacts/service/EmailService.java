package api.phonecontacts.service;

import api.phonecontacts.exception.NoSuchEntityFoundException;
import api.phonecontacts.model.dao.Email;
import api.phonecontacts.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmailService implements CrudService<Email> {
    private final EmailRepository repository;
    @Override
    public Email save(Email email) {
        if (email.getId() == null) {
            email.setId(UUID.randomUUID());
        }
        return repository.save(email);
    }

    @Override
    public Set<Email> findAll() {
        return new HashSet<>(repository.findAll());
    }

    @Override
    public Email findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchEntityFoundException("Email with id " + id + "not found!"));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public Optional<Email> findByEmail(String emailName) {
        return repository.findByEmail(emailName);
    }

    public Set<Email> setEmailsId(Set<String> emails){
        Set<Email> result = new HashSet<>();

        for (String email : emails) {
            Optional<Email> findEmail = findByEmail(email);
            if (findEmail.isEmpty()){
                Email toSave = new Email();
                toSave.setEmail(email);
                Email saved = save(toSave);
                result.add(saved);
            } else {
                result.add(findEmail.get());
            }
        }

        return result;
    }
}
