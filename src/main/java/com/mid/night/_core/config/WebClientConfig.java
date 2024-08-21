package com.mid.night._core.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("http-pool")
                // 동시 연결 수 제한
                .maxConnections(100)
                // 연결 풀에서 사용 가능한 연결을 탐색하는 시간
                .pendingAcquireTimeout(Duration.ofMillis(0))
                // 대기 중인 연결 요청의 최대 개수 설정 -> -1 : 무제한
                .pendingAcquireMaxCount(-1)
                // 유휴 (실제적인 작업이 없는 시간) 상태일 때 최대 유지 시간
                .maxIdleTime(Duration.ofMillis(1000L))
                .build();
    }

    @Bean
    public HttpClient httpClient(ConnectionProvider connectionProvider) {
        return HttpClient.create(connectionProvider)
                // 연결 시도 시 대기 시간 -> 10초 이후 실패
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                // 서버가 10초 안에 응답하지 않으면 연결 종료
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(10)));
    }

    @Bean
    public WebClient webClient(HttpClient httpClient) {

        // URI 를 빌드할 때 사용할 팩토리
        // VALUES_ONLY : 쿼리 매개변수의 값만 인코딩, 키는 인코딩 X
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        return WebClient.builder()
                .uriBuilderFactory(factory)
                // 디코더가 메모리에서 처리할 수 있는 최대 크기 설정 (응답 바디 크기 설정)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
