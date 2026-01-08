package com.hsryuuu.base.kakao;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Getter
@Component
public class KakaoApi {

  @Value("${kakao.api_key}")
  private String kakaoApiKey;

  @Value("${kakao.redirect_uri}")
  private String kakaoRedirectUri;


}
