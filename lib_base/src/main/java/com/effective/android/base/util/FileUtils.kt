package com.effective.android.base.util

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.res.AssetManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.annotation.StringDef

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.UnsupportedEncodingException
import java.io.Writer
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.util.Enumeration
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import kotlin.experimental.and


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
object FileUtils {

    private const val SIZE_TYPE_B = 1
    private const val SIZE_TYPE_KB = 2
    private const val SIZE_TYPE_MB = 3
    private const val SIZE_TYPE_GB = 4
    private const val JPEG_HEADER = "FFD8FF"
    private const val PNG_HEADER = "89504E47"
    private const val GIF_HEADER = "47494638"
    private const val BMP_HEADER = "424D"
    private val TAG = FileUtils::class.java.simpleName

    @StringDef(JPEG_HEADER, PNG_HEADER, GIF_HEADER, BMP_HEADER)
    annotation class TypeHeader

    //获取文件头信息
    fun getFileHeader(filePath: String): String? {
        var `is`: FileInputStream? = null
        var value: String? = null
        try {
            `is` = FileInputStream(filePath)
            val b = ByteArray(4)
            `is`.read(b, 0, b.size)
            value = bytesToHexString(b)
        } catch (e: Exception) {
        } finally {
            if (null != `is`) {
                try {
                    `is`.close()
                } catch (e: Exception) {
                }

            }
        }
        return value
    }

    fun getFileHeader(file: File): String? {
        var `is`: FileInputStream? = null
        var value: String? = null
        try {
            `is` = FileInputStream(file)
            val b = ByteArray(4)
            `is`.read(b, 0, b.size)
            value = bytesToHexString(b)
        } catch (e: Exception) {
        } finally {
            if (null != `is`) {
                try {
                    `is`.close()
                } catch (e: Exception) {
                }

            }
        }
        return value
    }

