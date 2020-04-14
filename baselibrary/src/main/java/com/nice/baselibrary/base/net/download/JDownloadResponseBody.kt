package com.nice.baselibrary.base.net.download

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.net.SocketException

/**
 * 下载响应
 * @author JPlus
 * @date 2019/2/22.
 */
class JDownloadResponseBody(private val response: ResponseBody?, private val downloadsListener: JDownloadProgressListener) : ResponseBody() {
    private var bufferSource: BufferedSource? = null

    override fun contentLength(): Long {
        return response?.contentLength()?:0
    }

    override fun contentType(): MediaType? {
        return response?.contentType()
    }

    override fun source(): BufferedSource? {
        response?.let{
            bufferSource = bufferSource?:Okio.buffer(getSource(it.source()))
        }
        return bufferSource
    }

    @Throws(SocketException::class)
    private fun getSource(source: Source): Source {
        var totalRead = 0L
        return object : ForwardingSource(source) {
            override fun read(sink: Buffer, byteCount: Long): Long {
                //当前读取字节数
                val bytesRead = super.read(sink, byteCount)
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalRead += if (bytesRead != -1L) bytesRead else 0
                //回调，如果contentLength()不知道长度，会返回-1
                downloadsListener.let {
                    response?.let { response ->
                        it.update(totalRead, response.contentLength(), bytesRead == -1L)
                    }
                }
                return bytesRead
            }
        }
    }
}