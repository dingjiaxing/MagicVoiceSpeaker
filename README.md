Speaktoit Mock
==============

开发环境
-------
- Android Studio 1.0+
- Gradle 2.2+
- Android SDK 22

工程
----
项目的目标是要实现类似语音助手的功能，能够对用户不同的输入进行不同的反馈，例如，发短信，打电话，按钮确认，浏览器搜搜等等。需要同时支持语言输入和文本输入。

工程中使用了以下的第三方库

- pingyin4j-2.5.0 用于汉字和拼音之间的转换
- litepal-1.2.1 用于本地数据库的管理

### 目录结构
- app App的主体工程
  - src 源代码
    - androidTest 集成测试
    - main 
      - adapter 页面的适配器
      - application 整个应用相关的功能     
      - api 远程接口
      - fragment 页面模块
      - model 模型层
      - util 工具类
 - gradle Gradle Wrapper


构建
----
构建项目需要 gradle 2.2+ 以上，也可以使用工程中自带的 gradle wrapper 进行构建。另外在构建之前之前需要将 Android SDK 目录设置到 `ANDROID_HOME` 环境变量。

```
bash export ANDROID_HOME=/opt/tools/android-sdk
```

满足上述条件即可进行构建，构建的过程十分简单：

- 在app目录执行 ./gradlew clean assemble 将会构建生成apk文件到 /build/outputs/apk 目录下
- 在根目录执行 ./gradlew clean install 将会构建apk并将其安装到连接的Android设备或模拟器

运行
---
apk的目标sdkVersion是22，最低支持16。所以运行App的Android设备或模拟器至少需要Android版本4.1。