package com.example.websocketdemo

import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

open class JWebSocketClient(serverUri: URI?) : WebSocketClient(serverUri, Draft_6455()) {
    override fun onOpen(handshake: ServerHandshake) {
        Log.e("JWebSocketClient", "onOpen()")
    }

    override fun onMessage(message: String?) {
        Log.e("JWebSocketClient", "onMessage()")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        Log.e("JWebSocketClient", "onClose()")
    }

    override fun onError(ex: Exception?) {
        Log.e("JWebSocketClient", "onError()")
    }
}