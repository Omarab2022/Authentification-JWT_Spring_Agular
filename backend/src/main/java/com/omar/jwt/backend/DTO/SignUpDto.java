package com.omar.jwt.backend.DTO;


public record SignUpDto (String firstName, String lastName, String login, char[] password) {

}
