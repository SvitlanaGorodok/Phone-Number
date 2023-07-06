package api.phonecontacts.model.mapper;

import api.phonecontacts.model.dao.ContactDao;
import api.phonecontacts.model.dao.Email;
import api.phonecontacts.model.dao.PhoneNumber;
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
    default String emailToString(Email email){
        return (email == null) ? null : email.getEmail();
    }

    default Email StringToEmail(String value){
        Email email = new Email();
        email.setEmail(value);
        return email;
    }

    default String phoneNumberToString(PhoneNumber phoneNumber){
        return (phoneNumber == null) ? null : phoneNumber.getNumber();
    }

    default PhoneNumber stringToPhoneNumber(String value){
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setNumber(value);
        return phoneNumber;
    }

}
