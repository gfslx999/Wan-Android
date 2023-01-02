package com.gfs.helper.common.network;

import androidx.annotation.Nullable;

import com.gfs.test.base.BuildConfig;
import com.gfs.test.base.exception.NetworkUnAvailableException;
import com.gfs.test.base.util.AppUtil;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * {@linkplain OkHttpClient#interceptors() application interceptor} or as a {@linkplain
 * OkHttpClient#networkInterceptors() network interceptor}. <p> The format of the logs created by
 * this class should not be considered stable and may change slightly between releases. If you need
 * a stable logging format, use your own interceptor.
 */
public final class LoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    public LoggingInterceptor() {
        this(HttpLoggingInterceptor.Logger.DEFAULT);
    }

    public LoggingInterceptor(HttpLoggingInterceptor.Logger logger) {
        this.logger = logger;
    }

    private final HttpLoggingInterceptor.Logger logger;

    private volatile HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;

    public HttpLoggingInterceptor.Level getLevel() {
        return level;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        // 请求前校验网络状态
        if (!AppUtil.INSTANCE.checkNetWorkIsConnect(true)) {
            throw new NetworkUnAvailableException();
        }
        HttpLoggingInterceptor.Level level = this.level;

        Request request = chain.request();
        //添加公共请求头
        Request.Builder requestBuilder = request.newBuilder();

        if (!BuildConfig.DEBUG && level != HttpLoggingInterceptor.Level.NONE) {
            level = HttpLoggingInterceptor.Level.NONE;
        }
        request = requestBuilder.build();

        boolean isNeedLog = level != HttpLoggingInterceptor.Level.NONE;
        boolean logBody = level == HttpLoggingInterceptor.Level.BODY;
        boolean logHeaders = logBody || level == HttpLoggingInterceptor.Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        if (isNeedLog) {
            Connection connection = chain.connection();
            String requestStartMessage = "--> "
                    + request.method()
                    + ' ' + request.url()
                    + (connection != null ? " " + connection.protocol() : "");
            if (!logHeaders && hasRequestBody) {
                requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
            }
            logger.log(requestStartMessage);
        }
        if (logHeaders) {
            toLogHeaders(request, logBody, requestBody, hasRequestBody);
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logger.log("<-- HTTP FAILED: " + e);
            // 在请求失败后，检测网络状态是否可用，不可用就抛出网络不可用异常
            if (!AppUtil.INSTANCE.checkNetWorkIsConnect()) {
                throw new NetworkUnAvailableException();
            }
            throw e;
        }
        if (isNeedLog) {
            Response response1 = toLogResponse(logBody, logHeaders, startNs, response);
            if (response1 != null) return response1;
        }

        return response;
    }

    @Nullable
    private Response toLogResponse(boolean logBody, boolean logHeaders, long startNs, Response response) throws IOException {
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        logger.log("<-- "
                + response.code()
                + (response.message().isEmpty() ? "" : ' ' + response.message())
                + ' ' + response.request().url()
                + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');
        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                logger.log(headers.name(i) + ": " + headers.value(i));
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logger.log("<-- END HTTP");
            } else if (bodyEncoded(response.headers())) {
                logger.log("<-- END HTTP (encoded body omitted)");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (!isPlaintext(buffer)) {
                    logger.log("");
                    logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                    return response;
                }

                if (contentLength != 0) {
                    logger.log("");
                    logger.log(buffer.clone().readString(charset));
                }

                logger.log("<-- END HTTP (" + buffer.size() + "-byte body)");
            }
        }
        return null;
    }

    private void toLogHeaders(Request request, boolean logBody, RequestBody requestBody, boolean hasRequestBody) throws IOException {
        if (hasRequestBody) {
            // Request body headers are only present when installed as a network interceptor. Force
            // them to be included (when available) so there values are known.
            if (requestBody.contentType() != null) {
                logger.log("Content-Type: " + requestBody.contentType());
            }
            if (requestBody.contentLength() != -1) {
                logger.log("Content-Length: " + requestBody.contentLength());
            }
        }

        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            // Skip headers from the request body as they are explicitly logged above.
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                logger.log(name + ": " + headers.value(i));
            }
        }

        if (!logBody || !hasRequestBody) {
            logger.log("--> END " + request.method());
        } else if (bodyEncoded(request.headers())) {
            logger.log("--> END " + request.method() + " (encoded body omitted)");
        } else {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            logger.log("");
            if (isPlaintext(buffer)) {
                logger.log(buffer.readString(charset));
                logger.log("--> END " + request.method()
                        + " (" + requestBody.contentLength() + "-byte body)");
            } else {
                logger.log("--> END " + request.method() + " (binary "
                        + requestBody.contentLength() + "-byte body omitted)");
            }
        }
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
