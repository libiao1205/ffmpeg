package com.example.demo.manager.websocket;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.JsonUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author libiao
 * @date 2022/4/11
 */
@ServerEndpoint("/websocket/{uid}")
@Component
public class WebSocketManager {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WebSocketManager.class);

    /**
     * 不可以在onopen方法中使用以下对象，为null
     */
    //@Value("${stl.redis.channel.web-socket}")
    private String webSocketChannel = "IMG_COMPRESS";

    @Autowired
    SessionManager  sessionManager;

    /**
     * 建立连接的用户
     */
    private static final Map<Long, List<Session>> userSessionMap = new ConcurrentHashMap<>();

    public void pushMessageFromRedis(JSONObject param) {
        String text = JsonUtils.toJson(param);
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        String noticeId = uuid;
        logger.info(String.format("----websocket , send:%s", text));
        try {
            List<Session> pushSessionList = userSessionMap.get(15774L);
            if (pushSessionList == null || pushSessionList.size() == 0) {
                return;
            }
            pushSessionList.forEach(pushSession -> pushSession.getAsyncRemote().sendText(text));
        } catch (Exception e) {
            logger.info("send push message occur io error, push param: {}", JsonUtils.toJson(param));
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("uid") Long uid, Session session) {
        logger.info("链接建立成功 uid={}", uid);
        sessionManager.add();
        changeUserSession(uid, session, true);
    }

    @OnClose
    public void onClose(@PathParam("uid") Long uid, Session session) {
        logger.info("websocket 有一链接关闭 uid={}", uid);
        changeUserSession(uid, session, false);
    }

    @OnMessage
    public void onMessage(@PathParam("uid")Long uid, String message, Session session) throws IOException {
        logger.info("服务端收到客户端的消息:{}", message);
        JSONObject json = JsonUtils.toObject(message, JSONObject.class);
         int code = Integer.parseInt(json.get("code").toString());
        if(0 == code) {
            session.getAsyncRemote().sendText(json.toString());
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("cause error sesseion id is {}, message: {}", session.getId(), error.getLocalizedMessage());
        /*try {
            session.close();
        } catch (IOException e) {
            logger.error("WebSocket Error: {}", error.getLocalizedMessage());
        }*/
    }

    /**
     * 用户连接发生改变
     */
    private void changeUserSession(Long uid, Session session, boolean isAdd){
        List<Session> pushSessionList = userSessionMap.get(uid);
        if (isAdd) {
            if (CollectionUtils.isEmpty(pushSessionList)) {
                pushSessionList = new ArrayList<>();
            }
            pushSessionList.add(session);
        } else {
            if (!CollectionUtils.isEmpty(pushSessionList)) {
                pushSessionList.remove(session);
            }
        }
        userSessionMap.put(uid, pushSessionList);
    }

}
