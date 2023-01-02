package com.gfs.test.base.exception

import java.io.IOException

class NetworkUnAvailableException : IOException("当前网络不可用，请检查网络！") {
}