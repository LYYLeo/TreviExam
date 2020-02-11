package com.leo.treviexam.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.leo.treviexam.R
import kotlinx.android.synthetic.main.activity_config.*

class ConfigActivity : AppCompatActivity() {

    companion object {
        const val MAX_COLUMN_AMT = 20
        const val MAX_ROW_AMT = 20
    }

    private val columnAmt get() = columnEditText.text.toString().toIntOrNull()
    private val rowAmt get() = rowEditText.text.toString().toIntOrNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        goBtn.setOnClickListener {
            when {
                columnAmt == null || rowAmt == null -> showErrorDialog(R.string.input_invalid)
                columnAmt!! !in 1..MAX_COLUMN_AMT || rowAmt!! !in 1..MAX_ROW_AMT -> showErrorDialog(R.string.input_out_of_bound)
                else -> GridActivity.start(this, columnAmt!!, rowAmt!!)
            }
        }
    }

    private fun showErrorDialog(message: Int) {
        AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .create()
                .show()
    }
}
