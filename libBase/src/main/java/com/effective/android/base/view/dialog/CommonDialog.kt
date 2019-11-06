package com.effective.android.base.view.dialog

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog
import com.effective.android.base.R
import kotlinx.android.synthetic.main.base_common_dialog_layout.*
import kotlinx.android.synthetic.main.base_title_layout.*

class CommonDialog : AppCompatDialog {

    constructor(context: Context) : super(context) {
        setContentView(LayoutInflater.from(context).inflate(R.layout.base_common_dialog_layout, null, false))
        window?.setGravity(Gravity.CENTER)
        window?.attributes?.width = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        setCanceledOnTouchOutside(true)
    }

    class Builder {

        private val context: Context
        private var title: CharSequence? = null
        private var content: CharSequence? = null
        private var left: Pair<CharSequence, View.OnClickListener?>? = null
        private var right: Pair<CharSequence, View.OnClickListener?>? = null
        private var single: Pair<CharSequence, View.OnClickListener?>? = null

        constructor(context: Context) {
            this.context = context
        }

        fun title(title: CharSequence): Builder {
            this.title = title
            return this
        }

        fun title(resId: Int): Builder = title(context.getString(resId))

        fun content(content: CharSequence): Builder {
            this.content = content
            return this
        }

        fun content(resId: Int): Builder = content(context.getString(resId))

        @JvmOverloads
        fun left(left: CharSequence, clickListener: View.OnClickListener? = null): Builder {
            this.left = Pair(left, clickListener)
            return this
        }

        @JvmOverloads
        fun left(resId: Int, clickListener: View.OnClickListener? = null): Builder = left(context.getString(resId), clickListener)

        @JvmOverloads
        fun right(right: CharSequence, clickListener: View.OnClickListener? = null): Builder {
            this.right = Pair(right, clickListener)
            return this
        }

        @JvmOverloads
        fun right(resId: Int, clickListener: View.OnClickListener? = null): Builder = right(context.getString(resId), clickListener)

        @JvmOverloads
        fun single(single: CharSequence, clickListener: View.OnClickListener? = null): Builder {
            this.single = Pair(single, clickListener)
            return this
        }

        @JvmOverloads
        fun single(resId: Int, clickListener: View.OnClickListener? = null): Builder = single(context.getString(resId), clickListener)

        fun build(): CommonDialog {
            val result = CommonDialog(context)
            if (!title.isNullOrEmpty()) {
                result.tv_title.text = title
                result.tv_title.visibility = View.VISIBLE
            } else {
                result.tv_title.visibility = View.GONE
            }

            if (!content.isNullOrEmpty()) {
                result.tv_content.text = content
                result.ll_content.visibility = View.VISIBLE
            } else {
                result.ll_content.visibility = View.GONE
            }

            if (left != null && right != null) {
                result.layout_left.visibility = View.VISIBLE
                result.tv_left.text = left?.first
                result.layout_left.setOnClickListener {
                    result.dismiss()
                    left?.second?.onClick(it)
                }

                result.layout_right.visibility = View.VISIBLE
                result.tv_right.text = right?.first
                result.layout_right.setOnClickListener {
                    result.dismiss()
                    right?.second?.onClick(it)
                }

                result.divider.visibility = View.VISIBLE
                return result
            }

            if (left != null) {
                single = left
            }

            if (right != null) {
                single = right
            }

            if (single != null) {
                result.layout_left.visibility = View.VISIBLE
                result.layout_right.visibility = View.GONE
                result.divider.visibility = View.GONE
                result.tv_left.text = single?.first
                result.layout_left.setOnClickListener {
                    result.dismiss()
                    single?.second?.onClick(it)
                }
            }

            return result
        }
    }
}