package com.nikhil.userauthentication.dtos;

import com.nikhil.userauthentication.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto
{
    private String name;
    private String email;

    //Terrible idea to send password after signup, just sending here for testing purpose
    private String password;

    public static UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
}
