package com.oldmen.stayintouch.domain.models

data class UserSession(var sortBy: String = "publishedAt",
                       var source: String = "abc-news",
                       var pageSize: Int = 10,
                       var page: Int = 1,
                       var from: String = "",
                       var to: String = "")