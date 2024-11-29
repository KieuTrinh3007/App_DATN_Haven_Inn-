package com.example.app_datn_haven_inn.ui.cccd

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.text.Normalizer

class CCCDDataExtractor {

    fun extractCCCDInfo(bitmap: Bitmap, callback: (Map<String, String>) -> Unit) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val dataMap = parseCCCDInfo(visionText)
                callback(dataMap)
            }

            .addOnFailureListener { e ->
                Log.e("CCCDDataExtractor", "Error recognizing text: ${e.message}")
                callback(emptyMap()) // Trả về dữ liệu rỗng nếu có lỗi
            }
    }

    private fun parseCCCDInfo(visionText: Text): Map<String, String> {
        val dataMap = mutableMapOf<String, String>()
        val lines = visionText.textBlocks.flatMap { it.lines }

        for (i in lines.indices) {
            val text = lines[i].text.trim()

            // Kiểm tra thông tin họ tên
            if (text.contains("Họ tên", ignoreCase = true) || text.contains("Tên", ignoreCase = true) || text.contains("Họ và tên", ignoreCase = true)) {
                if (i + 1 < lines.size) {
                    val name = lines[i + 1].text.trim()
                    dataMap["name"] = removeVietnameseAccents(name)
                }
            }

            // Kiểm tra thông tin ngày sinh
            if (text.contains("Ngày sinh", ignoreCase = true)) {
                val dateOfBirth = text.replace("Ngày sinh", "", ignoreCase = true)
                    .replace("Date of birth:", "", ignoreCase = true)
                    .trim()
                    .replace("^/+".toRegex(), "") // Loại bỏ dấu "/" thừa ở đầu chuỗi
                    .replace("/+", "/") // Giữ nguyên chỉ một dấu "/" giữa các phần ngày, tháng, năm
                dataMap["birth_date"] = removeVietnameseAccents(dateOfBirth)
            }

            // Kiểm tra thông tin giới tính
            if (text.contains("Giới tính", ignoreCase = true)) {
                if (text.contains("Nam", ignoreCase = true)) {
                    dataMap["gender"] = "Nam"
                } else if (text.contains("Nữ", ignoreCase = true)) {
                    dataMap["gender"] = "Nữ"
                }
            } else if (text.contains("Nam", ignoreCase = true)) {
                dataMap["gender"] = "Nam"
            } else if (text.contains("Nữ", ignoreCase = true)) {
                dataMap["gender"] = "Nữ"
            }

            // Kiểm tra thông tin số CCCD
            val cccdNumber = text.split(" ").find { it.matches(Regex("^[0-9]{12}\$")) }
            if (cccdNumber != null) {
                dataMap["cccd_number"] = cccdNumber
            }

            // Kiểm tra thông tin quê quán
            if (text.contains("Quê quán", ignoreCase = true) || text.contains("Place of origin", ignoreCase = true)) {
                if (i + 1 < lines.size) {
                    val permanentAddress = lines[i + 1].text.trim()
                    dataMap["address"] = removeVietnameseAccents(permanentAddress)
                }
            }

            // Kiểm tra thông tin quốc tịch
            if (text.contains("Quốc tịch", ignoreCase = true)) {
                val nationality = text.replace("Quốc tịch", "", ignoreCase = true)
                    .replace("Nationality:", "", ignoreCase = true)
                    .replace("/", "")
                    .trim()
                if (nationality.isNotEmpty()) {
                    dataMap["nationality"] = removeVietnameseAccents(nationality)
                }
            }

            // Kiểm tra thông tin ngày cấp
            if (text.contains("Ngày, tháng, năm", ignoreCase = true) || text.contains("Date, month, year", ignoreCase = true)) {
                val issueDateRegex = Regex("(?<=[:\\s])\\d{2}/\\d{2}/\\d{4}")
                val match = issueDateRegex.find(text)
                if (match != null) {
                    dataMap["issue_date"] = match.value
                }
            }
        }

        return dataMap
    }


    // Hàm loại bỏ dấu tiếng Việt
    private fun removeVietnameseAccents(input: String): String {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
            .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
    }
}
