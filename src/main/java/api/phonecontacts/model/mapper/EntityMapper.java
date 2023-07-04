package api.phonecontacts.model.mapper;

import api.phonecontacts.model.dao.ContactDao;
import api.phonecontacts.model.dao.UserDao;
import api.phonecontacts.model.dto.ContactDto;
import api.phonecontacts.model.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    UserDto userToDto(UserDao userDao);
    UserDao userToDao(UserDto userDto);

    ContactDto contactToDto(ContactDao contactDao);
    ContactDao contactToDao(ContactDto contactDto);

}
