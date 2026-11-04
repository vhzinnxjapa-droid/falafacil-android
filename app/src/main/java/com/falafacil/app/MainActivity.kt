package com.falafacil.app

import android.app.Activity
import android.os.Bundle
import android.content.ClipboardManager
import android.content.Context
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40, 80, 40, 40)

        val title = TextView(this)
        title.text = "FalaFácil"
        title.textSize = 32f

        status = TextView(this)
        status.text = "Copie um texto e toque no botão abaixo."
        status.textSize = 18f

        val button = Button(this)
        button.text = "Ler texto copiado"

        button.setOnClickListener {
            val clipboard = getSystemService(
                Context.CLIPBOARD_SERVICE
            ) as ClipboardManager

            val text = clipboard.primaryClip
                ?.getItemAt(0)?.coerceToText(this)?.toString()

            if (!text.isNullOrBlank()) {
                status.text = "Lendo texto..."
                tts?.speak(
                    text,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "fala_facil"
                )
            } else {
                status.text = "Nenhum texto copiado."
            }
        }

        layout.addView(title)
        layout.addView(status)
        layout.addView(button)

        setContentView(layout)
    }

    override fun onInit(statusCode: Int) {
        if (statusCode == TextToSpeech.SUCCESS) {
            tts?.language = Locale("pt", "BR")
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
