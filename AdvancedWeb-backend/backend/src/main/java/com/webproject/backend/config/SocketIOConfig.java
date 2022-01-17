package com.webproject.backend.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {

    @Value("${socketio.host}")
    private String host;

    @Value("${socketio.port}")
    private int port;

    @Value("${socketio.bossCount}")
    private int bossCount;

    @Value("${socketio.workerCount}")
    private int workerCount;

    @Value("${socketio.pingInterval}")
    private int pingInterval;

    @Value("${socketio.pingTimeout}")
    private int pingTimeout;

    @Bean
    public SocketIOServer socketIOServer(){
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        socketConfig.setReuseAddress(true);
        com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
        configuration.setSocketConfig(socketConfig);
        configuration.setHostname(host);
        configuration.setPort(port);
        configuration.setBossThreads(bossCount);
        configuration.setWorkerThreads(workerCount);
        configuration.setPingInterval(pingInterval);
        configuration.setPingTimeout(pingTimeout);
        return new SocketIOServer(configuration);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner() {
        return new SpringAnnotationScanner(socketIOServer());
    }

}
