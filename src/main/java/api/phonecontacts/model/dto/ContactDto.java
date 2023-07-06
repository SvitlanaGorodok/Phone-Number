package api.phonecontacts.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;

@Data
public class ContactDto {
    @JsonIgnore
    private UUID id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotEmpty(message = "Emails are mandatory")
    private Set<String> emails;

    @NotEmpty(message = "Phone numbers are mandatory")
    private Set<String> phoneNumbers;

    @JsonIgnore
    private UserDto user;
}
