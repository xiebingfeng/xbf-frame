package com.krt.frame.navigation


import android.os.Bundle
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.krt.frame.R
import com.krt.frame.frame.fragment.BaseFragment
import com.krt.frame.frame.fragment.BaseVMFragment
import com.krt.frame.frame.mvvm.BaseViewModel
import com.krt.frame.navigation.bean.NavigationTabBean
import me.yokeyword.fragmentation.ISupportFragment
import java.util.*

/**
 * 没有业务逻辑代码，就是普通的Fragment跳转，包含底部功能
 */
abstract class BaseNavigationFragment<VM : BaseViewModel> : BaseVMFragment<VM>(), View.OnClickListener {

    //底部Item列表
    private val TAB_BEANS = ArrayList<NavigationTabBean>()
    private val ITEM_DELEGATES = ArrayList<BaseFragment>()
    private val ITEMS = LinkedHashMap<NavigationTabBean, BaseFragment>()
    private var mCurrentPosition = -1

    abstract fun setItems(builder: NavigationItemBuilder): LinkedHashMap<NavigationTabBean, BaseFragment>

    abstract fun setIndexDelegate(): Int

    abstract fun getBottomContainer(): ViewGroup

    abstract fun getFragmentContainerId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //通过子类创建需要的Items
        val builder = NavigationItemBuilder.builder()
        val items = setItems(builder)
        ITEMS.putAll(items)
        for ((key, value) in ITEMS) {
            TAB_BEANS.add(key)
            ITEM_DELEGATES.add(value)
        }
    }

    /**
     * 初始化Bottom Items
     */
    override fun initView() {
        mCurrentPosition = setIndexDelegate()
        val size = TAB_BEANS.size
        for (i in 0 until size) {
            LayoutInflater.from(context).inflate(R.layout.frame_navigation_item_icon_text_layout, getBottomContainer())
            val item = getBottomContainer().getChildAt(i) as LinearLayout
            //设置每个item的点击事件
            item.tag = i
            item.setOnClickListener(this)
            val itemIv = item.getChildAt(0) as AppCompatImageView
            val itemTitle = item.getChildAt(1) as AppCompatTextView
            val bean = TAB_BEANS[i]
            //初始化数据
            itemIv.setImageResource(bean.drawableId)
            itemTitle.text = bean.title

            TAB_BEANS[i].iconView = itemIv

            if (i == mCurrentPosition) {
                itemIv.isSelected = true
                itemTitle.isSelected = true
            }
        }

        val delegateArray = ITEM_DELEGATES.toTypedArray<ISupportFragment>()
        supportDelegate.loadMultipleRootFragment(getFragmentContainerId(), mCurrentPosition, *delegateArray)
    }

    override fun onClick(v: View) {
        val tag = v.tag as Int
        if (tag == mCurrentPosition) {
            return
        }

        resetColor()
        val item = v as LinearLayout
        val itemIcon = item.getChildAt(0) as View
        if (itemIcon is ViewGroup) {
            val childCount = itemIcon.childCount
            for (i in 0 until childCount) {
                itemIcon.getChildAt(i).isSelected = true
            }
        }

        val itemTitle = item.getChildAt(1) as AppCompatTextView
        itemIcon.isSelected = true
        itemTitle.isSelected = true
        supportDelegate.showHideFragment(ITEM_DELEGATES[tag], ITEM_DELEGATES[mCurrentPosition])
        //注意先后顺序
        mCurrentPosition = tag
    }

    /**
     * Bottom Bar恢复默认颜色
     */
    private fun resetColor() {
        val count = getBottomContainer().childCount
        for (i in 0 until count) {
            val item = getBottomContainer().getChildAt(i) as LinearLayout
            val itemIcon = item.findViewById<AppCompatImageView>(R.id.iv_bottom_item)
            val itemTitle = item.findViewById<AppCompatTextView>(R.id.tv_bottom_item)
            itemIcon.isSelected = false
            itemTitle.isSelected = false
        }
    }

}
