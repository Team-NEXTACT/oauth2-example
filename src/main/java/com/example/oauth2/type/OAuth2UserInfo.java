package com.example.oauth2.type;

import java.util.Map;

public record OAuth2UserInfo(
    String email,
    String profileImage
) {

  public static OAuth2UserInfo fromKakao(KakaoUserInfo kakaoUserInfo) {
    Map<String, Object> kakaoAccount = kakaoUserInfo.kakaoAccount();
    Map<String, String> profile = (Map<String, String>) kakaoAccount.get("profile");
    return new OAuth2UserInfo((String) kakaoAccount.get("email"), profile.get("profile_image_url"));
  }

  public static OAuth2UserInfo fromGoogle(GoogleUserInfo googleUserInfo) {
    return new OAuth2UserInfo(googleUserInfo.email(), googleUserInfo.picture());
  }
}