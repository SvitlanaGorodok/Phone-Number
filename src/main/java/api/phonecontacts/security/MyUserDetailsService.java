package api.phonecontacts.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        String encodedPassword = passwordEncoder.encode("admin");
        User user = new User("admin", encodedPassword, "ROLE_ADMIN");
        return new UserPrincipal(user);
    }
}
