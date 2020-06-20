package com.nice.baselibrary.base.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import okhttp3.ResponseBody
import java.io.*
import java.nio.channels.FileChannel
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * 文件工具方法集
 * @author JPlus
 * @date 2019/2/22.
 */


/**
 * 文件流写入
 * @param stream
 * @param append
 * @return
 */
fun File.writeFile(stream: InputStream, append: Boolean): Boolean {
    var outputStream: OutputStream? = null
    LogUtils.d("writeFile:$absoluteFile")
    try {
        outputStream = FileOutputStream(this, append)
        val data = ByteArray(1024)
        var length = 0
        while ({ length = stream.read(data); length }() != -1) {
            outputStream.write(data, 0, length)
        }
        outputStream.flush()
        return true
    } catch (e: FileNotFoundException) {
        throw RuntimeException("FileNotFoundException occurred. ", e)
    } catch (e: IOException) {
        throw RuntimeException("IOException occurred. ", e)
    } finally {
        try {
            outputStream?.close()
            stream.close()
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        }
    }
}

/**
 * 文件字符串写入
 * @param content
 * @param append
 * @return
 */
fun File.writeFile(content: String, append: Boolean): Boolean {
    var writer: BufferedWriter? = null
    try {
        LogUtils.d("writeFile:" + absoluteFile)
        //FileWriter 文件不存在则会创建
        writer = BufferedWriter(FileWriter(this, append))
        writer.write(content)
        return true
    } catch (e: FileNotFoundException) {
        throw RuntimeException("FileNotFoundException occurred. ", e)
    } catch (e: IOException) {
        throw RuntimeException("IOException occurred. ", e)
    } finally {
        try {
            writer?.close()
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        }
    }
}


/**
 * 文件分行读取
 * @param charsetName
 * @return
 */
fun File.readFile2List(charsetName: String = "UTF-8"): ArrayList<String> {
    val fileContent = ArrayList<String>()
    var reader: BufferedReader? = null
    try {
        val input = InputStreamReader(FileInputStream(this), charsetName)
        reader = BufferedReader(input)
        var line: String? = null
        while ({ line = reader.readLine();line }() != null) {
            fileContent.add(line!!)
        }
        return fileContent
    } catch (e: FileNotFoundException) {
        throw RuntimeException("FileNotFoundException occurred. ", e)
    } catch (e: IOException) {
        throw RuntimeException("IOException occurred. ", e)
    } finally {
        try {
            reader?.close()
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        }

    }
}

/**
 * 读取输入流为byte数组
 * @return byte数组
 */
