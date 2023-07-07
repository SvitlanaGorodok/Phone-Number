package api.phonecontacts.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto {
    @JsonIgnore
    private UUID id;

    @Expose
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Expose
    @NotEmpty(message = "Emails are mandatory")
    private Set<String> emails;

    @Expose
    @NotEmpty(message = "Phone numbers are mandatory")
    private Set<String> phoneNumbers;

    @JsonIgnore
    private UserDto user;
}
