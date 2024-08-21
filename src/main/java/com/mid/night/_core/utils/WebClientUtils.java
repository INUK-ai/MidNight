package com.mid.night._core.utils;

import com.mid.night._core.error.exception.Exception400;
import com.mid.night._core.error.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WebClientUtils {

    private final WebClient webClient;

    // 동기 POST 요청
    public <T, V> T postSync(String url, V requestDto, Class<T> responseDtoClass) {
        return webClient.method(HttpMethod.POST) // HTTP 메소드 설정
                // 요청을 보낼 url
                .uri(url)
                // 요청 본문
                .bodyValue(requestDto)
                // 응답 수신, 처리
                .retrieve()
                // 400대 에러 확인
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(() -> new Exception400("잘못된 접근입니다.")))
                // 500대 에러 확인
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(() -> new Exception500("서버 에러가 발생하였습니다.")))
                // 응답 본문 'Mono' 변환, responseDtoClass 에 해당하는 타입으로 변환
                .bodyToMono(responseDtoClass)
                // 블로킹 호출 수행
                .block();
    }
}
