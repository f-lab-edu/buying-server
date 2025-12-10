package org.example.buyingserver.payment.config;

import ch.qos.logback.core.net.server.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class TossPaymentsWebClientConfig {

    private final TossPaymentsConfig tossPaymentsConfig;

    //toss전용 api호출 용
    // 거기에 요청에 자동으로 인증헤더 추가하도록 설정하는걸 추가
    @Bean
    public WebClient tossPaymentsWebClient() {
        return WebClient.builder()
                .baseUrl(tossPaymentsConfig.getBaseUrl())
                .filter(authFilter())
                .build();
    }

    //요청을 복사해서 새로운 요청, 내가 원하는 요청을 만들도록 하는 빌드 패턴
    private ExchangeFilterFunction authFilter() {
        return (request, next) -> {
            String authHeader = createAuthorizationHeader();

            ClientRequest clientRequest = ClientRequest.from(request)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .build();
            return next.exchange(clientRequest);
        };
    }

    //Authorization 헤더 생성
    private String createAuthorizationHeader() {
        //시크릿 키 뒤에 콜론(:) 추가
        String credentials = tossPaymentsConfig.getSecretKey() + ":";

        //인코딩
        byte[] encodedBytes = Base64.getEncoder()
                .encode(credentials.getBytes(StandardCharsets.UTF_8));
        String encoded = new String(encodedBytes);

        // "Basic " 접두사 추가 (뒤에 공백 있음)
        return "Basic " + encoded;

    }

}
