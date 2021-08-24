package com.fr1014.mycoludmusic.http.restructure

/**
 * Time: 2021/8/6
 * Author: fanrui07
 * Description:
 */
data class Rest<T>(
        val code: Int,
        val message: String,
        val data: T?
)