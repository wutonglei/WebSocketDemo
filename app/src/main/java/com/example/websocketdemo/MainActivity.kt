package com.example.websocketdemo

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.util.concurrent.TimeUnit

/**
 * 1.切换网络必须重新连接javaWebsocketClient    okhttp不需要
 *
 * onClose code:1002 reason:Invalid status code received: 403 Status line: HTTP/1.1 403 Forbidden remote:false
 *
 * WebSocket (WS)是HTML5一种新的协议。它实现了浏览器与服务器全双工通信，能更好地节省服务器资源和带宽并达到实时通讯。WebSocket建立在TCP之上，同HTTP一样通过TCP来传输数据，但是它和HTTP最大不同是：

WebSocket是一种双向通信协议，在建立连接后，WebSocket服务器和Browser/Client Agent都能主动的向对方发送或接收数据，就像Socket一样；WebSocket需要类似TCP的客户端和服务器端通过握手连接，连接成功后才能相互通信。

WSS（Web Socket Secure）是WebSocket的加密版本。
 */
class MainActivity : AppCompatActivity() {
    var isNetworkLog=false
    private val TAG = "MainActivity"
//    private val URL = "wss://socket.idcd.com:1443"
    private val URL = "ws://47.93.126.142:8990/connectWebSocket"
    fun showLog(data: String) {
        runOnUiThread {
            tv_log.append("$data \n")
        }
        Log.i(TAG, "showLog: $data")
    }

    var uri: URI = URI.create(URL)
    private var javaWebsocketClient: WebSocketClient? = null


    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(3, TimeUnit.SECONDS) //设置读取超时时间
        .writeTimeout(3, TimeUnit.SECONDS) //设置写的超时时间
        .connectTimeout(3, TimeUnit.SECONDS) //设置连接超时时间
        .build()

    var okHttpWebSocket: WebSocket? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListener()
        javaWebsocketClient = object : WebSocketClient(uri, Draft_6455()) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                if (handshakedata != null)
                    showLog("onOpen  ${handshakedata.toString()}  ${handshakedata.httpStatusMessage}")
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                showLog("onClose code:$code reason:$reason remote:$remote")
            }

            override fun onMessage(message: String?) {
                showLog("onMessage $message")
            }

            override fun onError(ex: Exception?) {
                showLog("onError ${ex.toString()}  ${ex?.message.toString()}")
            }

        }
    }

    private val request: Request = Request.Builder().get().url(URL).build()

    private fun setListener() {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        connMgr.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
//    wifi onCapabilitiesChanged: [ Transports: CELLULAR Capabilities: SUPL&INTERNET&NOT_RESTRICTED&TRUSTED&NOT_VPN&NOT_ROAMING&FOREGROUND&NOT_CONGESTED&NOT_SUSPENDED Unwanted:  LinkUpBandwidth>=51200Kbps LinkDnBandwidth>=102400Kbps Specifier: <1>]
//      4g onCapabilitiesChanged :[ Transports: CELLULAR Capabilities: SUPL&INTERNET&NOT_RESTRICTED&TRUSTED&NOT_VPN&VALIDATED&NOT_ROAMING&FOREGROUND&NOT_CONGESTED&NOT_SUSPENDED Unwanted:  LinkUpBandwidth>=51200Kbps LinkDnBandwidth>=102400Kbps Specifier: <1>]
                super.onCapabilitiesChanged(network, networkCapabilities)
                if(isNetworkLog){
                    Log.i(TAG, "onCapabilitiesChanged: ${networkCapabilities.toString()}")
                    showLog("onCapabilitiesChanged :${networkCapabilities.toString()}")
                }

            }
        })


// onError java.io.IOException  null
        btn_send_java_websocket.setOnClickListener {
            if (javaWebsocketClient?.isOpen == true)
                javaWebsocketClient?.send("javaWebsocketClient trh1231231")
        }
        btn_java_websocket.setOnClickListener {
            Thread {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    javaWebsocketClient?.connectBlocking();
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }.start()


        }
        btn_close_java_websocket.setOnClickListener {
            javaWebsocketClient?.close()
        }

        btn_send_okhttp.setOnClickListener {
            okHttpWebSocket?.send("okHttpWebSocket trh1231231")
        }


//ust be in range [1000,5000): 200
        btn_close_okhttp3.setOnClickListener {
            okHttpWebSocket?.close(1000, "123456")
        }


        btn_okhttp3.setOnClickListener {

            okHttpWebSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {

//
                    showLog("onOpen ## ${response.body()?.string()}")
                    showLog("onOpen ## ${response.message()}")
                    showLog("onOpen ## ${response.toString()}")

                }

                override fun onMessage(webSocket: WebSocket, text: String) {
//                {"message":"在线名单列表","messageType":3,"onlineUsers":["0","1","2"],"userId":"2"}
                    showLog("onMessage ## $text")

                }
//断开属于正常
                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    showLog("onClosing websocket断开")
                }
//
                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    showLog("onClosed websocket断开")
                }
//属于异常  不会去重新连接的
                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    showLog(
                        "onFailure websocket ${response?.body()
                            ?.string()}  ${t.message.toString()}   "
                    )
                    showLog("  ${t.toString()}   ")
                }


            })
        }



    }


    override fun onDestroy() {
        super.onDestroy()
//      okHttpWebSocket.close()
    }
}