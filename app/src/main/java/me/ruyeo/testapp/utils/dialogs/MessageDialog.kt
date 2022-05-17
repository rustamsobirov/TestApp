package me.ruyeo.testapp.utils.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import me.ruyeo.testapp.R
import me.ruyeo.testapp.databinding.DialogMessageBinding

class MessageDialog(private val message: String, private val dialogText: String? = null) : DialogFragment() {
    lateinit var onClickListener: (() -> Unit)
    private var _bn: DialogMessageBinding? = null
    private val bn get() = _bn!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _bn = DialogMessageBinding.inflate(inflater, container, false)
        return bn.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        bn.apply {
            if (dialogText != null) {
                ok.text = dialogText
            } else {
                ok.text = "OK"
            }
            title.text = message
            ok.setOnClickListener {
                onClickListener.invoke()
                dismiss()
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        _bn = null
    }

}