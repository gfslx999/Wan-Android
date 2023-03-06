package com.gfs.test.wanandroid.mvvm.model

data class TestSortModel(
    val title: String,
    val sequenceType: Int,
    val age: Int
) {
    companion object {
        const val SEQUENCE_TYPE_MAX = 9
        const val SEQUENCE_TYPE_AGE = 7
        const val SEQUENCE_TYPE_MIN = 5
    }
}
