package com.example.oauth2.persistence;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {


  private String email;

  private String profileImage;

  private String provider;

  public User(String email, String profileImage, String provider) {
    this.email = email;
    this.profileImage = profileImage;
    this.provider = provider;
  }

}