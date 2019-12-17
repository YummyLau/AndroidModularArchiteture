package com.effective.android.component.tab.recommendation.util

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri

class MediaScanner(context: Context) : MediaScannerConnection.MediaScannerConnectionClient {

    private val conn: MediaScannerConnection
    private var filePath: String? = null
    private var mimeType: String? = null

    init {
        conn = MediaScannerConnection(context, this)
        conn.connect()
    }

    fun scanFile(path: String, mime: String) {
        if (conn.isConnected)
            conn.scanFile(path, mime)
        else {
            filePath = path
            mimeType = mime
        }
    }

    override fun onMediaScannerConnected() {
        if (filePath != null)
            conn.scanFile(filePath, mimeType)

        filePath = null
        mimeType = null
    }

    override fun onScanCompleted(path: String, uri: Uri) {}
}