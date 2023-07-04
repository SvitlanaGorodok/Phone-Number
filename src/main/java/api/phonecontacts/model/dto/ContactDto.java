package api.phonecontacts.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class ContactDto {
    @JsonIgnore
    private UUID id;

    private String name;
    private Set<EmailDto> emails;
    private Set<PhoneNumberDto> phoneNumbers;

    @JsonIgnore
    private UserDto user;
}
