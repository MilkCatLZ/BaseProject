package com.base.net

import com.google.gson.JsonSyntaxException
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 拦截器，主要功能是拦截url和参数。方便重新构造url和添加公共参数
 */
abstract class BaseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        val method = request.method()

        try {

            //公共参数

            request = if ("GET" == method) {
                getMethodRequest(request)
            } else if ("POST" == method || "PUT" == method || "DELETE" == method) {
                postMethodRequest(request)
            } else {
                request
            }

        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }

        return chain.proceed(request)
    }

    /**
     * Post请求处理
     *
     * @param request
     *
     * @return
     */
    private fun postMethodRequest(request: Request): Request {
        val rootParams = HashMap<String, String>()
        val formBody = FormBody.Builder()
        val body = request.body()
        if (body is FormBody) {
            for (i in 0 until body.size()) {
                rootParams[body.encodedName(i)] = body.encodedValue(i)
                formBody.addEncoded(body.encodedName(i), body.encodedValue(i))
            }
        }
        var url = request.url().toString()
        val index = url.indexOf("?")
        if (index > 0) {
            url = url.substring(0, index)
        }
        val builder = request.newBuilder().url(postUrl(url, rootParams))
        builder.method(request.method(), formBody.build())
        return builder.build()
    }


    /**
     * Get请求
     *
     * @param request
     *
     * @return
     */
    private fun getMethodRequest(request: Request): Request {
        val rootParams = HashMap<String, String>()
        val mHttpUrl = request.url()
        val paramNames = mHttpUrl.queryParameterNames()


        for (key in paramNames) {
            rootParams[key] = mHttpUrl.queryParameter(key)!!
        }
        var url = mHttpUrl.toString()
        val index = url.indexOf("?")
        if (index > 0) {
            url = url.substring(0, index)
        }
        //重新构建请求
        var newRequest=request.newBuilder().url(getUrl(url, rootParams))
        addHeader(newRequest)
        return newRequest.build()
    }

    /**
     * 添加header,有需要的在这里添加
     */
    protected fun addHeader(newRequest: Request.Builder) {

    }

    /**
     * 拦截到的url和参数，如果需要重构url和参数，在这里进行
     */
    abstract fun postUrl(url: String, rootParams: HashMap<String, String>): String

    /**
     *
     * 拦截到的url和参数，如果需要重构url和参数，在这里进行
     * @param url        原始的url
     * @param rootParams 原始的参数列表
     *
     * @return
     */
    abstract fun getUrl(url: String, rootParams: HashMap<String, String>): String

    /**
     * 重建url，主要目的是加上公共参数
     */
    fun createUrl(url: String, rootParams: HashMap<String, String>): String {
        var newUrl = "$url?"
        for (entry in rootParams) {
            newUrl += "${entry.key}=${entry.value}&"
        }
        newUrl = newUrl.substring(0, newUrl.length - 1)
        return newUrl
    }

}



