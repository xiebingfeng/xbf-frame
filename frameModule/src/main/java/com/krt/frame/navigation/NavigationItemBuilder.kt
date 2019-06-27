package com.krt.frame.navigation


import com.krt.frame.frame.fragment.BaseFragment
import com.krt.frame.navigation.bean.NavigationTabBean
import java.util.*

/**
 * Created by xbf
 */

class NavigationItemBuilder {

    private val ITEMS = LinkedHashMap<NavigationTabBean, BaseFragment>()

    fun addItem(bean: NavigationTabBean, delegate: BaseFragment): NavigationItemBuilder {
        ITEMS[bean] = delegate
        return this
    }

    fun addItems(items: LinkedHashMap<NavigationTabBean, BaseFragment>): NavigationItemBuilder {
        ITEMS.putAll(items)
        return this
    }

    fun build(): LinkedHashMap<NavigationTabBean, BaseFragment> {
        return ITEMS
    }

    companion object {
        internal fun builder(): NavigationItemBuilder {
            return NavigationItemBuilder()
        }
    }
}
