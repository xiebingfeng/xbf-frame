package com.krt.network.test

import com.alibaba.fastjson.annotation.JSONField

object TestUrl {

    const val objectData = "https://xiebingfeng.oss-cn-hangzhou.aliyuncs.com/net_test.txt"

    const val listData1 = "https://xiebingfeng.oss-cn-hangzhou.aliyuncs.com/net_test_list_1.txt"

    //加载更多用的
    const val listData2 = "https://xiebingfeng.oss-cn-hangzhou.aliyuncs.com/net_test_list_2.txt"

    const val test_head_url =
            "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1910173974,2425346557&fm=58&bpow=2430&bpoh=3240"

    const val test_pic_url = "https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike272%2C5%2C5%2C272%2C90/sign=67b5af6d9c58d109d0eea1e0b031a7da/c83d70cf3bc79f3d423d2823b4a1cd11738b29c1.jpg"

    data class TestObject(
            @JSONField(name = "name")
            val name: String?
    )

    data class TestList(
            @JSONField(name = "current")
            val current: Int?,
            @JSONField(name = "pages")
            val pages: Int?,
            @JSONField(name = "records")
            val records: List<TestObject>?
    )
}