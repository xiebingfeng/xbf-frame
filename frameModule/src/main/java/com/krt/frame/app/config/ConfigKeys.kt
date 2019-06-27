package com.krt.frame.app.config

enum class ConfigKeys {
    //是否已初始化完成
    CONFIG_READY,

    //服务器url
    API_HOST,

    // 拦截器
    INTERCEPTOR,

    // Http请求时的默认头
    DEF_HTTP_HEADERS,

    //全局Handler
    HANDLER,

    //基础文件目录
    BASE_FILE_PATH,

    //缓存路径
    CACHE_FILE_PATH,

    //Glide缓存路径
    GLIDE_CACHE_FILE_PATH,

    JAVASCRIPT_INTERFACE,

    CURRENT_ACTIVITY,

    //fragment动画过度时间
    FRAGMENT_SKIP_ANIM_TIME,

    //网络访问定制
    NETWORK_CUSTOM_MADE,

    //网络请求结果适配器（转换器）
    NETWORK_RESULT_ADAPTER,

    //加载Dialog
    LOADING_DIALOG
}