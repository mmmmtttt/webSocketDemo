package com.example.demo.common;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.demo.handler.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class ServerRunner implements CommandLineRunner {

  private List<String> namespaceList = Arrays.asList(new String[]{"/message", "/test"});

  private final SocketIOServer server;
  private final SocketIONamespace messageSocketNameSpace;
  private final SocketIONamespace testSocketNameSpace;

  @PreDestroy
  public void destroy() {
    if (server != null) {
      server.stop();
    }
  }

  @Autowired
  private ServerRunner(SocketIOServer server) {
    this.server = server;
    messageSocketNameSpace = server.addNamespace(namespaceList.get(0));
    testSocketNameSpace = server.addNamespace(namespaceList.get(1));
  }

  @Bean(name = "messageNamespace")
  public SocketIONamespace messageNameSpace() {
    messageSocketNameSpace.addListeners(new MessageHandler(server));
    System.out.println("messageNameSpace add listener");
    return messageSocketNameSpace;
  }

  @Bean(name = "testNamespace")
  public SocketIONamespace testSpace() {
    testSocketNameSpace.addListeners(new MessageHandler(server));
    return testSocketNameSpace;
  }

  @Override
  public void run(String... args) {
    System.out.println("#  ServerRunner 开始启动...  #");
    server.start();
  }
}
