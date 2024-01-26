package com.omar.jwt.backend.Mappers;

import com.omar.jwt.backend.DTO.SignUpDto;
import com.omar.jwt.backend.DTO.UserDto;
import com.omar.jwt.backend.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserDto toUserDto(User user);

	@Mapping(target = "password", ignore = true)
	User signUpToUser(SignUpDto signUpDto);

}