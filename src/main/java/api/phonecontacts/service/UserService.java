package api.phonecontacts.service;

import api.phonecontacts.exception.NoSuchEntityFoundException;
import api.phonecontacts.model.dao.UserDao;
import api.phonecontacts.model.dto.UserDto;
import api.phonecontacts.model.mapper.EntityMapper;
import api.phonecontacts.repository.UserRepository;
import api.phonecontacts.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService implements CrudService<UserDto>{
    private final UserRepository repository;
    private final EntityMapper mapper;
    private final BCryptPasswordEncoder encoder;

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

    public UserDto findByLogin(String login){
        UserDao userDao = repository.findByLogin(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("User with username %s not found!", login)));
        return mapper.userToDto(userDao);
    }

    public UserDto registration(UserDto userDto){
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        return save(userDto);
    }

    public UUID getCurrentUserId(){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        UserDto user = findByLogin(userPrincipal.getUsername());
        return user.getId();
    }
}
