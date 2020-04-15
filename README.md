### What is BaseLibrary?

This base dependency library contains some tool classes and some custom views.

### Utils:
***
##### [ActivityCollect](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/utils/ActivityCollect.kt)
- 通过集合的方式 管理所有的Activity（推荐在BaseActivity的onCreate和onDestroy中添加和移除）

##### [AppHelper](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/utils/AppHelper.kt)
- 通过扩展函数实现的App相关方法集

##### [CrashHandler](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/utils/CrashHandler.kt)
- 崩溃日志管理类（单例），支持自定义数量、存放目录和文件名等

##### [DateHelper](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/utils/DateHelper.kt)
- 日期相关方法集

##### [DeCodeUtils](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/utils/DeCodeUtils.kt)
- 编码解码工具类

##### [ExcCommand](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/utils/ExcCommand.kt)
- 命令行工具类

##### [FileHelper](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/utils/FileHelper.kt)
- 文件相关方法集

##### [JPermissionsUtils](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/utils/JPermissionsUtils.kt)
- 动态权限的工具类，需配合assets/permissions.json使用

##### [LogUtils](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/utils/LogUtils.kt)
- 日志工具类，支持自定义数量、存放目录和文件名等

##### [MD5Helper](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/utils/MD5Helper.kt)
- MD5相关方法集

##### [PatchDexUtils](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/utils/PatchDexUtils.kt)
- 热修复工具类，暂时只使用dex热修复

##### [PhotoUtils](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/utils/PhotoUtils.kt)
- 系统相机、相册工具类

##### [StringUtils](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/utils/StringUtils.kt)
- String相关工具类

##### [UriHelper](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/utils/UriHelper.kt)
- Uri解析与转换方法集

### Widget:
***
##### [JAlertDialog](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/widget/dialog/JAlertDialog.kt)
- 继承自JDialogFragment（继承DialogFragment）的可自定义弹出框

##### [jDialog](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/widget/dialog/JDialog.kt)
- 继承自AlertDialog的封装的简易弹出框

##### [BaseCircleProgress](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/widget/BaseCircleProgress.kt)
- 封装的圆形进度条，可选择带下载进度、无进度

##### [CircleLoadingView](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/widget/CircleLoadingView.kt)
- 可自定义的疯转LoadingView，支持单层、内外双层模式

##### [JEditTextView](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/widget/JEditTextView.kt)
- 可一键清空内容的输入框

##### [JTextView](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/widget/JTextView.kt)
- 可扩展的TextView

##### [JTitleBar](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/widget/JTitleBar.kt)
- 可扩展标题栏

##### [LinesLoadingView](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/widget/LinesLoadingView.kt)
- 条形的loading条

### Other:
***
##### [NotDoubleOnClickListener](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/listener/NotDoubleOnClickListener.kt)
- 避免短时间内重复点击的点击监听（继承自View.OnClickListener）

##### [BaseAdapter](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/adapter/BaseAdapter.kt)
- RecyclerView的通用适配器

##### [BaseAdapterWrapper](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/adapter/BaseAdapterWrapper.kt)
- BaseAdapter的装饰器，用来配置headView和footView

##### [JDBHelper](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/db/JDBHelper.kt)
- 数据库操作的抽象基类

##### [JDataSource](https://github.com/nicejiang/baselibrary/blob/master/baselibrary/src/main/java/com/nice/baselibrary/base/db/JDataSource.kt)
- 数据处理基类

