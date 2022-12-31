package com.gfs.test.base.model

data class ThemeConfigModel(
    // 主题色
    val themeColor: Int,
    // 文本、图标颜色是否为深色
    val superStyleIsDark: Boolean = false
)
