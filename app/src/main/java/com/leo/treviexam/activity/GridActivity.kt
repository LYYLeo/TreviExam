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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_grid.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class GridActivity: AppCompatActivity() {

    companion object {
        private const val EXTRA_KEY_COLUMN_AMT = "EXTRA_KEY_COLUMN_AMT"
        private const val EXTRA_KEY_ROW_AMT = "EXTRA_KEY_ROW_AMT"
        private const val SEPARATOR_COORDINATE = ","

        /** custom settings */
        private const val INTERVAL_RANDOM = 10L // in seconds
        private const val UNSELECTED_ALPHA = 0.5f
        private const val BUTTON_HEIGHT_IN_DP = 40
        private const val CELL_MARGIN_IN_DP = 2

        fun start(context: Context, columnAmt: Int, rowAmt: Int) {
            val intent = Intent(context, GridActivity::class.java)
            intent.putExtra(EXTRA_KEY_COLUMN_AMT, columnAmt)
            intent.putExtra(EXTRA_KEY_ROW_AMT, rowAmt)
            context.startActivity(intent)
        }
    }

    private val columnAmt get() = intent.getIntExtra(EXTRA_KEY_COLUMN_AMT, 0)
    private val rowAmt get() = intent.getIntExtra(EXTRA_KEY_ROW_AMT, 0)

    private val cells = mutableListOf<TextView>()
    private val btns = mutableListOf<Button>()

    private lateinit var intervalDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
        (0 until rowAmt).onEach { rootView.addView(makeCellRow(it)) }
        rootView.addView(makeConfirmBtnRow())
        intervalDisposable = subscribeInterval()
    }

    override fun onDestroy() {
        super.onDestroy()
        intervalDisposable.dispose()
    }

    private fun makeCellRow(rowIndex: Int): View {
        val row = LinearLayout(this)
        row.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        row.orientation = LinearLayout.HORIZONTAL
        (0 until columnAmt).onEach { row.addView(makeCell(it, rowIndex)) }
        return row
    }

    private fun makeCell(columnIndex: Int, rowIndex: Int): View {
        val cell = TextView(this)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        layoutParams.setMargins(toPx(CELL_MARGIN_IN_DP), toPx(CELL_MARGIN_IN_DP), toPx(CELL_MARGIN_IN_DP), toPx(CELL_MARGIN_IN_DP))
        cell.layoutParams = layoutParams
        cell.setBackgroundColor(ContextCompat.getColor(this, if (rowIndex % 2 == 0) R.color.colorPrimary else R.color.colorPrimaryDark))
        cell.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        cell.gravity = Gravity.CENTER
        cell.alpha = UNSELECTED_ALPHA
        cell.tag = "$columnIndex$SEPARATOR_COORDINATE$rowIndex"
        cells.add(cell)
        return cell
    }

    private fun makeConfirmBtnRow(): View {
        val row = LinearLayout(this)
        row.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, toPx(BUTTON_HEIGHT_IN_DP))
        row.orientation = LinearLayout.HORIZONTAL
        (0 until columnAmt).onEach { row.addView(makeConfirmBtn(it)) }
        return row
    }

    private fun makeConfirmBtn(columnIndex: Int): View {
        val btn = Button(this)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        layoutParams.setMargins(toPx(CELL_MARGIN_IN_DP), toPx(CELL_MARGIN_IN_DP), toPx(CELL_MARGIN_IN_DP), toPx(CELL_MARGIN_IN_DP))
        btn.layoutParams = layoutParams
        btn.setBackgroundResource(R.drawable.bg_border_primary_3dp)
        btn.setText(R.string.confirm)
        btn.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        btn.alpha = UNSELECTED_ALPHA
        btn.tag = columnIndex.toString()
        btn.setOnClickListener {
            cells.forEach { cell ->
                cell.text = ""
                cell.alpha = UNSELECTED_ALPHA
            }
            btn.alpha = UNSELECTED_ALPHA
            btn.isEnabled = false
        }
        btns.add(btn)
        return btn
    }

    private fun subscribeInterval() = Observable.interval(INTERVAL_RANDOM, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            val randomColumn = Random.nextInt(0, columnAmt)
            val randomRow = Random.nextInt(0, rowAmt)
            cells.forEach { cell ->
                val coordinate = cell.tag.toString().split(SEPARATOR_COORDINATE).map { it.toInt() }
                cell.alpha = if (coordinate[0] == randomColumn) 1f else UNSELECTED_ALPHA
                cell.text = if (coordinate[0] == randomColumn && coordinate[1] == randomRow) "random" else ""
            }
            btns.forEach { btn ->
                val column = btn.tag.toString().toInt()
                btn.alpha = if (column == randomColumn) 1f else UNSELECTED_ALPHA
                btn.isEnabled = column == randomColumn
            }
        }
}