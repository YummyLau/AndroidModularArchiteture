package com.effective.com.service.media

import com.effective.android.service.media.ServiceVideoloader
import com.plugin.component.anno.AutoInjectImpl

@AutoInjectImpl(sdk = [ServiceVideoloader::class])
class VideoloaderImpl : ServiceVideoloader{
}