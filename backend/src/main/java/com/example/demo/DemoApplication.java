package com.example.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import java.util.Map;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(8080);
        SocketIOServer server = new SocketIOServer(config);

        server.addConnectListener(new ConnectListener() {
            // 添加客户端连接监听器
            public void onConnect(SocketIOClient client) {
                client.sendEvent("connect", client.getRemoteAddress().toString());
            }
        });

        //监听客户端事件，message为事件名称，-自定义事件
        server.addEventListener("message", String.class, new DataListener<String>() {
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) throws ClassNotFoundException {
                //客户端推送advert_info事件时，onData接受数据，这里是string类型的json数据，还可以为Byte[],object其他类型
                String sa = client.getRemoteAddress().toString();
                Map params = client.getHandshakeData().getUrlParams();//获取客户端url参数
                System.out.println(sa + "：客户端：************" + data);
				server.getBroadcastOperations().sendEvent("message", sa+":"+data);
            }
        });

        //添加客户端断开连接事件
        server.addDisconnectListener(new DisconnectListener() {
            public void onDisconnect(SocketIOClient client) {
                String sa = client.getRemoteAddress().toString();
                String clientIp = sa.substring(1, sa.indexOf(":"));//获取设备ip
                System.out.println(clientIp + "-------------------------" + "客户端已断开连接");
                //给客户端发送消息
                client.sendEvent("disconnect", clientIp);
            }
        });
        server.start();
    }
}
