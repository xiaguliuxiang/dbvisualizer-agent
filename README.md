# DbVisualizer Pro Agent

#### Support (more version need your feedback):
DbVisualizer 10.0.25, 20191209 update

## 使用说明

### 优势
* 提供基于java的命令行 keygen，更方便在终端环境使用。
* 开源项目，你知道破解时都做了什么。

### 直接下载
* 直接下载本项目[release](https://github.com/xiaguliuxiang/dbvisualizer-agent/releases)包。

### 自行编译
* Clone本项目源码，pom.xml同级目录执行`mvn package`后即可进行编译。
* 使用`target`目录产出的`dbvisualizer-agent-1.0-SNAPSHOT-jar-with-dependencies.jar`，而非`dbvisualizer-agent-1.0-SNAPSHOT.jar`！
* *如果你不知道我在说什么，最好还是直接下载我编译好的包。*

### 使用帮助
* 如果你已经获得`dbvisualizer-agent.jar`，可以试着执行`java -jar dbvisualizer-agent.jar -h`看看输出的帮助。

### 配置Agent
1. 将`dbvisualizer-agent.jar`放在一个你不会随便删除的位置。
2. 设置环境变量`VM options`（这其实是Java的环境变量，用来指定其启动java程序时附带的参数），把`-javaagent`参数附带上。具体可以这么做：
   * 你可以把：`-javaagent:/path/dbvisualizer-agent.jar`这样的命令直接放到`dbvis.vmoptions`这样的文件内。
   *   Windows: -javaagent:C:\Users\xiaguliuxiang\dbvisualizer-agent.jar
          dbvis.vmoptions: ${DbVisualizer_HOME}/dbvis.vmoptions
       Mac OS: -javaagent:/Users/xiaguliuxiang/dbvisualizer-agent.jar
          dbvis.vmoptions: ${DbVisualizer_HOME}/Contents/vmoptions.txt
       Linux: -javaagent:/home/xiaguliuxiang/dbvisualizer-agent.jar
          dbvis.vmoptions: ${DbVisualizer_HOME}/dbvis.vmoptions
   * 或者你所知的其他修改环境变量的方法。
   * 总之你想办法把`-javaagent`参数附带到要启动的java进程上。
3. 配置完成请重启你的`DbVisualizer`客户端。
4. 如果你想验证是否配置成功，可以这么做：
   * 执行类似命令：`ps aux|grep java` 找到对应的进程看看`-javaagent`参数是否正确附上。
   * 在`Linux`用户目录类似：`${HOME}/.dbvis/logs`软件运行日志内应该能找到：`-javaagent:/path/dbvisualizer-agent.jar`的输出字样。
   * 在`Windows`用户目录类似：`%USERPROFILE%/.dbvis/logs`软件运行日志内应该能找到：`-javaagent:/path/dbvisualizer-agent.jar`的输出字样。

### 使用KeyGen
* 你得确认已经配置好agent，参考上面说明。
* 当你试着执行`java -jar dbvisualizer-agent.jar -h`时应该可以看到输出的KeyGen参数帮助。
* 请仔细看看每个参数的作用。
* 提供了正确的参数运行KeyGen会在终端输出计算好的激活码。
* 将生成的激活码复制出来去激活你的DbVisualizer。
* 举个栗子：`java -jar dbvisualizer-agent.jar -n xiaguliuxiang -o https://github.com/xiaguliuxiang/dbvisualizer-agent`

### 申明
* 本项目只做个人学习研究之用，不得用于商业用途！
* 商业使用请向 [DbVisualizer](https://www.dbvis.com) 购买正版，谢谢合作！
* 本项目使用`GNU General Public License v3.0`开源许可！
* 不允许说我代码写的糟糕。

### 交流
* 给本项目发issue。
* 欢迎你来一起完善这个项目，请发PR。
* 你可以加入QQ群：`532944625` 和我实时交流。
* 访问网站：[https://github.com/xiaguliuxiang/dbvisualizer-agent](https://github.com/xiaguliuxiang/dbvisualizer-agent) 给我留言。

### 热心网友教程（感谢原作者，侵删！）
* [Google](https://www.google.com)

### 关于
* [侠骨留香](https://github.com/xiaguliuxiang)
* 2019年09月09日
