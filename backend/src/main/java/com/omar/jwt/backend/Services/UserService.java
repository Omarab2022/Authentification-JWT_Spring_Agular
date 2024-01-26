package com.omar.jwt.backend.Services;


import com.omar.jwt.backend.DTO.CredentialsDto;
import com.omar.jwt.backend.DTO.SignUpDto;
import com.omar.jwt.backend.DTO.UserDto;
import com.omar.jwt.backend.Entities.User;
import com.omar.jwt.backend.Exceptions.AppException;
import com.omar.jwt.backend.Mappers.UserMapper;
import com.omar.jwt.backend.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final UserMapper userMapper;

	public UserDto login(CredentialsDto credentialsDto){

		User user = userRepository.findByLogin(credentialsDto.login()).orElseThrow(
				() -> new AppException("Unknown User", HttpStatus.NOT_FOUND)
		);

		if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
			return userMapper.toUserDto(user);
		}
		throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
	}


	public UserDto register(SignUpDto userDto) {
		Optional<User> optionalUser = userRepository.findByLogin(userDto.login());

		if (optionalUser.isPresent()) {
			throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
		}

		User user = userMapper.signUpToUser(userDto);
		user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));

		User savedUser = userRepository.save(user);

		return userMapper.toUserDto(savedUser);
	}

}
