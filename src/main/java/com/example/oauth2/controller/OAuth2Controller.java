package com.example.oauth2.controller;

import com.example.oauth2.service.OAuth2Service;
import com.example.oauth2.type.OAuth2UserInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

  private final OAuth2Service oAuth2Service;

  // OAuth2.0 AccessToken 발급
  @PostMapping("/oauth2/token/{provider}")
  public ApiResponse getOAuth2Token(@PathVariable("provider") String provider, @RequestBody OAuth2TokenRequest tokenRequest) {
    TokenResponse tokenResponse = oAuth2Service.getAccessToken(provider.toUpperCase(), tokenRequest);
    return new ApiResponse(tokenResponse.accessToken);
  }

  // OAUth2.0 사용자 정보 조회
  @PostMapping("/oauth2/login/{provider}")
  public ApiResponse loginOAuth2(@PathVariable("provider") String provider, @RequestBody OAuth2UserInfoRequest request) {
    provider = provider.toUpperCase();
    OAuth2UserInfo oAuth2UserInfo = oAuth2Service.registerOrLoginUser(provider, request);
    return new ApiResponse(oAuth2UserInfo);
  }

  public record ApiResponse(
      Object data
  ) {

  }

  public record OAuth2TokenRequest(
      String code,
      String redirectUrl
  ) {

  }

  public record OAuth2UserInfoRequest(
      String accessToken
  ) {

  }

  public record TokenResponse(
      @JsonProperty("access_token")
      String accessToken
  ) {

  }

}