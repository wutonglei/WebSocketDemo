package com.example.websocketdemo.service;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
//https://blog.csdn.net/qq_40699089/article/details/106192897
//http://www.pc0359.cn/article/android/81588.html   查看手机的ip
/**
 * // 192.168.1.101为安卓服务端，需要连接wifi后 高级选项ip设置为静态,输入自定义地址
 *         // 方便客户端 找 服务端,不需要用getHostAddress等，可能连接不上
 *         // 9090为端口
 *https://wenku.baidu.com/view/4ceb6ee46d175f0e7cd184254b35eefdc9d3150d.html 手机端口映射到外网
 *         建议使用frp，在vps上搭建frps，内网搭建frpc，即可用vps的公网访问到内网，很简单的 frp可以在github上下载到
 *
 *         [Android] 超级简单的用安卓手机做web服务器ksweb     [复制链接]
 *         .https://www.52pojie.cn/thread-1020867-1-1.html
 */
public class MyWebSocketServer extends WebSocketServer{

    public MyWebSocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d("websocket", "onOpen()一个客户端连接成功："+conn.getRemoteSocketAddress());

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d("websocket", "onClose()服务器关闭");

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.d("websocket", "onMessage()网页端来的消息->"+message);

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.e("websocket", "->onError()出现异常："+ex);

    }

    @Override
    public void onStart() {
        Log.d("websocket", "onStart()，WebSocket服务端启动成功");

    }
}
