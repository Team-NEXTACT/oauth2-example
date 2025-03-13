package com.example.oauth2.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoUserInfo(
    @JsonProperty("kakao_account")
    Map<String, Object> kakaoAccount
) {

}
