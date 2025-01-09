//package com.example.danjamserver.chat;
//
//import com.example.danjamserver.chat.dto.requests.ChatCreateReq;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.messaging.converter.MappingJackson2MessageConverter;
//import org.springframework.messaging.simp.stomp.StompHeaders;
//import org.springframework.messaging.simp.stomp.StompSession;
//import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
////import org.springframework.messaging.simp.stomp.StompTextMessageBuilder;
//import org.springframework.web.socket.WebSocketHttpHeaders;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//import org.springframework.web.socket.messaging.WebSocketStompClient;
//
//import java.lang.reflect.Type;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class ChatControllerTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Test
//    public void testWebSocketEndpoint() throws Exception {
//        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
//        stompClient.setMessageConverter(new MappingJackson2MessageConverter()); // JSON 변환기 설정
//
//        StompSession session = stompClient.connect(
//                "ws://localhost:" + port + "/chat",
//                new WebSocketHttpHeaders(),
//                new MyStompSessionHandler()
//        ).get(1, TimeUnit.SECONDS);
//
//        ChatCreateReq chatCreateReq = ChatCreateReq.builder()
//                .message("Hello, WebSocket!")
//                .username("testuser")
//                .build();
//
//        // Send a message and validate the response
//        session.send("/pub/chat.sendMessage", chatCreateReq);
//    }
//
//    private class MyStompSessionHandler extends StompSessionHandlerAdapter {
//        @Override
//        public Type getPayloadType(StompHeaders headers) {
//            return ChatCreateReq.class;
//        }
//
//        @Override
//        public void handleFrame(StompHeaders headers, Object payload) {
//            ChatCreateReq msg = (ChatCreateReq) payload;
//            assertEquals("Hello, WebSocket!", msg.getMessage());
//            assertEquals("testuser", msg.getUsername());
//        }
//    }
//}
