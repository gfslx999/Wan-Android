package com.gfs.helper.common.entity

data class BaseResponseEntity<T>(
    val errorCode: Int,
    val errorMsg: String,
    val data: T?
)
