package com.example.app_datn_haven_inn.Socket

import android.util.Log
import com.example.app_datn_haven_inn.utils.Constans
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException

object SocketHandler {
    private var mSocket: Socket? = null

    // Phương thức khởi tạo socket với id_NguoiDung (truyền qua query)
    fun setSocket(userId: String?) {
        try {
            val options = IO.Options()
            options.query = "idNguoiDung=$userId" // Truyền id_NguoiDung qua query
            mSocket = IO.socket(Constans.DOMAIN.replace("api/", ""), options) // Kết nối tới server
        } catch (e: URISyntaxException) {
            Log.e("SocketHandler", "URISyntaxException: ${e.message}")
        }
    }

    // Kết nối socket
    fun establishConnection() {
        mSocket?.connect()
    }

    // Ngắt kết nối socket
    fun closeConnection() {
        mSocket?.disconnect()
    }

    // Lắng nghe sự kiện từ server
    fun onEvent(event: String, listener: (JSONObject) -> Unit) {
        mSocket?.on(event) { args ->
            if (args.isNotEmpty()) {
                val data = args[0] as JSONObject
                listener(data)
            }
        }
    }

    // Gửi sự kiện tới server
    fun emitEvent(event: String, data: JSONObject) {
        mSocket?.emit(event, data)
    }
}