FROM mdsol/java17-jdk
# 设置作者信息
LABEL maintainer="zemise <373933306@qq.com>"

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
#COPY ./src/docker/run.sh .
ENTRYPOINT ["java", "-jar", "/app.jar"]
# rofix not found
#COPY ./src/docker/run.sh .
#ADD alone/src/docker/run.sh .

#ENTRYPOINT ["bash", "run.sh"]




#   addFile("${jar.baseName}-${jar.version}.jar", "app.jar")