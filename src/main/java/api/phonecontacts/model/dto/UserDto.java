package api.phonecontacts.model.dto;

import api.phonecontacts.model.dao.ContactDao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDto {
    @JsonIgnore
    private UUID id;

    @NotEmpty
    private String login;

    @NotEmpty
    private String password;

    @JsonIgnore
    private Set<ContactDao> contacts = new HashSet<>();
}
