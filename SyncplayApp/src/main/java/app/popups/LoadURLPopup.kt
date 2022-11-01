package app.popups

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.core.net.toUri
import app.R
import app.controllers.activity.RoomActivity
import app.controllers.activity.SoloActivity
import app.utils.ExoPlayerUtils.injectVideo
import app.utils.RoomUtils.string
import app.utils.UIUtils.toasty
import app.wrappers.MediaFile
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import razerdp.basepopup.BasePopupWindow

class LoadURLPopup(val act1: RoomActivity?, val act2: SoloActivity?) : BasePopupWindow(act1 ?: act2) {
    init {
        setContentView(R.layout.popup_load_url)

        val edittext = findViewById<TextInputEditText>(R.id.url_edittext)

        findViewById<MaterialButton>(R.id.url_confirm).setOnClickListener {
            it.visibility = View.GONE
            val url = edittext.text.toString().trim()
            if (url.isNotBlank()) {
                if (act1 == null) {
                    act2?.injectVideo(url)
                } else {
                    act1.injectVideo(url, true)
                    act1.p.file = MediaFile()
                    act1.p.file?.uri = url.toUri()
                    act1.p.file?.url = url
                    act1.p.file?.collectInfoURL()
                    context.toasty(
                        act1.string(
                            R.string.room_selected_vid,
                            "${act1.p.file?.fileName}"
                        )
                    )
                    //act1.p.sendPacket(JsonSender.sendFile(act1.p.file ?: return@setOnClickListener, act1))
                }
                dismiss()
            }
        }
        findViewById<ImageButton>(R.id.url_paste).setOnClickListener {
            edittext.setText((context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).text.toString())
            context.toasty("Pasted clipboard content")
        }

        findViewById<FrameLayout>(R.id.loadurl_popup_dismisser).setOnClickListener {
            this.dismiss()
        }
    }
}