package com.omar.jwt.backend.config;


import com.omar.jwt.backend.DTO.UserDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.omar.jwt.backend.Entities.User;
import com.omar.jwt.backend.Exceptions.AppException;
import com.omar.jwt.backend.Repositories.UserRepository;
import com.omar.jwt.backend.Services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class UserAuthProvider {


	@Value("${security.jwt.token.secret-key:secret-key}")
	private String secretKey;

	private final UserService userService;

	private final UserRepository userRepository;

	@PostConstruct
	protected void init() {
		// this is to avoid having the raw secret key available in the JVM
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String createToken(UserDto user) {

		Date now = new Date();
		Date validity = new Date(now.getTime() + 3600000); // 1 hour

		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		return JWT.create()
				.withSubject(user.getLogin())
				.withIssuedAt(now)
				.withExpiresAt(validity)
				.withClaim("firstName", user.getFirstName())
				.withClaim("lastName", user.getLastName())
				.sign(algorithm);
	}

	public Authentication validateToken(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secretKey);

		JWTVerifier verifier = JWT.require(algorithm)
				.build();

		DecodedJWT decoded = verifier.verify(token);

		UserDto user = UserDto.builder()
				.login(decoded.getSubject())
				.firstName(decoded.getClaim("firstName").asString())
				.lastName(decoded.getClaim("lastName").asString())
				.build();

		return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
	}



	public Authentication validateTokenStrongly(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secretKey);

		JWTVerifier verifier = JWT.require(algorithm)
				.build();

		DecodedJWT decoded = verifier.verify(token);

		User user = userRepository.findByLogin(decoded.getIssuer()).orElseThrow(
				()->new AppException("unknown user" , HttpStatus.NOT_FOUND));


		return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
	}
}
