package com.example.websocketdemo.service

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.websocketdemo.R
import com.example.websocketdemo.utils.NetUtils
import kotlinx.android.synthetic.main.activity_web_socket_service.*
import java.net.InetSocketAddress

class WebSocketServiceActivity : AppCompatActivity() {

    var myWebsocketServer :MyWebSocketServer?=null

    override fun onDestroy() {
        super.onDestroy()

        myWebsocketServer?.stop()
    }
    // 实现方法，在服务中或者OnCreate()方法调用此方法
    open fun startMyWebsocketServer(): Unit {
        // 192.168.1.101为安卓服务端，需要连接wifi后 高级选项ip设置为静态,输入自定义地址
        // 方便客户端 找 服务端,不需要用getHostAddress等，可能连接不上
        // 9090为端口
        val myHost = InetSocketAddress(host, 9091)
        myWebsocketServer = MyWebSocketServer(myHost)
        myWebsocketServer!!.start()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_socket_service)
    }

    private  val TAG = "WebSocketServiceActivit"
    fun onClick(v: View){
        when(v){
            btn_open->{
                Log.i(TAG, "onClick: 启动WebSocket")
                startMyWebsocketServer()
            }

            btn_get_ip->{
                host=NetUtils.getHostIp()
                Log.i(TAG, "onClick getHostIp:${NetUtils.getHostIp()} ")
//                Log.i(TAG, "onClick getNetIp:${NetUtils.getNetIp()} ")


            }
        }
    }
    var host=""
}