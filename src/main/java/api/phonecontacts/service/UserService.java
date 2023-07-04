package api.phonecontacts.service;

import api.phonecontacts.exception.NoSuchEntityFoundException;
import api.phonecontacts.model.dao.UserDao;
import api.phonecontacts.model.dto.UserDto;
import api.phonecontacts.model.mapper.EntityMapper;
import api.phonecontacts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService implements CrudService<UserDto>{
    private final UserRepository repository;
    private final EntityMapper mapper;

    @Override
    public UserDto save(UserDto userDto) {
        if (userDto.getId() == null) {
            userDto.setId(UUID.randomUUID());
        }
        UserDao userDao = repository.save(mapper.userToDao(userDto));
        return mapper.userToDto(userDao);
    }

    @Override
    public Set<UserDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::userToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public UserDto findById(UUID id) {
        UserDao userDao = repository.findById(id)
                .orElseThrow(() -> new NoSuchEntityFoundException("User with id " + id + "not found!"));
        return mapper.userToDto(userDao);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
