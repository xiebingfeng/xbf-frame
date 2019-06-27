package com.krt.frame.frame.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.KeyboardUtils
import com.krt.frame.R
import com.krt.frame.app.config.KRT
import com.krt.frame.frame.toolbar.ToolBarConfig
import com.krt.frame.frame.toolbar.ToolBarConfigHelper
import com.krt.frame.frame.toolbar.manager.ToolBarProxy
import com.krt.frame.frame.toolbar.style.ToolBarStyle
import com.krt.frame.netmonitor.NetworkChangeManager
import com.krt.frame.permission.PermissionDelegate
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.SwipeBackLayout
import me.yokeyword.fragmentation_swipeback.core.ISwipeBackFragment
import me.yokeyword.fragmentation_swipeback.core.SwipeBackFragmentDelegate
import org.greenrobot.eventbus.EventBus

abstract class BaseFragment : SupportFragment(), ISwipeBackFragment {

    private var mSwipeBackDelegate: SwipeBackFragmentDelegate? = null

    /**
     * 获取 权限管理工具代理类
     */
    val permissionDelegate by lazy {
        PermissionDelegate.Factory().create(activity!!)
    }

    private lateinit var toolBarConfig: ToolBarConfig

    protected lateinit var toolBarConfigHelper: ToolBarConfigHelper

    private var isEventBusRegistered = false

    protected var actionCallBackWithCode: ((resultCode: Int, data: Array<Any>?) -> Unit)? = null

    //无差异回调，只有保证没问题的情况下才使用
    protected var actionCallBack: ((data: Any?) -> Unit)? = null

    protected var actionCallBackWithArgs: ((data: Array<Any>) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolBarConfig = initToolBarConfig()
        initArguments()
        toolBarConfig.currentFragment = this

        if (toolBarConfig.contentSwipeBackEnabled) {
            mSwipeBackDelegate = SwipeBackFragmentDelegate(this)
            mSwipeBackDelegate!!.onCreate(savedInstanceState)
        }

        //检测EventBus是否需要注册
        if (toolBarConfig.autoCheckEventBus) {
            for (method in this.javaClass.declaredMethods) {
                if (method.isAnnotationPresent(org.greenrobot.eventbus.Subscribe::class.java)) {
                    isEventBusRegistered = true
                    break
                }
            }
        }

        if (isEventBusRegistered) {
            EventBus.getDefault().register(this)
        }

        if (toolBarConfig.openNetWorkChange) {
            lifecycle.addObserver(NetworkChangeManager())
        }

        //TODO 应写成策略模式
//        MobclickAgent.onPageStart(javaClass.name)
    }

    /**
     * Fragment提供一个默认而已，主要就是包含 ToolBar和ContentLayout，根据不同的层叠关系，用不同的而已，
     * 但它们的ID多是一样的
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = if (toolBarConfig.toolBarStyle == ToolBarStyle.NONE
                || toolBarConfig.toolBarStyle == ToolBarStyle.NORMAL
        ) {
            inflater.inflate(R.layout.frame_fragment_default_root_view, null, false)
        } else {
            inflater.inflate(R.layout.frame_fragment_default_root_view_immerse, null, false)
        }

        inflater.inflate(toolBarConfig.layoutId, rootView.findViewById(R.id.core_contentView), true)

        ToolBarProxy().init(rootView, toolBarConfig)

        toolBarConfig.contentBackgroundColor?.let { color ->
            context?.let {
                rootView.setBackgroundColor(color)
            }
        }

        toolBarConfigHelper = ToolBarConfigHelper(rootView.findViewById(R.id.tool_bar_container))

        return if (toolBarConfig.contentSwipeBackEnabled) {
            attachToSwipeBack(rootView)
        } else {
            rootView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (toolBarConfig.contentSwipeBackEnabled) {
            mSwipeBackDelegate?.onViewCreated(view, savedInstanceState)
        }

        if (!toolBarConfig.isLazyInitView) {
            init()
        }
    }

    var isAnimationEnd = false

    var showChildFragmentEnterAnimationEnd = false

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if (toolBarConfig.isLazyInitView) {
            init()
        }
    }

    private fun init() {
        initView()
        initViewClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isEventBusRegistered) {
            EventBus.getDefault().unregister(this)
        }

        mSwipeBackDelegate?.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        KeyboardUtils.hideSoftInput(activity)
        KeyboardUtils.fixSoftInputLeaks(this.context)
        KRT.refWatcher?.watch(this)
    }

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    /*
     *初始化View
     */
    protected abstract fun initView()

    /**
     * 因项目是用kotlin写的，这样写方便，统一管理
     */
    open fun initViewClickListener() {

    }

    /**
     * 初始化传过来的arguments 参数
     */
    open fun initArguments() {

    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        hideSoftInput()
    }

    /**
     * resultCode,
     * data
     */
    fun onCallBackWithArgs(callBack: (data: Array<Any>) -> Unit): BaseFragment {
        this.actionCallBackWithArgs = callBack
        return this
    }

    fun onCallBackWithCode(callBack: (resultCode: Int, data: Array<Any>?) -> Unit): BaseFragment {
        this.actionCallBackWithCode = callBack
        return this
    }

    fun onCallBack(callBack: (data: Any?) -> Unit): BaseFragment {
        this.actionCallBack = callBack
        return this
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        isAnimationEnd = true
        if (showChildFragmentEnterAnimationEnd) {
            childFragmentManager.fragments.forEach {
                if (it is BaseVMFragment<*>) {
                    it.initViewModelLiveDataAfterAnimationEnd()
                }
            }
        }
    }

    protected abstract fun initToolBarConfig(): ToolBarConfig

    protected fun getToolBarConfig() = toolBarConfig

    /**
     * 直接获取 Parent Fragment，不用再转换了
     */
    fun getParentFragmentByThis(): BaseFragment? {
        return parentFragment as? BaseFragment
    }

    //SWIPE BACK

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mSwipeBackDelegate!!.swipeBackLayout
    }

    override fun attachToSwipeBack(view: View?): View {
        return mSwipeBackDelegate!!.attachToSwipeBack(view)
    }

    override fun setEdgeLevel(edgeLevel: SwipeBackLayout.EdgeLevel?) {
        mSwipeBackDelegate?.setEdgeLevel(edgeLevel)
    }

    override fun setEdgeLevel(widthPixel: Int) {
        mSwipeBackDelegate?.setEdgeLevel(widthPixel)
    }

    override fun setParallaxOffset(offset: Float) {
        mSwipeBackDelegate?.setParallaxOffset(offset)
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        mSwipeBackDelegate?.setSwipeBackEnable(enable)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mSwipeBackDelegate?.onHiddenChanged(hidden)
    }
}
