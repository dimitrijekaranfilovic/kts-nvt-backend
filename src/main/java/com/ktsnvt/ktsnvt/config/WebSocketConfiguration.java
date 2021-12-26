package com.ktsnvt.ktsnvt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket") // Endpoint for communication with server (STOMP registration), connection URL: http://localhost:8081/socket/
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/socket-subscriber")// Prefix that is used during message mapping.
                                                                        // Clients have to use it when they send messages to server.
                                                                        // Every URL starts with: http://localhost:8081/socket-subscriber
                .enableSimpleBroker("/socket-publisher");// Defining topic prefix
                                                                        // SimpleBroker saves data in memory and sends it to clients using predefined topics.
                                                                        // Server sends messages on routes that are defined here.
                                                                        // Routes are comma-separated for example: ("/route1", "/route2")
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().maxPoolSize(36).corePoolSize(10).queueCapacity(36);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().maxPoolSize(36).corePoolSize(10).queueCapacity(36);
    }
}
