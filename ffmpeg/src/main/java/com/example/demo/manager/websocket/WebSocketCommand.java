package com.example.demo.manager.websocket;

/**
 * @author libiao
 * @date 2022/4/12
 */
public interface WebSocketCommand {

    // client command

    String PING = "ping";
    String TAG = "tag";


    // server command

    String PONG = "pong";
    String PUSH = "push";

}