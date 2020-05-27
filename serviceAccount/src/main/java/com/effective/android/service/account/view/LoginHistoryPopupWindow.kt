package com.effective.android.service.account.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.effective.android.service.account.R
import com.effective.android.service.account.data.db.entity.LoginInfoEntity
import kotlinx.android.synthetic.main.account_pop_login_history.view.*
import kotlinx.android.synthetic.main.account_pop_login_history_item.view.*

/**
 * 登录历史
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2019/11/06.
 */
class LoginHistoryPopupWindow : PopupWindow {

    var callback: Callback? = null
    val context: Context

    constructor(context: Context) : super(context) {
        this.context = context
        contentView = LayoutInflater.from(context).inflate(R.layout.account_pop_login_history, null, false)
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.WHITE))
        contentView.login_list.layoutManager = LinearLayoutManager(context)
    }


    fun show(view: View, list: MutableList<LoginInfoEntity>?, callback: Callback) {
        if (!list.isNullOrEmpty()) {
            this.callback = callback
            val adapter = Adapter(list)
            width = view.width
            contentView.login_list.adapter = adapter
            adapter.notifyDataSetChanged()
            showAsDropDown(view)
        }
    }

    interface Callback {
        fun onLoginInfoSelected(name: String, password: String)
    }


    inner class Adapter(private val list: MutableList<LoginInfoEntity>) : RecyclerView.Adapter<Holder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
                Holder(LayoutInflater.from(this@LoginHistoryPopupWindow.context).inflate(R.layout.account_pop_login_history_item, parent, false))

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(list[position], position == itemCount - 1)
        }
    }


    inner class Holder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(loginInfoEntity: LoginInfoEntity, isLast: Boolean) {

            view.text.text = loginInfoEntity.nickname
            view.setOnClickListener {
                this@LoginHistoryPopupWindow.callback?.onLoginInfoSelected(loginInfoEntity.username, loginInfoEntity.password)
                this@LoginHistoryPopupWindow.dismiss()
            }
            view.divider.visibility =
                    if (isLast) View.GONE else View.VISIBLE
        }
    }
}