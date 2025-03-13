package com.example.oauth2.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleUserInfo(
    String email,
    String picture
) {

}