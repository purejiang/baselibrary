package com.nice.baselibrary.base.utils

import android.content.Context
import java.io.*
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * 文件工具类
 * @author JPlus
 * @date 2019/2/22.
 */
class FileUtils {
    companion object {

        /**
         * 文件写入
         * @param file
         * @param stream
         * @param append
         * @return
         */
        fun writeFile(file: File, stream: InputStream, append: Boolean): Boolean {
            var outputStream: OutputStream? = null
            LogUtils.instance.d("writeFile:" + file.absoluteFile)
            try {
                createOrExistsFile(file)
                outputStream = FileOutputStream(file, append)
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
         * 文件写入
         * @param file
         * @param content
         * @param append
         * @return
         */
        fun writeFile(file: File, content: String, append: Boolean): Boolean {
            LogUtils.instance.d("writeFile:" + file.absoluteFile)
            var writer: BufferedWriter? = null
            try {
                createOrExistsFile(file)
                writer = BufferedWriter(FileWriter(file, append))
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
         * 文件读取
         * @param file
         * @param charsetName
         * @return
         */
        fun readFile2List(file: File, charsetName: String = "UTF-8"): ArrayList<String> {
            val fileContent = ArrayList<String>()
            var reader: BufferedReader? = null
            try {
                val input = InputStreamReader(FileInputStream(file), charsetName)
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
         * 文件读取
         * @param inputs
         * @param charsetName
         * @return
         */
        fun readFile2List(inputs: InputStream, charsetName: String = "UTF-8"): ArrayList<String> {
            val fileContent = ArrayList<String>()
            var reader: BufferedReader? = null
            try {
                val input = InputStreamReader(inputs, charsetName)
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
         * 文件读取
         * @param file
         * @param charsetName
         * @return
         */
        fun readFile2String(file: File, charsetName: String = "UTF-8"): String {
            var input: FileInputStream? = null
            try {
                input = FileInputStream(file)
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
         * 文件读取
         * @param file
         * @return
         */
        fun readFile2InputStream(file: File): InputStream {
            var input: FileInputStream? = null
            try {
                input = FileInputStream(file)
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
         * @param input
         * @param charsetName
         * @return
         */
        fun readFile2String(input: InputStream, charsetName: String = "UTF-8"): String {
            val buffer = ByteArray(input.available())
            input.read(buffer)
            return String(buffer, charset(charsetName))
        }


        /**
         * 私有文件写入
         * @param filePath
         * @param context
         * @param content
         */
        fun writePrivateFile(filePath: String, context: Context, content: String) {
            val output = context.openFileOutput(filePath, Context.MODE_PRIVATE)
            output.write(content.toByteArray())
            output.close()
        }

        /**
         * 生成私有文件夹
         * @param dir
         * @param context
         * return
         */
        fun writePrivateDir(dir: String, context: Context): File {
            return context.getDir(dir, Context.MODE_PRIVATE)
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
         * @param file
         * @return 是否生成文件
         */
        fun createOrExistsFile(file: File): Boolean {
            if (file.exists()) return file.isFile
            if (!createOrExistsDir(file.parentFile)) return false
            return try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }

        /**
         * 生成文件夹
         * @param folder
         * @return
         */
        fun createOrExistsDir(folder: File): Boolean {
            return if (folder.exists() && folder.isDirectory) true else folder.mkdirs()
        }

        /**
         * 删除文件或文件夹
         * @param file
         * @return
         */
        fun delFileOrDir(file: File): Boolean {
            var tag = true

            if (file.isFile) {
                tag = tag && file.delete()
            } else {
                for (f: File in file.listFiles()) {
                    tag = tag && delFileOrDir(f)
                }
            }
            return tag
        }

        /**
         * 获取文件MD5值
         * @param filePath
         * @return String
         */
        fun getFileMD5(filePath: String): String {
            return MD5Utils.encryptionMD5(getFileMD5(File(filePath)))
        }

        /**
         * 获取文件MD5值
         * @param file
         * @return byte数组
         */
        fun getFileMD5(file: File): ByteArray {
            var digest: DigestInputStream? = null
            try {
                val fileInput = FileInputStream(file)
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
         * 获取文件夹下文件列表
         * @param file
         * @return
         */
        fun getDirFiles(file: File): MutableList<File>? {
            return try {
                if (file.isDirectory) file.listFiles().toMutableList() else null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

        }
    }
}