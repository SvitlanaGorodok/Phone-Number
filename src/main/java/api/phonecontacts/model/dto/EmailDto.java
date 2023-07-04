package api.phonecontacts.model.dto;

import api.phonecontacts.model.dao.ContactDao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class EmailDto {
    @JsonIgnore
    private UUID id;

    private String email;

    @JsonIgnore
    private Set<ContactDao> contacts;
}
