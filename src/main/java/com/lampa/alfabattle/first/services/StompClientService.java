package com.lampa.alfabattle.first.services;

import com.lampa.alfabattle.first.dto.ATMDetails;
import com.lampa.alfabattle.first.dto.AlfikDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.concurrent.FutureCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class StompClientService implements StompSessionHandler {
    @Value("${alfa.websocket.url}")
    private String url;

    @Value("${alfa.websocket.timeout}")
    private Integer timeout;

    private Map<Integer, FutureCallback> requests = new HashMap<>();
    private StompSession stompSession;

    @PostConstruct
    private void init() {
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.connect(url, this);
    }

    /**
     * request filter from ws
     *
     * @return
     */
    public void request(ATMDetails atmDetails, FutureCallback<Integer> futureCallback) {
        requests.put(atmDetails.getDeviceId(), futureCallback);
        stompSession.send("/", atmDetails);
    }

    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        this.stompSession = stompSession;
        this.stompSession.subscribe("/topic/alfik", this);
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
        log.error("err", throwable);
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        log.error("err2", throwable);
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        return AlfikDto.class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
        AlfikDto alfikDto = (AlfikDto) o;

        if (requests.containsKey(alfikDto.getDeviceId())) {
            requests.get(alfikDto.getDeviceId()).completed(alfikDto.getAlfik());
            requests.remove(alfikDto.getDeviceId());
        }
    }
}
