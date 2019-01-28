package com.effective.android.net.okhttp.certificater

import okhttp3.CertificatePinner

/**
 * 证书
 * Created by yummyLau on 2018/8/01.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class Certificaters {

    companion object {
        fun getCertificatePinner(): CertificatePinner = CertificatePinner.Builder().build()
    }
}