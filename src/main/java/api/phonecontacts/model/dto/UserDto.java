package api.phonecontacts.model.dto;

import api.phonecontacts.model.dao.ContactDao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @JsonIgnore
    private UUID id;

    @NotEmpty(message = "Login is mandatory")
    @Expose
    private String login;

    @NotEmpty(message = "Password is mandatory")
    @Expose
    private String password;

    @JsonIgnore
    private Set<ContactDao> contacts = new HashSet<>();

    public UserDto(UUID id) {
        this.id = id;
    }
}