fun InputStream.inputStream2ByteArray(): ByteArray? {
    val out = ByteArrayOutputStream()
    try {
        val b = ByteArray(2048)
        var readBytes = 0
        while (read(b).also { readBytes = it } > 0) {
            out.write(b, 0, readBytes)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    } finally {
        try {
            close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return out.toByteArray()
}

/**
 * 输入流读取
 * @param charsetName
 * @return
 */
fun InputStream.readFile2List(charsetName: String = "UTF-8"): ArrayList<String> {
    val fileContent = ArrayList<String>()
    var reader: BufferedReader? = null
    try {
        val input = InputStreamReader(this, charsetName)
        reader = BufferedReader(input)
        var line: String? = null
        while ({ line = reader.readLine();line }() != null) {
            fileContent.add(line!!)

        }
        return fileContent
    } catch (e: FileNotFoundException) {
        throw RuntimeException("FileNotFoundException occurred. ", e)
    } catch (e: IOException) {
        throw RuntimeException("IOException occurred. ", e)
    } finally {
        try {
            reader?.close()
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        }
    }
}

/**
 * 文件字符串内容读取
 * @param charsetName
 * @return
 */
fun File.readFile2String( charsetName: String = "UTF-8"): String {
    var input: FileInputStream? = null
    try {
        input = FileInputStream(this)
        val buffer = ByteArray(input.available())
        input.read(buffer)
        return String(buffer, charset(charsetName))
    } catch (e: FileNotFoundException) {
        throw RuntimeException("FileNotFoundException occurred. ", e)
    } catch (e: IOException) {
        throw RuntimeException("IOException occurred. ", e)
    } finally {
        try {
            input?.close()
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        }
    }
}

/**
 * 文件读取为输入流
 * @return
 */
fun File.readFile2InputStream(): InputStream {
    var input: FileInputStream? = null
    try {
        input = FileInputStream(this)
        return input
    } catch (e: FileNotFoundException) {
        throw RuntimeException("FileNotFoundException occurred. ", e)
    } catch (e: IOException) {
        throw RuntimeException("IOException occurred. ", e)
    } finally {
        try {
            input?.close()
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        }
    }
}

/**
 * 文件读取
 * @param charsetName
 * @return
 */
fun InputStream.readFile2String(charsetName: String = "UTF-8"): String {
    val buffer = ByteArray(available())
    read(buffer)
    return String(buffer, charset(charsetName))
}


/**
 * 私有文件写入
 * @param filePath
 * @param content
 */
fun Context.writePrivateFile(filePath: String, content: String) {
    openFileOutput(filePath, Context.MODE_PRIVATE).let{
        it.write(content.toByteArray())
        it.close()
    }
}
/**
 * 文件流写入私有目录
 * @param name
 * @param stream
 * @return
 */
fun Context.writePrivateFile(name:String, stream: InputStream): Boolean {
    var outputStream: OutputStream? = null
    try {
        outputStream = openFileOutput(name, Context.MODE_PRIVATE)
        val data = ByteArray(1024)
        var length = 0
        while ({ length = stream.read(data); length }() != -1) {
            outputStream.write(data, 0, length)
        }
        outputStream.flush()
        return true
    } catch (e: FileNotFoundException) {
        throw RuntimeException("FileNotFoundException occurred. ", e)
    } catch (e: IOException) {
        throw RuntimeException("IOException occurred. ", e)
    } finally {
        try {
            outputStream?.close()
            stream.close()
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        }
    }
}

/**
 * 生成私有文件夹
 * @param dir
 * return
 */
fun Context.writePrivateDir(dir: String): File {
    return getDir(dir, Context.MODE_PRIVATE)
}

/**
 * 获取文件夹名
 * @param filePath
 * @return String
 */
fun getFolderName(filePath: String): String {
    val filePosition = filePath.lastIndexOf(File.separator)
    return if (filePosition == -1) "" else filePath.substring(0, filePosition)
}

/**
 * 生成文件
 * @return 是否生成文件
 */
fun File.createFile(): Boolean {
    if (exists()) return true
    if (isDirectory) File(getFolderName(absolutePath)).createDir() else return false
    return try {
        createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}

/**
 * 生成文件夹
 * @return 是否生成文件夹
 */
fun File.createDir(): Boolean {
    return if (exists() && isDirectory) true else if(isFile) throw IOException("File is not dir.") else mkdirs()
}


/**
 * 获取文件MD5值
 * @param filePath
 * @return MD5值字符串
 */
fun getFileMD5(filePath: String): String {
    return File(filePath).getFileMD5().encryptionMD5()
}

/**
 * 获取文件MD5值byte数组
 * @return byte数组
 */
fun File.getFileMD5(): ByteArray {
    var digest: DigestInputStream? = null
    try {
        val fileInput = FileInputStream(this)
        var md = MessageDigest.getInstance("MD5")
        digest = DigestInputStream(fileInput, md)
        val bytes = ByteArray(1024 * 256)
        while (true) {
            if ((digest.read(bytes) <= 0)) {
                break
            }
        }
        md = digest.messageDigest
        return md.digest()
    } catch (e: (NoSuchAlgorithmException)) {

    } catch (e: IOException) {

    } catch (e: FileNotFoundException) {

    } finally {
        try {
            digest?.close()
        } catch (e: IOException) {

        }
    }
    return byteArrayOf()
}

/**
 * 获取 文件/文件夹 的大小
 * @return 大小
 */
fun File.getFolderSize(): Long {
    var size = 0L
    return if (isFile) {
        length()
    } else {
        this.listFiles()?.forEach {
            size += it.getFolderSize()
        }
        size
    }
}

/**
 * 获取文件夹下文件列表
 * @return 文件列表
 */
fun File.getDirFiles(): MutableList<File>? {
    return try {
        if (isDirectory) listFiles()?.toMutableList() else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

}

/**
 * 断点续传文件写入
 * @param responseBody 响应内容
 * @param read 已下载大小
 * @param count 文件总大小
 */
@Throws(IOException::class)
fun File.writeRandomAccessFile(responseBody: ResponseBody, read: Long, count: Long) {
    LogUtils.d("writeRandomAccessFile: contentLength:${responseBody.contentLength()} - file_path:${absolutePath}\nread:$read, count:$count")
    if (parentFile?.exists()==true)
        parentFile?.mkdirs()
    if(!this.exists()) createNewFile()

    val allLength: Long = if (count == 0L) {
        responseBody.contentLength()
    } else {
        count
    }
    RandomAccessFile(this, "rwd").let {
        val mappedBuffer = it.channel.map(FileChannel.MapMode.READ_WRITE, read, allLength - read)
        val buffer = ByteArray(1024 * 8)
        var len = 0
        var record = 0
        while ({ len = responseBody.byteStream().read(buffer);len }() != -1) {
            mappedBuffer.put(buffer, 0, len)
            record += len
        }
        responseBody.byteStream().close()
        it.channel.close()
        it.close()
    }
}