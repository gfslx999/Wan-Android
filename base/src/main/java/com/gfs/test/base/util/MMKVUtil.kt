package com.gfs.test.base.util

import android.content.Context
import com.tencent.mmkv.MMKV

/**
 * MMKV 工具类
 *
 * 内存映射轻量级存储方案
 */
object MMKVUtil {

    private val mmkv: MMKV by lazy { MMKV.mmkvWithID("wan_android_mmkv", Context.MODE_PRIVATE) }

    /**
     * 初始化，需在 application 中调用
     */
    internal fun initialize(context: Context) {
        MMKV.initialize(context)
    }

    fun save(key: String?, content: Any?): Boolean {
        if (key.isNullOrEmpty()) {
            LogUtil.logE("MMKVUtil save field, key is null or empty!")
            return false
        }
        if (content == null) {
            LogUtil.logE("MMKVUtil save field, content is null!")
            return false
        }
        return when (content) {
            is String -> mmkv.encode(key, content)
            is Boolean -> mmkv.encode(key, content)
            is Int -> mmkv.encode(key, content)
            is Long -> mmkv.encode(key, content)
            is Float -> mmkv.encode(key, content)
            is Double -> mmkv.encode(key, content)
            else -> {
                LogUtil.logE("MMKVUtil save field, only support：String、Boolean、Int、Long、Float、Double")
                false
            }
        }
    }

    fun save(key: String?, value: String) {
        if (key.isNullOrEmpty()) {
            return
        }
        mmkv.encode(key, value)
    }

    fun save(key: String?, value: Int) {
        if (key.isNullOrEmpty()) {
            return
        }
        mmkv.encode(key, value)
    }

    fun save(key: String?, value: Long) {
        if (key.isNullOrEmpty()) {
            return
        }
        mmkv.encode(key, value)
    }

    fun save(key: String?, value: Float) {
        if (key.isNullOrEmpty()) {
            return
        }
        mmkv.encode(key, value)
    }

    fun save(key: String?, value: Double) {
        if (key.isNullOrEmpty()) {
            return
        }
        mmkv.encode(key, value)
    }

    fun save(key: String?, value: Boolean) {
        if (key.isNullOrEmpty()) {
            return
        }
        mmkv.encode(key, value)
    }

    fun getString(key: String, defaultValue: String = ""): String = mmkv.decodeString(key, defaultValue)

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean = mmkv.decodeBool(key, defaultValue)

    fun getInt(key: String, defaultValue: Int = 0): Int = mmkv.decodeInt(key, defaultValue)

    fun getLong(key: String, defaultValue: Long = 0L): Long = mmkv.decodeLong(key, defaultValue)

    fun getDouble(key: String, defaultValue: Double = 0.0): Double = mmkv.decodeDouble(key, defaultValue)

    fun getFloat(key: String, defaultValue: Float = 0f): Float = mmkv.decodeFloat(key, defaultValue)

    fun delete(key: String?) {
        if (key.isNullOrEmpty()) {
            return
        }
        if (mmkv.containsKey(key)) {
            mmkv.removeValueForKey(key)
        }
    }

    fun clear() {
        mmkv.clearAll()
    }

}