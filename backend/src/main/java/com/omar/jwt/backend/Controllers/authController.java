package com.omar.jwt.backend.Controllers;


import com.omar.jwt.backend.DTO.CredentialsDto;
import com.omar.jwt.backend.DTO.SignUpDto;
import com.omar.jwt.backend.DTO.UserDto;
import com.omar.jwt.backend.Services.UserService;
import com.omar.jwt.backend.config.UserAuthProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class authController {

	private final UserService userService;

	private final UserAuthProvider userAuthenticationProvider;

	@PostMapping("/login")
	public ResponseEntity<UserDto> login(@RequestBody @Valid CredentialsDto credentialsDto){
		UserDto userDto =  userService.login(credentialsDto);

		userDto.setToken(userAuthenticationProvider.createToken(userDto));
		return ResponseEntity.ok(userDto);
	}


	@PostMapping("/register")
	public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto user) {
		UserDto createdUser = userService.register(user);
		createdUser.setToken(userAuthenticationProvider.createToken(createdUser));
		return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
	}
}
