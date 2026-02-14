package com.moviebrowser.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import com.moviebrowser.BuildConfig

/**
 * OkHttp interceptor responsible for attaching
 * required headers to every network request.
 *
 * This keeps authentication logic centralized
 * and avoids repeating headers in every API call.
 */
class AuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        // Builds a new request by adding required headers
        val request = chain.request().newBuilder()

            // Accept header to specify response format
            .addHeader("accept", "application/json")

            // Authorization header using TMDB Bearer token
            // Token is securely stored in BuildConfig
            .addHeader(
                "Authorization",
                "Bearer ${BuildConfig.TMDB_TOKEN}"
            )
            .build()

        // Proceeds with modified request
        return chain.proceed(request)
    }
}
