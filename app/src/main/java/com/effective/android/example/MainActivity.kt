package com.effective.android.example

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.effective.android.base.activity.BaseActivity
import com.google.android.material.tabs.TabLayout
import example.androidmodulararchiteture.R
import kotlinx.android.synthetic.main.app_activity_main_layout.*

class MainActivity : BaseActivity() {

    lateinit var componentList: MutableList<ComponentInfo>

    override fun getLayoutRes(): Int {
        return R.layout.app_activity_main_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
    }

    private fun initData() {
        componentList = mutableListOf()
        componentList.add(ComponentInfo("Kotlin(微博)", ComponentFragment()))
        componentList.add(ComponentInfo("Java(玩安卓)", ComponentFragment()))
        componentList.add(ComponentInfo("flutter(玩安卓)", ComponentFragment()))
    }

    private fun initView() {
        pageView.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getItem(position: Int): Fragment = componentList[position].fragment

            override fun getCount(): Int = componentList.size

            override fun getPageTitle(position: Int): CharSequence? = componentList[position].tabName
        }
        tabView.setupWithViewPager(pageView)
    }
}


//class MainActivity : BaseActivity() {
//    override fun getLayoutRes(): Int {
//        return R.layout.app_activity_main_layout
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        module_list.layoutManager = LinearLayoutManager(this)
//        module_list.adapter = Adapter(this,
//                listOf(Item("微博组件", false)))
//    }
//
//    data class Item(var name: String = "名称", var open: Boolean = false)
//
//
//    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        fun bindData(item: Item) {
//            setEnterTip(item.open, item.name)
//            itemView.inject.setOnCheckedChangeListener { _, checkedId ->
//                when (checkedId) {
//                    R.id.radio0 -> {
//                        item.open = true
//                        setEnterTip(item.open, item.name)
//                    }
//                    R.id.radio1 -> {
//                        item.open = false
//                        setEnterTip(item.open, item.name)
//                    }
//                }
//            }
//            itemView.enter.setOnClickListener {
//                if (item.open) {
//                    Toast.makeText(itemView.context, "跳转组件", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(itemView.context, "请先注册组件", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//
//        private fun setEnterTip(enableEnter: Boolean, tip: String) {
//            if (enableEnter) {
//                itemView.enter.text = "点击进入(${tip})"
//            } else {
//                itemView.enter.text = "未注册(${tip})"
//            }
//        }
//    }
//
//
//    class Adapter(context: Context, list: List<Item>) : RecyclerView.Adapter<Holder>() {
//
//        private val mContext = context
//        private val mData = list
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
//            return Holder(LayoutInflater.from(mContext).inflate(R.layout.app_holder_module_layout, parent, false))
//        }
//
//        override fun getItemCount(): Int = mData.size
//
//        override fun onBindViewHolder(holder: Holder, position: Int) {
//            holder.bindData(mData[position])
//        }
//    }
//}
