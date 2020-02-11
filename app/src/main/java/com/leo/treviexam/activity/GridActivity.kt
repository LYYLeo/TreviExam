package com.leo.treviexam.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.leo.treviexam.R
import com.leo.treviexam.extension.toPx
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_grid.*
import java.util.concurrent.TimeUnit

class GridActivity: AppCompatActivity() {

    companion object {
        private const val EXTRA_KEY_COLUMN_AMT = "EXTRA_KEY_COLUMN_AMT"
        private const val EXTRA_KEY_ROW_AMT = "EXTRA_KEY_ROW_AMT"

        fun start(context: Context, columnAmt: Int, rowAmt: Int) {
            val intent = Intent(context, GridActivity::class.java)
            intent.putExtra(EXTRA_KEY_COLUMN_AMT, columnAmt)
            intent.putExtra(EXTRA_KEY_ROW_AMT, rowAmt)
            context.startActivity(intent)
        }
    }

    private val columnAmt get() = intent.getIntExtra(EXTRA_KEY_COLUMN_AMT, 0)
    private val rowAmt get() = intent.getIntExtra(EXTRA_KEY_ROW_AMT, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
        (0 until rowAmt).onEach { rootView.addView(makeRow(it)) }
        rootView.addView(makeConfirmBtnRow())
    }

    private fun makeRow(rowIndex: Int): View {
        val row = LinearLayout(this)
        row.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        row.orientation = LinearLayout.HORIZONTAL
        (0 until columnAmt).onEach { row.addView(makeRowCell(rowIndex)) }
        return row
    }

    private fun makeRowCell(rowIndex: Int): View {
        val cell = TextView(this)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        layoutParams.setMargins(toPx(2), toPx(2), toPx(2), toPx(2))
        cell.layoutParams = layoutParams
        cell.setBackgroundColor(ContextCompat.getColor(this, if (rowIndex % 2 == 0) R.color.colorPrimary else R.color.colorPrimaryDark))
        cell.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        cell.gravity = Gravity.CENTER
        return cell
    }

    private fun makeConfirmBtnRow(): View {
        val row = LinearLayout(this)
        row.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, toPx(40))
        row.orientation = LinearLayout.HORIZONTAL
        (0 until columnAmt).onEach { row.addView(makeConfirmBtn()) }
        return row
    }

    private fun makeConfirmBtn(): View {
        val btn = Button(this)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        layoutParams.setMargins(toPx(2), toPx(2), toPx(2), toPx(2))
        btn.layoutParams = layoutParams
        btn.setBackgroundResource(R.drawable.bg_border_primary_3dp)
        btn.setText(R.string.confirm)
        btn.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        return btn
    }

    private fun subscribeInterval() = Observable.interval(10, TimeUnit.SECONDS)
}