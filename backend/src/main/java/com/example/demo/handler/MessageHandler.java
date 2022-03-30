package com.example.demo.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.demo.model.Message;

public class MessageHandler {

  private final SocketIOServer server;

  public MessageHandler(SocketIOServer server) {
    this.server = server;
  }

  //添加 connect 事件，当客户端发起连接时调用
  @OnConnect
  public void onConnect(SocketIOClient client) {
    System.out.println("onConnect > "+ client.getNamespace().getName()+client.getSessionId().toString());
    SocketIONamespace namespace = client.getNamespace();
    namespace.getBroadcastOperations().sendEvent("connect", client.getRemoteAddress().toString());
  }

  //添加 disconnect 事件，客户端断开连接时调用
  @OnDisconnect
  public void onDisconnect(SocketIOClient client) {
    client.disconnect();
    System.out.println("onDisconnect > "+ client.getNamespace().getName()+ client.getSessionId().toString());
    SocketIONamespace namespace = client.getNamespace();
    namespace.getBroadcastOperations().sendEvent("disconnect", client.getRemoteAddress().toString());
  }

  // 自定义 message 事件
  @OnEvent(value = "message")
  public void onEvent(SocketIOClient client, AckRequest ackRequest, Message message) {
    System.out.println("接收到客户端 "+ client.getNamespace().getName()+ message);
    SocketIONamespace namespace = client.getNamespace();
    namespace.getBroadcastOperations().sendEvent("message", client.getRemoteAddress().toString()+":"+message);
  }

  /**
   * How to use the protobuf protocol:
   * https://github.com/mrniko/netty-socketio/issues/497
   */

}
