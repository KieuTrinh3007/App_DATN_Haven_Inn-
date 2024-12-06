package com.example.app_datn_haven_inn.Socket

import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URI

object SocketHandler {
    private lateinit var mSocket: Socket

    // Thiết lập kết nối Socket với URL của server
    fun setSocket() {
        try {
            mSocket = IO.socket("http://192.168.100.4:3000")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Lấy socket đã kết nối
    fun getSocket(): Socket {
        return mSocket
    }

    // Kết nối Socket với server
    fun establishConnection() {
        if (!mSocket.connected()) {
            mSocket.connect()
        }
    }

    // Ngắt kết nối socket khi không cần thiết
    fun closeConnection() {
        if (mSocket.connected()) {
            mSocket.disconnect()
        }
    }

    // Lắng nghe một sự kiện và xử lý khi có sự kiện đó
    fun onEvent(event: String, callback: (JSONObject) -> Unit) {
        mSocket.on(event) { args ->
            if (args.isNotEmpty()) {
                val data = args[0] as JSONObject
                callback(data)
            }
        }
    }

    // Gửi dữ liệu đến server
    fun emitEvent(event: String, data: JSONObject) {
        mSocket.emit(event, data)
    }
}
