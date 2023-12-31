package api.phonecontacts.security;

import api.phonecontacts.exception.NoSuchEntityFoundException;
import api.phonecontacts.model.dto.UserDto;
import api.phonecontacts.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDto user = service.findByLogin(username)
                .orElseThrow(() -> new NoSuchEntityFoundException("User with username " + username + " not found!"));
        return new UserPrincipal(user);
    }
}
