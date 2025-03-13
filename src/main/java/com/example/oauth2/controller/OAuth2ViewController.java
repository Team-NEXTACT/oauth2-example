package com.example.oauth2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2ViewController {

  @Value("${oauth2.client.registration.kakao.client-id}")
  private String KAKAO_CLENT_ID;

  @Value("${oauth2.client.registration.google.client-id}")
  private String GOOGLE_CLIENT_ID;

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("kakaoClientId", KAKAO_CLENT_ID);
    model.addAttribute("googleClientId", GOOGLE_CLIENT_ID);
    return "index";
  }

  @GetMapping("/oauth2/code/google")
  public String oauth2CodeGoogle() {
    return "oauth-google";
  }

  @GetMapping("/oauth2/code/kakao")
  public String oauth2CodeKakao() {
    return "oauth-kakao";
  }

  @GetMapping("/oauth2/success")
  public String oauth2Success() {
    return "oauth-success";
  }
}