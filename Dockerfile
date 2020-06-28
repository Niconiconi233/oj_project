#基础镜像，如果本地没有，会从远程仓库拉取。
FROM openjdk:8-jdk

#镜像的制作人
MAINTAINER niconiconiyw

#工作目录
WORKDIR /app/

#创建目录
RUN mkdir /zip_temp && mkdir /test_case && mkdir /webconf


#拷贝本地文件到镜像中
COPY ojproject-0.0.1-SNAPSHOT.jar app.jar

#指定容器启动时要执行的命令，但如果存在CMD指令，CMD中的参数会被附加到ENTRYPOINT指令的后面
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]