    fun isSomeTypeByFilePath(path: String, @TypeHeader typeHeader: String): Boolean {
        try {
            if (TextUtils.isEmpty(path) || TextUtils.isEmpty(typeHeader)) {
                return false
            }
            val fileHeader = getFileHeader(path)
            return if (TextUtils.isEmpty(fileHeader)) {
                false
            } else fileHeader!!.startsWith(typeHeader)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun isSomeTypeByFileHead(fileHeader: String, @TypeHeader typeHeader: String): Boolean {
        return if (TextUtils.isEmpty(fileHeader) || TextUtils.isEmpty(typeHeader)) {
            false
        } else fileHeader.startsWith(typeHeader)
    }

    private fun bytesToHexString(src: ByteArray?): String? {
        try {
            val builder = StringBuilder()
            if (src == null || src.size <= 0) {
                return null
            }
            var hv: String
            for (i in src.indices) {
                hv = Integer.toHexString((src[i] and 0xFF.toByte()).toInt()).toUpperCase()
                if (hv.length < 2) {
                    builder.append(0)
                }
                builder.append(hv)
            }
            return builder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

    }

    val isSDCardExist: Boolean
        get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    // 取得SD卡文件路径
    // 获取单个数据块的大小(Byte)
    // 空闲的数据块的数量
    // 返回SD卡空闲大小
    // return freeBlocks * blockSize; //单位Byte
    // return (freeBlocks * blockSize)/1024; //单位KB
    // 单位MB
    val freeSize: Long
        get() {
            val path = Environment.getExternalStorageDirectory()
            val sf = StatFs(path.path)
            val blockSize = sf.blockSize.toLong()
            val freeBlocks = sf.availableBlocks.toLong()
            return freeBlocks * blockSize / 1024 / 1024
        }

    // 取得SD卡文件路径
    // 获取单个数据块的大小(Byte)
    // 获取所有数据块数
    // 返回SD卡大小
    // return allBlocks * blockSize; //单位Byte
    // return (allBlocks * blockSize)/1024; //单位KB
    // 单位MB
    val totalSize: Long
        get() {
            val path = Environment.getExternalStorageDirectory()
            val sf = StatFs(path.path)
            val blockSize = sf.blockSize.toLong()
            val allBlocks = sf.blockCount.toLong()
            return allBlocks * blockSize / 1024 / 1024
        }

    //崩溃log目录
    val crashLogDir: String
        get() = Environment.getExternalStorageDirectory().toString() + "/CrashLog"

    //下载插件安装包目录
    val downloadDir: String
        get() = Environment.getExternalStorageDirectory().toString() + "/Download"


    //图片缓存目录
    fun getImageCacheDir(context: Context): String {
        return context.cacheDir.toString() + "/image"
    }

    //递归删除文件
    fun delete(path: String) {
        val file = File(path)
        delete(file)
    }

    //递归删除文件
    fun delete(file: File?) {
        if (file == null || !file.exists()) {
            return
        }
        if (file.isFile) {
            file.delete()
            return
        }

        if (file.isDirectory) {
            val childFiles = file.listFiles()
            if (childFiles == null || childFiles.size == 0) {
                file.delete()
                return
            }

            for (childFile in childFiles) {
                delete(childFile)
            }
            file.delete()
        }
    }

    @Throws(IOException::class)
    fun saveBitmapToFile(bm: Bitmap, fileName: String): String {
        val path = FileUtils.downloadDir + "/picture/"
        val dirFile = File(path)
        if (!dirFile.exists()) {
            dirFile.mkdirs()
        }
        val myCaptureFile = File(path + fileName)
        val bos = BufferedOutputStream(FileOutputStream(myCaptureFile))
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos)
        bos.flush()
        bos.close()
        return myCaptureFile.toString()
    }

    //获取文件夹大小,返回大小为Bit
    fun getFolderSizeBit(file: File): Long {
        var size: Long = 0
        val fileList = file.listFiles()
        for (aFileList in fileList) {
            if (aFileList.isDirectory) {
                size = size + getFolderSizeBit(aFileList)
            } else {
                size = size + aFileList.length()
            }
        }
        return size
    }

    /**
     * 获取文件大小，赞只考虑K和M的情况
     *
     * @param filePath
     * @return
     */
    fun getFileSize(filePath: String): String {
        val file = File(filePath)
        var blockSize: Long = 0
        try {
            blockSize = getFileSize(file)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("获取文件大小", "获取失败!")
        }

        val df = DecimalFormat("#.0")
        var fileSizeLong: Double
        fileSizeLong = java.lang.Double.valueOf(df.format(blockSize.toDouble() / 1024))
        if (fileSizeLong < 1024) {
            return StringBuilder().append(fileSizeLong).append("K").toString()
        } else {
            fileSizeLong = java.lang.Double.valueOf(df.format(fileSizeLong / 1024f))
            return StringBuilder().append(fileSizeLong).append("M").toString()
        }
    }

    /**
     * 获取指定类型的文件夹大小
     *
     * @param filePath
     * @param sizeType
     * @return
     */
    fun getFileSizeForType(filePath: String, sizeType: Int): Double {
        val file = File(filePath)
        var blockSize: Long = 0
        try {
            blockSize = getFileSize(file)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("获取文件大小", "获取失败!")
        }

        return FormatFileSize(blockSize, sizeType)

    }

    /**
     * 获取文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getFileSize(file: File): Long {
        var size: Long = 0
        if (file.exists()) {
            var fis: FileInputStream? = null
            fis = FileInputStream(file)
            size = fis.available().toLong()
        } else {
            file.createNewFile()
            Log.e("获取文件大小", "文件不存在!")
        }
        return size
    }

    /**
     * 文件大小格式化
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private fun FormatFileSize(fileS: Long, sizeType: Int): Double {
        val df = DecimalFormat("#.00")
        var fileSizeLong = 0.0
        when (sizeType) {
            SIZE_TYPE_B -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble()))
            SIZE_TYPE_KB -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1024))
            SIZE_TYPE_MB -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1048576))
            SIZE_TYPE_GB -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1073741824))
            else -> {
            }
        }
        return fileSizeLong
    }


    //获取文件夹大小,返回大小为KB
    fun getFolderSizeKB(file: File): Long {
        return getFolderSizeBit(file) / 1024
    }

    //获取文件夹大小,返回大小为MB
    fun getFolderSizeMB(file: File): Long {
        return getFolderSizeBit(file) / 1048576
    }


    //通过uri获取path
    @SuppressLint("NewApi")
    fun getPath(context: Context?, uri: Uri?): String? {

        if (context == null || uri == null) {
            return null
        }

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return (Environment.getExternalStorageDirectory().toString() + "/"
                            + split[1])
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id))

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection,
                        selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)

        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    fun getDataColumn(context: Context, uri: Uri?,
                      selection: String?, selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!, projection,
                    selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }


    /**
     * 解压缩功能. 将zipFile文件解压到folderPath目录下.
     *
     * @throws Exception
     */
    @Throws(IOException::class)
    fun upZipFile(zipFile: File, folderPath: String): Int {
        // public static void upZipFile() throws Exception{
        val zfile = ZipFile(zipFile)
        val zList = zfile.entries()
        var ze: ZipEntry
        val buf = ByteArray(1024)
        while (zList.hasMoreElements()) {
            ze = zList.nextElement() as ZipEntry
            if (ze.isDirectory) {
                Log.i("upZipFile", "ze.getName() = " + ze.name)
                var dirstr = folderPath + ze.name
                // dirstr.trim();
                dirstr = String(dirstr.toByteArray(charset("8859_1")), charset("GB2312"))
                Log.i("upZipFile", "str = $dirstr")
                val f = File(dirstr)
                f.mkdir()
                continue
            }
            Log.i("upZipFile", "ze.getName() = " + ze.name)
            val os = BufferedOutputStream(FileOutputStream(getRealFileName(folderPath, ze.name)))
            val content = BufferedInputStream(zfile.getInputStream(ze))
            var readLen = content.read(buf,0,1024)
            while (readLen != -1) {
                os.write(buf, 0, readLen)
                readLen = content.read(buf,0,1024)
            }
            content.close()
            os.close()
        }
        zfile.close()
        Log.i("upZipFile", "unzip finished")
        return 0
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir     指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    fun getRealFileName(baseDir: String, absFileName: String): File {
        val dirs = absFileName.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var ret = File(baseDir)
        var substr: String
        if (dirs.size > 1) {
            for (i in 0 until dirs.size - 1) {
                substr = dirs[i]
                try {
                    // substr.trim();
                    substr = String(substr.toByteArray(charset("8859_1")), charset("GB2312"))

                } catch (e: UnsupportedEncodingException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

                ret = File(ret, substr)

            }
            Log.i("upZipFile", "1ret = $ret")
            if (!ret.exists())
                ret.mkdirs()
            substr = dirs[dirs.size - 1]
            try {
                // substr.trim();
                substr = String(substr.toByteArray(charset("8859_1")), charset("GB2312"))
                Log.i("upZipFile", "substr = $substr")
            } catch (e: UnsupportedEncodingException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            ret = File(ret, substr)
            Log.i("upZipFile", "2ret = $ret")
            return ret
        }
        return ret
    }

    /**
     * 复制文件
     *
     * @param srcFile  The file to copy
     * @param destFile The file to save
     * @return Return false if fail
     */
    fun copyFile(srcFile: File, destFile: File): Boolean {
        var result = false
        try {
            val `in` = FileInputStream(srcFile)
            try {
                result = copyToFile(`in`, destFile)
            } finally {
                `in`.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            result = false
        }

        return result
    }

    /**
     * 从流中复制一个文件
     *
     * @param inputStream The inputstream to write the #destFile.
     * @param destFile    The file to save.
     * @return Return false if failed
     */
    fun copyToFile(inputStream: InputStream, destFile: File): Boolean {
        try {
            if (destFile.exists()) {
                destFile.delete()
            }
            val out = FileOutputStream(destFile)
            try {
                val buffer = ByteArray(4096)
                var bytesRead = inputStream.read(buffer)
                while (bytesRead >= 0) {
                    out.write(buffer, 0, bytesRead)
                    bytesRead = inputStream.read(buffer)
                }
            } finally {
                out.flush()
                try {
                    out.fd.sync()
                } catch (e: IOException) {
                }

                out.close()
            }
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

    }

    /**
     * string to file
     *
     * @param str
     * @param filename
     */
    fun writeToFile(str: String, filename: String): String? {
        try {
            val file = File(filename)
            if (!file.exists()) {
                if (!file.parentFile.exists()) {
                    file.parentFile.mkdirs()
                }
                file.createNewFile()
            }
            val fWriter = OutputStreamWriter(FileOutputStream(file), "utf-8")
            fWriter.write(str)
            fWriter.flush()
            fWriter.close()
            return filename
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getContent(assetManager: AssetManager, fileName: String?): String? {
        var content: String? = null
        if (fileName == null) {
            return content
        }
        try {
            val contentStream = ByteArrayOutputStream()
            val inputStream = assetManager.open(fileName)
            try {
                val buffer = ByteArray(4096)
                var bytesRead = inputStream.read(buffer)
                while (bytesRead  >= 0) {
                    contentStream.write(buffer, 0, bytesRead)
                    bytesRead = inputStream.read(buffer)
                }
                content = String(contentStream.toByteArray())
            } finally {
                inputStream.close()
                contentStream.close()
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }

        return content
    }
}
