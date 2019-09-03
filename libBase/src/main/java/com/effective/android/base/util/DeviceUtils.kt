package com.effective.android.base.util

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.os.PowerManager
import android.os.StatFs
import android.provider.Settings
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*
import java.util.regex.Pattern

/**
 * 设备相关的工具类
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2019/06/16.
 */
object DeviceUtils {

    var mTotalMemory = -1L

    /**
     * 是否已经 root 过
     */
    fun isRooted(context: Context): Boolean {
        val isSdk = isGoogleSdk(context)
        val tags = Build.TAGS
        if (!isSdk && tags != null
                && (tags as String).contains("test-keys")) {
            return true
        }
        if (File("/system/app/Superuser.apk").exists()) {
            return true
        }
        val su = "su"
        val locations = arrayOf("/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/")
        for (location in locations) {
            if (File(location + su).exists()) {
                return true
            }
        }
        return false
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun isAdbEnabled(context: Context): Boolean {
        return Settings.Secure.getInt(
                context.getContentResolver(),
                Settings.Global.ADB_ENABLED, 0
        ) > 0
    }

    private fun isGoogleSdk(context: Context): Boolean {
        val str = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return "sdk" == Build.PRODUCT ||
                "google_sdk" == Build.PRODUCT ||
                str == null
    }

    /**
     * Return the version name of device's system.
     *
     * @return the version name of device's system
     */
    fun getSDKVersionName(): String {
        return android.os.Build.VERSION.RELEASE
    }

    /**
     * Return version code of device's system.
     *
     * @return version code of device's system
     */
    fun getSDKVersionCode(): Int {
        return android.os.Build.VERSION.SDK_INT
    }

    /**
     * Return the MAC address.
     *
     * Must hold
     * `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />`,
     * `<uses-permission android:name="android.permission.INTERNET" />`
     *
     * @return the MAC address
     */
    @RequiresPermission(allOf = [ACCESS_WIFI_STATE, INTERNET])
    fun getMacAddress(context: Context): String {
        return getMacAddress(context)
    }

    /**
     * Return the MAC address.
     *
     * Must hold
     * `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />`,
     * `<uses-permission android:name="android.permission.INTERNET" />`
     *
     * @return the MAC address
     */
    @RequiresPermission(allOf = [ACCESS_WIFI_STATE, INTERNET])
    fun getMacAddress(vararg excepts: String, context: Context): String {
        var macAddress = getMacAddressByNetworkInterface()
        if (isAddressNotInExcepts(macAddress, *excepts)) {
            return macAddress
        }
        macAddress = getMacAddressByInetAddress()
        if (isAddressNotInExcepts(macAddress, *excepts)) {
            return macAddress
        }
        macAddress = getMacAddressByWifiInfo(context)
        if (isAddressNotInExcepts(macAddress, *excepts)) {
            return macAddress
        }
        macAddress = getMacAddressByFile()
        return if (isAddressNotInExcepts(macAddress, *excepts)) {
            macAddress
        } else ""
    }

    private fun isAddressNotInExcepts(address: String, vararg excepts: String): Boolean {
        if (excepts == null || excepts.size == 0) {
            return "02:00:00:00:00:00" != address
        }
        for (filter in excepts) {
            if (address == filter) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    private fun getMacAddressByWifiInfo(context: Context): String {
        try {
            val wifi = context
                    .getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifi != null) {
                val info = wifi.connectionInfo
                if (info != null) return info.macAddress
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "02:00:00:00:00:00"
    }

    private fun getMacAddressByNetworkInterface(): String {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                if (ni == null || !ni.name.equals("wlan0", ignoreCase = true)) continue
                val macBytes = ni.hardwareAddress
                if (macBytes != null && macBytes.size > 0) {
                    val sb = StringBuilder()
                    for (b in macBytes) {
                        sb.append(String.format("%02x:", b))
                    }
                    return sb.substring(0, sb.length - 1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "02:00:00:00:00:00"
    }

    private fun getMacAddressByInetAddress(): String {
        try {
            val inetAddress = getInetAddress()
            if (inetAddress != null) {
                val ni = NetworkInterface.getByInetAddress(inetAddress)
                if (ni != null) {
                    val macBytes = ni.hardwareAddress
                    if (macBytes != null && macBytes.size > 0) {
                        val sb = StringBuilder()
                        for (b in macBytes) {
                            sb.append(String.format("%02x:", b))
                        }
                        return sb.substring(0, sb.length - 1)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "02:00:00:00:00:00"
    }

    private fun getInetAddress(): InetAddress? {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp) continue
                val addresses = ni.inetAddresses
                while (addresses.hasMoreElements()) {
                    val inetAddress = addresses.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        val hostAddress = inetAddress.hostAddress
                        if (hostAddress.indexOf(':') < 0) return inetAddress
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

        return null
    }

    private fun getMacAddressByFile(): String {
        var result = ShellUtils.execCmd("getprop wifi.interface", false)
        if (result.result === 0) {
            val name = result.successMsg
            if (name != null) {
                result = ShellUtils.execCmd("cat /sys/class/net/$name/address", false)
                if (result.result === 0) {
                    val address = result.successMsg
                    if (address != null && address!!.length > 0) {
                        return address
                    }
                }
            }
        }
        return "02:00:00:00:00:00"
    }

    /**
     * Return the manufacturer of the product/hardware.
     *
     * e.g. Xiaomi
     *
     * @return the manufacturer of the product/hardware
     */
    fun getManufacturer(): String {
        return Build.MANUFACTURER
    }

    /**
     * Return the model of device.
     *
     * e.g. MI2SC
     *
     * @return the model of device
     */
    fun getModel(): String {
        var model: String? = Build.MODEL
        if (model != null) {
            model = model.trim { it <= ' ' }.replace("\\s*".toRegex(), "")
        } else {
            model = ""
        }
        return model
    }

    /**
     * Return an ordered list of ABIs supported by this device. The most preferred ABI is the first
     * element in the list.
     *
     * @return an ordered list of ABIs supported by this device
     */
    fun getABIs(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Build.SUPPORTED_ABIS
        } else {
            if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
                arrayOf(Build.CPU_ABI, Build.CPU_ABI2)
            } else arrayOf(Build.CPU_ABI)
        }
    }


    /**
     * Shutdown the device
     *
     * Requires root permission
     * or hold `android:sharedUserId="android.uid.system"`,
     * `<uses-permission android:name="android.permission.SHUTDOWN/>`
     * in manifest.
     */
    fun shutdown(context: Context) {
        ShellUtils.execCmd("reboot -p", true)
        val intent = Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN")
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false)
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    /**
     * Reboot the device.
     *
     * Requires root permission
     * or hold `android:sharedUserId="android.uid.system"` in manifest.
     */
    fun reboot(context: Context) {
        ShellUtils.execCmd("reboot", true)
        val intent = Intent(Intent.ACTION_REBOOT)
        intent.putExtra("nowait", 1)
        intent.putExtra("interval", 1)
        intent.putExtra("window", 0)
        context.sendBroadcast(intent)
    }

    /**
     * Reboot the device.
     *
     * Requires root permission
     * or hold `android:sharedUserId="android.uid.system"`,
     * `<uses-permission android:name="android.permission.REBOOT" />`
     *
     * @param reason code to pass to the kernel (e.g., "recovery") to
     * request special boot modes, or null.
     */
    fun reboot(reason: String, context: Context) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager

        pm.reboot(reason)
    }

    /**
     * Reboot the device to recovery.
     *
     * Requires root permission.
     */
    fun reboot2Recovery() {
        ShellUtils.execCmd("reboot recovery", true)
    }

    /**
     * Reboot the device to bootloader.
     *
     * Requires root permission.
     */
    fun reboot2Bootloader() {
        ShellUtils.execCmd("reboot bootloader", true)
    }

    @RequiresPermission(allOf = [ACCESS_NETWORK_STATE, ACCESS_WIFI_STATE])
    fun getIpAddress(context: Context): String? {
        val info = (context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        if (info != null && info.isConnected) {
            // 3/4g网络
            if (info.type == ConnectivityManager.TYPE_MOBILE) {
                try {
                    val en = NetworkInterface.getNetworkInterfaces()
                    while (en.hasMoreElements()) {
                        val intf = en.nextElement()
                        val enumIpAddr = intf.inetAddresses
                        while (enumIpAddr.hasMoreElements()) {
                            val inetAddress = enumIpAddr.nextElement()
                            if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                return inetAddress.getHostAddress()
                            }
                        }
                    }
                } catch (e: SocketException) {
                    e.printStackTrace()
                }

            } else if (info.type == ConnectivityManager.TYPE_WIFI) {
                //  wifi网络
                val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
                return intIP2StringIP(wifiInfo.ipAddress)
            } else if (info.type == ConnectivityManager.TYPE_ETHERNET) {
                // 有限网络
                return getLocalIp()
            }
        }
        return null
    }

    private fun intIP2StringIP(ip: Int): String {
        return (ip and 0xFF).toString() + "." +
                (ip shr 8 and 0xFF) + "." +
                (ip shr 16 and 0xFF) + "." +
                (ip shr 24 and 0xFF)
    }


    // 获取有限网IP
    private fun getLocalIp(): String {
        try {
            val en = NetworkInterface
                    .getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf
                        .inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ex: SocketException) {

        }

        return "0.0.0.0"

    }

    /**
     * 获取电量信息
     */
    fun battery(context: Context): String {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val intent = context.registerReceiver(null, filter)
        val level = intent!!.getIntExtra("level", -1)
        val scale = intent.getIntExtra("scale", -1)
        return if (scale == -1) {
            "--"
        } else {
            String.format(Locale.US, "%d %%", level * 100 / scale)
        }
    }

    /**
     * 获取磁盘信息
     */
    fun disk(): String {
        val info = getSdCardMemory()
        val total = info[0]
        val avail = info[1]
        return if (total <= 0) {
            "--"
        } else {
            val ratio = (avail * 100 / total).toFloat()
            String.format(Locale.US, "%.01f%% [%s]", ratio, getSizeWithUnit(total))
        }
    }

    /**
     * 获取内存信息
     */
    private fun ram(context: Context): String {
        val total = getTotalMemory()
        val avail = getAvailableMemory(context)
        return if (total <= 0) {
            "--"
        } else {
            val ratio = (avail * 100 / total).toFloat()
            String.format(Locale.US, "%.01f%% [%s]", ratio, getSizeWithUnit(total))
        }
    }


    private fun getSdCardMemory(): LongArray {
        val sdCardInfo = LongArray(2)
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val sdcardDir = Environment.getExternalStorageDirectory()
            val sf = StatFs(sdcardDir.path)
            if (Build.VERSION.SDK_INT >= 18) {
                val bSize = sf.blockSizeLong
                val bCount = sf.blockCountLong
                val availBlocks = sf.availableBlocksLong
                sdCardInfo[0] = bSize * bCount
                sdCardInfo[1] = bSize * availBlocks
            } else {
                val bSize = sf.blockSize.toLong()
                val bCount = sf.blockCount.toLong()
                val availBlocks = sf.availableBlocks.toLong()
                sdCardInfo[0] = bSize * bCount
                sdCardInfo[1] = bSize * availBlocks
            }
        }
        return sdCardInfo
    }

    private fun getSizeWithUnit(size: Long): String {
        return when (size) {
            in 1048576..1073741824 -> {
                val i = (size / 1048576).toFloat()
                String.format(Locale.US, "%.02f MB", i)
            }
            else -> {
                if (size >= 1073741824) {
                    val i = (size / 1073741824).toFloat()
                    String.format(Locale.US, "%.02f GB", i)
                } else {
                    val i = (size / 1024).toFloat()
                    String.format(Locale.US, "%.02f KB", i)
                }
            }
        }
    }

    /**
     * 获取可使用的内存
     */
    fun getAvailableMemory(context: Context): Long {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        return mi.availMem
    }

    /**
     * 获取设备总内存
     */
    @Synchronized
    fun getTotalMemory(): Long {
        if (mTotalMemory == -1L) {
            var total = 0L
            var str: String?
            try {
                str = filterStringFromFile(File("/proc/meminfo"), "MemTotal")
                if (!TextUtils.isEmpty(str)) {
                    str = str!!.toUpperCase(Locale.US)
                    if (str.endsWith("KB")) {
                        total = getSize(str, "KB", 1024)
                    } else if (str.endsWith("MB")) {
                        total = getSize(str, "MB", 1048576)
                    } else if (str.endsWith("GB")) {
                        total = getSize(str, "GB", 1073741824)
                    } else {
                        total = -1
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            mTotalMemory = total
        }
        return mTotalMemory
    }

    private fun filterStringFromFile(file: File, filter: String): String? {
        var str: String? = null
        if (file.exists()) {
            var br: BufferedReader? = null
            try {
                br = BufferedReader(FileReader(file), 1024)
                var line = br.readLine()
                while (line != null) {
                    val pattern = Pattern.compile("\\s*:\\s*")
                    val ret = pattern.split(line, 2)
                    if (ret != null && ret.size > 1 && ret[0] == filter) {
                        str = ret[1]
                        break
                    }
                    br.readLine()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    br!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return str
    }

    private fun getSize(size: String, uint: String, factor: Int): Long {
        return java.lang.Long.parseLong(size.split(uint.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim { it <= ' ' }) * factor
    }
}