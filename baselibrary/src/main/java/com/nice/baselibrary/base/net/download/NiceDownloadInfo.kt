package com.nice.baselibrary.base.net.download

/**
 * 下载实体类
 * @author JPlus
 * @date 2019/3/1.
 */
data class NiceDownloadInfo(var id:Int,
                            var name:String,
                            var url:String,
                            var path:String,
                            var date:String,
                            var read:Long,
                            var count:Long,
                            var status:String)
