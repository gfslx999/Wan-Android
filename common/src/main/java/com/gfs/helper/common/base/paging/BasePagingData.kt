package com.gfs.helper.common.base.paging

abstract class BasePagingData {
    abstract fun soleId() : String

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}