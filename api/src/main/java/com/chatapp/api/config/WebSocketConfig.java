package com.chatapp.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Ajouter une règle pour l'endpoint WebSocket
        registry.addEndpoint("/auth/ws") // Assurez-vous que c'est le bon endpoint
                .setAllowedOrigins("http://localhost:4200") // Permettre les connexions WebSocket depuis le frontend
                                                            // Angular
                .withSockJS(); // Activer SockJS pour les connexions WebSocket
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue", "/topic"); // Permet les destinations comme "/queue"
        config.setApplicationDestinationPrefixes("/app"); // Préfixe des destinations d'application
    }
}
