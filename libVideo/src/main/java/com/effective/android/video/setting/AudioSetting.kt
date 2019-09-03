package com.effective.android.video.setting

import android.content.Context
import android.media.AudioManager

import androidx.annotation.IntDef

/**
 * 音量管理
 * Created by g8931 on 2018/3/24.
 */

object AudioSetting {

    @IntDef(AudioType.MUSIC, AudioType.CALL, AudioType.SYSTEM, AudioType.RING, AudioType.ALARM, AudioType.NOTIFICATION)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class AudioType {
        companion object {
            const val MUSIC = AudioManager.STREAM_MUSIC
            const val CALL = AudioManager.STREAM_VOICE_CALL
            const val SYSTEM = AudioManager.STREAM_SYSTEM
            const val RING = AudioManager.STREAM_RING
            const val ALARM = AudioManager.STREAM_ALARM
            const val NOTIFICATION = AudioManager.STREAM_NOTIFICATION
        }
    }

    fun getMaxVolume(context: Context, @AudioType type: Int): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.getStreamMaxVolume(type)
    }

    fun getCurrentVolume(context: Context, @AudioType type: Int): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.getStreamVolume(type)
    }

    fun setCurrentVolume(context: Context, @AudioType type: Int, index: Int, flags: Int) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(type, index, flags)
    }

    /**
     * @param context
     * @param type
     * @param direction ADJUST_LOWER 降低
     * ADJUST_RAISE 升高
     * ADJUST_SAME 保持不变
     * @param flags     FLAG_PLAY_SOUND 调整音量时播放声音
     * FLAG_SHOW_UI 调整时显示音量条,就是按音量键出现的那个
     * 0 表示什么也没有
     */
    fun adjustStreamVolume(context: Context, @AudioType type: Int, direction: Int, flags: Int) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.adjustStreamVolume(type, direction, flags)
    }
}
