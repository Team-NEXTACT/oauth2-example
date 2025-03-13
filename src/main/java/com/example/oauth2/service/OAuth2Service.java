package com.example.oauth2.service;

import com.example.oauth2.controller.OAuth2Controller.OAuth2TokenRequest;
import com.example.oauth2.controller.OAuth2Controller.OAuth2UserInfoRequest;
import com.example.oauth2.controller.OAuth2Controller.TokenResponse;
import com.example.oauth2.persistence.User;
import com.example.oauth2.persistence.UserRepository;
import com.example.oauth2.type.GoogleUserInfo;
import com.example.oauth2.type.KakaoUserInfo;
import com.example.oauth2.type.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {

  private final RestClient restClient;
  private final UserRepository userRepository;
  @Value("${oauth2.client.registration.kakao.client-id}")
  private String KAKAO_CLENT_ID;
  @Value("${oauth2.client.registration.kakao.client-secret}")
  private String KAKAO_SECRET_KEY;
  @Value("${oauth2.client.registration.kakao.token-uri}")
  private String KAKAO_TOKEN_URL;
  @Value("${oauth2.client.registration.kakao.user-info-uri}")
  private String KAKAO_USER_INFO_URL;
  @Value("${oauth2.client.registration.google.client-id}")
  private String GOOGLE_CLIENT_ID;
  @Value("${oauth2.client.registration.google.client-secret}")
  private String GOOGLE_SECRET_KEY;
  @Value("${oauth2.client.registration.google.token-uri}")
  private String GOOGLE_TOKEN_URL;
  @Value("${oauth2.client.registration.google.user-info-uri}")
  private String GOOGLE_USER_INFO_URL;

  /**
   * OAuth2 AccessToken 조회 메서드
   */
  public TokenResponse getAccessToken(String provider, OAuth2TokenRequest tokenRequest) {
    MultiValueMap<String, String> params = createBody(provider, tokenRequest);

    String requestUrl = provider.equals("KAKAO") ? KAKAO_TOKEN_URL : GOOGLE_TOKEN_URL;
    return restClient.post().uri(requestUrl)
                     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                     .body(params)
                     .retrieve()
                     .body(TokenResponse.class);
  }

  /**
   * OAuth2 UserInfo 조회 후 회원가입/로그인 진행 메서드
   */
  public OAuth2UserInfo registerOrLoginUser(String provider, OAuth2UserInfoRequest request) {
    String requestUrl = provider.equals("KAKAO") ? KAKAO_USER_INFO_URL : GOOGLE_USER_INFO_URL;
    OAuth2UserInfo oauth2UserInfo = getOAuth2UserInfo(provider, request, requestUrl);

    if (!userRepository.exists(oauth2UserInfo.email())) {
      userRepository.save(new User(oauth2UserInfo.email(), oauth2UserInfo.profileImage(), provider));
    } else {
      // 로그인 로직 수행
    }

    return oauth2UserInfo;
  }

  private OAuth2UserInfo getOAuth2UserInfo(String provider, OAuth2UserInfoRequest request, String requestUrl) {
    Class<?> clazz = provider.equals("KAKAO") ? KakaoUserInfo.class : GoogleUserInfo.class;
    Object body = restClient.post().uri(requestUrl).headers(headers -> {
                              headers.setBearerAuth(request.accessToken());
                              headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                            }).retrieve()
                            .body(clazz);
    return provider.equals("KAKAO") ?
        OAuth2UserInfo.fromKakao((KakaoUserInfo) body) : OAuth2UserInfo.fromGoogle((GoogleUserInfo) body);
  }

  private MultiValueMap<String, String> createBody(String provider, OAuth2TokenRequest tokenRequest) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "authorization_code");
    params.add("client_id", provider.equals("KAKAO") ? KAKAO_CLENT_ID : GOOGLE_CLIENT_ID);
    params.add("client_secret", provider.equals("KAKAO") ? KAKAO_SECRET_KEY : GOOGLE_SECRET_KEY);
    params.add("code", tokenRequest.code());
    params.add("redirect_uri", tokenRequest.redirectUrl());
    return params;
  }


}