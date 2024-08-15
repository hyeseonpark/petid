package com.android.petid.view.generate

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.databinding.ActivitySignatureBinding

class SignatureActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignatureBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonNext.button.setOnClickListener{
            val intent = Intent(this, CompleteCardActivity::class.java)
            val bitmap = getBitmapFromView(binding.drawingViewSignature)
//            val base64Bitmap = bitmapToBase64(bitmap)
//            sendBitmapToServer(base64Bitmap)
            startActivity(intent)
        }
    }

    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}
// 서버 전송 시
//import android.util.Base64
//import java.io.ByteArrayOutputStream
//
//fun bitmapToBase64(bitmap: Bitmap): String {
//    val byteArrayOutputStream = ByteArrayOutputStream()
//    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//    val byteArray = byteArrayOutputStream.toByteArray()
//    return Base64.encodeToString(byteArray, Base64.DEFAULT)
//}
//
//fun sendBitmapToServer(base64Bitmap: String) {
//    // Volley 라이브러리를 사용한 예시
//    val url = "https://yourserver.com/upload"
//    val request = object : StringRequest(Method.POST, url,
//        Response.Listener { response ->
//            // 서버 응답 처리
//        },
//        Response.ErrorListener { error ->
//            // 오류 처리
//        }) {
//        override fun getParams(): Map<String, String> {
//            val params = HashMap<String, String>()
//            params["image"] = base64Bitmap
//            return params
//        }
//    }
//    val queue = Volley.newRequestQueue(context)
//    queue.add(request)
//}