package org.example.buyingserver.chat.config;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.chat.websocket.ChatStompInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor

public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final ChatStompInterceptor chatStompInterceptor;



    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/publish");  // /publish/1 해야지 메세지 발행
        registry.enableSimpleBroker("/topic"); // /topic/1 형태로 메세지 수신
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*")
                //별도 cors
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(chatStompInterceptor);
    }
}