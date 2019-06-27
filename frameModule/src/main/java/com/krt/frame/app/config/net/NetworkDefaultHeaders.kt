package com.krt.frame.app.config.net

import com.lzy.okgo.model.HttpHeaders

class NetworkDefaultHeaders(
        var action: () -> HttpHeaders
)