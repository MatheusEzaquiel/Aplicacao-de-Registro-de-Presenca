package com.mbe.ada.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mbe.ada.model.user.User;

@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;

	public String generateToken(User user) {
		
		try {
			
			Algorithm algorithm = Algorithm.HMAC256(secret);
			
			String token = JWT.create()
					.withIssuer("ada") // Informa o microsserviço que gerou o token (caso haja + de um que utiliza o mesmo token adicionar outro withIssues()
					.withSubject(user.getEmail()) // Salvar dados no Token
					.withExpiresAt(generateExpirationDate())
					.sign(algorithm);
			
			return token;
			
		} catch (JWTCreationException ex) {
			throw new RuntimeException("Erro ao autenticar");
		}
		
	}
	
	public String validateToken(String token) {
		
		try {
			
			Algorithm algorithm = Algorithm.HMAC256(secret);
			
			return JWT.require(algorithm)
					.withIssuer("ada")
					.build()
					.verify(token)
					.getSubject(); // Retorna o(s) dado(s) incluído(s) no token
			
		} catch (JWTVerificationException ex) {
			return null;
		}
		
	}
	
	private Instant generateExpirationDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
	
}
