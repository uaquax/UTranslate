package com.example.utranslate

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val client: OkHttpClient = OkHttpClient()
    private lateinit var translation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val translateBtn: Button = findViewById(R.id.translate_btn)
        val source: EditText = findViewById(R.id.source)
        translation = findViewById(R.id.translation)

        translateBtn.setOnClickListener {
            val textToTranslate = source.text.toString()
            if (textToTranslate.isNotBlank()) {
                translateText(textToTranslate)
            }
        }
    }

    private fun translateText(text: String) {
        val url = "https://lingva.ml/api/v1/en/ru/$text"
        val request = Request.Builder().url(url).build()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && responseData != null) {
                        translation.text = responseData
                    } else {
                        translation.text = "Error: ${response.message}"
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    translation.text = "Error: ${e.message}"
                }
            }
        }
    }
}
